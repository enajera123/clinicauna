package cr.ac.una.clinicaunaws.security;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import jakarta.ws.rs.NameBinding;

/**
 * 
 * @author arayaroma
 */
@NameBinding
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface Secure {
}
