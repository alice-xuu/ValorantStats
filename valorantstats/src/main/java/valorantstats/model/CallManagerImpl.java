package valorantstats.model;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import valorantstats.model.apicalls.*;
import valorantstats.model.callhandlers.ApiCaller;
import valorantstats.model.callhandlers.CacheCaller;
import valorantstats.model.callhandlers.Caller;
import valorantstats.model.enums.CallResult;
import valorantstats.model.jsonobjects.ErrorResponseOutput;
import valorantstats.model.jsonobjects.ImgurData;
import valorantstats.model.jsonobjects.Match;
import valorantstats.model.jsonobjects.Team;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleEntry;
import java.util.Base64;

/** Implementation of CallManager to manage user team and match calls */
public class CallManagerImpl implements CallManager {
  private final boolean inputIsOnline;
  private final boolean outputIsOnline;
  private final String qrCodeSF = "qrcode.png";
  Caller chain;
  private InputApiCalls inputApiCalls;
  private OutputApiCalls outputApiCalls;
  private Integer currentTeamId = null;
  private CacheManager cacheManager;
  private Caller apiCaller;

  public CallManagerImpl(
      boolean inputIsOnline, boolean outputIsOnline, Database db, CacheManager cacheManager) {
    this.inputIsOnline = inputIsOnline;
    this.outputIsOnline = outputIsOnline;
    this.cacheManager = cacheManager;
    if (isInputIsOnline()) {
      this.inputApiCalls = new InputApiCallsOnline();
      Caller cacheCaller = new CacheCaller(cacheManager, inputApiCalls);
      apiCaller = new ApiCaller(cacheManager, inputApiCalls);
      cacheCaller.setNextCaller(apiCaller);
      chain = cacheCaller;

    } else {
      this.inputApiCalls = new inputApiCallsOffline();
      apiCaller = new ApiCaller(cacheManager, inputApiCalls);
      chain = apiCaller;
    }

    if (isOutputIsOnline()) {
      this.outputApiCalls = new OutputApiCallsOnline();
    } else {
      this.outputApiCalls = new OutputApiCallsOffline();
    }
  }

  @Override
  public SimpleEntry<CallResult, String> searchTeam(String name, boolean cacheOverride) {
    if (!validateInputTeamName(name)) {
      return new SimpleEntry<>(CallResult.ERROR, "Invalid input");
    }
    SimpleEntry<CallResult, String> result = chain.callTeam(name, cacheOverride);

    if (result.getKey() == CallResult.ERROR || result.getKey() == CallResult.CACHEHIT) {

      return result;
    } else {
      Team[] teams = stringToTeamArray(result.getValue());
      if (teams.length == 0) {
        String msg = "No team found: " + name;
        return new SimpleEntry<>(CallResult.ERROR, msg);
      } else {
        return new SimpleEntry<>(CallResult.SUCCESS, result.getValue());
      }
    }
  }

  @Override
  public Team[] stringToTeamArray(String string) {
    Team[] teams = new Gson().fromJson(string, Team[].class);
    return teams;
  }

  @Override
  public Team stringToTeamOne(String string) {
    Team[] teams = new Gson().fromJson(string, Team[].class);
    Team team = teams[0];

    return team;
  }

  @Override
  public SimpleEntry<CallResult, String> searchTeamUpcomingMatches(
      String teamId, boolean cacheOverride) {
    SimpleEntry<CallResult, String> result = chain.callMatch(teamId, cacheOverride);

    if (result.getKey() == CallResult.ERROR || result.getKey() == CallResult.CACHEHIT) {
      return result;
    } else {
      Match[] matches = stringToMatchArray(result.getValue());
      if (matches.length == 0) {
        String msg = "No matches found";
        return new SimpleEntry<>(CallResult.ERROR, msg);
      } else {
        return new SimpleEntry<>(CallResult.SUCCESS, result.getValue());
      }
    }
  }

  @Override
  public Match[] stringToMatchArray(String string) {
    return new Gson().fromJson(string, Match[].class);
  }

  @Override
  public boolean validateInputTeamName(String input) {
    if (input.equals("")) {
      return false;
    }
    return true;
  }

  /**
   * Upload qrCode to output API imgur
   *
   * @param img
   */
  @Override
  public SimpleEntry<CallResult, String> uploadImage(String img) {
    ApiResponse response = outputApiCalls.postRequestImgurShortForm(img);
    SimpleEntry<CallResult, String> result = validateResponseOutputApi(response);
    if (result.getKey() == CallResult.SUCCESS) {
      ImgurData imgurData = stringToImgurData(result.getValue());
      result.setValue(imgurData.getData().getLink());
    }
    return result;
  }

  @Override
  public ImgurData stringToImgurData(String string) {
    return new Gson().fromJson(string, ImgurData.class);
  }

  @Override
  public BufferedImage createQR(String data) {
    try {
      String fileName = "src/main/resources/" + qrCodeSF;
      int width = 100;
      int height = 100;

      // generate QR code
      QRCodeWriter qrCodeWriter = new QRCodeWriter();
      BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
      Path filePath = Paths.get(fileName);
      MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

      return MatrixToImageWriter.toBufferedImage(bitMatrix);

    } catch (WriterException | IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  public String getQrCodeSF() {
    return qrCodeSF;
  }

  @Override
  public String bufferedImageToB64(BufferedImage bi) {

    java.io.ByteArrayOutputStream os = new ByteArrayOutputStream();
    try {
      ImageIO.write(bi, "png", os);
      String b64 = Base64.getEncoder().encodeToString(os.toByteArray());
      return b64;
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public boolean isInputIsOnline() {
    return inputIsOnline;
  }

  @Override
  public boolean isOutputIsOnline() {
    return outputIsOnline;
  }

  @Override
  public InputApiCalls getInputApiCalls() {
    return inputApiCalls;
  }

  @Override
  public void setInputApiCalls(InputApiCalls inputApiCalls) {
    this.inputApiCalls = inputApiCalls;
  }

  @Override
  public OutputApiCalls getOutputApiCalls() {
    return outputApiCalls;
  }

  @Override
  public void setOutputApiCalls(OutputApiCalls outputApiCalls) {
    this.outputApiCalls = outputApiCalls;
  }

  @Override
  public SimpleEntry<CallResult, String> validateResponseOutputApi(ApiResponse response) {
    SimpleEntry<CallResult, String> result;
    int statusCode = response.getStatusCode();
    String body = response.getBody();
    if (statusCode == 200) {
      result = new SimpleEntry<>(CallResult.SUCCESS, body);
    } else {
      ErrorResponseOutput ero = new Gson().fromJson(body, ErrorResponseOutput.class);
      String msg = ero.getData().getError();
      result = new SimpleEntry<>(CallResult.ERROR, msg);
    }

    return result;
  }

  @Override
  public Integer getCurrentTeamId() {
    return currentTeamId;
  }

  @Override
  public void setCurrentTeamId(Integer currentTeamId) {
    this.currentTeamId = currentTeamId;
  }

  @Override
  public Caller getChain() {
    return chain;
  }

  @Override
  public Caller getApiCaller() {
    return apiCaller;
  }
}
