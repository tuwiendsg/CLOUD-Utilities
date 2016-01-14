local BasePlugin = require "kong.plugins.base_plugin"
local RestDecisionHandler = BasePlugin:extend()
local access = require "kong.plugins.restDecisionPlugin.access"

function RestDecisionHandler:new()
	RestDecisionHandler.super.new(self, "restDecisionPlugin")
end

function RestDecisionHandler:access(conf)
	RestDecisionHandler.super.access(self)
	local res = access.request(conf, "sveti") == "true"
	if not res then
		ngx.exit(ngx.HTTP_FORBIDDEN)
	end
end

return RestDecisionHandler