package es.pgomez.tc.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.model.Tester;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTesterRepository;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import es.pgomez.tc.testing.util.mapper.Mapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class TesterIT {

    @Inject
    MongoTesterRepository mongoTesterRepository;

    private final String CONTEXT_PATH = "http://localhost:8081/";

    @AfterEach
    void cleanDB() {
        mongoTesterRepository.deleteAll();
    }

    @Test
    void testCreateTester() throws IOException, URISyntaxException, InterruptedException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TokenDTO tokenDTO = getAdminToken();
        JSONObject payload = toJSON(testerCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .post("/tester")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
        Assertions.assertEquals(1, mongoTesterRepository.findAll().count());
    }

    @Test
    void testUpdateTester() throws IOException, URISyntaxException, InterruptedException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TokenDTO tokenDTO = getAdminToken();
        UUID testerId = createTester(tokenDTO);
        JSONObject payload = toJSON(testerCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .put("/tester/" + testerId.toString())
                .then()
                .statusCode(200)
                .body("id", notNullValue());
        Assertions.assertEquals(1, mongoTesterRepository.findAll().count());
        Tester tester = mongoTesterRepository.findById(testerId.toString());
        Assertions.assertEquals(testerCreationDTO.getName(), tester.getName().name());
    }

    @Test
    void testFindOne() throws IOException, URISyntaxException, InterruptedException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TokenDTO tokenDTO = getAdminToken();
        UUID testerId = createTester(tokenDTO);
        JSONObject payload = toJSON(testerCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .get("/tester/" + testerId)
                .then()
                .statusCode(200)
                .body("id", Matchers.is(testerId.toString()));
    }

    @Test
    void testFindAll() throws IOException, URISyntaxException, InterruptedException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TokenDTO tokenDTO = getAdminToken();
        createTester(tokenDTO);
        JSONObject payload = toJSON(testerCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .get("/tester")
                .then()
                .statusCode(200)
                .body("count", Matchers.is(1));
    }

    @Test
    void testDelete() throws IOException, URISyntaxException, InterruptedException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TokenDTO tokenDTO = getAdminToken();
        UUID testerId = createTester(tokenDTO);
        JSONObject payload = toJSON(testerCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .delete("/tester/" + testerId)
                .then()
                .statusCode(200);
        Assertions.assertEquals(0, mongoTesterRepository.findAll().count());
    }

    private TesterCreationDTO getTestCreationTester() {
        TesterCreationDTO testerDTO = new TesterCreationDTO();
        testerDTO.setName(UUID.randomUUID().toString());
        testerDTO.setEmail("test@test.com");
        testerDTO.setPassword("password");
        testerDTO.setBirthdate(LocalDate.now().minusDays(2L));
        testerDTO.setSex(Sex.MALE);
        testerDTO.setMeasuresCreationDate(LocalDateTime.now().minusDays(1L));
        testerDTO.setHeightCm(10d);
        testerDTO.setWeightKg(10d);
        return testerDTO;
    }

    private TokenDTO getAdminToken() throws URISyntaxException, IOException, InterruptedException {
        String email = "admin";
        String password = "admin";
        String userPass = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", MediaType.APPLICATION_JSON)
                .uri(new URI(CONTEXT_PATH + "login/admin"))
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        JSONObject tokenObject = objectMapper.readValue(response.body(), JSONObject.class);
        return new TokenDTO((String) tokenObject.get("token"));
    }

    private UUID createTester(TokenDTO tokenDTO) throws URISyntaxException, IOException, InterruptedException {
        TesterCreationDTO tester = getTestCreationTester();
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDTO.getToken())
                .uri(new URI(CONTEXT_PATH + "tester"))
                .POST(HttpRequest.BodyPublishers.ofString(toJSON(tester).toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        JSONObject jsonTester = objectMapper.readValue(response.body(), JSONObject.class);
        return UUID.fromString((String) jsonTester.get("id"));
    }

    private JSONObject toJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(object), JSONObject.class);
    }
}
