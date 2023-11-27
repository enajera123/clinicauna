package cr.ac.una.clinicauna.animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.PerspectiveCamera;
import static javafx.scene.transform.Rotate.Y_AXIS;
import javafx.util.Duration;

/**
 *
 * @author estebannajera
 */
public class FlipInY extends Animate {

    public FlipInY(Node node) {
        super(node);
    }

    public FlipInY() {
    }

    @Override
    protected Animate resetNode() {
        getNode().setOpacity(1);
        getNode().setRotate(0);
        return this;
    }

    @Override
    protected void initTimeline() {
        getNode().getScene().setCamera(new PerspectiveCamera());
        getNode().setRotationAxis(Y_AXIS);
        setTimeline(new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(getNode().opacityProperty(), 0, Interpolator.EASE_IN),
                        new KeyValue(getNode().rotateProperty(), -90, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(getNode().rotateProperty(), 20, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(getNode().rotateProperty(), -10, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(getNode().rotateProperty(), 5, Interpolator.EASE_IN)
                ),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(getNode().opacityProperty(), 1, Interpolator.EASE_IN),
                        new KeyValue(getNode().rotateProperty(), 0, Interpolator.EASE_IN)
                )
        ));
        getTimeline().setOnFinished(event -> getNode().getScene().setCamera(new ParallelCamera()));
    }
}
