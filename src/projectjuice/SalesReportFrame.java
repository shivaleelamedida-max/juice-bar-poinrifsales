package projectjuice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesReportFrame extends JFrame {

    private JTable tblSales;
    private DefaultTableModel model;
    private JLabel lblTotal;

    public SalesReportFrame(String reportType) {
        setTitle("Juice Bar POS - " + reportType.substring(0, 1).toUpperCase() + reportType.substring(1) + " Sales Report");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Table Setup =====
        String[] columns = {"Bill ID", "Customer Name", "Juice Name", "Quantity", "Total Amount", "Date"};
        model = new DefaultTableModel(columns, 0);
        tblSales = new JTable(model);
        tblSales.setFont(new Font("Arial", Font.PLAIN, 16));
        tblSales.setRowHeight(28);
        add(new JScrollPane(tblSales), BorderLayout.CENTER);

        // ===== Footer =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total Sales: ₹0");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        bottomPanel.add(lblTotal);
        add(bottomPanel, BorderLayout.SOUTH);

        // ===== Load Fake Data =====
        loadSalesData(reportType);

        setVisible(true);
    }

    private void loadSalesData(String reportType) {
        // 🕒 Today’s Date
        String todayDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        // 🔹 Fake sales data (for demo)
        Object[][] sampleData = {
            {"B101", "Arun", "Mango Juice", 2, 120.0, todayDate},
            {"B102", "Priya", "Apple Juice", 1, 55.0, todayDate},
            {"B103", "Kiran", "Orange Juice", 3, 150.0, todayDate},
            {"B104", "Divya", "Pineapple Juice", 2, 140.0, todayDate},
            {"B105", "Ravi", "Guava Juice", 1, 45.0, todayDate}
        };

        double totalSales = 0;

        for (Object[] row : sampleData) {
            model.addRow(new Object[]{
                row[0],
                row[1],
                row[2],
                row[3],
                "₹" + row[4],
                row[5]
            });

            totalSales += (double) row[4];
        }

        lblTotal.setText("Total Sales: ₹" + totalSales);
    }

    public static void main(String[] args) {
        new SalesReportFrame("today");
    }
}
