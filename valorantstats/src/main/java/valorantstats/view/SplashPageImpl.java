package valorantstats.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import valorantstats.presenter.DisplayPresenterImpl;

public class SplashPageImpl implements SplashPage {
  private final BorderPane splashPane;
  private final String splash = "ValorantWallpaper_Haven.jpg";
  private Timeline timeline;
  private DisplayPresenterImpl displayPresenter;
  private int width = 640;
  private int height = 430;

  public SplashPageImpl() {
    this.splashPane = new BorderPane();
  }

  @Override
  public void display() {
    Image imgSplash = new Image(splash, width, height, false, false);
    ImageView imgViewSplashImg = new ImageView(imgSplash);

    ProgressBar progressBar = new ProgressBar(0);
    VBox vBoxProgressBar = new VBox(progressBar);
    progressBar.setMaxWidth(Double.MAX_VALUE);
    vBoxProgressBar.setFillWidth(true);
    this.timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
            new KeyFrame(
                Duration.seconds(15),
                e -> { // # will last 15 seconds then go to login page
                  displayPresenter.showLoginPage();
                },
                new KeyValue(progressBar.progressProperty(), 1)));
    timeline.setCycleCount(1);

    splashPane.setTop(imgViewSplashImg);
    splashPane.setBottom(vBoxProgressBar);
  }

  @Override
  public void startTimeline() {
    timeline.play();
  }

  @Override
  public Pane getPane() {
    return splashPane;
  }

  @Override
  public void setDisplayPresenter(DisplayPresenterImpl displayPresenter) {
    this.displayPresenter = displayPresenter;
  }
}
