/**
 * ****************************************************************************
 *  Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	 https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  Contributors:
 *  	Eduardo Iglesias Taylor - initial API and implementation
 * *****************************************************************************
 */
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
import org.apache.commons.lang3.NotImplementedException;
import org.platkmframework.jpa.base.ParameterImpl;
import org.platkmframework.jpa.base.ParameterInfo;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;
import org.platkmframework.jpa.orm.mapping.DbMappingUtil;
import org.platkmframework.util.error.InvocationException;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Parameter;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.persistence.TemporalType;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class QueryImpl implements Query {

    /**
     * Atributo parameters
     */
    private List<ParameterInfo<?>> parameters;

    /**
     * Atributo maxResults
     */
    private int maxResults = 0;

    /**
     * Atributo firstResult
     */
    private int firstResult = 0;

    /**
     * Atributo flushMode
     */
    private FlushModeType flushMode;

    /**
     * Atributo lockMode
     */
    private LockModeType lockMode;

    /**
     * Atributo preparedStatement
     */
    private PreparedStatement preparedStatement;

    /**
     * Atributo resultSetMetaData
     */
    private ResultSetMetaData resultSetMetaData = null;

    /**
     * Atributo databaseMapper
     */
    private DatabaseMapperImpl databaseMapper;

    /**
     * Atributo returnClass
     */
    Class<?> returnClass;

    /**
     * Constructor QueryImpl
     * @param preparedStatement preparedStatement
     * @param returnClass returnClass
     * @param map map
     * @param databaseMapper databaseMapper
     */
    protected QueryImpl(PreparedStatement preparedStatement, Class<?> returnClass, Map map, DatabaseMapperImpl databaseMapper) {
        super();
        this.preparedStatement = preparedStatement;
        this.returnClass = returnClass;
        this.databaseMapper = databaseMapper;
    }

    /**
     * getResultList
     * @return List
     */
    @Override
    public List getResultList() {
        try {
            ResultSet rs = _executePrepareStatement();
            if (returnClass != null) {
                return _getListResultClass(rs);
            } else {
                return _getListNoResultClass(rs);
            }
        } catch (SQLException | InvocationException | DatabaseValidationException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * _getListResultClass
     * @param rs rs
     * @return List
     * @throws SQLException SQLException
     * @throws InvocationException InvocationException
     * @throws DatabaseValidationException DatabaseValidationException
     */
    private List<?> _getListResultClass(ResultSet rs) throws SQLException, InvocationException, DatabaseValidationException {
        List result = new ArrayList<>();
        while (rs.next()) {
            if (returnClass.isAssignableFrom(Map.class)) {
                String columns = DbMappingUtil.getMetaDataColumnsName(rs.getMetaData());
                String[] arrayColumn = columns.split(",");
                result.add(DbMappingUtil.getCustomMap(rs, arrayColumn, databaseMapper));
            } else if (returnClass.isAssignableFrom(Properties.class)) {
                String columns = DbMappingUtil.getMetaDataColumnsName(rs.getMetaData());
                String[] arrayColumn = columns.split(",");
                result.add(DbMappingUtil.getCustomProperties(rs, arrayColumn, databaseMapper));
            } else {
                result.add(DbMappingUtil.getCustomEntity(rs, returnClass, databaseMapper));
            }
        }
        return result;
    }

    /**
     * getSingleResult
     * @return Object
     */
    @Override
    public Object getSingleResult() {
        try {
            ResultSet rs = _executePrepareStatement();
            if (returnClass != null) {
                List<?> list = _getListResultClass(rs);
                if (list == null || list.isEmpty())
                    return null;
                return list.get(0);
            } else {
                List<Object[]> list = _getListNoResultClass(rs);
                if (list == null || list.isEmpty())
                    return null;
                return list.get(0);
            }
        } catch (SQLException | InvocationException | DatabaseValidationException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * executeUpdate
     * @return int
     */
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

    /**
     * setMaxResults
     * @param maxResult maxResult
     * @return Query
     */
    @Override
    public Query setMaxResults(int maxResult) {
        maxResults = maxResult;
        return this;
    }

    /**
     * getMaxResults
     * @return int
     */
    @Override
    public int getMaxResults() {
        return maxResults;
    }

    /**
     * setFirstResult
     * @param firstResult firstResult
     * @return Query
     */
    @Override
    public Query setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    /**
     * getFirstResult
     * @return int
     */
    @Override
    public int getFirstResult() {
        return firstResult;
    }

    /**
     * setHint
     * @param hintName hintName
     * @param value value
     * @return Query
     */
    @Override
    public Query setHint(String hintName, Object value) {
        throw new NotImplementedException("setHint");
    }

    /**
     * getHints
     * @return Map
     */
    @Override
    public Map<String, Object> getHints() {
        throw new NotImplementedException("getHints");
    }

    /**
     * setParameter
     * @param param param
     * @param value value
     * @return Query
     */
    @Override
    public <T> Query setParameter(Parameter<T> param, T value) {
        getparameterList().add(new ParameterInfo<T>(value, param));
        return this;
    }

    /**
     * setParameter
     * @param param param
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(Parameter<Calendar> param, Calendar value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<Calendar>(value, param, temporalType));
        return this;
    }

    /**
     * setParameter
     * @param param param
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(Parameter<Date> param, Date value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<Date>(value, param, temporalType));
        return this;
    }

    /**
     * setParameter
     * @param name name
     * @param value value
     * @return Query
     */
    @Override
    public Query setParameter(String name, Object value) {
        getparameterList().add(new ParameterInfo<Object>(value, new ParameterImpl<Object>(name, null, Object.class)));
        return this;
    }

    /**
     * setParameter
     * @param name name
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(String name, Calendar value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Calendar>(name, null, Calendar.class), temporalType));
        return this;
    }

    /**
     * setParameter
     * @param name name
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(String name, Date value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Date>(name, null, Date.class), temporalType));
        return this;
    }

    /**
     * setParameter
     * @param position position
     * @param value value
     * @return Query
     */
    @Override
    public Query setParameter(int position, Object value) {
        getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Object>(null, position, Object.class)));
        return this;
    }

    /**
     * setParameter
     * @param position position
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(int position, Calendar value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Calendar>(null, position, Calendar.class), temporalType));
        return this;
    }

    /**
     * setParameter
     * @param position position
     * @param value value
     * @param temporalType temporalType
     * @return Query
     */
    @Override
    public Query setParameter(int position, Date value, TemporalType temporalType) {
        getparameterList().add(new ParameterInfo<>(value, new ParameterImpl<Date>(null, position, Date.class), temporalType));
        return this;
    }

    /**
     * getParameters
     * @return Set
     */
    @Override
    public Set<Parameter<?>> getParameters() {
        final Set<Parameter<?>> hashsetParameter = new HashSet<>();
        List<Parameter<?>> listParameters = getparameterList().stream().map(p -> p.getParameter()).collect(Collectors.toList());
        hashsetParameter.addAll(listParameters);
        return hashsetParameter;
    }

    /**
     * getparameterList
     * @return List
     */
    private List<ParameterInfo<?>> getparameterList() {
        if (parameters == null)
            parameters = new ArrayList<>();
        return parameters;
    }

    /**
     * getParameter
     * @param name name
     * @return Parameter
     */
    @Override
    public Parameter<?> getParameter(String name) {
        ParameterInfo<?> parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getName().equals(name)).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getParameter();
        return null;
    }

    /**
     * getParameter
     * @param name name
     * @param type type
     * @return Parameter
     */
    @Override
    public <T> Parameter<T> getParameter(String name, Class<T> type) {
        ParameterInfo parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getName().equals(name) && p.getParameter().getParameterType().equals(type)).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getParameter();
        return null;
    }

    /**
     * getParameter
     * @param position position
     * @return Parameter
     */
    @Override
    public Parameter<?> getParameter(int position) {
        ParameterInfo parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getPosition() == position).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getParameter();
        return null;
    }

    /**
     * getParameter
     * @param position position
     * @param type type
     * @return Parameter
     */
    @Override
    public <T> Parameter<T> getParameter(int position, Class<T> type) {
        ParameterInfo parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getPosition() == position && p.getParameter().getParameterType().equals(type)).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getParameter();
        return null;
    }

    /**
     * isBound
     * @param param param
     * @return boolean
     */
    @Override
    public boolean isBound(Parameter<?> param) {
        ParameterInfo<?> parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getPosition() == param.getPosition() && p.getParameter().getParameterType().equals(param.getParameterType()) && param.getName().equals(param.getName())).findFirst().orElse(null);
        return (parameterInfo != null);
    }

    /**
     * getParameterValue
     * @param param param
     * @return T
     */
    @Override
    public <T> T getParameterValue(Parameter<T> param) {
        ParameterInfo<?> parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getPosition() == param.getPosition() && p.getParameter().getParameterType().equals(param.getParameterType()) && param.getName().equals(param.getName())).findFirst().orElse(null);
        if (parameterInfo != null)
            return (T) parameterInfo.getValue();
        return null;
    }

    /**
     * getParameterValue
     * @param name name
     * @return Object
     */
    @Override
    public Object getParameterValue(String name) {
        ParameterInfo<?> parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getName().equals(name)).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getValue();
        return null;
    }

    /**
     * getParameterValue
     * @param position position
     * @return Object
     */
    @Override
    public Object getParameterValue(int position) {
        ParameterInfo parameterInfo = getparameterList().stream().filter(p -> p.getParameter().getPosition() == position).findFirst().orElse(null);
        if (parameterInfo != null)
            return parameterInfo.getValue();
        return null;
    }

    /**
     * setFlushMode
     * @param flushMode flushMode
     * @return Query
     */
    @Override
    public Query setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
        return this;
    }

    /**
     * getFlushMode
     * @return FlushModeType
     */
    @Override
    public FlushModeType getFlushMode() {
        return flushMode;
    }

    /**
     * setLockMode
     * @param lockMode lockMode
     * @return Query
     */
    @Override
    public Query setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    /**
     * getLockMode
     * @return LockModeType
     */
    @Override
    public LockModeType getLockMode() {
        return lockMode;
    }

    /**
     * unwrap
     * @param cls cls
     * @return T
     */
    @Override
    public <T> T unwrap(Class<T> cls) {
        throw new NotImplementedException("unwrap");
    }

    /**
     * _getListNoResultClass
     * @param rs rs
     * @return List
     * @throws SQLException SQLException
     */
    private List<Object[]> _getListNoResultClass(ResultSet rs) throws SQLException {
        List<Object[]> result = new ArrayList<>();
        Object[] row;
        resultSetMetaData = rs.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        while (rs.next()) {
            row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            result.add(row);
        }
        return result;
    }

    /**
     * _executePrepareStatement
     * @return ResultSet
     * @throws SQLException SQLException
     */
    private ResultSet _executePrepareStatement() throws SQLException {
        for (ParameterInfo<?> parameterInfo : getparameterList()) {
            preparedStatement.setObject(parameterInfo.getParameter().getPosition(), parameterInfo.getValue());
        }
        return preparedStatement.executeQuery();
    }

    /**
     * getResultSetMetaData
     * @return ResultSetMetaData
     */
    public ResultSetMetaData getResultSetMetaData() {
        return resultSetMetaData;
    }
}
