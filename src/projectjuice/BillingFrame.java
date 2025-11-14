package projectjuice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillingFrame extends JFrame {

    private double subtotal = 0;
    private double tax = 0;
    private double total = 0;
    private DefaultTableModel model;

    public BillingFrame(String customerName, DefaultTableModel orders) {
        setTitle("Total Billing - Juice Bar POS");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(250, 250, 250));
        setLayout(null);

        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screen.width;
        int height = screen.height;

        JLabel lblTitle = new JLabel("🧾 Billing Summary", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 40));
        lblTitle.setBounds(width / 2 - 300, 30, 600, 60);
        add(lblTitle);

        JLabel lblCustomer = new JLabel("Customer: " + customerName);
        lblCustomer.setFont(new Font("Arial", Font.BOLD, 24));
        lblCustomer.setBounds(50, 120, 600, 30);
        add(lblCustomer);

        // ------------------ Table Setup ------------------
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Juice", "Size", "Quantity", "Price (₹)"});
        JTable billTable = new JTable(model);
        billTable.setFont(new Font("Arial", Font.PLAIN, 18));
        billTable.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(billTable);
        scroll.setBounds(50, 180, width - 120, height - 400);
        add(scroll);

        // ------------------ Calculate Totals ------------------
        subtotal = 0;
        for (int i = 0; i < orders.getRowCount(); i++) {
            Object juice = orders.getValueAt(i, 0);
            Object size = orders.getValueAt(i, 1);
            Object qty = orders.getValueAt(i, 2);
            Object price = orders.getValueAt(i, 3);

            model.addRow(new Object[]{juice, size, qty, price});
            subtotal += Double.parseDouble(price.toString());
        }

        tax = subtotal * 0.05; // 5% GST
        total = subtotal + tax;

        JLabel lblSubtotal = new JLabel("Subtotal: ₹" + String.format("%.2f", subtotal));
        lblSubtotal.setFont(new Font("Arial", Font.PLAIN, 22));
        lblSubtotal.setBounds(width - 400, height - 200, 300, 30);
        add(lblSubtotal);

        JLabel lblTax = new JLabel("GST (5%): ₹" + String.format("%.2f", tax));
        lblTax.setFont(new Font("Arial", Font.PLAIN, 22));
        lblTax.setBounds(width - 400, height - 160, 300, 30);
        add(lblTax);

        JLabel lblTotal = new JLabel("Total: ₹" + String.format("%.2f", total));
        lblTotal.setFont(new Font("Arial", Font.BOLD, 26));
        lblTotal.setForeground(new Color(0, 102, 204));
        lblTotal.setBounds(width - 400, height - 120, 300, 40);
        add(lblTotal);

        // ------------------ Save Bill Button ------------------
        JButton btnSave = new JButton("Save Bill to Oracle DB");
        btnSave.setFont(new Font("Arial", Font.BOLD, 20));
        btnSave.setBounds(width / 2 - 320, height - 120, 300, 45);
        btnSave.setBackground(new Color(0, 102, 204));
        btnSave.setForeground(Color.WHITE);

        // 👉 When clicked: Save to DB and open PDF bill
        btnSave.addActionListener(e -> {
            saveBillToOracle(customerName);

            // Convert table model to list for PDF
            List<String[]> itemList = new ArrayList<>();
            for (int i = 0; i < model.getRowCount(); i++) {
                String juice = model.getValueAt(i, 0).toString();
                String size = model.getValueAt(i, 1).toString();
                String qty = model.getValueAt(i, 2).toString();
                String price = model.getValueAt(i, 3).toString();
                double totalPerItem = Double.parseDouble(price);
                itemList.add(new String[]{juice, qty, price, String.valueOf(totalPerItem)});
            }

            // Generate PDF bill
            BillGeneratorFrame.generateBill(customerName, itemList, total);

            JOptionPane.showMessageDialog(this, "✅ Bill saved and PDF generated successfully!");
        });

        add(btnSave);

        // ------------------ Close Button ------------------
        JButton btnClose = new JButton("Close");
        btnClose.setFont(new Font("Arial", Font.BOLD, 20));
        btnClose.setBounds(width / 2 + 50, height - 120, 150, 45);
        btnClose.setBackground(new Color(0, 120, 0));
        btnClose.setForeground(Color.WHITE);
        btnClose.addActionListener(e -> dispose());
        add(btnClose);

        setVisible(true);
    }

    // ------------------ ORACLE DATABASE CONNECTION ------------------
    private Connection getOracleConnection() throws SQLException, ClassNotFoundException {
        // Load Oracle JDBC Driver
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // Connection settings
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "shiva";
        String pass = "root";

        return DriverManager.getConnection(url, user, pass);
    }

    // ------------------ SAVE BILL TO ORACLE DATABASE ------------------
    private void saveBillToOracle(String customerName) {
        Connection con = null;
        PreparedStatement pst = null;

        try {
            con = getOracleConnection();

            String sql = "INSERT INTO sales (customer_name, juice_name, juice_size, quantity, price, subtotal, tax, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);

            for (int i = 0; i < model.getRowCount(); i++) {
                pst.setString(1, customerName);
                pst.setString(2, model.getValueAt(i, 0).toString()); // Juice
                pst.setString(3, model.getValueAt(i, 1).toString()); // Size
                pst.setInt(4, Integer.parseInt(model.getValueAt(i, 2).toString())); // Quantity
                pst.setDouble(5, Double.parseDouble(model.getValueAt(i, 3).toString())); // Price
                pst.setDouble(6, subtotal);
                pst.setDouble(7, tax);
                pst.setDouble(8, total);
                pst.addBatch();
            }

            int[] results = pst.executeBatch();
            JOptionPane.showMessageDialog(this, "✅ " + results.length + " items saved successfully to Oracle Database!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "❌ Error saving bill: " + ex.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
