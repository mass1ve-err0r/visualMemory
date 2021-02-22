/*
 * Project: visualMemory
 * Author:  Saadat M. Baig
 * Date:    21.02.21
 * License: BSD-3-Clause-Attribution
 */
package de.saadatbaig.visualMemory.Extensions;

import de.saadatbaig.visualMemory.Utils.Tuple;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;


public class IdentifiableBlock extends Button {

    int _addr, _startBlock, _endBlock, _id;
    String _colorHex;

    public IdentifiableBlock(int addr, int startBlock, int endBlock) {
        super();
        setPrefSize(20, 20);
        setMaxSize(20, 20);
        setMinSize(20,20);
        _id = -1;
        _addr = addr;
        _startBlock = startBlock;
        _endBlock = endBlock;
        setBlockTooltip();
    }

    public void colorize() {
        setStyle(String.format("-fx-background-color: %s", _colorHex));
    }

    public void setBlockTooltip() {
        setTooltip(new Tooltip(String.format("Address: %d\nId: %d\nStart: %d\nEnd: %d", _addr, _id, _startBlock, _endBlock)));
    }

    public void refreshBlockData(int id, int start, int end, String color) {
        _id = id;
        _startBlock = start;
        _endBlock = end;
        _colorHex = color;
        setBlockTooltip();
        colorize();
    }

    public Tuple<Integer, Integer> getBlockDetails() {
        return new Tuple<>(_startBlock, _endBlock);
    }

    public String getColor() { return _colorHex; }


    /* End */
}