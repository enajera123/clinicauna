package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.AgendaDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaService {

    public ResponseWrapper getAgendas() {
        try {
            Request request = new Request("AgendaController/agendas");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<AgendaDto> agendaDtos = (List<AgendaDto>) request.readEntity(new GenericType<List<AgendaDto>>() {
            });
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Agendas retrieved successfully: ",
                    agendaDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }

    }

    public ResponseWrapper createAgenda(AgendaDto agendaDto) {
        try {
            Request request = new Request("AgendaController/create");
            request.post(agendaDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            agendaDto = (AgendaDto) request.readEntity(AgendaDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Agenda created successfully: ",
                    agendaDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper updateAgenda(AgendaDto agendaDto) {
        try {
            Request request = new Request("AgendaController/update");
            request.post(agendaDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            agendaDto = (AgendaDto) request.readEntity(AgendaDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Agenda updated successfully: ",
                    agendaDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper deleteAgenda(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("AgendaController/delete", "/{id}", params);
            request.delete();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }

            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Agenda deleted successfully: ",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getAgendaById(Long id) {
        try {
            Map params = new HashMap();
            params.put("id", id);
            Request request = new Request("AgendaController/agenda", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            AgendaDto agendaDto = (AgendaDto) request.readEntity(AgendaDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "Agenda retrieved successfully: ",
                    agendaDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }
}
