package cr.ac.una.clinicaunaws.controller;

import java.util.List;
import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.ReportParametersDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.ReportParametersService;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 * 
 * @author arayaroma
 */
@Secure
@Path("/ReportParametersController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "ReportParametersController", description = "Manage endpoints related to the ReportParameters.")
public class ReportParametersController {
    private static final Logger logger = Logger.getLogger(ReportParametersController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    ReportParametersService reportParametersService;

    /**
     * Create a new ReportParameters
     * 
     * @param reportParametersDto to be created
     * @return Response with the created ReportParameters
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new ReportParameters", description = "Create a new ReportParameters", tags = {
            "ReportParametersController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "ReportParameters created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    public Response createReportParameters(@Valid ReportParametersDto reportParametersDto) {
        try {
            ResponseWrapper response = reportParametersService.createReportParameters(reportParametersDto);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * get a ReportParameters by id
     * 
     * @param id of the ReportParameters to be fetched
     * @return Response with the fetched ReportParameters
     */
    @GET
    @Path("/reportParameters/{id}")
    @Operation(summary = "Get a ReportParameters by id", description = "Get a ReportParameters by id", tags = {
            "ReportParametersController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ReportParameters found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "ReportParameters not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getReportParametersById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = reportParametersService.getReportParametersById(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * get all ReportParameters
     * 
     * @return Response with all the ReportParameters
     */
    @GET
    @Path("/reportParameters")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all ReportParameters", description = "Get all ReportParameters", tags = {
            "ReportParametersController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ReportParameters found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "ReportParameters not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getAllReportValues() {
        try {
            ResponseWrapper response = reportParametersService.getAllReportParameters();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(
                    new GenericEntity<List<ReportParametersDto>>((List<ReportParametersDto>) response.getData()) {
                    }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Update a ReportParameters
     * 
     * @param reportParametersDto to be updated
     * @return Response with the updated ReportParameters
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a ReportParameters", description = "Update a ReportParameters", tags = {
            "ReportParametersController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ReportParameters updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "ReportParameters not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateReportParameters(@Valid ReportParametersDto reportParametersDto) {
        try {
            ResponseWrapper response = reportParametersService.updateReportParameters(reportParametersDto);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Delete a ReportParameters
     * 
     * @param id of the ReportParameters to be deleted
     * @return Response with the deleted ReportParameters
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a ReportParameters by id", description = "Delete a ReportParameters by id", tags = {
            "ReportParametersController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ReportParameters deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "ReportParameters not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response deleteReportParameters(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = reportParametersService.deleteReportParameters(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
