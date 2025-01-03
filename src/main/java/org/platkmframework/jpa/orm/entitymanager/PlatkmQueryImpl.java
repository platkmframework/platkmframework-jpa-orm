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
package org.platkmframework.jpa.orm.entitymanager;

import java.sql.PreparedStatement;
import java.util.Map;
import org.platkmframework.jpa.base.PlatkmQuery;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class PlatkmQueryImpl extends QueryImpl implements PlatkmQuery {

    /**
     * Atributo page
     */
    private int page;

    /**
     * Atributo pageCount
     */
    private int pageCount;

    /**
     * Constructor PlatkmQueryImpl
     * @param preparedStatement preparedStatement
     * @param returnClass returnClass
     * @param map map
     * @param databaseMapper databaseMapper
     */
    protected PlatkmQueryImpl(PreparedStatement preparedStatement, Class<?> returnClass, Map map, DatabaseMapperImpl databaseMapper) {
        super(preparedStatement, returnClass, map, databaseMapper);
    }

    /**
     * Constructor PlatkmQueryImpl
     * @param prepareStatement prepareStatement
     * @param returnClass returnClass
     * @param page page
     * @param pageCount pageCount
     * @param map map
     * @param databaseMapper databaseMapper
     */
    protected PlatkmQueryImpl(PreparedStatement prepareStatement, Class returnClass, int page, int pageCount, Map map, DatabaseMapperImpl databaseMapper) {
        this(prepareStatement, returnClass, map, databaseMapper);
        this.page = page;
        this.pageCount = pageCount;
    }

    /**
     * getPage
     * @return int
     */
    @Override
    public int getPage() {
        return this.page;
    }

    /**
     * getPageCount
     * @return long
     */
    @Override
    public long getPageCount() {
        return this.pageCount;
    }
}
