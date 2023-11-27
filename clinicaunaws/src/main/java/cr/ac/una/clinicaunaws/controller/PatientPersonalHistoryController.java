package cr.ac.una.clinicaunaws.controller;

import java.util.List;
import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.PatientPersonalHistoryDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.PatientPersonalHistoryService;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
@Path("/PatientPersonalHistoryController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "PatientPersonalHistoryController", description = "Manage endpoints related to the PatientPersonalHistory.")
public class PatientPersonalHistoryController {
    private static final Logger logger = Logger.getLogger(PatientPersonalHistoryController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    PatientPersonalHistoryService patientPersonalHistoryService;

    /**
     * Create a new PatientPersonalHistory
     * 
     * @param patientPersonalHistoryDto to be created
     * @return Response with the created PatientPersonalHistory
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new PatientPersonalHistory", description = "Create a new PatientPersonalHistory", tags = {
            "PatientPersonalHistoryController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "PatientPersonalHistory created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createPatientPersonalHistory(@Valid PatientPersonalHistoryDto patientPersonalHistoryDto) {
        try {
            ResponseWrapper response = patientPersonalHistoryService
                    .createPatientPersonalHistory(patientPersonalHistoryDto);
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
     * get a PatientPersonalHistory by id
     * 
     * @param id of the PatientPersonalHistory to be retrieved
     * @return Response with the retrieved PatientPersonalHistory
     */
    @GET
    @Path("/patientPersonalHistory/{id}")
    @Operation(summary = "Get a PatientPersonalHistory by id", description = "Get a PatientPersonalHistory by id", tags = {
            "PatientPersonalHistoryController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientPersonalHistory found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientPersonalHistory not found")
    })
    public Response getPatientPersonalHistoryById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientPersonalHistoryService.getPatientPersonalHistoryById(id);
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
     * get all PatientPersonalHistory
     * 
     * @return Response with the retrieved PatientPersonalHistory
     */
    @GET
    @Path("/patientPersonalHistory")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all PatientPersonalHistory", description = "Get all PatientPersonalHistory", tags = {
            "PatientPersonalHistoryController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientPersonalHistory found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientPersonalHistory not found")
    })
    public Response getAllPatientPersonalHistory() {
        try {
            ResponseWrapper response = patientPersonalHistoryService.getAllPatientPersonalHistory();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(
                    new GenericEntity<List<PatientPersonalHistoryDto>>(
                            (List<PatientPersonalHistoryDto>) response.getData()) {
                    }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * update a PatientPersonalHistory
     * 
     * @param patientPersonalHistoryDto to be updated
     * @return Response with the updated PatientPersonalHistory
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a PatientPersonalHistory", description = "Update a PatientPersonalHistory", tags = {
            "PatientPersonalHistoryController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientPersonalHistory updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updatePatientPersonalHistory(@Valid PatientPersonalHistoryDto patientPersonalHistoryDto) {
        try {
            ResponseWrapper response = patientPersonalHistoryService
                    .updatePatientPersonalHistory(patientPersonalHistoryDto);
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
     * delete a PatientPersonalHistory by id
     * 
     * @param id to be deleted
     * @return Response with the deleted PatientPersonalHistory
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a PatientPersonalHistory by id", description = "Delete a PatientPersonalHistory by id", tags = {
            "PatientPersonalHistoryController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientPersonalHistory deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public Response deletePatientPersonalHistory(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientPersonalHistoryService.deletePatientPersonalHistory(id);
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
