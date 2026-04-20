import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PembayaranSPPPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField nisnField, namaField, nominalField, tglBayarField;
    private JComboBox<String> bulanCombo, statusCombo;
    private JButton cariBtn, simpanBtn, refreshBtn, backBtn;
    private DefaultTableModel tableModel;
    private JTable table;
    private String currentNISN = "";
    
    public PembayaranSPPPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadDataSPP();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Pembayaran SPP"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // NISN dengan auto lookup
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("NISN:"), gbc);
        gbc.gridx = 1;
        nisnField = new JTextField(15);
        nisnField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(nisnField, gbc);
        
        gbc.gridx = 2;
        cariBtn = new JButton("CARI SISWA");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.setFocusPainted(false);
        cariBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inputPanel.add(cariBtn, gbc);
        
        // Nama Siswa (otomatis terisi)
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Nama Siswa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        namaField = new JTextField(20);
        namaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        namaField.setEditable(false);
        namaField.setBackground(new Color(240, 240, 240));
        inputPanel.add(namaField, gbc);
        gbc.gridwidth = 1;
        
        // Bulan
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Bulan Pembayaran:"), gbc);
        gbc.gridx = 1;
        bulanCombo = new JComboBox<>(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        bulanCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bulanCombo.setBackground(Color.WHITE);
        inputPanel.add(bulanCombo, gbc);
        
        // Nominal SPP
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Nominal SPP:"), gbc);
        gbc.gridx = 1;
        nominalField = new JTextField(15);
        nominalField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nominalField.setText("200000");
        inputPanel.add(nominalField, gbc);
        
        // Status
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"Lunas", "Belum"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusCombo.setBackground(Color.WHITE);
        inputPanel.add(statusCombo, gbc);
        
        // Tanggal Bayar
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Tanggal Bayar:"), gbc);
        gbc.gridx = 1;
        tglBayarField = new JTextField(15);
        tglBayarField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tglBayarField.setText(getCurrentDate());
        inputPanel.add(tglBayarField, gbc);
        
        // Tombol Aksi
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        simpanBtn = new JButton("SIMPAN PEMBAYARAN");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);
        simpanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPanel.add(simpanBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        inputPanel.add(btnPanel, gbc);
        
        // Tabel Riwayat Pembayaran SPP
        String[] columns = {"NISN", "Nama Siswa", "Bulan", "Nominal", "Status", "Tanggal Bayar"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Pembayaran SPP"));
        
        // Event Handlers
        cariBtn.addActionListener(e -> cariSiswa());
        
        // Enter key di NISN field juga bisa mencari
        nisnField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cariSiswa();
                }
            }
        });
        
        simpanBtn.addActionListener(e -> simpanPembayaran());
        refreshBtn.addActionListener(e -> {
            loadDataSPP();
            resetForm();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        // Klik tabel untuk mengisi form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    String nisn = tableModel.getValueAt(row, 0).toString();
                    String nama = tableModel.getValueAt(row, 1).toString();
                    String bulan = tableModel.getValueAt(row, 2).toString();
                    String nominal = tableModel.getValueAt(row, 3).toString();
                    String status = tableModel.getValueAt(row, 4).toString();
                    String tglBayar = tableModel.getValueAt(row, 5).toString();
                    
                    nisnField.setText(nisn);
                    namaField.setText(nama);
                    bulanCombo.setSelectedItem(bulan);
                    nominalField.setText(nominal);
                    statusCombo.setSelectedItem(status);
                    tglBayarField.setText(tglBayar);
                    currentNISN = nisn;
                }
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new Date());
    }
    
    private void cariSiswa() {
        String nisn = nisnField.getText().trim();
        if (nisn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NISN terlebih dahulu!");
            return;
        }
        
        String[] siswa = Database.getSiswaByNISN(nisn);
        if (siswa != null) {
            namaField.setText(siswa[1]); // Nama siswa
            currentNISN = nisn;
            JOptionPane.showMessageDialog(this, "Siswa ditemukan: " + siswa[1]);
            
            // Reset form tapi tetap simpan NISN dan Nama
            bulanCombo.setSelectedIndex(0);
            nominalField.setText("200000");
            statusCombo.setSelectedIndex(0);
            tglBayarField.setText(getCurrentDate());
        } else {
            JOptionPane.showMessageDialog(this, "Siswa dengan NISN " + nisn + " tidak ditemukan!");
            namaField.setText("");
            currentNISN = "";
        }
    }
    
    private void simpanPembayaran() {
        if (currentNISN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari siswa terlebih dahulu!");
            return;
        }
        
        String nisn = currentNISN;
        String nama = namaField.getText();
        String bulan = (String) bulanCombo.getSelectedItem();
        String nominal = nominalField.getText().trim();
        String status = (String) statusCombo.getSelectedItem();
        String tglBayar = tglBayarField.getText().trim();
        
        if (nominal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal SPP!");
            return;
        }
        
        if (tglBayar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal bayar!");
            return;
        }
        
        // Cek apakah sudah ada data untuk siswa dan bulan ini
        boolean found = false;
        for (int i = 0; i < Database.dataSPPModel.getRowCount(); i++) {
            if (Database.dataSPPModel.getValueAt(i, 0).toString().equals(nisn) &&
                Database.dataSPPModel.getValueAt(i, 2).toString().equals(bulan)) {
                Database.dataSPPModel.setValueAt(nominal, i, 3);
                Database.dataSPPModel.setValueAt(status, i, 4);
                Database.dataSPPModel.setValueAt(tglBayar, i, 5);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Database.dataSPPModel.addRow(new Object[]{nisn, nama, bulan, nominal, status, tglBayar});
        }
        
        loadDataSPP();
        JOptionPane.showMessageDialog(this, "Pembayaran SPP untuk " + nama + " bulan " + bulan + " berhasil disimpan!");
        
        // Reset form tapi tetap simpan NISN dan Nama
        bulanCombo.setSelectedIndex(0);
        nominalField.setText("200000");
        statusCombo.setSelectedIndex(0);
        tglBayarField.setText(getCurrentDate());
    }
    
    private void resetForm() {
        nisnField.setText("");
        namaField.setText("");
        bulanCombo.setSelectedIndex(0);
        nominalField.setText("200000");
        statusCombo.setSelectedIndex(0);
        tglBayarField.setText(getCurrentDate());
        currentNISN = "";
        nisnField.requestFocus();
    }
    
    private void loadDataSPP() {
        tableModel.setRowCount(0);
        for (int i = 0; i < Database.dataSPPModel.getRowCount(); i++) {
            tableModel.addRow(new Object[]{
                Database.dataSPPModel.getValueAt(i, 0),
                Database.dataSPPModel.getValueAt(i, 1),
                Database.dataSPPModel.getValueAt(i, 2),
                Database.dataSPPModel.getValueAt(i, 3),
                Database.dataSPPModel.getValueAt(i, 4),
                Database.dataSPPModel.getValueAt(i, 5)
            });
        }
    }
}