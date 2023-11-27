package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.util.ResponseWrapper;

import java.util.HashMap;

import cr.ac.una.clinicauna.model.PatientDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import jakarta.ws.rs.core.GenericType;
import java.util.List;

public class PatientService {

    public ResponseWrapper createPatient(PatientDto patientDto) {
        try {
            Request request = new Request("PatientController/create");
            request.post(patientDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientDto patient = (PatientDto) request.readEntity(PatientDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Patient created successfully: ",
                    patient);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updatePatient(PatientDto patientDto) {
        try {
            Request request = new Request("PatientController/update");
            request.put(patientDto);
            if (request.isError()) {
                System.out.println(request.getError());
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientDto patient = (PatientDto) request.readEntity(PatientDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Patient updated successfully: ",
                    patient);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper getPatients() {
        try {
            Request request = new Request("PatientController/patients");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<PatientDto> patients = (List<PatientDto>) request.readEntity(new GenericType<List<PatientDto>>() {
            });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Patient retrieved successfully: ",
                    patients);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper getPatientById(Long id) {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);
            Request request = new Request("PatientController/patient", "{id}", map);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientDto patient = (PatientDto) request.readEntity(PatientDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Patient retrieved successfully: ",
                    patient);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deletePatient(PatientDto patientDto) {
        try {
            HashMap map = new HashMap<>();
            map.put("id", patientDto.getId());
            Request request = new Request("PatientController/delete", "{id}", map);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            PatientDto patient = (PatientDto) request.readEntity(PatientDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Patient deleted successfully: ",
                    patient);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

}
