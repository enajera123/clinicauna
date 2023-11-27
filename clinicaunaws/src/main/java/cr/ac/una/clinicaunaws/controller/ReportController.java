package cr.ac.una.clinicaunaws.controller;

import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.ReportDto;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.ReportService;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

/**
 *
 * @author varga
 * @author arayaroma
 */
@Secure
@Path("/ReportController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "ReportController", description = "Manage endpoints related to the Report.")
public class ReportController {

    private static final Logger logger = Logger.getLogger(ReportController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    ReportService reportService;

    @POST
    @Path("/create")
    @Operation(summary = "Create a new Report", description = "Create a new Report", tags = {"ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Report created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createReport(@Valid ReportDto reportDto) {
        try {
            ResponseWrapper response = reportService.createReport(reportDto);
            return Response.status(response.getStatus())
                    .entity(response.getData())
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error creating the report: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/report/{id}")
    @Operation(summary = "Get a Report by id", description = "Get a Report by id", tags = {"ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getReport(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = reportService.getReportById(id);
            return Response.status(response.getStatus())
                    .entity(response.getData())
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting the report: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/report")
    @Operation(summary = "Get all Reports", description = "Get all Reports", tags = {"ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reports found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Reports not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response getAllReports() {
        try {
            ResponseWrapper response = reportService.getAllReports();
            return Response.status(response.getStatus())
                    .entity(response.getData())
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting the reports: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update")
    @Operation(summary = "Update a Report", description = "Update a Report", tags = {"ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response updateReport(@Valid ReportDto reportDto) {
        try {
            ResponseWrapper response = reportService.updateReport(reportDto);
            return Response.status(response.getStatus())
                    .entity(response.getData())
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error updating the report: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a Report by id", description = "Delete a Report by id", tags = {"ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Report deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "404", description = "Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response deleteReport(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = reportService.deleteReport(id);
            return Response.status(response.getStatus())
                    .entity(response.getData())
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error deleting the report: " + e.getMessage()).build();
        }
    }

    /**
     * FIXME: Finish implementation
     *
     * @param id
     * @return
     */
    @GET
    @Path("/createPatientReport/{id}/{language}")
    @Operation(summary = "Create a Patient Report", description = "Create a Patient Report", tags = {
        "ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Patient Report created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Patient Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createPatientReport(@PathParam("id") Long id, @PathParam("language") String language) {
        try {
            MediaType contentType;
            ResponseWrapper response = reportService.createPatientReport(id, language);
            if (response.getData() instanceof byte[]) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            } else {
                contentType = MediaType.APPLICATION_JSON_TYPE;
            }
            return Response.status(response.getStatus())
                    .entity(response.getData()) // cambio
                    .type(contentType) // cambio
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el informe").type(MediaType.TEXT_PLAIN).build();
        }
    }

    @GET
    @Path("/createAgendaReport/{doctorId}/{startDate}/{endDate}/{language}")
    @Operation(summary = "Create a Agenda Report", description = "Create a Agenda Report", tags = {
        "ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agenda Report created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Agenda Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createAgendaReport(@PathParam("doctorId") Long dId, @PathParam("startDate") String sDate,
            @PathParam("endDate") String eDate, @PathParam("language") String language) {
        try {
            MediaType contentType;
            System.out.println(sDate);
            System.out.println(eDate);
            ResponseWrapper response = reportService.createAgendaReport(dId, sDate, eDate, language);
            if (response.getData() instanceof byte[]) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            } else {
                contentType = MediaType.APPLICATION_JSON_TYPE;
            }
            return Response.status(response.getStatus())
                    .entity(response.getData()) // cambio
                    .type(contentType) // cambio
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el informe: " + e.toString()).type(MediaType.TEXT_PLAIN).build();
        }
    }
    
    @GET
    @Path("/createMedicalExamReport/{patientId}/{language}")
    @Operation(summary = "Create a Medical Exam Report", description = "Create a Medical Exam Report", tags = {
        "ReportController"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Medical Exam Report created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Medical Exam Report not found"),
        @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response createMedicalExamReport(@PathParam("patientId") Long patId, @PathParam("language") String language) {
        try {
            MediaType contentType;
            ResponseWrapper response = reportService.createMedicalExamReport(patId, language);
            if (response.getData() instanceof byte[]) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_TYPE;
            } else {
                contentType = MediaType.APPLICATION_JSON_TYPE;
            }
            return Response.status(response.getStatus())
                    .entity(response.getData()) // cambio
                    .type(contentType) // cambio
                    .build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al generar el informe: " + e.toString()).type(MediaType.TEXT_PLAIN).build();
        }
    }
}
