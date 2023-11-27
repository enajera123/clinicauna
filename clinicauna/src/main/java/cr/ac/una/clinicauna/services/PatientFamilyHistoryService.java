package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.PatientFamilyHistoryDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author estebannajera
 */
public class PatientFamilyHistoryService {

    public ResponseWrapper getPatientFamilyHistoryById(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("PatientFamilyHistoryController/patientFamilyHistory", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientFamilyHistoryDto patientFamilyHistoryDto = (PatientFamilyHistoryDto) request.readEntity(PatientFamilyHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Family history removed successfully: ",
                    patientFamilyHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updatePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto) {
        try {
            Request request = new Request("PatientFamilyHistoryController/update");
            request.put(patientFamilyHistoryDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientFamilyHistoryDto = (PatientFamilyHistoryDto) request.readEntity(PatientFamilyHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Family history updated successfully: ",
                    patientFamilyHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deletePatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto) {
        try {
            Map params = new HashMap();
            params.put("id", patientFamilyHistoryDto.getId());
            Request request = new Request("PatientFamilyHistoryController/delete", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Family history removed successfully: ",
                    null);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }

    }

    public ResponseWrapper createPatientFamilyHistory(PatientFamilyHistoryDto patientFamilyHistoryDto) {
        try {
            Request request = new Request("PatientFamilyHistoryController/create");
            request.post(patientFamilyHistoryDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientFamilyHistoryDto = (PatientFamilyHistoryDto) request.readEntity(PatientFamilyHistoryDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Family history created successfully: ",
                    patientFamilyHistoryDto);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

}
