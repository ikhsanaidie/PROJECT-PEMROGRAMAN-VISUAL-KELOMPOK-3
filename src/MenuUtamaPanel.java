import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class MenuUtamaPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel timeLabel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;
    private JButton activeButton;
    private JLabel totalSiswaLabel, totalGuruLabel, totalKelasLabel, totalSPPLabel;
    
    public MenuUtamaPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        startClock();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        
        // ============ HEADER ============
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 65));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("🏫");
            logoLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 30));
            logoLabel.setForeground(Color.WHITE);
        }
        logoPanel.add(logoLabel);
        
        JLabel titleLabel = new JLabel("SIAKAD - SMA PGRI 4 JAKARTA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        userPanel.setOpaque(false);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        timeLabel.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("🔄 REFRESH");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        JButton logoutBtn = new JButton("🚪 LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MenuUtamaPanel.this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "login");
            }
        });
        
        userPanel.add(refreshBtn);
        userPanel.add(Box.createHorizontalStrut(10));
        userPanel.add(timeLabel);
        userPanel.add(Box.createHorizontalStrut(15));
        userPanel.add(logoutBtn);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // ============ MAIN CONTENT ============
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(240, 242, 245));
        
        // ============ SIDEBAR ============
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(240, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Profile
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(44, 62, 80));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 20, 15));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel avatarLabel = new JLabel("👨‍💼");
        avatarLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40));
        avatarLabel.setForeground(Color.WHITE);
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(avatarLabel);
        
        JLabel userRoleLabel = new JLabel("Administrator");
        userRoleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        userRoleLabel.setForeground(Color.WHITE);
        userRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(userRoleLabel);
        
        JLabel userNameLabel = new JLabel("SMA PGRI 4 Jakarta");
        userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        userNameLabel.setForeground(new Color(189, 195, 199));
        userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilePanel.add(userNameLabel);
        
        sidebarPanel.add(profilePanel);
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(68, 86, 104));
        sep.setMaximumSize(new Dimension(220, 2));
        sidebarPanel.add(sep);
        sidebarPanel.add(Box.createVerticalStrut(15));
        
        // Menu Items dengan Emoji
        String[][] menuItems = {
            {"📊", "DASHBOARD", "dashboard"},
            {"📁", "MASTER DATA", "master"},
            {"📝", "TRANSAKSI", "transaksi"},
            {"📄", "LAPORAN", "laporan"}
        };
        
        for (String[] item : menuItems) {
            final String target = item[2];
            JButton menuBtn = new JButton(item[0] + "  " + item[1]);
            menuBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 13));
            menuBtn.setBackground(new Color(44, 62, 80));
            menuBtn.setForeground(Color.WHITE);
            menuBtn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            menuBtn.setFocusPainted(false);
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            
            menuBtn.addActionListener(e -> {
                if (activeButton != null) {
                    activeButton.setBackground(new Color(44, 62, 80));
                }
                menuBtn.setBackground(new Color(41, 128, 185));
                activeButton = menuBtn;
                contentCardLayout.show(contentPanel, target);
                if (target.equals("dashboard")) {
                    refreshDashboard();
                }
            });
            
            sidebarPanel.add(menuBtn);
            sidebarPanel.add(Box.createVerticalStrut(5));
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        // ============ CONTENT PANEL ============
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(new Color(240, 242, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Dashboard
        contentPanel.add(createDashboardPanel(), "dashboard");
        
        // Master Data Sub Menu (dengan emoji)
        contentPanel.add(createSubMenuPanel(new String[]{"👨‍🎓 DATA SISWA", "👨‍🏫 DATA GURU", "🏫 DATA KELAS"}, 
                new String[]{"dataSiswa", "dataGuru", "dataKelas"}), "master");
        
        // Transaksi Sub Menu (dengan emoji)
        contentPanel.add(createSubMenuPanel(new String[]{"📝 INPUT NILAI", "📅 ABSENSI SISWA", "💰 PEMBAYARAN SPP"}, 
                new String[]{"inputNilai", "absensi", "pembayaranSPP"}), "transaksi");
        
        // Laporan Sub Menu (dengan emoji)
        contentPanel.add(createSubMenuPanel(new String[]{"📄 LAPORAN SISWA", "📄 LAPORAN GURU", "📊 LAPORAN NILAI", "📋 LAPORAN ABSENSI"}, 
                new String[]{"laporanSiswa", "laporanGuru", "laporanNilai", "laporanAbsensi"}), "laporan");
        
        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // ============ FOOTER ============
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 30));
        
        JLabel footerLabel = new JLabel("© 2026 SMA PGRI 4 Jakarta - Sistem Informasi Akademik Terintegrasi");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerLabel.setForeground(new Color(189, 195, 199));
        footerPanel.add(footerLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createSubMenuPanel(String[] menuNames, String[] targetPanels) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        int cols = menuNames.length == 4 ? 2 : menuNames.length;
        int rows = menuNames.length == 4 ? 2 : 1;
        
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 20, 20));
        gridPanel.setOpaque(false);
        
        for (int i = 0; i < menuNames.length; i++) {
            final String target = targetPanels[i];
            JButton btn = new JButton(menuNames[i]);
            btn.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(41, 128, 185));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(new Color(52, 73, 94));
                }
            });
            
            btn.addActionListener(e -> {
                cardLayout.show(mainPanel, target);
                new Timer(500, evt -> {
                    refreshDashboard();
                    ((Timer) evt.getSource()).stop();
                }).start();
            });
            
            gridPanel.add(btn);
        }
        
        panel.add(gridPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));
        
        JLabel welcomeLabel = new JLabel("🏠 Selamat Datang di SIAKAD");
        welcomeLabel.setFont(new Font("Segoe UI Symbol", Font.BOLD, 24));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        panel.add(welcomeLabel, BorderLayout.NORTH);
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        
        // Buat card satu per satu dan simpan referensi label
        JPanel cardSiswa = createStatCard("👨‍🎓", "Total Siswa", "0", new Color(52, 152, 219));
        JPanel cardGuru = createStatCard("👨‍🏫", "Total Guru", "0", new Color(46, 204, 113));
        JPanel cardKelas = createStatCard("🏫", "Total Kelas", "0", new Color(155, 89, 182));
        JPanel cardSPP = createStatCard("💰", "Total SPP (Lunas)", "Rp 0", new Color(241, 196, 15));
        
        statsPanel.add(cardSiswa);
        statsPanel.add(cardGuru);
        statsPanel.add(cardKelas);
        statsPanel.add(cardSPP);
        
        // Simpan referensi label dengan cara yang benar
        totalSiswaLabel = (JLabel) ((JPanel) cardSiswa.getComponent(1)).getComponent(1);
        totalGuruLabel = (JLabel) ((JPanel) cardGuru.getComponent(1)).getComponent(1);
        totalKelasLabel = (JLabel) ((JPanel) cardKelas.getComponent(1)).getComponent(1);
        totalSPPLabel = (JLabel) ((JPanel) cardSPP.getComponent(1)).getComponent(1);
        
        panel.add(statsPanel, BorderLayout.CENTER);
        
        // Info Panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        JPanel infoBorderPanel = new JPanel(new BorderLayout());
        infoBorderPanel.setBorder(BorderFactory.createTitledBorder(" ℹ️ Informasi Sistem "));
        infoBorderPanel.setBackground(Color.WHITE);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setText("Sistem Informasi Akademik SMA PGRI 4 Jakarta\n\n" +
            "Fitur yang tersedia:\n" +
            "  📁 Master Data (Siswa, Guru, Kelas)\n" +
            "  📝 Transaksi (Input Nilai, Absensi, Pembayaran SPP)\n" +
            "  📊 Laporan (Siswa, Guru, Nilai, Absensi)\n" +
            "  🖨️ Cetak PDF dengan format surat resmi\n\n" +
            "Gunakan menu di samping kiri untuk mengakses fitur.\n\n" +
            "💡 Tips: Klik tombol REFRESH untuk update data terbaru.");
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoBorderPanel.add(infoArea, BorderLayout.CENTER);
        infoPanel.add(infoBorderPanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        refreshDashboard();
        
        return panel;
    }
    
    private JPanel createStatCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 35));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    public void refreshDashboard() {
        SwingUtilities.invokeLater(() -> {
            try {
                int totalSiswa = Database.getAllSiswa().size();
                int totalGuru = Database.getAllGuru().size();
                int totalKelas = Database.getAllKelas().size();
                double totalSPP = Database.getTotalSPP();
                
                NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                String formattedSPP = rupiahFormat.format(totalSPP);
                
                if (totalSiswaLabel != null) totalSiswaLabel.setText(String.valueOf(totalSiswa));
                if (totalGuruLabel != null) totalGuruLabel.setText(String.valueOf(totalGuru));
                if (totalKelasLabel != null) totalKelasLabel.setText(String.valueOf(totalKelas));
                if (totalSPPLabel != null) totalSPPLabel.setText(formattedSPP);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void startClock() {
    Thread clock = new Thread(() -> {
        while (true) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm:ss", new Locale("id", "ID"));
                String time = sdf.format(new java.util.Date());
                SwingUtilities.invokeLater(() -> timeLabel.setText("🕐 " + time));
                Thread.sleep(1000);
            } catch (InterruptedException e) { break; }
        }
    });
    clock.start();
    }
}