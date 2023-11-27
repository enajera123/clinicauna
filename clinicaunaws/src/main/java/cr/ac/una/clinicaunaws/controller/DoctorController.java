package cr.ac.una.clinicaunaws.controller;

import cr.ac.una.clinicaunaws.dto.DoctorDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.DoctorService;
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
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author arayaroma
 * @author vargas
 */
@Secure
@Path("/DoctorController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "DoctorController", description = "Manage endpoints related to the Doctor.")
public class DoctorController {
    private static final Logger logger = Logger.getLogger(DoctorController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    DoctorService doctorService;

    /**
     * Create a new Doctor
     *
     * @param doctorDto to be created
     * @return Response with the created Doctor
     */
    @POST
    @Path("/create")
    @Operation(summary = "Create a new Doctor", description = "Create a new Doctor", tags = {
            "DoctorController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "Doctor already exists"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createDoctor(@Valid DoctorDto doctorDto) {
        try {
            ResponseWrapper response = doctorService.createDoctor(doctorDto);
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
     * Get a Doctor by id
     * 
     * @param id to be fetched
     * @return Response with the Doctor
     */
    @GET
    @Path("/doctor/{id}")
    @Operation(summary = "Get a Doctor by id", description = "Get a Doctor by id", tags = {
            "DoctorController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Doctor not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getDoctorById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = doctorService.getDoctorById(id);
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
     * @return Response with the list of Doctors
     */
    @GET
    @Path("/doctors")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all Doctors", description = "Get all Doctors", tags = {
            "DoctorController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctors found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getDoctors() {
        try {
            ResponseWrapper response = doctorService.getDoctors();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(new GenericEntity<List<DoctorDto>>((List<DoctorDto>) response.getData()) {
            }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Update a doctor
     * 
     * @param doctorDto to be updated
     * @return Response with the updated Doctor
     */
    @PUT
    @Path("/update")
    @Operation(summary = "Update a Doctor", description = "Update a Doctor", tags = {
            "DoctorController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor updated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateDoctor(@Valid DoctorDto doctorDto) {
        try {
            ResponseWrapper response = doctorService.updateDoctor(doctorDto);
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
     * delete a Doctor by id
     * 
     * @param id to be deleted
     * @return Response with the deleted Doctor
     */
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a Doctor by id", description = "Delete a Doctor by id", tags = {
            "DoctorController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor deleted"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response deleteDoctorById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = doctorService.deleteDoctorById(id);
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
