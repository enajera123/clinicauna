package cr.ac.una.clinicauna.services;

import java.util.HashMap;
import java.util.List;
import cr.ac.una.clinicauna.model.ReportRecipientsDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.io.IOException;

/**
 *
 * @author arayaroma
 */
public class ReportRecipientsService {

    public ResponseWrapper createReportRecipients(ReportRecipientsDto reportRecipientsDto) {
        try {
            Request request = new Request("ReportRecipientsController/create");
            request.post(reportRecipientsDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            reportRecipientsDto = (ReportRecipientsDto) request.readEntity(ReportRecipientsDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportRecipients created successfully: ",
                    reportRecipientsDto);
        } catch (IOException e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper getReportRecipientsById(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportRecipientsController/reportRecipients", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            ReportRecipientsDto reportRecipients = (ReportRecipientsDto) request.readEntity(ReportRecipientsDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportRecipients retrieved successfully: ",
                    reportRecipients);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReportRecipients() {
        try {
            Request request = new Request("ReportRecipientsController/reportRecipients");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            List<ReportRecipientsDto> reportRecipients = (List<ReportRecipientsDto>) request
                    .readEntity(new GenericType<List<ReportRecipientsDto>>() {
                    });
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportRecipients retrieved successfully: ",
                    reportRecipients);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updateReportRecipients(ReportRecipientsDto reportRecipientsDto) {
        try {
            Request request = new Request("ReportRecipientsController/update");
            request.put(reportRecipientsDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            reportRecipientsDto = (ReportRecipientsDto) request.readEntity(ReportRecipientsDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportRecipients updated successfully: ",
                    reportRecipientsDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteReportRecipients(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportRecipientsController/delete", "/{id}", params);
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
                    "ReportRecipients deleted successfully: ",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

}
