# Gateway Adapter

This is a small Java library intended to help registering new APIs with our gateway.

- The AdapterService
	
	You can get a default instance of the `AdapterService` from the `GatewayAdapterFactory`.
	``` Java
	AdapterService adapterService = GatewayAdapterFactory
				.adapterServiceImpl(discoverySettings);		//settings for the default discovery service
	```
- Creating a new API

	``` Java
	adapterService.createApiAdapter()
					.withName("ktsRoot") 								//name of the API
					.withRestPath("kts") 								//path on the API gateway
					.withTargetUrl("http://{TARGET_IP}:{TARGET_PORT}")	//upstream path of local service
					.send();											//send the API to the gateway
	```