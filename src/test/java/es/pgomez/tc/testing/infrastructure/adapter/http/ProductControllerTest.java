package es.pgomez.tc.testing.infrastructure.adapter.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.ports.dto.BrandDTO;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ProductService;
import es.pgomez.tc.testing.util.mapper.Mapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.Header;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.MediaType;
import org.hamcrest.Matchers;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestHTTPEndpoint(ProductController.class)
@TestSecurity(authorizationEnabled = false)
class ProductControllerTest {

    @InjectMock
    ProductService productService;

    @Test
    void getEmptyProductListTest() {
        PageableDTO<ProductDTO> pageableDTO = new PageableDTO<>(new ArrayList<>(), 0L);
        Mockito.when(productService.getAll(null, null)).thenReturn(pageableDTO);
        when()
                .get()
                .then()
                .statusCode(200)
                .body("count", Matchers.is(0));
    }

    @Test
    void getProductListTest() {
        List<ProductDTO> productDTOList = Collections.singletonList(getTestProduct());
        PageableDTO<ProductDTO> pageableDTO = new PageableDTO<>(productDTOList, 1L);
        Mockito.when(productService.getAll(null, null)).thenReturn(pageableDTO);
        when()
                .get()
                .then()
                .statusCode(200)
                .body("count", Matchers.is(1));
    }

    @Test
    void getProductByIdTest() throws InstanceNotFoundException {
        ProductDTO productDTO = getTestProduct();
        Mockito.when(productService.getById(productDTO.getId())).thenReturn(productDTO);
        given()
                .pathParam("id", productDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(productDTO.getId().toString()));
    }


    @Test
    void getProductByNonExistentIdTest() throws InstanceNotFoundException {
        Mockito.when(productService.getById(Mockito.any(UUID.class))).thenThrow(new InstanceNotFoundException());
        given()
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void createProductTest() throws JsonProcessingException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        ProductDTO productDTO = getTestProduct();
        JSONObject payload = toJSON(productCreationDTO);
        Mockito.when(productService.createProduct(productCreationDTO)).thenReturn(productDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void createInvalidProductTest() throws JsonProcessingException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        JSONObject payload = toJSON(productCreationDTO);
        Mockito.when(productService.createProduct(Mockito.any(ProductCreationDTO.class))).thenThrow(new ValidationException());
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(400);
    }

    @Test
    void updateProductTest() throws InstanceNotFoundException, JsonProcessingException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        ProductDTO productDTO = getTestProduct();
        JSONObject payload = toJSON(productCreationDTO);
        Mockito.when(productService.updateProduct(productDTO.getId(), productCreationDTO)).thenReturn(productDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .pathParam("id", productDTO.getId())
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(productDTO.getId().toString()));
    }

    @Test
    void updateInvalidProductTest() throws InstanceNotFoundException, JsonProcessingException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        JSONObject payload = toJSON(productCreationDTO);
        UUID id = UUID.randomUUID();
        Mockito.when(productService.updateProduct(id, productCreationDTO)).thenThrow(ValidationException.class);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .pathParam("id", id)
                .when()
                .put("/{id}")
                .then()
                .statusCode(400);
    }

    @Test
    void updateNonExistentProductTest() throws InstanceNotFoundException, JsonProcessingException {
        ProductCreationDTO productCreationDTO = getTestCreationProduct();
        JSONObject payload = toJSON(productCreationDTO);
        UUID id = UUID.randomUUID();
        Mockito.when(productService.updateProduct(id, productCreationDTO)).thenThrow(InstanceNotFoundException.class);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .pathParam("id", id)
                .when()
                .put("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void deleteProductTest() throws InstanceNotFoundException {
        ProductDTO productDTO = getTestProduct();
        Mockito.doNothing().when(productService).deleteProduct(productDTO.getId());
        given()
                .pathParam("id", productDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteNonExistentProductTest() throws InstanceNotFoundException {
        Mockito.doThrow(new InstanceNotFoundException()).when(productService).deleteProduct(Mockito.any(UUID.class));
        given()
                .pathParam("id", UUID.randomUUID())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(404);
    }

    private JSONObject toJSON(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(object), JSONObject.class);
    }

    private ProductDTO getTestProduct() {
        List<String> sizes = Collections.singletonList("SizeTest");
        List<String> pictures = Collections.singletonList("PictureTest");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(UUID.randomUUID());
        productDTO.setSku("SKD Test");
        productDTO.setSizes(sizes);
        productDTO.setPictures(pictures);
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("Test brand");
        brandDTO.setLogo("http://test.com");
        productDTO.setBrand(brandDTO);
        productDTO.setColor("Test color");
        return productDTO;
    }

    private ProductCreationDTO getTestCreationProduct() {
        List<String> sizes = Collections.singletonList("SizeTest");
        List<String> pictures = Collections.singletonList("PictureTest");
        ProductCreationDTO productDTO = new ProductCreationDTO();
        productDTO.setSku("SKD Test");
        productDTO.setSizes(sizes);
        productDTO.setPictures(pictures);
        productDTO.setBrandName("Test brand");
        productDTO.setBrandLogo("Test brand logo");
        productDTO.setColor("Test color");
        return productDTO;
    }


}
