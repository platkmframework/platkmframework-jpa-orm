/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.jpa.orm.entitymanager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.NotImplementedException;
import org.platkmframework.jpa.base.ParameterImpl;
import org.platkmframework.jpa.base.ParameterInfo;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;
import org.platkmframework.jpa.orm.mapping.DbMappingUtil;
import org.platkmframework.util.error.InvocationException;

/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class QueryImpl implements Query{
	
	private List<ParameterInfo<?>> parameters;
	private int maxResults = 0;
	private int firstResult = 0;
	private FlushModeType flushMode;
	private LockModeType lockMode;
	private PreparedStatement preparedStatement;
	
	private ResultSetMetaData resultSetMetaData = null;
	
	private DatabaseMapperImpl databaseMapper;
	
	Class<?> returnClass;
	 
	protected QueryImpl(PreparedStatement preparedStatement, Class<?> returnClass, Map map, DatabaseMapperImpl databaseMapper) {
		super();
		this.preparedStatement = preparedStatement;
		this.returnClass = returnClass;
		this.databaseMapper = databaseMapper;
	}

	@Override
	public List getResultList() { 
		try {
			  
			ResultSet rs = _executePrepareStatement();  
			
			if(returnClass != null){
				return _getListResultClass(rs);
			}else {
				return _getListNoResultClass(rs);
			}  
		} catch (SQLException | InvocationException | DatabaseValidationException e) {
			throw new PlatkmJpaException(e);
		} 
	}
	
	private List<?> _getListResultClass(ResultSet rs ) throws SQLException, InvocationException, DatabaseValidationException {
		 
		List result = new ArrayList<>();
		while(rs.next()) { 
			if(returnClass.isAssignableFrom(Map.class)) {
				
				String columns = DbMappingUtil.getMetaDataColumnsName(rs.getMetaData());  
				String[] arrayColumn = columns.split(",");  
				result.add(DbMappingUtil.getCustomMap(rs, arrayColumn, databaseMapper)); 
				
			}else if(returnClass.isAssignableFrom(Properties.class)) {
				
				String columns = DbMappingUtil.getMetaDataColumnsName(rs.getMetaData());  
				String[] arrayColumn = columns.split(",");  
				result.add(DbMappingUtil.getCustomProperties(rs, arrayColumn, databaseMapper)); 
				
			}else {
				result.add(DbMappingUtil.getCustomEntity(rs, returnClass, databaseMapper));
			} 
		}
		return result; 
	}
	
	

	@Override
	public Object getSingleResult() {
		
		try {
			  
			ResultSet rs = _executePrepareStatement();  
			if(returnClass != null){
				List<?> list =  _getListResultClass(rs);
				if(list == null || list.isEmpty()) return null;
				return list.get(0);
			}else {
				List<Object[]> list = _getListNoResultClass(rs);
				if(list == null || list.isEmpty()) return null;
				return list.get(0);
			}  
		} catch (SQLException | InvocationException | DatabaseValidationException e) {
			throw new PersistenceException(e);
		} 
	}

	@Override
	public int executeUpdate() { 
		try {
			
			for (ParameterInfo<?> parameterInfo : getparameterList()) { 
				preparedStatement.setObject(parameterInfo.getParameter().getPosition(), parameterInfo.getValue());
			}
			
			return preparedStatement.executeUpdate();
		} catch (SQLException e) { 
			throw new PersistenceException(e);
		}
	}

	@Override
	public Query setMaxResults(int maxResult) {
		maxResults = maxResult;
		return this;
	}

	@Override
	public int getMaxResults() { 
		return maxResults;
	}

	@Override
	public Query setFirstResult(int firstResult) { 
		this.firstResult = firstResult;
		return this;
	}

	@Override
	public int getFirstResult() { 
		return firstResult;
	}

	@Override
	public Query setHint(String hintName, Object value) {
		throw new NotImplementedException("setHint");
	}

	@Override
	public Map<String, Object> getHints() {
		throw new NotImplementedException("getHints");
	}

	@Override
	public <T> Query setParameter(Parameter<T> param, T value) {
		getparameterList().add(new ParameterInfo<T>(value, param));
		return this;
	}

	@Override
	public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
		getparameterList().add(new ParameterInfo<Calendar>(value, param, temporalType));
		return this;
	}

	@Override
	public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
		getparameterList().add(new ParameterInfo<Date>(value, param, temporalType));
		return this;
	}

	@Override
	public Query setParameter(String name, Object value) {
		getparameterList().add(new ParameterInfo<Object>(value, new ParameterImpl<Object>(name, null, Object.class)));
		return this;
	}

	@Override
	public Query setParameter(String name, Calendar value, TemporalType temporalType) {
		getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Calendar>(name, null, Calendar.class), temporalType));
		return this;
	}

	@Override
	public Query setParameter(String name, Date value, TemporalType temporalType) {
		getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Date>(name, null, Date.class), temporalType));
		return this;
	}

	@Override
	public Query setParameter(int position, Object value) {
		getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Object>(null, position, Object.class)));
		return this;
	}

	@Override
	public Query setParameter(int position, Calendar value, TemporalType temporalType) {
		getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Calendar>(null,position,Calendar.class), temporalType));
		return this;
	}

	@Override
	public Query setParameter(int position, Date value, TemporalType temporalType) { 
		getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Date>(null,position,Date.class), temporalType));
		return this;
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		final Set<Parameter<?>>  hashsetParameter = new HashSet<>();
		
		 List<Parameter<?>> listParameters = getparameterList().stream() 
	                .map(p -> p.getParameter())
	                .collect(Collectors.toList());
		 
		hashsetParameter.addAll(listParameters);
		return hashsetParameter;
		 
	}
	
	private List<ParameterInfo<?>> getparameterList() {
		if(parameters == null) parameters = new ArrayList<>();
		return parameters;
	}
	

	@Override
	public Parameter<?> getParameter(String name) { 
		ParameterInfo<?> parameterInfo = getparameterList().stream() .filter(p ->  p.getParameter().getName(). equals(name)).findFirst().orElse(null);
		if(parameterInfo != null) return parameterInfo.getParameter();		
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(String name, Class<T> type) {
		ParameterInfo parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getName(). equals(name) && p.getParameter().getParameterType(). equals(type)).findFirst().orElse(null);
		if(parameterInfo != null) return parameterInfo.getParameter();		
		return null;	
	}

	@Override
	public Parameter<?> getParameter(int position) {
		ParameterInfo parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getPosition() == position).findFirst().orElse(null);
		if(parameterInfo != null) return parameterInfo.getParameter();		
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(int position, Class<T> type) {
		ParameterInfo  parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getPosition() == position && p.getParameter().getParameterType(). equals(type)).findFirst().orElse(null);
		if(parameterInfo != null) return  parameterInfo.getParameter();		
		return null;
	}

	@Override
	public boolean isBound(Parameter<?> param) {
		ParameterInfo<?> parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getPosition() == param.getPosition() && p.getParameter().getParameterType(). equals(param.getParameterType()) && param.getName().equals(param.getName())).findFirst().orElse(null);
		return (parameterInfo != null);
	}

	@Override
	public <T> T getParameterValue(Parameter<T> param) {
		ParameterInfo<?>  parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getPosition() == param.getPosition() && p.getParameter().getParameterType(). equals(param.getParameterType()) && param.getName().equals(param.getName())).findFirst().orElse(null);
		if(parameterInfo != null) return (T) parameterInfo.getValue();
		return null;
	}

	@Override
	public Object getParameterValue(String name) {
		ParameterInfo<?> parameterInfo = getparameterList().stream() .filter(p ->  p.getParameter().getName(). equals(name)).findFirst().orElse(null);
		if(parameterInfo != null) return parameterInfo.getValue();		
		return null;
	}

	@Override
	public Object getParameterValue(int position) {
		ParameterInfo parameterInfo = getparameterList().stream() .filter(p -> p.getParameter().getPosition() == position).findFirst().orElse(null);
		if(parameterInfo != null) return parameterInfo.getValue();		
		return null;
	}

	
	@Override
	public Query setFlushMode(FlushModeType flushMode) {
		this.flushMode = flushMode;
		return this;
	}

	@Override
	public FlushModeType getFlushMode() { 
		return flushMode;
	}

	@Override
	public Query setLockMode(LockModeType lockMode) { 
		this.lockMode = lockMode;
		return this;
	}

	@Override
	public LockModeType getLockMode() {  
		return lockMode;
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		throw new NotImplementedException("unwrap");
	}
	
	private List<Object[]> _getListNoResultClass(ResultSet rs) throws SQLException {
		
		List<Object[]> result = new ArrayList<>();
		Object[] row;
		resultSetMetaData = rs.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount(); 
		while(rs.next()) { 
			row = new Object[columnCount];
			for (int i = 0; i < columnCount; i++) {
				row[i] = rs.getObject(i+1); 
			}
			result.add(row); 
		}
		return result;		
	}
 
	
	private ResultSet _executePrepareStatement() throws SQLException {
		
		for (ParameterInfo<?> parameterInfo : getparameterList()) { 
			preparedStatement.setObject(parameterInfo.getParameter().getPosition(), parameterInfo.getValue());
		}
		return preparedStatement.executeQuery();
	}


/**	public String getLastTableSequence() {
		if(StringUtils.isBlank(lastTableSequence)) {
			QueryManager queryManager = (QueryManager) ObjectContainer.instance().geApptScopeObj(QueryManager.class);  
			lastTableSequence = queryManager.getQuerySyntaxValue("last_table_sequence");
		}
		
		return lastTableSequence;
	}
*/
	public ResultSetMetaData getResultSetMetaData() {
		return resultSetMetaData;
	}
	
 
}
