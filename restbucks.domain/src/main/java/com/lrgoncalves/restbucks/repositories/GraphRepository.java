/**
 * 
 */
package com.lrgoncalves.restbucks.repositories;

import java.io.IOException;

import com.lrgoncalves.restbucks.domain.Identifier;
import com.lrgoncalves.restbucks.repositories.neo4j.RepositoryStoreException;

/**
 * @author lrgoncalves
 *
 */
public interface GraphRepository <T> {

	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public abstract T get(final Identifier identifier)throws IOException;
	
	/**
	 * 
	 * @param identifier
	 * @return
	 */
	public abstract T take(final Identifier identifier)throws IOException;
	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws RepositoryStoreException
	 */
	public abstract Identifier store(T entity)throws RepositoryStoreException;
	
	/**
	 * 
	 * @param identifier
	 * @param entity
	 * @throws RepositoryStoreException
	 */
	public abstract void store(Identifier identifier, T entity)throws RepositoryStoreException;
	
	/**
	 * 
	 * @param identifier
	 */
	public abstract void remove(Identifier identifier)throws IOException;
}
