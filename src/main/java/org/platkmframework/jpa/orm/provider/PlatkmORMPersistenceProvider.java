package org.platkmframework.jpa.orm.provider;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.platkmframework.annotation.db.DatabaseConfig;
import org.platkmframework.content.ObjectContainer;
import org.platkmframework.database.query.manager.QueryManager;
import org.platkmframework.jpa.exception.PlatkmJpaException;
import org.platkmframework.jpa.mapping.DatabaseMapper;
import org.platkmframework.jpa.mapping.JpaPropertyConstant;
import org.platkmframework.jpa.orm.database.h2.mapping.SqlSentencesProcessorH2;
import org.platkmframework.jpa.orm.database.oracle.mapping.SqlSentencesProcessorOracle;
import org.platkmframework.jpa.orm.database.postgresql.mapping.SqlSentencesProcessorPostgreSQL;
import org.platkmframework.jpa.orm.database.sqlserver.mapping.SqlSentencesProcessorSqlServer;
import org.platkmframework.jpa.orm.factory.PlatkmPooledEntityManagerFactory;
import org.platkmframework.jpa.orm.persistence.ORMPersistenceUnit;
import org.platkmframework.jpa.orm.persistence.ddl.SchemaGeneratorImpl;
import org.platkmframework.jpa.persistence.PlatkmEntityManagerFactory;
import org.platkmframework.jpa.persistence.PlatkmPersistenceProvider;
import org.platkmframework.jpa.persistence.reader.PersistenceInfo;
import org.platkmframework.jpa.persistence.reader.PlatkmPersistenceFileParse;
import org.platkmframework.util.manager.ManagerException;

public class PlatkmORMPersistenceProvider extends PlatkmPersistenceProvider  {

	public PlatkmORMPersistenceProvider() {
		super();
	}
	 
	@Override
	protected void createPlakmEntityManagerFactory(String persistenceUnitName, Map map) {
		
		
		try {
			
			PersistenceInfo persistenceInfo = PlatkmPersistenceFileParse.persistenceInfoList.stream().filter((info)-> info.getName().equals(persistenceUnitName)).findFirst().orElse(null);
			ORMPersistenceUnit oRMPersistenceUnit = createORMPersistenceUnit(persistenceInfo);

			
			if("h2".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
				oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorH2());
			}else if("oracle".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
				oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorOracle());
			}else if("postgresql".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
				oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorPostgreSQL());
			}else if("mssqlserver".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
				oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorSqlServer());
			}
			
			mapFactory.put(oRMPersistenceUnit.getName(), new PlatkmEntityManagerFactory(oRMPersistenceUnit, 
					 												new PlatkmPooledEntityManagerFactory(oRMPersistenceUnit),
					 												new SchemaGeneratorImpl()));
	
			
		} catch (ManagerException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		 
	}

	protected ORMPersistenceUnit createORMPersistenceUnit(PersistenceInfo persistenceInfo) throws ManagerException {
		
		ORMPersistenceUnit oRMPersistenceUnit = new ORMPersistenceUnit();
		oRMPersistenceUnit.setProperties(persistenceInfo.getProperties());
		oRMPersistenceUnit.setName(persistenceInfo.getName());
		oRMPersistenceUnit.setTransactionType(persistenceInfo.getTransactionType());
		oRMPersistenceUnit.setProvider(persistenceInfo.getProvider());
		oRMPersistenceUnit.setClasses(persistenceInfo.getClasses());		
		
		String jdbcDriver = persistenceInfo.getStringPropertyValue("javax.persistence.jdbc.driver");
		if(StringUtils.isBlank(jdbcDriver)) throw new PlatkmJpaException(" No se encontró un mapper para la base de datos");
		
		DatabaseMapper databaseMapper = null; 
		List<Object> list = ObjectContainer.instance().getListObjectByAnnontation(DatabaseConfig.class);
		for (Object object : list) {
			if(object.getClass().getAnnotation(DatabaseConfig.class).name().equals(jdbcDriver)){
				databaseMapper = (DatabaseMapper) object;
				break;
			}
		} 
		
		if(databaseMapper == null) throw new PlatkmJpaException(" No se encontró un mapper para la base de datos");
		oRMPersistenceUnit.setDatabaseMapper(databaseMapper);		
		
		
		if("h2".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
			oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorH2());
		}else if("oracle".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
			oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorOracle());
		}else if("postgresql".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
			oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorPostgreSQL());
		}else if("mssqlserver".equals(oRMPersistenceUnit.getDatabaseMapper().getDatabaseName())){
			oRMPersistenceUnit.setSqlSentencesProcessor(new SqlSentencesProcessorSqlServer());
		}
		
		QueryManager queryManager = new QueryManager(databaseMapper.getDatabaseName());
		//Default
		queryManager.readModel("system", PlatkmORMPersistenceProvider.class.getResourceAsStream("/queries/system/model.xml"));

		String queriesManagerPath = persistenceInfo.getStringPropertyValue(JpaPropertyConstant.ORG_PLATKMFRAMEWORK_DATABASE_QUERYMANAGERS_PATH);
		if(StringUtils.isNotBlank(queriesManagerPath)) {
			String[] qmPaths = queriesManagerPath.split(",");
			for (int j = 0; j < qmPaths.length; j++) { 
				queryManager.readModel("key_" + j, PlatkmORMPersistenceProvider.class.getResourceAsStream("/queries/" + qmPaths[j]));
			} 
		} 
		
		oRMPersistenceUnit.setQueryManager(queryManager);
		return oRMPersistenceUnit;
	}

}
