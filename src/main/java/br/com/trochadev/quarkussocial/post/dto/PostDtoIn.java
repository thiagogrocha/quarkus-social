package br.com.trochadev.quarkussocial.post.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PostDtoIn {

    @NotBlank(message = "Campo texto é obrigatório!")
    private String text;

}
