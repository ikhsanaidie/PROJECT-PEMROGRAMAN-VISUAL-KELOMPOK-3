import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DataKelasPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, namaField;
    private JComboBox<String> waliCombo;
    
    public DataKelasPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
        loadWaliKelas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Data Kelas"));
        formPanel.setBackground(Color.WHITE);
        
        idField = new JTextField();
        namaField = new JTextField();
        waliCombo = new JComboBox<>();
        waliCombo.setBackground(Color.WHITE);
        
        formPanel.add(new JLabel("ID Kelas:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Nama Kelas:"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Wali Kelas:"));
        formPanel.add(waliCombo);
        
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
        String[] columns = {"ID Kelas", "Nama Kelas", "Wali Kelas"};
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
            loadWaliKelas();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        // Klik tabel untuk mengisi form
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idField.setText(tableModel.getValueAt(row, 0).toString());
                    namaField.setText(tableModel.getValueAt(row, 1).toString());
                    waliCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
        
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadWaliKelas() {
        waliCombo.removeAllItems();
        List<String[]> guruList = Database.getAllGuru();
        
        if (guruList.isEmpty()) {
            waliCombo.addItem("-- Belum ada data guru, input guru dulu --");
            JOptionPane.showMessageDialog(this, 
                "Belum ada data guru! Silakan input data guru terlebih dahulu di menu DATA GURU.", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
        } else {
            for (String[] guru : guruList) {
                waliCombo.addItem(guru[1]); // Nama Guru
            }
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        for (String[] kelas : Database.getAllKelas()) {
            tableModel.addRow(kelas);
        }
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data kelas. Silakan tambah data terlebih dahulu.");
        } else {
            JOptionPane.showMessageDialog(this, "Total " + tableModel.getRowCount() + " kelas.");
        }
    }
    
    private void tambahData() {
        // Cek apakah ada data guru
        if (Database.getAllGuru().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Harap input data GURU terlebih dahulu sebelum menambahkan kelas!\nBuka menu MASTER DATA → DATA GURU", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String wali = (String) waliCombo.getSelectedItem();
        
        if (id.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Kelas dan Nama Kelas harus diisi!");
            return;
        }
        
        if (wali == null || wali.equals("-- Belum ada data guru, input guru dulu --")) {
            JOptionPane.showMessageDialog(this, "Pilih Wali Kelas terlebih dahulu!");
            return;
        }
        
        if (Database.tambahKelas(id, nama, wali)) {
            loadData();
            idField.setText("");
            namaField.setText("");
            JOptionPane.showMessageDialog(this, "Data kelas berhasil ditambahkan!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data! ID Kelas mungkin sudah ada.");
        }
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String idLama = tableModel.getValueAt(row, 0).toString();
        String idBaru = idField.getText().trim();
        String nama = namaField.getText().trim();
        String wali = (String) waliCombo.getSelectedItem();
        
        if (idBaru.isEmpty() || nama.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Kelas dan Nama Kelas harus diisi!");
            return;
        }
        
        if (Database.updateKelas(idLama, idBaru, nama, wali)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Data kelas berhasil diubah!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin hapus data kelas ini?\nSiswa yang memiliki kelas ini juga akan terpengaruh.", 
            "Konfirmasi", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String id = tableModel.getValueAt(row, 0).toString();
            if (Database.hapusKelas(id)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Data kelas dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }
}