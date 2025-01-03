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

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class DbType {

    /**
     * Atributo name
     */
    private String name;

    /**
     * Atributo minLength
     */
    private Long minLength;

    /**
     * Atributo maxLength
     */
    private Long maxLength;

    /**
     * Atributo defaultLength
     */
    private Long defaultLength;

    /**
     * Atributo admitsParameter
     */
    boolean admitsParameter = false;

    /**
     * Atributo defaultType
     */
    boolean defaultType = false;

    /**
     * Atributo javaSQLtype
     */
    private int javaSQLtype;

    /**
     * Constructor DbType
     * @param name name
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     */
    public DbType(String name, Long minLength, Long maxLength, Long defaultLength) {
        this(name, minLength, maxLength, defaultLength, false);
    }

    /**
     * Constructor DbType
     * @param name name
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     */
    public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter) {
        this(name, minLength, maxLength, defaultLength, admitsParameter, false);
    }

    /**
     * Constructor DbType
     * @param name name
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     * @param defaultType defaultType
     */
    public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType) {
        super();
        this.name = name;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.defaultLength = defaultLength;
        this.admitsParameter = admitsParameter;
        this.defaultType = defaultType;
    }

    /**
     * Constructor DbType
     * @param name name
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     * @param defaultType defaultType
     * @param javaSQLtype javaSQLtype
     */
    public DbType(String name, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType, int javaSQLtype) {
        this(name, minLength, maxLength, defaultLength, admitsParameter, defaultType);
        this.javaSQLtype = javaSQLtype;
    }

    /**
     * getName
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * setName
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getMinLength
     * @return Long
     */
    public Long getMinLength() {
        return minLength;
    }

    /**
     * setMinLength
     * @param minLength minLength
     */
    public void setMinLength(Long minLength) {
        this.minLength = minLength;
    }

    /**
     * getMaxLength
     * @return Long
     */
    public Long getMaxLength() {
        return maxLength;
    }

    /**
     * setMaxLength
     * @param maxLength maxLength
     */
    public void setMaxLength(Long maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * getDefaultLength
     * @return Long
     */
    public Long getDefaultLength() {
        return defaultLength;
    }

    /**
     * setDefaultLength
     * @param defaultLength defaultLength
     */
    public void setDefaultLength(Long defaultLength) {
        this.defaultLength = defaultLength;
    }

    /**
     * isAdmitsParameter
     * @return boolean
     */
    public boolean isAdmitsParameter() {
        return admitsParameter;
    }

    /**
     * setAdmitsParameter
     * @param admitsParameter admitsParameter
     */
    public void setAdmitsParameter(boolean admitsParameter) {
        this.admitsParameter = admitsParameter;
    }

    /**
     * isDefaultType
     * @return boolean
     */
    public boolean isDefaultType() {
        return defaultType;
    }

    /**
     * setDefaultType
     * @param defaultType defaultType
     */
    public void setDefaultType(boolean defaultType) {
        this.defaultType = defaultType;
    }

    /**
     * getJavaSQLtype
     * @return int
     */
    public int getJavaSQLtype() {
        return javaSQLtype;
    }

    /**
     * setJavaSQLtype
     * @param javaSQLtype javaSQLtype
     */
    public void setJavaSQLtype(int javaSQLtype) {
        this.javaSQLtype = javaSQLtype;
    }
}
