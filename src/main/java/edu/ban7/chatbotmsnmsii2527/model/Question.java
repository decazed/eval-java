package edu.ban7.chatbotmsnmsii2527.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank
    protected String content;

    @ManyToOne
    protected AppUser appUser;

    @ManyToMany
    @JoinTable(
            name = "question_include_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    protected List<Tag> includeTags = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "question_exclude_tag",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    protected List<Tag> excludeTags = new ArrayList<>();
}
