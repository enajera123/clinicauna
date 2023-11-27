package cr.ac.una.clinicaunaws.controller;

import java.util.List;
import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.MedicalExamDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.MedicalExamService;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
@Path("/MedicalExamController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "MedicalExamController", description = "Manage endpoints related to the MedicalExam.")
public class MedicalExamController {
    private static final Logger logger = Logger.getLogger(MedicalExamController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    MedicalExamService medicalExamService;

    /**
     * Create a new MedicalExam
     * 
     * @param medicalExamDto to be created
     * @return Response with the created MedicalExam
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new MedicalExam", description = "Create a new MedicalExam", tags = {
            "MedicalExamController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MedicalExam created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "MedicalExam data conflict"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createMedicalExam(@Valid MedicalExamDto medicalExamDto) {
        try {
            ResponseWrapper response = medicalExamService.createMedicalExam(medicalExamDto);
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
     * get a MedicalExam by id
     * 
     * @param id of the MedicalExam to be retrieved
     * @return Response with the MedicalExamDto
     */
    @GET
    @Path("/medicalExam/{id}")
    @Operation(summary = "Get a MedicalExam by id", description = "Get a MedicalExam by id", tags = {
            "MedicalExamController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalExam found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalExam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getMedicalExamById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = medicalExamService.getMedicalExamById(id);
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
     * get all MedicalExams
     * 
     * @return Response with all MedicalExams
     */
    @GET
    @Path("/medicalExams")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all MedicalExams", description = "Get all MedicalExams", tags = {
            "MedicalExamController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalExams found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalExams not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllMedicalExams() {
        try {
            ResponseWrapper response = medicalExamService.getAllMedicalExams();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(
                    new GenericEntity<List<MedicalExamDto>>((List<MedicalExamDto>) response.getData()) {
                    }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * update a MedicalExam
     * 
     * @param medicalExamDto to be updated
     * @return Response with the updated MedicalExam
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a MedicalExam", description = "Update a MedicalExam", tags = {
            "MedicalExamController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalExam updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalExam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateMedicalExam(@Valid MedicalExamDto medicalExamDto) {
        try {
            ResponseWrapper response = medicalExamService.updateMedicalExam(medicalExamDto);
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
     * delete a MedicalExam by id
     * 
     * @param id to be deleted
     * @return Response with the deleted MedicalExam
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a MedicalExam by id", description = "Delete a MedicalExam by id", tags = {
            "MedicalExamController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalExam deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalExam not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteMedicalExam(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = medicalExamService.deleteMedicalExam(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).build();

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }

    }

}
