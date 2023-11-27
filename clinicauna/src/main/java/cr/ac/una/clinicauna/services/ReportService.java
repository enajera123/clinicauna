package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.io.File;
import java.util.HashMap;
import java.awt.Desktop;
import java.util.List;
import cr.ac.una.clinicauna.model.ReportDto;
import cr.ac.una.clinicauna.util.FileLoader;
import jakarta.ws.rs.core.GenericType;
import java.io.IOException;

/**
 *
 * @author vargas
 * @author arayaroma
 */
public class ReportService {

    public ResponseWrapper createReport(ReportDto reportDto) {
        try {
            Request request = new Request("ReportController/create");
            request.post(reportDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            reportDto = (ReportDto) request.readEntity(ReportDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report created successfully: ",
                    reportDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getReport(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportController/report", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            ReportDto report = (ReportDto) request.readEntity(ReportDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report retrieved successfully: ",
                    report);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(),
                    null);
        }
    }

    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReports() {
        try {
            Request request = new Request("ReportController/report");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            List<ReportDto> reportDtos = (List<ReportDto>) request.readEntity(new GenericType<List<ReportDto>>() {
            });
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Reports retrieved successfully: ",
                    reportDtos);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper updateReport(ReportDto reportDto) {
        try {
            Request request = new Request("ReportController/update");
            request.put(reportDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            reportDto = (ReportDto) request.readEntity(ReportDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report updated successfully: ",
                    reportDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteReport(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportController/delete", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Report deleted successfully: ",
                    null);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper createPatientReport(Long id, String language) {
        try {
            String path = FileLoader.chooseSavePath();
            if (path == null || path.isBlank()) {
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Path is required",
                        null);
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("language", language);
            Request request = new Request("ReportController/createPatientReport", "/{id}/{language}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: " + request.getError(), null);
            }
            byte[] pdf = (byte[]) request.readEntity(byte[].class);
            if (FileLoader.createFile(path, pdf)) {
                Desktop.getDesktop().open(new File(path));
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Report created successfully: ",
                        pdf);
            }
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error creating the PDF",
                    null);
        } catch (IOException ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(), null);
        }
    }

    public ResponseWrapper createMedicalExamReport(Long patientId, String language) {
        try {
            String path = FileLoader.chooseSavePath();
            if (path == null || path.isBlank()) {
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Path is required",
                        null);
            }
            HashMap<String, Object> params = new HashMap<>();
            params.put("patientId", patientId);
            params.put("language", language);
            Request request = new Request("ReportController/createMedicalExamReport", "/{patientId}/{language}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: " + request.getError(), null);
            }
            byte[] pdf = (byte[]) request.readEntity(byte[].class);
            if (FileLoader.createFile(path, pdf)) {
                Desktop.getDesktop().open(new File(path));
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Report created successfully: ",
                        pdf);
            }
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error creating the PDF",
                    null);
        } catch (IOException ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(), null);
        }
    }

    public ResponseWrapper createAgendaReport(Long doctorId, String sDate, String eDate, String language) {
        try {
            String path = FileLoader.chooseSavePath();
            if (path == null || path.isBlank()) {
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Path is required",
                        null);
            }
            HashMap params = new HashMap();
            params.put("doctorId", doctorId);
            params.put("startDate", sDate);
            params.put("endDate", eDate);
            params.put("language", language);
            Request request = new Request("ReportController/createAgendaReport", "/{doctorId}/{startDate}/{endDate}/{language}",
                    params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: " + request.getError(), null);
            }
            byte[] pdf = (byte[]) request.readEntity(byte[].class);
            if (FileLoader.createFile(path, pdf)) {
                Desktop.getDesktop().open(new File(path));
                return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Report created successfully: ",
                        pdf);
            }
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error creating the PDF",
                    null);
        } catch (IOException ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.toString(), null);
        }
    }

}
