package com.minimalsoft.smsmx.desktop;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/*
 *
 * @author David
 */
public class Cliente_Renderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        setOpaque(isSelected);
        if (value instanceof String) {
            String valor = (String) value;
            if (valor.equals("FAILED")) {
                setBackground(Color.red);
                setOpaque(true);
            }
            if (valor.equals("PENDING")) {
                setBackground(Color.yellow);
                setOpaque(true);
            }
        }
        return this;
    }
}
