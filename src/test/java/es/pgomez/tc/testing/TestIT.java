package es.pgomez.tc.testing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TestDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoProductRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTestRepository;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoTesterRepository;
import es.pgomez.tc.testing.util.auth.TokenDTO;
import es.pgomez.tc.testing.util.mapper.Mapper;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
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
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class TestIT {

    @Inject
    MongoTesterRepository mongoTesterRepository;
    @Inject
    MongoProductRepository mongoProductRepository;
    @Inject
    MongoTestRepository mongoTestRepository;

    private final String CONTEXT_PATH = "http://localhost:8081/";

    @AfterEach
    void cleanDB() {
        mongoTesterRepository.deleteAll();
        mongoProductRepository.deleteAll();
        mongoTestRepository.deleteAll();
    }

    @Test
    void registerTestTest() throws URISyntaxException, IOException, InterruptedException {
        UUID testerId = createTester(getAdminToken());
        UUID productId = createProduct(getAdminToken());
        TokenDTO tokenDTO = getTesterToken();
        TestDTO testDTO = getTest(productId);
        JSONObject payload = toJSON(testDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .post("/reporting")
                .then()
                .statusCode(204);
        Assertions.assertEquals(1, mongoTestRepository.findAll().count());
        Assertions.assertEquals(1, mongoTesterRepository.findById(testerId.toString()).getTestDone().testDone());
    }

    @Test
    void deleteTesterWithTest() throws URISyntaxException, IOException, InterruptedException {
        UUID testerId = createTester(getAdminToken());
        UUID productId = createProduct(getAdminToken());
        TokenDTO tokenDTO = getAdminToken();
        createTest(getTesterToken(), productId);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .when()
                .delete("/tester/" + testerId)
                .then()
                .statusCode(200);
        Assertions.assertEquals(0, mongoTestRepository.findAll().count());
        Assertions.assertEquals(0, mongoTesterRepository.findAll().count());
    }


    @Test
    void deleteProductWithTest() throws URISyntaxException, IOException, InterruptedException {
        UUID testerId = createTester(getAdminToken());
        UUID productId = createProduct(getAdminToken());
        TokenDTO tokenDTO = getAdminToken();
        createTest(getTesterToken(), productId);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .when()
                .delete("/product/" + productId)
                .then()
                .statusCode(200);
        Assertions.assertEquals(0, mongoTestRepository.findAll().count());
        Assertions.assertEquals(0, mongoProductRepository.findAll().count());
    }


    private TokenDTO getTesterToken() throws URISyntaxException, IOException, InterruptedException {
        String email = "test@test.com";
        String password = "password";
        String userPass = email + ":" + password;
        String token = new String(Base64.getEncoder().encode(userPass.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", MediaType.APPLICATION_JSON)
                .uri(new URI(CONTEXT_PATH + "login"))
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        JSONObject tokenObject = objectMapper.readValue(response.body(), JSONObject.class);
        return new TokenDTO((String) tokenObject.get("token"));
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

    private ProductCreationDTO getTestCreationProduct() {
        List<String> sizes = Collections.singletonList("XL");
        List<String> pictures = Collections.singletonList("http://picture.test/picture");
        ProductCreationDTO productDTO = new ProductCreationDTO();
        productDTO.setSku("SKD Test");
        productDTO.setSizes(sizes);
        productDTO.setPictures(pictures);
        productDTO.setBrandName(UUID.randomUUID().toString());
        productDTO.setBrandLogo("http://brand.logo/logo");
        productDTO.setColor("Test color");
        return productDTO;
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

    private void createTest(TokenDTO tokenDTO, UUID productId) throws URISyntaxException, IOException, InterruptedException {
        TestDTO test = getTest(productId);
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDTO.getToken())
                .uri(new URI(CONTEXT_PATH + "reporting"))
                .POST(HttpRequest.BodyPublishers.ofString(toJSON(test).toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private UUID createProduct(TokenDTO tokenDTO) throws URISyntaxException, IOException, InterruptedException {
        ProductCreationDTO product = getTestCreationProduct();
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + tokenDTO.getToken())
                .uri(new URI(CONTEXT_PATH + "product"))
                .POST(HttpRequest.BodyPublishers.ofString(toJSON(product).toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        JSONObject jsonProduct = objectMapper.readValue(response.body(), JSONObject.class);
        return UUID.fromString((String) jsonProduct.get("id"));
    }

    private TestDTO getTest(UUID productId) {
        TestDTO testDTO = new TestDTO();
        testDTO.setSize("XL");
        testDTO.setProductId(productId);
        return testDTO;
    }

    private JSONObject toJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(object), JSONObject.class);
    }
}
