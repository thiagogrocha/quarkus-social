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

            if (!violations.isEmpty())
                return ResponseError
                        .createFromValidation(violations)
                        .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            User newUser = new User(dto.getName(), dto.getAge());

            repo.persist(newUser);

            return Response.created(UriBuilder.fromResource(UserResource.class).path(newUser.getId().toString()).build()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_ACCEPTABLE.getStatusCode(), e.getMessage()).build();
        }
    }

    @GET
    public Response getAllUsers() {
        try {
            return Response.ok(repo.findAll().list()).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

    @PUT
    @Path("{userId}")
    @Transactional
    public Response updateUser(@PathParam("userId") Long id, UserDtoIn dto) {
        try {
            Set<ConstraintViolation<UserDtoIn>> violations = validator.validate(dto);

            if (!violations.isEmpty())
                return ResponseError
                        .createFromValidation(violations)
                        .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            User user = repo.findById(id);

            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            user.setAge(dto.getAge());
            user.setName(dto.getName());

            return Response.status(Response.Status.OK.getStatusCode(), "Usuário atualizado com sucesso!").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

    @DELETE
    @Path("{userId}")
    @Transactional
    public Response deleteUser(@PathParam("userId") Long id) {
        try {
            User user = repo.findById(id);

            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            repo.delete(user);

            return Response.status(Response.Status.NO_CONTENT.getStatusCode(), "Usuário deletado com sucesso!").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

}
