package es.pgomez.tc.testing.infrastructure.adapter.http;

import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.Header;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(AuthController.class)
@TestSecurity(authorizationEnabled = false)
class AuthControllerTest {

    @InjectMock
    TesterService testerService;

    @Test
    void testLogin() {
        String email = "admin@admin.com";
        String password = "password";
        String userPass = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Mockito.when(testerService.login(email, password)).thenReturn(new TokenDTO("token"));
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(token)
                .when()
                .post()
                .then()
                .statusCode(200);
    }

    @Test
    void testInvalidLogin() {
        String email = "admin@admin.com";
        String password = "password";
        String userPass = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Mockito.when(testerService.login(email, password)).thenReturn(null);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(token)
                .when()
                .post()
                .then()
                .statusCode(403);
    }

    @Test
    void testAdminLogin() {
        String email = "admin";
        String password = "admin";
        String userPass = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Mockito.when(testerService.login(email, password)).thenReturn(new TokenDTO("token"));
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(token)
                .when()
                .post("/admin")
                .then()
                .statusCode(200);
    }

    @Test
    void testInvalidAdminLogin() {
        String email = "admin";
        String userPass = email;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        Mockito.when(testerService.login(email, null)).thenReturn(null);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(token)
                .when()
                .post("/admin")
                .then()
                .statusCode(403);
    }
}
