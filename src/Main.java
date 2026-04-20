import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public Main() {
        setTitle("SIAKAD - SMA PGRI 4 Jakarta");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/images/smapgri4.png"));
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Logo tidak ditemukan, lanjut tanpa icon");
        }
        
        Database.initDatabase();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(240, 242, 245));
        
        mainPanel.add(new LoginPanel(cardLayout, mainPanel), "login");
        mainPanel.add(new MenuUtamaPanel(cardLayout, mainPanel), "menuUtama");
        mainPanel.add(new DataSiswaPanel(cardLayout, mainPanel), "dataSiswa");
        mainPanel.add(new DataGuruPanel(cardLayout, mainPanel), "dataGuru");
        mainPanel.add(new DataKelasPanel(cardLayout, mainPanel), "dataKelas");
        mainPanel.add(new InputNilaiPanel(cardLayout, mainPanel), "inputNilai");
        mainPanel.add(new AbsensiPanel(cardLayout, mainPanel), "absensi");
        mainPanel.add(new PembayaranSPPPanel(cardLayout, mainPanel), "pembayaranSPP");
        mainPanel.add(new LaporanSIswaPanel(cardLayout, mainPanel), "laporanSiswa");
        mainPanel.add(new LaporanGuruPanel(cardLayout, mainPanel), "laporanGuru");
        mainPanel.add(new LaporanNilaiPanel(cardLayout, mainPanel), "laporanNilai");
        mainPanel.add(new LaporanAbsensiPanel(cardLayout, mainPanel), "laporanAbsensi");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "login");
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}