package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.MedicalExamDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author estebannajera
 */
public class MedicalExamService {

    public ResponseWrapper getMedicalExams() {
        try {
            Request request = new Request("MedicalExamController/medicalExams");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<MedicalExamDto> medicalExamDtos = (List<MedicalExamDto>) request
                    .readEntity(new GenericType<List<MedicalExamDto>>() {
                    });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Exam retrieved successfully: ",
                    medicalExamDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper createMedicalExam(MedicalExamDto medicalExamDto) {
        try {
            Request request = new Request("MedicalExamController/create");
            request.post(medicalExamDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            medicalExamDto = (MedicalExamDto) request.readEntity(MedicalExamDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Exam created successfully: ",
                    medicalExamDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper updateMedicalExam(MedicalExamDto medicalExamDto) {
        try {
            Request request = new Request("MedicalExamController/update");
            request.put(medicalExamDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            medicalExamDto = (MedicalExamDto) request.readEntity(MedicalExamDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Exam updated successfully: ",
                    medicalExamDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteMedicalExam(Long id) {
        try {
            Map<String, Object> params = new HashMap();
            params.put("id", id);
            Request request = new Request("MedicalExamController/delete", "{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Exam removed successfully: ",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getMedicalExamById(Long id) {
        try {
            Map<String, Object> params = new HashMap();
            params.put("id", id);
            Request request = new Request("MedicalExamController/medicalExam", "{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            MedicalExamDto medicalExamDto = (MedicalExamDto) request.readEntity(MedicalExamDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Exam retrieved successfully: ",
                    medicalExamDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

}
