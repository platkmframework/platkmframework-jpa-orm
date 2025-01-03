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

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.platkmframework.content.project.ProjectContent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public final class JavaValueParser_TIMESTAMP extends BasicJavaValueParser<Timestamp> {

    /**
     * readValue
     * @param rs rs
     * @param index index
     * @param param param
     * @return Timestamp
     * @throws SQLException SQLException
     */
    @Override
    public Timestamp readValue(ResultSet rs, int index, String... param) throws SQLException {
        return rs.getTimestamp(index);
    }

    /**
     * readValue
     * @param rs rs
     * @param name name
     * @param param param
     * @return Timestamp
     * @throws SQLException SQLException
     */
    @Override
    public Timestamp readValue(ResultSet rs, String name, String... param) throws SQLException {
        return rs.getTimestamp(name);
    }

    /**
     * setValue
     * @param ps ps
     * @param index index
     * @param value value
     * @param param param
     * @throws SQLException SQLException
     */
    @Override
    public void setValue(PreparedStatement ps, int index, Object value, String... param) throws SQLException {
        if (value == null)
            ps.setNull(index, java.sql.Types.TIMESTAMP);
        if (value instanceof String) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(ProjectContent.instance().getDateTimeFormat());
            LocalDateTime localDateTime = LocalDateTime.from(formatter.parse(value.toString()));
            ps.setObject(index, Timestamp.valueOf(localDateTime));
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else
            ps.setNull(index, java.sql.Types.TIMESTAMP);
    }

    /**
     * getSqlType
     * @return int
     */
    @Override
    public int getSqlType() {
        return java.sql.Types.TIMESTAMP;
    }

    /**
     * getJavaType
     * @return Class
     */
    @Override
    public Class<Timestamp> getJavaType() {
        return Timestamp.class;
    }

    /**
     * JavaValueParser_TIMESTAMP
     */
    public JavaValueParser_TIMESTAMP() {
        super();
    }
}
