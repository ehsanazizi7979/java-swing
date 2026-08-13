import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.Random;

public class StockApp extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private ScheduledExecutorService executor;
    private Random random = new Random();

    public StockApp() {
        setTitle("Stock Monitor");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildTable();
        setJMenuBar(buildMenuBar());
        startLiveFeed();
    }



    // add this field at the top of the class:
    private List<Stock> stocks;

    private void buildTable() {
        stocks = new ArrayList<Stock>();
        stocks.add(new Stock("IKCO", 1250.0,  12.5,  340000));
        stocks.add(new Stock("MSMI", 3800.0, -45.0,  120000));
        stocks.add(new Stock("FOLD", 8900.0, 200.0,   75000));
        stocks.add(new Stock("TPPH", 4300.0,0.0,  200000));
        stocks.add(new Stock("us30", 43000.0,0.5,  2000000));

        String[] columns = {"Symbol", "Price", "Change", "Volume", "Alarm"};
        tableModel = new DefaultTableModel(columns, 0);

        for (Stock s : stocks) {
            tableModel.addRow(new Object[]{
                    s.getSymbol(),
                    s.getPrice(),
                    s.getChange(),
                    s.getVolume(),
                    s.getAlarmLevel()
            });
        }

        table = new JTable(tableModel);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);
        AlarmCellRenderer renderer = new AlarmCellRenderer(stocks);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executor.shutdown();
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("View");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(StockApp.this, "Refresh coming in Step 2");
            }
        });
        viewMenu.add(refreshItem);

        bar.add(fileMenu);
        bar.add(viewMenu);
        return bar;
    }

    private void startLiveFeed() {
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate((Runnable) new Runnable() {
            public void run() {
                updatePrices();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void updatePrices() {
        for (int i = 0; i < stocks.size(); i++) {
            Stock old = stocks.get(i);
            double delta = (random.nextDouble() - 0.5) * 100;
            double newPrice = old.getPrice() + delta;
            Stock updated = new Stock(old.getSymbol(), newPrice, delta, old.getVolume());
            stocks.set(i, updated);

            final int row = i;
            final Stock s = updated;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    tableModel.setValueAt(s.getPrice(), row, 1);
                    tableModel.setValueAt(s.getChange(), row, 2);
                    tableModel.setValueAt(s.getAlarmLevel(), row, 4);
                    table.repaint();
                }
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new StockApp().setVisible(true);
            }
        });
    }
}