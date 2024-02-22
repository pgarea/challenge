package es.pgomez.tc.testing;

import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title="Technical challenge API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Pablo Gómez Area",
                        email = "pablo.gomeza@icloud.com"))
)
public class TechnicalChallengeApplication extends Application { }