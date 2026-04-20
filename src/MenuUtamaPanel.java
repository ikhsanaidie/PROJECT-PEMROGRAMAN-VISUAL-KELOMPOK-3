import javax.swing.*;
import java.awt.*;

public class MenuUtamaPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel timeLabel;
    
    public MenuUtamaPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        startClock();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 242, 245));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 80));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        logoPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logoIcon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            Image scaledImage = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            logoLabel.setText("SMA PGRI 4");
            logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            logoLabel.setForeground(Color.WHITE);
        }
        logoPanel.add(logoLabel);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1));
        textPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("SISTEM INFORMASI AKADEMIK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel subTitleLabel = new JLabel("SMA PGRI 4 JAKARTA");
        subTitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subTitleLabel.setForeground(new Color(230, 240, 255));
        
        textPanel.add(titleLabel);
        textPanel.add(subTitleLabel);
        logoPanel.add(textPanel);
        
        headerPanel.add(logoPanel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        userPanel.setOpaque(false);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        timeLabel.setForeground(Color.WHITE);
        
        JButton logoutBtn = new JButton("LOGOUT");
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.setBackground(new Color(231, 76, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutBtn.setBackground(new Color(231, 76, 60));
            }
        });
        
        logoutBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                cardLayout.show(mainPanel, "login");
            }
        });
        
        userPanel.add(timeLabel);
        userPanel.add(logoutBtn);
        
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(new Color(240, 242, 245));
        mainContent.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 30, 0));
        cardsPanel.setOpaque(false);
        
        JPanel masterCard = createCard("📁", "MASTER DATA", 
            "Kelola Data Siswa, Guru, dan Kelas", 
            new Color(52, 152, 219));
        masterCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSubMenu("master");
            }
        });
        
        JPanel transaksiCard = createCard("📝", "TRANSAKSI", 
            "Input Nilai, Absensi, dan Pembayaran SPP", 
            new Color(46, 204, 113));
        transaksiCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSubMenu("transaksi");
            }
        });
        
        JPanel laporanCard = createCard("📊", "LAPORAN", 
            "Cetak Laporan Siswa, Guru, Nilai, Absensi", 
            new Color(155, 89, 182));
        laporanCard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showSubMenu("laporan");
            }
        });
        
        cardsPanel.add(masterCard);
        cardsPanel.add(transaksiCard);
        cardsPanel.add(laporanCard);
        
        mainContent.add(cardsPanel, BorderLayout.NORTH);
        
        JPanel subMenuContainer = new JPanel(new CardLayout());
        subMenuContainer.setOpaque(false);
        subMenuContainer.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JPanel masterSubMenu = createSubMenuPanel(new String[]{
            "👨‍🎓 DATA SISWA", "👨‍🏫 DATA GURU", "🏫 DATA KELAS"
        }, new String[]{"dataSiswa", "dataGuru", "dataKelas"});
        
        JPanel transaksiSubMenu = createSubMenuPanel(new String[]{
            "📝 INPUT NILAI", "📅 ABSENSI SISWA", "💰 PEMBAYARAN SPP"
        }, new String[]{"inputNilai", "absensi", "pembayaranSPP"});
        
        JPanel laporanSubMenu = createSubMenuPanel(new String[]{
            "📄 LAPORAN SISWA", "📄 LAPORAN GURU", "📊 LAPORAN NILAI", "📋 LAPORAN ABSENSI"
        }, new String[]{"laporanSiswa", "laporanGuru", "laporanNilai", "laporanAbsensi"});
        
        subMenuContainer.add(masterSubMenu, "master");
        subMenuContainer.add(transaksiSubMenu, "transaksi");
        subMenuContainer.add(laporanSubMenu, "laporan");
        
        mainContent.add(subMenuContainer, BorderLayout.CENTER);
        
        add(mainContent, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(52, 73, 94));
        footerPanel.setPreferredSize(new Dimension(0, 35));
        
        JLabel footerLabel = new JLabel("© 2026 SMA PGRI 4 Jakarta - Sistem Informasi Akademik Terintegrasi");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(189, 195, 199));
        footerPanel.add(footerLabel);
        
        add(footerPanel, BorderLayout.SOUTH);
        
        putClientProperty("subMenuContainer", subMenuContainer);
    }
    
    private JPanel createCard(String emoji, String title, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 20, 30, 20)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 55));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        descLabel.setForeground(new Color(127, 140, 141));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        card.add(emojiLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createSubMenuPanel(String[] menuNames, String[] targetPanels) {
        int rows = menuNames.length == 4 ? 2 : 1;
        int cols = menuNames.length == 4 ? 2 : menuNames.length;
        
        JPanel panel = new JPanel(new GridLayout(rows, cols, 20, 15));
        panel.setOpaque(false);
        
        for (int i = 0; i < menuNames.length; i++) {
            final String target = targetPanels[i];
            JButton btn = new JButton(menuNames[i]);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBackground(new Color(52, 73, 94));
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
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
            
            btn.addActionListener(e -> cardLayout.show(mainPanel, target));
            panel.add(btn);
        }
        
        return panel;
    }
    
    private void showSubMenu(String menu) {
        JPanel subMenuContainer = (JPanel) getClientProperty("subMenuContainer");
        if (subMenuContainer != null) {
            CardLayout cl = (CardLayout) subMenuContainer.getLayout();
            cl.show(subMenuContainer, menu);
        }
    }
    
    private void startClock() {
        Thread clock = new Thread(() -> {
            while (true) {
                try {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm:ss");
                    java.util.Locale id = new java.util.Locale("id", "ID");
                    sdf = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy | HH:mm:ss", id);
                    String time = sdf.format(new java.util.Date());
                    SwingUtilities.invokeLater(() -> timeLabel.setText("🕒" + time));
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        clock.start();
    }
}