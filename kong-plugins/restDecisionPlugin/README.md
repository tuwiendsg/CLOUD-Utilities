# Kong RestDecisionPlugin

## Adding the plug-in to your Kong instance

1. After cloning the repository or just the needed plug-in folder you have the following two possibilities:
	* Make a dynamic link in the plug-ins folder of Kong. (recommended)

		```
		ln -s /path/to/repo/CLOUD-Utilities/kong-plugins/restDecisionPlugin /path/to/kong/plugins/restDecisionPlugin`
		```
		The advantage here is that you can simply pull the repository for updates and your plug-in code will be simply updated.
	* Copy the plug-in folder into the Kong plug-ins folder. (not-recommended)
	* Tip: If you do not know where your Kong plug-ins folder is try: `sudo find / -path */kong/plugins`
2. Now you have to enable the plug-in in your Kong config

	```
	## Available plug-ins on this server
	plugins_available:
	  - ssl
	  - jwt
	  - acl
	  - cors
	  - oauth2
	  - tcp-log
	  - udp-log
	  - file-log
	  - http-log
	  - key-auth
	  - hmac-auth
	  - basic-auth
	  - ip-restriction
	  - mashape-analytics
	  - request-transformer
	  - response-transformer
	  - request-size-limiting
	  - rate-limiting
	  - response-ratelimiting
	  - restDecisionPlugin ## Add this!
	```
	Again `find` is your friend: `sudo find / -name kong.yml`
3. Restart your Kong instance: `kong restart`

## Activating all necessary plug-ins.

- You need the enable an authentication plugin
```
curl -X POST http://{KONG_IP}:8001/apis/{API}/plugins --data "name=key-auth"
```

- You need the enable the restDecisionPlugin
```
curl -X POST -H "Content-Type: application/json" -d '{                        
    "name": "restDecisionPlugin",
    "config": {
        "url": "http://{GATEWAY_REGISTRY_IP}:{GATEWAY_REGISTRY_PORT}/users/check"
    }
}'  http://{KONG_IP}:8001/apis/{API}/plugins
```

- Create a consumer

- Register the consumer with our gateway
