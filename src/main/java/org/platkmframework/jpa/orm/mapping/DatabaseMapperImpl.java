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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.platkmframework.jpa.mapping.DatabaseMapper;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_CUSTOM_ASCIISTREAM;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_CUSTOM_BINARYSTREAM;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_CUSTOM_CHARACTERSTREAM;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_CUSTOM_NCHARACTERSTREAM;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_LOCALDATE;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_LOCALDATETIME;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_LOCALTIME;
import org.platkmframework.jpa.orm.mapping.custom.JavaValueParser_UTILDATE;
import org.platkmframework.util.reflection.ReflectionUtil;

/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public abstract class DatabaseMapperImpl implements DatabaseMapper {
 
//	protected DbMappings dbMappings;

	protected List<BasicJavaValueParser<?>> valueParsers;
	
	protected Map<Integer, String> defaultColumnTypes;
	 
	//protected String sqlLastSequence;
	
	protected JavaValueParser_Converter javaValueParser_Converter;
	
	public DatabaseMapperImpl() {
		super();
		//this.dbMappings = new DbMappings(); 
		this.defaultColumnTypes = new HashMap<Integer, String>();
		init();
		initSqlDataBase();
	}
	
	protected abstract void initSqlDataBase();

	private  void init() {
		
		javaValueParser_Converter = new JavaValueParser_Converter();
		
		this.valueParsers = new ArrayList<>();
		valueParsers.add(new JavaValueParser_ARRAY());
		valueParsers.add(new JavaValueParser_BIGINT());
		valueParsers.add(new JavaValueParser_BINARY());
		valueParsers.add(new JavaValueParser_BIT());
		valueParsers.add(new JavaValueParser_BLOG());
		valueParsers.add(new JavaValueParser_BOOLEAN());
		valueParsers.add(new JavaValueParser_CHAR());
		valueParsers.add(new JavaValueParser_CLOB());
		valueParsers.add(new JavaValueParser_DATALINK());
		valueParsers.add(new JavaValueParser_DATE());
		valueParsers.add(new JavaValueParser_DECIMAL());
		valueParsers.add(new JavaValueParser_DISTINCT());
		valueParsers.add(new JavaValueParser_DOUBLE());
		valueParsers.add(new JavaValueParser_FLOAT());
		valueParsers.add(new JavaValueParser_INTEGER());
		valueParsers.add(new JavaValueParser_LONGNVARCHAR());
		valueParsers.add(new JavaValueParser_LONGVARBINARY());
		valueParsers.add(new JavaValueParser_LONGVARCHAR());
		valueParsers.add(new JavaValueParser_NVARCHAR());
		valueParsers.add(new JavaValueParser_NCHAR());
		valueParsers.add(new JavaValueParser_NCLOB());
		valueParsers.add(new JavaValueParser_NULL());
		valueParsers.add(new JavaValueParser_NUMERIC());
		valueParsers.add(new JavaValueParser_NVARCHAR());
		valueParsers.add(new JavaValueParser_OTHER());
		valueParsers.add(new JavaValueParser_REAL());
		valueParsers.add(new JavaValueParser_REF_CURSOR());
		valueParsers.add(new JavaValueParser_REF());
		valueParsers.add(new JavaValueParser_ROWID());
		valueParsers.add(new JavaValueParser_SMALLINT());
		valueParsers.add(new JavaValueParser_SQLXML());
		valueParsers.add(new JavaValueParser_STRUCT());
		
		valueParsers.add(new JavaValueParser_TIME_WITH_TIMEZONE());
		valueParsers.add(new JavaValueParser_TIME());
		valueParsers.add(new JavaValueParser_TIMESTAMP_WITH_TIMEZONE());
		valueParsers.add(new JavaValueParser_TIMESTAMP());
		valueParsers.add(new JavaValueParser_TINYINT());
		valueParsers.add(new JavaValueParser_VARBINARY());
		valueParsers.add(new JavaValueParser_VARCHAR());
		valueParsers.add(new JavaValueParser_ARRAY());
		
		//valueParsers.add(new JavaValueParser_NUMERIC_Integer());
		valueParsers.add(new JavaValueParser_INTEGER_Object());
		valueParsers.add(new JavaValueParser_LOCALDATETIME());
		valueParsers.add(new JavaValueParser_LOCALDATE());
		valueParsers.add(new JavaValueParser_UTILDATE());
		valueParsers.add(new JavaValueParser_TIMESTAMP_LocalDateTime());

		valueParsers.add(new JavaValueParser_CUSTOM_ASCIISTREAM());
		valueParsers.add(new JavaValueParser_CUSTOM_BINARYSTREAM());
		valueParsers.add(new JavaValueParser_CUSTOM_CHARACTERSTREAM());
		valueParsers.add(new JavaValueParser_CUSTOM_NCHARACTERSTREAM());
		valueParsers.add(new JavaValueParser_LOCALTIME());
	}
	
	
	/**
	public String getDbTypeFromJavaType(Class<?> classType) {

		DbMapping<?> dbMapping = this.dbMappings.getH2mapping().stream().filter(dbm -> (dbm.getJavatype().getName().equals(classType.getName()))).findAny().orElse(null);
		if(dbMapping == null) 
			throw new PlatkmJpaException("no db mapping found for " + classType.getName());
		
		DbType dbtype = dbMapping.getDbtypes().stream().filter(dbt->(dbt.isDefaultType())).findAny().orElse(null);
		if(dbtype == null) 
			throw new PlatkmJpaException("no db type found for " + classType.getName());
		 
		return dbtype.getName();
	}
	
	public Class<?> getJavaTypeFromDbType(String sqlType) {

		DbType dbType = null;
		for (DbMapping<?> dbMapping : this.dbMappings.getH2mapping()) {
			
			dbType = dbMapping.getDbtypes().stream().filter(dbt -> (dbt.getName().equalsIgnoreCase(sqlType))).findAny().orElse(null);
			if(dbType != null) return  dbMapping.getJavatype(); 
		}
		return null;
	}
	
	public DbMapping<?>  getDbMapping(String sqlType) {

		DbType dbType = null;
		for (DbMapping<?> dbMapping : this.dbMappings.getH2mapping()) {
			
			dbType = dbMapping.getDbtypes().stream().filter(dbt -> (dbt.getName().equalsIgnoreCase(sqlType))).findAny().orElse(null);
			if(dbType != null) return  dbMapping; 
		}
		return null;
	}

	public  SqlSentencesProcessor getSqlSentencesProcessor() {
		return this.sqlSentencesProcessor;
	}

 

	public SqlSentencesProcessor getSqlSentencesProcessor() {
		return sqlSentencesProcessor;
	}
*/
	public BasicJavaValueParser<?> getJavaValueParserByJavaType(Class<?> javaType){
		
		//  check(new JavaValueParser_VARBINARY(), javaType);
		BasicJavaValueParser<?> vpReturn = null;
		
		for (BasicJavaValueParser<?> vp : valueParsers) {
			if(check(vp, javaType)){
				vpReturn = vp;
			}
		}
		return vpReturn;
		//return valueParsers.stream().filter((vp)-> check(vp, javaType)).findAny().orElse(null);
	}
	
	private final boolean check(BasicJavaValueParser<?> vp, Class<?> javaType) {
		return 
				vp.getJavaType().getName().equals(javaType.getName()) ||
				(javaType.isPrimitive() && vp.getJavaType().getSimpleName().equalsIgnoreCase(javaType.getName())) ||
					(javaType.isArray() &&  vp.getJavaType().getComponentType() != null &&
				(javaType.getComponentType().getSimpleName().equals(vp.getJavaType().getComponentType().getSimpleName())) 
		);
	}

	public BasicJavaValueParser<?> getJavaValueParserBySqlTypeAndReturnJavaType(int sqlType, Class<?> javaType){
		return valueParsers.stream().filter((vp)-> vp.getJavaType().getName().equals(javaType.getName()) && vp.getSqlType() == sqlType).findAny().orElse(null);
	}
	
	public BasicJavaValueParser<?> getDefaultJavaValueParserBySqlType(int sqlType){
		return valueParsers.stream().filter((vp)-> vp.isDefualtParseSqlTypeToJavaType() && vp.getSqlType() == sqlType).findAny().orElse(null);
	}

	public String getDbDataType(Class<?> javaType) {
		return defaultColumnTypes.get(getJavaValueParserByJavaType(getObjectJavaType(javaType)).getSqlType());
	}

	private Class<?> getObjectJavaType(Class<?> javaType) {
		if (javaType.isPrimitive()) return ReflectionUtil.primitiveToObjectType(javaType);
		if (javaType.isArray() && javaType.getComponentType().getName().equals(byte.class.getSimpleName())) return  byte[].class; //only byte[]
		return javaType;
	}

	public JavaValueParser_Converter getJavaValueParserByConverter() {
		return javaValueParser_Converter;
	}
}

