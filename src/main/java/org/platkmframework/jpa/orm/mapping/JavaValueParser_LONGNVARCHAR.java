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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class JavaValueParser_LONGNVARCHAR extends BasicJavaValueParser<String> {

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return String
     * @throws SQLException SQLException
     */
    @Override
    public String readValue(ResultSet rs, int index, String... param) throws SQLException {
        return rs.getString(index);
    }

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return String
     * @throws SQLException SQLException
     */
    @Override
    public String readValue(ResultSet rs, String name, String... param) throws SQLException {
        return rs.getString(name);
    }

    /**
     * getSqlType
     * @return int
     */
    public int getSqlType() {
        return java.sql.Types.LONGNVARCHAR;
    }

    /**
     * getJavaType
     * @return Class
     */
    @Override
    public Class<String> getJavaType() {
        return String.class;
    }

    /**
     * isDefualtParseSqlTypeToJavaType
     * @return Boolean
     */
    @Override
    public Boolean isDefualtParseSqlTypeToJavaType() {
        return Boolean.TRUE;
    }

    /**
     * JavaValueParser_LONGNVARCHAR
     */
    public JavaValueParser_LONGNVARCHAR() {
        super();
    }
}
