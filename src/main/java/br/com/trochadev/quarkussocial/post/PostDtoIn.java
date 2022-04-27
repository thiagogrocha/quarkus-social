package br.com.trochadev.quarkussocial.post;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.validation.constraints.NotBlank;

@Schema(name = "PostEntity in", description = "PostEntity in")
@Data
public class PostDtoIn {

    @NotBlank(message = "Campo texto é obrigatório!")
    @Schema(title = "PostEntity")
    @Parameter(description = "PostEntity parameter")
    private String text;

}
