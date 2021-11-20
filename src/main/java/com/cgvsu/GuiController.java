package com.cgvsu;

import com.cgvsu.math.vector.Vector3f;
import com.cgvsu.model.MyModel;
import com.cgvsu.myreader.MyObjReader;
import com.cgvsu.obj_writer.ObjWriter;
import javafx.fxml.FXML;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.nio.file.Path;
import java.io.IOException;
import java.io.File;


import com.cgvsu.render_engine.Camera;
import com.cgvsu.render_engine.RenderEngine;

public class GuiController {

    final private float TRANSLATION = 0.5F;

    @FXML
    AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    private MyModel model = null;

    private float percent = 2;
    private float alpha = 30;
    private char token = 'z';
    private Vector3f target = new Vector3f(0,0, 0);

    private Camera camera = new Camera(
            new Vector3f(0, 0, 100),
            new Vector3f(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private Timeline timeline;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.millis(15), event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();

            canvas.getGraphicsContext2D().clearRect(0, 0, width, height);
            camera.setAspectRatio((float) (width / height));

            if (model != null) {
                try {
                    RenderEngine.render(canvas.getGraphicsContext2D(), camera, model, (int) width, (int) height, percent, alpha, target, token);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    for (StackTraceElement el: e.getStackTrace()) {
                        System.out.println(el.toString());
                    }
                }
            }
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    @FXML
    private void onOpenModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog((Stage) canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path fileName = Path.of(file.getAbsolutePath());

        try {
            model = MyObjReader.read(fileName.toString());
            // todo: обработка ошибок
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            System.out.println(exception.getStackTrace());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
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
            ObjWriter.write(model, filename.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage()); //todo сделать нормальные обработки ошибок
        }
    }

    

    @FXML
    public void handleScalePlus(ActionEvent actionEvent) {
        percent += 0.05F;
    }
    @FXML
    public void handleScaleMinus(ActionEvent actionEvent) {
        if (percent > 0.05F) {
            percent -= 0.05F;
        }
    }

    @FXML
    public void handleRotateRight(ActionEvent actionEvent) {
        alpha += 0.1F;
    }

    @FXML
    public void handleRotateLeft(ActionEvent actionEvent) {
        alpha -=0.1F;
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
