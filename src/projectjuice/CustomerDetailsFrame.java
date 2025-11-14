package projectjuice;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class CustomerDetailsFrame extends JFrame {

    private JTextField txtName, txtEmail, txtPhone;
    private JButton btnMenu, btnSave, btnEdit, btnDelete, btnBilling;
    private JTable table;
    private DefaultTableModel tableModel;

    public CustomerDetailsFrame() {
        setTitle("Juice Bar POS - Customer Details");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE); // plain white background

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // ===== Title =====
        JLabel lblTitle = new JLabel("🍹 Customer Details 🍹", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI Black", Font.BOLD, 36));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setBounds(0, 30, width, 50);
        add(lblTitle);

        // ===== Customer Fields =====
        JLabel lblName = makeLabel("Name:", 100, 150);
        add(lblName);
        txtName = makeTextField(250, 150);
        add(txtName);

        JLabel lblEmail = makeLabel("Email:", 100, 210);
        add(lblEmail);
        txtEmail = makeTextField(250, 210);
        add(txtEmail);

        JLabel lblPhone = makeLabel("Phone:", 100, 270);
        add(lblPhone);
        txtPhone = makeTextField(250, 270);
        add(txtPhone);

        // ===== Buttons =====
        btnSave = makeButton("💾 Save Details", 100, 330);
        add(btnSave);

        btnMenu = makeButton("🍽 Go to Menu", 300, 330);
        btnMenu.setEnabled(false);
        add(btnMenu);

        // ===== Table =====
        tableModel = new DefaultTableModel(new String[]{"Juice", "Size", "Quantity", "Price"}, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 16));
        table.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(600, 150, 700, 400);
        add(scrollPane);

        // ===== Row Buttons =====
        btnEdit = makeButton("✏️ Edit Row", 600, 570);
        add(btnEdit);

        btnDelete = makeButton("🗑 Delete Row", 820, 570);
        add(btnDelete);

        btnBilling = makeButton("🧾 Total Billing", 1040, 570);
        add(btnBilling);

        // ===== Button Actions =====
        btnSave.addActionListener(e -> {
            if (txtName.getText().isEmpty() || txtEmail.getText().isEmpty() || txtPhone.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
            } else {
                JOptionPane.showMessageDialog(this, "Customer details saved!");
                btnMenu.setEnabled(true);
                btnSave.setEnabled(false);
            }
        });

        btnMenu.addActionListener(e -> {
            new MenuFrame(this, "Cashier");
            setVisible(false);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to edit!");
                return;
            }

            String juice = tableModel.getValueAt(row, 0).toString();
            String size = tableModel.getValueAt(row, 1).toString();
            String qty = tableModel.getValueAt(row, 2).toString();

            JTextField juiceField = new JTextField(juice);
            JTextField sizeField = new JTextField(size);
            JTextField qtyField = new JTextField(qty);

            Object[] fields = {"Juice:", juiceField, "Size:", sizeField, "Quantity:", qtyField};

            int option = JOptionPane.showConfirmDialog(this, fields, "Edit Order", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int newQty = Integer.parseInt(qtyField.getText());
                    tableModel.setValueAt(juiceField.getText(), row, 0);
                    tableModel.setValueAt(sizeField.getText(), row, 1);
                    tableModel.setValueAt(newQty, row, 2);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Quantity must be a number!");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a row to delete!");
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this item?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(row);
                }
            }
        });

        btnBilling.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No items to bill!");
                return;
            }
            String customerName = txtName.getText().trim();
            if (customerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter customer name before billing!");
                return;
            }
            try {
                BillGeneratorFrame.generateBill(customerName, tableModel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error generating bill: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    // ===== Helper methods =====
    private JLabel makeLabel(String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLACK);
        label.setBounds(x, y, 120, 30);
        return label;
    }

    private JTextField makeTextField(int x, int y) {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        field.setBounds(x, y, 250, 30);
        return field;
    }

    private JButton makeButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBounds(x, y, 180, 40);
        btn.setBackground(new Color(255, 140, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void addOrder(String juice, String size, int quantity) {
        double pricePerJuice = 50 + Math.random() * 50;
        double total = pricePerJuice * quantity;
        tableModel.addRow(new Object[]{juice, size, quantity, total});
        setVisible(true);
    }

    public static void main(String[] args) {
        new CustomerDetailsFrame();
    }
}
