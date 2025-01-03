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
 */
public class PlatkmEntityTransaction implements EntityTransaction {

    /**
     * Atributo logger
     */
    private static Logger logger = LoggerFactory.getLogger(PlatkmEntityTransaction.class);

    /**
     * Atributo ds
     */
    protected BasicDataSource ds;

    /**
     * Atributo rollbackOnly
     */
    protected boolean rollbackOnly = false;

    /**
     * Atributo active
     */
    protected boolean active = false;

    /**
     * Atributo rollbacked
     */
    protected boolean rollbacked = false;

    /**
     * Atributo con
     */
    private Connection con;

    /**
     * Constructor PlatkmEntityTransaction
     * @param ds ds
     */
    public PlatkmEntityTransaction(BasicDataSource ds) {
        super();
        this.ds = ds;
    }

    /**
     * begin
     */
    @Override
    public synchronized void begin() {
        try {
            if (active)
                throw new IllegalStateException("transaction already active");
            con = ds.getConnection();
            active = true;
            rollbackOnly = false;
            rollbacked = false;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * commit
     */
    @Override
    public void commit() {
        try {
            if (!active || rollbacked)
                throw new IllegalStateException("commit not available active " + active + " and rollbacked " + rollbacked);
            con.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * rollback
     */
    @Override
    public void rollback() {
        try {
            // throw new IllegalStateException("rollback not available active " + active + " and rollbacked " + rollbacked);
            if (!active || rollbacked)
                return;
            con.rollback();
            rollbacked = true;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * setRollbackOnly
     */
    @Override
    public void setRollbackOnly() {
        rollbackOnly = true;
    }

    /**
     * getRollbackOnly
     * @return boolean
     */
    @Override
    public boolean getRollbackOnly() {
        return rollbackOnly;
    }

    /**
     * isActive
     * @return boolean
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * close
     * @throws SQLException SQLException
     */
    public void close() throws SQLException {
        active = false;
        rollbackOnly = false;
        rollbacked = false;
        if (con != null)
            con.close();
    }

    /**
     * getCon
     * @return Connection
     */
    public Connection getCon() {
        return con;
    }

    /**
     * setCon
     * @param con con
     */
    public void setCon(Connection con) {
        this.con = con;
    }
}
