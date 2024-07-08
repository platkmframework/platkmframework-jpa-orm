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
package org.platkmframework.jpa.orm.entitymanager;

import java.sql.Connection;
import java.sql.SQLException; 

import org.apache.commons.dbcp2.BasicDataSource;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityTransaction;


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class PlatkmEntityTransaction implements EntityTransaction{
	
	
	private static Logger logger = LoggerFactory.getLogger(PlatkmEntityTransaction.class);
	  
	 protected BasicDataSource ds;
	 protected boolean rollbackOnly = false;
	 protected boolean active = false;
	 protected boolean rollbacked = false;
	 private Connection con;
			
	 
	public PlatkmEntityTransaction(BasicDataSource ds) {
		super();
		this.ds = ds;  
	}
	 

	@Override
	public synchronized void begin() {
		
		try {
			 
			if (active) throw new IllegalStateException("transaction already active"); 
			con = ds.getConnection();
			active = true;
			rollbackOnly = false;
			rollbacked = false;
			
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new PlatkmJpaException(e);
		}
		
	}

	@Override
	public void commit() {
		try {
			
			if(!active || rollbacked) throw new IllegalStateException("commit not available active " + active + " and rollbacked " + rollbacked);
			con.commit();
		} catch ( SQLException e) {
			logger.error(e.getMessage(), e);
			throw new PlatkmJpaException(e);
		}
	}

	@Override
	public void rollback() {
		try {
			if(!active || rollbacked) return; // throw new IllegalStateException("rollback not available active " + active + " and rollbacked " + rollbacked);
			con.rollback();
			rollbacked = true;
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			throw new PlatkmJpaException(e);
		}
	}

	@Override
	public void setRollbackOnly() {
		rollbackOnly = true;
	}

	@Override
	public boolean getRollbackOnly() { 
		return rollbackOnly;
	}

	@Override
	public boolean isActive() { 
		return active;
	}

	public void close() throws SQLException {
		active = false;
		rollbackOnly = false;
		rollbacked = false;
		if(con != null)
			con.close(); 
	}


	public Connection getCon() {
		return con;
	}


	public void setCon(Connection con) {
		this.con = con;
	} 

}
