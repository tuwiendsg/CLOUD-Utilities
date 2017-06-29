local BasePlugin = require "kong.plugins.base_plugin"
local RestDecisionHandler = BasePlugin:extend()
local access = require "kong.plugins.restDecisionPlugin.access"

function RestDecisionHandler:new()
	RestDecisionHandler.super.new(self, "restDecisionPlugin")
end

function RestDecisionHandler:access(conf)
	--return responses.send_HTTP_FORBIDDEN("Cannot identify the consumer, please do authenticate your request!")
	local consumer_id
	if ngx.ctx.authenticated_credential then
		consumer_id = ngx.ctx.authenticated_credential.consumer_id
	else
		return responses.send_HTTP_FORBIDDEN("Cannot identify the consumer, please do authenticate your request!")
	end
	
	RestDecisionHandler.super.access(self)
	local res = access.request(conf, consumer_id) == "true"
	if not res then
		--ngx.say("blub")
		ngx.exit(ngx.HTTP_FORBIDDEN)
	end
end

return RestDecisionHandler