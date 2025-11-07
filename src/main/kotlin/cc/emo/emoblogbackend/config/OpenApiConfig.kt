package cc.emo.emoblogbackend.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.models.security.SecurityRequirement as ModelSecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme as ModelSecurityScheme

private const val BEARER_SCHEME = "bearerAuth"

@Configuration
@OpenAPIDefinition(
    security = [SecurityRequirement(name = BEARER_SCHEME)],
    info = Info(
        title = "EmoBlog API",
        version = "v1",
        description = "REST API for EmoBlog services.",
        contact = Contact(name = "EmoBlog Team"),
    )
)
@SecurityScheme(
    name = BEARER_SCHEME,
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    BEARER_SCHEME,
                    ModelSecurityScheme()
                        .type(ModelSecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                )
            )
            .addSecurityItem(ModelSecurityRequirement().addList(BEARER_SCHEME))
}
