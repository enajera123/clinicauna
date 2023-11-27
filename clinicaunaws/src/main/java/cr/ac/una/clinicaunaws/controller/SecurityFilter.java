package cr.ac.una.clinicaunaws.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;

import cr.ac.una.clinicaunaws.security.JwtManager;
import cr.ac.una.clinicaunaws.security.Secure;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

/**
 * 
 * @author arayaroma
 */
@Secure
@Provider
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
    private static final String AUTHENTICATION_SCHEME = "Bearer";

    /**
     * Services methods names that are not filtered
     */
    private static final String AUTHORIZATION_SERVICE_PATH = "getUserByUsernameAndPassword";
    private static final String GENERAL_INFORMATION_PATH = "getAllGeneralInformation";

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        Method method = resourceInfo.getResourceMethod();

        if (method.getName().equals(AUTHORIZATION_SERVICE_PATH) ||
                method.getName().equals(GENERAL_INFORMATION_PATH)) {
            return;
        }
        String authorizationHeader = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            abortWithUnauthorized(request, "Authorization header must be provided");
            return;
        } else if (!isTokenBasedAuthentication(authorizationHeader)) {
            abortWithUnauthorized(request, "Invalid authorization header format");
            return;
        }
        String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
        try {
            try {
                Claims claims = JwtManager.getInstance().claimKey(token);
                if (method.getName().equals(AUTHORIZATION_SERVICE_PATH)
                        || method.getName().equals(GENERAL_INFORMATION_PATH)) {
                    if (!(boolean) claims.getOrDefault("rnw", false)) {
                        abortWithUnauthorized(request, "Invalid authorization");
                    }
                }

                final SecurityContext currentSecurityContext = request.getSecurityContext();
                request.setSecurityContext(new SecurityContext() {

                    @Override
                    public Principal getUserPrincipal() {
                        return () -> claims.getSubject();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        return currentSecurityContext.isSecure();
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return AUTHENTICATION_SCHEME;
                    }
                });
            } catch (ExpiredJwtException | MalformedJwtException e) {
                if (e instanceof ExpiredJwtException) {
                    abortWithUnauthorized(request, "Authorization is expired");
                } else if (e instanceof MalformedJwtException) {
                    abortWithUnauthorized(request, "Authorization is not correct");
                }
            }

        } catch (Exception e) {
            abortWithUnauthorized(request, "Invalid authorization");
        }
    }

    /**
     * Check if the Authorization header is valid
     * It must not be null and must be prefixed with "Bearer" plus a whitespace
     * The authentication scheme comparison must be case-insensitive
     * 
     * @param authorizationHeader the value of Authorization header
     * @return true if authorizationHeader is valid, false otherwise
     */
    private boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase());
    }

    /**
     * Abort the filter chain with a 401 status code response
     * The WWW-Authenticate header is sent along with the response
     * 
     * @param requestContext the request context
     * @param message        the message to be sent
     *                       with the response
     */
    private void abortWithUnauthorized(ContainerRequestContext requestContext, String message) {
        System.out.println("zabort");
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED.getStatusCode(), message)
                        .header(HttpHeaders.WWW_AUTHENTICATE,
                                message)
                        .build());
    }

}
