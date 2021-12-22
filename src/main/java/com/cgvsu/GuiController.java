package com.cgvsu;

import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.ModelSettings;
import com.cgvsu.model.MyModel;
import com.cgvsu.myreader.MyObjReader;
import com.cgvsu.model.Normals;
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
    public TextField changeScaleX, changeScaleY, changeScaleZ;
    
    @FXML
    private Canvas canvas;
    private ToggleGroup modelSelectionGroup = new ToggleGroup();
    private ArrayList<RadioButton> radioButtons = new ArrayList<>();
    private ModelSettings actualModel = null;
    private ArrayList<ModelSettings> models = new ArrayList<>();

    private char token = 'x';

    private float positionPrimaryButtonX = 0, positionPrimaryButtonY = 0,
            positionSecondaryButtonX = 0, positionSecondaryButtonY = 0,
            positionPrimaryControlX = 0;

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

        modelSelectionGroup.selectedToggleProperty().addListener((changed, oldValue, newValue) -> {

            //получаем выбранный элемент RadioButton
            RadioButton selectedBtn = (RadioButton) newValue;

            //Сохраняем старую позицию камеры для модели, выставляем камеру для текущей модели
            actualModel = models.get(radioButtons.indexOf(selectedBtn));
            camera.setTarget(actualModel.getTarget());
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
                        Normals.calculateNormals(model.getModel());
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
        actualModel.plusAlphaX();
    }
    @FXML
    public void handleRotateLeft(ActionEvent actionEvent) {
        actualModel.minusAlphaX();
    }

    @FXML
    public void handleRotateUp(ActionEvent actionEvent) {
        actualModel.plusAlphaY();
    }
    @FXML
    public void handleRotateDown(ActionEvent actionEvent) {
        actualModel.minusAlphaY();
    }
    @FXML
    public void handleRotateForward(ActionEvent actionEvent) {
        actualModel.plusAlphaZ();
    }
    @FXML
    public void handleRotateBackward(ActionEvent actionEvent) {
        actualModel.minusAlphaZ();
    }

    @FXML
    public void focusChangeScaleForX(MouseEvent mouseEvent) {
        changeScaleX.requestFocus();
    }

    @FXML
    public void focusChangeScaleForY(MouseEvent mouseEvent) {
        changeScaleY.requestFocus();
    }

    @FXML
    public void forChangeScaleForZ(MouseEvent mouseEvent) {
        changeScaleZ.requestFocus();
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
        ModelSettings model = models.get(0);
        if (mouseEvent.isPrimaryButtonDown()) {
            if (mouseEvent.getX() - positionPrimaryButtonX > 10) {
                model.minusAlphaY();
                positionPrimaryButtonX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - positionPrimaryButtonX < -10) {
                model.plusAlphaY();
                positionPrimaryButtonX = (float) mouseEvent.getX();
            }
            if (mouseEvent.getY() - positionPrimaryButtonY > 10) {
                model.minusAlphaX();
                positionPrimaryButtonY = (float) mouseEvent.getY();
            } else if (mouseEvent.getY() - positionPrimaryButtonY < -10) {
                model.plusAlphaX();
                positionPrimaryButtonY = (float) mouseEvent.getY();
            }
        }
        if (mouseEvent.isSecondaryButtonDown()) {
            if (mouseEvent.getX() - positionSecondaryButtonX > 10) {
                model.addX(-0.5f);
                positionSecondaryButtonX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - positionSecondaryButtonX < -10) {
                model.addX(0.5f);
                positionSecondaryButtonX = (float) mouseEvent.getX();
            }
            if (mouseEvent.getY() - positionSecondaryButtonY > 10) {
                model.addY(-0.5f);
                positionSecondaryButtonY = (float) mouseEvent.getY();
            } else if (mouseEvent.getY() - positionSecondaryButtonY < -10) {
                model.addY(0.5f);
                positionSecondaryButtonY = (float) mouseEvent.getY();
            }
        }
        if (mouseEvent.isControlDown() && mouseEvent.isPrimaryButtonDown()) {
            if (mouseEvent.getX() - positionPrimaryControlX > 10) {
                model.minusAlphaZ();
                positionPrimaryControlX = (float) mouseEvent.getX();
            } else if (mouseEvent.getX() - positionPrimaryControlX < -10) {
                model.plusAlphaZ();
                positionPrimaryControlX = (float) mouseEvent.getX();
            }
        }
    }

    @FXML
    public void focusCanvas(MouseEvent mouseEvent) {
        canvas.requestFocus();
    }

    @FXML
    public void handlerStartDrag(MouseDragEvent mouseDragEvent) {
        if (mouseDragEvent.isPrimaryButtonDown()) {
            positionPrimaryButtonX = (float) mouseDragEvent.getX();
            positionPrimaryButtonY = (float) mouseDragEvent.getY();
        } else if (mouseDragEvent.isSecondaryButtonDown()) {
            positionSecondaryButtonX = (float) mouseDragEvent.getX();
            positionSecondaryButtonY = (float) mouseDragEvent.getY();
        }
    }

    @FXML
    public void handlerTranslateAlongZ(ScrollEvent scrollEvent) {
        ModelSettings model = models.get(0);
        if (scrollEvent.getDeltaY() > 0) {
             model.increaseScale(2);
        } else {
            model.increaseScale(-0.5f);
        }
    }
}
