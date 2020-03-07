 /*
 * =============================================================================
 * ChatterTableModel.java
 * Copyright (c) 2010 Santosh Vaza.[vazasantosh@gmail.com]
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * =============================================================================
 */
package com.chatter.ui.chatter;

import javax.swing.table.AbstractTableModel;

/**
 * The table model for table in chatter UI.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatterTableModel extends AbstractTableModel {

    String columnsData[];
    Object rowData[][];

    public ChatterTableModel(String col[], Object row[][]) {
        columnsData = col;
        rowData = row;
    }

    public String getColumnName(int col) {
        return columnsData[col].toString();
    }

    public int getRowCount() {
        return rowData.length;
    }

    public int getColumnCount() {
        return columnsData.length;
    }

    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }

    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = (String) value;
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
