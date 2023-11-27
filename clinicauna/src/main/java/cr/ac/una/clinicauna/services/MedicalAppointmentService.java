package cr.ac.una.clinicauna.services;

import java.util.HashMap;
import java.util.List;
import cr.ac.una.clinicauna.model.MedicalAppointmentDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;

public class MedicalAppointmentService {

    public ResponseWrapper getMedicalAppointments() {
        try {
            Request request = new Request("MedicalAppointmentController/medicalAppointments");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<MedicalAppointmentDto> medicalAppointmentDtos = (List<MedicalAppointmentDto>) request
                    .readEntity(new GenericType<List<MedicalAppointmentDto>>() {
                    });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "MedicalAppointments retrieved successfully: ",
                    medicalAppointmentDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }

    }

    public ResponseWrapper createMedicalAppointments(MedicalAppointmentDto medicalAppointmentDto) {
        try {
            Request request = new Request("MedicalAppointmentController/create");
            request.post(medicalAppointmentDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            MedicalAppointmentDto medicalAppointment = (MedicalAppointmentDto) request
                    .readEntity(MedicalAppointmentDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Appointment created successfully: ",
                    medicalAppointment);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updateMedicalAppointments(MedicalAppointmentDto medicalAppointmentDto) {
        try {
            Request request = new Request("MedicalAppointmentController/update");
            request.put(medicalAppointmentDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            MedicalAppointmentDto medicalAppointment = (MedicalAppointmentDto) request
                    .readEntity(MedicalAppointmentDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "Medical Appointment updated successfully: ",
                    medicalAppointment);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteMedicalAppointments(MedicalAppointmentDto medicalAppointmentDto) {
        try {
            HashMap map = new HashMap<>();
            map.put("id", medicalAppointmentDto.getId());
            Request request = new Request("MedicalAppointmentController/delete", "{id}", map);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK,
                    "MedicalAppointment deleted successfully: ",
                    null);
        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

}
