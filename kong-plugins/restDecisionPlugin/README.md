# Kong RestDecisionPlugin
- You need the enable the authentication plugin
```
curl -X POST http://KONGIP:8001/apis/ktsRoot2/plugins --data "name=key-auth"
```

- You need the enable the restDecisionPlugin
```
curl -X POST -H "Content-Type: application/json" -d '{                        
    "name": "restDecisionPlugin",
    "config": {
        "url": "http://127.0.0.1:8280/users/check"
    }
}'  http://KONGIP:8001/apis/ktsRoot2/plugins
```

- Create a consumer

- Register the consumer with our gateway
