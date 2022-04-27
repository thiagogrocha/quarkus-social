package br.com.trochadev.quarkussocial.post;

import br.com.trochadev.quarkussocial.validation.ResponseError;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {

    PostService service;

    @Inject
    public PostResource(PostService service) {
        this.service = service;
    }

    @POST
    @Operation(summary = "Cria Post", description = "Cria um Post")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Criado",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PostDtoOut.class))),
            @APIResponse(responseCode = "422", description = "Campos obrigatórios não informados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ResponseError.class))),
            @APIResponse(responseCode = "403", description = "Usuário não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno")})
    public Response savePost(@PathParam("userId") Long userId, PostDtoIn dto) {
        try {
            return service.save(userId, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(e).build();
        }
    }

    @GET
    @Operation(summary = "Lista Posts", description = "Lista Posts de um Usuário")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PostDtoOut.class))),
            @APIResponse(responseCode = "403", description = "Usuário não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno")})
    public Response listPosts(@PathParam("userId") Long userId) {
        try {
            return service.posts(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(e).build();
        }
    }

    @PUT
    @Path("{postId}")
    @Operation(summary = "Atualiza Post", description = "Atualiza um Post")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PostDtoOut.class))),
            @APIResponse(responseCode = "422", description = "Campos obrigatórios não informados",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ResponseError.class))),
            @APIResponse(responseCode = "403", description = "Usuário ou Post não encontrado"),
            @APIResponse(responseCode = "500", description = "Erro interno")})
    public Response updatePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId, PostDtoIn dto) {
        try {
            return service.update(userId, postId, dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(e).build();
        }
    }

    @DELETE
    @Path("{postId}")
    public Response deletePost(@PathParam("userId") Long userId, @PathParam("postId") Long postId) {
        try {
            return service.delete(userId, postId);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()).entity(e).build();
        }
    }
}
