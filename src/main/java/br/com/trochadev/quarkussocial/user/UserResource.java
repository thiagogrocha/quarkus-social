package br.com.trochadev.quarkussocial.user;

import br.com.trochadev.quarkussocial.user.entity.UserRepository;
import br.com.trochadev.quarkussocial.user.dto.UserDtoIn;
import br.com.trochadev.quarkussocial.user.entity.User;
import br.com.trochadev.quarkussocial.validation.ResponseError;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Set;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserRepository repo;
    private Validator validator;

    @Inject
    public UserResource(UserRepository repository, Validator validator) {
        this.repo = repository;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response createUser(UserDtoIn dto) {
        try {
            Set<ConstraintViolation<UserDtoIn>> violations = validator.validate(dto);

            if (!violations.isEmpty()) {
                return ResponseError
                        .createFromValidation(violations)
                        .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
            }

            User newUser = new User();
            newUser.setName(dto.getName());
            newUser.setAge(dto.getAge());

            repo.persist(newUser);

            return Response.created(
                            UriBuilder.fromResource(UserResource.class)
                                    .path(newUser.getId().toString())
                                    .build())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getMessage()).build();
        }
    }

    @GET
    public Response getAllUsers() {
        return Response.ok(repo.findAll().list()).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam("id") Long id, UserDtoIn dto) {
        User user = repo.findById(id);

        if (user != null) {
            user.setAge(dto.getAge());
            user.setName(dto.getName());

        } else
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

        return Response.status(Response.Status.OK.getStatusCode(), "Usuário atualizado com sucesso!").build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam("id") Long id) {
        User user = repo.findById(id);
        try {
            if (user != null)
                repo.delete(user);
            else
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e.toString()).build();
        }
        return Response.status(Response.Status.NO_CONTENT.getStatusCode(), "Usuário deletado com sucesso!").build();
    }


}
