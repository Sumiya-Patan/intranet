package user_management.user_management.auth.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            // UI metadata (optional)
            .info(new Info()
                 .title("User‑Management API")
                 .version("v1.0"))
            // add security requirement so it's pre‑applied to every endpoint
            .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
            // register the scheme (type HTTP, scheme bearer, format JWT)
            .components(new Components().addSecuritySchemes(
                SCHEME_NAME,
                new SecurityScheme()
                    .name("Authorization")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
            ));
    }
}
