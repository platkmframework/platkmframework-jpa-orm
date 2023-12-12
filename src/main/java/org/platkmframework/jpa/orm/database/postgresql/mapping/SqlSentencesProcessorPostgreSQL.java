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
package org.platkmframework.jpa.orm.database.postgresql.mapping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.platkmframework.common.domain.filter.criteria.DeleteCriteria;
import org.platkmframework.common.domain.filter.criteria.base.ConditionFilterBase;
import org.platkmframework.common.domain.filter.enumerator.ConditionOperator;
import org.platkmframework.common.domain.filter.enumerator.SqlOperator;
import org.platkmframework.common.domain.filter.enumerator.SqlOrder;
import org.platkmframework.common.domain.filter.info.FilterData;
import org.platkmframework.common.domain.filter.info.FilterDataType;
import org.platkmframework.database.query.manager.model.QuerySyntax;
import org.platkmframework.jpa.base.PlatkmEntityManager;
import org.platkmframework.jpa.exception.DatabaseValidationException;
import org.platkmframework.jpa.processor.ProcessResult;
import org.platkmframework.jpa.processor.SqlSentencesProcessorBase;
 


/**
 *   Author: 
 *     Eduardo Iglesias
 *   Contributors: 
 *   	Eduardo Iglesias - initial API and implementation
 **/
public class SqlSentencesProcessorPostgreSQL extends SqlSentencesProcessorBase {


	private static final Logger logger = LogManager.getLogger(SqlSentencesProcessorPostgreSQL.class);
	
	
	protected String getExpression(String colName, String operator, Object value, List<Object> param) {
		
		String sql = " ";
	    if(ConditionOperator.startWith.name().equals(operator))
	    {
	    	sql =  "LOWER("+ colName + ") like LOWER(?) ";
	    	if(value!=null)
	    		value =  value.toString() + '%' ;
	    }else if(ConditionOperator.contain.name().equals(operator))
	    {
	    	sql = "LOWER("+ colName + ") like LOWER(?) ";
	    	if(value!= null)
	    		value = '%' + value.toString() + '%';
	    }
	    else if(ConditionOperator.notContain.name().equals(operator))
	    {
	    	sql = "LOWER("+ colName + ") NOT like LOWER(?) ";
	    	if(value != null)
	    		value = '%' + value.toString() + '%';
	    }
	    else if(ConditionOperator.endWith.name().equals(operator))
	    {
	    	sql = "LOWER(" + colName + ") like LOWER(?) ";
	    	if(value != null)
	    		value = '%' + value.toString() ;
	    } 
	    else if(ConditionOperator.equal.name().equals(operator))  
	    	sql = colName + " =? ";
	    else if(ConditionOperator.notEqual.name().equals(operator)) 
	    	sql = colName +  "  !=?  ";
	    else if(ConditionOperator.greateThan.name().equals(operator)) 
	    	sql = colName +  " >?  ";
	    else if(ConditionOperator.greateEqual.name().equals(operator)) 
	    	sql = colName +  " >=?  ";
	    else if(ConditionOperator.lessThan.name().equals(operator))
	    	sql = colName +  " <?  "; 
	    else if(ConditionOperator.lessEqual.name().equals(operator))
	    	sql = colName +  " <=?  ";  
	    else if(ConditionOperator.include.name().equals(operator))
	    	sql = colName +  " in (?) ";
	    else if(ConditionOperator.notInclude.name().equals(operator))
	    	sql = colName +  " NOT in (?) ";
	    else if(ConditionOperator.isNull.name().equals(operator))
	    	sql = colName +  " IS NULL ";
	    else if(ConditionOperator.isNotNull.name().equals(operator))
	    	sql = colName +  " IS NOT NULL ";
	     
	    if( !ConditionOperator.isNull.name().equals(operator)
	    		&& !ConditionOperator.isNotNull.name().equals(operator)
	    		&& param!=null)
	    	param.add(value);
	    
	    //if(condOperator.getValueCondition() != null)
	    //	condOperator.getValueCondition().setValue(value);
	    
	    return sql;
	}
	
