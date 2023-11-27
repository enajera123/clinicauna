package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.PatientCareDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientCareService {

    public ResponseWrapper getPatientCares() {
        try {
            Request request = new Request("PatientCareController/patientCare");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<PatientCareDto> patientCareDtos = (List<PatientCareDto>) request
                    .readEntity(new GenericType<List<PatientCareDto>>() {
                    });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Patient Care retrieved successfully: ",
                    patientCareDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }

    }

    public ResponseWrapper createPatientCare(PatientCareDto patientCareDto) {
        try {
            Request request = new Request("PatientCareController/create");
            request.post(patientCareDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientCareDto = (PatientCareDto) request
                    .readEntity(PatientCareDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Patient Care created successfully: ",
                    patientCareDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper updatePatientCare(PatientCareDto patientCareDto) {
        try {
            Request request = new Request("PatientCareController/update");
            request.put(patientCareDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            patientCareDto = (PatientCareDto) request
                    .readEntity(PatientCareDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Patient Care updated successfully: ",
                    patientCareDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper deletePatientCare(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("PatientCareController/delete", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Patient Care deleted successfully: ",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getPatientCareById(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("PatientCareController/patientCare", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientCareDto patientCareDto = (PatientCareDto) request
                    .readEntity(PatientCareDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Patient Care retrieved successfully: ",
                    patientCareDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }

    }
}


/*
 {
  "id": 2,
  "patientCareDate": "2023-10-13 23:46:02.000",
  "patientHistory": {
    "id": 1,
  },
  "bloodPressure": "string",
  "heartRate": "string",
  "weight": "string",
  "height": "string",
  "temperature": "string",
  "bodyMassIndex": "string",
  "nursingNotes": "string",
  "reason": "string",
  "carePlan": "string",
  "observations": "string",
  "physicalExam": "string",
  "treatment": "string",
   
  "version": 0
}
 */