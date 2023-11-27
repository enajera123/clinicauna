package cr.ac.una.clinicauna.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import javafx.scene.image.Image;

/**
 *
 * @author arayaroma
 * @author enajera
 */
public class ImageLoader {

    public static byte[] imageToByteArray(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static ByteArrayInputStream byteArrayToImage(byte[] image) {
        return new ByteArrayInputStream(image);
    }

    public static Image setImage(File file) {
        return new Image(file.toURI().toString());
    }

    public static Image setImage(byte[] image) {
        return new Image(byteArrayToImage(image));
    }

}
