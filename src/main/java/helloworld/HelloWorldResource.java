package helloworld;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class HelloWorldResource {

    @GET
    public String sayHello() {
        return "Hello World! (" + System.currentTimeMillis() + ")";
    }

}
