package br.com.trochadev.quarkussocial.post;

import br.com.trochadev.quarkussocial.post.dto.PostDtoIn;
import br.com.trochadev.quarkussocial.post.dto.PostDtoOut;
import br.com.trochadev.quarkussocial.post.entity.Post;
import br.com.trochadev.quarkussocial.post.entity.PostRepository;
import br.com.trochadev.quarkussocial.user.entity.User;
import br.com.trochadev.quarkussocial.user.entity.UserRepository;
import br.com.trochadev.quarkussocial.validation.ResponseError;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    private UserRepository userRepo;
    private PostRepository repo;
    private Validator validator;

    @Inject
    public PostResource(PostRepository repo, UserRepository userRepo, Validator validator) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    @POST
    @Transactional
    public Response savePost(@PathParam("userId") Long userId, PostDtoIn dto) {
        try {
            Set<ConstraintViolation<PostDtoIn>> violations = validator.validate(dto);

            if (!violations.isEmpty())
                return ResponseError
                        .createFromValidation(violations)
                        .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            User user = userRepo.findById(userId);

            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            repo.persist(new Post(user, dto.getText()));

            return Response.status(Response.Status.CREATED).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

    @GET
    public Response listPosts(@PathParam("userId") Long userId) {
        try {
            User user = userRepo.findById(userId);

            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            return Response.ok(repo.find("user", Sort.by("dateTime", Sort.Direction.Descending), user)
                    .stream()
                    .map(PostDtoOut::fromEntity)
                    .collect(Collectors.toList())).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

    @PUT
    @Path("{postId}")
    @Transactional
    public Response updatePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId, PostDtoIn dto) {
        try {
            Set<ConstraintViolation<PostDtoIn>> violations = validator.validate(dto);

            if (!violations.isEmpty())
                return ResponseError
                        .createFromValidation(violations)
                        .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

            User user = userRepo.findById(userId);

            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            Post post = repo.find("user = :user and id = :id", Parameters.with("user", user).and("id", postId)).firstResult();
            post.setText(dto.getText());

            return Response.status(Response.Status.OK.getStatusCode(), "Post atualizado com sucesso!").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }

    @DELETE
    @Path("{postId}")
    @Transactional
    public Response deletePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId) {
        try {
            User user = userRepo.findById(userId);
            if (user == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Usuário não encontrado!").build();

            Post post = repo.find("user = :user and id = :id", Parameters.with("user", user).and("id", postId)).firstResult();
            if (post == null)
                return Response.status(Response.Status.NOT_FOUND.getStatusCode(), "Post não encontrado!").build();

            repo.delete(post);

            return Response.status(Response.Status.NO_CONTENT.getStatusCode(), "Post deletado com sucesso!").build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), "Erro interno!\n" + e).build();
        }
    }
}
