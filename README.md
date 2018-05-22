# adidas Coding Challenge

#### Intro
This challenge defines a simple cloud scenario using Spring Cloud and Docker. The elements of this scenario are the following:
- mongodb: A simple mongodb instance, used by _flight-data_ microservice to obtain the flights data.
- config-server: Server used by the rest of the apps to obtain their properties.
- eureka-server: Discovery and Registry server. Each instance of each microservice will register itself on this server. The gateway server will use this registry to know if a microservice is alive.
- security-server: Authentication service. Creates and checks a JWT token. Zuul will use this server to validate the user credentials every time it receives a request.
- zuul-server: Gateway server. Main entry point for the infrastructure. It checks the user credentials, performs security access and redirects each request to the corresponding microservice. Also, exposes a Hystrix dashboard and a Swagger UI.
- flight-data: Microservice that exposes an endpoint to obtain the flight data present on mongo.
- flight-info: Microservice that exposes two endpoints to get the shortest and quickest route/s between two airports. All the business logic is here.

![alt text](https://image.ibb.co/fD7i1T/cloud.png "Flight Graph")


#### Technology
Newest technologies and features have been used when developing this challenge. Thus, Spring Boot version 2 is used, same as Spring Cloud. Also, _flight-data_ and _flight-info_ expose its endpoints using non-blocking WebFlux.

The base stack is the following:
- Java8
- Spring Boot 2.0.2
- Spring Cloud 2.0.0
- io.jsonwebtoken
- Project Lombok
- MongoDB

###### Spring Boot 2.0.2
The reason to choose Spring Boot 2.0.2 is because Spring Boot provides a fast development experience using Spring. It configures many things out-of-the-box, saving a lot of time when starting a new project. Also, this version includes the last release of Spring, version 5. Among the new features, one of the most interesting is Webflux, which allows to develop REST services in a non-blocking way, providing high scalability.

###### Spring Cloud 2.0.0
Spring Cloud has been chose because it´s the simple option to go if Spring Boot is used. The integration is seamless. It provides a config server, a discovery server, a gateway server and fallback mechanisms.

###### io.jsonwebtoken
One of the most popular libraries when handling JWT tokens. Hides the complexity of the tokens, and provides a fluent API.

###### Project Lombok
By using annotations, it generates setters, getters, constructors, performs null checks... While Spring reduces the boilerplate of JEE, Project Lombok reduces the boilerplate of Java POJOs.

###### MongoDB
The election of MongoDB was because it´s a fast database with a distributed nature. MongoDB automatically maintains replica sets, multiple copies of data that are distributed across servers, racks and data centers for high availability.


#### Solution
The given solution proposes the creation of a graph of flights. This graph is created by establishing a series of relationships among the airports instances. Each airport will contain a list of destination airports, and each airport in that list, will contain its own list of destinations airports. This graph is created by the method _ItineraryFacadeImpl.buildFlightGraph_
![alt text](https://image.ibb.co/dB0dY8/flight_graph.png "Flight Graph")

To find the routes, what the code does is look for all possible paths between the departure and destination airports. Method _getRoutes_ on _RouteFinderTemplate_ is the one that finds these paths. It begins by finding the departure airport in the graph, and iterating among its destinations objects. For every destination, the method _iterateOverChild_ is called. And so it goes.
If a destination object has destinations as well, _iterateOverChild_ method will be called recursively until there are no more destinations.


###### Design Patterns
To code the logic a few design patterns have been used. These are the following: _Strategy_, _Template_ and _Facade_. All of these patterns are present in flight-info microservice, which is the one that performs all the business logic of the challenge.
- Template Pattern
_RouterFinderTemplate.java_ defines base methods to obtain all the possible routes between  two airports. Subclasses of _RouteFinderTemplate_ must implement the method _getResult_. This method takes a list of _RouteDto_ as argument, and its purpose is to return those Route object that match the desired behaviour. For example, _QuickestRoute.java_ returns the faster routes between origin and destination airport. This pattern allow us to create new behaviours (subclasses) without modifying the main _RouteFinderTemplate class_. If we would like to know which routes have the higher number of stopovers, it would be sufficient to create a new class _LongestRoute_ that inherits from _RouteFinderTemplate_.

- Strategy Pattern
_ItineraryFacadeImpl_ defines a method _getItinerary_ that accepts a instance of _RouteFinderTemplate_. This is the strategy that the facade will be used to obtain the desired routes. It´s not a 100%-by-definition strategy pattern because _ItineraryFacadeImpl_ doesn´t hold a _has-a_ relationship with _RouteFinderTemplate_. But since we can dynamically choose the behaviour, it qualifies as a _Strategy_ pattern. The benefits of this pattern is that we can create new strategies (LongestRoute!) and the _ItineraryFacadeImpl_ class will remain the same.

- Facade Pattern
_ItineraryFacadeImpl_ class provides a simple interface for its clients. In this case _FlightInfoServiceImpl_ just have to call _ItineraryFacadeImpl.getItinerary_ method. The client doesn´t know, and doesn´t care, about all the logic of creating the flights graph, calling the strategy method... This pattern makes the code more readable and easier to test and provides loose coupling between the client and the business logic.

#### How it works
The cloud scenario works this way:
- Config server
Provides config properties for every application. To avoid using a Git server, a local file configuration has been selected.

- Eureka server
Provides a discovery server, so Zuul can be able to redirect the incoming requests.

- Security server
Creates and checks a JWT token. This token will be created and obtained from an HTTP Header, by default named _Authorization_ (although his value can be changed on config-server security props) Bear in mind that security server just performs authentication, doesn´t perform authorization. Zuul is the one that does that!

- Zuul server
Being the main entry point, it has a lots of responsabilities. Acts as gateway. Is the only one that is exposed to the exterior. The rest of the elements are exposed only to the docker network (mongodb is also exposed to the exterior just to facilite the management of the flight data, but it shouldn´t be exposed in a normal scenario). For every request, Zuul will check if the request has a JWT token. If it does, Zuul will call  _security-server_ to validate the authenticity of this token. If _security-server_ returns a 200, the token is valid an Zuul will decode it to create an SecurityContext, so Spring Security could perform the authorization.
Because it´s the only one exposed to the exterior, is the logical place to provide a Hystrix dashboard and a Swagger UI. From here, it´s possible to access the different microservices hystrix streams and Swagger apidocs.
Zuul only has route capabilities to _security-server_ (so users can login) and _flight-info_. _flight-data_ is not mapped so it cannot be accesible from the exterior. This election was motivated to keep the data as isolated as possible. If any of this services fail, Hystrix (_DefaultHystrixFallback_) will come into play and a default response will be returned.
Furthermore, thanks to Eureka, Zuul can use Ribbon to perform load balanced requests. If there are more than one instance of a service, Ribbon will decide which one will be called.

- flight-data
Exposes an endpoint that returns the flight data stored on MongoDB. Only exposes a GET operation, but it could also be used to create a whole CRUD. Both its endpoints and the mongodb connection are non-blocking.

- flight-info
Exposes two endpoints to obtain the shortests and quickest routes. It calls _flight-data_ service to get the flights, and perform the logic described in Solution. It uses Hystrix as a fallback mechanism when calling _flight-data_, so its stream could be seen on Zuul´s Hystrix console.
Also, any exception thrown inside will be handled by a _@RestControllerAdvice_ so the user will never get an ugly java exception.

#### How to start
There are a few tools that must be installed in the computer prior to run and execute the challenge. These tools are Docker, Maven and Java.

In a UNIX environment, just execute the script _startAll.sh_.
When the script finishes, simply start the docker containers.

In a Windows environment, execute _mvn clean package_ and then create the docker containers by executing these commands:
```sh
$ docker build -t adidas/config-server:1.0.0 ./config-server
$ docker build -t adidas/eureka-server:1.0.0 ./eureka-server
$ docker build -t adidas/security-server:1.0.0 ./security-server
$ docker build -t adidas/zuul-server:1.0.0 ./zuul-server
$ docker build -t adidas/flight-data:1.0.0 ./flight-data
$ docker build -t adidas/flight-info:1.0.0 ./flight-info
```

Finally, execute the following command:
```sh
$ docker-compose up --no-start
```

At least, _config-server_, _eureka-server_ and _zuul-server_ containers must be started at all times. The rest of the containers, are optional. For example, keeping _flight-data_ stopped and starting _flight-info_ will let see the Hystrix _flight-info_ working at its best.

In order to perform requests, the user must be authenticated. Start _security-server_, if not started yet, and use Postman, or your favourite tool, to perform a request to _http://localhost:8000/auth/login_. The body must be a JSON like this _{ "user": "admin", "password": "password"}_ The response of this request will contain an _Authorization_ header. From now on, use this header in every request you dispatch.

We mentioned before the posibility of having a look at _flight-info_ Hystryx stream. Just start _flight-info_ container, and launch a request to _http://localhost:8000/flightsInfo/quickest_. You will need to send a JSON body. Luckily for us, the definition of this JSON can be seen on the SwaggerUI (_http://localhost:8000/swagger-ui.html_, you may need to refresh in you get an error).
Before dispatching, go to _http://localhost:8000/hystrix_, insert _http://localhost:8000/flightsInfo/actuator/hystrix.stream_ in the text input and click on 'Monitor Stream'. Now, send a few requests! Keep in mind that this url is obtained via Zuul, so it will only work as expected when only one instance of _flight_info_ is alive. 

Please, notice that you may need to try several times until Ribbon Load Balancing refreshes its configuration, every 30 seconds by default. You can have a look at Zuul Hystrix stream to know whether the flight-info instance is working or not _http://localhost:8000/actuator/hystrix.stream_.
