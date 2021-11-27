package com.cgvsu;

import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.ModelSettings;
import com.cgvsu.model.MyModel;
import com.cgvsu.myreader.MyObjReader;
import com.cgvsu.obj_writer.ObjWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    FlowPane flowPane;

    @FXML
    VBox graphicConveyorArea;

    @FXML
    FlowPane modelMenu;

    @FXML
    private Canvas canvas;
    private ToggleGroup modelSelectionGroup = new ToggleGroup();
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
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
        exception.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, but program have got an error while processing your request ;<(" +
                "\n" + exception.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        modelSelectionGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){

            public void changed(ObservableValue<? extends Toggle> changed, Toggle oldValue, Toggle newValue){

                //получаем выбранный элемент RadioButton
                RadioButton selectedBtn = (RadioButton) newValue;

                //Сохраняем старую позицию камеры для модели, выставляем камеру для текущей модели
                actualModel.setCameraPos(camera.getPosition());
                actualModel = models.get(radioButtons.indexOf(selectedBtn));
                if (actualModel.getCameraPos() != null) {
                    camera.setPosition(actualModel.getCameraPos());
                    camera.setTarget(actualModel.getTarget());
                }

                //Создаём интерфейс для перемещения текущей модели, убираем старый
                modelMenu.getChildren().clear();
                Vector3f actualModelCoords = actualModel.getTarget();
                Text textX = new Text("X moving:");
                TextField selectX = new TextField(Float.toString(actualModelCoords.getX()));
                selectX.setText(Float.toString(actualModelCoords.getX()));
                modelMenu.getChildren().add(textX);
                modelMenu.getChildren().add(selectX);
                Text textY = new Text("Y moving:");
                TextField selectY = new TextField(Float.toString(actualModelCoords.getY()));
                selectX.setText(Float.toString(actualModelCoords.getY()));
                modelMenu.getChildren().add(textY);
                modelMenu.getChildren().add(selectY);
                Text textZ = new Text("Z moving:");
                TextField selectZ = new TextField(Float.toString(actualModelCoords.getZ()));
                selectX.setText(Float.toString(actualModelCoords.getZ()));
                modelMenu.getChildren().add(textZ);
                modelMenu.getChildren().add(selectZ);
                Button button = new Button("Update model position");
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        try {
                            Vector3f newModelTarget = new Vector3f(Float.parseFloat(selectX.getText()),
                                    Float.parseFloat(selectY.getText()),
                                    Float.parseFloat(selectZ.getText()));

                            //Здесь просчитываем позицию камеры относительно модели, потом получаем новую позицию камеры
                            Vector3f betweenModelAndCamera = (Vector3f)
                                    camera.getPosition().subtraction(actualModel.getTarget());
                            Vector3f newCameraPos = (Vector3f) newModelTarget.sum(betweenModelAndCamera);

                            camera.setPosition(newCameraPos);
                            camera.setTarget(newModelTarget);
                            actualModel.setTarget(newModelTarget);
                            actualModel.setCameraPos(newCameraPos);
                        } catch (Exception e) {
                            exceptionHandler(e);
                        }
                    }
                });
                modelMenu.getChildren().add(button);
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
                for (ModelSettings model:
                        models) {
                    try {
                        RenderEngine.render(canvas.getGraphicsContext2D(), camera, model.getModel(), (int) width, (int) height, model.getPercentX(), model.getPercentY(), model.getPercentZ(), model.getAlpha(), model.getTarget(), token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //RenderEngine.render(canvas.getGraphicsContext2D(), camera, actualModel.getModel(), (int) width, (int) height, actualModel.getPercentX(),actualModel.getPercentY(), actualModel.getPercentZ(), actualModel.getAlpha(), actualModel.getTarget(), token);
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
            ModelSettings modelSettings = new ModelSettings(model, new Vector3f(0, 0, 100),
                    new Vector3f(0, 0, 0));
            models.add(modelSettings);
            actualModel = modelSettings;
            RadioButton button = new RadioButton("Model " + Integer.toString(counter));
            button.setToggleGroup(modelSelectionGroup);
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

    @FXML
    private void handleMakeScaleArea() {
        Label name = new Label("Scale");
        name.setAlignment(Pos.CENTER);
        Label labelX = new Label("Set X coordinate");
        Label labelY = new Label("Set Y coordinate");
        Label labelZ = new Label("Set Z coordinate");

        TextField setXArea = new TextField("1");
        TextField setYArea = new TextField("1");
        TextField setZArea = new TextField("1");

        Button result = new Button("Scale");
        result.setOnAction(actionEvent -> {
            actualModel.setPercentX(Float.parseFloat(setXArea.getText()));
            actualModel.setPercentY(Float.parseFloat(setYArea.getText()));
            actualModel.setPercentZ(Float.parseFloat(setZArea.getText()));
        });


        if(graphicConveyorArea.getChildren().size() != 0) {
            graphicConveyorArea.getChildren().clear();
        }

        graphicConveyorArea.getChildren().addAll(name, labelX, setXArea, labelY, setYArea, labelZ, setZArea, result);

    }

    @FXML
    private void handleMakeTranslationArea() {
        Label name = new Label("Translate");
        name.setAlignment(Pos.CENTER);
        Label labelX = new Label("Set X coordinate");
        Label labelY = new Label("Set Y coordinate");
        Label labelZ = new Label("Set Z coordinate");

        TextField setXArea = new TextField("0");
        TextField setYArea = new TextField("0");
        TextField setZArea = new TextField("0");

        Button result = new Button("Translate");
        result.setOnAction(actionEvent -> actualModel.setTarget(new Vector3f(Float.parseFloat(setXArea.getText()), Float.parseFloat(setYArea.getText()), Float.parseFloat(setZArea.getText()))));


        if(graphicConveyorArea.getChildren().size() != 0) {
            graphicConveyorArea.getChildren().clear();
        }

        graphicConveyorArea.getChildren().addAll(name, labelX, setXArea, labelY, setYArea, labelZ, setZArea, result);

    }


    @FXML
    public void handleScalePlus(ActionEvent actionEvent) {
        actualModel.setPercentX(actualModel.getPercentX() + 0.1F);
        actualModel.setPercentY(actualModel.getPercentY() + 0.1F);
        actualModel.setPercentZ(actualModel.getPercentY() + 0.1F);
    }

    @FXML
    public void handleScaleMinus(ActionEvent actionEvent) {
        actualModel.setPercentX(actualModel.getPercentX() - 0.1F);
        actualModel.setPercentY(actualModel.getPercentY() - 0.1F);
        actualModel.setPercentZ(actualModel.getPercentY() - 0.1F);
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