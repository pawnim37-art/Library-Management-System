package com.library.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 2000)
    private String biography;

    public Author() {}

    public Author(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public static AuthorBuilder builder() { return new AuthorBuilder(); }

    public static class AuthorBuilder {
        private Long id;
        private String name;
        private String biography;

        public AuthorBuilder id(Long id) { this.id = id; return this; }
        public AuthorBuilder name(String name) { this.name = name; return this; }
        public AuthorBuilder biography(String biography) { this.biography = biography; return this; }

        public Author build() { return new Author(id, name, biography); }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBiography() { return biography; }
    public void setBiography(String biography) { this.biography = biography; }
}
