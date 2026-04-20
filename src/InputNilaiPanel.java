import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class InputNilaiPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField nisnField, namaField, tugasField, utsField, uasField, akhirField;
    private JButton cariBtn, hitungBtn, simpanBtn, resetBtn, refreshBtn, backBtn;
    private DefaultTableModel tableModel;
    private JTable table;
    private String currentNISN = "";
    
    public InputNilaiPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadDataNilai();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Panel Input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Input Nilai"));
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
        
        // Nilai Tugas
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Nilai Tugas (20%):"), gbc);
        gbc.gridx = 1;
        tugasField = new JTextField(10);
        tugasField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(tugasField, gbc);
        
        // Nilai UTS
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Nilai UTS (30%):"), gbc);
        gbc.gridx = 1;
        utsField = new JTextField(10);
        utsField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(utsField, gbc);
        
        // Nilai UAS
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Nilai UAS (50%):"), gbc);
        gbc.gridx = 1;
        uasField = new JTextField(10);
        uasField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputPanel.add(uasField, gbc);
        
        // Nilai Akhir
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Nilai Akhir:"), gbc);
        gbc.gridx = 1;
        akhirField = new JTextField(10);
        akhirField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        akhirField.setEditable(false);
        akhirField.setBackground(new Color(240, 240, 240));
        inputPanel.add(akhirField, gbc);
        
        // Tombol Aksi
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        hitungBtn = new JButton("HITUNG NILAI");
        hitungBtn.setBackground(new Color(52, 152, 219));
        hitungBtn.setForeground(Color.WHITE);
        hitungBtn.setFocusPainted(false);
        hitungBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        simpanBtn = new JButton("SIMPAN NILAI");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);
        simpanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        resetBtn = new JButton("RESET");
        resetBtn.setBackground(new Color(231, 76, 60));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.setFocusPainted(false);
        resetBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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
        
        btnPanel.add(hitungBtn);
        btnPanel.add(simpanBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        inputPanel.add(btnPanel, gbc);
        
        // Tabel Riwayat Nilai
        String[] columns = {"NISN", "Nama Siswa", "Tugas", "UTS", "UAS", "Nilai Akhir"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Nilai Siswa"));
        
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
        
        hitungBtn.addActionListener(e -> hitungNilai());
        simpanBtn.addActionListener(e -> simpanNilai());
        resetBtn.addActionListener(e -> resetForm());
        refreshBtn.addActionListener(e -> {
            loadDataNilai();
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
                    String tugas = tableModel.getValueAt(row, 2).toString();
                    String uts = tableModel.getValueAt(row, 3).toString();
                    String uas = tableModel.getValueAt(row, 4).toString();
                    String akhir = tableModel.getValueAt(row, 5).toString();
                    
                    nisnField.setText(nisn);
                    namaField.setText(nama);
                    tugasField.setText(tugas);
                    utsField.setText(uts);
                    uasField.setText(uas);
                    akhirField.setText(akhir);
                    currentNISN = nisn;
                }
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
            
            // Cek apakah sudah ada nilai untuk siswa ini
            for (int i = 0; i < Database.dataNilaiModel.getRowCount(); i++) {
                if (Database.dataNilaiModel.getValueAt(i, 0).toString().equals(nisn)) {
                    tugasField.setText(Database.dataNilaiModel.getValueAt(i, 2).toString());
                    utsField.setText(Database.dataNilaiModel.getValueAt(i, 3).toString());
                    uasField.setText(Database.dataNilaiModel.getValueAt(i, 4).toString());
                    akhirField.setText(Database.dataNilaiModel.getValueAt(i, 5).toString());
                    JOptionPane.showMessageDialog(this, "Data nilai sudah ada, silakan edit jika perlu.");
                    return;
                }
            }
            
            // Reset nilai jika belum ada
            tugasField.setText("");
            utsField.setText("");
            uasField.setText("");
            akhirField.setText("");
            
            JOptionPane.showMessageDialog(this, "Siswa ditemukan: " + siswa[1]);
        } else {
            JOptionPane.showMessageDialog(this, "Siswa dengan NISN " + nisn + " tidak ditemukan!");
            namaField.setText("");
            currentNISN = "";
        }
    }
    
    private void hitungNilai() {
        if (currentNISN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari siswa terlebih dahulu!");
            return;
        }
        
        try {
            double tugas = tugasField.getText().isEmpty() ? 0 : Double.parseDouble(tugasField.getText());
            double uts = utsField.getText().isEmpty() ? 0 : Double.parseDouble(utsField.getText());
            double uas = uasField.getText().isEmpty() ? 0 : Double.parseDouble(uasField.getText());
            
            if (tugas < 0 || tugas > 100 || uts < 0 || uts > 100 || uas < 0 || uas > 100) {
                JOptionPane.showMessageDialog(this, "Nilai harus antara 0-100!");
                return;
            }
            
            double akhir = (tugas * 0.2) + (uts * 0.3) + (uas * 0.5);
            akhirField.setText(String.format("%.2f", akhir));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan angka yang valid untuk nilai!");
        }
    }
    
    private void simpanNilai() {
        if (currentNISN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari siswa terlebih dahulu!");
            return;
        }
        
        if (tugasField.getText().isEmpty() || utsField.getText().isEmpty() || uasField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan semua nilai (Tugas, UTS, UAS)!");
            return;
        }
        
        if (akhirField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hitung nilai akhir terlebih dahulu!");
            return;
        }
        
        String nisn = currentNISN;
        String nama = namaField.getText();
        String tugas = tugasField.getText();
        String uts = utsField.getText();
        String uas = uasField.getText();
        String akhir = akhirField.getText();
        
        // Cek apakah sudah ada data untuk siswa ini
        boolean found = false;
        for (int i = 0; i < Database.dataNilaiModel.getRowCount(); i++) {
            if (Database.dataNilaiModel.getValueAt(i, 0).toString().equals(nisn)) {
                Database.dataNilaiModel.setValueAt(tugas, i, 2);
                Database.dataNilaiModel.setValueAt(uts, i, 3);
                Database.dataNilaiModel.setValueAt(uas, i, 4);
                Database.dataNilaiModel.setValueAt(akhir, i, 5);
                found = true;
                break;
            }
        }
        
        if (!found) {
            Database.dataNilaiModel.addRow(new Object[]{nisn, nama, tugas, uts, uas, akhir});
        }
        
        loadDataNilai();
        JOptionPane.showMessageDialog(this, "Nilai berhasil disimpan untuk " + nama);
        resetForm();
    }
    
    private void resetForm() {
        nisnField.setText("");
        namaField.setText("");
        tugasField.setText("");
        utsField.setText("");
        uasField.setText("");
        akhirField.setText("");
        currentNISN = "";
        nisnField.requestFocus();
    }
    
    private void loadDataNilai() {
        tableModel.setRowCount(0);
        for (int i = 0; i < Database.dataNilaiModel.getRowCount(); i++) {
            tableModel.addRow(new Object[]{
                Database.dataNilaiModel.getValueAt(i, 0),
                Database.dataNilaiModel.getValueAt(i, 1),
                Database.dataNilaiModel.getValueAt(i, 2),
                Database.dataNilaiModel.getValueAt(i, 3),
                Database.dataNilaiModel.getValueAt(i, 4),
                Database.dataNilaiModel.getValueAt(i, 5)
            });
        }
    }
}