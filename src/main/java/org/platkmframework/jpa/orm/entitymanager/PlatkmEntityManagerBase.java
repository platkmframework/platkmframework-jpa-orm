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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.platkmframework.content.ObjectContainer;
import org.platkmframework.database.query.common.ColumnInfoValue;
import org.platkmframework.doi.data.BeanMethodInfo;
import org.platkmframework.jpa.base.PlakmBaseDao;
import org.platkmframework.jpa.converter.JpaConverterJson;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;
import org.platkmframework.jpa.orm.mapping.DbMappingUtil;
import org.platkmframework.jpa.persistence.PersistenceUnit;
import org.platkmframework.util.error.InvocationException; 
 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public abstract class PlatkmEntityManagerBase  extends PlakmBaseDao{
	
  
	protected PlatkmEntityTransaction platkmEntityTransaction;
	
	 
	public PlatkmEntityManagerBase(PersistenceUnit  persistenceUnit, PlatkmEntityTransaction platkmEntityTransaction) {
		super(persistenceUnit);
		this.platkmEntityTransaction = platkmEntityTransaction;
	}

	public ColumnInfoValue insertSQL(String tableName, List<ColumnInfoValue> columns) throws DatabaseValidationException   {
		  
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO  " + tableName + " ");
			sb.append("(" + _getInsertIntoFieldNames(columns) + ")");
			sb.append("VALUES ( ");
			sb.append(_getInsertIntoValues(columns));
			sb.append(")");
			//java.sql.Types
			
			PreparedStatement preparedStatement;
			ColumnInfoValue cvPkAuntoIncrement = columns.stream().filter(cv -> cv.isPk() && cv.isIncrement()).findAny().orElse(null);
			if(cvPkAuntoIncrement != null) {
				preparedStatement = platkmEntityTransaction.getCon(). prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS); 
			}else {
				preparedStatement = platkmEntityTransaction.getCon(). prepareStatement(sb.toString()); 
			}
				
			_setValuesToPrepareStatment(columns, preparedStatement, true);
			preparedStatement.executeUpdate();
			
			if(cvPkAuntoIncrement == null) return null;
			
			ResultSet rsKey = preparedStatement.getGeneratedKeys();
			if(rsKey.next())
				cvPkAuntoIncrement.setValue(DbMappingUtil.getValue(rsKey.getMetaData().getColumnType(1), cvPkAuntoIncrement.getClassType(), rsKey, 1, (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper()));
			 
			return cvPkAuntoIncrement;  
		}catch (SQLException e) {
			throw new DatabaseValidationException(e.getMessage());
		}
	}
	
	public void updateSQL(String tablename, List<ColumnInfoValue> columns) throws  DatabaseValidationException {
	
		try {
			
			StringBuilder sb = new StringBuilder();
			sb.append("UPDATE " + tablename + " SET ");
		  	sb.append(_getUpdateIntoValues(columns));
		  	
		  	ColumnInfoValue cvPkAuntoIncrement = columns.stream().filter(cv -> cv.isPk()).findAny().orElse(null);
		  	if(cvPkAuntoIncrement == null) throw new DatabaseValidationException("No se encontró llave primaria para el update de entidad");
		  	
		 	sb.append(" WHERE " + cvPkAuntoIncrement.getName() + "=? ");
		 	 
		 	PreparedStatement preparedStatement = platkmEntityTransaction.getCon(). prepareStatement(sb.toString());
			_setValuesToPrepareStatment(columns, preparedStatement, false); 
			preparedStatement.execute(); 
		}catch (SQLException e) {
			throw new DatabaseValidationException(e.getMessage());
		}
	}
	
	public List<Object> findRecord(String sql, List<Object> parameters, List<ColumnInfoValue> columns) throws SQLException {
 		
		PreparedStatement stmt = platkmEntityTransaction.getCon().prepareStatement(sql);
 		if(parameters != null) 
        	for (int i = 0; i<parameters.size(); i++) 
        		stmt.setObject(i+1, parameters.get(i)); 
	  
 		ResultSet rs = stmt.executeQuery();
 		int colCount = rs.getMetaData().getColumnCount();
 		List<Object> list = new ArrayList<>();
 		Object value;
 		 if(rs.next()){   
 			for (int i = 0; i < colCount; i++) {  
 				value = DbMappingUtil.getValue( rs.getMetaData().getColumnType(i+1), columns.get(i).getClassType(), rs, i+1, (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
 				list.add(value); 
				columns.get(i).setValue(value);
			} 
 		 }
 		 return list;
	}
	 
	
	public List<Object> findRecords(String sql, List<Object> parameters) throws SQLException {
  
		PreparedStatement stmt = platkmEntityTransaction.getCon().prepareStatement(sql);
	   	if(parameters!= null) 
	       	for (int i = 0; i<parameters.size(); i++) 
	       		stmt.setObject(i+1,parameters.get(i));     
		  
			ResultSet rs = stmt.executeQuery(); 
			List<Object> list = new  ArrayList<>();
			Object[] row;
			int colCount = rs.getMetaData().getColumnCount();
			while (rs.next()){ 
				row = new Object[colCount];
				for (int i = 0; i <= colCount; i++)  
					row[i]=rs.getObject(i+1); 
				
				list.add(row);
			} 
		return list;
	}

	public <E> List<E> findRecords(String sql, List<Object> parameters, Class<E> resultClass) throws SQLException, DatabaseValidationException {
		
		PreparedStatement stmt = platkmEntityTransaction.getCon().prepareStatement(sql);
	   	if(parameters!= null) 
	       	for (int i = 0; i<parameters.size(); i++) 
	       		stmt.setObject(i+1,parameters.get(i));    
	  
		ResultSet rs = stmt.executeQuery(); 
        List<E> result = new ArrayList<>();
        while (rs.next()) {
       	 result.add(DbMappingUtil.getCustomEntity(rs, resultClass, (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper())); 
        }
        return result;
	}

	protected String getMetaDataColumnsName(ResultSetMetaData resultSetMetaData ) throws SQLException {
		 String columns = "";
         int i = resultSetMetaData.getColumnCount();
         String coma = "";
         for(int j=1; j <= i; j++) {
         	columns+= coma + resultSetMetaData.getColumnName(j);
         	coma = ",";
         }
         return columns;
         
	}
	
	protected void  _setValuesToPrepareStatment(List<ColumnInfoValue> columns, PreparedStatement ps, boolean isInsert) throws DatabaseValidationException, SQLException {
		 
		if(columns != null){
			ColumnInfoValue columnInfoValue; 
			List<ColumnInfoValue> pkList = new ArrayList<>();
			JpaConverterJson<?> jpaConverterJson;
			BeanMethodInfo beanMethodInfo;
			int y = 0;
			for (int i = 0; i < columns.size(); i++){ 
				columnInfoValue = columns.get(i);
				Object value = columnInfoValue.getValue();
				
				if(!isInsert && columnInfoValue.isPk()  ){
					_validate(columnInfoValue, value);
					pkList.add(columnInfoValue); 
				
				}else if(!columnInfoValue.isIncrement()){
					y++;
					_validate(columnInfoValue, value);
					if(columnInfoValue.getConverter() != null) {
						List<Object> list =  ObjectContainer.instance().getListObjectByInstance(columnInfoValue.getConverter());
						if(list != null && !list.isEmpty()) {
							jpaConverterJson = JpaConverterJson.class.cast(list.get(0));
							DbMappingUtil.setValue(String.class, ps, y, value != null?  jpaConverterJson.convertToDatabaseColumn(value) :"" , (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
						}else {
							throw new DatabaseValidationException("para el valor del campo: " + columnInfoValue.getName()  + ", no se encontró el convertidor-> " + columnInfoValue.getConverter().toString());
						}
					}else {
						
						if(columnInfoValue.getSystemColumnAction() != null) {
							try {
								beanMethodInfo = getColumnSystemMethodByKey(columnInfoValue.getSystemColumnKey());
								value = getColumnSystemValue(beanMethodInfo.getMethod(), beanMethodInfo.getObj());
								//returnType = beanMethodInfo.getMethod().getReturnType();
							} catch (InvocationException e) {
								throw new DatabaseValidationException("Error obteniendo el valor por defecto del campo ->" + columnInfoValue.getName());
							}
						}
						DbMappingUtil.setValue(columnInfoValue.getClassType(), ps, y, value, (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
						
					}
				}
			}
			if(pkList.size() >0  && !isInsert) {
				for (int i = 0; i < pkList.size(); i++){
					y++;
					DbMappingUtil.setValue(pkList.get(i).getClassType(), ps, y, pkList.get(i).getValue(), (DatabaseMapperImpl)persistenceUnit.getDatabaseMapper());
				}
			}
		}	 
		
	}
} 
