package cr.ac.una.clinicauna.util;

import static cr.ac.una.clinicauna.util.Constants.SERVER_PATH;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;
import java.util.Base64;
import java.util.Map;
import cr.ac.una.clinicauna.App;
import cr.ac.una.clinicauna.services.UserService;

/**
 *
 * @author estebannajera
 * @author arayaroma
 */
public class Request {

    private static final String AUTHENTICATION_SCHEME = "Bearer ";
    private Client client;
    private Invocation.Builder builder;
    private WebTarget webTarget;
    private Response response;

    public Request() {
        this.client = ClientBuilder.newClient();
    }

    public Request(String target) {
        this();
        setTarget(target);
    }

    public Request(String target, String parametros, Map<String, Object> valores) {
        this();
        this.webTarget = client.target(SERVER_PATH + target).path(parametros).resolveTemplates(valores);
        this.builder = webTarget.request(MediaType.APPLICATION_JSON);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (Data.getInstance().getData("Token") != null) {
            headers.add("Authorization",
                    Data.getInstance().getData("Token").toString());
        }
        builder.headers(headers);
    }

    /**
     * Ingresa el objetivo de la petición
     *
     * @param target Objetivo de la petición
     */
    public void setTarget(String target) {
        this.webTarget = client.target(SERVER_PATH + target);
        this.builder = webTarget.request(MediaType.APPLICATION_JSON);
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("Content-Type", "application/json; charset=UTF-8");

        if (Data.getInstance().getData("Token") != null) {
            headers.add("Authorization",
                    Data.getInstance().getData("Token").toString());
        }

        builder.headers(headers);
    }

    public void setHeader(String key, Object value) {
        builder.header(key, value);
    }

    public void setHeader(MultivaluedMap<String, Object> values) {
        values.add("Content-Type", "application/json; charset=UTF-8");
        builder.headers(values);
    }

    public void get() {
        if (isTokenExpired()) {
            response = builder.get();
        }
    }

    public void getToken() {
        response = builder.get();
    }

    public void post(Object clazz) {
        if (isTokenExpired()) {
            Entity<?> entity = Entity.entity(clazz, "application/json; charset=UTF-8");
            response = builder.post(entity);
        }
    }

    public void put(Object clazz) {
        if (isTokenExpired()) {
            Entity<?> entity = Entity.entity(clazz, "application/json; charset=UTF-8");
            response = builder.put(entity);
        }
    }

    public void delete() {
        if (isTokenExpired()) {
            response = builder.delete();
        }
    }

    public int getStatus() {
        return response.getStatus();
    }

    public Boolean isError() throws IOException {
        boolean validResponse = getStatus() == Response.Status.OK.getStatusCode()
                || getStatus() == Response.Status.CREATED.getStatusCode()
                || getStatus() == Response.Status.NO_CONTENT.getStatusCode();

        if (validResponse) {
            return false;
        }

        if (getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
            Message.showNotification("Session expired", MessageType.INFO,
                    "Your session has expired, please login again");

            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                        App.setRoot("Login");
                    } catch (InterruptedException | IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }.start();
        }


        return getStatus() != Response.Status.OK.getStatusCode();
    }

    public String getError() {
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            String mensaje;
            if (response.hasEntity()) {
                if (response.getMediaType().equals(MediaType.TEXT_PLAIN_TYPE)) {
                    mensaje = response.readEntity(String.class);
                } else if (response.getMediaType().getType().equals(MediaType.TEXT_HTML_TYPE.getType())
                        && response.getMediaType().getSubtype()
                                .equals(MediaType.TEXT_HTML_TYPE.getSubtype())) {
                    mensaje = response.readEntity(String.class);
                    mensaje = mensaje.substring(mensaje.indexOf("<b>message</b>") + ("<b>message</b>").length());
                    mensaje = mensaje.substring(0, mensaje.indexOf("</p>"));
                } else if (response.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
                    mensaje = response.readEntity(String.class);
                } else {
                    mensaje = response.getStatusInfo().getReasonPhrase();
                }
            } else {
                mensaje = response.getStatusInfo().getReasonPhrase();
            }
            return mensaje;
        }
        return "Empty";
    }

    public Object readEntity(Class<?> clazz) {
        return response.readEntity(clazz);
    }

    public Object readEntity(GenericType<?> genericType) {
        return response.readEntity(genericType);
    }

    public void getRenewal() {
        JsonObject payload = getPayloadToken(Data.getInstance().getData("Token").toString());
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
        headers.add("Authorization", payload.getString("rnt"));
        setHeader(headers);
        response = builder.get();
    }

    public JsonObject getPayloadToken(String token) {
        if (token != null && !token.isEmpty()) {
            token = token.substring(AUTHENTICATION_SCHEME.length()).trim();
            String[] parts = token.split("\\.");
            String decodedToken = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonObject object;
            try (JsonReader jsonReader = Json.createReader(new StringReader(decodedToken))) {
                object = jsonReader.readObject();
            }
            return object;
        } else {
            return null;
        }
    }

    public boolean isTokenExpired() {
        if (Data.getInstance().getData("Token") == null) {
            return true;
        }

        JsonObject payload = getPayloadToken(Data.getInstance().getData("Token").toString());
        if (payload.getJsonNumber("exp").longValue() > System.currentTimeMillis()
                / 5000) {
            return true;
        } else {
            payload = getPayloadToken(payload.getString("rnt"));
            if (payload != null && payload.getJsonNumber("exp").longValue() > System.currentTimeMillis() / 5000) {
                UserService userService = new UserService();
                ResponseWrapper response = userService.renewToken();

                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    String newToken = response.getData().toString();
                    Data.getInstance().setData("Token", newToken);

                    MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
                    headers.add("Authorization", newToken);

                    setHeader(headers);
                    return true;
                }
            }
            response = Response.status(401, "The token has expired").build();
            return false;
        }
    }

}
