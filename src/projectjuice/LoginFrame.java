package projectjuice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel;
    private JLabel lblBackground, lblTitle;
    private JComboBox<String> cmbRole;  // 🔹 Role selector (Admin / Cashier)

    public LoginFrame() {
        setTitle("Juice Bar POS - Login");
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
        lblTitle = new JLabel("🍹 Juice Bar POS 🍹", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 60));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(width / 2 - 300, 50, 600, 70);
        lblBackground.add(lblTitle);

        // Role (Admin/Cashier)
        JLabel lblRole = new JLabel("Login As:");
        lblRole.setFont(new Font("Arial", Font.BOLD, 24));
        lblRole.setForeground(Color.BLACK);
        lblRole.setBounds(width / 2 + 50, 140, 150, 35);
        lblBackground.add(lblRole);

        cmbRole = new JComboBox<>(new String[]{"Admin", "Cashier"});
        cmbRole.setFont(new Font("Arial", Font.PLAIN, 22));
        cmbRole.setBounds(width / 2 + 200, 140, 250, 35);
        lblBackground.add(cmbRole);

        // Username
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Arial", Font.BOLD, 24));
        lblUsername.setBounds(width / 2 + 50, 200, 150, 35);
        lblBackground.add(lblUsername);

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 24));
        txtUsername.setBounds(width / 2 + 200, 200, 250, 35);
        lblBackground.add(txtUsername);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 24));
        lblPassword.setBounds(width / 2 + 50, 270, 150, 35);
        lblBackground.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 24));
        txtPassword.setBounds(width / 2 + 200, 270, 250, 35);
        lblBackground.add(txtPassword);

        // Buttons
        btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 22));
        btnLogin.setBounds(width / 2 + 150, 350, 120, 40);
        btnLogin.setBackground(new Color(255, 140, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(this);
        lblBackground.add(btnLogin);

        btnCancel = new JButton("Cancel");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 22));
        btnCancel.setBounds(width / 2 + 300, 350, 120, 40);
        btnCancel.setBackground(Color.GRAY);
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(this);
        lblBackground.add(btnCancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String username = txtUsername.getText().trim().toLowerCase();
            String password = new String(txtPassword.getPassword()).trim();
            String role = cmbRole.getSelectedItem().toString();

            // Admin credentials
            String adminUser = "admin";
            String adminPass = "admin@123";

            // Cashier credentials
            String cashierUser = "shiva";
            String cashierPass = "shiva@123";

            // ✅ Admin Login
            if (role.equals("Admin")) {
                if (username.equals(adminUser) && password.equals(adminPass)) {
                    JOptionPane.showMessageDialog(this, "Welcome, Admin!");
                    dispose();
                    new MainMenuFrame("Admin"); // Navigate to Admin Dashboard
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!");
                }
            }

            // ✅ Cashier Login
            else if (role.equals("Cashier")) {
                if (username.equals(cashierUser) && password.equals(cashierPass)) {
                    JOptionPane.showMessageDialog(this, "Welcome, Cashier!");
                    dispose();
                    new CustomerDetailsFrame(); // Navigate to Customer Details
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Cashier Credentials!");
                }
            }
        }

        // Cancel Button
        else if (e.getSource() == btnCancel) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
