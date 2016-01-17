package main.Creation.CourseCreation;

import javax.swing.table.DefaultTableModel;

/**
 * Created by phili on 2016-01-15.
 */
public class MyTableModel extends DefaultTableModel {

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            default:
                return Boolean.class;
        }
    }
}
