package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.util.ResponseWrapper;
import cr.ac.una.clinicauna.model.DoctorDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import jakarta.ws.rs.core.GenericType;

import java.util.HashMap;
import java.util.List;

public class DoctorService {

    public ResponseWrapper getDoctors() {
        try {
            Request request = new Request("DoctorController/doctors");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                                + request.getError(),
                        null);
            }
            List<DoctorDto> userDtos = (List<DoctorDto>) request.readEntity(new GenericType<List<DoctorDto>>() {
            });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Doctors retrieved successfully: ",
                    userDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }

    }

    public ResponseWrapper createDoctor(DoctorDto doctorDto) {
        try {
            Request request = new Request("DoctorController/create");
            request.post(doctorDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                                + request.getError(),
                        null);
            }
            DoctorDto doctor = (DoctorDto) request.readEntity(DoctorDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Doctor created successfully: ",
                    doctor);

        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper updateDoctor(DoctorDto doctorDto) {
        try {
            Request request = new Request("DoctorController/update");
            request.put(doctorDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                                + request.getError(),
                        null);
            }
            DoctorDto doctor = (DoctorDto) request.readEntity(DoctorDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Doctor updated successfully: ",
                    doctor);
        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteDoctor(Long id) {
        try {
            HashMap map = new HashMap<>();
            map.put("id", id);
            Request request = new Request("DoctorController/delete","{id}",map);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                                + request.getError(),
                        null);
            }
            DoctorDto doctor = (DoctorDto) request.readEntity(DoctorDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Doctor deleted successfully: ",
                    doctor);
        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }

    public ResponseWrapper getDoctorById(Long id) {
        try {
            HashMap map = new HashMap<>();
            map.put("id", id);
            Request request = new Request("DoctorController/doctor", "{id}", map);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                                + request.getError(),
                        null);
            }
            DoctorDto doctor = (DoctorDto) request.readEntity(DoctorDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Doctor retrieved successfully: ",
                    doctor);
        } catch (Exception e) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + e.toString(),
                    null);
        }
    }
}
