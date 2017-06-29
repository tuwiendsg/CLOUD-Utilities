/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestContext.class)//, initializers = ConfigFileApplicationContextInitializer.class)
public class UserControllerTest {
	
	private static WireMockServer kong;
	private MockMvc mock;

	@Autowired
	private UserController userController;
	
	public UserControllerTest() {
	}
	
	private static String readJsonFileHelper(String fileName) throws IOException {
		return IOUtils.toString(Thread
				.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(fileName));
	}
	
	@BeforeClass
	public static void setUpClass() throws IOException {
		kong = new WireMockServer(8001);

		kong.start();
		
		kong.stubFor(WireMock
				.get(WireMock
						.urlEqualTo(KongURIs.KONG_CONSUMERS_URI))
				.willReturn(WireMock
						.aResponse()
						.withHeader("Content-Type", 
								MediaType.APPLICATION_JSON.toString())
						.withBody(readJsonFileHelper("UsersList.json"))));
	}
	
	@AfterClass
	public static void tearDown() {
		kong.stop();
	}
	
	@Before
	public void setUp() {
		mock = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void testCheck() throws Exception {		
		
		String checkPath = UserController.REST_CONTROLLER_PATH 
				+ UserController.REST_CHECK_PATH;
		
		mock.perform(MockMvcRequestBuilders
				.post(URI.create(checkPath))
				.content("test1"))
				.andExpect(MockMvcResultMatchers
						.content()
						.string("true"));

		mock.perform(MockMvcRequestBuilders
				.post(URI.create(checkPath))
				.content("testWrong"))
				.andExpect(MockMvcResultMatchers
						.content()
						.string("false"));
	}

	@Test
	public void testRegister() throws Exception {
		kong.stubFor(WireMock
				.post(WireMock
						.urlEqualTo(KongURIs.KONG_CONSUMERS_URI))
				.withHeader("Content-Type", WireMock.equalTo(
						MediaType.APPLICATION_JSON.toString()))
				.withRequestBody(WireMock.equalToJson(
						readJsonFileHelper("UserCreateRequest.json")))
				.willReturn(WireMock
						.aResponse()
						.withStatus(201)
						.withHeader("Content-Type", 
								MediaType.APPLICATION_JSON.toString())
						.withBody(readJsonFileHelper("User.json"))));
		
		String keyUri = KongURIs.KONG_CONSUMERS_URI 
				+ "/max" 
				+ KongURIs.KONG_CONSUMER_KEY_AUTH;
		
		kong.stubFor(WireMock
				.post(WireMock
						.urlEqualTo(keyUri))
				.willReturn(WireMock
						.aResponse()
						.withStatus(201)
						.withHeader("Content-Type", 
								MediaType.APPLICATION_JSON.toString())
						.withBody(readJsonFileHelper("UserKeyAuth.json"))));
		
		String maxPath = UserController.REST_CONTROLLER_PATH + "/max";
		
		mock.perform(MockMvcRequestBuilders
				.put(URI.create(maxPath)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers
						.content()
						.string("62eb165c070a41d5c1b58d9d3d725ca1"));

		kong.stubFor(WireMock
				.post(WireMock
						.urlEqualTo(KongURIs.KONG_CONSUMERS_URI))
				.withHeader("Content-Type", WireMock.equalTo(
						MediaType.APPLICATION_JSON.toString()))
				.withRequestBody(WireMock
						.equalToJson(readJsonFileHelper("UserCreateRequest.json")))
				.willReturn(WireMock
						.aResponse()
						.withStatus(409)
						.withHeader("Content-Type", 
								MediaType.APPLICATION_JSON.toString())
						.withBody("User exists!")));
		
		mock.perform(MockMvcRequestBuilders
				.put(URI.create(maxPath)))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError());
	}

	@Test
	public void testRemove() throws Exception {
		kong.stubFor(WireMock
				.delete(WireMock
						.urlEqualTo(KongURIs.KONG_CONSUMERS_URI
								+ "/79a85605-590e-44a7-c4f1-14fdf00b428d"))
				.willReturn(WireMock
						.aResponse()
						.withStatus(204)));
		
		mock.perform(MockMvcRequestBuilders
				.delete(URI.create(UserController.REST_CONTROLLER_PATH 
						+ "/test")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
}
