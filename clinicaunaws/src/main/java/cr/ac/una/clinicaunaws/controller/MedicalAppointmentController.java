package cr.ac.una.clinicaunaws.controller;

import java.util.List;
import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.MedicalAppointmentDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.MedicalAppointmentService;
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
@Path("/MedicalAppointmentController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "MedicalAppointmentController", description = "Manage endpoints related to the MedicalAppointment.")
public class MedicalAppointmentController {
    private static final Logger logger = Logger.getLogger(MedicalAppointmentController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    MedicalAppointmentService medicalAppointmentService;

    /**
     * Create a new MedicalAppointment
     * 
     * @param medicalAppointmentDto to be created
     * @return Response with the created MedicalAppointment
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new MedicalAppointment", description = "Create a new MedicalAppointment", tags = {
            "MedicalAppointmentController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "MedicalAppointment created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "MedicalAppointment already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response createMedicalAppointment(@Valid MedicalAppointmentDto medicalAppointmentDto) {
        try {
            ResponseWrapper response = medicalAppointmentService.createMedicalAppointment(medicalAppointmentDto);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            System.out.println("Error en el controller: " + e.getMessage());
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * get a MedicalAppointment by id
     * 
     * @param id of the MedicalAppointment to be retrieved
     * @return Response with the requested MedicalAppointment
     */
    @GET
    @Path("/medicalAppointment/{id}")
    @Operation(summary = "Get a MedicalAppointment by id", description = "Get a MedicalAppointment by id", tags = {
            "MedicalAppointmentController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalAppointment found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalAppointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getMedicalAppointmentById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = medicalAppointmentService.getMedicalAppointmentById(id);
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
     * get all MedicalAppointments
     * 
     * @return Response with all MedicalAppointments
     */
    @GET
    @Path("/medicalAppointments")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all MedicalAppointments", description = "Get all MedicalAppointments", tags = {
            "MedicalAppointmentController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalAppointments found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalAppointments not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getAllMedicalAppointments() {
        try {
            ResponseWrapper response = medicalAppointmentService.getAllMedicalAppointments();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(
                    new GenericEntity<List<MedicalAppointmentDto>>((List<MedicalAppointmentDto>) response.getData()) {
                    }).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * update a MedicalAppointment
     * 
     * @param medicalAppointmentDto to be updated
     * @return Response with the updated MedicalAppointment
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a MedicalAppointment", description = "Update a MedicalAppointment", tags = {
            "MedicalAppointmentController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalAppointment updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalAppointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateMedicalAppointment(@Valid MedicalAppointmentDto medicalAppointmentDto) {
        try {
            System.out.println(medicalAppointmentDto.toString());
            ResponseWrapper response = medicalAppointmentService.updateMedicalAppointment(medicalAppointmentDto);
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
     * delete a MedicalAppointment by id
     * 
     * @param id of the MedicalAppointment to be deleted
     * @return Response with the deleted MedicalAppointment
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a MedicalAppointment by id", description = "Delete a MedicalAppointment by id", tags = {
            "MedicalAppointmentController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "MedicalAppointment deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "MedicalAppointment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteMedicalAppointment(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = medicalAppointmentService.deleteMedicalAppointment(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getMessage()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

}
