package br.com.trochadev.quarkussocial.post;

import br.com.trochadev.quarkussocial.validation.ResponseMsg;
import br.com.trochadev.quarkussocial.user.UserResource;
import br.com.trochadev.quarkussocial.user.entity.User;
import br.com.trochadev.quarkussocial.user.entity.UserRepository;
import br.com.trochadev.quarkussocial.validation.ResponseError;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@ApplicationScoped
public class PostService {

    private UserRepository userRepo;
    private PostRepository postRepo;
    private Validator validator;

    @Inject
    public PostService(PostRepository postRepo, UserRepository userRepo, Validator validator) {
        this.postRepo = postRepo;
        this.userRepo = userRepo;
        this.validator = validator;
    }

    public Response save(Long userId, PostDtoIn dto) {
        Set<ConstraintViolation<PostDtoIn>> violations = validator.validate(dto);

        if (!violations.isEmpty())
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        User user = userRepo.findById(userId);

        if (user == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Usuário não encontrado!")).build();

        PostEntity newPost = new PostEntity(user, dto.getText());
        postRepo.persist(newPost);

        return Response.created(UriBuilder.fromResource(UserResource.class).path(userId.toString()).path("posts").path(newPost.getId().toString()).build()).build();
    }

    public Response posts(Long userId) {
        User user = userRepo.findById(userId);

        if (user == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Usuário não encontrado!")).build();

        return Response.ok(postRepo.find("user", Sort.by("dateTime", Sort.Direction.Descending), user)
                .stream()
                .map(PostDtoOut::fromEntity)
                .collect(Collectors.toList())).build();
    }

    public Response update(Long userId, Long postId, PostDtoIn dto) {
        Set<ConstraintViolation<PostDtoIn>> violations = validator.validate(dto);

        if (!violations.isEmpty())
            return ResponseError
                    .createFromValidation(violations)
                    .withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);

        User user = userRepo.findById(userId);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Usuário não encontrado!")).build();

        PostEntity postEntity = postRepo.find("user = :user and id = :id", Parameters.with("user", user).and("id", postId)).firstResult();
        if (postEntity == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Post não encontrado!")).build();

        postEntity.setText(dto.getText());

        return Response.status(Response.Status.OK.getStatusCode()).entity(new ResponseMsg("Post atualizado!")).build();
    }

    public Response delete(Long userId, Long postId) {
        User user = userRepo.findById(userId);
        if (user == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Usuário não encontrado!")).build();

        PostEntity postEntity = postRepo.find("user = :user and id = :id", Parameters.with("user", user).and("id", postId)).firstResult();
        if (postEntity == null)
            return Response.status(Response.Status.NOT_FOUND.getStatusCode()).entity(new ResponseMsg("Post não encontrado!")).build();

        postRepo.delete(postEntity);

        return Response.status(Response.Status.NO_CONTENT.getStatusCode()).entity(new ResponseMsg("Post deletado!")).build();
    }
}
