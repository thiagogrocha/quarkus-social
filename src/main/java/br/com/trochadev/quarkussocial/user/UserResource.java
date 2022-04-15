package br.com.trochadev.quarkussocial.user;

import br.com.trochadev.quarkussocial.user.dto.UserDtoIn;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response createUser(UserDtoIn dto) {
        return Response.ok(dto).build();
    }

    @GET
    public Response getAllUsers() {
        return Response.ok().build();
    }

}
