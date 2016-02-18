package main.DataStore;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

/**
 * Created by phili on 2016-02-18.
 */
public class MyJTable extends JTable {

    private int columnStart;
    private int columnEnd;
    private ArrayList<Integer> singleRowArray;

    public MyJTable(TableModel dm, int columnStart, int columnEnd, ArrayList<Integer> singleRowArray) {
        super(dm);
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
        this.singleRowArray = singleRowArray;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column >= columnStart && column <= columnEnd || singleRowArray.contains(column)) {
            return false;
        }
        return super.isCellEditable(row, column);
    }

}
