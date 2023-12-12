/*******************************************************************************
 * Copyright(c) 2023 the original author Eduardo Iglesias Taylor.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	 https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Eduardo Iglesias Taylor - initial API and implementation
 *******************************************************************************/
package org.platkmframework.jpa.orm.mapping;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JavaValueParser_TIMESTAMP_LocalDateTime extends BasicJavaValueParser<LocalDateTime> {

	@Override
	public LocalDateTime readValue(ResultSet rs, int index, String... param) throws SQLException { 
		java.sql.Timestamp value = rs.getTimestamp(index);   
		if(value == null) return null;
		return value.toLocalDateTime();
	}

	@Override
	public LocalDateTime readValue(ResultSet rs, String name, String... param) throws SQLException {
		java.sql.Timestamp value = rs.getTimestamp(name);
		if(value == null) return null;
		return  value.toLocalDateTime(); 
	}
	
	public void setValue(PreparedStatement ps, int index, Object value, String... param) throws SQLException {
		
		if(value == null) ps.setNull(index, java.sql.Types.TIMESTAMP);
		
		if(value instanceof String) {
			ps.setObject(index, Timestamp.valueOf(value.toString()));
		}else if(value instanceof Timestamp) {
			ps.setTimestamp(index, (Timestamp)value); 
		}else if(value instanceof LocalDateTime) {
			ps.setTimestamp(index, java.sql.Timestamp.valueOf((LocalDateTime)value)); 
		}else
			ps.setNull(index, java.sql.Types.TIMESTAMP);
		
	}
	
	public int getSqlType() { 
		return java.sql.Types.TIMESTAMP;
	}

	@Override
	public Class<LocalDateTime> getJavaType() {
		return LocalDateTime.class;
	}
	
	@Override
	public Boolean isDefualtParseSqlTypeToJavaType() { 
		return Boolean.TRUE;
	}
}
