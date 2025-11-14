package projectjuice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.ArrayList;

public class MenuFrame extends JFrame implements ActionListener {

    private CustomerDetailsFrame customerFrame;
    private String role;
    private JButton btnAddItem;

    private ArrayList<String> juices = new ArrayList<>();
    private ArrayList<Double> prices = new ArrayList<>();
    private ArrayList<ImageIcon> juiceImages = new ArrayList<>();

    private int[] quantities;
    private JLabel[] qtyLabels;
    private JButton[] plusButtons, minusButtons, addButtons;

    private JPanel mainPanel;
    private JScrollPane scrollPane;

    public MenuFrame(CustomerDetailsFrame customerFrame, String role) {
        this.customerFrame = customerFrame;
        this.role = role;

        setTitle("Juice Bar POS - Menu (" + role + ")");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // ===== Default Juice Data =====
        String[] defaultJuices = {
            "Orange", "Mango", "Apple", "Guava", "Pineapple", "Strawberry",
            "Watermelon", "Kiwi", "Banana", "Grapes", "Papaya", "Lemon",
            "Pomegranate", "Blueberry", "Tender Coconut", "Mixed Fruit"
        };
        double[] defaultPrices = {
            50, 60, 55, 45, 70, 80,
            65, 75, 50, 65, 55, 40,
            85, 90, 60, 100
        };

        String basePath = "C:\\Users\\home\\Desktop\\cjtts\\programs\\eclipseprograms\\juicebar\\assets\\";
        String[] imgNames = {
            "orange.jpg", "mango.jpg", "apple.jpg", "guva.jpg", "pineapple.jpg", "straberry.jpg",
            "watermelon.jpg", "kiwi.jpg", "banana.jpg", "graps.jpeg", "popayya.jpg", "lemon.jpg",
            "pronegenete.jpg", "blueberry.jpg", "coconut.jpg", "mixed.jpg"
        };

        for (int i = 0; i < defaultJuices.length; i++) {
            juices.add(defaultJuices[i]);
            prices.add(defaultPrices[i]);
            juiceImages.add(loadImage(basePath + imgNames[i]));
        }

        setupUI();
    }

