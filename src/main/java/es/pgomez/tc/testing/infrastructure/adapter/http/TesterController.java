package es.pgomez.tc.testing.infrastructure.adapter.http;

import es.pgomez.tc.testing.domain.ports.dto.TesterCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.TesterDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.TesterService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.management.InstanceNotFoundException;
import java.util.UUID;

@Path("/tester")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Tester", description = "Tester operations.")
@RolesAllowed({"ADMIN"})
public class TesterController {

    private final TesterService testerService;

    public TesterController(TesterService testerService) {
        this.testerService = testerService;
    }

    @GET
    @Operation(summary = "Get testers", description = "Lists all testers")
    @APIResponses(
            value = @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TesterDTO.class)))
    )
    public Response getTesters(@QueryParam("page") Long page, @QueryParam("size") Long size) {
        return Response.status(Response.Status.OK).entity(testerService.getAll(page, size)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get by id", description = "Lists all testers")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TesterDTO.class))),
            @APIResponse(responseCode = "404", description = "Not Found")
    })
    public Response getTesterById(@PathParam("id") UUID id) {
        try {
            return Response.status(Response.Status.OK).entity(testerService.getById(id)).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }


    @POST
    @Operation(summary = "Create", description = "Creates a new tester")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TesterCreationDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "plain/text"))
    })
    public Response createTester(TesterCreationDTO tester) {
        try {
            return Response.status(Response.Status.CREATED).entity(testerService.createTester(tester)).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update", description = "Updates an existing tester")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TesterCreationDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "plain/text")),
            @APIResponse(responseCode = "404", description = "Not Found")
    })
    public Response updateTester(@PathParam("id") UUID id, TesterCreationDTO tester) {
        try {
            return Response.status(Response.Status.OK).entity(testerService.updateTester(id, tester)).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete", description = "Deletes a tester")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "404", description = "Not found")
    })
    public Response deleteTester(@PathParam("id") UUID id) {
        try {
            testerService.deleteTester(id);
            return Response.status(Response.Status.OK).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
