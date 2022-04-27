package br.com.trochadev.quarkussocial.post;

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

    public static PostDtoOut fromEntity(PostEntity postEntity) {
        return new PostDtoOut(postEntity.getId(), postEntity.getText(), postEntity.getDateTime());
    }
}
