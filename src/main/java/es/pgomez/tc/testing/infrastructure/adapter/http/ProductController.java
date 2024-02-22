package es.pgomez.tc.testing.infrastructure.adapter.http;


import es.pgomez.tc.testing.domain.ports.dto.ProductCreationDTO;
import es.pgomez.tc.testing.domain.ports.dto.ProductDTO;
import es.pgomez.tc.testing.domain.ports.input.interfaces.ProductService;
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

@Path("/product")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Product", description = "Product operations.")
@RolesAllowed({"ADMIN"})
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GET
    @Operation(summary = "Get products", description = "Lists all products")
    @APIResponses(
            value = @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class)))
    )
    public Response getProducts(@QueryParam("page") Long page, @QueryParam("size") Long size) {
        return Response.status(Response.Status.OK).entity(productService.getAll(page, size)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get by id", description = "Gets a product by its id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDTO.class))),
            @APIResponse(responseCode = "404", description = "Not found"),
    })
    public Response getProductById(@PathParam("id") UUID id) {
        try {
            return Response.status(Response.Status.OK).entity(productService.getById(id)).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Operation(summary = "Create", description = "Creates a new product")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCreationDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "plain/text")),
    })
    public Response createProduct(ProductCreationDTO product) {
        try {
            return Response.status(Response.Status.CREATED).entity(productService.createProduct(product)).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update", description = "Updates an existing product")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductCreationDTO.class))),
            @APIResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "plain/text")),
            @APIResponse(responseCode = "404", description = "Not Found")
    })
    public Response updateProduct(@PathParam("id") UUID id, ProductCreationDTO product) {
        try {
            return Response.status(Response.Status.OK).entity(productService.updateProduct(id, product)).build();
        } catch (ValidationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete", description = "Deletes a product")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success"),
            @APIResponse(responseCode = "404", description = "Not found")
    })
    public Response deleteProduct(@PathParam("id") UUID id) {
        try {
            productService.deleteProduct(id);
            return Response.status(Response.Status.OK).build();
        } catch (InstanceNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
