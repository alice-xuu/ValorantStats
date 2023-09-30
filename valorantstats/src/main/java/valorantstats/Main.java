package valorantstats;

import javafx.application.Application;
import javafx.stage.Stage;
import valorantstats.model.Database;
import valorantstats.model.ModelFacade;
import valorantstats.presenter.DisplayPresenter;
import valorantstats.presenter.DisplayPresenterImpl;
import valorantstats.presenter.SearchPagePresenter;
import valorantstats.presenter.SearchPagePresenterImpl;
import valorantstats.view.*;

public class Main extends Application {

  public static void main(String[] args) {
    if (args.length < 2) {
      System.exit(1);
    }

    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    boolean inputIsOnline = getParameters().getRaw().get(0).equals("online");
    boolean outputIsOnline = getParameters().getRaw().get(1).equals("online");
    Database db = new Database();
    // db.removeDB();
    db.createDB();
    db.setupDB();

    ModelFacade modelFacade = new ModelFacade(inputIsOnline, outputIsOnline, db);
    SplashPage splashPage = new SplashPageImpl();
    LoginPage loginPage = new LoginPageImpl();
    PersonalisationPageImpl personalisationPage = new PersonalisationPageImpl();
    SearchPage searchPage = new SearchPageImpl();
    SearchPage searchPageFast = new SearchPageImplFast();

    WitDisplay witDisplay = new WitDisplayImpl();
    Display display =
        new DisplayImpl(splashPage, loginPage, personalisationPage, searchPage, witDisplay);
    DisplayPresenter displayPresenter =
        new DisplayPresenterImpl(
            modelFacade,
            display,
            splashPage,
            loginPage,
            personalisationPage,
            searchPage,
            witDisplay,
            searchPageFast);
    SearchPagePresenter searchPagePresenter =
        new SearchPagePresenterImpl(modelFacade, searchPage, searchPageFast);
    primaryStage.setScene(display.getScene());
    primaryStage.setTitle("Pandascore Valorant");
    primaryStage.show();
  }
}
