package cr.ac.una.clinicaunaws.controller;

import java.util.logging.Logger;
import cr.ac.una.clinicaunaws.dto.UserDto;
import cr.ac.una.clinicaunaws.security.JwtManager;
import cr.ac.una.clinicaunaws.security.Secure;
import cr.ac.una.clinicaunaws.services.UserService;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
import java.util.List;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 *
 * @author arayaroma
 */

@Path("/UserController")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityRequirement(name = "jwt-auth")
@Tag(name = "UserController", description = "Manage endpoints related to the User.")
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Context
    SecurityContext securityContext;

    @EJB
    UserService userService;

    /**
     * Create a new user
     *
     * @param userDto to be created
     * @return Response with the created user
     */
    @Secure
    @POST
    @Path("/create")
    @Operation(summary = "Create a new user", description = "Create a new user", tags = { "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response createUser(@Valid UserDto userDto) {
        try {
            ResponseWrapper response = userService.createUser(userDto);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Get a user by id
     *
     * @param id to be fetched
     * @return Response with the user
     */
    @Secure
    @GET
    @Path("/user/{id}")
    @Operation(summary = "Get a user by id", description = "Get a user by id", tags = { "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response getUserById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = userService.getUserById(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }

            UserDto user = (UserDto) response.getData();
            user.setToken(JwtManager.getInstance().generatePrivateKey(user.getPassword()));

            return Response.ok(user).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Get a user by username and password
     *
     * @param username to be fetched
     * @param password to be fetched
     * @return Response with the user
     */
    @GET
    @Path("/user/{username}/{password}")
    @Operation(summary = "Get a user by username and password", description = "Get a user by username and password", tags = {
            "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response getUserByUsernameAndPassword(
            @PathParam("username") String username,
            @PathParam("password") String password) {
        try {
            ResponseWrapper response = userService.getUserByUsernameAndPassword(username, password);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }

            UserDto user = (UserDto) response.getData();
            user.setToken(JwtManager.getInstance().generatePrivateKey(user.getPassword()));
            System.out.println(user);
            return Response.ok(user).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Get all the users
     *
     * @return Response with the list of users
     */
    @Secure
    @GET
    @Path("/users")
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get all the users", description = "Get all the users", tags = { "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Users not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response getUsers() {
        try {
            ResponseWrapper response = userService.getUsers();
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(new GenericEntity<List<UserDto>>((List<UserDto>) response.getData()) {
            }).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Update a user
     *
     * @param userDto to be updated
     * @return Response with the updated user
     */
    @Secure
    @PUT
    @Path("/update")
    @Operation(summary = "Update a user", description = "Update a user", tags = { "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response updateUser(@Valid UserDto userDto) {
        try {
            ResponseWrapper response = userService.updateUser(userDto);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * delete a user by id
     *
     * @param id to be deleted
     * @return Response with the deleted user
     */
    @Secure
    @DELETE
    @Path("/delete/{id}")
    @Operation(summary = "Delete a user by id", description = "Delete a user by id", tags = { "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response deleteUserById(@PathParam("id") Long id) {
        try {
            ResponseWrapper response = userService.deleteUserById(id);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Activate a user by hash
     *
     * @param hash of the user
     * @return Response with the activated user
     */
    @POST
    @Path("/activate/{hash}")
    @Operation(summary = "Activate a user by hash", description = "Activate a user by hash", tags = {
            "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response activateUser(@PathParam("hash") String hash) {
        try {
            ResponseWrapper response = userService.activateUser(hash);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Recover the password of a user by email
     *
     * @param email of the user
     * @return Response with the user with the new password
     */
    @POST
    @Path("/recoverPassword")
    @Operation(summary = "Recover the password of a user by email", description = "Recover the password of a user by email", tags = {
            "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password recovered"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response recoverPassword(String email) {
        try {
            ResponseWrapper response = userService.recoverPassword(email);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Change the password of a user
     *
     * @param id          of the user
     * @param oldPassword to be changed
     * @param newPassword to be set
     * @return Response with the updated user
     */
    @Secure
    @PUT
    @Path("/changePassword/{id}/{oldPassword}/{newPassword}")
    @Operation(summary = "Change the password of a user", description = "Change the password of a user", tags = {
            "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response changePassword(
            @PathParam("id") Long id,
            @PathParam("oldPassword") String oldPassword,
            @PathParam("newPassword") String newPassword) {
        try {
            ResponseWrapper response = userService.changePassword(id, oldPassword, newPassword);
            if (response.getCode() != ResponseCode.OK) {
                return Response.status(response.getStatus()).entity(response.getMessage()).build();
            }
            return Response.ok(response.getStatus()).entity(response.getData()).build();
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    /**
     * Renew the token of a user
     * 
     * @return Response with the new token
     */
    @GET
    @Path("/renewToken")
    @Operation(summary = "Renew the token of a user", description = "Renew the token of a user", tags = {
            "UserController" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token renewed"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public Response renewToken() {
        try {
            String userRequest = securityContext.getUserPrincipal().getName();
            if (userRequest != null && !userRequest.isEmpty()) {
                return Response.ok(JwtManager.getInstance().generatePrivateKey(userRequest)).build();
            } else {
                return Response.status(ResponseCode.UNAUTHORIZED.getCode()).entity("Can't renew token").build();
            }
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return Response.status(ResponseCode.UNAUTHORIZED.getCode()).entity("Can't renew token").build();
        }
    }

}
