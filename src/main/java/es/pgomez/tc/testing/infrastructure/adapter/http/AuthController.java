package es.pgomez.tc.testing.infrastructure.adapter.http;

import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
import es.pgomez.tc.testing.util.auth.Role;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import es.pgomez.tc.testing.util.auth.TokenUtils;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Login", description = "User authentication.")
public class AuthController {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    private final TesterService testerService;

    public AuthController(TesterService testerService) {
        this.testerService = testerService;
    }

    @PermitAll
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login", description = "Log tester in")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @APIResponse(responseCode = "403", description = "Forbidden")
    })
    public Response login(String userPass) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(userPass), StandardCharsets.UTF_8);
            String user = decodedToken.split(":")[0];
            String pass = decodedToken.split(":")[1];
            TokenDTO token = testerService.login(user, pass);
            if (token == null || token.getToken() == null) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            return Response.status(Response.Status.OK).entity(token).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    @PermitAll
    @POST
    @Path("/admin")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Admin login", description = "Log admin in")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @APIResponse(responseCode = "403", description = "Forbidden")
    })
    public Response adminLogin(String userPass) {
        try {
            String decodedToken = new String(Base64.getDecoder().decode(userPass), StandardCharsets.UTF_8);
            String user = decodedToken.split(":")[0];
            String pass = decodedToken.split(":")[1];
            if (user.equals("admin") && pass.equals("admin")) {
                TokenDTO tokenDTO = new TokenDTO(TokenUtils.generateToken(UUID.randomUUID(), Role.ADMIN, issuer));
                return Response.status(Response.Status.OK).entity(tokenDTO).build();
            }
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

}
