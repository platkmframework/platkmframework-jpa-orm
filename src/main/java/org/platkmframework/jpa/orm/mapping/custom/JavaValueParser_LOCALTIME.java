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

import java.time.LocalTime;
import org.platkmframework.jpa.orm.mapping.BasicJavaValueParser;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class JavaValueParser_LOCALTIME extends BasicJavaValueParser<LocalTime> {

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return LocalTime
     * @throws SQLException SQLException
     */
    @Override
    public LocalTime readValue(ResultSet rs, int index, String... param) throws SQLException {
        java.sql.Time value = rs.getTime(index);
        if (value == null)
            return null;
        return value.toLocalTime();
    }

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return LocalTime
     * @throws SQLException SQLException
     */
    @Override
    public LocalTime readValue(ResultSet rs, String name, String... param) throws SQLException {
        java.sql.Time value = rs.getTime(name);
        if (value == null)
            return null;
        return value.toLocalTime();
    }

    /**
     * customParse
     * @return Class
     */
    @Override
    public Class<?> customParse() {
        return java.time.LocalTime.class;
    }

    /**
     * getSqlType
     * @return int
     */
    @Override
    public int getSqlType() {
        return java.sql.Types.TIME;
    }

    /**
     * getJavaType
     * @return Class
     */
    @Override
    public Class<LocalTime> getJavaType() {
        return LocalTime.class;
    }

    /**
     * JavaValueParser_LOCALTIME
     */
    public JavaValueParser_LOCALTIME() {
        super();
    }
}
