return {
	no_consumer = true,
	fields = {
		method = {type = "string", default = "POST"},
		url = {type = "string", required = true},
		contentType = {type = "string", default = "text/plain"}
	}
}