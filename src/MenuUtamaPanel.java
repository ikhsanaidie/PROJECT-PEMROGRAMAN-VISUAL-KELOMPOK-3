import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class MenuUtamaPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel timeLabel;
    private JPanel contentPanel;
    private CardLayout contentCardLayout;
    private JButton activeButton;
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JPanel mainContentPanel;
    private JPanel footerPanel;
    private JLabel footerLabel;
    private JLabel footerTimeLabel;
    
    // Dark Mode
    private boolean isDarkMode = false;
    private JButton darkModeBtn;
    
    // Label untuk statistik
    private JLabel totalSiswaLabel;
    private JLabel totalLakiLabel;
    private JLabel totalPerempuanLabel;
    private JLabel totalKelasLabel;
    private JLabel notifikasiLabel;
    
    // Dataset untuk chart
    private DefaultPieDataset pieDataset;
    private DefaultCategoryDataset barDataset;
    private ChartPanel pieChartPanel;
    private ChartPanel barChartPanel;
    
    // Panel-panel asli
    private DataSiswaPanel dataSiswaPanel;
    private DataGuruPanel dataGuruPanel;
    private DataKelasPanel dataKelasPanel;
    private InputNilaiPanel inputNilaiPanel;
    private AbsensiPanel absensiPanel;
    private PembayaranSPPPanel pembayaranSPPPanel;
    private LaporanSIswaPanel laporanSiswaPanel;
    private LaporanGuruPanel laporanGuruPanel;
    private LaporanNilaiPanel laporanNilaiPanel;
    private LaporanAbsensiPanel laporanAbsensiPanel;
    private ProfilSekolahPanel profilSekolahPanel;
    
    public MenuUtamaPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        startClock();
        
        new Timer(500, e -> {
            refreshDashboard();
            ((Timer)e.getSource()).stop();
        }).start();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        
        // ============ HEADER ============
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 65));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 12));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("🏫");
            logoLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
            logoLabel.setForeground(Color.WHITE);
        }
        logoPanel.add(logoLabel);
        
        JLabel titleLabel = new JLabel("SIAKAD - SMA PGRI 4 JAKARTA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        // Right Panel dengan Dark Mode Toggle
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rightPanel.setOpaque(false);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);
        
        // Dark Mode Toggle Button
        darkModeBtn = new JButton("🌙");
        darkModeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        darkModeBtn.setBackground(new Color(52, 73, 94));
        darkModeBtn.setForeground(Color.WHITE);
        darkModeBtn.setFocusPainted(false);
        darkModeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        darkModeBtn.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        darkModeBtn.addActionListener(e -> toggleDarkMode());
        
        JButton refreshBtn = new JButton("🔄");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MenuUtamaPanel.this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "login");
            }
        });
        
        rightPanel.add(timeLabel);
        rightPanel.add(darkModeBtn);
        rightPanel.add(refreshBtn);
        rightPanel.add(logoutBtn);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // ============ MAIN CONTENT ============
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(240, 242, 245));
        
        // ============ SIDEBAR ============
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setPreferredSize(new Dimension(230, 0));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Profile Panel
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));
        profilePanel.setBackground(new Color(44, 62, 80));
        profilePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel avatarLabel = new JLabel("👤");
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
        sidebarPanel.add(Box.createVerticalStrut(10));
        
        // Separator
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(68, 86, 104));
        sep.setMaximumSize(new Dimension(230, 2));
        sidebarPanel.add(sep);
        sidebarPanel.add(Box.createVerticalStrut(15));
        
        // Menu Items
        String[][] menuItems = {
            {"📊", "DASHBOARD", "dashboard"},
            {"🏫", "PROFIL SEKOLAH", "profilSekolah"},
            {"👨‍🎓", "DATA SISWA", "dataSiswa"},
            {"👨‍🏫", "DATA GURU", "dataGuru"},
            {"🏫", "DATA KELAS", "dataKelas"},
            {"📝", "INPUT NILAI", "inputNilai"},
            {"📅", "ABSENSI", "absensi"},
            {"💰", "PEMBAYARAN SPP", "pembayaranSPP"},
            {"📄", "LAPORAN SISWA", "laporanSiswa"},
            {"📄", "LAPORAN GURU", "laporanGuru"},
            {"📊", "LAPORAN NILAI", "laporanNilai"},
            {"📋", "LAPORAN ABSENSI", "laporanAbsensi"}
        };
        
        for (String[] item : menuItems) {
            final String target = item[2];
            JButton menuBtn = new JButton(item[0] + "  " + item[1]);
            menuBtn.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
            menuBtn.setBackground(new Color(44, 62, 80));
            menuBtn.setForeground(Color.WHITE);
            menuBtn.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
            menuBtn.setFocusPainted(false);
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            menuBtn.setMaximumSize(new Dimension(230, 40));
            menuBtn.setPreferredSize(new Dimension(230, 38));
            
            menuBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (activeButton != menuBtn) {
                        menuBtn.setBackground(new Color(52, 73, 94));
                    }
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (activeButton != menuBtn) {
                        menuBtn.setBackground(new Color(44, 62, 80));
                    }
                }
            });
            
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
            sidebarPanel.add(Box.createVerticalStrut(3));
        }
        
        sidebarPanel.add(Box.createVerticalGlue());
        
        // ============ INISIALISASI PANEL ASLI ============
        dataSiswaPanel = new DataSiswaPanel(cardLayout, mainPanel);
        dataGuruPanel = new DataGuruPanel(cardLayout, mainPanel);
        dataKelasPanel = new DataKelasPanel(cardLayout, mainPanel);
        inputNilaiPanel = new InputNilaiPanel(cardLayout, mainPanel);
        absensiPanel = new AbsensiPanel(cardLayout, mainPanel);
        pembayaranSPPPanel = new PembayaranSPPPanel(cardLayout, mainPanel);
        laporanSiswaPanel = new LaporanSIswaPanel(cardLayout, mainPanel);
        laporanGuruPanel = new LaporanGuruPanel(cardLayout, mainPanel);
        laporanNilaiPanel = new LaporanNilaiPanel(cardLayout, mainPanel);
        laporanAbsensiPanel = new LaporanAbsensiPanel(cardLayout, mainPanel);
        profilSekolahPanel = new ProfilSekolahPanel(cardLayout, mainPanel);
        
        // ============ CONTENT PANEL ============
        contentCardLayout = new CardLayout();
        contentPanel = new JPanel(contentCardLayout);
        contentPanel.setBackground(new Color(240, 242, 245));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane dashboardScrollPane = new JScrollPane(createDashboardPanel());
        dashboardScrollPane.setBorder(BorderFactory.createEmptyBorder());
        dashboardScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        contentPanel.add(dashboardScrollPane, "dashboard");
        contentPanel.add(profilSekolahPanel, "profilSekolah");
        contentPanel.add(dataSiswaPanel, "dataSiswa");
        contentPanel.add(dataGuruPanel, "dataGuru");
        contentPanel.add(dataKelasPanel, "dataKelas");
        contentPanel.add(inputNilaiPanel, "inputNilai");
        contentPanel.add(absensiPanel, "absensi");
        contentPanel.add(pembayaranSPPPanel, "pembayaranSPP");
        contentPanel.add(laporanSiswaPanel, "laporanSiswa");
        contentPanel.add(laporanGuruPanel, "laporanGuru");
        contentPanel.add(laporanNilaiPanel, "laporanNilai");
        contentPanel.add(laporanAbsensiPanel, "laporanAbsensi");
        
        mainContentPanel.add(sidebarPanel, BorderLayout.WEST);
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // ============ FOOTER ============
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 32));
        
        footerLabel = new JLabel("SIAKAD SMA PGRI 4 Jakarta");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(189, 195, 199));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        footerPanel.add(footerLabel, BorderLayout.WEST);
        
        footerTimeLabel = new JLabel();
        footerTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerTimeLabel.setForeground(new Color(189, 195, 199));
        footerTimeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        footerPanel.add(footerTimeLabel, BorderLayout.EAST);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        new Timer(1000, e -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            footerTimeLabel.setText(sdf.format(new java.util.Date()));
        }).start();
    }
    
    // ============ DARK MODE TOGGLE METHOD ============
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        
        if (isDarkMode) {
            // === DARK MODE COLORS ===
            setBackground(new Color(30, 30, 35));
            mainContentPanel.setBackground(new Color(30, 30, 35));
            contentPanel.setBackground(new Color(30, 30, 35));
            headerPanel.setBackground(new Color(20, 20, 25));
            sidebarPanel.setBackground(new Color(20, 20, 25));
            
            // Sidebar buttons
            for (Component comp : sidebarPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    comp.setBackground(new Color(20, 20, 25));
                }
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn != activeButton) {
                        btn.setBackground(new Color(20, 20, 25));
                        btn.setForeground(new Color(200, 200, 200));
                    }
                }
            }
            
            // Footer
            footerPanel.setBackground(new Color(20, 20, 25));
            footerLabel.setForeground(new Color(150, 150, 150));
            footerTimeLabel.setForeground(new Color(150, 150, 150));
            
            // Dark mode button
            darkModeBtn.setText("☀️");
            darkModeBtn.setBackground(new Color(241, 196, 15));
            darkModeBtn.setForeground(Color.BLACK);
            
        } else {
            // === LIGHT MODE COLORS ===
            setBackground(new Color(240, 242, 245));
            mainContentPanel.setBackground(new Color(240, 242, 245));
            contentPanel.setBackground(new Color(240, 242, 245));
            headerPanel.setBackground(new Color(41, 128, 185));
            sidebarPanel.setBackground(new Color(44, 62, 80));
            
            // Sidebar buttons
            for (Component comp : sidebarPanel.getComponents()) {
                if (comp instanceof JPanel) {
                    comp.setBackground(new Color(44, 62, 80));
                }
                if (comp instanceof JButton) {
                    JButton btn = (JButton) comp;
                    if (btn != activeButton) {
                        btn.setBackground(new Color(44, 62, 80));
                        btn.setForeground(Color.WHITE);
                    }
                }
            }
            
            // Footer
            footerPanel.setBackground(new Color(52, 73, 94));
            footerLabel.setForeground(new Color(189, 195, 199));
            footerTimeLabel.setForeground(new Color(189, 195, 199));
            
            // Light mode button
            darkModeBtn.setText("🌙");
            darkModeBtn.setBackground(new Color(52, 73, 94));
            darkModeBtn.setForeground(Color.WHITE);
        }
        
        // Refresh
        sidebarPanel.revalidate();
        sidebarPanel.repaint();
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        footerPanel.revalidate();
        footerPanel.repaint();
    }
    
    // ============ DASHBOARD PANEL ============
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));
        
        JPanel card1 = createModernCard("👨‍🎓", "TOTAL SISWA", "0", "Siswa Aktif", new Color(52, 152, 219), new Color(41, 128, 185));
        JPanel card2 = createModernCard("👨", "LAKI-LAKI", "0", "Orang", new Color(46, 204, 113), new Color(39, 174, 96));
        JPanel card3 = createModernCard("👩", "PEREMPUAN", "0", "Orang", new Color(155, 89, 182), new Color(142, 68, 173));
        JPanel card4 = createModernCard("🏫", "TOTAL KELAS", "0", "Kelas Aktif", new Color(241, 196, 15), new Color(243, 156, 18));
        
        totalSiswaLabel = (JLabel) card1.getClientProperty("valueLabel");
        totalLakiLabel = (JLabel) card2.getClientProperty("valueLabel");
        totalPerempuanLabel = (JLabel) card3.getClientProperty("valueLabel");
        totalKelasLabel = (JLabel) card4.getClientProperty("valueLabel");
        
        statsPanel.add(card1);
        statsPanel.add(card2);
        statsPanel.add(card3);
        statsPanel.add(card4);
        panel.add(statsPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        
        pieDataset = new DefaultPieDataset();
        pieDataset.setValue("Laki-laki", 0);
        pieDataset.setValue("Perempuan", 0);
        
        JFreeChart pieChart = ChartFactory.createPieChart("STATISTIK GENDER", pieDataset, true, true, false);
        pieChart.setBackgroundPaint(Color.WHITE);
        PiePlot piePlot = (PiePlot) pieChart.getPlot();
        piePlot.setBackgroundPaint(new Color(245, 245, 250));
        piePlot.setOutlinePaint(null);
        piePlot.setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        piePlot.setSectionPaint("Laki-laki", new Color(46, 204, 113));
        piePlot.setSectionPaint("Perempuan", new Color(155, 89, 182));
        
        pieChartPanel = new ChartPanel(pieChart);
        pieChartPanel.setBorder(BorderFactory.createTitledBorder("📊 STATISTIK GENDER"));
        pieChartPanel.setPreferredSize(new Dimension(450, 350));
        
        barDataset = new DefaultCategoryDataset();
        barDataset.addValue(0, "Siswa", "");
        
        JFreeChart barChart = ChartFactory.createBarChart("STATISTIK PER JURUSAN", "Jurusan", "Jumlah Siswa", barDataset);
        barChart.setBackgroundPaint(Color.WHITE);
        CategoryPlot barPlot = barChart.getCategoryPlot();
        barPlot.setBackgroundPaint(new Color(245, 245, 250));
        barPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        barPlot.getDomainAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        barPlot.getRangeAxis().setLabelFont(new Font("Segoe UI", Font.BOLD, 11));
        
        BarRenderer renderer = (BarRenderer) barPlot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 128, 185));
        
        barChartPanel = new ChartPanel(barChart);
        barChartPanel.setBorder(BorderFactory.createTitledBorder("📈 STATISTIK PER JURUSAN"));
        barChartPanel.setPreferredSize(new Dimension(450, 350));
        
        chartsPanel.add(pieChartPanel);
        chartsPanel.add(barChartPanel);
        panel.add(chartsPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Info Panel
        JPanel infoBorderPanel = new JPanel(new BorderLayout());
        infoBorderPanel.setBorder(BorderFactory.createTitledBorder("ℹ️ INFORMASI SISTEM"));
        infoBorderPanel.setBackground(Color.WHITE);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setText("Sistem Informasi Akademik SMA PGRI 4 Jakarta\n\n" +
            "Fitur yang tersedia:\n" +
            "  • Master Data (Siswa, Guru, Kelas)\n" +
            "  • Transaksi (Input Nilai, Absensi, Pembayaran SPP)\n" +
            "  • Laporan (Siswa, Guru, Nilai, Absensi)\n" +
            "  • Cetak PDF dengan format surat resmi\n" +
            "  • Grafik Dashboard (Statistik Gender & Jurusan)\n\n" +
            "Gunakan menu di samping kiri untuk mengakses fitur.");
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoBorderPanel.add(infoArea, BorderLayout.CENTER);
        panel.add(infoBorderPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // Notifikasi
        JPanel notifPanel = new JPanel(new BorderLayout());
        notifPanel.setBorder(BorderFactory.createTitledBorder("⚠️ NOTIFIKASI PEMBAYARAN SPP"));
        notifPanel.setBackground(new Color(255, 245, 230));
        
        notifikasiLabel = new JLabel("Memuat data tunggakan...");
        notifikasiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        notifikasiLabel.setForeground(new Color(231, 76, 60));
        notifikasiLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        notifPanel.add(notifikasiLabel, BorderLayout.CENTER);
        
        panel.add(notifPanel);
        
        return panel;
    }
    
    private JPanel createModernCard(String icon, String title, String defaultValue, String subtitle, Color startColor, Color endColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, startColor, w, h, endColor);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(2, 5, w, h, 20, 20);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 20, 20);
            }
        };
        
        card.setLayout(new BorderLayout(10, 8));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(25, 18, 25, 18));
        card.setPreferredSize(new Dimension(0, 160));
        
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 40));
        iconLabel.setForeground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        JLabel valueLabel = new JLabel(defaultValue);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subLabel.setForeground(new Color(255, 255, 255, 220));
        
        bottomPanel.add(subLabel);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);
        
        card.putClientProperty("valueLabel", valueLabel);
        
        return card;
    }
    
    public void refreshDashboard() {
        new Thread(() -> {
            try {
                List<String[]> siswaList = Database.getAllSiswa();
                List<String[]> kelasList = Database.getAllKelas();
                
                final int totalSiswa = siswaList.size();
                final int totalKelas = kelasList.size();
                
                int lakiTemp = 0, perempuanTemp = 0;
                for (String[] s : siswaList) {
                    if (s.length > 3 && s[3] != null) {
                        if (s[3].equalsIgnoreCase("Laki-laki")) lakiTemp++;
                        else if (s[3].equalsIgnoreCase("Perempuan")) perempuanTemp++;
                    }
                }
                
                final int laki = lakiTemp;
                final int perempuan = perempuanTemp;
                
                double totalTunggakan = 0;
                int siswaTunggakan = 0;
                for (String[] siswa : siswaList) {
                    String nisn = siswa[0];
                    double tunggakan = Database.getTotalTunggakanSiswa(nisn);
                    if (tunggakan > 0) {
                        totalTunggakan += tunggakan;
                        siswaTunggakan++;
                    }
                }
                
                final int finalSiswaTunggakan = siswaTunggakan;
                final double finalTotalTunggakan = totalTunggakan;
                final NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
                
                SwingUtilities.invokeLater(() -> {
                    if (totalSiswaLabel != null) totalSiswaLabel.setText(String.valueOf(totalSiswa));
                    if (totalLakiLabel != null) totalLakiLabel.setText(String.valueOf(laki));
                    if (totalPerempuanLabel != null) totalPerempuanLabel.setText(String.valueOf(perempuan));
                    if (totalKelasLabel != null) totalKelasLabel.setText(String.valueOf(totalKelas));
                    
                    if (pieDataset != null) {
                        pieDataset.setValue("Laki-laki", laki);
                        pieDataset.setValue("Perempuan", perempuan);
                        if (pieChartPanel != null) pieChartPanel.repaint();
                    }
                    
                    if (barDataset != null) {
                        barDataset.clear();
                        Map<String, Integer> jurusanCount = new HashMap<>();
                        jurusanCount.put("MIPA", 0);
                        jurusanCount.put("IPS", 0);
                        jurusanCount.put("Bahasa", 0);
                        
                        for (String[] siswa : siswaList) {
                            if (siswa.length > 11) {
                                String jurusan = siswa[11];
                                if (jurusan == null || jurusan.isEmpty()) continue;
                                if (jurusan.equalsIgnoreCase("MIPA")) jurusanCount.put("MIPA", jurusanCount.get("MIPA") + 1);
                                else if (jurusan.equalsIgnoreCase("IPS")) jurusanCount.put("IPS", jurusanCount.get("IPS") + 1);
                                else if (jurusan.equalsIgnoreCase("Bahasa")) jurusanCount.put("Bahasa", jurusanCount.get("Bahasa") + 1);
                            }
                        }
                        
                        barDataset.addValue(jurusanCount.get("MIPA"), "Siswa", "MIPA");
                        barDataset.addValue(jurusanCount.get("IPS"), "Siswa", "IPS");
                        barDataset.addValue(jurusanCount.get("Bahasa"), "Siswa", "Bahasa");
                        if (barChartPanel != null) barChartPanel.repaint();
                    }
                    
                    if (notifikasiLabel != null) {
                        if (finalSiswaTunggakan == 0) {
                            notifikasiLabel.setText("✅ Semua siswa sudah lunas! Tidak ada tunggakan pembayaran SPP.");
                            notifikasiLabel.setForeground(new Color(46, 204, 113));
                        } else {
                            notifikasiLabel.setText("⚠️ Terdapat " + finalSiswaTunggakan + " siswa dengan total tunggakan " + rupiahFormat.format(finalTotalTunggakan) + ". Segera lakukan pembayaran!");
                            notifikasiLabel.setForeground(new Color(231, 76, 60));
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    public void showDashboard() {
        contentCardLayout.show(contentPanel, "dashboard");
        refreshDashboard();
    }
    
    private void startClock() {
        Thread clock = new Thread(() -> {
            while (true) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH : mm : ss");
                    String time = sdf.format(new java.util.Date());
                    SwingUtilities.invokeLater(() -> timeLabel.setText(time));
                    Thread.sleep(1000);
                } catch (InterruptedException e) { break; }
            }
        });
        clock.start();
    }
}