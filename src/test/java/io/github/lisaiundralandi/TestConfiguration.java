package io.github.lisaiundralandi;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Configuration
public class TestConfiguration {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> {
            restTemplate.getInterceptors()
                    .add(new CookieInterceptor());
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
            restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    HttpStatus statusCode = response.getStatusCode();
                    return statusCode.series() == HttpStatus.Series.SERVER_ERROR;
                }
            });

        };
    }
}
