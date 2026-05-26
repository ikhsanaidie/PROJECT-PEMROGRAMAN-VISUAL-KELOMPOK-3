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
    
    private JLabel totalSiswaLabel;
    private JLabel totalLakiLabel;
    private JLabel totalPerempuanLabel;
    private JLabel totalKelasLabel;
    private JLabel notifikasiLabel;
    
    private DefaultPieDataset pieDataset;
    private DefaultCategoryDataset barDataset;
    private ChartPanel pieChartPanel;
    private ChartPanel barChartPanel;
    
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
    private CetakRaportPanel cetakRaportPanel;
    private KalenderAkademikPanel kalenderAkademikPanel;
    
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
        
        // HEADER
        JPanel headerPanel = new JPanel(new BorderLayout());
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
            logoLabel.setText("SMA");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            logoLabel.setForeground(Color.WHITE);
        }
        logoPanel.add(logoLabel);
        
        JLabel titleLabel = new JLabel("SIAKAD - SMA PGRI 4 JAKARTA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        logoPanel.add(titleLabel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 12));
        userPanel.setOpaque(false);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> refreshDashboard());
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(MenuUtamaPanel.this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Session.clear();
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
        
        // MAIN CONTENT
        JPanel mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.setBackground(new Color(240, 242, 245));
        
        // SIDEBAR
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(44, 62, 80));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        JScrollPane sidebarScrollPane = new JScrollPane(sidebarPanel);
        sidebarScrollPane.setBorder(BorderFactory.createEmptyBorder());
        sidebarScrollPane.setBackground(new Color(44, 62, 80));
        sidebarScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        sidebarScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScrollPane.setPreferredSize(new Dimension(230, 0));
        
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

// Ambil role dan nama dari Session
String roleText = "Guest";
String namaText = "SMA PGRI 4 Jakarta";

if (Session.getRole() != null) {
    if (Session.getRole().equals("admin")) {
        roleText = "Administrator";
    } else if (Session.getRole().equals("guru")) {
        roleText = "Guru";
    } else if (Session.getRole().equals("kepsek")) {
        roleText = "Kepala Sekolah";
    } else {
        roleText = Session.getRole();
    }
}
if (Session.getNama() != null && !Session.getNama().isEmpty()) {
    namaText = Session.getNama();
}

JLabel userRoleLabel = new JLabel(roleText);
userRoleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
userRoleLabel.setForeground(Color.WHITE);
userRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
profilePanel.add(userRoleLabel);

JLabel userNameLabel = new JLabel(namaText);
userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
userNameLabel.setForeground(new Color(189, 195, 199));
userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
profilePanel.add(userNameLabel);

sidebarPanel.add(profilePanel);
sidebarPanel.add(Box.createVerticalStrut(10));
        
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(68, 86, 104));
        sep.setMaximumSize(new Dimension(230, 2));
        sidebarPanel.add(sep);
        sidebarPanel.add(Box.createVerticalStrut(15));
        
        // MENU ITEMS - ALL
        String[][] allMenuItems = {
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
            {"📋", "LAPORAN ABSENSI", "laporanAbsensi"},
            {"📑", "CETAK RAPORT", "cetakRaport"},
            {"📅", "KALENDER AKADEMIK", "kalenderAkademik"}
        };
        
       // FILTER MENU BERDASARKAN ROLE
java.util.ArrayList<String[]> filteredMenu = new java.util.ArrayList<>();
String role = Session.getRole();

System.out.println("=== MENU UTAMA ===");
System.out.println("Session Role: " + role);

if (role == null) {
    System.out.println("Role NULL - Tampilkan menu terbatas");
    for (String[] item : allMenuItems) {
        if (item[1].equals("DASHBOARD") || item[1].equals("PROFIL SEKOLAH")) {
            filteredMenu.add(item);
        }
    }
} else if (role.equals("admin")) {
    System.out.println("Role ADMIN - Tampilkan SEMUA menu");
    for (String[] item : allMenuItems) {
        filteredMenu.add(item);
        System.out.println("  + " + item[1]);
    }
} else if (role.equals("guru")) {
    System.out.println("Role GURU - Tampilkan menu terbatas");
    for (String[] item : allMenuItems) {
        String menuName = item[1];
        if (!menuName.equals("DATA GURU") && !menuName.equals("DATA KELAS") && 
            !menuName.equals("PEMBAYARAN SPP") && !menuName.equals("LAPORAN GURU")) {
            filteredMenu.add(item);
            System.out.println("  + " + item[1]);
        }
    }
} else if (role.equals("kepsek")) {
    System.out.println("Role KEPSEK - Tampilkan laporan");
    for (String[] item : allMenuItems) {
        String menuName = item[1];
        if (menuName.equals("DASHBOARD") || menuName.equals("PROFIL SEKOLAH") ||
            menuName.equals("LAPORAN SISWA") || menuName.equals("LAPORAN GURU") ||
            menuName.equals("LAPORAN NILAI") || menuName.equals("LAPORAN ABSENSI") ||
            menuName.equals("KALENDER AKADEMIK")) {
            filteredMenu.add(item);
            System.out.println("  + " + item[1]);
        }
    }
} else {
    System.out.println("Role tidak dikenal: " + role);
    for (String[] item : allMenuItems) {
        if (item[1].equals("DASHBOARD")) {
            filteredMenu.add(item);
        }
    }
}

