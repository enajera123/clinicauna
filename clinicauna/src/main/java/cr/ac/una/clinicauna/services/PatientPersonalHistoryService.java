package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.PatientPersonalHistoryDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author estebannajera
 */
public class PatientPersonalHistoryService {

    public ResponseWrapper getPatientPersonalHistoryById(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("PatientPersonalHistoryController/patientPersonalHistory", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientPersonalHistoryDto patientPersonalHistoryDto = (PatientPersonalHistoryDto) request.readEntity(PatientPersonalHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Personal history removed successfully: ",
                    patientPersonalHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updatePatientPersonalHistory(PatientPersonalHistoryDto patientPersonalHistoryDto) {
        try {
            Request request = new Request("PatientPersonalHistoryController/update");
            request.put(patientPersonalHistoryDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientPersonalHistoryDto = (PatientPersonalHistoryDto) request.readEntity(PatientPersonalHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Personal history updated successfully: ",
                    patientPersonalHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deletePatientPersonalHistory(PatientPersonalHistoryDto patientPersonalHistoryDto) {
        try {
            Map params = new HashMap();
            params.put("id", patientPersonalHistoryDto.getId());
            Request request = new Request("PatientPersonalHistoryController/delete", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientPersonalHistoryDto = (PatientPersonalHistoryDto) request.readEntity(PatientPersonalHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Personal history removed successfully: ",
                    patientPersonalHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }

    }

    public ResponseWrapper createPatientPersonalHistory(PatientPersonalHistoryDto patientPersonalHistoryDto) {
        try {
            Request request = new Request("PatientPersonalHistoryController/create");
            request.post(patientPersonalHistoryDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientPersonalHistoryDto = (PatientPersonalHistoryDto) request.readEntity(PatientPersonalHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Personal history created successfully: ",
                    patientPersonalHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

}
