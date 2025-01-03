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

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.platkmframework.jpa.orm.mapping.BasicJavaValueParser;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class JavaValueParser_CUSTOM_ASCIISTREAM extends BasicJavaValueParser<InputStream> {

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return InputStream
     * @throws SQLException SQLException
     */
    @Override
    public InputStream readValue(ResultSet rs, int index, String... param) throws SQLException {
        return rs.getAsciiStream(index);
    }

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return InputStream
     * @throws SQLException SQLException
     */
    @Override
    public InputStream readValue(ResultSet rs, String name, String... param) throws SQLException {
        return rs.getAsciiStream(name);
    }

    /**
     * getSqlType
     * @return int
     */
    @Override
    public int getSqlType() {
        return java.sql.Types.LONGVARCHAR;
    }

    /**
     * getJavaType
     * @return Class
     */
    @Override
    public Class<InputStream> getJavaType() {
        return InputStream.class;
    }

    /**
     * JavaValueParser_CUSTOM_ASCIISTREAM
     */
    public JavaValueParser_CUSTOM_ASCIISTREAM() {
        super();
    }
}
