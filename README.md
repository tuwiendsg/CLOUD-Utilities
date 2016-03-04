# CLOUD-Utilities

This is a small collection of tools which aims to help the iCOMOT 
platform appearing more as one centrally accessible service to the user.

For more detailed descriptions please read the READMEs of the different tools.

## Messaging API
The messaging API defines the interfaces which have to be provided by different message
broker implementation like RabbitMQ or ActiveMQ so that they can be used for 
communication.

## Messaging Lightweight
Messaging lightweight is a library package which can be included to use one of the here
provided messaging providers as communication platform.

At the moment there is only an implementation for RabbitMQ given.

## Gateway Adapter
Is a library package which can be used for easily publishing new APIs to the common
gateway.

The adapter makes internally use of the Messaging API and Lightweight packages for 
establishing the communication with the gateway.

## Gateway Registry
This service is the endpoint which communicates and handles the requests of the
services which are using the Gateway Adapter package regarding 
publishing/deleting APIs on the common gateway. This service will pass all 
incoming requests on to the actual gateway.

Further this service also provides a simple, experimental endpoint for the
RestDecisionPlugin.

## Kong Plug-ins
At the moment the used gateway is Kong. The following custom plug-ins where developed.

### RestDecisionPlugin
This plug-in allows the gateway to query another service for approval if a certain user 
is allowed to use a certain API or not.

## Kong Test Service
This is a small test service which will register an usable API at the gateway for
demonstration purpose.