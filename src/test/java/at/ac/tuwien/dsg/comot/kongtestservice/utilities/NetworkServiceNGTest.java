/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.comot.kongtestservice.utilities;

import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
public class NetworkServiceNGTest {
	
	public NetworkServiceNGTest() {
	}

	@BeforeMethod
	public void setUpMethod() throws Exception {
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
	}

	/**
	 * Test of getIp method, of class NetworkService.
	 */
	@Test
	public void testGetIp() throws Exception {
		System.out.println("getIp");
		String expResult = "128.131.";
		String result = NetworkService.getIp();
		assertTrue(result.startsWith(expResult));
	}
	
}
