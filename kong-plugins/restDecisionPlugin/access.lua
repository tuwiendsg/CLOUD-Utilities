local http = require "socket.http"
local ltn12 = require "ltn12"

local M = {}

function M.request(config, reqBody)
	respBody = {}
	result, respcode, respheaders, respstatus = http.request {
		method = config.method,
--		url = "http://128.130.172.214:9090/check";,
--		url = "http://127.0.0.1:8280/check",
		url = config.url,
		source = ltn12.source.string(reqBody),
		headers = {
			["content-type"] = config.contentType,
			["content-length"] = tostring(#reqBody)
		},
		sink = ltn12.sink.table(respBody)
	}

	if respcode ~= 200 then
		return false;
	end

	-- get body as string by concatenating table filled by sink
	respBody = table.concat(respBody)

	return respBody
end

return M