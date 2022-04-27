package br.com.trochadev.quarkussocial.config;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "API - Quarkus Social",
                description = "CRUD de recursos da rede social Quarkus Social",
                version = "1.0.0",
                contact = @Contact(
                        name = "Thiago Guimaraes Rocha",
                        url = "https://github.com/thiagorocha-dev/quarkus-social",
                        email = "trochadev@gmail.com"),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT")))
public class SwaggerConfig extends Application {
}
