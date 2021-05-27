package com.example.github_api_proxy_server.controller;


import com.example.github_api_proxy_server.models.Contributor;
import com.example.github_api_proxy_server.service.ContributorService;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "org/{org_name}/contributors")
public class ContributorController {

    private final ContributorService contributorService;
    private final String errorMsg = "{error :)}";

    @Autowired
    public ContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping
    public String getContributors(@PathVariable("org_name") String orgName) throws ExecutionException, InterruptedException {
        List<Contributor> contributorList = contributorService.getContributors(orgName);
        if(contributorList==null)
            return errorMsg;
        return new JSONArray(contributorList).toString();
    }
}
