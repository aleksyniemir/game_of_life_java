package com.eee;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.util.Duration;

import java.util.concurrent.TimeUnit;


public class MainView extends Pane {
    private Simulation simulation;
    private Canvas canvas;
    private Affine affine;
    private Label incorrectInputsLabel;

    private int drawMode = 1;
    private double squareSize = 35;

    //zmienne do zmiany rozmiaru
    private Button setTableSizeButton;
    private TextField userWidthSize;
    private TextField userHeightSize;
    private Label textFieldSeparatorLabel;

    //zmienne do zmiany kolorow
    private Label rLabel;
    private Label gLabel;
    private Label bLabel;
    private Button setColorButton;
    private Slider rSlider;
    private Slider gSlider;
    private Slider bSlider;

    //zmienne do gornego rzedu napisow
    private Button stepButton;
    private Button restartButton;
    private Button startButton;
    private Button stopButton;
    private Label speedLabel;
    private TextField userAnimationSpeed;
    private boolean stopAnimation = false;



    //dostep pakietowy po to by w klasie App.java mozna bylo korzystac z tych zmiennych
    int tableWidth = 10;
    int tableHeight = 10;
    int rUntil;
    int gUntil;
    int bUntil;
    double speedAnimation;



    public MainView() {
        this.stepButton = new Button("step");
        stepButton.setLayoutX(2);
        stepButton.setLayoutY(2);
        this.stepButton.setOnAction(actionEvent -> {
            if(!stopAnimation)
                stopAnimation = true;
            else
            {
                simulation.step();
                draw(tableWidth, tableHeight);
            }
        });


        //Sekcja odpowiedzialna za zmiane rozmiaru planszy
        {
            setTableSizeButton = new Button("Set size");
            userWidthSize = new TextField();
            userHeightSize = new TextField();
            textFieldSeparatorLabel = new Label("X");
            incorrectInputsLabel = new Label("Złe dane wejściowe!");

            //pole tekstowe Width
            userWidthSize.setPromptText("Width");
            userWidthSize.setAlignment(Pos.CENTER);
            userWidthSize.setLayoutX(10);
            userWidthSize.setLayoutY(40);
            userWidthSize.setPrefWidth(70);

            //pole tekstowe Height
            userHeightSize.setPromptText("Height");
            userHeightSize.setAlignment(Pos.CENTER);
            userHeightSize.setLayoutX(100);
            userHeightSize.setLayoutY(40);
            userHeightSize.setPrefWidth(70);

            //napis rozdzielajacy pola tesktowe
            textFieldSeparatorLabel.setLayoutX(85);
            textFieldSeparatorLabel.setLayoutY(43);

            //napis wyswietlajacy sie podczas bledu
            incorrectInputsLabel.setLayoutX(10);
            incorrectInputsLabel.setLayoutY(107);
            incorrectInputsLabel.setVisible(false);
            incorrectInputsLabel.setTextFill(Color.RED);
            incorrectInputsLabel.setFont(new Font(18));

            //przycisk do zmiany rozmiaru
            setTableSizeButton.setLayoutX(35);
            setTableSizeButton.setPrefWidth(120);
            setTableSizeButton.setLayoutY(75);
        }


        //Sekcja odpowidzialana za zmiane koloru
        {
            rLabel = new Label("R:");
            gLabel = new Label("G:");
            bLabel = new Label("B:");
            setColorButton = new Button("Set color");
            rSlider = new Slider(0,255,255);
            gSlider = new Slider(0,255,255);
            bSlider = new Slider(0,255,255);

            rLabel.setLayoutX(10);
            rLabel.setLayoutY(140);

            gLabel.setLayoutX(10);
            gLabel.setLayoutY(180);

            bLabel.setLayoutX(10);
            bLabel.setLayoutY(220);

            rSlider.setLayoutX(10);
            rSlider.setLayoutY(160);

            gSlider.setLayoutX(10);
            gSlider.setLayoutY(200);

            bSlider.setLayoutX(10);
            bSlider.setLayoutY(240);

            setColorButton.setLayoutX(25);
            setColorButton.setLayoutY(270);
            setColorButton.setPrefWidth(120);
            setColorButton.setOnAction(actionEvent -> {
                rUntil = (int)rSlider.getValue();
                gUntil = (int)gSlider.getValue();
                bUntil = (int)bSlider.getValue();
                System.out.println(rUntil + " " + gUntil + " " + bUntil);
            });
        }

        //Seckja gornego rzedu przysiskow
        {
            restartButton = new Button("Restart");
            startButton = new Button("Start");
            stopButton = new Button("Stop");
            speedLabel = new Label("Speed Animation:");
            userAnimationSpeed = new TextField();

            restartButton.setLayoutX(50);
            restartButton.setLayoutY(2);

            startButton.setLayoutX(160);
            startButton.setLayoutY(2);

            stopButton.setLayoutX(110);
            stopButton.setLayoutY(2);

            speedLabel.setLayoutX(217);
            speedLabel.setLayoutY(6);

            userAnimationSpeed.setLayoutX(325);
            userAnimationSpeed.setLayoutY(4);

            stopButton.setOnAction(actionEvent -> {
                stopAnimation = true;
            });

            restartButton.setOnAction(actionEvent -> {
                stopAnimation = true;
                this.simulation.board = new int[tableWidth][tableHeight];
                draw(tableWidth, tableHeight);
            });

            startButton.setOnAction(actionEvent -> {
                stopAnimation = false;

                //TODO: ZROBIC ODDZIELNY WANTEK I TYLKO JEGO ZATRZYMYWAC SLEEPEM

                while(!stopAnimation)   //dopoki nie zostanie wcisniety przysik step, restart, stop
                {

                }
            });

            //TODO: SPRAWDZIC DLACZEGO NIE DZIALA ZMIANA ROZMIARU JAK ZMIENIAMY PLANSZE NA NIE KWADRAT



        }


        this.canvas = new Canvas(tableWidth * squareSize, tableHeight * squareSize);
        this.canvas.setOnMousePressed(this::handleDraw);
        this.canvas.setOnMouseDragged(this::handleDraw);
        canvas.setLayoutX(182);
        canvas.setLayoutY(40);
        this.setOnKeyPressed(this::onKeyPressed);
        this.affine = new Affine();
        this.affine.appendScale(squareSize, squareSize);
        this.simulation = new Simulation(tableWidth,tableHeight);
        this.getChildren().addAll(this.stepButton, this.userHeightSize, this.userWidthSize,this.textFieldSeparatorLabel, this.setTableSizeButton, this.incorrectInputsLabel,
                rLabel, gLabel, bLabel, setColorButton, rSlider, bSlider, gSlider,
                restartButton, startButton, stopButton, speedLabel, userAnimationSpeed,
                this.canvas);


        setTableSizeButton.setOnAction(actionEvent -> {
            if(!userWidthSize.getText().isEmpty() && !userWidthSize.getText().isEmpty())
            {
                //odczytanie nowych wartosci okna
                tableWidth = Integer.parseInt(userWidthSize.getText());
                tableHeight = Integer.parseInt(userHeightSize.getText());
                incorrectInputsLabel.setVisible(false);
            }
            else
                incorrectInputsLabel.setVisible(true);  //przy zlych danych pojawi  sie napis: "zle dane"

            //zmiana rozmiaru okna
            this.canvas.setWidth(tableWidth * squareSize);
            this.canvas.setHeight(tableHeight * squareSize);
            this.simulation.width = tableWidth;
            this.simulation.height = tableHeight;
            this.simulation.board = new int[tableWidth][tableHeight];
            draw(tableWidth, tableHeight);
        });

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
            draw(tableWidth, tableHeight);
        } catch (NonInvertibleTransformException e) {
            System.out.println("affine inverseTransform fail");
        }
    }

    public void draw(int tableWidth, int tableHeight) {
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
            g.strokeLine(x, 0, x, tableWidth); // jebnac zmienne do wymiarow planszy
        }
        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, tableHeight, y);  // jebnac zmienne do wymiarow planszy
        }
    }
}
