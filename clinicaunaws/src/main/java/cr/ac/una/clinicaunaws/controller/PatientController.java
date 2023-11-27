package cr.ac.una.clinicaunaws.controller;

import cr.ac.una.clinicaunaws.dto.PatientDto;
import cr.ac.una.clinicaunaws.dto.UserDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.PatientService;
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
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author arayaroma
 * @author vargas
 */
@Secure
@Path("/PatientController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "PatientController", description = "Manage endpoints related to the Patient.")
public class PatientController {
    private static final Logger logger = Logger.getLogger(PatientController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    PatientService patientService;

    /**
     * Create a new Patient
     *
     * @param patientDto to be created
     * @return Response with the created Patient
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new Patient", description = "Create a new Patient", tags = {
            "PatientController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Patient already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createPatient(@Valid PatientDto patientDto) {
        try {
            ResponseWrapper response = patientService.createPatient(patientDto);
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
     * Get a Patient by id
     *
     * @param id to be fetched
     * @return Response with the Patient
     */
    @GET
    @Path("/patient/{id}")
    @Operation(summary = "Get a Patient by id", description = "Get a Patient by id", tags = {
            "PatientController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getPatientById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientService.getPatientById(id);
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
     * Get all the doctors
     *
     * @return Response with the list of Patients
     */
    @GET
    @Path("/patients")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all Patients", description = "Get all Patients", tags = {
            "PatientController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patients not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getPatients() {
        try {
            ResponseWrapper response = patientService.getPatients();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(new GenericEntity<List<UserDto>>((List<UserDto>) response.getData()) {
            }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Update a doctor
     *
     * @param patientDto to be updated
     * @return Response with the updated Patient
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a Patient", description = "Update a Patient", tags = {
            "PatientController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updatePatient(@Valid PatientDto patientDto) {
        try {
            ResponseWrapper response = patientService.updatePatient(patientDto);
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
     * delete a Patient by id
     *
     * @param id to be deleted
     * @return Response with the deleted Patient
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a Patient by id", description = "Delete a Patient by id", tags = {
            "PatientController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deletePatientById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = patientService.deletePatientById(id);
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
