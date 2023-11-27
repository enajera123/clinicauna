package cr.ac.una.clinicaunaws.util;

/**
 * 
 * @author arayaroma
 */
public enum ResponseCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    MOVED_PERMANENTLY(301),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405),
    PROXY_AUTHENTICATION_REQUIRED(407),
    REQUEST_TIMEOUT(408),
    CONFLICT(409),
    IM_A_TEAPOT(418),
    UNPROCESSABLE_CONTENT(422),
    TOO_MANY_REQUESTS(429),
    INTERNAL_SERVER_ERROR(500),
    NOT_IMPLEMENTED(501),
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503);

    private Integer code;

    ResponseCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
