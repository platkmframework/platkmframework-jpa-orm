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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public final class JavaValueParser_BIGINT extends BasicJavaValueParser<Long> {

	@Override
	public Long readValue(ResultSet rs, int index, String... param) throws SQLException { 
		Object val = rs.getObject(index);
		if( val == null) return null;
		return Long.valueOf(val.toString());
	}

	@Override
	public Long readValue(ResultSet rs, String name, String... param) throws SQLException {
		Object val = rs.getObject(name);
		if( val == null) return null;
		return Long.valueOf(val.toString());
	}
	  
	@Override
	public void setValue(PreparedStatement ps, int index, Object value, String... param) throws SQLException {
		if(value == null) ps.setNull(index, java.sql.Types.BIGINT); else ps.setLong(index, Long.valueOf(value.toString()));
	}
  
	public int getSqlType() { 
		return java.sql.Types.BIGINT;
	} 

	@Override
	public Class<Long> getJavaType() {  
		return Long.class;
	}
	
	@Override
	public Boolean isDefualtParseSqlTypeToJavaType() { 
		return Boolean.TRUE;
	}
}
