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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.platkmframework.common.domain.filter.criteria.SearchCriteria;
import org.platkmframework.database.query.common.exception.DaoException;
import org.platkmframework.jpa.base.PlatkmORMEntityManager;
import org.platkmframework.jpa.persistence.PlatkmEntityManagerFactory; 

@Disabled
public class QueryDao_Test {
	
	static PlatkmORMEntityManager platkmEntityManager;
	static String testh2persistenceunit3 = "testh2persistenceunit3";
	static PlatkmEntityManagerFactory plakmEntityManagerFactory;
 
    @BeforeAll
    public static void setup() { 
    	plakmEntityManagerFactory = (PlatkmEntityManagerFactory) PlatkmJpaUtil.getPlakmEntityManagerFactory(testh2persistenceunit3);
    	platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
    	platkmEntityManager.getTransaction().begin();
    }
 /**   
	@Test
	public void queryDaoRemove(){
		platkmEntityManager.getQueryDao().remove(null);
	}
	*/
	@Test
	public void queryDaoSearch(){
	/**	List<Object> params = null;  
		try {
		   FilterResult<PersonVO>  filterResult = platkmEntityManager.getQueryDao().search(new SearchCriteria().select("person").where().eq("id", 100), null, PersonVO.class);
		   assertTrue(filterResult.getList().size()>0);
		} catch (DaoException e) { 
			e.printStackTrace();
		}*/
	}
	
	@Test
	public void queryDaoSelect() {
		try {
			Object object = platkmEntityManager.getQueryDao().select(new SearchCriteria().select("person"), null);
			assertTrue(object != null);
			assertTrue(object instanceof List);
			
			List list = (List) object;
			assertTrue(list.size() > 0);
			assertTrue(list.get(0) instanceof Object[]);
			
			Object[] row = (Object[]) list.get(0);
			Integer id = Integer.valueOf(row[0].toString()); 
			assertTrue(id == (int)100);
			
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void queryDaoSelect_returnClass() {
		/**
		 * try {
			List<PersonVO> personList = platkmEntityManager.getQueryDao().select(new SearchCriteria().select("person"), null, PersonVO.class);
			assertTrue(personList.size() > 0);
			assertTrue(personList.get(0) instanceof PersonVO);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}	

	@Test
	public void queryDaoSelectOne() throws DaoException {
		/**PersonVO personVO = platkmEntityManager.getQueryDao().selectOne(new SearchCriteria().select("person"), null, PersonVO.class);
		assertTrue(personVO != null);
		assertTrue(personVO.getId() == (long)100);*/
	}	
	
    @AfterAll
    public static void tearDown() {
    	platkmEntityManager.getTransaction().commit(); 
    	platkmEntityManager.close();  
    }

}
