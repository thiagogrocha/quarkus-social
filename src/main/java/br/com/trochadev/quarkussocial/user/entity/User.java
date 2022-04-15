package br.com.trochadev.quarkussocial.user.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
@SequenceGenerator(name = "UsersSeq", sequenceName = "users_seq", allocationSize = 1)
public class User {

    @Id
    @GeneratedValue(generator = "UsersSeq", strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
