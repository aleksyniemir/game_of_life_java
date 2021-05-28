package com.eee;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {

    private Simulation simulation;
    private Button stepButton;
    private Canvas canvas;
    private Affine affine;
    private int drawMode = 1;

    public MainView() {
        this.stepButton = new Button("step");
        this.stepButton.setOnAction(actionEvent -> {
            simulation.step();
            draw();
        });

        this.canvas = new Canvas(450, 450);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        this.setOnKeyPressed(this::onKeyPressed);
        this.affine = new Affine();
        this.affine.appendScale(450 / 15f, 450 / 15f); // jebnac zmienne do wymiarow planszy
        this.getChildren().addAll(this.stepButton, this.canvas);
        this.simulation = new Simulation(15,15); // jebnac zmienne do wymiarow planszy
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.D){
            drawMode = 1;
        } else if (keyEvent.getCode() == KeyCode.E){
            drawMode = 0;
        } // draw mody - dodac 2 dla sciany
    }

    private void handleDraw(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            int simX = (int) simCoord.getX();
            int simY = (int) simCoord.getY();

            this.simulation.setState(simX, simY, drawMode);
            draw();
        } catch (NonInvertibleTransformException e) {
            System.out.println("affine inverseTransform fail");
        }
    }

    public void draw() {
        GraphicsContext g = this.canvas.getGraphicsContext2D();
        g.setTransform(this.affine);

        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,400, 400);


        g.setFill(Color.BLACK);
        for (int x = 0; x < this.simulation.width; x++) {
            for (int y = 0; y < this.simulation.height; y++) {
                if(this.simulation.getState(x,y) == 1) {
                    g.fillRect(x,y,1,1);
                }
            }
        }

        g.setStroke(Color.GREY);
        g.setLineWidth(0.05);
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x, 0, x, 15);
        }
        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, 15, y);
        }
    }
}
