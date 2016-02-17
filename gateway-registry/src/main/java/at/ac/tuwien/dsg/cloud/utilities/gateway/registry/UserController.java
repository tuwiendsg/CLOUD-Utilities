/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.ac.tuwien.dsg.cloud.utilities.gateway.registry;

import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongURIs;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUser;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUserCreateDto;
import at.ac.tuwien.dsg.cloud.utilities.gateway.registry.kongDtos.KongUserList;
import java.net.URI;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

/**
 *
 * @author Svetoslav Videnov <s.videnov@dsg.tuwien.ac.at>
 */
@RestController
@RequestMapping(value = UserController.REST_CONTROLLER_PATH)
public class UserController {

	public static final String REST_CONTROLLER_PATH = "/users";
	public static final String REST_CHECK_PATH = "/check";
	public static final String REST_USER_PATH_VARIABLE = "/{user}";

	private static Logger logger = LoggerFactory.getLogger(UserController.class);
	private KongUserList kongUsers;

	private KongURIs kongUris;
	private RestUtilities restUtilities;

	@Autowired
	public UserController(KongURIs kongURIs, RestUtilities restUtilities) {
		this.kongUris = kongURIs;
		this.restUtilities = restUtilities;
	}

	@PostConstruct
	public void init() {
		RequestEntity<Void> req = RequestEntity
				.get(URI.create(this.kongUris.getKongConsumersUri()))
				.build();

		try {
			ResponseEntity<KongUserList> resp = restUtilities
					.simpleRestExchange(req, KongUserList.class);
			this.kongUsers = resp.getBody();
		} catch (ResourceAccessException ex) {
			this.kongUsers = new KongUserList();
		}
	}

	@RequestMapping(method = RequestMethod.POST,
			value = REST_CHECK_PATH)
	public boolean check(@RequestBody String user) {
		logger.info("User {} requested authentication check!", user);
		return this.kongUsers.getUsers().stream().anyMatch((u)
				-> u.getUserName().equals(user));
	}

	@RequestMapping(method = RequestMethod.PUT,
			value = REST_USER_PATH_VARIABLE)
	public ResponseEntity<String> register(@PathVariable String user) {

		RequestEntity<KongUserCreateDto> request = RequestEntity
				.post(URI.create(this.kongUris.getKongConsumersUri()))
				.contentType(MediaType.APPLICATION_JSON)
				.body(KongUserCreateDto.build(user));

		ResponseEntity<KongUser> resp = restUtilities
				.simpleRestExchange(request, KongUser.class);

		if (resp == null || resp.getStatusCode() != HttpStatus.CREATED) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		this.kongUsers.getUsers().add(resp.getBody());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE,
			value = REST_USER_PATH_VARIABLE)
	public ResponseEntity<String> remove(@PathVariable String user) {
		boolean res = this.kongUsers.getUsers().removeIf((KongUser u) -> {
			if (u.getUserName().equals(user)) {
				RequestEntity<Void> request = RequestEntity
						.delete(URI.create(this.kongUris
								.getKongConsumerIdUri(u.getId())))
						.accept(MediaType.ALL)
						.build();

				ResponseEntity<String> resp = restUtilities.simpleRestExchange(request, String.class);

				if (resp == null || resp.getStatusCode() != HttpStatus.NO_CONTENT) {
					return false;
				}

				return true;
			}
			return false;
		});

		if (!res) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
