local access = require("access")

config = { method="POST", 
			url="http://127.0.0.1:8280/users/check",
			contentType="text/plain" }

print(access.request(config, "sveti"))
--[[
if access.request(config, "sveti") == "true" then 
	io.write("Result was true!\n")
else
	io.write("Result was false!\n")
end
--]]