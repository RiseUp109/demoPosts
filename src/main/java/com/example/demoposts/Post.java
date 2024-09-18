package com.example.demoposts;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
    int userId;
    int id;
    String title;
    String body;

    public Post() {}

    public Post(int userId, int id, String title, String body) {
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

}
