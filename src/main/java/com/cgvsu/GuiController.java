package com.cgvsu;

import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.ModelSettings;
import com.cgvsu.model.MyModel;
import com.cgvsu.myreader.MyObjReader;
import com.cgvsu.obj_writer.ObjWriter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Path;
import java.io.File;
import java.util.ArrayList;

import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;

public class GuiController {

    final private float TRANSLATION = 1F;

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

    private float firstPosX = 0,
            firstPosY = 0,
            secondPosX = 0,
            secondPosY = 0,
            FirstControlPositionX = 0;

    private final Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    public void exceptionHandler(Exception exception) {
        exception.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Sorry, but program have got an error while processing your request ;<(" +
                "\n" + exception.getMessage(), ButtonType.OK);
        alert.showAndWait();
    }

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        modelSelectionGroup.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {

            //получаем выбранный элемент RadioButton
            RadioButton selectedBtn = (RadioButton) newValue;

            //Сохраняем старую позицию камеры для модели, выставляем камеру для текущей модели
            actualModel = models.get(radioButtons.indexOf(selectedBtn));
            handleMakeTranslationArea();
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
                        model.getModel().triangulateFaces();
                        RenderEngine.render(canvas.getGraphicsContext2D(), camera, model.getModel(),
                                (int) width, (int) height,
                                model.getPercentX(), model.getPercentY(), model.getPercentZ(),
                                model.getAlphaX(), model.getAlphaY(), model.getAlphaZ(),
                                model.getTarget());
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
            ModelSettings modelSettings = new ModelSettings(model);
            models.add(modelSettings);
            RadioButton button = new RadioButton("Model " + Integer.toString(counter));
            button.setToggleGroup(modelSelectionGroup);
            radioButtons.add(button);
            modelSelectionGroup.selectToggle(button.getToggleGroup().getSelectedToggle());
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
    private void handleMakeTranslationArea() {
        graphicConveyorArea.getChildren().clear();
        Label name = new Label("Translate");
        name.setAlignment(Pos.CENTER);
        Label labelX = new Label("Set X coordinate");
        Label labelY = new Label("Set Y coordinate");
        Label labelZ = new Label("Set Z coordinate");

        TextField setXArea = new TextField("0");
        TextField setYArea = new TextField("0");
        TextField setZArea = new TextField("0");
        if (actualModel != null){
            Vector3f actualModelPosition = actualModel.getTarget();
            setXArea.setText(Float.toString(actualModelPosition.getX()));
            setYArea.setText(Float.toString(actualModelPosition.getY()));
            setZArea.setText(Float.toString(actualModelPosition.getZ()));
        }

        Button result = new Button("Update model position");
        result.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    Vector3f newModelTarget = new Vector3f(Float.parseFloat(setXArea.getText()),
                            Float.parseFloat(setYArea.getText()),
                            Float.parseFloat(setZArea.getText()));
                    // Меняем фокус камеры и положение модели
                    camera.setTarget(newModelTarget);
                    actualModel.setTarget(newModelTarget);
                } catch (Exception e) {
                    exceptionHandler(e);
                }
            }
        });


        if(graphicConveyorArea.getChildren().size() != 0) {
            graphicConveyorArea.getChildren().clear();
        }

        graphicConveyorArea.getChildren().addAll(name, labelX, setXArea, labelY, setYArea, labelZ, setZArea, result);

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

    @FXML
    public void handlerRotateAndTranslateModel(MouseEvent mouseEvent) {

        modelSelectionGroup.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {
            RadioButton selectedBtn = (RadioButton) newValue;
            actualModel = models.get(radioButtons.indexOf(selectedBtn));
            handleMakeTranslationArea();
        });

        final float MAX_COORDINATE = 10;
        if (mouseEvent.isPrimaryButtonDown()) {
            if (mouseEvent.getX() - firstPosX > MAX_COORDINATE) {
                actualModel.minusAlphaY();
                firstPosX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - firstPosX < -MAX_COORDINATE) {
                actualModel.plusAlphaY();
                firstPosX = (float) mouseEvent.getX();
            }
            if (mouseEvent.getY() - firstPosY > MAX_COORDINATE) {
                actualModel.minusAlphaX();
                firstPosY = (float) mouseEvent.getY();
            } else if (mouseEvent.getY() - firstPosY < -MAX_COORDINATE) {
                actualModel.plusAlphaX();
                firstPosY = (float) mouseEvent.getY();
            }
        }

        if (mouseEvent.isSecondaryButtonDown()) {
            if (mouseEvent.getX() - secondPosX > MAX_COORDINATE) {
                actualModel.addX(-TRANSLATION);
                secondPosX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - secondPosX < -MAX_COORDINATE) {
                actualModel.addX(TRANSLATION);
                secondPosX = (float) mouseEvent.getX();
            }
            if (mouseEvent.getY() - secondPosY > MAX_COORDINATE) {
                actualModel.addY(-TRANSLATION);
                secondPosY = (float) mouseEvent.getY();
            } else if (mouseEvent.getY() - secondPosY < -MAX_COORDINATE) {
                actualModel.addY(TRANSLATION);
                secondPosY = (float) mouseEvent.getY();
            }
        }

        if (mouseEvent.isControlDown() && mouseEvent.isPrimaryButtonDown()) {
            if (mouseEvent.getX() - FirstControlPositionX > MAX_COORDINATE) {
                actualModel.minusAlphaZ();
                FirstControlPositionX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - FirstControlPositionX < -MAX_COORDINATE) {
                actualModel.plusAlphaZ();
                FirstControlPositionX = (float) mouseEvent.getX();
            }
        }
    }

    @FXML
    public void handlerTranslateAlongZ(ScrollEvent scrollEvent) {
        final float MIN_DELTA = 0;
        if (scrollEvent.getDeltaY() > MIN_DELTA) {
            final float SCALE_INCREASE_VALUE = 2;
            actualModel.increaseScale(SCALE_INCREASE_VALUE);
        } else {
            final float SCALE_DECREASE_VALUE = 0.5f;
            actualModel.increaseScale(SCALE_DECREASE_VALUE);
        }
    }

    @FXML
    public void handlerStartDrag(MouseDragEvent mouseDragEvent) {
        if (mouseDragEvent.isPrimaryButtonDown()) {
            firstPosX = (float) mouseDragEvent.getX();
            firstPosY = (float) mouseDragEvent.getY();
        } else if (mouseDragEvent.isSecondaryButtonDown()) {
            secondPosX = (float) mouseDragEvent.getX();
            secondPosY = (float) mouseDragEvent.getY();
        }
    }
    @FXML
    public void focusCanvas(MouseEvent mouseEvent) {
        canvas.requestFocus();
    }
}