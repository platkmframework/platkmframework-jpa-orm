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
package org.platkmframework.jpa.orm.persistence.ddl;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.jpa.dll.SchemaGenerator;
import org.platkmframework.jpa.persistence.PersistenceUnit;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class SchemaGeneratorImpl implements SchemaGenerator {

    /**
     * generateSchema
     * @param map map
     * @return boolean
     */
    @Override
    public boolean generateSchema(Map map) {
        return true;
        /**
         * 	PlatkmEntityManager<F> platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
         * 		try {
         * 			platkmEntityManager.getTransaction().begin();
         *
         * 			String schemaDatabaseAction = (String)plakmEntityManagerFactory.getPersistenceUnit().getProperties().get(C_SCHEMA_GENERATION_DATABASE_ACTION);
         * 			if("create".equalsIgnoreCase(schemaDatabaseAction)){
         * 				createSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
         * 			}else if("drop-and-create".equalsIgnoreCase(schemaDatabaseAction)){
         * 				dropSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
         * 				createSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
         * 			}if("drop".equalsIgnoreCase(schemaDatabaseAction)){
         * 				dropSchema(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
         * 			}
         *
         * 			executeLoadScriptSource(plakmEntityManagerFactory.getPersistenceUnit(), platkmEntityManager, map);
         *
         * 			platkmEntityManager.getTransaction().commit();
         * 			return true;
         *
         * 		} catch (ClassNotFoundException | IOException e) {
         * 			logger.error(e);
         * 			platkmEntityManager.getTransaction().rollback();
         * 			throw new PlatkmJpaException("Error updating schema");
         * 		}finally{
         * 			platkmEntityManager.close();
         * 			plakmEntityManagerFactory.returnObject(platkmEntityManager);
         * 		}
         */
    }

    /**
     * dropScriptFromMetaData
     * @param persistenceUnit persistenceUnit
     * @return String
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public String dropScriptFromMetaData(PersistenceUnit persistenceUnit) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        if (persistenceUnit.getClasses() != null && !persistenceUnit.getClasses().isEmpty()) {
            Class<?> class1;
            String tableName;
            for (String strClass : persistenceUnit.getClasses()) {
                class1 = Class.forName(strClass);
                if (class1.isAnnotationPresent(Entity.class)) {
                    tableName = class1.getSimpleName();
                    if (class1.isAnnotationPresent(Table.class)) {
                        if (StringUtils.isNotBlank(class1.getAnnotation(Table.class).name())) {
                            tableName = class1.getAnnotation(Table.class).name();
                        }
                    }
                    sb.append("DROP TABLE IF EXISTS " + tableName + ";");
                }
            }
        }
        return sb.toString();
    }

    /**
     * createScriptFromMetadata
     * @param persistenceUnit persistenceUnit
     * @return String
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public String createScriptFromMetadata(PersistenceUnit persistenceUnit) throws ClassNotFoundException {
        StringBuilder sb = new StringBuilder();
        /**
         * 		if(persistenceUnit.getClasses() != null && !persistenceUnit.getClasses().isEmpty()) {
         * 			String createTableScript;
         * 			Class<?> class1;
         * 			String primmaryKey = " AUTO_INCREMENT PRIMARY KEY ";
         * 			String tableName;
         * 			for (String strClass : persistenceUnit.getClasses()) {
         * 				class1 = Class.forName(strClass);
         * 				if(class1.isAnnotationPresent(Entity.class)){
         *
         * 					tableName = class1.getSimpleName();
         *
         * 					if(class1.isAnnotationPresent(Table.class)){
         * 						if(StringUtils.isNotBlank(class1.getAnnotation(Table.class).name())) {
         * 							tableName = class1.getAnnotation(Table.class).name();
         * 						}
         * 					}
         * 					createTableScript = "CREATE TABLE " +  tableName + "(";
         *
         * 					Field[] fields = class1.getDeclaredFields();
         * 					if(fields != null) {
         * 						Column column;
         * 						String coma = "";
         * 						for (int i = 0; i < fields.length; i++){
         * 							if(fields[i].isAnnotationPresent(Column.class)){
         * 								column = fields[i].getAnnotation(Column.class);
         * 								if(fields[i].isAnnotationPresent(Id.class)){
         * 									createTableScript+= coma + column.name() + " " + persistenceUnit.getDatabaseMapper().getDbTypeFromJavaType(fields[i].getType()) + " " + primmaryKey;
         * 								}else {
         * 									createTableScript+= coma + column.name() + " " + persistenceUnit.getDatabaseMapper().getDbTypeFromJavaType(fields[i].getType());
         * 								}
         * 								coma = ",";
         * 							}
         * 						}
         * 					}
         * 					sb.append(createTableScript + ");");
         * 				}
         * 			}
         * 		}
         */
        return sb.toString();
    }

    /**
     * SchemaGeneratorImpl
     */
    public SchemaGeneratorImpl() {
        super();
    }
}
