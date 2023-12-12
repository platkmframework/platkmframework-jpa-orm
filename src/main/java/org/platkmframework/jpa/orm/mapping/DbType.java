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



/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class DbType {
	 
	private String name;
	private Long minLength;
	private Long maxLength;
	private Long defaultLength; 
	boolean admitsParameter = false;
	boolean defaultType = false;
	private int javaSQLtype; 
 
	public DbType(String name, Long minLength, Long maxLength, Long defaultLength) {
		this(name, minLength, maxLength, defaultLength, false); 
	}
	
	public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter) {
		this(name, minLength, maxLength, defaultLength, admitsParameter, false);
	}
	
	public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType) {
		super();
		this.name = name;
		this.minLength = minLength;
		this.maxLength = maxLength;
		this.defaultLength = defaultLength; 
		this.admitsParameter = admitsParameter;
		this.defaultType = defaultType;
	}
	
	public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType, int javaSQLtype) {
		this(name, minLength, maxLength, defaultLength, admitsParameter, defaultType);
		this.javaSQLtype = javaSQLtype;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Long getMinLength() {
		return minLength;
	}

	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}

	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	public Long getDefaultLength() {
		return defaultLength;
	}

	public void setDefaultLength(Long defaultLength) {
		this.defaultLength = defaultLength;
	}

	public boolean isAdmitsParameter() {
		return admitsParameter;
	}

	public void setAdmitsParameter(boolean admitsParameter) {
		this.admitsParameter = admitsParameter;
	}

	public boolean isDefaultType() {
		return defaultType;
	}

	public void setDefaultType(boolean defaultType) {
		this.defaultType = defaultType;
	}

	public int getJavaSQLtype() {
		return javaSQLtype;
	}

	public void setJavaSQLtype(int javaSQLtype) {
		this.javaSQLtype = javaSQLtype;
	} 
  
}
