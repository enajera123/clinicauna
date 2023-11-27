package cr.ac.una.clinicaunaws.services;

import jakarta.ejb.EJB;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import cr.ac.una.clinicaunaws.util.ResponseCode;
import cr.ac.una.clinicaunaws.util.ResponseWrapper;
import static cr.ac.una.clinicaunaws.util.PersistenceContext.PERSISTENCE_UNIT_NAME;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import cr.ac.una.clinicaunaws.dto.UserDto;
import cr.ac.una.clinicaunaws.entities.User;
import cr.ac.una.clinicaunaws.security.HashGenerator;

/**
 *
 * @author arayaroma
 */
@Stateless
@LocalBean
public class UserService {

    @PersistenceContext(unitName = PERSISTENCE_UNIT_NAME)
    private EntityManager em;

    @EJB
    EmailService emailService;

    /**
     * TODO: Test this method
     * 
     * @param userDto to be created
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    private void EncryptPassword(UserDto userDto) {
        userDto.setPassword(HashGenerator.generateHash(userDto.getPassword(),
                HashGenerator.HashAlgorithm.SHA256.getAlgorithm()));
    }

    /**
     * FIXME: Encrypt password with SHA256 to save it in database
     * 
     * @param userDto to be created
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    public ResponseWrapper createUser(UserDto userDto) {
        try {
            User user = new User(userDto);
            try {
                user.setActivationCode(generateHash(userDto));
                userDto.setActivationCode(user.getActivationCode());
                sendActivationEmail(userDto);
            } catch (Exception ex) {
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "User created successfully, but email could not be sent: " + ex.getMessage(),
                        userDto);
            }
            em.persist(user);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User created successfully.",
                    new UserDto(user));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while creating user: " + ex.getMessage(),
                    null);
        }
    }

    private String generateHash(UserDto userDto) {
        return HashGenerator.generateHash(userDto.getUsername(),
                HashGenerator.HashAlgorithm.SHA256.getAlgorithm());
    }

    private ResponseWrapper sendActivationEmail(UserDto userDto) {
        try {
            emailService.sendActivationHashLink(userDto);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User created successfully.",
                    userDto);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User created successfully, but email could not be sent: " + ex.getMessage(),
                    userDto);
        }
    }

    /**
     * @param hash hash to activate user
     * @return ResponseWrapper with the response from database, or null if an
     */
    @Transactional
    public ResponseWrapper activateUser(String hash) {
        try {
            User user;
            Query query = em.createNamedQuery("User.findByActivationCode", User.class)
                    .setParameter("activationCode", hash);
            user = (User) query.getSingleResult();
            System.out.println(user.getActivationCode());
            if (isUserNull(user)) {
                return handleUserNull();
            }
            UserDto userDto = new UserDto(user);
            if (HashGenerator.validateHash(userDto.getActivationCode(), hash)) {
                em.createNativeQuery("CALL CLINICAUNA.ACTIVATE_USER(?id)")
                        .setParameter("id", user.getId())
                        .executeUpdate();

                em.merge(user);
                em.flush();

                emailService.sendUserActivated(userDto);
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "User activated successfully.",
                        userDto);
            }
            return new ResponseWrapper(
                    ResponseCode.UNAUTHORIZED.getCode(),
                    ResponseCode.UNAUTHORIZED,
                    "Invalid hash.",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while activating user: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param email user email to recover password
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    @Transactional
    public ResponseWrapper recoverPassword(String email) {
        try {
            User user;
            user = em.createNamedQuery("User.findByEmail", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (isUserNull(user)) {
                return handleUserNull();
            }
            String newPassword = generateRandomPassword(user.getId());
            user.setPasswordChanged("Y");
            em.merge(user);
            em.flush();

            UserDto userDto = new UserDto(user);
            userDto.setPassword(newPassword);
            emailService.sendPasswordRecovery(userDto);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Password recovery email sent successfully.",
                    userDto);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while recovering password: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id user id to generate random password
     * @return String with the new password queried from databse generated by
     *         the stored procedure
     */
    @Transactional
    private String generateRandomPassword(Long id) {
        StoredProcedureQuery query = em
                .createStoredProcedureQuery("CLINICAUNA.GENERATE_RANDOM_PASSWORD");
        query.registerStoredProcedureParameter("USER_ID", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NEW_PASSWORD", String.class, ParameterMode.OUT);
        query.setParameter("USER_ID", id);
        query.execute();

        return (String) query.getOutputParameterValue("NEW_PASSWORD");
    }

    /**
     * @param id          user id to change password
     * @param oldPassword old password to be verified
     * @param newPassword new password to be set
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    @Transactional
    public ResponseWrapper changePassword(Long id, String oldPassword, String newPassword) {
        if (isUserIdNull(id)) {
            return handleUserIdNull();
        }
        try {
            User user;
            user = em.createNamedQuery("User.findById", User.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (isUserNull(user)) {
                return handleUserNull();
            }
            if (user.getPassword().equals(oldPassword)) {

                String changedPassword = changedPassword(id, newPassword);
                user.setPasswordChanged("N");
                UserDto userDto = new UserDto(user);

                // emailService.sendPasswordRecovered(userDto);
                em.merge(user);
                em.flush();

                userDto.setPassword(changedPassword);
                userDto.setPasswordChanged("Y");
                return new ResponseWrapper(
                        ResponseCode.OK.getCode(),
                        ResponseCode.OK,
                        "Password changed successfully.",
                        userDto);
            }
            return new ResponseWrapper(
                    ResponseCode.UNAUTHORIZED.getCode(),
                    ResponseCode.UNAUTHORIZED,
                    "Invalid old password.",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while changing password: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param id          user id to change password
     * @param newPassword new password to be set
     * @return String with the new password queried from databse generated by
     *         the stored procedure
     */
    @Transactional
    private String changedPassword(Long id, String newPassword) {
        StoredProcedureQuery query = em
                .createStoredProcedureQuery("CLINICAUNA.CHANGE_PASSWORD");
        query.registerStoredProcedureParameter("USER_ID", Long.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("NEW_PASSWORD", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("CHANGED_PASSWORD", String.class, ParameterMode.OUT);
        query.setParameter("USER_ID", id);
        query.setParameter("NEW_PASSWORD", newPassword);
        query.execute();

        return (String) query.getOutputParameterValue("CHANGED_PASSWORD");
    }

    private boolean isUserIdNull(Long id) {
        return id == null || id <= 0;
    }

    private ResponseWrapper handleUserIdNull() {
        return new ResponseWrapper(
                ResponseCode.BAD_REQUEST.getCode(),
                ResponseCode.BAD_REQUEST,
                "Id can't be null.",
                null);
    }

    private boolean isUserNull(User user) {
        return user == null;
    }

    private ResponseWrapper handleUserNull() {
        return new ResponseWrapper(
                ResponseCode.NOT_FOUND.getCode(),
                ResponseCode.NOT_FOUND,
                "User not found.",
                null);
    }

    /**
     * @param id user id to be retrieved
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    public ResponseWrapper getUserById(Long id) {
        try {
            User user;
            user = em.find(User.class, id);
            if (user == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "User not found, id: " + id + ")",
                        null);
            }
            UserDto userDto = new UserDto(user);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User retrieved successfully.",
                    userDto.convertFromEntityToDTO(user, userDto));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving user: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @param username Username to match the user
     * @param password Password to match the user
     * @return
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getUserByUsernameAndPassword(String username, String password) {
        if (username == null || username.isEmpty()) {
            return new ResponseWrapper(
                    ResponseCode.BAD_REQUEST.getCode(),
                    ResponseCode.BAD_REQUEST,
                    "Username can't be null.",
                    null);
        }
        if (password == null || password.isEmpty()) {
            return new ResponseWrapper(
                    ResponseCode.BAD_REQUEST.getCode(),
                    ResponseCode.BAD_REQUEST,
                    "A password is required.",
                    null);
        }
        try {
            User user;
            Query query = em.createNamedQuery("User.findByUsernameAndPassword", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            List<User> results = query.getResultList();
            if (results.isEmpty()) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "Authentication failed, username: " + username + ")",
                        null);
            }
            user = results.get(0);
            if (user == null) {
                return new ResponseWrapper(
                        ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                        ResponseCode.INTERNAL_SERVER_ERROR,
                        "Retrieved user is null.",
                        null);
            }
            UserDto userDto = new UserDto(user);
            userDto = userDto.convertFromEntityToDTO(user, userDto);
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User retrieved successfully.",
                    userDto);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving user: " + ex.getMessage(),
                    null);
        }
    }

    /**
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    @SuppressWarnings("unchecked")
    public ResponseWrapper getUsers() {
        try {
            Query query = em.createNamedQuery("User.findAll", User.class);
            List<User> users = (List<User>) query.getResultList();
            List<UserDto> usersDto = new ArrayList<>();

            for (User user : users) {
                UserDto userDto = new UserDto(user);
                usersDto.add(userDto.convertFromEntityToDTO(user, userDto));
            }

            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "Users retrieved successfully.",
                    usersDto);
        } catch (Exception e) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while retrieving users: " + e.getMessage(),
                    null);
        }
    }

    /**
     * @param userDto User to be updated
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    public ResponseWrapper updateUser(UserDto userDto) {
        try {
            User user;
            user = em.createNamedQuery("User.findById", User.class)
                    .setParameter("id", userDto.getId())
                    .getSingleResult();
            if (user == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "User not found, id: " + userDto.getId() + ")",
                        null);
            }
            if (!Objects.equals(userDto.getUsername(), user.getUsername())) {
                if (!verifyUniqueUsername(userDto.getUsername())) {
                    return new ResponseWrapper(
                            ResponseCode.CONFLICT.getCode(),
                            ResponseCode.CONFLICT,
                            "Username already exists.",
                            null);
                }
            }
            user.updateUser(userDto);
            em.merge(user);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User updated successfully.",
                    new UserDto(user));
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while updating user: " + ex.getMessage(),
                    null);
        }
    }

    private boolean verifyUniqueUsername(String username) {
        return em.createNamedQuery("User.findByUsername", User.class)
                .setParameter("username", username)
                .getResultList().isEmpty();
    }

    /**
     * @param id id from user to be deleted
     * @return ResponseWrapper with the response from database, or null if an
     *         exception occurred
     */
    public ResponseWrapper deleteUserById(Long id) {
        try {
            User user;
            user = em.find(User.class, id);
            if (user == null) {
                return new ResponseWrapper(
                        ResponseCode.NOT_FOUND.getCode(),
                        ResponseCode.NOT_FOUND,
                        "User not found, id: " + id + ")",
                        null);
            }
            em.remove(user);
            em.flush();
            return new ResponseWrapper(
                    ResponseCode.OK.getCode(),
                    ResponseCode.OK,
                    "User deleted successfully.",
                    null);
        } catch (Exception ex) {
            return new ResponseWrapper(
                    ResponseCode.INTERNAL_SERVER_ERROR.getCode(),
                    ResponseCode.INTERNAL_SERVER_ERROR,
                    "Exception occurred while deleting user: " + ex.getMessage(),
                    null);
        }
    }

}
