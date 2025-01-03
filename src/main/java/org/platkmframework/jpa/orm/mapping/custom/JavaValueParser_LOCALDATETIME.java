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
package org.platkmframework.jpa.orm.mapping.custom;

import java.time.LocalDateTime;
import org.platkmframework.jpa.orm.mapping.BasicJavaValueParser;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class JavaValueParser_LOCALDATETIME extends BasicJavaValueParser<LocalDateTime> {

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return LocalDateTime
     * @throws SQLException SQLException
     */
    @Override
    public LocalDateTime readValue(ResultSet rs, int index, String... param) throws SQLException {
        java.sql.Timestamp value = rs.getTimestamp(index);
        if (value == null)
            return null;
        return value.toLocalDateTime();
    }

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return LocalDateTime
     * @throws SQLException SQLException
     */
    @Override
    public LocalDateTime readValue(ResultSet rs, String name, String... param) throws SQLException {
        java.sql.Timestamp value = rs.getTimestamp(name);
        if (value == null)
            return null;
        return value.toLocalDateTime();
    }

    /**
     * customParse
     * @return Class
     */
    @Override
    public Class<?> customParse() {
        return java.time.LocalDateTime.class;
    }

    /**
     * getSqlType
     * @return int
     */
    public int getSqlType() {
        return java.sql.Types.DATE;
    }

    /**
     * getJavaType
     * @return Class
     */
    @Override
    public Class<LocalDateTime> getJavaType() {
        return LocalDateTime.class;
    }

    /**
     * JavaValueParser_LOCALDATETIME
     */
    public JavaValueParser_LOCALDATETIME() {
        super();
    }
}
