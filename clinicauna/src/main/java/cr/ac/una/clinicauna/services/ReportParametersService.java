package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import cr.ac.una.clinicauna.model.ReportParametersDto;

/**
 *
 * @author arayaroma
 */
public class ReportParametersService {

    public ResponseWrapper createReportParameters(ReportParametersDto reportParametersDto) {
        try {
            Request request = new Request("ReportParametersController/create");
            request.post(reportParametersDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            ReportParametersDto reportParameters = (ReportParametersDto) request.readEntity(ReportParametersDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters created successfully: ",
                    reportParameters);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper getReportParametersById(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportParametersController/reportParameters", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            ReportParametersDto reportParameters = (ReportParametersDto) request.readEntity(ReportParametersDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters retrieved successfully: ",
                    reportParameters);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    @SuppressWarnings("unchecked")
    public ResponseWrapper getAllReportParameters() {
        try {
            Request request = new Request("ReportParametersController/reportParameters");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            List<ReportParametersDto> reportParameters = (List<ReportParametersDto>) request
                    .readEntity(new GenericType<List<ReportParametersDto>>() {
                    });
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters retrieved successfully: ",
                    reportParameters);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updateReportParameters(ReportParametersDto reportParametersDto) {
        try {
            Request request = new Request("ReportParametersController/update");
            request.put(reportParametersDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            reportParametersDto = (ReportParametersDto) request.readEntity(ReportParametersDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "ReportParameters updated successfully: ",
                    reportParametersDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteReportParameters(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("ReportParametersController/delete", "/{id}", params);
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
                    "ReportParameters deleted successfully: ",
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
