package io.github.lisaiundralandi;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplate -> restTemplate.getInterceptors()
                .add(new CookieInterceptor());
    }
}
