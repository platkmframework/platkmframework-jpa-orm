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
package org.platkmframework.jpa.orm.factory;
  

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.platkmframework.jpa.base.PlatkmORMEntityManager;
import org.platkmframework.jpa.orm.entitymanager.PlatkmEntityManagerImpl;
import org.platkmframework.jpa.orm.persistence.ORMPersistenceUnit;
import org.platkmframework.util.DataTypeUtil; 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PlatkmPooledEntityManagerFactory extends BasePooledObjectFactory<PlatkmORMEntityManager>{

	private BasicDataSource ds;
	private ORMPersistenceUnit  persistenceUnit; 
	
	public PlatkmPooledEntityManagerFactory(ORMPersistenceUnit persistenceUnit) {
		this.ds = new BasicDataSource();
		 
		ds.setUrl(persistenceUnit.getStringPropertyValue("javax.persistence.jdbc.url"));
		ds.setPassword(persistenceUnit.getStringPropertyValue("javax.persistence.jdbc.password"));
		ds.setUsername(persistenceUnit.getStringPropertyValue("javax.persistence.jdbc.user"));
		ds.setMinIdle(DataTypeUtil.getIntegerValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.minidle"), 5));
		ds.setMaxIdle(DataTypeUtil.getIntegerValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.maxidle"), 10));
		ds.setMaxOpenPreparedStatements(DataTypeUtil.getIntegerValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.maxopen"), 100));
		ds.setDriverClassName(persistenceUnit.getStringPropertyValue("javax.persistence.jdbc.driver"));
		ds.setAutoCommitOnReturn(DataTypeUtil.getBooleanValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.auto.commit"), false));
		ds.setDefaultAutoCommit(DataTypeUtil.getBooleanValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.default.auto.commit"), false));
		ds.setMaxTotal(DataTypeUtil.getIntegerValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.max.total"), 100));
		ds.setAutoCommitOnReturn(DataTypeUtil.getBooleanValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.auto.commit"), false));
		ds.setDefaultAutoCommit(DataTypeUtil.getBooleanValue(persistenceUnit.getProperties().get("javax.persistence.jdbc.default.auto.commit"), false));

		this.persistenceUnit = persistenceUnit;
	}

	
	@Override
	public PlatkmORMEntityManager create() throws Exception {  
		return new PlatkmEntityManagerImpl(this.persistenceUnit, persistenceUnit.getSqlSentencesProcessor(), ds);
	}

	@Override
	public PooledObject<PlatkmORMEntityManager> wrap(PlatkmORMEntityManager platkmEntityManager) {
		return new DefaultPooledObject<>(platkmEntityManager);
	}
	
    @Override
    public void passivateObject(PooledObject<PlatkmORMEntityManager> platkmEntityManager) throws Exception {
    	super.passivateObject(platkmEntityManager);
    }
	 

}
