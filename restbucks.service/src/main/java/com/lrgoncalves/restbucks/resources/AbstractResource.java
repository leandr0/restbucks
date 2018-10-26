/**
 * 
 */
package com.lrgoncalves.restbucks.resources;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author lrgoncalves
 *
 */
public class AbstractResource {
	
	protected final Log LOGGER; 

	/**
	 * 
	 */
	public AbstractResource(Class<? extends AbstractResource> clss) {
		LOGGER = LogFactory.getLog(clss);
	}
}
