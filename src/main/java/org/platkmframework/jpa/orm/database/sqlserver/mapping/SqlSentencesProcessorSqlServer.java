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
package org.platkmframework.jpa.orm.database.sqlserver.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.platkmframework.database.query.manager.model.QuerySelect;
import org.platkmframework.database.query.manager.model.QuerySyntax;
import org.platkmframework.jpa.base.PlatkmORMEntityManager;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.processor.ProcessResult;
import org.platkmframework.jpa.processor.SqlSentencesProcessorBase;
import org.platkmframework.persistence.filter.criteria.DeleteCriteria;
import org.platkmframework.persistence.filter.criteria.WhereCriteria;
import org.platkmframework.persistence.filter.criteria.base.ConditionFilterBase;
import org.platkmframework.persistence.filter.enumerator.ConditionOperator;
import org.platkmframework.persistence.filter.enumerator.SqlOperator;
import org.platkmframework.persistence.filter.enumerator.SqlOrder;
import org.platkmframework.persistence.filter.info.FilterData;
import org.platkmframework.persistence.filter.info.FilterDataType;
import jakarta.persistence.Query;

/**
 *   Author:
 *     Eduardo Iglesias
 *   Contributors:
 *   	Eduardo Iglesias - initial API and implementation
 */
public class SqlSentencesProcessorSqlServer extends SqlSentencesProcessorBase {

    /**
     * SqlSentencesProcessorSqlServer
     */
    private static Logger logger = LoggerFactory.getLogger(SqlSentencesProcessorSqlServer.class);

    /**
     * getExpression
     * @param colName colName
     * @param operator operator
     * @param value value
     * @param param param
     * @return String
     */
    protected String getExpression(String colName, String operator, Object value, List<Object> param) {
        String sql = " ";
        if (ConditionOperator.startWith.name().equals(operator)) {
            sql = "LOWER(" + colName + ") like LOWER(?) ";
            if (value != null)
                value = value.toString() + '%';
        } else if (ConditionOperator.contain.name().equals(operator)) {
            sql = "LOWER(" + colName + ") like LOWER(?) ";
            if (value != null)
                value = '%' + value.toString() + '%';
        } else if (ConditionOperator.notContain.name().equals(operator)) {
            sql = "LOWER(" + colName + ") NOT like LOWER(?) ";
            if (value != null)
                value = '%' + value.toString() + '%';
        } else if (ConditionOperator.endWith.name().equals(operator)) {
            sql = "LOWER(" + colName + ") like LOWER(?) ";
            if (value != null)
                value = '%' + value.toString();
        } else if (ConditionOperator.equal.name().equals(operator))
            sql = colName + " =? ";
        else if (ConditionOperator.notEqual.name().equals(operator))
            sql = colName + "  !=?  ";
        else if (ConditionOperator.greateThan.name().equals(operator))
            sql = colName + " >?  ";
        else if (ConditionOperator.greateEqual.name().equals(operator))
            sql = colName + " >=?  ";
        else if (ConditionOperator.lessThan.name().equals(operator))
            sql = colName + " <?  ";
        else if (ConditionOperator.lessEqual.name().equals(operator))
            sql = colName + " <=?  ";
        else if (ConditionOperator.include.name().equals(operator))
            sql = colName + " in (?) ";
        else if (ConditionOperator.notInclude.name().equals(operator))
            sql = colName + " NOT in (?) ";
        else if (ConditionOperator.isNull.name().equals(operator))
            sql = colName + " IS NULL ";
        else if (ConditionOperator.isNotNull.name().equals(operator))
            sql = colName + " IS NOT NULL ";
        if (!ConditionOperator.isNull.name().equals(operator) && !ConditionOperator.isNotNull.name().equals(operator) && param != null)
            param.add(value);
        //if(condOperator.getValueCondition() != null)
        //	condOperator.getValueCondition().setValue(value);
        return sql;
    }

