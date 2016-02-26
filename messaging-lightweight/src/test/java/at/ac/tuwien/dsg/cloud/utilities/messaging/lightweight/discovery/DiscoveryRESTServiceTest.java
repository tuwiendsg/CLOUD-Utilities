/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.JacksonSerializer;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DiscoveryRESTServiceTest {
	
	private WireMockServer discovery;
	private DiscoverySettings settings;
	private RestServiceDiscovery service;
	
	public DiscoveryRESTServiceTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@Before
	public void setUp() {
		settings = new DiscoverySettings();
		settings.setIp("127.0.0.1");
		settings.setPort(8009);
		settings.setServiceName("testRabbit");
		
		discovery = new WireMockServer(settings.getPort());
		discovery.start();
		
		service = new RestServiceDiscovery(settings, new JacksonSerializer());
	}
	
	@After
	public void tearDown() {
		discovery.stop();
	}

//	@Test
//	public void testCheckForDiscovery() {
//		Assert.assertFalse(service.checkForDiscovery());
//		
//		discovery.stubFor(WireMock
//				.get(WireMock
//						.urlEqualTo("/isDeployed"))
//				.willReturn(WireMock.aResponse()
//						.withStatus(200)));
//		
//		Assert.assertTrue(service.checkForDiscovery());
//	}

	@Test
	public void testDiscoverHost() throws Exception {
		discovery.stubFor(WireMock
				.post(WireMock
						.urlEqualTo("/discover"))
				.withRequestBody(WireMock
						.equalToJson(readJsonFileHelper("DiscoveryRequest.json")))
				.willReturn(WireMock
						.aResponse()
						.withHeader("Content-Type", "application/json")
						.withBody(readJsonFileHelper("DiscoveryResponse.json"))));
		
		Assert.assertEquals("127.0.0.12", service.discoverHost());
	}
	
	private static String readJsonFileHelper(String fileName) throws IOException {
		return IOUtils.toString(Thread
				.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(fileName));
	}
	
}
