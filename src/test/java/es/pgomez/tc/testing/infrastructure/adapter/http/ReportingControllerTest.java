package es.pgomez.tc.testing.infrastructure.adapter.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.pgomez.tc.testing.domain.ports.dto.TestDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ReportingService;
import es.pgomez.tc.testing.util.mapper.Mapper;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.Header;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.json.internal.json_simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@QuarkusTest
@TestHTTPEndpoint(ReportingController.class)
@TestSecurity(authorizationEnabled = false)
class ReportingControllerTest {

    @InjectMock
    ReportingService reportingService;

    @InjectMock
    JsonWebToken jwt;

    @Test
    void registerTestTest() throws InstanceNotFoundException, JsonProcessingException {
        TestDTO testDTO = getTestTest();
        JSONObject payload = toJSON(testDTO);
        Mockito.when(jwt.getSubject()).thenReturn(UUID.randomUUID().toString());
        Mockito.doNothing().when(reportingService).reportNewTest(UUID.randomUUID(), testDTO);
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(204);
    }

    @Test
    void registerTestForNonExistentProductTest() throws InstanceNotFoundException, JsonProcessingException {
        TestDTO testDTO = getTestTest();
        JSONObject payload = toJSON(testDTO);
        Mockito.when(jwt.getSubject()).thenReturn(UUID.randomUUID().toString());
        Mockito.doThrow(new InstanceNotFoundException()).when(reportingService).reportNewTest(Mockito.any(UUID.class), Mockito.any(TestDTO.class));
        given()
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .body(payload)
                .when()
                .post()
                .then()
                .statusCode(404);
    }

    private JSONObject toJSON(TestDTO testDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = Mapper.getObjectMapper();
        return objectMapper.readValue(objectMapper.writeValueAsString(testDTO), JSONObject.class);
    }

    private TestDTO getTestTest() {
        TestDTO testDTO = new TestDTO();
        testDTO.setProductId(UUID.randomUUID());
        testDTO.setSize("M");
        return testDTO;
    }
}
