/**
 * 
 */
package com.lrgoncalves.restbucks.domain.query;

import com.lrgoncalves.neo4j.CypherQueryType;

/**
 * @author lrgoncalves
 *
 */
public enum OrderCypherQueryType implements CypherQueryType {


	SEARCH_ITENS_BY_ORDER("MATCH (order:Order {identifier: '$identifier'} )-[*]->(item:Item) return item"),
	SEARCH_LINKS_BY_ORDER("MATCH (order:Order {identifier: '$identifier'} )-[*]->(hypermedia:Hypermedia)-[*]->(link:Link) return link"),
	FIND_BY_IDENTIFIER("MATCH (order:Order {identifier: '$identifier'} ) return order"),
	FIND_BY_IDENTIFIER_RELATIONSHIPS("MATCH (order:Order {identifier: '$identifier'}) OPTIONAL MATCH (order)-[relationships]-() return order,relationships"),
	FIND_FIRST_PATH_BY_IDENTIFIER("MATCH (order:Order {identifier: '$identifier'} ) -[*0..1]-(nodes) return nodes , labels(nodes)");

	private String query;

	private OrderCypherQueryType(final String query){
		this.query = query;
	};

	@Override
	public String getQuery() {
		return query;
	}

}
