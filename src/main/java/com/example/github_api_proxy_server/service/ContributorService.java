package com.example.github_api_proxy_server.service;

import com.example.github_api_proxy_server.models.Contributor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ContributorService {
    final String token = System.getenv("GH_TOKEN");
    private final RestTemplate restTemplate;
    private final AsyncService asyncService;

    @Autowired
    public ContributorService(RestTemplate restTemplate, AsyncService asyncService) {
        this.restTemplate = restTemplate;
        this.asyncService = asyncService;
    }

    public List<Contributor> getContributors(String orgName) throws ExecutionException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("authorization", "token " + token);
        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange("https://api.github.com/orgs/" + orgName + "/repos",
                    HttpMethod.GET, request, String.class, 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        JSONArray reposArray = new JSONArray(response.getBody());
        List<CompletableFuture<JSONArray>> contributorsArrays = new ArrayList<>();
        for (int i = 0; i < reposArray.length(); i++) {
            var array = asyncService.getJsonArrayByUrl(
                    (String) reposArray.getJSONObject(i).get("contributors_url"), restTemplate, request);
            if (array == null)
                continue;
            contributorsArrays.add(array);
        }
        CompletableFuture.allOf(contributorsArrays.toArray(new CompletableFuture[0])).join();
        NavigableSet<Contributor> contributorSet = new TreeSet<>();
        for (var arr : contributorsArrays) {
            JSONArray contributors = arr.get();
            if (contributors == null)
                continue;
            for (int j = 0; j < contributors.length(); j++) {
                JSONObject contributor = contributors.getJSONObject(j);
                contributorSet.add(new Contributor((String) contributor.get("login"),
                        (int) contributor.get("contributions")));
            }
        }

        Iterator<Contributor> iterator = contributorSet.descendingIterator();
        List<Contributor> contributorList = new ArrayList<>();
        int i = 0;
        while (i < 10 && iterator.hasNext()) {
            contributorList.add(iterator.next());
            i++;
        }
        return contributorList;
    }
}