	protected String getExpression(FilterData expression, List<Object> param) throws DatabaseValidationException {
		String sql = " ";
		if(expression.isValueAsColumn()){ 
			Object value1, value2;
			if(expression.getFilterDataType().name().equals(FilterDataType.VALUEINFO.name())){
				value1 = expression.getValue1();
				value2 = expression.getValue2();
			}else {
				value1 = expression.getColumn();
				value2 = expression.getExpesionValue();
			}
			String operator = "";
			
			if(ConditionOperator.equal.name().equals(expression.getExpesionOperator().name())) {  
				operator = " = ";
		    } else if(ConditionOperator.notEqual.name().equals(expression.getExpesionOperator().name()))  { 
		    	operator = " != ";
		    }else if(ConditionOperator.greateThan.name().equals(expression.getExpesionOperator().name()))  { 
		    	operator = " > "; 
		    } else if(ConditionOperator.greateEqual.name().equals(expression.getExpesionOperator().name())) {  
		    	operator = " >= "; 
		    }else if(ConditionOperator.lessThan.name().equals(expression.getExpesionOperator().name())) { 
		    	operator = " < "; 
		    } else if(ConditionOperator.lessEqual.name().equals(expression.getExpesionOperator().name())) { 
		    	operator = " <= "; 
		    }else {
		    	logger.error("no se encontro operador para expresion que no son campos");
		    	throw new DatabaseValidationException(" error consult admin");
		    } 
			
			sql = value1 + "  " + operator + "  " + value2 + "  " ;
		}else {
			sql = getExpression(expression.getColumn(), expression.getExpesionOperator().name(), expression.getExpesionValue() , param);
		}
		
		return sql;
	}
	
	
	protected String processFastSearch(List<QuerySyntax> listQuerySyntax, boolean withWhere, String fastsearchValue, List<Object> parameters, List<String> fastsearchList) {
		 
		String sql;
		if(withWhere)
			sql = " AND (";
		else
			sql = " WHERE (";
		
		String concant  = _getQuerySyntaxValue(listQuerySyntax, "concat");
		concant = concant + "' '"  + concant;
		String sqlLower = _getQuerySyntaxValue(listQuerySyntax, "sql_lower");
		
		String aux = "";
		for (String column : fastsearchList) {
			sql+= aux + sqlLower + "(" + column + ") ";
			aux = concant;
		}
		parameters.add("%" + fastsearchValue + "%" );
		return  sql + " like " + sqlLower + " (?) ) ";
	}

	@Override
	public String processWhere(List<Object> param, ConditionFilterBase whereFilter) throws DatabaseValidationException {
		String sql = "";
		if(whereFilter.getSql() != null &&  whereFilter.getSql().isEmpty()){  
        	for (FilterData ob : whereFilter.getSql()) {
        		
        		 if(ob.isType(FilterDataType.EXPRESSIONINFO)) {
        			 sql+= " " + getExpression(ob, param);
         			 
         		}else if(ob.isType(FilterDataType.OPERATORINFO)) {
         			 sql+= " " +  getOperator(ob.getSqlOperator()); 
         		}  
        	}
		}
		
		return StringUtils.isNotBlank(sql)? " WHERE " + sql : "";
	}

	@Override
	protected String getOrderByType(SqlOrder orderType) throws DatabaseValidationException {
		
		if(SqlOrder.asc.name().equals(orderType.name()))
			return "  ASC ";
		else if(SqlOrder.desc.name().equals(orderType.name()))
			return " DESC ";
		else{
	    	logger.error("no se encontro operaor para order by ");
	    	throw new DatabaseValidationException(" error consult admin");
	    }
	}