    /**
     * getExpression
     * @param expression expression
     * @param param param
     * @return String
     * @throws DatabaseValidationException DatabaseValidationException
     */
    protected String getExpression(FilterData expression, List<Object> param) throws DatabaseValidationException {
        String sql = " ";
        if (expression.isValueAsColumn()) {
            Object value1, value2;
            if (expression.getFilterDataType().name().equals(FilterDataType.VALUEINFO.name())) {
                value1 = expression.getValue1();
                value2 = expression.getValue2();
            } else {
                value1 = expression.getColumn();
                value2 = expression.getExpesionValue();
            }
            String operator = "";
            if (ConditionOperator.equal.name().equals(expression.getExpesionOperator().name())) {
                operator = " = ";
            } else if (ConditionOperator.notEqual.name().equals(expression.getExpesionOperator().name())) {
                operator = " != ";
            } else if (ConditionOperator.greateThan.name().equals(expression.getExpesionOperator().name())) {
                operator = " > ";
            } else if (ConditionOperator.greateEqual.name().equals(expression.getExpesionOperator().name())) {
                operator = " >= ";
            } else if (ConditionOperator.lessThan.name().equals(expression.getExpesionOperator().name())) {
                operator = " < ";
            } else if (ConditionOperator.lessEqual.name().equals(expression.getExpesionOperator().name())) {
                operator = " <= ";
            } else {
                logger.error("no se encontro operador para expresion que no son campos");
                throw new DatabaseValidationException(" error consult admin");
            }
            sql = value1 + "  " + operator + "  " + value2 + "  ";
        } else {
            sql = getExpression(expression.getColumn(), expression.getExpesionOperator().name(), expression.getExpesionValue(), param);
        }
        return sql;
    }

    /**
     * processFastSearch
     * @param listQuerySyntax listQuerySyntax
     * @param withWhere withWhere
     * @param fastsearchValue fastsearchValue
     * @param parameters parameters
     * @param fastsearchList fastsearchList
     * @return String
     */
    protected String processFastSearch(List<QuerySyntax> listQuerySyntax, boolean withWhere, String fastsearchValue, List<Object> parameters, List<String> fastsearchList) {
        String sql;
        if (withWhere)
            sql = " AND (";
        else
            sql = " WHERE (";
        String concant = _getQuerySyntaxValue(listQuerySyntax, "concat");
        concant = concant + "' '" + concant;
        String sqlLower = _getQuerySyntaxValue(listQuerySyntax, "sql_lower");
        String aux = "";
        for (String column : fastsearchList) {
            sql += aux + sqlLower + "(" + column + ") ";
            aux = concant;
        }
        parameters.add("%" + fastsearchValue + "%");
        return sql + " like " + sqlLower + " (?) ) ";
    }

    /**
     * processWhere
     * @param param param
     * @param whereFilter whereFilter
     * @return String
     * @throws DatabaseValidationException DatabaseValidationException
     */
    @Override
    public String processWhere(List<Object> param, ConditionFilterBase whereFilter) throws DatabaseValidationException {
        String sql = "";
        if (whereFilter.getSql() != null && whereFilter.getSql().isEmpty()) {
            for (FilterData ob : whereFilter.getSql()) {
                if (ob.isType(FilterDataType.EXPRESSIONINFO)) {
                    sql += " " + getExpression(ob, param);
                } else if (ob.isType(FilterDataType.OPERATORINFO)) {
                    sql += " " + getOperator(ob.getSqlOperator());
                }
            }
        }
        return StringUtils.isNotBlank(sql) ? " WHERE " + sql : "";
    }

    /**
     * getOrderByType
     * @param orderType orderType
     * @return String
     * @throws DatabaseValidationException DatabaseValidationException
     */
    @Override
    protected String getOrderByType(SqlOrder orderType) throws DatabaseValidationException {
        if (SqlOrder.asc.name().equals(orderType.name()))
            return "  ASC ";
        else if (SqlOrder.desc.name().equals(orderType.name()))
            return " DESC ";
        else {
            logger.error("no se encontro operaor para order by ");
            throw new DatabaseValidationException(" error consult admin");
        }
    }

