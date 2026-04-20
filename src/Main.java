import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Main() {
        setTitle("SIAKAD - SMA PGRI 4 Jakarta");
        setSize(1300, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set Look and Feel modern
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 12));
            UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
            UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {}
        }
        
        // Test koneksi database
        if (!KoneksiDB.testConnection()) {
            JOptionPane.showMessageDialog(null, 
                "Gagal koneksi ke database!\nPastikan MySQL sedang berjalan.", 
                "Error Koneksi", JOptionPane.ERROR_MESSAGE);
        }
        
        // Set icon frame
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {}
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240, 242, 245));
        
        // Tambahkan panel
        mainPanel.add(new LoginPanel(cardLayout, mainPanel), "login");
        mainPanel.add(new MenuUtamaPanel(cardLayout, mainPanel), "menuUtama");
        mainPanel.add(new DataSiswaPanel(cardLayout, mainPanel), "dataSiswa");
        mainPanel.add(new DataGuruPanel(cardLayout, mainPanel), "dataGuru");
        mainPanel.add(new DataKelasPanel(cardLayout, mainPanel), "dataKelas");
        mainPanel.add(new InputNilaiPanel(cardLayout, mainPanel), "inputNilai");
        mainPanel.add(new AbsensiPanel(cardLayout, mainPanel), "absensi");
        mainPanel.add(new PembayaranSPPPanel(cardLayout, mainPanel), "pembayaranSPP");
        mainPanel.add(new LaporanSiswaPanel(cardLayout, mainPanel), "laporanSiswa");
        mainPanel.add(new LaporanGuruPanel(cardLayout, mainPanel), "laporanGuru");
        mainPanel.add(new LaporanNilaiPanel(cardLayout, mainPanel), "laporanNilai");
        mainPanel.add(new LaporanAbsensiPanel(cardLayout, mainPanel), "laporanAbsensi");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
        
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            KoneksiDB.closeConnection();
        }));
    }
}