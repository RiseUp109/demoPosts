package com.example.demotests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import com.example.demoposts.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class DemoTest {

    private static final Logger log = LoggerFactory.getLogger(DemoTest.class);
    String url = "https://jsonplaceholder.typicode.com/posts";

    public void report(String message) {
        log.info(message);
    }

    @Test
    @Description("Testing get request")
    @Tag("Task1")
    public void getTest() {
        report("Testing get request");
        Response response = RestAssured.given()
                .get(url);
        report("Performing assertions");
        assertEquals(200, response.getStatusCode());
        List<Post> posts = new Gson().fromJson(response.asString(), new TypeToken<List<Post>>() {}.getType());
        assertEquals(100, posts.size());
        assertEquals(1, posts.get(0).getId());
        report("Assertions passed\n");
    }

    @Test
    @Description("Testing post request")
    @Tag("Task1")
    public void postTest() {
        report("Testing post request");
        Post post = new Post(1, 101, "test title", "test body");
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(post)
                .post(url);
        report("Performing assertions");
        assertEquals(201, response.getStatusCode());
        Post postResponse = response.as(Post.class);
        assertEquals(101, postResponse.getId());
        assertEquals("test title", postResponse.getTitle());
        assertEquals("test body", postResponse.getBody());
        assertEquals(1, postResponse.getUserId());
        report("Assertions passed\n");
    }

    @Test
    @Description("Testing put request")
    @Tag("Task1")
    public void putTest() {
        report("Testing put request");
        Post post = new Post(1, 1, "test title", "test body");
        Response response = RestAssured.given()
                .contentType("application/json")
                .body(post)
                .put(url + "/1");
        report("Performing assertions");
        assertEquals(200, response.getStatusCode());
        Post postResponse = response.as(Post.class);
        assertEquals(1, postResponse.getId());
        assertEquals("test title", postResponse.getTitle());
        assertEquals("test body", postResponse.getBody());
        assertEquals(1, postResponse.getUserId());
        assertEquals(1, postResponse.getId());
        report("Assertions passed\n");
    }

    @Test
    @Description("Testing patch request")
    @Tag("Task1")
    public void patchTest() {
        report("Testing patch request");
        String title = "test title";
        Response response = RestAssured.given()
                .contentType("application/json")
                .body("{\"title\":\"" + title + "\"}")
                .patch(url + "/1");
        report("Performing assertions");
        assertEquals(200, response.getStatusCode());
        Post postResponse = response.as(Post.class);
        assertEquals(1, postResponse.getId());
        assertEquals(title, postResponse.getTitle());
        report("Assertions passed\n");
    }

    @Test
    @Description("Testing delete request")
    @Tag("Task1")
    public void deleteTest() {
        report("Testing delete request");
        Response response = RestAssured.given()
                .delete(url + "/1");
        report("Performing assertions");
        assertEquals(200, response.getStatusCode());
        report("Assertions passed\n");
    }

    @Test
    @Description("Count top 10 words")
    @Tag("Task2")
    public void countTop10WordsTest() {
        report("Count top 10 words");
        Response response = RestAssured.given()
                .get(url);
        assertEquals(200, response.getStatusCode());
        List<Post> posts = new Gson().fromJson(response.asString(), new TypeToken<List<Post>>() {}.getType());
        countTop10Words(posts);
        report("Count top 10 words passed\n");
    }

    @Step("Count top 10 words and create file top10words.txt")
    private static void countTop10Words(List<Post> posts) {
        // Map to store word counts
        Map<String, Integer> wordCountMap = new HashMap<>();

        // Loop through each post's body and count words
        posts.forEach(post -> {
            String[] words = post.getBody().toLowerCase().split("\\W+");
            for (String word : words) {
                if (!word.isEmpty()) {
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                }
            }
        });

        // Get the top 10 words by frequency
        List<Map.Entry<String, Integer>> top10Words = wordCountMap.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(10)
                .collect(Collectors.toList());

        // Write the top 10 words to the file in the specified format
        String filename = "top10words.txt";
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry<String, Integer> entry : top10Words) {
                writer.write(entry.getKey() + " - " + entry.getValue() + "\n");
            }
            System.out.println("Top 10 words written to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