    /**
     * removeProcess
     * @param platkmEntityManager platkmEntityManager
     * @param deleteCriteria deleteCriteria
     * @param param param
     * @return ProcessResult
     * @throws DatabaseValidationException DatabaseValidationException
     */
    @Override
    public ProcessResult removeProcess(PlatkmORMEntityManager platkmEntityManager, DeleteCriteria deleteCriteria, List<Object> param) throws DatabaseValidationException {
        List<FilterData> joinTables = new ArrayList<>();
        List<FilterData> others = new ArrayList<>();
        List<FilterData> sql = new ArrayList<>();
        List<FilterData> joinConditions = new ArrayList<>();
        List<FilterData> whereConditions = new ArrayList<>();
        boolean existsWhere = false;
        for (FilterData filterData : deleteCriteria.getSql()) {
            if (filterData.isType(FilterDataType.DELETEINFO)) {
                sql.add(filterData);
            } else if (filterData.isType(FilterDataType.FROMINFO)) {
                sql.add(filterData);
            } else if (filterData.isType(FilterDataType.JOININFO)) {
                joinTables.add(filterData);
            } else if (filterData.isType(FilterDataType.WHEREINFO)) {
                existsWhere = true;
            } else if (filterData.isType(FilterDataType.EXPRESSIONINFO) || filterData.isType(FilterDataType.VALUEINFO) || filterData.isType(FilterDataType.OPERATORINFO)) {
                if (!existsWhere) {
                    joinConditions.add(filterData);
                } else {
                    whereConditions.add(filterData);
                }
            } else {
                others.add(filterData);
            }
        }
        if (joinTables.size() > 0) {
            String using = " USING ";
            String coma = "";
            for (FilterData filterData : joinTables) {
                using += coma + filterData.getTable() + " AS " + filterData.getTableAlias();
                coma = ", ";
            }
            FilterData filterData = new FilterData();
            filterData.setUsing(using);
            sql.add(filterData);
        }
        if (joinConditions.size() > 0 || whereConditions.size() > 0) {
            sql.add(new FilterData(FilterDataType.WHEREINFO));
            sql.addAll(whereConditions);
            if (whereConditions.size() > 0 && joinConditions.size() > 0) {
                sql.add(new FilterData().addOperatorInfo(SqlOperator.and.name()));
            }
            sql.addAll(joinConditions);
        }
        sql.addAll(others);
        deleteCriteria.getSql().clear();
        deleteCriteria.getSql().addAll(sql);
        return process(platkmEntityManager, deleteCriteria, param);
    }

    /**
     * 	@Override
     * 	public ProcessResult processSelectOptions(PlatkmEntityManager platkmEntityManager, QuerySelect querySelect,
     * 			String tableName, String keyColumn, String textColumns, WhereCriteria whereCriteria) throws DatabaseValidationException {
     *
     * 		List<QuerySyntax> listQuerySyntax =  platkmEntityManager.getQueryManager().getAllQuerySyntax();
     * 		String concant = _getQuerySyntaxValue(listQuerySyntax, "concat");
     * 		concant = concant + "' '"  + concant;
     * 		String sqlLower = _getQuerySyntaxValue(listQuerySyntax, "sql_lower");
     *
     * 		String sql1 = querySelect.getSelect() + " " + querySelect.getFrom() + " " + (querySelect.getWhere() == null ?"":querySelect.getWhere());
     * 		sql1 = sql1.replace("${id}", keyColumn).
     * 				  replace("${description}", getConcat(concant, sqlLower, textColumns)).
     * 				  replace("${tableName}", tableName);
     *
     * 		StringBuilder sb = new StringBuilder();
     * 	    sb.append(sql1);
     * 	    List<Object> parameters = new ArrayList<Object>();
     * 	    ProcessResult processResult;
     * 	    //the sql supose to be only where sentences
     * 	    String strWhere = "";
     * 	    String strQueryWhere = querySelect.getWhere();
     * 	    boolean withWhere = false;
     *
     * 	    if(whereCriteria == null) {
     * 	    	processResult = new ProcessResult();
     * 	    	processResult.setParameters(parameters);
     *
     * 	    	if(StringUtils.isNotBlank(strQueryWhere)) {
     * 		    	sb.append(strQueryWhere);
     * 		    	withWhere = true;
     * 		    }
     *
     * 	    }else {
     *
     * 	    	whereCriteria.getSql().removeIf(p -> p.getFilterDataType().name().equalsIgnoreCase(FilterDataType.WHEREINFO.name()));
     * 	    	processResult = processCriteria(platkmEntityManager, whereCriteria.getSql(), parameters);
     * 	    	strWhere = processResult.getSbSQL();
     *
     * 	    	if(StringUtils.isNotBlank(strWhere)){//overwrite the where sentences from  querySelect
     * 		    	sb.append(" WHERE " + strWhere);
     * 		    	withWhere = true;
     * 		    }else if(StringUtils.isNotBlank(strQueryWhere)) {
     * 		    	sb.append(strQueryWhere);
     * 		    	withWhere = true;
     * 		    }
     *
     * 	    	for (String fastsearch : querySelect.getFastsearch()) {
     * 	    		processResult.getFastSearchInfo().getFastSearchList().add(fastsearch);
     * 			}
     *
     * 	    	//fastsearch
     * 	        if(processResult.getFastSearchInfo() != null && StringUtils.isNotBlank(processResult.getFastSearchInfo().getFastsearch())  && processResult.getFastSearchInfo().getFastSearchList()!= null && !processResult.getFastSearchInfo().getFastSearchList().isEmpty()){
     * 	        	//processResult.addSQL(processFastSearch(platkmEntityManager.getQueryManager().getAllQuerySyntax(), withWhere, processResult.getFastSearchInfo().getFastsearch(), parameters, processResult.getFastSearchInfo().getFastSearchList()));
     * 	        	sb.append( " " + processFastSearch(platkmEntityManager.getQueryManager().getAllQuerySyntax(), withWhere, processResult.getFastSearchInfo().getFastsearch(), parameters, processResult.getFastSearchInfo().getFastSearchList()));
     * 	        }
     *
     * 	    }
     *
     *         String orderBy = "";
     *
     *         //, order by
     *         if(processResult.getOrderBy().size()>0) {
     *         	String coma = "";
     *         	for (FilterData filterData : processResult.getOrderBy()) {
     *         		orderBy+= coma +  " ORDER BY " + filterData.getOrderColumn() + " " + getOrderByType(filterData.getOrderType());
     *         		coma = ",";
     * 			}
     *         }else {
     *         	orderBy = querySelect.getAdditional() == null? "": querySelect.getAdditional();
     *         } //queryCriteria.getArguments(
     *         sb.append(" " + orderBy + " ");
     *
     * 	    //setting arguments
     *         List<Object> listArgs = new ArrayList<>();
     *         listArgs.add("tableWhere=" 	 + processResult.getSbSQL());
     *         listArgs.add("tableGroupBy=" + processResult.getGroupBy());
     *         listArgs.add("orderBy="      + orderBy);
     *
     *         if(whereCriteria != null && whereCriteria.getArguments() != null) {
     *         	listArgs.addAll(whereCriteria.getArguments());
     *         }
     *
     * 	    String[] arg = listArgs.stream().toArray(String[]::new);
     *         String sql   = process(sb.toString(), arg, parameters);
     *
     *         processResult.addSQL(" " + orderBy);
     *
     *         sql = checkingArgs(sql, arg);
     *
     *         if(processResult.getOffSetInfo()!= null && processResult.getOrderBy()!= null && processResult.getOrderBy().size() >0){
     *         	//offset
     *         	sql = processOffset(processResult, sql, platkmEntityManager, parameters);
     *         }
     *         processResult.setSql(sql);
     *         logger.debug(processResult.getSql());
     * 		return processResult;
     *
     * 	}
     */
    private CharSequence getConcat(String concant, String sqlLower, String columns) {
        String[] arrayLikes = columns.split(",");
        String likesConcat = "";
        String aux = "";
        for (String column : arrayLikes) {
            likesConcat += aux + column;
            aux = concant;
        }
        return sqlLower + "(" + likesConcat + ") ";
    }

