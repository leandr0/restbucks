/**
 * 
 */
package com.lrgoncalves.restbucks.repositories.neo4j;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import com.lrgoncalves.neo4j.Neo4JClient;


/**
 * @author lrgoncalves
 *
 */

@Named
public class Neo4jRepository {

	//private static final String JNDI_NEO4J_ENDPOINT = "java:global/neo4j/url/endpoint";

	
	
	//@Resource(lookup = JNDI_NEO4J_ENDPOINT)
	//protected URL graphDatabaseEndpoint;

	//private static final String NEO4J_ENDPOINT = "host";
    @Inject
	private static Neo4JClient neo4jClient;

	/*public synchronized Neo4JClient getGraphClient() throws IOException{

		if(neo4jClient == null){
			neo4jClient = Neo4JClient.getInstance();
		}
		
		return neo4jClient;
	}*/
	
	/**
	 * Fot unit tests
	 * @param graphDatabaseEndpoint
	 */
	/*public void setRepositoryEndpoint(URL graphDatabaseEndpoint){
		this.graphDatabaseEndpoint = graphDatabaseEndpoint;
	}*/
	
	/**
	 * Fot unit tests
	 * @param neo4jClient
	 */
	@SuppressWarnings("static-access")
	public void setNeo4jClient(Neo4JClient neo4jClient){
		this.neo4jClient = neo4jClient;
	}
}