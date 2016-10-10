/**
 * Created by Oleksandr on 26.08.2016.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class MainScene extends Application {

    GraphicsContext graphicsContext;
    Square auxSquare = new Square();
    Techniq techniq = new Techniq();

    int SIZE = Grid.getGrid().getSIZE();
    int cellSize = 30;
    double fontSize = 12;
    int startCell = 5;
    double lastSelectedRow = 0;
    double lastSelectedColumn = 0;
    double currentSelectedRow = lastSelectedRow;
    double currentSelectedColumn = lastSelectedColumn;

    ValueSetter valueSetter = new ValueSetter(this);
    Solver solver = new Solver(valueSetter);
    BruteSolver bruteSolver = new BruteSolver(valueSetter);
    SudokuCreator sudokuCreator = new SudokuCreator(valueSetter);
    CellOperator cellOperator = new CellOperator();
    Saver saver = new Saver();

    Stage errorStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        javafx.scene.image.Image sudokuImage = new Image("pencil.png");
        primaryStage.setTitle("Sudoku v2.0");
        primaryStage.getIcons().add(sudokuImage);
        GridPane rootNode = new GridPane();
        FlowPane gridPane = new FlowPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        FlowPane buttonPane = new FlowPane(50, 20);
        buttonPane.setAlignment(Pos.TOP_CENTER);
        Scene scene = new Scene(rootNode, cellSize * SIZE + 40, cellSize * SIZE + 120);
        primaryStage.setScene(scene);

        Canvas gridCanvas = new Canvas(cellSize * SIZE + 10, cellSize * SIZE + 10);

        graphicsContext = gridCanvas.getGraphicsContext2D();

        Button solveButton = new Button("Solve");
        solveButton.setPrefSize(50, 20);
        Button sudokuButton = new Button("Sudoku");
        solveButton.setPrefSize(50, 20);
        Button saveButton = new Button("Save");
        saveButton.setPrefSize(50, 20);
        Button clearButton = new Button("Clear");
        clearButton.setPrefSize(50, 20);
        Button exitButton = new Button("Exit");
        exitButton.setPrefSize(50, 20);
        Tooltip possibleValuesPopup = new Tooltip();

        gridPane.getChildren().add(gridCanvas);

        buttonPane.getChildren().add(solveButton);
        buttonPane.getChildren().add(sudokuButton);
        buttonPane.getChildren().add(exitButton);

        javafx.scene.control.MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("_File");
        MenuItem saveMenu = new MenuItem("Save");
        MenuItem openMenu = new MenuItem("Open");
        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        MenuItem exitMenu = new MenuItem("Exit");
        fileMenu.getItems().addAll(saveMenu, openMenu, separatorMenuItem, exitMenu);
        saveMenu.setAccelerator(KeyCombination.keyCombination("shortcut+S"));
        exitMenu.setAccelerator(KeyCombination.keyCombination("shortcut+E"));

        Menu optionsMenu = new Menu("_Options");
        MenuItem solveMenu = new MenuItem("Solve");
        MenuItem sudokuMenu = new MenuItem("Sudoku");
        MenuItem clearMenu = new MenuItem("Clear");
        optionsMenu.getItems().addAll(solveMenu, sudokuMenu, clearMenu);
        solveMenu.setAccelerator(KeyCombination.keyCombination("shortcut+F"));
        sudokuMenu.setAccelerator(KeyCombination.keyCombination("shortcut+N"));
        clearMenu.setAccelerator(KeyCombination.keyCombination("shortcut+D"));

        Menu helpMenu = new Menu("_Help");
        MenuItem aboutMenu = new MenuItem("About");
        helpMenu.getItems().addAll(aboutMenu);

        menuBar.getMenus().addAll(fileMenu, optionsMenu, helpMenu);

        rootNode.setMargin(gridPane, new Insets(10));
        rootNode.setMargin(buttonPane, new Insets(10));

        rootNode.add(menuBar, 0, 0);
        rootNode.add(gridPane, 0, 1);
        rootNode.add(buttonPane, 0, 2);

        primaryStage.show();

        valueSetter.initiateGrid();

        for (int i = 0; i < (SIZE + 1) * cellSize; i += cellSize) {
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeLine(startCell, startCell + i, startCell + SIZE * cellSize, startCell + i);
            graphicsContext.strokeLine(startCell + i, startCell, startCell + i, startCell + SIZE * cellSize);
        }

        graphicsContext.setFont(new Font(fontSize));

        gridCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentSelectedRow = Math.floor((event.getY() - startCell) / cellSize);
                currentSelectedColumn = Math.floor((event.getX() - startCell) / cellSize);
                if (currentSelectedRow >= 0 && currentSelectedColumn >= 0) {
                    drawSquare(currentSelectedRow, currentSelectedColumn);
                }
                possibleValuesPopup.hide();
                if (event.isSecondaryButtonDown()) {
                    if (Grid.getGrid().getCells().get((int) (currentSelectedRow * Grid.getGrid().getSIZE() + currentSelectedColumn)) == 0) {
                        possibleValuesPopup.setText(String.valueOf(cellOperator.oneCellInOneGroupPossibleValues((int) (currentSelectedRow * Grid.getGrid().getSIZE() + currentSelectedColumn))));
                        possibleValuesPopup.show(gridCanvas, event.getScreenX() + 5, event.getScreenY() + 5);
                    }
                }
            }
        });

        rootNode.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                currentSelectedRow = lastSelectedRow;
                currentSelectedColumn = lastSelectedColumn;
                if (keyEvent.getCode() == KeyCode.RIGHT && currentSelectedColumn < SIZE - 1) {
                    currentSelectedColumn++;
                } else if (keyEvent.getCode() == KeyCode.LEFT && currentSelectedColumn > 0) {
                    currentSelectedColumn--;
                } else if (keyEvent.getCode() == KeyCode.DOWN && currentSelectedRow < SIZE - 1) {
                    currentSelectedRow++;
                } else if (keyEvent.getCode() == KeyCode.UP && currentSelectedRow > 0) {
                    currentSelectedRow--;
                } else if (keyEvent.getCode() == KeyCode.DELETE) {
                    fillSquare(currentSelectedRow, currentSelectedColumn);
                    Grid.getGrid().getCells().put((int) (currentSelectedRow * Grid.getGrid().getSIZE() + currentSelectedColumn), 0);
                }
                drawSquare(currentSelectedRow, currentSelectedColumn);
            }
        });

        scene.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                try {
                    if (Integer.parseInt(event.getCharacter()) > 0 && Integer.parseInt(event.getCharacter()) < 10) {
                        drawNumber(event.getCharacter(), currentSelectedRow, currentSelectedColumn);
                        Grid.getGrid().getCells().put((int) (currentSelectedRow * Grid.getGrid().getSIZE() + currentSelectedColumn), Integer.parseInt(event.getCharacter()));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("should be number");
                }
            }
        });

        EventHandler<ActionEvent> solveEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                long startTime = System.currentTimeMillis();
                bruteSolver.clearStacks();
                bruteSolver.updateStack();
                bruteSolver.setCheckSum(0);
                bruteSolver.setSolveProcess(true);
                solver.simpleCheck();
                //solver.simpleCheck2();
                bruteSolver.bruteGuess();
                long endTime = System.currentTimeMillis();
                valueSetter.initiateGrid();
            }
        };

        solveButton.setOnAction(solveEvent);
        solveMenu.setOnAction(solveEvent);

        EventHandler<ActionEvent> sudokuEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                valueSetter.clearGrid();
                bruteSolver.setCheckSum(0);
                bruteSolver.setCellToCheck(0);
                bruteSolver.setValueToCheck(0);
                valueSetter.initiateGrid();

                bruteSolver.setSolveProcess(false);
                bruteSolver.bruteGuess();

                sudokuCreator.createGrid();
                bruteSolver.setCheckSum(0);
                bruteSolver.setCellToCheck(0);
                bruteSolver.setValueToCheck(0);
                valueSetter.initiateGrid();
            }
        };

        sudokuButton.setOnAction(sudokuEvent);
        sudokuMenu.setOnAction(sudokuEvent);

        EventHandler<ActionEvent> saveEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saver.save();
            }
        };

        saveButton.setOnAction(saveEvent);
        saveMenu.setOnAction(saveEvent);

        EventHandler<ActionEvent> openEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                saver.open();
                valueSetter.initiateGrid();
            }
        };

        openMenu.setOnAction(openEvent);

        EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i = 0; i < SIZE * SIZE; i++) {
                    Grid.getGrid().getCells().put(i, 0);
                }
                Grid.getGrid().getCellPossibleValues().clear();
                valueSetter.initiateGrid();
                bruteSolver.clearStacks();
                bruteSolver.setCheckSum(0);
                bruteSolver.setCellToCheck(0);
                bruteSolver.setValueToCheck(0);
                BruteSolver.bruteCounter = 0;
                Solver.countSimple = 0;
                Solver.countUniquer = 0;
            }
        };

        clearButton.setOnAction(clearEvent);
        clearMenu.setOnAction(clearEvent);

        EventHandler<ActionEvent> exitEvent = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        };

        exitButton.setOnAction(exitEvent);
        exitMenu.setOnAction(exitEvent);

        Stage aboutStage = new Stage();
        aboutStage.setTitle("About");
        FlowPane aboutPane = new FlowPane(20, 50);
        aboutPane.setAlignment(Pos.CENTER);
        Label aboutLabel = new Label("Created on Java 8. \nIntelliJ IDEA 15\n(c) pavlovfontanka@gmail.com\n2016");
        Button aboutExit = new Button("Close");
        Scene aboutScene = new Scene(aboutPane, 200, 160);
        aboutStage.setScene(aboutScene);

        aboutPane.getChildren().add(aboutLabel);
        aboutPane.getChildren().add(aboutExit);

        aboutMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutStage.show();
            }
        });

        aboutExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutStage.hide();
            }
        });

        errorStage = new Stage();
        errorStage.setTitle("Error");
        GridPane errorPane = new GridPane();
        errorPane.setAlignment(Pos.CENTER);
        Label errorLabel = new Label("Error!\nNo solution.\n\n\n\n");
        Button errorExit = new Button("Close");
        Scene errorScene = new Scene(errorPane, 200, 160);
        errorStage.setScene(errorScene);

        errorPane.add(errorLabel, 0, 0);
        errorPane.add(errorExit, 0, 1);

        errorExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorStage.hide();
            }
        });


    }

    public Stage getErrorStage() {
        return errorStage;
    }

    void drawNumber(String number, double selectedRow, double selectedColumn) {
        fillSquare(selectedRow, selectedColumn);
        if (Integer.parseInt(number) > 0) {
            graphicsContext.setStroke(Color.BLACK);
            graphicsContext.strokeText(number, startCell + selectedColumn * cellSize + fontSize, startCell + selectedRow * cellSize + fontSize + cellSize / 4);
        }
    }

    private void drawSquare(double row, double column) {
        graphicsContext.setStroke(Color.BLACK);
        graphicsContext.strokeRect(startCell + lastSelectedColumn * cellSize, startCell + lastSelectedRow * cellSize, cellSize, cellSize);
        graphicsContext.setStroke(Color.MAGENTA);
        graphicsContext.strokeRect(startCell + column * cellSize, startCell + row * cellSize, cellSize, cellSize);
        lastSelectedRow = row;
        lastSelectedColumn = column;
    }

    public void fillSquare(double row, double column) {
        double shiftFill = auxSquare.getSquareId((int) (row * SIZE + column));
        if (shiftFill % 2 == 0) {
            graphicsContext.setFill(Color.WHITE);
        } else {
            graphicsContext.setFill(Color.LIGHTBLUE);
        }
        graphicsContext.fillRect(startCell + column * cellSize + 1, startCell + row * cellSize + 1, cellSize - 2, cellSize - 2);
    }

    public BruteSolver getBruteSolver() {
        return bruteSolver;
    }

}