    /**
     * processOffset
     * @param processResult processResult
     * @param sql sql
     * @param platkmEntityManager platkmEntityManager
     * @param parameters parameters
     * @return String
     */
    protected String processOffset(ProcessResult processResult, String sql, PlatkmORMEntityManager platkmEntityManager, List<Object> parameters) {
        int pageCount = 0;
        int page = 0;
        int recordCount = processOffsetRecordCount(sql, platkmEntityManager, parameters);
        if (recordCount > 0) {
            if (StringUtils.isNotBlank(processResult.getAddtionalDataInfo())) {
                sql += " " + processResult.getAddtionalDataInfo();
            }
            page = processResult.getOffSetInfo().getPage();
            if (page < 1) {
                page = 1;
            }
            long offsetValue = processResult.getOffSetInfo().getRecordPerPage() * (processResult.getOffSetInfo().getPage() - 1);
            if (offsetValue < 0)
                offsetValue = 0;
            pageCount = (recordCount + processResult.getOffSetInfo().getRecordPerPage() - 1) / processResult.getOffSetInfo().getRecordPerPage();
            sql += " OFFSET " + offsetValue + " ROWS FETCH NEXT " + processResult.getOffSetInfo().getRecordPerPage() + " ROWS ONLY ";
        }
        processResult.setPage(page);
        processResult.setPageCount(pageCount);
        return sql;
    }

