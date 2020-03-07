 /*
 * =============================================================================
 * AboutChatter.java
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
package com.chatter.ui.about;

import com.chatter.core.Util;
import com.chatter.starter.Chatter;
import com.chatter.ui.chatter.ChatterUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Provides the information about Application, author and license
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public final class AboutChatter {

    private Font fontBtn;
    JDialog frmAbout;

    public AboutChatter() {
    }

    public void show() {
        getFrmAbout().pack();
        getFrmAbout().toFront();
        getFrmAbout().setTitle((new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" ").append("1.2").toString());
        getFrmAbout().setLocationRelativeTo(null);
        getFrmAbout().setResizable(true);
        getFrmAbout().setVisible(true);
        getFrmAbout().setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
        getFrmAbout().setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    private JDialog getFrmAbout() {
        if (frmAbout == null) {
            fontBtn = new Font("segoe ui", 0, 12);
            JLabel hlab = new JLabel();
            JButton btnLis = new JButton();
            btnLis.setText("License");
            btnLis.setBorder(getBtnBorder());
            btnLis.setCursor(new Cursor(12));
            btnLis.setFont(fontBtn);
            btnLis.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    StringBuilder strLis = new StringBuilder();
                    strLis.append("<html>");
                    strLis.append("Chatter-A Programme for chatting and file transfer over intranet").append("<br/>");
                    strLis.append("Copyright (c) 2010 Santosh Jagdish Vaza &lt;vazasantosh@gmail.com&gt;").append("<br/>").append("<br/>");
                    strLis.append("This programme is free software; you can redistribute it and/or").append("<br/>");
                    strLis.append("modify it under the terms of the GNU General Public License").append("<br/>");
                    strLis.append("as published by the Free Software Foundation; either version2").append("<br/>");
                    strLis.append("of the License, or(at your option) any later version.").append("<br/>").append("<br/>");
                    strLis.append("This programme is distributed in the hope that it will be useful,").append("<br/>");
                    strLis.append("but WITHOUT ANY WARRANTY; without even the implied warranty of").append("<br/>");
                    strLis.append("MERCHANTABLITY or FITNESS FOR A PARTICULAR PURPOSE. See the").append("<br/>");
                    strLis.append("GNU General Public Lisence for more details.").append("<br/>").append("<br/>");
                    strLis.append("You should have recieved a copy of the GNU General Public License ").append("<br/>");
                    strLis.append("along with this program; if not, write to the Free Software").append("<br/>");
                    strLis.append("Foundation, Inc., 59 Temple Place-Suite 330, Boston, MA 02111-1307, USA.").append("<br/>");
                    JLabel lab = Chatter.getOptionLabel(strLis.toString());
                    lab.setForeground(Color.black);
                    Util.showInformationDialog(frmAbout, true, lab, "Message", false);
                }
            });
            StringBuilder hstr = new StringBuilder();
            hstr.append("<html>");
            hstr.append("<table border=\"0\" bgcolor=\"#EEEEEE\">");
            hstr.append("<tr>");
            hstr.append("<td colspan=\"2\" align=\"center\">");
            hstr.append("<font face=\"segoe ui\" size=\"5\" color=\"#FC2C2C\"><i><u>").append(Chatter.getApplicationProperty("appln.name")).append("&nbsp;1.2</u></i><sup><font size=\"2\">&reg;</font></sup></font>");
            hstr.append("</td>");
            hstr.append("</tr>");
            hstr.append("<tr>");
            hstr.append("<td align=\"left\">");
            hstr.append("<font face=\"segoe ui\" size=\"3\" color=\"black\"><u>Developed By</u></font>");
            hstr.append("</td>");
            hstr.append("<td align=\"left\">");
            hstr.append("<font face=\"segoe ui\" size=\"3\" color=\"black\"><i>Santosh Jagdish Vaza</i></font>");
            hstr.append("</td>");
            hstr.append("</tr>");
            hstr.append("<tr>");
            hstr.append("<td align=\"left\">");
            hstr.append("<font face=\"segoe ui\" size=\"3\" color=\"black\"><u>Project Source</u></font>");
            hstr.append("</td>");
            hstr.append("<td align=\"left\">");
            hstr.append("<font face=\"segoe ui\" size=\"3\" color=\"black\"><i>").append(Chatter.getApplicationProperty("project.source")).append("</i></font>");
            hstr.append("</td>");
            hstr.append("</tr>");
            hstr.append("<tr>");
            hstr.append("<td colspan=\"2\">");
            hstr.append("<hr>");
            hstr.append("</td>");
            hstr.append("</tr>");
            hstr.append("</table>");
            hlab.setText(hstr.toString());
            Box bxbtn = Box.createHorizontalBox();
            bxbtn.add(Box.createHorizontalGlue());
            bxbtn.add(btnLis);
            bxbtn.add(Box.createHorizontalStrut(5));
            Box bxmain = Box.createHorizontalBox();
            bxmain.add(hlab);
            Box all = Box.createVerticalBox();
            all.add(bxmain);
            all.add(Box.createVerticalStrut(2));
            all.add(bxbtn);
            all.add(Box.createVerticalStrut(2));
            frmAbout = new JDialog();
            frmAbout.add(all);
        }
        return frmAbout;
    }

    private Border getBtnBorder() {
        return BorderFactory.createEtchedBorder(1);
    }
}
