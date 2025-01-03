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
package org.platkmframework.jpa.orm.mapping;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 * @param <E> E
 */
public abstract class BasicJavaValueParser<E> {

    /**
     * BasicJavaValueParser
     */
    public BasicJavaValueParser() {
        super();
    }

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return E
     * @throws SQLException SQLException
     */
    public abstract E readValue(ResultSet rs, int index, String... param) throws SQLException;

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return E
     * @throws SQLException SQLException
     */
    public abstract E readValue(ResultSet rs, String name, String... param) throws SQLException;

    /**
     * customParse
     * @return Class
     */
    public Class<?> customParse() {
        return null;
    }

    /**
     * getSqlType
     * @return int
     */
    public abstract int getSqlType();

    /**
     * getJavaType
     * @return Class
     */
    public abstract Class<E> getJavaType();

    /**
     * isDefualtParseSqlTypeToJavaType
     * @return Boolean
     */
    public Boolean isDefualtParseSqlTypeToJavaType() {
        return false;
    }

    /**
     * setValue
     * @param ps ps
     * @param index index
     * @param value value
     * @param param param
     * @throws SQLException SQLException
     */
    public void setValue(PreparedStatement ps, int index, Object value, String... param) throws SQLException {
        ps.setObject(index, value);
    }
}
