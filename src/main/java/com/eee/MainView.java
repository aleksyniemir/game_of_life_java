package com.eee;

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


public class MainView extends Pane {
    private Simulation simulation;
    private final Canvas canvas;
    private final Affine affine;
    private final CheckBox checkBoxBorder;
    private final Label incorrectInputsLabel;

    private int drawMode = 1; //0 - adding | 1 - erasing | 2 - wall adding
    private double squareSize = 35; // wielkosc kwadratow na planszy

    //zmienne do zmiany rozmiaru
    private final Button setTableSizeButton;
    private final TextField userWidthSize;
    private final TextField userHeightSize;
    private final Label textFieldSeparatorLabel;

    //zmienne do zmiany kolorow
    private final Label rLabel;
    private final Label gLabel;
    private final Label bLabel;
    private final Button setColorButton;
    private final Slider rSlider;
    private final Slider gSlider;
    private final  Slider bSlider;

    //zmienne do gornego rzedu napisow
    private final Button stepButton;
    private final Button restartButton;
    private final Button startButton;
    private final Button stopButton;
    private final Label speedLabel;
    private final Label instructionLabel;
    private final TextField userAnimationSpeed;
    private boolean stopAnimation = false;

    //napis z aktualnym trybem
    private Label promptModeLabel;
    private Label modeLabel;

    //dostep pakietowy po to by w klasie App.java mozna bylo korzystac z tych zmiennych
    int tableWidth = 10;        //ilosc kwadratow w rzedzie
    int tableHeight = 10;       //ilosc kwadratow w kolumnie
    int rUntil = 255;           //czastka koloru RGB
    int gUntil = 255;           //czastka koloru RGB
    int bUntil = 255;           //czastka koloru RGB
    double speedAnimation = 1;  //szybkosc animacji
    String mode = "ADDING";     //tekst z aktualnym trybem wprowadzania


