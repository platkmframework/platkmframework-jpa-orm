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

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.platkmframework.annotation.db.SystemColumnAction;
import org.platkmframework.content.ObjectContainer;
import org.platkmframework.content.project.ProjectContent;
import org.platkmframework.database.query.QueryDao;
import org.platkmframework.database.query.QueryManagerDao;
import org.platkmframework.database.query.common.ColumnInfoValue;
import org.platkmframework.database.query.common.limit.ApplicationLimitException;
import org.platkmframework.database.query.common.limit.LimitChecker;
import org.platkmframework.database.query.manager.QueryManager;
import org.platkmframework.databasereader.core.DataBaseReaderConstants;
import org.platkmframework.databasereader.core.DatabaseReader;
import org.platkmframework.databasereader.core.IDatabaseReader;
import org.platkmframework.databasereader.model.Table;
import org.platkmframework.jpa.base.PlatkmEntityManager;
import org.platkmframework.jpa.base.PlatkmORMEntityManager;
import org.platkmframework.jpa.base.PlatkmQuery;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.platkmframework.jpa.mapping.DatabaseMapper;
import org.platkmframework.jpa.orm.mapping.DatabaseMapperImpl;
import org.platkmframework.jpa.orm.mapping.DbMappingUtil;
import org.platkmframework.jpa.persistence.PersistenceUnit;
import org.platkmframework.jpa.processor.ProcessResult;
import org.platkmframework.jpa.processor.SqlSentencesProcessor;
import org.platkmframework.jpa.querydao.QueryDaoImpl;
import org.platkmframework.jpa.querydao.QueryManagerDaoImpl;
import org.platkmframework.jpa.util.DaoUtil;
import org.platkmframework.persistence.filter.criteria.DeleteCriteria;
import org.platkmframework.persistence.filter.criteria.FilterCriteria;
import org.platkmframework.util.reflection.ReflectionUtil;
import jakarta.persistence.Column;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Id;
import jakarta.persistence.LockModeType;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.metamodel.Metamodel;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class PlatkmEntityManagerImpl extends PlatkmEntityManagerBase implements PlatkmORMEntityManager, PlatkmEntityManager {

    //private Map<String, Object> properties;
    private FlushModeType flushMode;

    /**
     * Atributo queryDao
     */
    private QueryDao queryDao;

    /**
     * Atributo queryManagerDao
     */
    private QueryManagerDao queryManagerDao;

    /**
     * Atributo queryManager
     */
    private QueryManager queryManager;

    /**
     * Atributo databaseReader
     */
    private IDatabaseReader databaseReader;

    /**
     * Atributo sqlprocessor
     */
    private SqlSentencesProcessor sqlprocessor;

    /**
     * Constructor PlatkmEntityManagerImpl
     * @param persistenceUnit persistenceUnit
     * @param sqlprocessor sqlprocessor
     * @param ds ds
     */
    public PlatkmEntityManagerImpl(PersistenceUnit persistenceUnit, SqlSentencesProcessor sqlprocessor, BasicDataSource ds) {
        super(persistenceUnit, new PlatkmEntityTransaction(ds));
        //this.properties = new HashMap<>();
        //TODO se podra mejorar para no tener que iniciarlizar queryDao y queryManagerDao por cada entity manager?
        this.queryDao = new QueryDaoImpl(this, sqlprocessor);
        this.queryManager = persistenceUnit.getQueryManager();
        this.queryManagerDao = new QueryManagerDaoImpl(this, sqlprocessor, persistenceUnit.getQueryManager());
        this.databaseReader = new DatabaseReader(getExcludedTables(persistenceUnit));
        this.sqlprocessor = sqlprocessor;
    }

    /**
     * getExcludedTables
     * @param persistenceUnit persistenceUnit
     * @return List
     */
    private List<String> getExcludedTables(PersistenceUnit persistenceUnit) {
        String excludedtables = ProjectContent.instance().getProperty(DataBaseReaderConstants.ORG_PLATKMFRAMEWORK_DATABASE_READER_EXCLUDED_TABLES);
        return (StringUtils.isNotBlank(excludedtables)) ? Arrays.asList(excludedtables.split(",")) : null;
    }

    /**
     * persist
     * @param entity entity
     */
    @Override
    public void persist(Object entity) {
        try {
            LimitChecker limitChecker = (LimitChecker) ObjectContainer.instance().geApptScopeObj(LimitChecker.class);
            if (!limitChecker.check(entity))
                throw new ApplicationLimitException("No se pueden crear más registros en este proceso, por exceder el límite permitido");
            String tableName = DaoUtil.getTableName(entity.getClass());
            List<ColumnInfoValue> columns = getColumInfoValues(entity, SystemColumnAction.CREATE);
            ColumnInfoValue cvPkAuntoIncrement = insertSQL(tableName, columns);
            if (cvPkAuntoIncrement != null && cvPkAuntoIncrement.getValue() != null)
                ReflectionUtil.setAttributeValue(entity, StringUtils.isNotBlank(cvPkAuntoIncrement.getClassFieldName()) ? cvPkAuntoIncrement.getClassFieldName() : cvPkAuntoIncrement.getName(), cvPkAuntoIncrement.getValue());
        } catch (Exception e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * merge
     * @param entity entity
     * @return T
     */
    @Override
    public <T> T merge(T entity) {
        try {
            String tableName = DaoUtil.getTableName(entity.getClass());
            List<ColumnInfoValue> columns = getColumInfoValues(entity, SystemColumnAction.UPDATE);
            List<Field> fields = ReflectionUtil.getAllFieldHeritage(entity.getClass());
            Field field = fields.stream().filter(f -> f.isAnnotationPresent(Id.class)).findAny().orElse(null);
            if (field == null)
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion de llave primaria");
            if (!field.isAnnotationPresent(Column.class))
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion que represente los valores del campo para la llave primaria");
            Column pkColum = field.getAnnotation(Column.class);
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE " + tableName + " SET ");
            sb.append(_getUpdateIntoValues(columns));
            sb.append(" WHERE " + pkColum.name() + " =? ");
            PreparedStatement preparedStatement = platkmEntityTransaction.getCon().prepareStatement(sb.toString());
            _setValuesToPrepareStatment(columns, preparedStatement, false);
            preparedStatement.executeUpdate();
            return entity;
        } catch (Exception e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * remove
     * @param entity entity
     */
    @Override
    public void remove(Object entity) {
        try {
            String tableName = DaoUtil.getTableName(entity.getClass());
            List<Field> fields = ReflectionUtil.getAllFieldHeritage(entity.getClass());
            Field field = fields.stream().filter(f -> f.isAnnotationPresent(Id.class)).findAny().orElse(null);
            if (field == null)
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion de llave primaria");
            if (!field.isAnnotationPresent(Column.class))
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion que represente los valores del campo para la llave primaria");
            Column pkColum = field.getAnnotation(Column.class);
            PreparedStatement preparedStatement = platkmEntityTransaction.getCon().prepareStatement("DELETE  FROM " + tableName + " WHERE " + pkColum.name() + " =?");
            DbMappingUtil.setValue(field.getType(), preparedStatement, 1, ReflectionUtil.getAttributeValue(entity, field), (DatabaseMapperImpl) getDatabaseMapper());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * find
     * @param entityClass entityClass
     * @param primaryKey primaryKey
     * @return T
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey) {
        try {
            List<Field> fields = ReflectionUtil.getAllFieldHeritage(entityClass);
            Field field = fields.stream().filter(f -> f.isAnnotationPresent(Id.class)).findAny().orElse(null);
            if (field == null)
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion de llave primaria");
            if (!field.isAnnotationPresent(Column.class))
                throw new PlatkmJpaException("No se encontró en la entidad una anotacion que represente los valores del campo para la llave primaria");
            Column pkColum = field.getAnnotation(Column.class);
            String tableName = DaoUtil.getTableName(entityClass);
            PreparedStatement preparedStatement = platkmEntityTransaction.getCon().prepareStatement(" SELECT * FROM " + tableName + " WHERE " + pkColum.name() + " =?");
            DbMappingUtil.setValue(primaryKey.getClass(), preparedStatement, 1, primaryKey, (DatabaseMapperImpl) getDatabaseMapper());
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return DbMappingUtil.getCustomEntity(rs, entityClass, (DatabaseMapperImpl) getDatabaseMapper());
            } else
                return null;
        } catch (Exception e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * find
     * @param entityClass entityClass
     * @param primaryKey primaryKey
     * @param properties properties
     * @return T
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
        return find(entityClass, primaryKey);
    }

    /**
     * find
     * @param entityClass entityClass
     * @param primaryKey primaryKey
     * @param lockMode lockMode
     * @return T
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
        return find(entityClass, primaryKey);
    }

    /**
     * find
     * @param entityClass entityClass
     * @param primaryKey primaryKey
     * @param lockMode lockMode
     * @param properties properties
     * @return T
     */
    @Override
    public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode, Map<String, Object> properties) {
        return find(entityClass, primaryKey);
    }

    /**
     * getReference
     * @param entityClass entityClass
     * @param primaryKey primaryKey
     * @return T
     */
    @Override
    public <T> T getReference(Class<T> entityClass, Object primaryKey) {
        throw new NotImplementedException("getReference");
    }

    /**
     * flush
     */
    @Override
    public void flush() {
        platkmEntityTransaction.commit();
    }

    /**
     * setFlushMode
     * @param flushMode flushMode
     */
    @Override
    public void setFlushMode(FlushModeType flushMode) {
        this.flushMode = flushMode;
    }

    /**
     * getFlushMode
     * @return FlushModeType
     */
    @Override
    public FlushModeType getFlushMode() {
        return flushMode;
    }

    /**
     * lock
     * @param entity entity
     * @param lockMode lockMode
     */
    @Override
    public void lock(Object entity, LockModeType lockMode) {
        throw new NotImplementedException("lock");
    }

    /**
     * lock
     * @param entity entity
     * @param lockMode lockMode
     * @param properties properties
     */
    @Override
    public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new NotImplementedException("lock");
    }

    /**
     * refresh
     * @param entity entity
     */
    @Override
    public void refresh(Object entity) {
        throw new NotImplementedException("refresh");
    }

    /**
     * refresh
     * @param entity entity
     * @param properties properties
     */
    @Override
    public void refresh(Object entity, Map<String, Object> properties) {
        throw new NotImplementedException("refresh");
    }

    /**
     * refresh
     * @param entity entity
     * @param lockMode lockMode
     */
    @Override
    public void refresh(Object entity, LockModeType lockMode) {
        throw new NotImplementedException("refresh");
    }

    /**
     * refresh
     * @param entity entity
     * @param lockMode lockMode
     * @param properties properties
     */
    @Override
    public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
        throw new NotImplementedException("refresh");
    }

    /**
     * clear
     */
    @Override
    public void clear() {
        throw new NotImplementedException("clear");
    }

    /**
     * detach
     * @param entity entity
     */
    @Override
    public void detach(Object entity) {
        throw new NotImplementedException("detach");
    }

    /**
     * contains
     * @param entity entity
     * @return boolean
     */
    @Override
    public boolean contains(Object entity) {
        throw new NotImplementedException("contains");
    }

    /**
     * getLockMode
     * @param entity entity
     * @return LockModeType
     */
    @Override
    public LockModeType getLockMode(Object entity) {
        throw new NotImplementedException("getLockMode");
    }

    /**
     * setProperty
     * @param propertyName propertyName
     * @param value value
     */
    @Override
    public void setProperty(String propertyName, Object value) {
        throw new NotImplementedException("getLockMode");
    }

    /**
     * getProperties
     * @return Map
     */
    @Override
    public Map<String, Object> getProperties() {
        return persistenceUnit.getProperties();
    }

    /**
     * createQuery
     * @param sql sql
     * @return Query
     */
    @Override
    public Query createQuery(String sql) {
        try {
            return new QueryImpl(platkmEntityTransaction.getCon().prepareStatement(sql), null, this.persistenceUnit.getProperties(), (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
        } catch (SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createQuery
     * @param criteriaQuery criteriaQuery
     * @return TypedQuery
     */
    @Override
    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
        throw new NotImplementedException("createQuery(CriteriaQuery<T> criteriaQuery) ");
    }

    /**
     * createQuery
     * @param updateQuery updateQuery
     * @return Query
     */
    @Override
    public Query createQuery(CriteriaUpdate updateQuery) {
        throw new NotImplementedException("createQuery(CriteriaUpdate updateQuery)");
    }

    /**
     * createQuery
     * @param deleteQuery deleteQuery
     * @return Query
     */
    @Override
    public Query createQuery(CriteriaDelete deleteQuery) {
        throw new NotImplementedException("createQuery(CriteriaDelete deleteQuery)");
    }

    /**
     * createQuery
     * @param filterCriteria filterCriteria
     * @param params params
     * @param returnClass returnClass
     * @return PlatkmQuery
     */
    @Override
    public PlatkmQuery createQuery(FilterCriteria filterCriteria, List<Object> params, Class<?> returnClass) {
        try {
            ProcessResult processResult = sqlprocessor.process(this, filterCriteria, params);
            PlatkmQuery query = new PlatkmQueryImpl(platkmEntityTransaction.getCon().prepareStatement(processResult.getSql()), returnClass, processResult.getPage(), processResult.getPageCount(), this.persistenceUnit.getProperties(), (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
            if (processResult.getParameters() != null) {
                for (int i = 0; i < processResult.getParameters().size(); i++) query.setParameter(i + 1, processResult.getParameters().get(i));
            }
            return query;
        } catch (DatabaseValidationException | SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createQuery
     * @param deleteCriteria deleteCriteria
     * @param params params
     * @return Query
     */
    @Override
    public Query createQuery(DeleteCriteria deleteCriteria, List<Object> params) {
        try {
            ProcessResult processResult = sqlprocessor.process(this, deleteCriteria, params);
            if (processResult.getParameters() == null || processResult.getParameters().isEmpty()) {
                throw new PlatkmJpaException("No se puede procesar una eliminaci�n sin filtros");
            }
            Query query = new QueryImpl(platkmEntityTransaction.getCon().prepareStatement(processResult.getSql()), null, this.persistenceUnit.getProperties(), (DatabaseMapperImpl) this.persistenceUnit.getDatabaseMapper());
            if (processResult.getParameters() != null) {
                for (int i = 0; i < processResult.getParameters().size(); i++) query.setParameter(i + 1, processResult.getParameters().get(i));
            }
            return query;
        } catch (DatabaseValidationException | SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createQuery
     * @param sql sql
     * @param resultClass resultClass
     * @return TypedQuery
     */
    @Override
    public <T> TypedQuery<T> createQuery(String sql, Class<T> resultClass) {
        try {
            return new TypedQueryImpl<>(platkmEntityTransaction.getCon().prepareStatement(sql), resultClass, this.persistenceUnit.getProperties(), (DatabaseMapperImpl) this.persistenceUnit.getDatabaseMapper());
        } catch (SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createNamedQuery
     * @param name name
     * @return Query
     */
    @Override
    public Query createNamedQuery(String name) {
        throw new NotImplementedException("createNamedQuery)");
    }

    /**
     * createNamedQuery
     * @param name name
     * @param resultClass resultClass
     * @return TypedQuery
     */
    @Override
    public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
        throw new NotImplementedException("createNamedQuery)");
    }

    /**
     * createNativeQuery
     * @param sql sql
     * @return Query
     */
    @Override
    public Query createNativeQuery(String sql) {
        try {
            return new QueryImpl(platkmEntityTransaction.getCon().prepareStatement(sql), null, this.persistenceUnit.getProperties(), (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
        } catch (SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createNativeQuery
     * @param sql sql
     * @param resultClass resultClass
     * @return Query
     */
    @Override
    public Query createNativeQuery(String sql, Class resultClass) {
        try {
            return new QueryImpl(platkmEntityTransaction.getCon().prepareStatement(sql), resultClass, this.persistenceUnit.getProperties(), (DatabaseMapperImpl) persistenceUnit.getDatabaseMapper());
        } catch (SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * createNativeQuery
     * @param sql sql
     * @param resultSetMapping resultSetMapping
     * @return Query
     */
    @Override
    public Query createNativeQuery(String sql, String resultSetMapping) {
        return createNativeQuery(sql);
    }

    /**
     * createNamedStoredProcedureQuery
     * @param name name
     * @return StoredProcedureQuery
     */
    @Override
    public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
        throw new NotImplementedException("createNamedStoredProcedureQuery");
    }

    /**
     * createStoredProcedureQuery
     * @param procedureName procedureName
     * @return StoredProcedureQuery
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
        throw new NotImplementedException("createStoredProcedureQuery");
    }

    /**
     * createStoredProcedureQuery
     * @param procedureName procedureName
     * @param resultClasses resultClasses
     * @return StoredProcedureQuery
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class... resultClasses) {
        throw new NotImplementedException("createStoredProcedureQuery");
    }

    /**
     * createStoredProcedureQuery
     * @param procedureName procedureName
     * @param resultSetMappings resultSetMappings
     * @return StoredProcedureQuery
     */
    @Override
    public StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
        throw new NotImplementedException("createStoredProcedureQuery");
    }

    /**
     * joinTransaction
     */
    @Override
    public void joinTransaction() {
        throw new NotImplementedException("joinTransaction");
    }

    /**
     * isJoinedToTransaction
     * @return boolean
     */
    @Override
    public boolean isJoinedToTransaction() {
        throw new NotImplementedException("isJoinedToTransaction");
    }

    /**
     * unwrap
     * @param cls cls
     * @return T
     */
    @Override
    public <T> T unwrap(Class<T> cls) {
        throw new NotImplementedException("unwrap(Class<T> cls)");
    }

    /**
     * getDelegate
     * @return Object
     */
    @Override
    public Object getDelegate() {
        throw new NotImplementedException("getDelegate()");
    }

    /**
     * close
     */
    @Override
    public void close() {
        try {
            platkmEntityTransaction.close();
        } catch (SQLException e) {
            throw new PlatkmJpaException(e);
        }
    }

    /**
     * isOpen
     * @return boolean
     */
    @Override
    public boolean isOpen() {
        return platkmEntityTransaction.isActive();
    }

    /**
     * getTransaction
     * @return EntityTransaction
     */
    @Override
    public EntityTransaction getTransaction() {
        return platkmEntityTransaction;
    }

    /**
     * getEntityManagerFactory
     * @return EntityManagerFactory
     */
    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        throw new NotImplementedException("getEntityManagerFactory())");
    }

    /**
     * getCriteriaBuilder
     * @return CriteriaBuilder
     */
    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        throw new NotImplementedException("getCriteriaBuilder()");
    }

    /**
     * getMetamodel
     * @return Metamodel
     */
    @Override
    public Metamodel getMetamodel() {
        throw new NotImplementedException("getMetamodel() ");
    }

    /**
     * createEntityGraph
     * @param rootType rootType
     * @return EntityGraph
     */
    @Override
    public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
        throw new NotImplementedException("createEntityGraph(Class<T> rootType) ");
    }

    /**
     * createEntityGraph
     * @param graphName graphName
     * @return EntityGraph
     */
    @Override
    public EntityGraph<?> createEntityGraph(String graphName) {
        throw new NotImplementedException("createEntityGraph(String graphName) ");
    }

    /**
     * getEntityGraph
     * @param graphName graphName
     * @return EntityGraph
     */
    @Override
    public EntityGraph<?> getEntityGraph(String graphName) {
        throw new NotImplementedException("getEntityGraph(String graphName)");
    }

    /**
     * getEntityGraphs
     * @param entityClass entityClass
     * @return List
     */
    @Override
    public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
        throw new NotImplementedException("getEntityGraphs(Class<T> entityClass))");
    }

    /**
     * getQueryDao
     * @return QueryDao
     */
    public QueryDao getQueryDao() {
        return queryDao;
    }

    /**
     * restart
     */
    public void restart() {
        this.flushMode = null;
    }

    /**
     * getQueryManagerDao
     * @return QueryManagerDao
     */
    @Override
    public QueryManagerDao getQueryManagerDao() {
        return queryManagerDao;
    }

    /**
     * getQueryManager
     * @return QueryManager
     */
    @Override
    public QueryManager getQueryManager() {
        return queryManager;
    }

    /**
     * getDatabaseMapper
     * @return DatabaseMapper
     */
    @Override
    public DatabaseMapper getDatabaseMapper() {
        return persistenceUnit.getDatabaseMapper();
    }

    /**
     * getMetadata
     * @param catalog catalog
     * @param schema schema
     * @return List
     */
    @Override
    public List<Table> getMetadata(String catalog, String schema) {
        return databaseReader.getMetadata(platkmEntityTransaction.getCon(), catalog, schema);
    }

    /**
     * getTablePksContraints
     * @param catalog catalog
     * @param schema schema
     * @param tableName tableName
     * @return List
     */
    @Override
    public List<String> getTablePksContraints(String catalog, String schema, String tableName) {
        return databaseReader.getTablePksContraints(platkmEntityTransaction.getCon(), catalog, schema, tableName);
    }

    /**
     * getTableColumnMetaData
     * @param catalog catalog
     * @param schema schema
     * @param tablename tablename
     * @return List
     */
    @Override
    public List<org.platkmframework.databasereader.model.Column> getTableColumnMetaData(String catalog, String schema, String tablename) {
        return databaseReader.getTableColumnMetaData(platkmEntityTransaction.getCon(), catalog, schema, tablename);
    }

    /**
     * insert
     * @param entity entity
     * @param columns columns
     * @return ColumnInfoValue
     * @throws DatabaseValidationException DatabaseValidationException
     * @throws PlatkmJpaException PlatkmJpaException
     */
    @Override
    public ColumnInfoValue insert(String entity, List<ColumnInfoValue> columns) throws DatabaseValidationException, PlatkmJpaException {
        return insertSQL(entity, columns);
    }

    /**
     * update
     * @param entity entity
     * @param columns columns
     * @throws DatabaseValidationException DatabaseValidationException
     * @throws PlatkmJpaException PlatkmJpaException
     */
    @Override
    public void update(String entity, List<ColumnInfoValue> columns) throws DatabaseValidationException, PlatkmJpaException {
        updateSQL(entity, columns);
    }
}
