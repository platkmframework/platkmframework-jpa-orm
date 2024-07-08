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
package org.platkmframework.jpa.orm.mapping;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.annotation.Component;
import org.platkmframework.annotation.db.ConverterReference;
import org.platkmframework.content.ObjectContainer;
import org.platkmframework.jpa.converter.BaseConverter;
import org.platkmframework.jpa.converter.JpaConverterJson;
import org.platkmframework.jpa.exception.DatabaseConnectionException;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.util.error.InvocationException;
import org.platkmframework.util.reflection.ReflectionUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;

public class DbMappingUtil {
	
	public static Object getValue(int sqlType, Class<?> classType, ResultSet rs, int rsIndex, DatabaseMapperImpl databaseMapper ) throws SQLException { 
		BasicJavaValueParser<?> javaValueParser = databaseMapper.getJavaValueParserBySqlTypeAndReturnJavaType(sqlType, classType);
	
		if(javaValueParser == null) {
			try {
				javaValueParser = databaseMapper.getJavaValueParserByJavaType(Class.forName(rs.getMetaData().getColumnClassName(rsIndex)));
			} catch (ClassNotFoundException e) {
				throw new DatabaseConnectionException("No se encontró la información para la " +
						   " clase->" + classType.getName()  +
                        " sqlType ->" + sqlType +
                        " column name ->" + rs.getMetaData().getColumnType(rsIndex)
                        );
			}
		} 	
		
		return javaValueParser.readValue(rs, rsIndex);
	}
	
	public static Object getValueFromJsonConverter(JpaConverterJson<?> jpaConverterJson, ResultSet rs, int rsIndex, DatabaseMapperImpl databaseMapper ) throws SQLException { 
		JavaValueParser_Converter javaValueParser = databaseMapper.getJavaValueParserByConverter();
		
		if(javaValueParser == null) {
			 throw new DatabaseConnectionException("No se encontró la información para la " +
						" clase->" + jpaConverterJson.getClass().getName()  +
						" column name ->" + rs.getMetaData().getColumnType(rsIndex)
						);
			 
		} 	
		
		return jpaConverterJson.convertToEntityAttribute(javaValueParser.readValue(rs, rsIndex));
	}
	
	public static Object getValueFromCustomConverter(BaseConverter attributeConverter, ResultSet rs, int rsIndex, DatabaseMapperImpl databaseMapper ) throws SQLException { 
		JavaValueParser_Converter javaValueParser = databaseMapper.getJavaValueParserByConverter();
		
		if(javaValueParser == null) {
			 throw new DatabaseConnectionException("No se encontró la información para la " +
						" clase->" + attributeConverter.getClass().getName()  +
						" column name ->" + rs.getMetaData().getColumnType(rsIndex)
						);
		} 	
		
		return attributeConverter.convertToEntityAttribute(javaValueParser.readValue(rs, rsIndex));
	}
	
	public static void setValue(Class<?> classType, PreparedStatement ps, int index, Object value, DatabaseMapperImpl databaseMapper ) throws SQLException { 
		BasicJavaValueParser<?>  javaValueParser = databaseMapper.getJavaValueParserByJavaType(classType);
		
		if(javaValueParser == null) 
			throw new DatabaseConnectionException("No se encontró la información para tipo de clase ->" + classType.getName());
		javaValueParser.setValue(ps, index, value); 
	}	
	
