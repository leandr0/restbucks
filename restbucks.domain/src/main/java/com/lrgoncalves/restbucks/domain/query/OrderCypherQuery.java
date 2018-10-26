package com.lrgoncalves.restbucks.domain.query;

import java.util.HashMap;
import java.util.Map;

import com.lrgoncalves.neo4j.CypherQuery;
import com.lrgoncalves.neo4j.QueryCypherParamType;

public class OrderCypherQuery implements CypherQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6677215952390794805L;

	private Map<QueryCypherParamType, Object> parameters = new HashMap<QueryCypherParamType, Object>();
	
	@Override
	public void setParameters(Map<QueryCypherParamType, Object> parameters) {
		this.parameters = parameters;
	}

	@Override
	public Map<QueryCypherParamType, Object> getParameters() {
		return this.parameters;
	}

	@Override
	public void setParameter(QueryCypherParamType queryCypherParamType, final Object value) {
		parameters.put(queryCypherParamType, value);
	}
}