    /**
     * process
     * @param platkmEntityManager platkmEntityManager
     * @param querySelect querySelect
     * @param whereCriteria whereCriteria
     * @param param param
     * @param replacements replacements
     * @return ProcessResult
     * @throws DatabaseValidationException DatabaseValidationException
     */
    @Override
    public ProcessResult process(PlatkmORMEntityManager platkmEntityManager, QuerySelect querySelect, WhereCriteria whereCriteria, List<Object> param, String... replacements) throws DatabaseValidationException {
        List<Object> parameters = cloneList(param);
        StringBuilder sb = new StringBuilder();
        sb.append(querySelect.getSelect() + " " + (querySelect.getFrom() != null ? querySelect.getFrom() : ""));
        ProcessResult processResult;
        //the sql supose to be only where sentences
        String strWhere = "";
        String strQueryWhere = querySelect.getWhere();
        boolean withWhere = false;
        if (whereCriteria == null) {
            processResult = new ProcessResult();
            processResult.setParameters(parameters);
            if (StringUtils.isNotBlank(strQueryWhere)) {
                sb.append(strQueryWhere);
                withWhere = true;
            }
        } else {
            whereCriteria.getSql().removeIf(p -> p.getFilterDataType().name().equalsIgnoreCase(FilterDataType.WHEREINFO.name()));
            processResult = processCriteria(platkmEntityManager, whereCriteria.getSql(), parameters);
            strWhere = processResult.getSbSQL();
            if (StringUtils.isNotBlank(strWhere)) {
                //overwrite the where sentences from  querySelect
                sb.append(" WHERE " + strWhere);
                withWhere = true;
            } else if (StringUtils.isNotBlank(strQueryWhere)) {
                sb.append(strQueryWhere);
                withWhere = true;
            }
            for (String fastsearch : querySelect.getFastsearch()) {
                processResult.getFastSearchInfo().getFastSearchList().add(fastsearch);
            }
            //fastsearch
            if (processResult.getFastSearchInfo() != null && StringUtils.isNotBlank(processResult.getFastSearchInfo().getFastsearch()) && processResult.getFastSearchInfo().getFastSearchList() != null && !processResult.getFastSearchInfo().getFastSearchList().isEmpty()) {
                //processResult.addSQL(processFastSearch(platkmEntityManager.getQueryManager().getAllQuerySyntax(), withWhere, processResult.getFastSearchInfo().getFastsearch(), parameters, processResult.getFastSearchInfo().getFastSearchList()));
                sb.append(" " + processFastSearch(platkmEntityManager.getQueryManager().getAllQuerySyntax(), withWhere, processResult.getFastSearchInfo().getFastsearch(), parameters, processResult.getFastSearchInfo().getFastSearchList()));
            }
        }
        String orderBy = "";
        //, order by
        if (processResult.getOrderBy().size() > 0) {
            String coma = "";
            for (FilterData filterData : processResult.getOrderBy()) {
                orderBy += coma + " ORDER BY " + filterData.getOrderColumn() + " " + getOrderByType(filterData.getOrderType());
                coma = ",";
            }
        } else {
            orderBy = querySelect.getAdditional() == null ? "" : querySelect.getAdditional();
        }
        //queryCriteria.getArguments(
        sb.append(" " + orderBy + " ");
        //setting arguments
        List<Object> listArgs = new ArrayList<>();
        listArgs.add("tableWhere=" + processResult.getSbSQL());
        listArgs.add("tableGroupBy=" + processResult.getGroupBy());
        listArgs.add("orderBy=" + orderBy);
        if (whereCriteria != null && whereCriteria.getArguments() != null) {
            listArgs.addAll(whereCriteria.getArguments());
        }
        String[] arg = listArgs.stream().toArray(String[]::new);
        String sql = process(sb.toString(), arg, parameters);
        processResult.addSQL(" " + orderBy);
        sql = checkingArgs(sql, arg);
        if (replacements != null) {
            String[] aValue;
            for (int i = 0; i < replacements.length; i++) {
                aValue = replacements[i].split("=");
                sql = sql.replaceAll("(?i)" + Pattern.quote("${" + aValue[0] + "}"), aValue[1]);
                //sql = sql.replace(aValue[0], aValue[1]);
            }
        }
        //offset
        if (processResult.getOffSetInfo() != null && processResult.getOrderBy() != null) {
            sql = processOffset(processResult, sql, platkmEntityManager, parameters);
        }
        logger.debug(sql);
        processResult.setSql(sql);
        return processResult;
    }

    /**
     * processOffsetRecordCount
     * @param sql sql
     * @param platkmEntityManager platkmEntityManager
     * @param parameters parameters
     * @return Integer
     */
    protected Integer processOffsetRecordCount(String sql, PlatkmORMEntityManager platkmEntityManager, List<Object> parameters) {
        String sqlCount = "SELECT count('x') as cantidad FROM (" + sql + " OFFSET 0 ROWS) AS vwSelect";
        Query query = platkmEntityManager.createNativeQuery(sqlCount);
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); i++) query.setParameter(i + 1, parameters.get(i));
        }
        Integer recordCount = 0;
        Object obj = query.getSingleResult();
        if (obj != null)
            recordCount = Integer.valueOf(((Object[]) obj)[0].toString());
        return recordCount;
    }

    /**
     * Constructor SqlSentencesProcessorSqlServer
     */
    public SqlSentencesProcessorSqlServer() {
        super();
    }
}
