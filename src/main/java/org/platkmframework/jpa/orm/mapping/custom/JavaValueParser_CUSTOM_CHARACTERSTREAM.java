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
package org.platkmframework.jpa.orm.mapping.custom;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.platkmframework.jpa.orm.mapping.BasicJavaValueParser;

public final class JavaValueParser_CUSTOM_CHARACTERSTREAM extends BasicJavaValueParser<Reader> {

	@Override
	public Reader readValue(ResultSet rs, int index, String... param) throws SQLException { 
		return rs.getCharacterStream(index);
	}

	@Override
	public Reader readValue(ResultSet rs, String name, String... param) throws SQLException {
		return rs.getCharacterStream(name);
	}
	
	@Override
	public int getSqlType() { 
		return java.sql.Types.LONGVARCHAR;
	} 

	@Override
	public Class<Reader> getJavaType() {
		return Reader.class;
	}
}
