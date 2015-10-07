package cz.piczkemon;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;


public class Piczke extends Application {

    // Frames names
    private final int WALK_RIGHT1 = 0;
    private final int WALK_RIGHT2 = 1;
    private final int WALK_RIGHT3 = 2;
    private final int WALK_RIGHT4 = 3;
    private final int WALK_LEFT1 = 4;
    private final int WALK_LEFT2 = 5;
    private final int WALK_LEFT3 = 6;
    private final int WALK_LEFT4 = 7;
    private final int IDLE = 8;
    private final int DRAGGING = 9;

    private Image[] sprites = {
      new Image(getClass().getResourceAsStream("/walkright01.png")),
      new Image(getClass().getResourceAsStream("/walkright02.png")),
      new Image(getClass().getResourceAsStream("/walkright03.png")),
      new Image(getClass().getResourceAsStream("/walkright04.png")),
      new Image(getClass().getResourceAsStream("/walkleft01.png")),
      new Image(getClass().getResourceAsStream("/walkleft02.png")),
      new Image(getClass().getResourceAsStream("/walkleft03.png")),
      new Image(getClass().getResourceAsStream("/walkleft04.png")),
      new Image(getClass().getResourceAsStream("/idle.png")),
      new Image(getClass().getResourceAsStream("/dragging.png")),
    };

    private int[] walking_right_indexes = {WALK_RIGHT1, WALK_RIGHT2, WALK_RIGHT3, WALK_RIGHT4};
    private int[] walking_left_indexes = {WALK_LEFT1, WALK_LEFT2, WALK_LEFT3, WALK_LEFT4};

    private Duration frameDuration = Duration.millis(1000/5);
    private Timeline mainLoop;
    private KeyFrame renderFrame;
    private ImageView imageView;
    private int currentFrame = IDLE;
    private int currentWalkingFrame = 0;
    private Double currentX = 0.0;
    private Double currentY = 0.0;
    private int velocity = 5;

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
        VBox box = new VBox();
        imageView = new ImageView(sprites[currentFrame]);

        box.getChildren().add(imageView);
        final Scene scene = new Scene(box, 0, 0);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(currentX);
        stage.setY(currentY);
        scene.setFill(null);
        stage.setScene(scene);
        mainLoop = new Timeline();
        mainLoop.setCycleCount(Animation.INDEFINITE);
        renderFrame = new KeyFrame(frameDuration, new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
            Double currentFrameWidth = sprites[currentFrame].getWidth();
            Double currentFrameHeight = sprites[currentFrame].getHeight();
            stage.setWidth(currentFrameWidth);
            stage.setHeight(currentFrameHeight);
            currentY = screenBounds.getMinY() + screenBounds.getHeight() - currentFrameHeight;

            Double minLimitX = 0.0;
            Double maxLimitX = screenBounds.getMinX() + screenBounds.getWidth() - currentFrameWidth;
            if (currentX >= maxLimitX || currentX <= minLimitX) {
              velocity = -1 * velocity;
              currentX = currentX >= maxLimitX ? maxLimitX - 1 : minLimitX + 1;
              currentWalkingFrame = 0;
            } else {
              currentX += velocity;
            }

            if (velocity > 0) {
              currentFrame = walking_right_indexes[currentWalkingFrame];
            } else {
              currentFrame = walking_left_indexes[currentWalkingFrame];
            }

            stage.setX(currentX);
            stage.setY(currentY);
            imageView.setImage(sprites[currentFrame]);
            currentWalkingFrame = (++currentWalkingFrame) % walking_right_indexes.length;
          }
        });
        mainLoop.getKeyFrames().add(renderFrame);
        stage.show();
        mainLoop.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
