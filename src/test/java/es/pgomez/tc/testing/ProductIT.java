package es.pgomez.tc.testing;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.model.Product;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.infrastructure.adapter.mongo.panache.MongoProductRepository;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
class ProductIT {

    @Inject
    MongoProductRepository mongoProductRepository;

    private final String CONTEXT_PATH = "http://localhost:8081/";

    @AfterEach
    void cleanDB() {
        mongoProductRepository.deleteAll();
    }

    @Test
    void testCreateProduct() throws IOException, URISyntaxException, InterruptedException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        TokenDTO tokenDTO = getAdminToken();
        JSONObject payload = toJSON(productCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .post("/product")
                .then()
                .statusCode(201)
                .body("id", notNullValue());
        Assertions.assertEquals(1, mongoProductRepository.findAll().count());
    }

    @Test
    void testUpdateProduct() throws IOException, URISyntaxException, InterruptedException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        TokenDTO tokenDTO = getAdminToken();
        UUID productId = createProduct(tokenDTO);
        JSONObject payload = toJSON(productCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .put("/product/" + productId)
                .then()
                .statusCode(200)
                .body("id", notNullValue());
        Assertions.assertEquals(1, mongoProductRepository.findAll().count());
        Product product = mongoProductRepository.findById(productId.toString());
        Assertions.assertEquals(productCreationDTO.getBrandName(), product.getBrand().getName().name());
    }

    @Test
    void testFindOne() throws IOException, URISyntaxException, InterruptedException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        TokenDTO tokenDTO = getAdminToken();
        UUID productId = createProduct(tokenDTO);
        JSONObject payload = toJSON(productCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .get("/product/" + productId)
                .then()
                .statusCode(200)
                .body("id", Matchers.is(productId.toString()));
    }

    @Test
    void testFindAll() throws IOException, URISyntaxException, InterruptedException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        TokenDTO tokenDTO = getAdminToken();
        createProduct(tokenDTO);
        JSONObject payload = toJSON(productCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .get("/product")
                .then()
                .statusCode(200)
                .body("count", Matchers.is(1));
    }

    @Test
    void testDelete() throws IOException, URISyntaxException, InterruptedException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        TokenDTO tokenDTO = getAdminToken();
        UUID productId = createProduct(tokenDTO);
        JSONObject payload = toJSON(productCreationDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .header(new Header("Authorization", "Bearer " + tokenDTO.getToken()))
                .body(payload)
                .when()
                .delete("/product/" + productId)
                .then()
                .statusCode(200);
        Assertions.assertEquals(0, mongoProductRepository.findAll().count());
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

    private JSONObject toJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(object), JSONObject.class);
    }

}
