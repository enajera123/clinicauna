package cr.ac.una.clinicaunaws.util;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author arayaroma
 */
public class HtmlFileReader {

    public static final String TEMPLATE_DIR = "/templates/";
    public static final String TITLE = "[title]";
    public static final String HEADER = "[header]";
    public static final String BODY = "[body]";
    public static final String FOOTER = "[footer]";
    public static final String TEAM = "[team]";
    public static final String USER_NAME = "[user_name]";
    public static final String USER_USERNAME = "[user_username]";
    public static final String USER_EMAIL = "[user_email]";

    public static String readHtmlFromWebApp(String path) {
        String WEBAPP_DIR = "src/main/webapp/";
        return new String(WEBAPP_DIR + path);
    }

    public static String readHtml(String path) throws IOException {
        return new String(HtmlFileReader.class
                .getResourceAsStream(TEMPLATE_DIR + path)
                .readAllBytes());
    }

    public static String readEmailTemplate(String title, String header, String userName, String body, String footer) throws IOException {
        try (InputStream inputStream = HtmlFileReader.class.getResourceAsStream(TEMPLATE_DIR + "template.html")) {
            String template = new String(inputStream.readAllBytes());
            String emailContent = template
                    .replace(TITLE, title)
                    .replace(HEADER, header)
                    .replace(USER_NAME, userName)
                    .replace(BODY, body)
                    .replace(FOOTER, footer)
                    .replace(TEAM, Constants.COMPANY_NAME);
            return emailContent;
        } catch (IOException e) {
            throw new IOException("Failed to read email template: " + e.getMessage(), e);
        }
    }
}
