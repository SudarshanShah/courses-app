package dev.ssh.courses.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info()
                .title("Course App")
                .version("1.0")
                .description("This API exposes endpoints for managing courses");

        return new OpenAPI().info(info);
    }
}
