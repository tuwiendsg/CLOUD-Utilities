# CLOUD-Utilities

This is a small collection of tools which aims to help the iCOMOT 
platform appearing more as one centrally accessible service to the user.

For more detailed descriptions please read the READMEs of the different tools.

## Tools

1. **Messaging API**

	The messaging API defines the interfaces which have to be provided by different message broker implementation like RabbitMQ or ActiveMQ so that they can be used for communication.

2. **Messaging Lightweight**

	Messaging lightweight is a library package which can be included to use one of the here provided messaging providers as communication platform.

	At the moment there is only an implementation for RabbitMQ given.

3. **Gateway Adapter**

	Is a library package which can be used for easily publishing new APIs to the common gateway.

	The adapter makes internally use of the Messaging API and Lightweight packages for establishing the communication with the gateway.

4. **Gateway Registry**

	This service is the endpoint which communicates and handles the requests of the services which are using the Gateway Adapter package regarding publishing deleting APIs on the common gateway. This service will pass all incoming requests on to the actual gateway.
	Further this service also provides a simple, experimental endpoint for the RestDecisionPlugin.

5. **Kong Plug-ins**

	At the moment the used gateway is Kong. The following custom plug-ins where developed:

	* **RestDecisionPlugin** - This plug-in allows the gateway to query another service for approval if a certain user is allowed to use a certain API or not.

6. **Kong Test Service**

	This is a small test service which will register an usable API at the gateway for demonstration purpose.

## Installation

For running the dynamic gateway where services can register and unregister as they are spawned you will need to provide the following additional components:
* RabbitMQ Server - If used with a CLOUD the Gateway Registry can automatically deploy the RabbitMQ Server
* Kong

Afterwards you only need to start the Gateway Registry.

### Deploying Kong
It is recomended to use the provided Kong Docker containers.
For installing Kong on a local machine please refer to the Kong manuals.
The current version is tested with Kong 0.7.0.

1. Before starting the Kong image we need to provide a Cassandra container.

	```
	docker run -p 9042:9042 -d --name cassandra cassandra:2.2.5
	```

2. Give it some time. Cassandra needs a bit to start up. You can check the progress with `docker logs cassandra`

3. Now we can start Kong.
	
	```
	docker run -d --name kong \                                
            --link cassandra:cassandra \
            -v /path/to/the/repository/CLOUD-Utilities/kong-plugins:/etc/kong/ \
            -v /path/to/the/repository/CLOUD-Utilities/kong-plugins/restDecisionPlugin/:/usr/local/share/lua/5.1/kong/plugins/restDecisionPlugin/ \
            -p 8000:8000 \
            -p 8443:8443 \
            -p 8001:8001 \
            -p 7946:7946 \
            -p 7946:7946/udp \
            --security-opt seccomp:unconfined \
            mashape/kong:0.7.0
	```

### Deploying local RabbitMQ
For running everything locally on your machine you will also need to deploy a RabbitMQ container.

```
docker run -d --hostname my-rabbit --name some-rabbit rabbitmq:management
```

### Configuring the Gateway Registry
The Gateway Registry configuration can be changed by simply putting an `application.yml` file beside the Gateway jar-package.
When starting the Gateway it will outomatically prefer the configuration out side of the package to the one inside.

To run the Gateway simple execute the `java -jar gateway-registry-0.0.1-SNAPSHOT.jar` command.

```YML
## Application properties for gateway registry
---
# ip and port of Kong
kong:
    ip: 127.0.0.1
    port: 8001

# Discovery configuration
# ip			- the ip of the discovery service or of the RabbitMQ Server
# port			- (optional) only needed if used with a discovery; 
#					the port of the	discovery service
# serviceName	- (optional) only needed if used with a discovery service;
#					the name of the RabbitMQ Service on SALSA;
#					if no service name is provided the gateway will 
#					try for a direct connection
discovery:
    ip: 127.0.0.1
#    port: 8580
#    serviceName: ManualTestRabbitService

# SALSA configuration
# ip,port		- ip and port of SALSA
# deploy		- if the gateway service shall attempt to automatically 
#					deploy the RabbitMQ Service
# serverCount	- how many RabbitMQ nodes should be deployed
# serviceName	- the name of the RabbitMQ Service on SALSA
salsa:
    deploy: false
    ip: 128.130.172.215
    port: 8080
    serverCount: 1
    serviceName: ManualTestRabbitService

# port on which the gateway registry will be available
server:
    port: 8280
   
# ip, port and REST path of the decision service to use
decision:
    ip: 127.0.0.1
    port: 8280
    restPath: /users/check

# Log levels
logging:
    level:
        at:
            ac:
                tuwien: TRACE
```

### Running the Kong Test Service
After the Gateway is up an running you can start the Kong Test Service again by emiting the `java -jar kong-test-service-0.0.1-SNAPSHOT.jar` command.
The Test Service will then if everything is properly configured register an API call at the Gateway.
The Test Service can also be configured by an `application.yml` file.

```
## Application properties for kong test service
---
# Discovery configuration
# ip			- the ip of the discovery service or of the RabbitMQ Server
# port			- (optional) only needed if used with a discovery; 
#					the port of the	discovery service
# serviceName	- (optional) only needed if used with a discovery service;
#					the name of the RabbitMQ Service on SALSA;
#					if no service name is provided the gateway will 
#					try for a direct connection
discovery:
    ip: 127.0.0.1
#    port: 8580
#    serviceName: ManualTestRabbitService

# port on which the test service will be available
server:
    port: 8080

# Log levels
logging:
    level:
        at:
            ac:
                tuwien: TRACE
```

Before being able to test this API call you will need to create an user.
To this simple perform the following command:
```
curl -X PUT http://127.0.0.1:8280/users/{username}
```

This will register a user with the given username and return an API key for this user.
This key can then be used to query the registered APIs in the following ways.
In a web browser simply append `?apikey={key}` and from commandline by adding `-H 'apikey: {key}'` to the request headers.