import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private String[] quotes = {
        "Pendidikan adalah senjata paling ampuh untuk mengubah dunia.",
        "Belajar tanpa berpikir tidak ada gunanya, berpikir tanpa belajar itu berbahaya.",
        "Ilmu tanpa agama buta, agama tanpa ilmu lumpuh.",
        "Hiduplah seolah engkau mati besok, belajarlah seolah engkau hidup selamanya.",
        "Pendidikan bukanlah persiapan untuk hidup, pendidikan adalah kehidupan itu sendiri.",
        "Jadilah manusia yang bermanfaat bagi orang lain.",
        "Tuntutlah ilmu dari buaian hingga liang lahat.",
        "Sebaik-baik manusia adalah yang paling bermanfaat bagi orang lain."
    };
    private JLabel quoteLabel;
    private int quoteIndex = 0;
    private Timer quoteTimer;
    
    public LoginPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        startQuoteRotation();
    }
    
    private void initComponents() {
        setLayout(new GridLayout(1, 2));
        setBackground(new Color(245, 245, 250));
        
        // ============ PANEL KIRI (FORM LOGIN) ============
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("SMA PGRI 4");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            logoLabel.setForeground(new Color(41, 128, 185));
        }
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        leftPanel.add(logoLabel, gbc);
        
        // Judul
        JLabel schoolName = new JLabel("SMA PGRI 4 JAKARTA");
        schoolName.setFont(new Font("Segoe UI", Font.BOLD, 22));
        schoolName.setForeground(new Color(41, 128, 185));
        gbc.gridy = 1;
        leftPanel.add(schoolName, gbc);
        
        JLabel systemName = new JLabel("SISTEM INFORMASI AKADEMIK");
        systemName.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        systemName.setForeground(new Color(120, 120, 120));
        gbc.gridy = 2;
        leftPanel.add(systemName, gbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(280, 10));
        gbc.gridy = 3;
        leftPanel.add(separator, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.gridy = 4;
        gbc.gridx = 0;
        leftPanel.add(new JLabel("Username"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 5;
        gbc.gridx = 0;
        leftPanel.add(new JLabel("Password"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridy = 6;
        gbc.gridx = 0;
        leftPanel.add(new JLabel("Hak Akses"), gbc);
        
        gbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Admin TU", "Guru"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(roleCombo, gbc);
        
        // Login Button
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        leftPanel.add(loginBtn, gbc);
        
        // Info
        gbc.gridy = 8;
        JLabel infoLabel = new JLabel("Username: admin / guru  |  Password: (bebas)");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(150, 150, 150));
        leftPanel.add(infoLabel, gbc);
        
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            if (username.equals("admin") || username.equals("guru")) {
                cardLayout.show(mainPanel, "menuUtama");
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal! Username: admin atau guru", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // ============ PANEL KANAN (QUOTES) ============
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(new Color(41, 128, 185));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints rgc = new GridBagConstraints();
        rgc.insets = new Insets(15, 15, 15, 15);
        
        JLabel quoteIcon1 = new JLabel("\"");
        quoteIcon1.setFont(new Font("Times New Roman", Font.BOLD, 80));
        quoteIcon1.setForeground(new Color(255, 255, 255, 80));
        rgc.gridx = 0;
        rgc.gridy = 0;
        rgc.anchor = GridBagConstraints.NORTH;
        rightPanel.add(quoteIcon1, rgc);
        
        quoteLabel = new JLabel();
        quoteLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
        quoteLabel.setForeground(Color.WHITE);
        quoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rgc.gridy = 1;
        rightPanel.add(quoteLabel, rgc);
        
        JLabel quoteIcon2 = new JLabel("\"");
        quoteIcon2.setFont(new Font("Times New Roman", Font.BOLD, 80));
        quoteIcon2.setForeground(new Color(255, 255, 255, 80));
        rgc.gridy = 2;
        rgc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(quoteIcon2, rgc);
        
        JSeparator lineSep = new JSeparator();
        lineSep.setForeground(new Color(255, 255, 255, 100));
        lineSep.setPreferredSize(new Dimension(200, 2));
        rgc.gridy = 3;
        rightPanel.add(lineSep, rgc);
        
        JLabel subText = new JLabel("— Inspirasi Pendidikan —");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subText.setForeground(new Color(255, 255, 255, 180));
        rgc.gridy = 4;
        rightPanel.add(subText, rgc);
        
        add(leftPanel);
        add(rightPanel);
    }
    
    private void startQuoteRotation() {
        quoteLabel.setText("<html><div style='text-align: center; width: 320px;'>" + quotes[0] + "</div></html>");
        quoteTimer = new Timer(7000, e -> {
            quoteIndex = (quoteIndex + 1) % quotes.length;
            quoteLabel.setText("<html><div style='text-align: center; width: 320px;'>" + quotes[quoteIndex] + "</div></html>");
        });
        quoteTimer.start();
    }
}