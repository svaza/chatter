 /*
 * =============================================================================
 * FileRecieverTableModel.java
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
package com.chatter.work.receiver;

import javax.swing.table.AbstractTableModel;

public class FileRecieverTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    String columnsData[];
    public Object rowData[][];

    public FileRecieverTableModel(String col[], Object row[][]) {
        columnsData = col;
        rowData = row;
    }

    @Override
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

    @Override
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col == 2;
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
}
