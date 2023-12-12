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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.platkmframework.jpa.base.PlatkmEntityManager;
import org.platkmframework.jpa.persistence.PlatkmEntityManagerFactory; 

@Disabled
public class QueryManagerDao_Test {
	
	static PlatkmEntityManager platkmEntityManager;
	static String testh2persistenceunit5 = "testh2persistenceunit5";
	static PlatkmEntityManagerFactory plakmEntityManagerFactory;
 
    @BeforeAll
    public static void setup(){  
    /**	plakmEntityManagerFactory = (PlatkmOrmEntityManagerFactory) PlatkmJpaUtil.getPlakmEntityManagerFactory(testh2persistenceunit5);
    	platkmEntityManager = plakmEntityManagerFactory.createEntityManager();
    	platkmEntityManager.getTransaction().begin();*/
    }
 
 /**
	@Test
	public void queryManagerDao_selectOne() throws DaoException{
		Object object = platkmEntityManager.getQueryManagerDao().
						selectOne(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
								new WhereCriteria().
								addArguments("tableColumns","p.ID, p.FULL_NAME").
								addArguments("tablename","PERSON").
								addArguments("tableAlias","p"), null);
		
		assertTrue(object != null);
		assertTrue(object instanceof Object[]);
		assertTrue("100".equals((((Object[])object)[0]).toString()));
		
		CustomPersonVO customPersonVO = platkmEntityManager.getQueryManagerDao().
				selectOne(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
						new WhereCriteria().
						addArguments("tableColumns","p.ID, p.FULL_NAME").
						addArguments("tablename","PERSON").
						addArguments("tableAlias","p"), null, CustomPersonVO.class);
		
		
		
		assertTrue(customPersonVO != null); 
		assertEquals((long)100, customPersonVO.getId().longValue()); 
		
	}
	
	
	@Test
	public void queryManagerDao_search() throws DaoException{
		FilterResult<List<Object>>  filterResult = platkmEntityManager.getQueryManagerDao().
									search(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
											new WhereCriteria().
											addArguments("tableColumns","p.ID, p.FULL_NAME").
											addArguments("tablename","PERSON").
											addArguments("tableAlias","p"), null);
		
		assertTrue(filterResult != null);
		assertTrue(filterResult.getList().size()>0); 
		
		FilterResult<CustomPersonVO>  filterResultClassResult = platkmEntityManager.getQueryManagerDao().
				search(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
						new WhereCriteria().
						addArguments("tableColumns","p.ID, p.FULL_NAME").
						addArguments("tablename","PERSON").
						addArguments("tableAlias","p"), null, CustomPersonVO.class);
		
		
		
		assertTrue(filterResultClassResult != null); 
		assertTrue(filterResultClassResult.getList().size()>0); 
		assertEquals(filterResultClassResult.getList().get(0).getId().longValue(), (long)100);  
		
	}
	
	
	@Test
	public void queryManagerDao_select() throws DaoException{
		List<Object>  list = platkmEntityManager.getQueryManagerDao().
									select(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
											new WhereCriteria().
											addArguments("tableColumns","p.ID, p.FULL_NAME").
											addArguments("tablename","PERSON").
											addArguments("tableAlias","p"), null);
		
		assertTrue(list != null);
		assertTrue(list.size()>0); 
		
		List<CustomPersonVO>  listResult = platkmEntityManager.getQueryManagerDao().
				select(QueryManager.SELECT_TOP_CUSTOM_TABLE, 
						new WhereCriteria().
						addArguments("tableColumns","p.ID, p.FULL_NAME").
						addArguments("tablename","PERSON").
						addArguments("tableAlias","p"), null, CustomPersonVO.class);
		
		assertTrue(listResult != null); 
		assertTrue(listResult.size()>0); 
		assertEquals(listResult.get(0).getId().longValue(), (long)100);  
		
	}
	
    @AfterAll
    public static void tearDown() {
    	platkmEntityManager.getTransaction().commit(); 
    	platkmEntityManager.close();  
    }
 */
}
