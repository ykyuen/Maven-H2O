/**
 * 
 */
package hk.hku.cecid.edi.sfrm.com;

import java.io.File;
import java.io.IOException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.ArrayList;

import hk.hku.cecid.edi.sfrm.spa.SFRMProcessor;

import hk.hku.cecid.piazza.commons.io.Archiver;

import hk.hku.cecid.piazza.commons.util.Instance;
import hk.hku.cecid.piazza.commons.util.InstanceException;

/**
 * The outgoing payloads repository retrieves the payload 
 * which is in the form of zip files.<br><br> 
 * 
 * Creation Date: 5/10/2006
 * 
 * @author Twinsen Tsang
 * @version 1.0.2
 * @since	1.0.0
 */
public class PackagedPayloadsRepository extends PayloadsRepository {
	
	/**
	 * The packaged payload extension. 
	 */
	public static final String PACKAGE_EXT = ".sfrm";
	
	/**
	 * The archiver to pack / unpack the packaged payload.
	 */
	private Archiver archiver = null;
			
	/**
	 * Invoke for component initialization.
	 */
	protected void init() throws Exception {
		super.init();
        Properties params = getParameters();
        Instance ins = new Instance(params.getProperty("archiver"), this
				.getClass().getClassLoader());
        Object obj	 = ins.getObject();
        if (!(obj instanceof Archiver))
        	throw new InstanceException("Invalid Instance for the archiver.");
        this.archiver = (Archiver) ins.getObject();
	}

	/**
	 * @return A set of directories which contains the payloads set.
	 */
	private Collection getPayloads(String regex){
		// Get all directories without "~" symbol.
		Collection dirs = this.getRepositorySystem().getFiles(false, regex);
		// Use array list is the fastest.
		Collection payloads = new ArrayList();
		
		Iterator   itr		= dirs.iterator();  
		while(itr.hasNext()){
			try{
				PackagedPayloads payload = new PackagedPayloads((File)itr.next(), this);
				// Set the default archiver for each payload.
				payload.setDefaultArchiver(this.archiver);
				payloads.add(payload);			
			}catch(IOException ioe){
				SFRMProcessor.core.log.error("IO Error in Packaged payloads Repository",ioe);
			}
		}
		return payloads;
	}
	
	/**
     * @return return a set of packaged payload.
     */
	public Collection getPayloads() {
		return this.getPayloads("[^\\~\\.|^\\#\\#|\\%\\%].*");
	}
	
	/**
     * @return Get the list of processing payloads in the payload repositoy;
     */
    public Collection getProcessingPayloads(){
    	return this.getPayloads("[^\\~\\.|^\\%\\%].*");
    }
	
	/**
     * Create a customizing payloads for the specified 
     * parameter.<br><br>
     * 
     * Since the packaged payloads is in the form of 
     * &lt;partnership_id&gt;$&lt;message_id&gt;, 
     * so the size of parameters size should have at least 
     * 2. 
     * 
     * @param params
     * 			An array object parameters set for creating the 
     * 			payload.
     * @throws IllegalArgumentException
     * 			if the size of parameters is smaller than 2. 
     */
    public NamedPayloads createPayloads(Object[] params, int initialState) throws Exception {
    	if (params.length < 2)
    		throw new IllegalArgumentException(
					"Not enough parameters for creating payload.");
    	String payloadName = params[0].toString() + 
    						 NamedPayloads.decodeDelimiters + 
    						 params[1].toString() + PACKAGE_EXT;    	    	
    	PackagedPayloads pp = 
    		new PackagedPayloads(payloadName, initialState, this);    	
    	pp.setDefaultArchiver(this.archiver);
    	return pp;
    }
    
    /**
     * Get a particular payload in the payload repository 
     * by the specified parameters.
     *   
     * Since the outgoing payloads is in the form of 
     * &lt;partnership_id&gt;$&lt;message_id&gt;, 
     * so the size of parameters size should have at least 
     * 2.    
     *   
     * @param params
     * 			An array object parameters set for creating the 
     * 			payload.      		
     * @param state
     * 			The current state of that payload.	
     * @return the payload with the specified params or null
     * 		   if it does not exist.
     */
    public NamedPayloads getPayload(Object[] params, int state){
    	if (params.length < 2)
    		throw new IllegalArgumentException(
					"Not enough parameters for getting payload.");
    	String payloadName = NamedPayloads.getStateForm(state) + 
    						 params[0].toString() + 
    						 NamedPayloads.decodeDelimiters + 
    						 params[1].toString() + PACKAGE_EXT;
    	File f = new File(this.getRepositoryPath(), payloadName);
    	if (f.exists())
    		return this.createPayloadsProxy(f);
    	return null;
    }

	/**
     * Create a customizing payloads for this repository.
     * 
     * @param proxyObj
     * 			The file object for the payloads.
     *  
     * @return a customizing payloads.
     */
    protected NamedPayloads createPayloadsProxy(File proxyObj){
    	try{
    		PackagedPayloads pp = new PackagedPayloads(proxyObj, this);
    		pp.setDefaultArchiver(this.archiver);
    		return pp;
    	}catch(IOException ioe){
    		return null;
    	}
    }	
    
    /**
     * toString method
     */
    public String toString(){
    	StringBuffer ret = new StringBuffer(super.toString());
    	ret .append("Extension: " + PACKAGE_EXT);
    	return ret.toString();
    }
}
