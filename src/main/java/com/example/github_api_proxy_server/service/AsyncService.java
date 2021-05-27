package com.example.github_api_proxy_server.service;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async
    public CompletableFuture<JSONArray> getJsonArrayByUrl(String url, RestTemplate restTemplate, HttpEntity request) {
        ResponseEntity<String> response = restTemplate.exchange(url,HttpMethod.GET, request,
                String.class, 1);
        String responseBody = response.getBody();
        if(responseBody==null)
            return null;
        return CompletableFuture.completedFuture(new JSONArray(responseBody));

    }
}
