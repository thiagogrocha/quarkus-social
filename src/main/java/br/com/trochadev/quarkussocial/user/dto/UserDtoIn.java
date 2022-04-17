package br.com.trochadev.quarkussocial.user.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDtoIn {

    @NotBlank(message = "Campo nome é obrigatório!")
    private String name;
    @NotNull(message = "Campo idade é obrigatório!")
    private Integer age;

}
