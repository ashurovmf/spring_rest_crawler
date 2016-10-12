package com.gft.backend.services;


import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by miav on 2016-10-11.
 */
@Service
public class FacebookHttpService {
    private static final Logger logger = Logger.getLogger(FacebookHttpService.class);

    @Autowired
    HttpClient client;

    public String sendHttpRequest(String methodName, String url, String[] names, String[] values)
            throws HttpException, IOException {
        if (names.length != values.length)
            throw new IllegalArgumentException("Names and values are not equal");
        if (!"GET".equalsIgnoreCase(methodName) && !"POST".equalsIgnoreCase(methodName))
            throw  new IllegalArgumentException("Method supports only GET and POST");
        HttpRequestBase method;
        if ("GET".equalsIgnoreCase(methodName)) {
            method = new HttpGet(getURLEncoded(url, names, values));
        } else {
            method = new HttpPost(getURLEncoded(url, names, values));
            method.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }
        HttpResponse response = client.execute(method);
        String responseString = EntityUtils.toString(response.getEntity());
        return responseString;
    }

    private String getURLEncoded(String url, String[] names, String[] values) {
        StringBuilder builder = new StringBuilder(64);
        builder.append(url);
        builder.append("?");
        for (int i = 0; i < names.length - 1; i++) {
            builder.append(names[i]);
            builder.append("=");
            builder.append(values[i]);
            builder.append("&");
        }
        builder.append(names[names.length - 1]);
        builder.append("=");
        builder.append(values[names.length - 1]);
        return builder.toString();
    }

}
