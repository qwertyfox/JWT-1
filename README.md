# JWT-1
Using Spring Security with JWT and SQLite3 Database.<br />
The project uses Json codes containig username and password instead of GUI. 
MyUsernamePasswordAuthFilter checks the username and password with the Db record and also sends a Json Web Token (JWT) to client.
The token has a secret key and the expiry of token is set to 2 weeks. Each time client sents request to access secured content url "/test", the token is authenticated each time.

## General:
1. Application maps the username and password using ObjectMapper to authenticate it with the Db record.
2. Sends the Json Web Token (JWT) to the client with signature and expiry date.
3. Client can use the JWT to access the secured content i.e. URL: Localhost:8080/test
4. Each time the client sends request, the JWT is checked by filters before authentication is granted. <br />

## Code: 
1. For Postman to work, csrf is disabled at AppSecurityConfig class which extends WebSecurityConfigurerAdapter.
2. JWT works with stateless sessions and thus the SessionCreationPolicy.STATELESS is used.
3. JWT is send as response to client under header "Authorization" with "Barer " as the prefix.
4. The Key is set as "This_Need_To_Be_Very_Secured_Key".
5. The class MyTokenVerifier extends OncePerRequestFilter which is called everytime a client sends the request.
6. MyTokenVerifier checks if the client has the JWT and if yes, it extracts the content under pre determined conditions; Header "Authorization" prefix "Barer".
7. The Token is authenticated and the client is sent to next filter in chain, in this case, resource of /test URL

## Testing conditions in Postman <br />
1. URL: localhost:8080/login  Method: POST
2. Request Body type: raw --->JSON <br />
  Content 
{"username" : "Peter","password" : "peter123"}
// or any username and password defined in the database 
3. From the response Headers, "Authorization" header's content is copied. It must have prefix "Barer "
4. URL: localhost:8080/test Method: GET
5. A new header with name "Authorization" in Request Headers is created and content is pasted, with "Barer " prefix

** Notes:
The JWT content authenticity and signature can be verified at www.jwt.io


