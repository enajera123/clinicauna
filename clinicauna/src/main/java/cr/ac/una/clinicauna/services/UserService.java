package cr.ac.una.clinicauna.services;

import cr.ac.una.clinicauna.model.UserDto;
import cr.ac.una.clinicauna.util.Request;
import cr.ac.una.clinicauna.util.ResponseCode;
import cr.ac.una.clinicauna.util.ResponseWrapper;
import jakarta.ws.rs.core.GenericType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author estebannajera
 * @author arayaroma
 */
public class UserService {

    public ResponseWrapper createUser(UserDto userDto) {
        try {
            Request request = new Request("UserController/create");
            request.post(userDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            userDto = (UserDto) request.readEntity(UserDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User created successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper findUserById(Long id) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            Request request = new Request("UserController/user", "/{id}", params);
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            UserDto userDto = (UserDto) request.readEntity(UserDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User retrieved successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    @SuppressWarnings("unchecked")
    public ResponseWrapper getUsers() {
        try {
            Request request = new Request("UserController/users");
            request.get();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            List<UserDto> userDtos = (List<UserDto>) request.readEntity(new GenericType<List<UserDto>>() {
            });
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Users retrieved successfully: ",
                    userDtos);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper updateUser(UserDto userDto) {
        try {
            Request request = new Request("UserController/update");
            request.put(userDto);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            userDto = (UserDto) request.readEntity(UserDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User updated successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper deleteUser(UserDto userDto) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", userDto.getId());
            Request request = new Request("UserController/delete", "/{id}", params);
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
                    ResponseCode.OK, "User removed successfully: ",
                    null);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper changePassword(Long id, String oldPassword, String newPassword) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("id", id);
            params.put("oldPassword", oldPassword);
            params.put("newPassword", newPassword);
            Request request = new Request("UserController/changePassword", "/{id}/{oldPassword}/{newPassword}", params);
            request.put(params);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            UserDto userDto = (UserDto) request.readEntity(UserDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User retrieved successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper recoverPassword(String email) {
        try {
            Request request = new Request("UserController/recoverPassword");
            request.post(email);
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            UserDto userDto = (UserDto) request.readEntity(UserDto.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User retrieved successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error in the service: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper verifyUser(String user, String password) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("username", user);
            params.put("password", password);
            Request request = new Request("UserController/user", "/{username}/{password}", params);
            request.getToken();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            UserDto userDto = (UserDto) request.readEntity(UserDto.class);
            System.out.println(userDto.getToken());
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User verified successfully: ",
                    userDto);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error verifying the user: " + ex.getMessage(),
                    null);
        }
    }

    public ResponseWrapper renewToken() {
        try {
            Request request = new Request("UserController/renewToken");
            request.getRenewal();
            if (request.isError()) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Error in the request: " + request.getError(),
                        null);
            }
            String token = (String) request.readEntity(String.class);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Token renewed successfully: ",
                    token);
        } catch (IOException ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Error renewing the token: " + ex.getMessage(),
                    null);
        }
    }

}
