/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Views;

import de.saadatbaig.visualMemory.Extensions.IdentifiableBlock;
import de.saadatbaig.visualMemory.Utils.Tuple;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;


public class BlockView {

    private VBox _view;
    private ScrollPane _container;
    private GridPane _grid;
    private TextField _inputField;
    private int currentPointer, currentId;

    public BlockView(int sz) {
        currentPointer = 0;
        initGUI(sz);
    }

    public Parent asParent() { return _view; }

    private void initGUI(int size) {
        _container = new ScrollPane();
        _container.setMaxSize(500, 500);
        _container.setPannable(true);

        _grid = new GridPane();
        _grid.setHgap(1);
        _grid.setVgap(2);
        int iditer = 0;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                IdentifiableBlock block = new IdentifiableBlock(iditer, -1, -1);
                block.setOnMousePressed(evt -> {
                    Tuple<Integer, Integer> blockDetails = block.getBlockDetails();
                    if (blockDetails.getFirst() == -1 || blockDetails.getSecond() == -1) { return; }
                    System.out.println("pressed");
                    for (int i = blockDetails.getFirst(); i < blockDetails.getSecond()+1; i++) {
                        String colorCode = ((IdentifiableBlock)_grid.getChildren().get(i)).getColor();
                        _grid.getChildren().get(i).setStyle(String.format("-fx-background-color: %s; -fx-border-color: black", colorCode));
                    }
                });
                block.setOnMouseReleased(evt -> {
                    Tuple<Integer, Integer> blockDetails = block.getBlockDetails();
                    if (blockDetails.getFirst() == -1 || blockDetails.getSecond() == -1) { return; }
                    System.out.println("released");
                    for (int i = blockDetails.getFirst(); i < blockDetails.getSecond()+1; i++) {
                        String colorCode = ((IdentifiableBlock)_grid.getChildren().get(i)).getColor();
                        _grid.getChildren().get(i).setStyle(String.format("-fx-background-color: %s; -fx-border-color: transparent", colorCode));
                    }
                });
                _grid.add(block, col, row);
                iditer++;
            }
        }

        _inputField = new TextField();
        _inputField.setPromptText("Enter command here");
        _inputField.onKeyPressedProperty().set(evt -> {
            if (evt.getCode() == KeyCode.ENTER) {
                String[] components = _inputField.getText().split("\\s+");
                switch (components[0]) {
                    case "new":
                        if (components.length == 2) {
                            Tuple<Integer, String> blockType = getArgumentValue(components[1]);
                            setBlock(blockType);
                            System.out.println("done?");
                        } else if (components.length == 3) {
                            System.out.println("new with 3");
                        }
                        break;
                    case "malloc":
                        if (components.length == 2) {
                            System.out.println("malloc with 2");
                        } else if (components.length == 3) {
                            System.out.println("malloc with 3");
                        }
                        break;
                    case "delete":
                        if (components.length == 2) {
                            System.out.println("del with 2");
                        } else if (components.length == 3) {
                            System.out.println("del with 3");
                        }
                        break;
                    case "read":
                        if (components.length == 2) {
                            System.out.println("read with 2");
                        }
                        break;
                }
            }
        });

        _view = new VBox();
        _view.setSpacing(5);
        _view.setPadding(new Insets(10));

        _container.setContent(_grid);
        _view.getChildren().add(_container);
        _view.getChildren().add(_inputField);
        Platform.runLater(() ->_inputField.requestFocus());
    }

    private void setBlock(Tuple<Integer, String> blockInfo) {
        int startpos = currentPointer;
        int endpos = currentPointer + blockInfo.getFirst() -1;
        for (int i = 0; i < blockInfo.getFirst(); i++) {
            IdentifiableBlock block = (IdentifiableBlock) _grid.getChildren().get(currentPointer);
            block.refreshBlockData(currentId, startpos, endpos, blockInfo.getSecond());
            _grid.getChildren().set(currentPointer, block);
            currentPointer++;
        }
        currentId++;
    }

    private Tuple<Integer, String> getArgumentValue(String s) {
        switch (s) {
            case "char":
                return new Tuple<>(1, "#FF6188");
            case "short":
                return new Tuple<>(2, "#FFD866");
            case "int":
                return new Tuple<>(4, "#A9DC76");
            case "long":
                return new Tuple<>(8, "#78DCE8");
        }
        return null;
    }


    /* End */
}