    // ===== UI Setup =====
    private void setupUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("🍹 Juice Menu 🍹", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 38));
        lblTitle.setForeground(new Color(255, 102, 0));
        topPanel.add(lblTitle, BorderLayout.CENTER);

        // === Admin Add Item Button ===
        if ("Admin".equalsIgnoreCase(role)) {
            btnAddItem = new JButton("➕ Add New Item");
            btnAddItem.setFont(new Font("Arial", Font.BOLD, 20));
            btnAddItem.setForeground(Color.WHITE);
            btnAddItem.setBackground(new Color(255, 140, 0));
            btnAddItem.setFocusPainted(false);
            btnAddItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAddItem.addActionListener(e -> openAddItemDialog());
            topPanel.add(btnAddItem, BorderLayout.EAST);
        }

        add(topPanel, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        mainPanel = new JPanel(new GridLayout(0, 4, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        scrollPane = new JScrollPane(mainPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        refreshMenuCards();

        // ===== BOTTOM PANEL ===== (Cashier Only)
        if (!"Admin".equalsIgnoreCase(role)) {
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(Color.WHITE);

            JButton btnProceed = createGradientButton("🧾 Proceed to Billing");
            btnProceed.addActionListener(e -> proceedToBilling());
            bottomPanel.add(btnProceed);
            add(bottomPanel, BorderLayout.SOUTH);
        }

        setVisible(true);
    }

    // ===== Load Image =====
    private ImageIcon loadImage(String path) {
        try {
            Image img = ImageIO.read(new File(path));
            if (img != null) {
                return new ImageIcon(img.getScaledInstance(200, 140, Image.SCALE_SMOOTH));
            }
        } catch (Exception e) {
            System.err.println("⚠️ Missing image: " + path);
        }
        return new ImageIcon();
    }

    // ===== Refresh Cards =====
    private void refreshMenuCards() {
        mainPanel.removeAll();

        quantities = new int[juices.size()];
        qtyLabels = new JLabel[juices.size()];
        plusButtons = new JButton[juices.size()];
        minusButtons = new JButton[juices.size()];
        addButtons = new JButton[juices.size()];

        for (int i = 0; i < juices.size(); i++) {
            mainPanel.add(createJuiceCard(i));
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ===== Create Juice Card =====
    private JPanel createJuiceCard(int index) {
        JPanel card = new JPanel(null);
        card.setPreferredSize(new Dimension(250, 350));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(255, 140, 0), 2, true));

        JLabel lblImage = new JLabel(juiceImages.get(index));
        lblImage.setBounds(25, 10, 200, 140);
        card.add(lblImage);

        JLabel lblName = new JLabel(juices.get(index), SwingConstants.CENTER);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblName.setForeground(new Color(255, 102, 0));
        lblName.setBounds(10, 160, 230, 25);
        card.add(lblName);

        JLabel lblPrice = new JLabel("₹" + prices.get(index), SwingConstants.CENTER);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setForeground(new Color(0, 128, 0));
        lblPrice.setBounds(10, 190, 230, 25);
        card.add(lblPrice);

        // === Add quantity & button for both Admin & Cashier ===
        minusButtons[index] = createSimpleButton("-");
        minusButtons[index].setBounds(35, 225, 45, 30);
        minusButtons[index].setActionCommand("minus" + index);
        minusButtons[index].addActionListener(this);
        card.add(minusButtons[index]);

        qtyLabels[index] = new JLabel("0", SwingConstants.CENTER);
        qtyLabels[index].setFont(new Font("Arial", Font.BOLD, 16));
        qtyLabels[index].setBounds(95, 225, 50, 30);
        card.add(qtyLabels[index]);

        plusButtons[index] = createSimpleButton("+");
        plusButtons[index].setBounds(155, 225, 45, 30);
        plusButtons[index].setActionCommand("plus" + index);
        plusButtons[index].addActionListener(this);
        card.add(plusButtons[index]);

        addButtons[index] = createSimpleButton("Add");
        addButtons[index].setFont(new Font("Arial", Font.BOLD, 14));
        addButtons[index].setBounds(60, 270, 120, 35);
        addButtons[index].setActionCommand("add" + index);
        addButtons[index].addActionListener(this);
        card.add(addButtons[index]);

        return card;
    }

    // ===== Add Item Dialog =====
    private void openAddItemDialog() {
        JTextField txtName = new JTextField();
        JTextField txtPrice = new JTextField();
        JTextField txtImagePath = new JTextField();
        JButton btnBrowse = new JButton("Browse");

        btnBrowse.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                txtImagePath.setText(file.getAbsolutePath());
            }
        });

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Juice Name:"));
        panel.add(txtName);
        panel.add(new JLabel("Price (₹):"));
        panel.add(txtPrice);
        panel.add(btnBrowse);
        panel.add(txtImagePath);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Juice Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = txtName.getText().trim();
            String priceStr = txtPrice.getText().trim();
            String imagePath = txtImagePath.getText().trim();

            if (name.isEmpty() || priceStr.isEmpty() || imagePath.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Please fill all fields!");
                return;
            }

            try {
                double price = Double.parseDouble(priceStr);
                juices.add(name);
                prices.add(price);
                juiceImages.add(loadImage(imagePath));

                JOptionPane.showMessageDialog(this, "✅ Added: " + name + " (₹" + price + ")");
                refreshMenuCards();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format!");
            }
        }
    }

    // ===== Proceed to Billing =====
    private void proceedToBilling() {
        boolean added = false;
        for (int i = 0; i < juices.size(); i++) {
            if (quantities[i] > 0 && customerFrame != null) {
                customerFrame.addOrder(juices.get(i), "Medium", quantities[i]);
                added = true;
            }
        }

        if (!added) {
            JOptionPane.showMessageDialog(this, "⚠️ No items selected!");
            return;
        }

        JOptionPane.showMessageDialog(this, "✅ Items added to billing!");
        dispose();
        customerFrame.setVisible(true);
    }

    private JButton createSimpleButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(255, 140, 0));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton createGradientButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 140, 0),
                        getWidth(), getHeight(), new Color(255, 70, 0));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getAscent();
                g2.drawString(getText(), (getWidth() - textWidth) / 2,
                        (getHeight() + textHeight) / 2 - 3);
                g2.dispose();
            }
        };
        btn.setPreferredSize(new Dimension(300, 50));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        for (int i = 0; i < juices.size(); i++) {
            if (cmd.equals("plus" + i)) quantities[i]++;
            else if (cmd.equals("minus" + i) && quantities[i] > 0) quantities[i]--;
            else if (cmd.equals("add" + i)) {
                if (quantities[i] == 0) quantities[i] = 1;
                if (customerFrame != null)
                    customerFrame.addOrder(juices.get(i), "Medium", quantities[i]);
                JOptionPane.showMessageDialog(this, quantities[i] + " x " + juices.get(i) + " added!");
            }
            qtyLabels[i].setText(String.valueOf(quantities[i]));
        }
    }

    public static void main(String[] args) {
        new MenuFrame(null, "Admin"); // test admin layout
    }
}
