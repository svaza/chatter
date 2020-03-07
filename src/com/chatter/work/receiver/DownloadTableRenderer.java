 /*
 * =============================================================================
 * DownloadTableRenderer.java
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

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

class DownloadTableRenderer extends JButton
        implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JButton btn = (JButton) value;
        setText(btn.getText());
        setBorder(btn.getBorder());
        setFont(btn.getFont());
        setCursor(btn.getCursor());
        return this;
    }
}
