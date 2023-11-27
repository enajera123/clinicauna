package cr.ac.una.clinicaunaws.util;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author arayaroma
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer status;
    private ResponseCode code;
    private String message;
    private Object data;
}
