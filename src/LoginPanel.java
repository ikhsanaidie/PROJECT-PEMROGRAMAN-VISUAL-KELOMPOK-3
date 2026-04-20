import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private String[] quotes = {
        "Pendidikan adalah senjata paling ampuh untuk mengubah dunia. - Nelson Mandela",
        "Belajar tanpa berpikir tidak ada gunanya, berpikir tanpa belajar itu berbahaya. - Soekarno",
        "Ilmu tanpa agama buta, agama tanpa ilmu lumpuh. - KH. Ahmad Dahlan",
        "Hiduplah seolah engkau mati besok, belajarlah seolah engkau hidup selamanya. - Mahatma Gandhi",
        "Pendidikan bukanlah persiapan untuk hidup, pendidikan adalah kehidupan itu sendiri. - John Dewey",
        "Jadilah manusia yang bermanfaat bagi orang lain. - Ki Hajar Dewantara",
        "Tuntutlah ilmu dari buaian hingga liang lahat. - Nabi Muhammad SAW",
        "Sebaik-baik manusia adalah yang paling bermanfaat bagi orang lain. - Muhammad SAW"
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
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        // Panel Kiri (Form Login) - warna putih
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(10, 10, 10, 10);
        leftGbc.anchor = GridBagConstraints.CENTER;
        
        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("🏫");
            logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));
        }
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.gridwidth = 2;
        leftPanel.add(logoLabel, leftGbc);
        
        // Judul Sekolah
        JLabel schoolName = new JLabel("SMA PGRI 4 JAKARTA");
        schoolName.setFont(new Font("Times New Roman", Font.BOLD, 22));
        schoolName.setForeground(new Color(41, 128, 185));
        leftGbc.gridy = 1;
        leftPanel.add(schoolName, leftGbc);
        
        JLabel systemName = new JLabel("SISTEM INFORMASI AKADEMIK");
        systemName.setFont(new Font("Times New Roman", Font.BOLD, 16));
        systemName.setForeground(new Color(52, 73, 94));
        leftGbc.gridy = 2;
        leftPanel.add(systemName, leftGbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(280, 10));
        separator.setForeground(new Color(200, 200, 200));
        leftGbc.gridy = 3;
        leftPanel.add(separator, leftGbc);
        
        // Username
        leftGbc.gridwidth = 1;
        leftGbc.gridy = 4;
        leftGbc.gridx = 0;
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        leftPanel.add(userIcon, leftGbc);
        
        leftGbc.gridx = 1;
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        leftPanel.add(usernameField, leftGbc);
        
        // Password
        leftGbc.gridy = 5;
        leftGbc.gridx = 0;
        JLabel passIcon = new JLabel("🔒");
        passIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        leftPanel.add(passIcon, leftGbc);
        
        leftGbc.gridx = 1;
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        leftPanel.add(passwordField, leftGbc);
        
        // Role
        leftGbc.gridy = 6;
        leftGbc.gridx = 0;
        JLabel roleIcon = new JLabel("👔");
        roleIcon.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 20));
        leftPanel.add(roleIcon, leftGbc);
        
        leftGbc.gridx = 1;
        roleCombo = new JComboBox<>(new String[]{"Admin TU", "Guru"});
        roleCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        leftPanel.add(roleCombo, leftGbc);
        
        // Login Button
        leftGbc.gridy = 7;
        leftGbc.gridx = 0;
        leftGbc.gridwidth = 2;
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(14, 30, 14, 30));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(41, 128, 185));
            }
        });
        
        leftPanel.add(loginBtn, leftGbc);
        
        // Info Login
        leftGbc.gridy = 8;
        JLabel infoLabel = new JLabel("Username: admin / guru | Password: (bebas)");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(127, 140, 141));
        leftPanel.add(infoLabel, leftGbc);
        
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            if (username.equals("admin") || username.equals("guru")) {
                cardLayout.show(mainPanel, "menuUtama");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Login gagal!\nUsername: admin atau guru", 
                    "Error Login", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // ==================== PANEL KANAN (QUOTES) ====================
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(new Color(41, 128, 185));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(20, 20, 20, 20);
        rightGbc.anchor = GridBagConstraints.CENTER;
        
        // Icon quotes besar
        JLabel quoteIcon = new JLabel("“");
        quoteIcon.setFont(new Font("Times New Roman", Font.BOLD, 100));
        quoteIcon.setForeground(new Color(255, 255, 255, 80));
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.anchor = GridBagConstraints.NORTH;
        rightPanel.add(quoteIcon, rightGbc);
        
        // Label quotes
        quoteLabel = new JLabel();
        quoteLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
        quoteLabel.setForeground(Color.WHITE);
        quoteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        rightGbc.gridy = 1;
        rightGbc.insets = new Insets(0, 30, 20, 30);
        rightPanel.add(quoteLabel, rightGbc);
        
        // Penutup quotes
        JLabel quoteIcon2 = new JLabel("”");
        quoteIcon2.setFont(new Font("Times New Roman", Font.BOLD, 100));
        quoteIcon2.setForeground(new Color(255, 255, 255, 80));
        rightGbc.gridy = 2;
        rightGbc.anchor = GridBagConstraints.SOUTH;
        rightPanel.add(quoteIcon2, rightGbc);
        
        // Garis pemisah di bawah quotes
        JSeparator quoteSeparator = new JSeparator();
        quoteSeparator.setForeground(new Color(255, 255, 255, 100));
        quoteSeparator.setPreferredSize(new Dimension(200, 2));
        rightGbc.gridy = 3;
        rightGbc.insets = new Insets(20, 40, 10, 40);
        rightPanel.add(quoteSeparator, rightGbc);
        
        // Sub teks
        JLabel subText = new JLabel("— SMA PGRI 4 JAKARTA —");
        subText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subText.setForeground(new Color(255, 255, 255, 180));
        rightGbc.gridy = 4;
        rightGbc.insets = new Insets(5, 20, 20, 20);
        rightPanel.add(subText, rightGbc);
        
        // ==================== LAYOUT UTAMA ====================
        // Mengatur panel kiri (form login) dan kanan (quotes) secara horizontal
        setLayout(new GridLayout(1, 2));
        add(leftPanel);
        add(rightPanel);
        
        // Set ukuran preferensi untuk landscape
        setPreferredSize(new Dimension(1100, 550));
    }
    
    private void startQuoteRotation() {
        // Set quote pertama
        quoteLabel.setText("<html><div style='text-align: center; width: 350px;'>" + quotes[0] + "</div></html>");
        
        // Timer untuk ganti quotes setiap 5 detik
        quoteTimer = new Timer(7000, e -> {
            quoteIndex = (quoteIndex + 1) % quotes.length;
            quoteLabel.setText("<html><div style='text-align: center; width: 350px;'>" + quotes[quoteIndex] + "</div></html>");
        });
        quoteTimer.start();
    }
}