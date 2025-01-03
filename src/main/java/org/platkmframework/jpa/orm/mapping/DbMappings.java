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
 */
public class DbMappings {

    /**
     * DbMappings
     */
    private List<DbMapping> h2mapping;

    /**
     * getH2mapping
     * @return List
     */
    public List<DbMapping> getH2mapping() {
        if (h2mapping == null)
            h2mapping = new ArrayList<>();
        return h2mapping;
    }

    /**
     * setH2mapping
     * @param h2mapping h2mapping
     */
    public void setH2mapping(List<DbMapping> h2mapping) {
        this.h2mapping = h2mapping;
    }

    /**
     * Constructor DbMappings
     */
    public DbMappings() {
        super();
    }
}
