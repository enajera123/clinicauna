package cr.ac.una.clinicauna.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * 
 * @author arayaroma
 */
public class ResponseWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer status;
    private ResponseCode code;
    private String message;
    private Object data;

    public ResponseWrapper() {
    }

    public ResponseWrapper(Integer status, ResponseCode code, String message,
            Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Integer getStatus() {
        return this.status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ResponseCode getCode() {
        return this.code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof ResponseWrapper)) {
            return false;
        }
        ResponseWrapper responseWrapper = (ResponseWrapper) o;
        return Objects.equals(status, responseWrapper.status) && Objects.equals(code, responseWrapper.code)
                && Objects.equals(message, responseWrapper.message) && Objects.equals(data, responseWrapper.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, message, data);
    }

    @Override
    public String toString() {
        return "{" +
                " status='" + getStatus() + "'" +
                ", code='" + getCode() + "'" +
                ", message='" + getMessage() + "'" +
                ", data='" + getData() + "'" +
                "}";
    }

}
