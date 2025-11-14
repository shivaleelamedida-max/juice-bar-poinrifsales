package projectjuice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuFrame extends JFrame implements ActionListener {

    private JButton btnMenu, btnTodaySales, btnWeeklySales, btnMonthlySales, btnLogout;
    private JLabel lblBackground, lblTitle;
    private String role; // 🔹 To store whether user is Admin or Cashier

    public MainMenuFrame(String role) {
        this.role = role;

        setTitle("Juice Bar POS - " + role + " Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;

        // Background Image
        ImageIcon bgIcon = new ImageIcon("C:\\Users\\home\\Desktop\\cjtts\\programs\\eclipseprograms\\juicebar\\assets\\juice bar.jpg");
        Image img = bgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        lblBackground = new JLabel(new ImageIcon(img));
        lblBackground.setBounds(0, 0, width, height);
        add(lblBackground);

        // Title
        lblTitle = new JLabel("🍹 " + role + " Dashboard - Shiva Juice Bar 🍹", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 50, width, 60);
        lblBackground.add(lblTitle);

        // Buttons
        btnMenu = createButton("🧃 Manage Juice Menu", width / 2 - 200, 200);
        btnTodaySales = createButton("📅 Today's Sales", width / 2 - 200, 300);
        btnWeeklySales = createButton("📈 Weekly Sales", width / 2 - 200, 400);
        btnMonthlySales = createButton("📊 Monthly Sales", width / 2 - 200, 500);
        btnLogout = createButton("🚪 Logout", width / 2 - 200, 600);

        lblBackground.add(btnMenu);
        lblBackground.add(btnTodaySales);
        lblBackground.add(btnWeeklySales);
        lblBackground.add(btnMonthlySales);
        lblBackground.add(btnLogout);

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 26));
        btn.setBounds(x, y, 400, 60);
        btn.setBackground(new Color(255, 153, 51));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(this);
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnMenu) {
            JOptionPane.showMessageDialog(this, "Opening Juice Menu...");
            dispose();
            new MenuFrame(null, role);
        } 
        else if (e.getSource() == btnTodaySales) {
            JOptionPane.showMessageDialog(this, "Opening Today's Sales Report...");
            dispose();
            new SalesReportFrame("today"); // ✅ Fixed: Opens SalesReportFrame
        } 
        else if (e.getSource() == btnWeeklySales) {
            JOptionPane.showMessageDialog(this, "Opening Weekly Sales Report...");
            dispose();
            new SalesReportFrame("weekly"); // ✅ Fixed
        } 
        else if (e.getSource() == btnMonthlySales) {
            JOptionPane.showMessageDialog(this, "Opening Monthly Sales Report...");
            dispose();
            new SalesReportFrame("monthly"); // ✅ Fixed
        } 
        else if (e.getSource() == btnLogout) {
            JOptionPane.showMessageDialog(this, "Logged Out Successfully!");
            dispose();
            new LoginFrame();
        }
    }

    public static void main(String[] args) {
        new MainMenuFrame("Admin");
    }
}
