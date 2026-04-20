import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DataSiswaPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nisnField, namaField, alamatField;
    private JComboBox<String> jkCombo, agamaCombo, kelasCombo;
    
    public DataSiswaPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
        loadKelas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Data Siswa"));
        formPanel.setBackground(Color.WHITE);
        
        nisnField = new JTextField();
        namaField = new JTextField();
        jkCombo = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        agamaCombo = new JComboBox<>(new String[]{"Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu"});
        alamatField = new JTextField();
        kelasCombo = new JComboBox<>();
        
        formPanel.add(new JLabel("NISN:"));
        formPanel.add(nisnField);
        formPanel.add(new JLabel("Nama Lengkap:"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Jenis Kelamin:"));
        formPanel.add(jkCombo);
        formPanel.add(new JLabel("Agama:"));
        formPanel.add(agamaCombo);
        formPanel.add(new JLabel("Alamat:"));
        formPanel.add(alamatField);
        formPanel.add(new JLabel("Kelas:"));
        formPanel.add(kelasCombo);
        
        // Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        JButton tambahBtn = new JButton("TAMBAH");
        tambahBtn.setBackground(new Color(46, 204, 113));
        tambahBtn.setForeground(Color.WHITE);
        tambahBtn.setFocusPainted(false);
        
        JButton ubahBtn = new JButton("UBAH");
        ubahBtn.setBackground(new Color(52, 152, 219));
        ubahBtn.setForeground(Color.WHITE);
        ubahBtn.setFocusPainted(false);
        
        JButton hapusBtn = new JButton("HAPUS");
        hapusBtn.setBackground(new Color(231, 76, 60));
        hapusBtn.setForeground(Color.WHITE);
        hapusBtn.setFocusPainted(false);
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        
        JButton backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        
        btnPanel.add(tambahBtn);
        btnPanel.add(ubahBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        // Tabel
        String[] columns = {"NISN", "Nama", "Jenis Kelamin", "Agama", "Alamat", "Kelas"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // Event Handlers
        tambahBtn.addActionListener(e -> tambahData());
        ubahBtn.addActionListener(e -> ubahData());
        hapusBtn.addActionListener(e -> hapusData());
        refreshBtn.addActionListener(e -> {
            loadData();
            loadKelas();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        // Klik tabel isi form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    nisnField.setText(tableModel.getValueAt(row, 0).toString());
                    namaField.setText(tableModel.getValueAt(row, 1).toString());
                    jkCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    agamaCombo.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    alamatField.setText(tableModel.getValueAt(row, 4).toString());
                    kelasCombo.setSelectedItem(tableModel.getValueAt(row, 5).toString());
                }
            }
        });
        
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadKelas() {
        kelasCombo.removeAllItems();
        for (String[] kelas : Database.getAllKelas()) {
            kelasCombo.addItem(kelas[1]);
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        for (String[] siswa : Database.getAllSiswa()) {
            tableModel.addRow(new Object[]{
                siswa[0], siswa[1], siswa[2], siswa[3], siswa[4], siswa[5]
            });
        }
        JOptionPane.showMessageDialog(this, "Total " + tableModel.getRowCount() + " siswa.");
    }
    
    private void tambahData() {
        String nisn = nisnField.getText().trim();
        String nama = namaField.getText().trim();
        String jk = (String) jkCombo.getSelectedItem();
        String agama = (String) agamaCombo.getSelectedItem();
        String alamat = alamatField.getText().trim();
        String kelas = (String) kelasCombo.getSelectedItem();
        
        if (nisn.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NISN, Nama, dan Alamat harus diisi!");
            return;
        }
        
        if (Database.getSiswaByNISN(nisn) != null) {
            JOptionPane.showMessageDialog(this, "NISN sudah ada!");
            return;
        }
        
        Database.dataSiswaModel.addRow(new Object[]{nisn, nama, jk, agama, alamat, kelas});
        loadData();
        
        nisnField.setText("");
        namaField.setText("");
        alamatField.setText("");
        
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String nisnLama = tableModel.getValueAt(row, 0).toString();
        String nisnBaru = nisnField.getText().trim();
        String nama = namaField.getText().trim();
        String jk = (String) jkCombo.getSelectedItem();
        String agama = (String) agamaCombo.getSelectedItem();
        String alamat = alamatField.getText().trim();
        String kelas = (String) kelasCombo.getSelectedItem();
        
        if (nisnBaru.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NISN, Nama, dan Alamat harus diisi!");
            return;
        }
        
        Database.dataSiswaModel.setValueAt(nisnBaru, row, 0);
        Database.dataSiswaModel.setValueAt(nama, row, 1);
        Database.dataSiswaModel.setValueAt(jk, row, 2);
        Database.dataSiswaModel.setValueAt(agama, row, 3);
        Database.dataSiswaModel.setValueAt(alamat, row, 4);
        Database.dataSiswaModel.setValueAt(kelas, row, 5);
        
        // Update data terkait di transaksi
        for (int i = 0; i < Database.dataNilaiModel.getRowCount(); i++) {
            if (Database.dataNilaiModel.getValueAt(i, 0).toString().equals(nisnLama)) {
                Database.dataNilaiModel.setValueAt(nisnBaru, i, 0);
                Database.dataNilaiModel.setValueAt(nama, i, 1);
            }
        }
        
        for (int i = 0; i < Database.dataSPPModel.getRowCount(); i++) {
            if (Database.dataSPPModel.getValueAt(i, 0).toString().equals(nisnLama)) {
                Database.dataSPPModel.setValueAt(nisnBaru, i, 0);
                Database.dataSPPModel.setValueAt(nama, i, 1);
            }
        }
        
        for (int i = 0; i < Database.dataAbsensiModel.getRowCount(); i++) {
            if (Database.dataAbsensiModel.getValueAt(i, 2).toString().equals(nisnLama)) {
                Database.dataAbsensiModel.setValueAt(nisnBaru, i, 2);
                Database.dataAbsensiModel.setValueAt(nama, i, 3);
            }
        }
        
        loadData();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?\nData nilai, SPP, dan absensi terkait juga akan terhapus!", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String nisn = tableModel.getValueAt(row, 0).toString();
            
            for (int i = Database.dataNilaiModel.getRowCount() - 1; i >= 0; i--) {
                if (Database.dataNilaiModel.getValueAt(i, 0).toString().equals(nisn)) {
                    Database.dataNilaiModel.removeRow(i);
                }
            }
            for (int i = Database.dataSPPModel.getRowCount() - 1; i >= 0; i--) {
                if (Database.dataSPPModel.getValueAt(i, 0).toString().equals(nisn)) {
                    Database.dataSPPModel.removeRow(i);
                }
            }
            for (int i = Database.dataAbsensiModel.getRowCount() - 1; i >= 0; i--) {
                if (Database.dataAbsensiModel.getValueAt(i, 2).toString().equals(nisn)) {
                    Database.dataAbsensiModel.removeRow(i);
                }
            }
            
            Database.dataSiswaModel.removeRow(row);
            loadData();
            JOptionPane.showMessageDialog(this, "Data dihapus!");
        }
    }
}