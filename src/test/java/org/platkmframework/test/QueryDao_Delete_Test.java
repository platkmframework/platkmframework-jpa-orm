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
package org.platkmframework.test;
 
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse; 
import javax.xml.bind.JAXBException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test; 
import org.platkmframework.database.query.common.exception.DaoException;
import org.platkmframework.jpa.base.PlatkmORMEntityManager;
import org.platkmframework.jpa.persistence.PlatkmEntityManagerFactory;
import org.platkmframework.persistence.filter.criteria.DeleteCriteria;
import org.platkmframework.persistence.filter.criteria.SearchCriteria;

@Disabled
public class QueryDao_Delete_Test {
	
	static PlatkmORMEntityManager platkmEntityManager;
	static String testh2persistenceunit4 = "testh2persistenceunit4";
	static PlatkmEntityManagerFactory plakmEntityManagerFactory;
 
    @BeforeAll
    public static void setup() throws JAXBException { 
    	/**plakmEntityManagerFactory = (PlatkmEntityManagerFactory) PlatkmJpaUtil.getPlakmEntityManagerFactory(testh2persistenceunit4);
    	platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
    	platkmEntityManager.getTransaction().begin();*/
    }
   
	@Test
	public void queryDao_Remove(){
		try {
			int cant =  platkmEntityManager.getQueryDao().remove(new DeleteCriteria("person").eq("id",100));
			assertEquals(1,cant);
			Object object = platkmEntityManager.getQueryDao().select(new SearchCriteria().select("person").where().eq("id", 100), null);
			assertFalse(object == null);  
		} catch (DaoException e) { 
			e.printStackTrace();
		}
	}
	 
	
    @AfterAll
    public static void tearDown() {
    	platkmEntityManager.getTransaction().commit(); 
    	platkmEntityManager.close();  
    }

}