    public MainView() {
        this.checkBoxBorder = new CheckBox("Granica żywa");
        this.stepButton = new Button("step");
        stepButton.setLayoutX(2);
        stepButton.setLayoutY(2);

        //Sekcja napisów wyświetlających aktualny tyrb
        {
            promptModeLabel = new Label("Mode: ");
            modeLabel = new Label(mode);

            checkBoxBorder.setLayoutX(10);
            checkBoxBorder.setLayoutY(355);

            promptModeLabel.setLayoutX(10);
            promptModeLabel.setLayoutY(325);

            modeLabel.setLayoutX(50);
            modeLabel.setLayoutY(325);
        }

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
                stopAnimation = true;
                incorrectInputsLabel.setVisible(false);
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
            instructionLabel = new Label("Ex.  '2' do twice as fast | '0.5' do twice as slow");
            userAnimationSpeed = new TextField();

            restartButton.setLayoutX(50);
            restartButton.setLayoutY(2);

            startButton.setLayoutX(160);
            startButton.setLayoutY(2);

            stopButton.setLayoutX(110);
            stopButton.setLayoutY(2);

            speedLabel.setLayoutX(217);
            speedLabel.setLayoutY(7);

            instructionLabel.setLayoutX(440);
            instructionLabel.setLayoutY(7);

            userAnimationSpeed.setLayoutX(325);
            userAnimationSpeed.setLayoutY(4);
            userAnimationSpeed.setPrefWidth(100);
            userAnimationSpeed.setPromptText("default: 1");

            checkBoxBorder.setOnAction(actionEvent -> {       //actionevent checkboksa odpowiedzialnego za granice
                if(checkBoxBorder.isSelected())
                    simulation.granica=1;
                else
                    simulation.granica=0;
            });

            stopButton.setOnAction(actionEvent -> {
                incorrectInputsLabel.setVisible(false);
                stopAnimation = true;
            });

            restartButton.setOnAction(actionEvent -> {
                incorrectInputsLabel.setVisible(false);
                stopAnimation = true;
                this.simulation.board = new Cell[tableWidth][tableHeight];
                for(int i = 0; i < tableHeight; i++)
                {
                    for(int j = 0; j < tableWidth; j++)
                        this.simulation.board[j][i] = new Cell(0,204,204,204);
                }
                draw(tableWidth, tableHeight);
            });

            startButton.setOnAction(actionEvent -> {
                incorrectInputsLabel.setVisible(false);
                stopAnimation = false;
                if (!userAnimationSpeed.getText().isEmpty())
                    speedAnimation = Double.parseDouble(userAnimationSpeed.getText());

                if (speedAnimation >= 100)
                    speedAnimation = 100;
                if(speedAnimation <= 0)
                    speedAnimation = -speedAnimation;

                Thread animation = new Thread(() -> {
                    incorrectInputsLabel.setVisible(false);

                    while (!stopAnimation) {
                        simulation.step();
                        draw(tableWidth, tableHeight);
                        try {
                            Thread.sleep((long) (1000 / speedAnimation));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });
                animation.start();
            });
        }

        this.stepButton.setOnAction(actionEvent -> {
            if(!stopAnimation)  //jezeli odbywa sie animacja to ją przerwij
                stopAnimation = true;
            else
            {
                simulation.step();
                draw(tableWidth, tableHeight);
            }
            incorrectInputsLabel.setVisible(false);
        });


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
                rLabel, gLabel, bLabel, setColorButton, rSlider, bSlider, gSlider, instructionLabel,
                restartButton, startButton, stopButton, speedLabel, userAnimationSpeed,
                promptModeLabel, modeLabel, checkBoxBorder,
                this.canvas);


        setTableSizeButton.setOnAction(actionEvent -> {
            stopAnimation = true;

            if(!userWidthSize.getText().isEmpty() && !userWidthSize.getText().isEmpty())
            {
                //odczytanie nowych wartosci okna
                tableWidth = Integer.parseInt(userWidthSize.getText());
                if(tableWidth < 1)
                    tableWidth = 0;

                tableHeight = Integer.parseInt(userHeightSize.getText());
                if(tableHeight < 1)
                    tableHeight = 0;

                incorrectInputsLabel.setVisible(false);
            }
            else
                incorrectInputsLabel.setVisible(true);  //przy zlych danych pojawi  sie napis: "zle dane"

            //zmiana rozmiaru planszy do gry
            this.canvas.setWidth(tableWidth * squareSize);
            this.canvas.setHeight(tableHeight * squareSize);
            this.simulation.width = tableWidth;
            this.simulation.height = tableHeight;
            this.simulation.board = new Cell[tableWidth][tableHeight];
            for(int i = 0; i < tableHeight; i++)
                    {
                        for(int j = 0; j < tableWidth; j++)
                        this.simulation.board[j][i] = new Cell(0,204,204,204);
                    }
            draw(tableWidth, tableHeight);
        });

    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.A){
            drawMode = 1;
            mode = "ADDING";
            modeLabel.setText(mode);
        } else if (keyEvent.getCode() == KeyCode.E){
            drawMode = 0;
            mode = "ERASING";
            modeLabel.setText(mode);
        }else if (keyEvent.getCode() == KeyCode.W){
            drawMode = 2;
            mode = "WALL ADDING";
            modeLabel.setText(mode);
        }
    }

    private void handleDraw(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        try {
            Point2D simCoord = this.affine.inverseTransform(mouseX, mouseY);
            int simX = (int) simCoord.getX();
            int simY = (int) simCoord.getY();

            this.simulation.setState(simX, simY, drawMode, rUntil, gUntil, bUntil);
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


        Color  c;  
        for (int x = 0; x < this.simulation.width; x++) {
            for (int y = 0; y < this.simulation.height; y++) {
                c = Color.rgb(this.simulation.board[x][y].r, this.simulation.board[x][y].g, this.simulation.board[x][y].b);
                g.setFill(c);
                g.fillRect(x,y,1,1);
            }
        }

        g.setStroke(Color.GREY);
        g.setLineWidth(0.05);
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x, 0, x, tableHeight);
        }
        for (int y = 0; y <= this.simulation.height; y++) {
            g.strokeLine(0, y, tableWidth, y);
        }
    }
}