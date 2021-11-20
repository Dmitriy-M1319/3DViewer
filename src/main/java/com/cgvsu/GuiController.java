package com.cgvsu;

import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.ModelSettings;
import com.cgvsu.model.MyModel;
import com.cgvsu.myreader.MyObjReader;
import com.cgvsu.obj_writer.ObjWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;

import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    FlowPane flowPane;

    @FXML
    private Canvas canvas;
    private ToggleGroup group = new ToggleGroup();
    private ArrayList<RadioButton>radioButtons = new ArrayList<>();

    private ModelSettings actualModel = null;
    private ArrayList<ModelSettings> models = new ArrayList<>();

    private char token = 'x';



    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    public void exceptionHandler(Exception exception){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, but program have got an error while processing your request ;<(" +
                "\n" + exception.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldValue, Toggle newValue){

                // получаем выбранный элемент RadioButton
                RadioButton selectedBtn = (RadioButton) newValue;
                actualModel = models.get(radioButtons.indexOf(selectedBtn));
            }
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (actualModel != null) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, actualModel.getModel(), (int) width, (int) height, actualModel.getPercent(),actualModel.getPercent(), actualModel.getPercent(), actualModel.getAlpha(), actualModel.getTarget(), token);
                } catch (Exception e) {
                    exceptionHandler(e);
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private int counter = 1;
    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            MyModel model = MyObjReader.read(fileName.toString());
            ModelSettings modelSettings = new ModelSettings(model);
            models.add(modelSettings);
            actualModel = modelSettings;

            RadioButton button = new RadioButton("Model " + Integer.toString(counter));
            button.setToggleGroup(group);
            radioButtons.add(button);
            flowPane.getChildren().add(button);
            counter++;
        } catch (Exception exception) {
            exceptionHandler(exception);
        }
    }

    @FXML
    public void onSaveModelMenuItemClick() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        chooser.setTitle("Save Model");

        File file = chooser.showSaveDialog((Stage) canvas.getScene().getWindow());

        Path filename = Path.of(file.getAbsolutePath());
        try {
            ObjWriter.write(actualModel.getModel(), filename.toString());
        } catch (Exception exception) {
            exceptionHandler(exception);
        }
    }


// todo: Сделать для каждой оси.
    @FXML
    public void handleScalePlus(ActionEvent actionEvent) {
        actualModel.plusPercent();
    }

    @FXML
    public void handleScaleMinus(ActionEvent actionEvent) {
        actualModel.minusPercent();
    }

    @FXML
    public void handleSetX(ActionEvent actionEvent) { this.token = 'x';}

    @FXML
    public void handleSetY(ActionEvent actionEvent) { this.token = 'y';}
    @FXML
    public void handleSetZ(ActionEvent actionEvent) { this.token = 'z';}

    @FXML
    public void handleRotateRight(ActionEvent actionEvent) {
        actualModel.plusAlpha();
    }

    @FXML
    public void handleRotateLeft(ActionEvent actionEvent) {
        actualModel.minusAlpha();
    }

    @FXML
    public void handleCameraForward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, -TRANSLATION));
    }

    @FXML
    public void handleCameraBackward(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, 0, TRANSLATION));
    }

    @FXML
    public void handleCameraLeft(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraRight(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(-TRANSLATION, 0, 0));
    }

    @FXML
    public void handleCameraUp(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, TRANSLATION, 0));
    }

    @FXML
    public void handleCameraDown(ActionEvent actionEvent) {
        camera.movePosition(new Vector3f(0, -TRANSLATION, 0));
    }

}
