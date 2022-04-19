package br.com.trochadev.quarkussocial.post.dto;

import br.com.trochadev.quarkussocial.post.entity.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDtoOut {
    private Long id;
    private String text;
    private LocalDateTime dateTime;

    public PostDtoOut() {
    }

    public PostDtoOut(Long id, String text, LocalDateTime dateTime) {
        this.id = id;
        this.text = text;
        this.dateTime = dateTime;
    }

    public static PostDtoOut fromEntity(Post post) {
        return new PostDtoOut(post.getId(), post.getText(), post.getDateTime());
    }
}
