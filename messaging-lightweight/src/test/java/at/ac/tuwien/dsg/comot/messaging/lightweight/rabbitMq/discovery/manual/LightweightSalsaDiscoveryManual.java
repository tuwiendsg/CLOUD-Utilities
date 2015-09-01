/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.discovery.manual;

import at.ac.tuwien.dsg.comot.messaging.lightweight.rabbitMq.discovery.LightweightSalsaDiscovery;
import at.ac.tuwien.dsg.comot.messaging.lightweight.util.Config;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * This test suite needs a running RabbitMQ cluster to work properly.
 * Due to the large amount of time which is needed to set this cluster up 
 * the tests in here are only intended as manual tests.
 * Please do execute them when working on the project!
 * 
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class LightweightSalsaDiscoveryManual {
	
	public LightweightSalsaDiscoveryManual() {
	}

	@BeforeMethod
	public void setUpMethod() throws Exception {
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
	}

	/**
	 * Test of getHost method, of class LightweightSalsaDiscovery.
	 */
	@Test
	public void testGetHost() {
		System.out.println("getHost");
		
		Config config = new Config();
		config.setSalsaIp("128.130.172.215");
		config.setSalsaPort(8080);
		config.setServiceName("ManualTestRabbitService");
		
		LightweightSalsaDiscovery instance = new LightweightSalsaDiscovery(config);
		String expResult = "10.99.0.65";
		String result = instance.discoverHost();
		assertEquals(result, expResult);
	}
	
}
