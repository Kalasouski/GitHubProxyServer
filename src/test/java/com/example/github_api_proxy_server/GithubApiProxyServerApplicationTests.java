package com.example.github_api_proxy_server;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class GithubApiProxyServerApplicationTests {
    @Test
    public void nonExistingOrg() throws IOException {
        HttpUriRequest request = new HttpGet( "http://localhost:8080/org/smthrandomorg/contributors" );
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        HttpEntity entity = httpResponse.getEntity();
        String result = EntityUtils.toString(entity);

        assertEquals(result,"{error :)}");
    }

    @Test
    public void adobeOrg() throws IOException {
        HttpUriRequest request = new HttpGet( "http://localhost:8080/org/adobe/contributors" );
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
        HttpEntity entity = httpResponse.getEntity();
        String result = EntityUtils.toString(entity);

        assertEquals(result,"[{\"contributions\":4755,\"name\":\"brianpaul\"},{\"contributions\":2727,\"name\":" +
                "\"jrfonseca\"},{\"contributions\":2575,\"name\":\"anholt\"},{\"contributions\":1842,\"name\":" +
                "\"redmunds\"},{\"contributions\":1724,\"name\":\"jasonsanjose\"},{\"contributions\":1433,\"name\":" +
                "\"njx\"},{\"contributions\":1331,\"name\":\"marekolsak\"},{\"contributions\":1157,\"name\":" +
                "\"airlied\"},{\"contributions\":1139,\"name\":\"ianromanick\"},{\"contributions\":1064,\"name\":" +
                "\"peterflynn\"}]");
    }
}