System.out.println("Total menu setelah filter: " + filteredMenu.size());
String[][] menuItems = filteredMenu.toArray(new String[0][]);
        
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
        
        // INIT PANELS
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
        cetakRaportPanel = new CetakRaportPanel(cardLayout, mainPanel);
        kalenderAkademikPanel = new KalenderAkademikPanel(cardLayout, mainPanel);
        
        // CONTENT PANEL
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
        contentPanel.add(cetakRaportPanel, "cetakRaport");
        contentPanel.add(kalenderAkademikPanel, "kalenderAkademik");
        
        mainContentPanel.add(sidebarScrollPane, BorderLayout.WEST);
        mainContentPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainContentPanel, BorderLayout.CENTER);
        
        // FOOTER
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 32));
        
        JLabel footerLabel = new JLabel("SIAKAD SMA PGRI 4 Jakarta");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(189, 195, 199));
        footerLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        footerPanel.add(footerLabel, BorderLayout.WEST);
        
        JLabel footerTimeLabel = new JLabel();
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
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        // WELCOME PANEL
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setBackground(new Color(240, 248, 255));
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        welcomePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        String namaUser = Session.getNama() != null ? Session.getNama() : "Pengguna";
        JLabel welcomeLabel = new JLabel("👋 Selamat Datang, " + namaUser + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        welcomeLabel.setForeground(new Color(41, 128, 185));
        
        String roleTextInfo = "";
        if (Session.isAdmin()) {
            roleTextInfo = "Administrator - Anda memiliki akses penuh ke semua fitur";
        } else if (Session.isGuru()) {
            String mapel = Session.getMapel() != null ? Session.getMapel() : "";
            roleTextInfo = "Guru - " + mapel;
        } else if (Session.isKepsek()) {
            roleTextInfo = "Kepala Sekolah - Akses laporan dan dashboard";
        } else {
            roleTextInfo = "Silakan login untuk mengakses semua fitur";
        }
        
        JLabel roleLabel = new JLabel(roleTextInfo);
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(100, 100, 100));
        
        welcomePanel.add(welcomeLabel);
        welcomePanel.add(Box.createVerticalStrut(5));
        welcomePanel.add(roleLabel);
        
        panel.add(welcomePanel);
        panel.add(Box.createVerticalStrut(15));
        
        // STATS CARDS
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
        
        // CHARTS
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
        
        // INFO
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
            "  • Grafik Dashboard (Statistik Gender & Jurusan)\n" +
            "  • Cetak Raport Otomatis\n" +
            "  • Kalender Akademik\n\n" +
            "Gunakan menu di samping kiri untuk mengakses fitur.");
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setEditable(false);
        infoArea.setBackground(Color.WHITE);
        infoArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        infoBorderPanel.add(infoArea, BorderLayout.CENTER);
        panel.add(infoBorderPanel);
        panel.add(Box.createVerticalStrut(15));
        
        // NOTIFIKASI
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
                
                int lakiTemp = 0;
                int perempuanTemp = 0;
                
                for (String[] s : siswaList) {
                    if (s.length > 3) {
                        if (s[3].equalsIgnoreCase("Laki-laki")) {
                            lakiTemp++;
                        } else if (s[3].equalsIgnoreCase("Perempuan")) {
                            perempuanTemp++;
                        }
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
                                
                                if (jurusan.equalsIgnoreCase("MIPA")) {
                                    jurusanCount.put("MIPA", jurusanCount.get("MIPA") + 1);
                                } else if (jurusan.equalsIgnoreCase("IPS")) {
                                    jurusanCount.put("IPS", jurusanCount.get("IPS") + 1);
                                } else if (jurusan.equalsIgnoreCase("Bahasa")) {
                                    jurusanCount.put("Bahasa", jurusanCount.get("Bahasa") + 1);
                                }
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
    public void refreshMenu() {
    // Cari sidebarPanel yang ada di dalam sidebarScrollPane
    JScrollPane sidebarScrollPane = null;
    JPanel sidebarPanel = null;
    
    // Cari komponen sidebar
    for (Component comp : getComponents()) {
        if (comp instanceof JPanel) {
            JPanel mainContent = (JPanel) comp;
            for (Component comp2 : mainContent.getComponents()) {
                if (comp2 instanceof JScrollPane) {
                    sidebarScrollPane = (JScrollPane) comp2;
                    if (sidebarScrollPane.getViewport().getView() instanceof JPanel) {
                        sidebarPanel = (JPanel) sidebarScrollPane.getViewport().getView();
                        break;
                    }
                }
            }
        }
    }
    
    if (sidebarPanel == null) return;
    
    // Hapus semua komponen di sidebarPanel kecuali profile panel dan separator
    sidebarPanel.removeAll();
    
    // ============ PROFILE PANEL ============
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
    
    String roleText = "Guest";
    String namaText = "SMA PGRI 4 Jakarta";
    
    if (Session.getRole() != null) {
        if (Session.getRole().equals("admin")) roleText = "Administrator";
        else if (Session.getRole().equals("guru")) roleText = "Guru";
        else if (Session.getRole().equals("kepsek")) roleText = "Kepala Sekolah";
    }
    if (Session.getNama() != null && !Session.getNama().isEmpty()) {
        namaText = Session.getNama();
    }
    
    JLabel userRoleLabel = new JLabel(roleText);
    userRoleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
    userRoleLabel.setForeground(Color.WHITE);
    userRoleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    profilePanel.add(userRoleLabel);
    
    JLabel userNameLabel = new JLabel(namaText);
    userNameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
    userNameLabel.setForeground(new Color(189, 195, 199));
    userNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    profilePanel.add(userNameLabel);
    
    sidebarPanel.add(profilePanel);
    sidebarPanel.add(Box.createVerticalStrut(10));
    
    // ============ SEPARATOR ============
    JSeparator sep = new JSeparator();
    sep.setForeground(new Color(68, 86, 104));
    sep.setMaximumSize(new Dimension(230, 2));
    sidebarPanel.add(sep);
    sidebarPanel.add(Box.createVerticalStrut(15));
    
    // ============ MENU ITEMS ============
    String[][] allMenuItems = {
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
        {"📋", "LAPORAN ABSENSI", "laporanAbsensi"},
        {"📑", "CETAK RAPORT", "cetakRaport"},
        {"📅", "KALENDER AKADEMIK", "kalenderAkademik"}
    };
    
    // Filter menu berdasarkan role
    java.util.ArrayList<String[]> filteredMenu = new java.util.ArrayList<>();
    String role = Session.getRole();
    
    if (role == null) {
        for (String[] item : allMenuItems) {
            if (item[1].equals("DASHBOARD") || item[1].equals("PROFIL SEKOLAH")) {
                filteredMenu.add(item);
            }
        }
    } else if (role.equals("admin")) {
        for (String[] item : allMenuItems) {
            filteredMenu.add(item);
        }
    } else if (role.equals("guru")) {
        for (String[] item : allMenuItems) {
            String menuName = item[1];
            if (!menuName.equals("DATA GURU") && !menuName.equals("DATA KELAS") && 
                !menuName.equals("PEMBAYARAN SPP") && !menuName.equals("LAPORAN GURU")) {
                filteredMenu.add(item);
            }
        }
    } else if (role.equals("kepsek")) {
        for (String[] item : allMenuItems) {
            String menuName = item[1];
            if (menuName.equals("DASHBOARD") || menuName.equals("PROFIL SEKOLAH") ||
                menuName.equals("LAPORAN SISWA") || menuName.equals("LAPORAN GURU") ||
                menuName.equals("LAPORAN NILAI") || menuName.equals("LAPORAN ABSENSI") ||
                menuName.equals("KALENDER AKADEMIK")) {
                filteredMenu.add(item);
            }
        }
    } else {
        for (String[] item : allMenuItems) {
            if (item[1].equals("DASHBOARD")) {
                filteredMenu.add(item);
            }
        }
    }
    
    String[][] menuItems = filteredMenu.toArray(new String[0][]);
    
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
    sidebarPanel.revalidate();
    sidebarPanel.repaint();
}
    public void refreshContentPanel() {
    // Buat ulang semua panel yang ada
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
    cetakRaportPanel = new CetakRaportPanel(cardLayout, mainPanel);
    kalenderAkademikPanel = new KalenderAkademikPanel(cardLayout, mainPanel);
    
    // Update contentPanel
    contentPanel.removeAll();
    
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
    contentPanel.add(cetakRaportPanel, "cetakRaport");
    contentPanel.add(kalenderAkademikPanel, "kalenderAkademik");
    
    contentPanel.revalidate();
    contentPanel.repaint();
}
}