	@Override
	public ProcessResult removeProcess(PlatkmEntityManager platkmEntityManager, DeleteCriteria deleteCriteria, List<Object> param) throws DatabaseValidationException {
		
	List<FilterData> joinTables = new ArrayList<>(); 
		List<FilterData> others = new ArrayList<>();
		List<FilterData> sql = new ArrayList<>(); 
		List<FilterData> joinConditions = new ArrayList<>();
		List<FilterData> whereConditions = new ArrayList<>();
		boolean existsWhere = false;
		
		for (FilterData filterData : deleteCriteria.getSql()) {
			if(filterData.isType(FilterDataType.DELETEINFO)){
				sql.add(filterData);
			}else if(filterData.isType(FilterDataType.FROMINFO)) {
				sql.add(filterData);
			}else if(filterData.isType(FilterDataType.JOININFO)) {
				joinTables.add(filterData);
			}else if(filterData.isType(FilterDataType.WHEREINFO)) {
				existsWhere = true;
			}else if(filterData.isType(FilterDataType.EXPRESSIONINFO) || filterData.isType(FilterDataType.VALUEINFO ) || filterData.isType(FilterDataType.OPERATORINFO)) {
				if(!existsWhere){
					joinConditions.add(filterData);
				}else {
					whereConditions.add(filterData);
				}	
			}else {
				others.add(filterData);
			}
		}  
		
		if(joinTables.size()>0){ 
			String using = " USING ";
			String coma  = "";
			for (FilterData filterData : joinTables) {
				using+= coma + filterData.getTable() +  " AS "  + filterData.getTableAlias(); 
				coma= ", ";
			}
			FilterData filterData = new FilterData();
			filterData.setUsing(using);
			sql.add(filterData);
		}
		
		if(joinConditions.size() > 0 || whereConditions.size() > 0){
			sql.add(new FilterData(FilterDataType.WHEREINFO));
			
			sql.addAll(whereConditions); 
			
			if(whereConditions.size()>0 && joinConditions.size() > 0) {
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
	@Override
	public ProcessResult processSelectOptions(PlatkmEntityManager entyEntityManager, QuerySelect querySelect,
			String tableName, String keyColumn, String textColumns,  WhereCriteria whereCriteria) throws DatabaseValidationException {
	 
		List<QuerySyntax> listQuerySyntax =  entyEntityManager.getQueryManager().getAllQuerySyntax(); 
		String concant = _getQuerySyntaxValue(listQuerySyntax, "concat");
		concant = concant + "' '"  + concant;
		String sqlLower = _getQuerySyntaxValue(listQuerySyntax, "sql_lower");
		 
		String sql = querySelect.getSelect() + " " + querySelect.getFrom() + " " + (StringUtils.isBlank(querySelect.getWhere())?"":querySelect.getWhere()); 
		sql = sql.replace("${id}", keyColumn).
				  replace("${description}", getConcat(concant, sqlLower, textColumns)).
				  replace("${tableName}", tableName);
		
		
		ProcessResult processResult;
		if(whereCriteria != null) {
			List<Object> parameters = cloneList(null); 
			whereCriteria.getSql().removeIf(p -> p.getFilterDataType().name().equalsIgnoreCase(FilterDataType.WHEREINFO.name()));
			processResult = processCriteria(entyEntityManager, whereCriteria.getSql(), parameters); 
		}else {
			processResult = new ProcessResult(); 
		} 
		 
		if(StringUtils.isNotBlank(processResult.getSql())){
			 sql+=" and " + processResult.getSbSQL();
		} 
		processResult.setSql(sql + " " + querySelect.getAdditional());
		
		List<Object> parameters = new ArrayList<>();
		processResult.setParameters(parameters); 
		
		return processResult;
		
	}
*/
	private CharSequence getConcat(String concant, String sqlLower, String columns) {

		String[] arrayLikes = columns.split(",");
		String likesConcat = "";
		String aux = "";
		for (String column : arrayLikes) {
			likesConcat+= aux + column;
			aux = concant;
		}  
		return sqlLower + "(" + likesConcat + ") ";
	}

}
