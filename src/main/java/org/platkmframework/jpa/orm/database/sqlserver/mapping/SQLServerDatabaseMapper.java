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
package org.platkmframework.jpa.orm.database.sqlserver.mapping;

import org.platkmframework.annotation.db.DatabaseConfig;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
@DatabaseConfig(name = "com.microsoft.sqlserver.jdbc.SQLServerDriver")
public class SQLServerDatabaseMapper extends DatabaseMapperImpl {

    /**
     * Constructor SQLServerDatabaseMapper
     */
    public SQLServerDatabaseMapper() {
        super();
    }

    /**
     * getDatabaseName
     * @return String
     */
    @Override
    public String getDatabaseName() {
        return "mssqlserver";
    }

    /**
     * getVersion
     * @return String
     */
    @Override
    public String getVersion() {
        return "SQL Server 2022";
    }

    /**
     * initSqlDataBase
     */
    @Override
    protected void initSqlDataBase() {
        this.defaultColumnTypes.put(java.sql.Types.ARRAY, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.BIGINT, "[Bigint]");
        this.defaultColumnTypes.put(java.sql.Types.BIT, "[bit]");
        this.defaultColumnTypes.put(java.sql.Types.BLOB, "[BINARY](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.BOOLEAN, "[bit]");
        this.defaultColumnTypes.put(java.sql.Types.CHAR, "[VARCHAR](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.NCHAR, "[VARCHAR](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.CLOB, "[BINARY](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.DATALINK, "[BINARY](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.DATE, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.DECIMAL, "[float]");
        this.defaultColumnTypes.put(java.sql.Types.DISTINCT, "[BINARY](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.DOUBLE, "[decimal](${precision},${scale})");
        this.defaultColumnTypes.put(java.sql.Types.FLOAT, "[float]");
        this.defaultColumnTypes.put(java.sql.Types.INTEGER, "[int]");
        this.defaultColumnTypes.put(java.sql.Types.INTEGER, "[int]");
        this.defaultColumnTypes.put(java.sql.Types.LONGNVARCHAR, "[varchar](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.LONGVARBINARY, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.LONGVARCHAR, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.NVARCHAR, "[varchar](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.NCLOB, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.NULL, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.NUMERIC, "[bigint]");
        this.defaultColumnTypes.put(java.sql.Types.NUMERIC, "[decimal](${precision},${scale})");
        this.defaultColumnTypes.put(java.sql.Types.NVARCHAR, "[varchar](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.OTHER, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.REF_CURSOR, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.REAL, "[float]");
        this.defaultColumnTypes.put(java.sql.Types.REF, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.ROWID, "[bigint]");
        this.defaultColumnTypes.put(java.sql.Types.SMALLINT, "[smallint]");
        this.defaultColumnTypes.put(java.sql.Types.SQLXML, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.STRUCT, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.TIME_WITH_TIMEZONE, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.TIME, "[time](${precision})");
        this.defaultColumnTypes.put(java.sql.Types.TIMESTAMP, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.TIMESTAMP_WITH_TIMEZONE, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.TIMESTAMP, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.TINYINT, "[smallint]");
        this.defaultColumnTypes.put(java.sql.Types.VARBINARY, "[varbinary](max)");
        this.defaultColumnTypes.put(java.sql.Types.VARCHAR, "[varchar](${precision})");
    }
}
