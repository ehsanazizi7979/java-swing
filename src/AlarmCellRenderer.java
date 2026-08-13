import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class AlarmCellRenderer extends DefaultTableCellRenderer {

    private java.util.List<Stock> stocks;

    public AlarmCellRenderer(java.util.List<Stock> stocks) {
        this.stocks = stocks;
    }

    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        Stock.AlarmLevel level = stocks.get(row).getAlarmLevel();

        if (!isSelected) {
            switch (level) {
                case CRITICAL: c.setBackground(new Color(255, 100, 100)); break;
                case WARNING:  c.setBackground(new Color(255, 220, 100)); break;
                default:       c.setBackground(Color.WHITE);break;
            }
        }

        return c;
    }
}