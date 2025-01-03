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
package org.platkmframework.jpa.orm.database.oracle.mapping;

import org.platkmframework.annotation.db.DatabaseConfig;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
@DatabaseConfig(name = "oracle.jdbc.driver.OracleDriver")
public class OracleDatabaseMapper extends DatabaseMapperImpl {

    /**
     * Constructor OracleDatabaseMapper
     */
    public OracleDatabaseMapper() {
        super();
    }

    /**
     * getVersion
     * @return String
     */
    @Override
    public String getVersion() {
        return null;
    }

    /**
     * initSqlDataBase
     */
    @Override
    protected void initSqlDataBase() {
        // TODO Auto-generated method stub
    }

    /**
     * getDatabaseName
     * @return String
     */
    @Override
    public String getDatabaseName() {
        return "oracle";
    }
}
