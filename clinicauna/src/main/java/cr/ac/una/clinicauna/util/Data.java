package cr.ac.una.clinicauna.util;

import cr.ac.una.clinicauna.App;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 *
 * @author estebannajera
 * @author arayaroma
 */
public class Data {

    private static Data instance = null;
    public static final ResourceBundle spanishBundle = ResourceBundle.getBundle(App.DOMAIN_PATH + "language/lang_es");
    public static final ResourceBundle englishBundle = ResourceBundle.getBundle(App.DOMAIN_PATH + "language/lang_en");
    public static String languageOption = "es";
    private static HashMap<String, Object> data = new HashMap<>();

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null) {
            instance = new Data();
        }
        return instance;
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public void setData(String key, Object object) {
        data.put(key, object);
    }

    public void removeData(String key) {
        data.remove(key);
    }

    public void clearData() {
        data.clear();
    }

    public ResourceBundle getEnglishBundle() {
        return englishBundle;
    }

    public ResourceBundle getSpanishBundle() {
        return spanishBundle;
    }

    public String getLanguageOption() {
        return languageOption;
    }

    public void setLanguageOption(String languageOption) {
        Data.languageOption = languageOption;
    }
}
