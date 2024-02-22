package es.pgomez.tc.testing.infrastructure.adapter.http;


import es.pgomez.tc.testing.domain.ports.dto.TestDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ReportingService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

@Path("/reporting")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reporting", description = "Reporting operations.")
@RolesAllowed({"TESTER"})
public class ReportingController {

    private final JsonWebToken jwt;

    private final ReportingService reportingService;

    public ReportingController(JsonWebToken jwt, ReportingService reportingService) {
        this.reportingService = reportingService;
        this.jwt = jwt;
    }

    @POST
    @Operation(summary = "Report test", description = "Report a new test")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "No content"),
            @APIResponse(responseCode = "404", description = "Product not found"),
    })
    public Response registerTest(TestDTO testDTO) {
        try {
            reportingService.reportNewTest(UUID.fromString(jwt.getSubject()), testDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
