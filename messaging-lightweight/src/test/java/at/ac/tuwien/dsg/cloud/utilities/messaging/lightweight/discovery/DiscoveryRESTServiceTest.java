/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.discovery;

import at.ac.tuwien.dsg.cloud.utilities.messaging.lightweight.util.DiscoverySettings;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import junit.framework.Assert;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class DiscoveryRESTServiceTest {
	
	private WireMockServer discovery;
	private DiscoverySettings settings;
	private DiscoveryRESTService service;
	
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
		
		service = new DiscoveryRESTService(settings);
	}
	
	@After
	public void tearDown() {
		discovery.stop();
	}

	@Test
	public void testCheckForDiscovery() {
		Assert.assertFalse(service.checkForDiscovery());
		
		discovery.stubFor(WireMock
				.get(WireMock
						.urlEqualTo("/isDeployed"))
				.willReturn(WireMock.aResponse()
						.withStatus(HttpStatus.OK.value())));
		
		Assert.assertTrue(service.checkForDiscovery());
	}

	@Test
	public void testDiscoverHost() throws IOException {
		discovery.stubFor(WireMock
				.post(WireMock
						.urlEqualTo("/discover"))
				.withRequestBody(WireMock
						.equalToJson(readJsonFileHelper("DiscoveryRequest.json")))
				.willReturn(WireMock
						.aResponse()
						.withHeader("Content-Type", 
								MediaType.APPLICATION_JSON.toString())
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
