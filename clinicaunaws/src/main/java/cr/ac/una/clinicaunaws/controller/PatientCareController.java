package cr.ac.una.clinicaunaws.controller;

import java.util.List;
import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.PatientCareDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.PatientCareService;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@Path("/PatientCareController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "PatientCareController", description = "Manage endpoints related to the PatientCare.")
public class PatientCareController {
    private static final Logger logger = Logger.getLogger(PatientCareController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    PatientCareService patientCareService;

    /**
     * Create a new PatientCare
     * 
     * @param patientCareDto to be created
     * @return Response with the created PatientCare
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new PatientCare", description = "Create a new PatientCare", tags = {
            "PatientCareController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "PatientCare created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "PatientCare already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createPatientCare(@Valid PatientCareDto patientCareDto) {
        try {
            ResponseWrapper response = patientCareService.createPatientCare(patientCareDto);
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
     * get a PatientCare by id
     *
     * @param id of the PatientCare to be retrieved
     * @return Response with the retrieved PatientCare
     */
    @GET
    @Path("/patientCare/{id}")
    @Operation(summary = "Get a PatientCare by id", description = "Get a PatientCare by id", tags = {
            "PatientCareController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientCare found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientCare not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getPatientCareById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientCareService.getPatientCareById(id);
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
     * get all PatientCare
     * 
     * @return Response with the retrieved PatientCare
     */
    @GET
    @Path("/patientCare")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all PatientCare", description = "Get all PatientCare", tags = {
            "PatientCareController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientCare found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientCare not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllPatientCare() {
        try {
            ResponseWrapper response = patientCareService.getAllPatientCare();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(
                    new GenericEntity<List<PatientCareDto>>((List<PatientCareDto>) response.getData()) {
                    }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * update a PatientCare
     * 
     * @param patientCareDto to be updated
     * @return Response with the updated PatientCare
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a PatientCare", description = "Update a PatientCare", tags = {
            "PatientCareController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientCare updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientCare not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updatePatientCare(@Valid PatientCareDto patientCareDto) {
        try {
            ResponseWrapper response = patientCareService.updatePatientCare(patientCareDto);
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
     * delete a PatientCare by id
     * 
     * @param id to be deleted
     * @return Response with the deleted PatientCare
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a PatientCare by id", description = "Delete a PatientCare by id", tags = {
            "PatientCareController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PatientCare deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "PatientCare not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deletePatientCare(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientCareService.deletePatientCare(id);
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