	public static <F> F getCustomEntity(ResultSet rs, Class<F> class1, DatabaseMapperImpl databaseMapper) throws DatabaseValidationException {
		 
		try {
			F instance = ReflectionUtil.createInstance1(class1);
			List<Field> fields = ReflectionUtil.getAllFieldHeritage(class1);
			//String[] arrayColumn = columns.split(",");  
			String columName;
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) { 
				 for (Field field : fields) {
					 if(field.isAnnotationPresent(Column.class)) {
						 columName = field.getAnnotation(Column.class).name();
					 }else {
						 columName = field.getName();
					 }
					 if( rs.getMetaData().getColumnName(i+1).equalsIgnoreCase(columName)){
						 if(field.isAnnotationPresent(Convert.class)) {
							 
							List<Object> list =  ObjectContainer.instance().getListObjectByInstance(field.getAnnotation(Convert.class).converter());
							if(list != null && !list.isEmpty()) {
								
								processConverter(instance, field, rs, databaseMapper, i, list.get(0));
								
							}else {
								throw new DatabaseValidationException("para el valor del campo: " + field.getName()  + ", no se encontró el convertidor-> " + JpaConverterJson.class.toString());
							} 
							 
						 }else if(field.isAnnotationPresent(ConverterReference.class)) {
							 String references = field.getAnnotation(ConverterReference.class).reference();
							 if(StringUtils.isNotBlank(references)) {
								 List<Object> components = ObjectContainer.instance().getListObjectByAnnontation(Component.class);
								 Object converter = components.stream().filter((o)->  references.equalsIgnoreCase(o.getClass().getAnnotation(Component.class).reference())).findFirst().orElse(null);
								 if(converter != null) {
									 processConverter(instance, field, rs, databaseMapper, i, converter);
								 }else {
									 ReflectionUtil.setAttributeValue(instance, field,  
											 getValue(rs.getMetaData().getColumnType(i+1), field.getType(), rs, i+1, databaseMapper), false);
								 }
							 }else {
								 ReflectionUtil.setAttributeValue(instance, field,  
										 getValue(rs.getMetaData().getColumnType(i+1), field.getType(), rs, i+1, databaseMapper), false);
							 }
						 }else {
						  
							 ReflectionUtil.setAttributeValue(instance, field,  
									 getValue(rs.getMetaData().getColumnType(i+1), field.getType(), rs, i+1, databaseMapper), false);
						 }
						 break;
					 }
				}
			}
			return instance;
			
		} catch (InvocationException | SQLException e) {
			throw new DatabaseConnectionException(e.getMessage());
		} 
		
		
	}
	
	protected static void processConverter(Object instance, Field field, ResultSet rs, DatabaseMapperImpl databaseMapper, int index, Object converter) throws InvocationException, SQLException {
		
		if(converter instanceof JpaConverterJson) {
			JpaConverterJson<?> jpaConverterJson = JpaConverterJson.class.cast(converter);
			ReflectionUtil.setAttributeValue(instance, field,  
					getValueFromJsonConverter(jpaConverterJson, rs, index+1, databaseMapper), false); 
		}else {
			
			BaseConverter attributeConverter = BaseConverter.class.cast(converter);
			ReflectionUtil.setAttributeValue(instance, field,  
					getValueFromCustomConverter(attributeConverter, rs, index+1, databaseMapper), false); 
		}
	}
	
	public static Properties getCustomProperties(ResultSet rs, String[] arrayColumn, DatabaseMapperImpl databaseMapper) {
		Properties  row = new Properties();
		try {
			for (int i = 0; i < arrayColumn.length; i++) { 
				row.put(arrayColumn[i], rs.getObject(i+1));
				 
			}
			return row;
			
		} catch ( SQLException e) {
			throw new DatabaseConnectionException(e.getMessage());
		} 
	}
	
	public static Map<String, Object> getCustomMap(ResultSet rs,  String[] arrayColumn, DatabaseMapperImpl databaseMapper) {
		Map<String, Object> row = new HashMap<>();
		try {
			for (int i = 0; i < arrayColumn.length; i++) { 
				row.put(arrayColumn[i], rs.getObject(i+1));
				 
			}
			return row;
			
		} catch ( SQLException e) {
			throw new DatabaseConnectionException(e.getMessage());
		} 
	}
	
	
	public static  String getMetaDataColumnsName(ResultSetMetaData resultSetMetaData ) throws SQLException {
		 String columns = "";
       int i = resultSetMetaData.getColumnCount();
       String coma = "";
       for(int j=1; j <= i; j++) {
       	columns+= coma + resultSetMetaData.getColumnName(j);
       	coma = ",";
       }
       return columns;
       
	}
}
