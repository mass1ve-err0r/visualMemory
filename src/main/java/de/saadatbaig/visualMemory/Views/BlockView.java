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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class BlockView {

    private VBox _view;
    private ScrollPane _container;
    private HBox _info;
    private Label _availableSpace, _pointerLocation;
    private GridPane _grid;
    private TextField _inputField;
    private TextField _outputField;
    private int currentPointer, currentId, _maxSize, _usedSize;
    private String _windowName;

    public BlockView(String name, int sz) {
        _windowName = name;
        currentPointer = 0;
        _maxSize = sz*sz;
        _usedSize = 0;
        initGUI(sz);
    }

    public Parent asParent() { return _view; }

    private void initGUI(int size) {
        _container = new ScrollPane();
        _container.setMaxSize(500, 500);
        _container.setPannable(true);

        _availableSpace = new Label(String.format("Used: %d/%d", _usedSize, _maxSize));
        _availableSpace.fontProperty().set(Font.font(14));
        _pointerLocation = new Label(String.format("Pointer: %d", currentPointer));
        _pointerLocation.fontProperty().set(Font.font(14));
        _info = new HBox(25);
        _info.setAlignment(Pos.CENTER);
        _info.getChildren().addAll(_availableSpace, _pointerLocation);


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

        //TODO: Continue here later with proper input parsing
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
                            System.out.println("new with 2");
                        } else if (components.length == 3) {
                            Tuple<Integer, String> blockType = getArgumentValue(components[1]);
                            setBlock2(blockType, Integer.parseInt(components[2]));
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
                            readBlock(Integer.parseInt(components[1]));
                            System.out.println("read with 2");
                        }
                        break;
                }
            }
        });

        _outputField = new TextField("Last Read:");
        _outputField.setDisable(true);

        _view = new VBox();
        _view.setSpacing(5);
        _view.setPadding(new Insets(10));

        _view.getChildren().add(_info);

        _container.setContent(_grid);
        _view.getChildren().add(_container);
        _view.getChildren().add(_inputField);
        _view.getChildren().add(_outputField);
        Platform.runLater(() ->_inputField.requestFocus());
    }

    private void setBlock(Tuple<Integer, String> blockInfo) {
        int startpos = currentPointer;
        int endpos = currentPointer + blockInfo.getFirst() -1;
        if (endpos > _maxSize-1) {
            showWarning(Alert.AlertType.WARNING, "Memory Failure", "Cannot allocate! We're low on memory!");
            return;
        }
        for (int i = 0; i < blockInfo.getFirst(); i++) {
            IdentifiableBlock block = (IdentifiableBlock) _grid.getChildren().get(currentPointer);
            block.refreshBlockData(currentId, startpos, endpos, blockInfo.getSecond());
            _grid.getChildren().set(currentPointer, block);
            currentPointer++;
        }
        currentId++;
        _usedSize += blockInfo.getFirst();
        _availableSpace.setText(String.format("Used: %d/%d", _usedSize, _maxSize));
        _pointerLocation.setText(String.format("Pointer: %d", currentPointer));
    }

    private void setBlock2(Tuple<Integer, String> blockInfo, int pos) {
        int startpos = pos;
        int endpos = pos + blockInfo.getFirst() -1;
        int alreadyoccupied = 0;
        if (endpos > _maxSize-1) {
            showWarning(Alert.AlertType.WARNING, "Memory Failure", "Cannot allocate! We're low on memory!");
            return;
        }
        for (int i = 0; i < blockInfo.getFirst(); i++) {
            IdentifiableBlock block = (IdentifiableBlock) _grid.getChildren().get(pos);
            if (block.getIdentifier() != -1) { alreadyoccupied++; }
            block.refreshBlockData(currentId, startpos, endpos, blockInfo.getSecond());
            _grid.getChildren().set(pos, block);
            pos++;
        }
        currentId++;
        _usedSize += (blockInfo.getFirst() - alreadyoccupied);
        _availableSpace.setText(String.format("Used: %d/%d", _usedSize, _maxSize));
    }

    private void readBlock(int pos) {
        if (pos > _maxSize-1 || pos < 0) {
            showWarning(Alert.AlertType.ERROR, "Read Failure", "Address is out of bound!");
            setLastRead("Read Error");
            return;
        }
        IdentifiableBlock block = (IdentifiableBlock) _grid.getChildren().get(pos);
        int targetId = block.getIdentifier();
        if (targetId == -1) {
            setLastRead("Empty");
            return;
        }
        int start = block.getStartLocation();
        int end = block.getEndLocation();
        for (int i = start; i < end; i++) {
            block = (IdentifiableBlock) _grid.getChildren().get(i);
            if (targetId != block.getIdentifier()) {
                showWarning(Alert.AlertType.ERROR, "Read Failure", "Block is corrupted! Cannot read original block.");
                setLastRead("Block Error");
                return;
            }
        }
        setLastRead("Success");
    }

    private void setLastRead(String message) {
        _outputField.setText(String.format("Last Read: %s", message));
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

    private void showWarning(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(String.format("%s - Error", _windowName));
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }


    /* End */
}