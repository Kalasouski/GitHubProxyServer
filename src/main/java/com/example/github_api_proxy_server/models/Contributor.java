package com.example.github_api_proxy_server.models;


import lombok.Getter;
import lombok.Setter;

public class Contributor implements Comparable<Contributor> {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int contributions;

    public Contributor(String name, int contributions) {
        this.name = name;
        this.contributions = contributions;
    }

    @Override
    public int compareTo(Contributor o) {
        return Integer.compare(contributions, o.contributions);
    }
}