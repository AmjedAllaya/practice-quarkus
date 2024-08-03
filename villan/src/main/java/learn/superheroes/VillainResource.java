package learn.superheroes;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.Operation;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.validation.Valid;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;


@Path("/api/villains")
@Tag(name = "villains")

public class VillainResource {

    Logger logger;
    VillainService service;

    public VillainResource(Logger logger, VillainService service) {
        this.service = service;
        this.logger = logger;
    }


    @Operation(summary = "Returns a random villain")
    @GET
    @APIResponse(responseCode = "200",content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
            @Path("/random")
    @Produces(APPLICATION_JSON)
    public RestResponse<Villain> getRandomVillain() {
        Villain villain = service.findRandomVillain();
        logger.debug("Found random villain " + villain);
        return RestResponse.ok(villain);
    }

    @Operation(summary = "Returns all the villains from the database")
    @APIResponse(responseCode = "200",content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY)))
    @GET
    @Produces(APPLICATION_JSON)
    public RestResponse<List<Villain>> getAllVillains() {
        List<Villain> villains = service.findAllVillains();
        logger.debug("Total number of villains " + villains.size());
        return RestResponse.ok(villains);
    }


    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @APIResponse(responseCode = "204", description = "The villain is not found for a given identifier")
    @Produces(APPLICATION_JSON)
    @Path("/{id}")
    public RestResponse<Villain> getVillain(@RestPath Long id) {
        Villain villain = service.findVillainById(id);
        if (villain != null) {
            logger.debug("Found villain " + villain);
            return RestResponse.ok(villain);
        } else {
            logger.debug("No villain found with id " + id);
            return RestResponse.noContent();
        }
    }

    @Operation(summary = "Creates a valid villain")
    @POST
    @APIResponse(responseCode = "201",description = "The URI of the created villain",content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public RestResponse<Void> createVillain(@Valid Villain villain, @Context UriInfo uriInfo) {
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debug("New villain created with URI " + builder.build().toString());
        return RestResponse.created(builder.build());
    }

    @PUT
    @Operation(summary = "Updates an exiting  villain")
    @APIResponse(responseCode = "200",description = "The updated villain",content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public RestResponse<Villain> updateVillain(@Valid Villain villain) {
        villain = service.updateVillain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return RestResponse.ok(villain);
    }

    @DELETE
    @Operation(summary = "Deletes an exiting villain")
    @APIResponse(responseCode = "204")
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    public RestResponse<Void> deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return RestResponse.noContent();
    }

    @GET
    @Path("/hello")
    @Produces(TEXT_PLAIN)
    public String hello() {
        return "Hello Villain Resource";
    }
}
