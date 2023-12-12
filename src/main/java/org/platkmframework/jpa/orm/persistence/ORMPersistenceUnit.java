package org.platkmframework.jpa.orm.persistence;

import org.platkmframework.jpa.persistence.PersistenceUnit;
import org.platkmframework.jpa.processor.SqlSentencesProcessor;

public class ORMPersistenceUnit extends PersistenceUnit {

	private SqlSentencesProcessor  sqlSentencesProcessor;

	public ORMPersistenceUnit() {
		super(); 
	}

	public SqlSentencesProcessor getSqlSentencesProcessor() {
		return sqlSentencesProcessor;
	}

	public void setSqlSentencesProcessor(SqlSentencesProcessor  sqlSentencesProcessor) {
		this.sqlSentencesProcessor = sqlSentencesProcessor;
	}
	
	
}
