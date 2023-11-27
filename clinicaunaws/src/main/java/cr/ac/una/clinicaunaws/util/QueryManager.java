package cr.ac.una.clinicaunaws.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Data;

/**
 * 
 * @author arayaroma
 */
@Data
public class QueryManager<D> {

    private String query;
    private String status;
    private List<D> result;

    @SuppressWarnings("unchecked")
    public void setResult(List<?> result) {
        this.result = (List<D>) result;
    }

    public static List<String> extractAlias(String query) {
        List<String> parameters = new ArrayList<>();
        Pattern pattern = Pattern.compile("SELECT\\s+(.*?)\\s+FROM", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(query);
        if (matcher.find()) {
            String selectedValues = matcher.group(1);
            String[] valueArray = selectedValues.split(",");
            for (String value : valueArray) {
                String[] parts = value.trim().split("(?i)\\s+(?i)as\\s+");
                if (parts.length > 1) {
                    parameters.add(parts[1]);
                } else {
                    parameters.add(parts[0]);
                }
            }
        }
        return parameters;
    }

}
