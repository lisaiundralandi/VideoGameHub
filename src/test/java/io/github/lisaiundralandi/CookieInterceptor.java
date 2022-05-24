package io.github.lisaiundralandi;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CookieInterceptor implements ClientHttpRequestInterceptor {

    private String cookie = "JSESSION=19BFCF5202B150D1B29D06F6FFC9F195";

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution
    ) throws IOException {
        request.getHeaders()
                .add("Cookie", cookie);

        ClientHttpResponse response = execution.execute(request, body);

        String newCookie = response.getHeaders()
                .getFirst("Set-Cookie");
        if (newCookie != null) {
            cookie = newCookie;
        }

        return response;
    }
}
