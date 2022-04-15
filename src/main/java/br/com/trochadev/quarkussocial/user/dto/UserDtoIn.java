package br.com.trochadev.quarkussocial.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserDtoIn {

    @NotBlank(message = "Campo nome é obrigatório!")
    private String name;
    @NotNull(message = "Campo idade é obrigatório!")
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
