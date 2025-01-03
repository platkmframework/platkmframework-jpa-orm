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
package org.platkmframework.jpa.orm.database.postgresql.mapping;

import org.platkmframework.annotation.db.DatabaseConfig;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
@DatabaseConfig(name = "org.postgresql.Driver")
public class PostgreSQLDatabaseMapper extends DatabaseMapperImpl {

    /**
     * Constructor PostgreSQLDatabaseMapper
     */
    public PostgreSQLDatabaseMapper() {
        super();
    }

    /**
     * getDatabaseName
     * @return String
     */
    @Override
    public String getDatabaseName() {
        return "postgresql";
    }

    /**
     * getVersion
     * @return String
     */
    @Override
    public String getVersion() {
        return "PostgreSQL 15";
    }

    /**
     * initSqlDataBase
     */
    @Override
    protected void initSqlDataBase() {
        this.defaultColumnTypes.put(java.sql.Types.ARRAY, "TEXT");
        this.defaultColumnTypes.put(java.sql.Types.BIGINT, "BIGINT");
        this.defaultColumnTypes.put(java.sql.Types.BIT, "bit(${precision}) ");
        this.defaultColumnTypes.put(java.sql.Types.BLOB, "BLOB");
        this.defaultColumnTypes.put(java.sql.Types.BOOLEAN, "boolean");
        this.defaultColumnTypes.put(java.sql.Types.CHAR, "CHAR(${length})");
        this.defaultColumnTypes.put(java.sql.Types.CLOB, "TEXT");
        this.defaultColumnTypes.put(java.sql.Types.DATALINK, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.DATE, "timestamp");
        this.defaultColumnTypes.put(java.sql.Types.DECIMAL, "decimal(${precision},${scale})");
        this.defaultColumnTypes.put(java.sql.Types.DISTINCT, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.DOUBLE, "float8");
        this.defaultColumnTypes.put(java.sql.Types.FLOAT, "float4");
        this.defaultColumnTypes.put(java.sql.Types.INTEGER, "int");
        this.defaultColumnTypes.put(java.sql.Types.LONGNVARCHAR, "varchar (${length})");
        this.defaultColumnTypes.put(java.sql.Types.LONGVARBINARY, "text");
        this.defaultColumnTypes.put(java.sql.Types.LONGVARCHAR, "text");
        this.defaultColumnTypes.put(java.sql.Types.NVARCHAR, "varchar(${length})");
        this.defaultColumnTypes.put(java.sql.Types.NCLOB, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.NULL, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.NUMERIC, "decimal(${precision},${scale})");
        this.defaultColumnTypes.put(java.sql.Types.NVARCHAR, "varchar(${length})");
        this.defaultColumnTypes.put(java.sql.Types.OTHER, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.REF_CURSOR, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.REAL, "float4");
        this.defaultColumnTypes.put(java.sql.Types.REF, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.ROWID, "bytea");
        this.defaultColumnTypes.put(java.sql.Types.SMALLINT, "int2");
        this.defaultColumnTypes.put(java.sql.Types.SQLXML, "TEXT");
        this.defaultColumnTypes.put(java.sql.Types.STRUCT, "TEXT");
        this.defaultColumnTypes.put(java.sql.Types.TIME_WITH_TIMEZONE, "time(8)");
        this.defaultColumnTypes.put(java.sql.Types.TIME, "time(12)");
        this.defaultColumnTypes.put(java.sql.Types.TIMESTAMP, "timestamp");
        this.defaultColumnTypes.put(java.sql.Types.TIMESTAMP_WITH_TIMEZONE, "[datetime]");
        this.defaultColumnTypes.put(java.sql.Types.TINYINT, "int");
        this.defaultColumnTypes.put(java.sql.Types.VARBINARY, "varchar(${length})");
        this.defaultColumnTypes.put(java.sql.Types.VARCHAR, "varchar(${length})");
    }
}
