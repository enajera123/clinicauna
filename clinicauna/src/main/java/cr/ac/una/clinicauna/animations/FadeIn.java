package cr.ac.una.clinicauna.animations;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 *
 * @author estebannajera
 */
public class FadeIn extends Animate {

    public FadeIn(Node node) {
        super(node);
    }

    public FadeIn() {
    }

    @Override
    protected Animate resetNode() {
        getNode().setOpacity(1);
        return this;
    }

    @Override
    protected void initTimeline() {
        setTimeline(new Timeline(new KeyFrame(Duration.millis(0), new KeyValue(getNode().opacityProperty(), 0, Interpolator.SPLINE(0.25, 0.1, 0.25, 1))),
                new KeyFrame(Duration.millis(1000), new KeyValue(getNode().opacityProperty(), 1, Interpolator.SPLINE(0.25, 0.1, 0.25, 1)))));
    }
}
