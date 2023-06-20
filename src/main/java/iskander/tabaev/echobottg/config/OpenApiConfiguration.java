package iskander.tabaev.echobottg.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUiPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("API Документация на Эхо-Телеграмм бота").version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("v1")
                .packagesToScan("iskander.tabaev.echobottg.controllers") // Замените на путь к вашему API v1
                .build();
    }

}