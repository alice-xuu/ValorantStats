package valorantstats.view;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import valorantstats.presenter.DisplayPresenterImpl;

/** Personalisation display for user color preferences */
public interface PersonalisationPage {
  /**
   * Display color pickers
   *
   * @return Pane
   */
  Pane colourSettingsPane();

  /**
   * Create preview pane with text and highlight
   *
   * @param highlightColor highlight color style
   */
  void buildPreviewPane(String highlightColor);

  void updateBackgroundColorString(Color backgroundColor);

  void updateTextColorString(Color textColor);

  void updateButtonColorString(Color buttonColor);

  void updateHighlightColorString(Color highlightColor);

  /**
   * Convert Color to a hex string representation
   *
   * @param color
   * @return
   */
  default String toHexString(Color color) {
    int r = ((int) Math.round(color.getRed() * 255)) << 24;
    int g = ((int) Math.round(color.getGreen() * 255)) << 16;
    int b = ((int) Math.round(color.getBlue() * 255)) << 8;
    int a = ((int) Math.round(color.getOpacity() * 255));
    return String.format("#%08X", (r + g + b + a));
  }

  Pane getPersonalisationPage();

  void setDisplayPresenter(DisplayPresenterImpl displayPresenter);
}
