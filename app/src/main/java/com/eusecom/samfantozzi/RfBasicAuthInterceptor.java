package com.eusecom.samfantozzi;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static java.lang.String.format;

public class RfBasicAuthInterceptor implements Interceptor {

    private String githubToken;

    public RfBasicAuthInterceptor(String token) {
        this.githubToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
            .header("Authorization", format("token %s", githubToken)).build();
        return chain.proceed(authenticatedRequest);
    }

}