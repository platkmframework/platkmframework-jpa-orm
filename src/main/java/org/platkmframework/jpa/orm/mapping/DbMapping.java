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

import java.util.ArrayList;
import java.util.List;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 * @param <E> E
 */
public abstract class DbMapping<E> {

    /**
     * Atributo javatype
     */
    private Class<E> javatype;

    /**
     * Atributo dbtypes
     */
    List<DbType> dbtypes;

    /**
     * Atributo arrayFormat
     */
    boolean arrayFormat = false;

    /**
     * DbMapping
     */
    public DbMapping() {
        super();
    }

    /**
     * addJavaType
     * @param javatype javatype
     * @return DbMapping
     */
    public DbMapping<E> addJavaType(Class<E> javatype) {
        addJavaType(javatype, false);
        return this;
    }

    /**
     * addJavaType
     * @param javatype javatype
     * @param arrayFormat arrayFormat
     * @return DbMapping
     */
    public DbMapping<E> addJavaType(Class<E> javatype, boolean arrayFormat) {
        this.javatype = javatype;
        this.arrayFormat = arrayFormat;
        return this;
    }

    /**
     * addType
     * @param typename typename
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @return DbMapping
     */
    public DbMapping<E> addType(String typename, Long minLength, Long maxLength, Long defaultLength) {
        getDbtypes().add(new DbType(typename, minLength, maxLength, defaultLength));
        return this;
    }

    /**
     * addType
     * @param typename typename
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     * @return DbMapping
     */
    public DbMapping<E> addType(String typename, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter) {
        getDbtypes().add(new DbType(typename, minLength, maxLength, defaultLength, admitsParameter));
        return this;
    }

    /**
     * addType
     * @param typename typename
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     * @param defaultType defaultType
     * @return DbMapping
     */
    public DbMapping<E> addType(String typename, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType) {
        getDbtypes().add(new DbType(typename, minLength, maxLength, defaultLength, admitsParameter, defaultType));
        return this;
    }

    /**
     * addType
     * @param typename typename
     * @param minLength minLength
     * @param maxLength maxLength
     * @param defaultLength defaultLength
     * @param admitsParameter admitsParameter
     * @param defaultType defaultType
     * @param javaSQLtype javaSQLtype
     * @return DbMapping
     */
    public DbMapping<E> addType(String typename, Long minLength, Long maxLength, Long defaultLength, boolean admitsParameter, boolean defaultType, int javaSQLtype) {
        getDbtypes().add(new DbType(typename, minLength, maxLength, defaultLength, admitsParameter, defaultType, javaSQLtype));
        return this;
    }

    /**
     * getJavatype
     * @return Class
     */
    public Class<E> getJavatype() {
        return javatype;
    }

    /**
     * setJavatype
     * @param javatype javatype
     */
    public void setJavatype(Class<E> javatype) {
        this.javatype = javatype;
    }

    /**
     * getDbtypes
     * @return List
     */
    public List<DbType> getDbtypes() {
        if (dbtypes == null)
            dbtypes = new ArrayList<>();
        return dbtypes;
    }

    /**
     * setDbtypes
     * @param dbtypes dbtypes
     */
    public void setDbtypes(List<DbType> dbtypes) {
        this.dbtypes = dbtypes;
    }

    /**
     * isArrayFormat
     * @return boolean
     */
    public boolean isArrayFormat() {
        return arrayFormat;
    }

    /**
     * setArrayFormat
     * @param arrayFormat arrayFormat
     */
    public void setArrayFormat(boolean arrayFormat) {
        this.arrayFormat = arrayFormat;
    }

    /**
     * valueOf
     * @param value value
     * @return E
     */
    public abstract E valueOf(Object value);
}
