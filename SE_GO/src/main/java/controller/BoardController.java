package controller;

import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GoModel;
import singleComponents.HLine;
import singleComponents.Settings;
import singleComponents.Stone;
import singleComponents.VLine;

import java.io.IOException;

public class BoardController {
    private int boardSize;
    private int handicap;
    private double komi;
    int sceneWidth = 0;
    double stoneRatio = 0.8;
    private GoModel model;
    private HostServices hostServices;

    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices ;
    }

    public HostServices getHostServices() {
        return hostServices ;
    }

    public int getHandicap() {
        return handicap;
    }

    public double getKomi() {
        return komi;
    }

    @FXML
    private Button btn_exit;

    @FXML
    private Button btn_pass;

    @FXML
    private Button btn_saveGame;

    @FXML
    private Button btn_surrender;

    @FXML
    private GridPane gp_bigGrid;

    @FXML
    private GridPane gp_boardGrid;

    @FXML
    private Text txt_status;

    @FXML
    private Text txt_blackPoints;

    @FXML
    private Text txt_blackPointsLabel;

    @FXML
    private Text txt_heading;

    @FXML
    private Text txt_whitePoints;

    @FXML
    private Text txt_whitePointsLabel;

    private int zug = 0;

    public void setZug(int zug) {
        this.zug = zug;
    }

    public void initData(Settings s) {
        boardSize = s.getBoardSize();
        handicap = s.getHandicap();
        komi = s.getKomi();

        model = new GoModel(boardSize, this);

        createAndConfigurePane();
        createAndLayoutControls();
        updateControllerFromListeners();
        gridReload();
    }

    @FXML
    void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onPass(ActionEvent event) {
        zug++;
        if(zug == 2){
            model.endGame();
        }
        model.setStatusTextPassed();
    }

    @FXML
    void onSave(ActionEvent event) {

    }

    @FXML
    void onSurrender(ActionEvent event) {
        model.endGame();
    }

    @FXML
    void newGame(ActionEvent event) {

    }

    @FXML
    void openRules(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Rules.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Regeln");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    @FXML
    void openLink(ActionEvent event) {
        hostServices = this.getHostServices();
        hostServices.showDocument("https://de.wikipedia.org/wiki/Go_(Spiel)");
    }

    @FXML
    void openAbout(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/About.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Über");
        stage.setScene(new Scene(root, 300, 200));
        stage.show();
    }

    public void setStatusText(String text){
        txt_status.setText(text);
    }

    private void createAndConfigurePane() {
        gp_boardGrid.setGridLinesVisible(false);

        gp_boardGrid.setStyle("-fx-background-color: #FAEBD7;");
        gp_bigGrid.setStyle("-fx-background-color: #FAEBD7;");

        this.sceneWidth = (int) gp_boardGrid.getWidth();

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49 + (i % 9));
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, 0, i + 1);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 1, 0);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int id = i * boardSize + j;
                Group group = new Group();
                int stoneRadius = sceneWidth / (boardSize + 2) / 2;
                Stone stone = new Stone(id, stoneRadius * stoneRatio, model);
                HLine newLineH;
                VLine newLineV;

                if (j == 0) {
                    newLineH = new HLine(id, 0, stoneRadius);
                } else if (j == boardSize - 1) {
                    newLineH = new HLine(id, -stoneRadius, 0);
                } else {
                    newLineH = new HLine(id, -stoneRadius, stoneRadius);
                }

                if (i == 0) {
                    newLineV = new VLine(id, 0, stoneRadius);
                } else if (i == boardSize - 1) {
                    newLineV = new VLine(id, -stoneRadius, 0);
                } else {
                    newLineV = new VLine(id, -stoneRadius, stoneRadius);
                }
                group.getChildren().add(newLineH);
                group.getChildren().add(newLineV);
                group.getChildren().add(stone);
                gp_boardGrid.add(group, j + 1, i + 1);
            }
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (49 + (i % 9));
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, boardSize + 1, i + 1);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }

        for (int i = 0; i < boardSize; i++) {
            char vertical = (char) (65 + i);
            Text text = new Text(Character.toString(vertical));
            gp_boardGrid.add(text, i + 1, boardSize + 1);
            gp_boardGrid.setHalignment(text, HPos.CENTER);
        }
    }

    private void createAndLayoutControls() {
        //gp_bigGrid.add(gp_boardGrid, 1, 0);
        //btn_cstones = new Button("CSTONES");
        //bigGrid.add(btn_cstones, 0, 0);
    }

    private void updateControllerFromListeners() {
        /*btn_cstones.setOnMouseClicked(mouseEvent -> {
            model.removeCatchedStones();
            gridReload();
        });*/

        gp_boardGrid.widthProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_boardGrid.getHeight(), newVal.doubleValue());
            radius = radius / (boardSize + 2) / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Group) {
                    for (Node n2 : ((Group) n).getChildren()) {
                        if (n2 instanceof Stone) ((Stone) n2).setRadius(radius * stoneRatio);
                        if (n2 instanceof HLine) {
                            HLine l = (HLine) n2;
                            if (l.getGoLineId() % boardSize == 0) {
                                l.setEndX(radius);
                            } else if (l.getGoLineId() % boardSize == boardSize - 1) {
                                l.setStartX(-radius);
                            } else {
                                l.setStartX(-radius);
                                l.setEndX(radius);
                            }
                        }
                    }
                }
            }
        });

        gp_boardGrid.heightProperty().addListener((obs, oldVal, newVal) -> {
            double radius = Math.min(gp_boardGrid.getWidth(), newVal.doubleValue());
            radius = radius / (boardSize + 2) / 2;
            for (Node n : gp_boardGrid.getChildren()) {
                if (n instanceof Group) {
                    for (Node n2 : ((Group) n).getChildren()) {
                        if (n2 instanceof Stone) ((Stone) n2).setRadius(radius * stoneRatio);
                        if (n2 instanceof VLine) {
                            VLine l = (VLine) n2;
                            if (l.getGoLineId() / boardSize == 0) {
                                l.setEndY(radius);
                            } else if (l.getGoLineId() / boardSize == boardSize - 1) {
                                l.setStartY(-radius);
                            } else {
                                l.setStartY(-radius);
                                l.setEndY(radius);
                            }

                        }
                    }
                }
            }
        });
    }

    public void gridReload() {
        for (Node n : gp_boardGrid.getChildren()) {
            if (n instanceof Group) {
                for (Node n2 : ((Group) n).getChildren()) {
                    if (n2 instanceof Stone) ((Stone) n2).setFill(model.getColorById(((Stone) n2).id));
                }
            }
        }
        txt_whitePoints.setText("" + model.getWhitePoints());
        txt_blackPoints.setText("" + model.getBlackPoints());
    }


}
