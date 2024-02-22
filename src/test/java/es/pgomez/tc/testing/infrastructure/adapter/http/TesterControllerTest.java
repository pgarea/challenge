package es.pgomez.tc.testing.infrastructure.adapter.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.model.common.util.Sex;
import es.pgomez.tc.testing.domain.ports.dto.MeasureDTO;
import es.pgomez.tc.testing.domain.ports.dto.PageableDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@QuarkusTest
@TestHTTPEndpoint(TesterController.class)
@TestSecurity(authorizationEnabled = false)
class TesterControllerTest {

    @InjectMock
    TesterService testerService;


    @Test
    void getEmptyTesterListTest() {
        PageableDTO<TesterDTO> pageableDTO = new PageableDTO<>(new ArrayList<>(), 0L);
        Mockito.when(testerService.getAll(null, null)).thenReturn(pageableDTO);
        when()
                .get()
                .then()
                .statusCode(200)
                .body("count", Matchers.is(0));
    }

    @Test
    void getTestListTest() {
        List<TesterDTO> testerDTOList = Collections.singletonList(getTestTester());
        PageableDTO<TesterDTO> pageableDTO = new PageableDTO<>(testerDTOList, 1L);
        Mockito.when(testerService.getAll(null, null)).thenReturn(pageableDTO);
        when()
                .get()
                .then()
                .statusCode(200)
                .body("count", Matchers.is(1));
    }

    @Test
    void getProductByIdTest() throws InstanceNotFoundException {
        TesterDTO testerDTO = getTestTester();
        Mockito.when(testerService.getById(testerDTO.getId())).thenReturn(testerDTO);
        given()
                .pathParam("id", testerDTO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(testerDTO.getId().toString()));
    }


    @Test
    void getProductByNonExistentIdTest() throws InstanceNotFoundException {
        Mockito.when(testerService.getById(Mockito.any(UUID.class))).thenThrow(new InstanceNotFoundException());
        given()
                .pathParam("id", UUID.randomUUID())
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }

    @Test
    void createProductTest() throws JsonProcessingException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TesterDTO testerDTO = getTestTester();
        JSONObject payload = toJSON(testerCreationDTO);
        Mockito.when(testerService.createTester(testerCreationDTO)).thenReturn(testerDTO);
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
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        JSONObject payload = toJSON(testerCreationDTO);
        Mockito.when(testerService.createTester(Mockito.any(TesterCreationDTO.class))).thenThrow(new ValidationException());
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
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        TesterDTO testerDTO = getTestTester();
        JSONObject payload = toJSON(testerCreationDTO);
        Mockito.when(testerService.updateTester(testerDTO.getId(), testerCreationDTO)).thenReturn(testerDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .pathParam("id", testerDTO.getId())
                .when()
                .put("/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(testerDTO.getId().toString()));
    }

    @Test
    void updateInvalidProductTest() throws InstanceNotFoundException, JsonProcessingException {
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        UUID id = UUID.randomUUID();
        JSONObject payload = toJSON(testerCreationDTO);
        Mockito.when(testerService.updateTester(id, testerCreationDTO)).thenThrow(ValidationException.class);
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
        TesterCreationDTO testerCreationDTO = getTestCreationTester();
        UUID id = UUID.randomUUID();
        JSONObject payload = toJSON(testerCreationDTO);
        Mockito.when(testerService.updateTester(id, testerCreationDTO)).thenThrow(InstanceNotFoundException.class);
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
        TesterDTO testerDTO = getTestTester();
        Mockito.doNothing().when(testerService).deleteTester(testerDTO.getId());
        given()
                .pathParam("id", testerDTO.getId())
                .when()
                .delete("/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    void deleteNonExistentProductTest() throws InstanceNotFoundException {
        Mockito.doThrow(new InstanceNotFoundException()).when(testerService).deleteTester(Mockito.any(UUID.class));
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

    private TesterDTO getTestTester() {
        TesterDTO testerDTO = new TesterDTO();
        testerDTO.setId(UUID.randomUUID());
        testerDTO.setName("Test name");
        testerDTO.setEmail("test@test.com");
        testerDTO.setBirthdate(LocalDate.now().minusDays(1L));
        testerDTO.setSex(Sex.MALE);
        testerDTO.setTestDone(1L);
        MeasureDTO measureDTO = new MeasureDTO();
        measureDTO.setCreationDate(LocalDateTime.now());
        measureDTO.setHeightCm(10d);
        measureDTO.setWeightKg(10d);
        testerDTO.setMeasures(measureDTO);
        return testerDTO;
    }

    private TesterCreationDTO getTestCreationTester() {
        TesterCreationDTO testerDTO = new TesterCreationDTO();
        testerDTO.setName("Test name");
        testerDTO.setEmail("test@test.com");
        testerDTO.setPassword("password");
        testerDTO.setBirthdate(LocalDate.now().minusDays(1L));
        testerDTO.setSex(Sex.MALE);
        testerDTO.setMeasuresCreationDate(LocalDateTime.now());
        testerDTO.setHeightCm(10d);
        testerDTO.setWeightKg(10d);
        return testerDTO;
    }
}
