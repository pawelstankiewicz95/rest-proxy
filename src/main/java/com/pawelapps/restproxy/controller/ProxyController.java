package com.pawelapps.restproxy.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class ProxyController {

    private final RestTemplate restTemplate;

    public ProxyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/getJson")
    public ResponseEntity<?> getJson(HttpServletRequest request) {
        String backendUrl = "https://jsonplaceholder.typicode.com/posts/1";
        ResponseEntity<String> response = restTemplate.getForEntity(backendUrl, String.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/getFile")
    public ResponseEntity<?> getFile(HttpServletRequest request, @RequestHeader HttpHeaders headers) {
        String backendUrl = "https://picsum.photos/200";

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response = restTemplate.exchange(
                backendUrl,
                HttpMethod.GET,
                httpEntity,
                byte[].class
        );

        return ResponseEntity.status(response.getStatusCode())
                .body(response.getBody());
    }

    @PostMapping("/**")
    public ResponseEntity<?> postJson(HttpServletRequest request, @RequestBody String body) {
        String backendUrl = "https://fakestoreapi.com/products";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);

        System.out.println("Request Body: " + body);

        ResponseEntity<String> response = restTemplate.exchange(backendUrl, HttpMethod.POST, httpEntity, String.class);

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody());
    }
}
