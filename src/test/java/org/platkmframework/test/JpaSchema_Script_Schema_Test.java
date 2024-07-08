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
import org.junit.jupiter.api.Test;
import org.platkmframework.jpa.base.PlatkmORMEntityManager; 


public class JpaSchema_Script_Schema_Test { 
	
	 static final String testh2persistenceunit   = "testh2persistenceunit";
	 static final String testh2persistenceunit_2 = "testh2persistenceunit_2";
	 
	 static PlatkmORMEntityManager platkmEntityManager1;
	 static PlatkmORMEntityManager platkmEntityManager2;
	 
	 
	 @BeforeAll
	 public static void init() {
	/**	 PlakmEntityManagerFactory plakmEntityManagerFactory = PlatkmJpaUtil.getPlakmEntityManagerFactory(testh2persistenceunit);
		 platkmEntityManager1 = plakmEntityManagerFactory.createEntityManager();
		 
		 plakmEntityManagerFactory = PlatkmJpaUtil.getPlakmEntityManagerFactory(testh2persistenceunit_2);
		 platkmEntityManager2 = plakmEntityManagerFactory.createEntityManager(); 
		*/
	 }
	
	@Test
	public void dropCreateInsertWithScripts(){  
		//dropCreateInsert(platkmEntityManager1); 
	}
	
	@Test
	public void dropCreateInsertWithSchema() {
	//	dropCreateInsert(platkmEntityManager2); 
	}
	
	public void dropCreateInsert(PlatkmORMEntityManager platkmEntityManager){ 
	/**	 
		platkmEntityManager.getTransaction().begin();
		
		//search native query
		Query query = platkmEntityManager.createNativeQuery("select * from person",PersonVO.class);
		List<?> list = query.getResultList();
		
		assertFalse(list.isEmpty()); 
		assertEquals(list.size(), 1);
		
		assertTrue((list.get(0) instanceof PersonVO));
		PersonVO personVO = (PersonVO) list.get(0); 
		
		assertTrue(personVO.getCountryNumber() != null);
		assertEquals(personVO.getCountryNumber().intValue(),(int)35455);
		 
		//insert
		Person person = new Person();
		person.setCountryNumber(22);
		person.setFullName("Ernesto Mart�nez G�mez");
		person.setRegisterDate(LocalDateTime.now());
		person.setSalary((double) 30000.91);
		
		platkmEntityManager.persist(person);
		assertTrue(person.getId() != null);
		
		//load
		Query query1 = platkmEntityManager.createQuery("select * from person where id=?", Person.class);
		query1.setParameter(1, person.getId());
		Person person1 = (Person) query1.getSingleResult();
		assertTrue(person.getId().longValue() == person1.getId().longValue());
		
		//udpate
		person1.setCountryNumber(33);
		platkmEntityManager.persist(person1);
		
		query1 = platkmEntityManager.createQuery("select * from person where id=?", Person.class);
		query1.setParameter(1, person1.getId());
		person1 = (Person) query1.getSingleResult();
		assertTrue(person1.getCountryNumber().intValue() == 33);
		
		//delete, find, null
		platkmEntityManager.remove(person1);
		person1 = platkmEntityManager.find(Person.class, person1.getId()); 
		assertTrue(person1 == null);
		
		platkmEntityManager.getTransaction().commit();
		platkmEntityManager.close(); 
		*/
	}



}
