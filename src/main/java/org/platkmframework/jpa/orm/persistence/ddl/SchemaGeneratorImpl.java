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
package org.platkmframework.jpa.orm.persistence.ddl;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.jpa.dll.SchemaGenerator;
import org.platkmframework.jpa.persistence.PersistenceUnit;

/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class SchemaGeneratorImpl implements SchemaGenerator  {
	
//	private static final Logger logger = LogManager.getLogger(SchemaGeneratorImpl.class);
	
/**	private static final String C_SCHEMA_GENERATION_DATABASE_ACTION = "javax.persistence.schema-generation.database.action";
	private static final String C_SCHEMA_GENERATION_CREATE_SOURCE = "javax.persistence.schema-generation.create-source";
	private static final String C_SCHEMA_GENERATION_CREATE_SCRIPT_SOURCE = "javax.persistence.schema-generation.create-script-source";
	private static final String C_SCHEMA_GENERATION_DROP_SOURCE = "javax.persistence.schema-generation.drop-source";
	private static final String C_SCHEMA_GENERATION_DROP_SCRIPT_SOURCE = "javax.persistence.schema-generation.drop-script-source";
	private static final String C_SCHEMA_SQL_LOAD_SCRIPT_SOURCE = "javax.persistence.sql-load-script-source";
**/

	@Override
	public boolean generateSchema(Map map) {
		return true;
	/**	PlatkmEntityManager<F> platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
		try {
			platkmEntityManager.getTransaction().begin();
			
			String schemaDatabaseAction = (String)plakmEntityManagerFactory.getPersistenceUnit().getProperties().get(C_SCHEMA_GENERATION_DATABASE_ACTION);
			if("create".equalsIgnoreCase(schemaDatabaseAction)){
				createSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
			}else if("drop-and-create".equalsIgnoreCase(schemaDatabaseAction)){
				dropSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
				createSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
			}if("drop".equalsIgnoreCase(schemaDatabaseAction)){
				dropSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
			} 
			
			executeLoadScriptSource(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
			 
			platkmEntityManager.getTransaction().commit();
			return true;
			
		} catch (ClassNotFoundException | IOException e) {
			logger.error(e);
			platkmEntityManager.getTransaction().rollback();
			throw new PlatkmJpaException("Error updating schema");
		}finally{
			platkmEntityManager.close();
			plakmEntityManagerFactory.returnObject(platkmEntityManager);
		}
		*/ 
	}	
	
	/**private void executeLoadScriptSource(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager, Map map) throws IOException {
		String sqlLoadScriptSource = (String)persistenceUnit.getProperties().get(C_SCHEMA_SQL_LOAD_SCRIPT_SOURCE);
		if(StringUtils.isNotBlank(sqlLoadScriptSource)){
			InputStream inputSteram = this.getClass().getResourceAsStream("/" + sqlLoadScriptSource);
			String script = IOUtils.toString(inputSteram, StandardCharsets.UTF_8);
			platkmEntityManager.createNativeQuery(script).executeUpdate();
		}
	}

	private void createSchema(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager,  Map map ) throws IOException, ClassNotFoundException {
		 
		String schemaGenerationCreateSource = (String)persistenceUnit.getProperties().get(C_SCHEMA_GENERATION_CREATE_SOURCE);
		if("script".equalsIgnoreCase(schemaGenerationCreateSource)){
			 executeCreateScript(persistenceUnit,platkmEntityManager,  map); 
		}else if("metadata".equalsIgnoreCase(schemaGenerationCreateSource)){
			executeCreateMetaData(persistenceUnit, platkmEntityManager); 
		}if("metadata-then-script".equalsIgnoreCase(schemaGenerationCreateSource)){
			executeCreateMetaData(persistenceUnit, platkmEntityManager); 
			executeCreateScript(persistenceUnit,platkmEntityManager, map); 
		}else if("script-then-metadata".equalsIgnoreCase(schemaGenerationCreateSource)){
			executeCreateScript(persistenceUnit,platkmEntityManager, map); 
			executeCreateMetaData(persistenceUnit, platkmEntityManager); 
		}  
	}
 
	private void executeCreateScript(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager, Map map) throws IOException {
		
		String schemaGenerationCreateScriptSource = (String)persistenceUnit.getProperties().get(C_SCHEMA_GENERATION_CREATE_SCRIPT_SOURCE);
		if(StringUtils.isNotBlank(schemaGenerationCreateScriptSource)){
			InputStream inputSteram = this.getClass().getResourceAsStream("/" + schemaGenerationCreateScriptSource);
			String script = IOUtils.toString(inputSteram, StandardCharsets.UTF_8);
			platkmEntityManager.createNativeQuery(script).executeUpdate();
		}
	}
	
	private void executeCreateMetaData(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager) throws ClassNotFoundException {
		 String createScript = createScriptFromMetadata(persistenceUnit);
		 if(StringUtils.isNotBlank(createScript)) {
			 platkmEntityManager.createNativeQuery(createScript).executeUpdate();
		 }
	}
 
	
	private void dropSchema(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager, Map map) throws IOException, ClassNotFoundException {
		String schemaGenerationDropSource = (String)persistenceUnit.getProperties().get(C_SCHEMA_GENERATION_DROP_SOURCE);
		if("script".equalsIgnoreCase(schemaGenerationDropSource)){
			 executeDropScript(persistenceUnit, map, platkmEntityManager); 
		}else if("metadata".equalsIgnoreCase(schemaGenerationDropSource)){
			executeDropMetaData(persistenceUnit, platkmEntityManager); 
		}if("metadata-then-script".equalsIgnoreCase(schemaGenerationDropSource)){
			executeDropMetaData(persistenceUnit, platkmEntityManager); 
			executeDropScript(persistenceUnit, map, platkmEntityManager); 
		}else if("script-then-metadata".equalsIgnoreCase(schemaGenerationDropSource)){
			executeDropScript(persistenceUnit, map, platkmEntityManager); 
			executeDropMetaData(persistenceUnit, platkmEntityManager); 
		}  
	}
 

	private void executeDropMetaData(PersistenceUnit<F> persistenceUnit, PlatkmEntityManager<F> platkmEntityManager) throws ClassNotFoundException {
		 String dropScript = dropScriptFromMetaData(persistenceUnit);
		 if(StringUtils.isNotBlank(dropScript)) {
			 platkmEntityManager.createNativeQuery(dropScript).executeUpdate();
		 }
		
	}

	private void executeDropScript(PersistenceUnit<F> persistenceUnit, Map map, PlatkmEntityManager<F> platkmEntityManager) throws IOException {
		String schemaGenerationDropScriptSource = (String)persistenceUnit.getProperties().get(C_SCHEMA_GENERATION_DROP_SCRIPT_SOURCE);
		if(StringUtils.isNotBlank(schemaGenerationDropScriptSource)){
			InputStream inputSteram = this.getClass().getResourceAsStream("/" + schemaGenerationDropScriptSource);
			String script = IOUtils.toString(inputSteram, StandardCharsets.UTF_8);
			platkmEntityManager.createNativeQuery(script).executeUpdate();
		}
	}
*/
	/**private DatabaseMapper databaseMapper;
	
	public String schemaFromEntities() {
		
		StringBuilder sb = new StringBuilder();
		Collection<Class<?>> entities = ObjectContainer.instance().getEntities();
		if(entities != null) {
			entities.forEach(cl-> {
					sb.append(classProcess(cl));
			});
		}
		 
		return sb.toString();  
		
	}
	 * @throws ClassNotFoundException */

	public String dropScriptFromMetaData(PersistenceUnit persistenceUnit) throws ClassNotFoundException {
		
		StringBuilder sb = new StringBuilder();
		if(persistenceUnit.getClasses() != null && !persistenceUnit.getClasses().isEmpty()) {
			Class<?> class1;
			String tableName;
			for (String strClass : persistenceUnit.getClasses()) { 
				class1 = Class.forName(strClass);
				if(class1.isAnnotationPresent(Entity.class)){ 
					tableName = class1.getSimpleName();
					
					if(class1.isAnnotationPresent(Table.class)){ 
						if(StringUtils.isNotBlank(class1.getAnnotation(Table.class).name())) {
							tableName = class1.getAnnotation(Table.class).name();
						}
					}
					sb.append("DROP TABLE IF EXISTS " +  tableName + ";");
				}
			}
		}
		return sb.toString();
	}

 
	/**
	 * TODO
	 * @param persistenceUnit
	 * @return
	 * @throws ClassNotFoundException
	 */
	public String createScriptFromMetadata(PersistenceUnit persistenceUnit) throws ClassNotFoundException {
		
		StringBuilder sb = new StringBuilder();
		/**
		if(persistenceUnit.getClasses() != null && !persistenceUnit.getClasses().isEmpty()) {
			String createTableScript;
			Class<?> class1;
			String primmaryKey = " AUTO_INCREMENT PRIMARY KEY ";
			String tableName;
			for (String strClass : persistenceUnit.getClasses()) { 
				class1 = Class.forName(strClass);
				if(class1.isAnnotationPresent(Entity.class)){ 
					
					tableName = class1.getSimpleName();
					
					if(class1.isAnnotationPresent(Table.class)){ 
						if(StringUtils.isNotBlank(class1.getAnnotation(Table.class).name())) {
							tableName = class1.getAnnotation(Table.class).name();
						}
					}
					createTableScript = "CREATE TABLE " +  tableName + "(";
					
					Field[] fields = class1.getDeclaredFields();
					if(fields != null) {
						Column column;
						String coma = "";
						for (int i = 0; i < fields.length; i++){
							if(fields[i].isAnnotationPresent(Column.class)){
								column = fields[i].getAnnotation(Column.class);
								if(fields[i].isAnnotationPresent(Id.class)){
									createTableScript+= coma + column.name() + " " + persistenceUnit.getDatabaseMapper().getDbTypeFromJavaType(fields[i].getType()) + " " + primmaryKey;
								}else {
									createTableScript+= coma + column.name() + " " + persistenceUnit.getDatabaseMapper().getDbTypeFromJavaType(fields[i].getType());
								}
								coma = ",";
							}
						}
					} 
					sb.append(createTableScript + ");");
				}
			}
		}*/
		return sb.toString();
	}
	
									
									
	





}
