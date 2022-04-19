package br.com.trochadev.quarkussocial.post.entity;

import br.com.trochadev.quarkussocial.user.entity.User;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
@SequenceGenerator(name = "PostsSeq", sequenceName = "posts_seq", allocationSize = 1)
public class Post {

    @Id
    @GeneratedValue(generator = "PostsSeq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "dateTime")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    public Post() {
    }

    public Post(User user, String text) {
        this.user = user;
        this.text = text;
    }

    @PrePersist
    public void prePersist() {
        setDateTime(LocalDateTime.now());
    }

}
