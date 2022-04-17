package br.com.trochadev.quarkussocial.user.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@SequenceGenerator(name = "UsersSeq", sequenceName = "users_seq", allocationSize = 1)
@Data
public class User {

    @Id
    @GeneratedValue(generator = "UsersSeq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer age;

}
