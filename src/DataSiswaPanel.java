import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import javax.swing.border.TitledBorder;

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
        
        // ============ FORM PANEL ============
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(41, 128, 185), 1),
            " Form Data Siswa ",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 13),
            new Color(41, 128, 185)
        ));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Row 1
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("NISN:"), gbc);
        gbc.gridx = 1;
        nisnField = new JTextField(15);
        nisnField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nisnField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 3;
        namaField = new JTextField(15);
        namaField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(namaField, gbc);
        
        // Row 2
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        gbc.gridx = 1;
        jkCombo = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        jkCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(jkCombo, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Agama:"), gbc);
        gbc.gridx = 3;
        agamaCombo = new JComboBox<>(new String[]{"Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Konghucu"});
        agamaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(agamaCombo, gbc);
        
        // Row 3
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Alamat:"), gbc);
        gbc.gridx = 1;
        alamatField = new JTextField(15);
        alamatField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(alamatField, gbc);
        
        gbc.gridx = 2;
        formPanel.add(new JLabel("Kelas:"), gbc);
        gbc.gridx = 3;
        kelasCombo = new JComboBox<>();
        kelasCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(kelasCombo, gbc);
        
        // ============ BUTTON PANEL ============
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        
        JButton tambahBtn = createStyledButton("TAMBAH", new Color(46, 204, 113));
        JButton ubahBtn = createStyledButton("UBAH", new Color(52, 152, 219));
        JButton hapusBtn = createStyledButton("HAPUS", new Color(231, 76, 60));
        JButton refreshBtn = createStyledButton("REFRESH", new Color(52, 152, 219));
        JButton backBtn = createStyledButton("KEMBALI", new Color(52, 73, 94));
        
        btnPanel.add(tambahBtn);
        btnPanel.add(ubahBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        // ============ TABLE ============
        String[] columns = {"NISN", "Nama", "Jenis Kelamin", "Agama", "Alamat", "Kelas"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        // ============ EVENTS ============
        tambahBtn.addActionListener(e -> tambahData());
        ubahBtn.addActionListener(e -> ubahData());
        hapusBtn.addActionListener(e -> hapusData());
        refreshBtn.addActionListener(e -> {
            loadData();
            loadKelas();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
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
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
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
            tableModel.addRow(siswa);
        }
    }
    
    private void tambahData() {
        String nisn = nisnField.getText().trim();
        String nama = namaField.getText().trim();
        String jk = (String) jkCombo.getSelectedItem();
        String agama = (String) agamaCombo.getSelectedItem();
        String alamat = alamatField.getText().trim();
        String kelas = (String) kelasCombo.getSelectedItem();
        
        if (nisn.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NISN, Nama, dan Alamat harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (Database.tambahSiswa(nisn, nama, jk, agama, alamat, kelas)) {
            loadData();
            nisnField.setText("");
            namaField.setText("");
            alamatField.setText("");
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data! NISN mungkin sudah ada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (Database.updateSiswa(nisnLama, nisnBaru, nama, jk, agama, alamat, kelas)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin hapus data ini?\nData nilai, SPP, dan absensi terkait juga akan terhapus!", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            String nisn = tableModel.getValueAt(row, 0).toString();
            if (Database.hapusSiswa(nisn)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Data dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}