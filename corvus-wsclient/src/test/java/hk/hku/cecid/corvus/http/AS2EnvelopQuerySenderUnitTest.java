/* 
 * Copyright(c) 2005 Center for E-Commerce Infrastructure Development, The
 * University of Hong Kong (HKU). All Rights Reserved.
 *
 * This software is licensed under the GNU GENERAL PUBLIC LICENSE Version 2.0 [1]
 * 
 * [1] http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt
 */

package hk.hku.cecid.corvus.http;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import hk.hku.cecid.piazza.commons.test.utils.FixtureStore;
import hk.hku.cecid.piazza.commons.io.IOHandler;
import hk.hku.cecid.corvus.http.EnvelopQuerySenderUnitTest;
import hk.hku.cecid.corvus.util.FileLogger;
import hk.hku.cecid.corvus.ws.data.AS2AdminData;

/** 
 * The <code>AS2EnvelopQuerySenderUnitTest</code> is unit test of <code>AS2EnvelopQuerySender</code>.
 *
 * @author 	Twinsen Tsang
 * @version 1.0.0
 * @since   JDK5.0, H2O 0908
 */
public class AS2EnvelopQuerySenderUnitTest extends EnvelopQuerySenderUnitTest 
{
	// Fixture name.
	public static final String 	TEST_LOG 		= "test.log";
	// Fixture loader
	private static ClassLoader 	FIXTURE_LOADER	= FixtureStore.createFixtureLoader(false, AS2EnvelopQuerySenderUnitTest.class);
	
	/** 
	 * The testing target which is an AS2EnvelopQuerySender and the associated data. 
	 * The testing target variable is available in the super class.
	 */
	/* private AS2PartnershipSender 	target */ 	
	private AS2AdminData  adata;		
	
	/** Initialize the test data **/
	public void initTestData()
	{
		super.initTestData();		
		// Create a EBMS administration data.
		this.adata = new AS2AdminData();
		this.adata.setUsername(USER_NAME);
		this.adata.setPassword(PASSWORD);
		this.adata.setEnvelopQueryEndpoint(TEST_ENDPOINT);		 						
		this.adata.setMessageIdCriteria("test-message-id");
		this.adata.setMessageBoxCriteria("INBOX");
	}
	
	/** Initialize the test target which is a PartnershipSender. */
	public void initTestTarget() throws Exception 
	{
		URL logURL = FIXTURE_LOADER.getResource(TEST_LOG);
		if (logURL == null)
			throw new NullPointerException("Missing fixture " + TEST_LOG + " in the fixture path");
				
		File log = new File(logURL.getFile());
		this.testClassLogger = new FileLogger(log);		
		// Create an AS2EnvelopQuerySender sender.
		this.target = new AS2EnvelopQuerySender(this.testClassLogger, this.adata);
		// Set something for preventing throwing exception		
		this.target.setMessageCriteriaToDownload("test-message-id", "INBOX");
		
	}
		
	/* (non-Javadoc)
	 * @see hk.hku.cecid.corvus.http.EnvelopQuerySenderUnitTest#testEnvelopQuery()
	 */
	public void testEnvelopQuery() throws Exception {
		super.testEnvelopQuery();
		InputStream eins = this.target.getEnvelopStream();
		String envelop = IOHandler.readString(eins, null);
		this.logger.info(envelop);
	}
	
	/* 
	 * Used for debug purpose only ! Because it depends H2O has been started up property ! 
	 */	
	/*
	 public void testEnvelopQueryToH2O() throws Exception {
	 	// To use this test-case, change the value in the message criteria and the end-point.
		// Also one thing ! Your H2O MUST start up properly and that message must exist !.
		this.target.setMessageCriteriaToDownload("20071113-064159-14623@127.0.1.1", "INBOX");
		this.target.setServiceEndPoint("http://localhost:8080/corvus/admin/as2/repository");
		this.target.run();
		InputStream eins = this.target.getEnvelopStream();
		String envelop = IOHandler.readString(eins, null);
		this.logger.info(envelop);
	}*/
}
