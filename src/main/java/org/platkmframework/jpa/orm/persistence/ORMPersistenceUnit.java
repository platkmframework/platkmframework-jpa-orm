package org.platkmframework.jpa.orm.persistence;

import org.platkmframework.jpa.persistence.PersistenceUnit;
import org.platkmframework.jpa.processor.SqlSentencesProcessor;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class ORMPersistenceUnit extends PersistenceUnit {

    /**
     * Atributo sqlSentencesProcessor
     */
    private SqlSentencesProcessor sqlSentencesProcessor;

    /**
     * Constructor ORMPersistenceUnit
     */
    public ORMPersistenceUnit() {
        super();
    }

    /**
     * getSqlSentencesProcessor
     * @return SqlSentencesProcessor
     */
    public SqlSentencesProcessor getSqlSentencesProcessor() {
        return sqlSentencesProcessor;
    }

    /**
     * setSqlSentencesProcessor
     * @param sqlSentencesProcessor sqlSentencesProcessor
     */
    public void setSqlSentencesProcessor(SqlSentencesProcessor sqlSentencesProcessor) {
        this.sqlSentencesProcessor = sqlSentencesProcessor;
    }
}
