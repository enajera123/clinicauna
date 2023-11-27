package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.GeneralInformationDto;
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
public class GeneralInformationService {

    public ResponseWrapper createGeneralInformation(GeneralInformationDto generalInformationDto) {
        try {
            Request request = new Request("GeneralInformationController/create");
            request.post(generalInformationDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            generalInformationDto = (GeneralInformationDto) request.readEntity(GeneralInformationDto.class);

            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "General Information created successfully: ",
                    generalInformationDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper updateGeneralInformation(GeneralInformationDto generalInformationDto) {
        try {
            Request request = new Request("GeneralInformationController/update");
            request.put(generalInformationDto);
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            generalInformationDto = (GeneralInformationDto) request.readEntity(GeneralInformationDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "General Information updated successfully: ",
                    generalInformationDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getGeneralInformationById(Long id) {
        try {
            Map<String, Object> params = new HashMap();
            params.put("id", id);
            Request request = new Request("GeneralInformationController/generalInformation", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            GeneralInformationDto generalInformationDto = (GeneralInformationDto) request.readEntity(GeneralInformationDto.class);
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "General Information retieved successfully: ",
                    generalInformationDto);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }

    public ResponseWrapper getAllGeneralInformation() {
        try {

            Request request = new Request("GeneralInformationController/generalInformation");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR, "Error in the request: "
                        + request.getError(),
                        null);
            }
            List<GeneralInformationDto> generalInformationDtos = (List<GeneralInformationDto>) request.readEntity(new GenericType<List<GeneralInformationDto>>() {});
            return new ResponseWrapper(ResponseCode.OK.getCode(), ResponseCode.OK, "General Information retieved successfully: ",
                    generalInformationDtos);
        } catch (Exception ex) {
            return new ResponseWrapper(ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR, "Error in the service: " + ex.toString(),
                    null);
        }
    }
}
