package valorantstats.view;

import javafx.scene.layout.Pane;
import valorantstats.presenter.DisplayPresenterImpl;

/** Splash screen */
public interface SplashPage {
  void display();

  /** Start timer for splash */
  void startTimeline();

  Pane getPane();

  void setDisplayPresenter(DisplayPresenterImpl displayPresenter);
}
