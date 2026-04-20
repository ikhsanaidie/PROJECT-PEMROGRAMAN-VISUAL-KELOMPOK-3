import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DataKelasPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTextField idKelasField, namaKelasField, waliKelasField;
    private JTable table;
    
    public DataKelasPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        idKelasField = new JTextField();
        namaKelasField = new JTextField();
        waliKelasField = new JTextField();
        
        formPanel.add(new JLabel("ID Kelas:"));
        formPanel.add(idKelasField);
        formPanel.add(new JLabel("Nama Kelas:"));
        formPanel.add(namaKelasField);
        formPanel.add(new JLabel("Wali Kelas:"));
        formPanel.add(waliKelasField);
        
        String[] columns = {"ID Kelas", "Nama Kelas", "Wali Kelas"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton tambahBtn = new JButton("TAMBAH");
        JButton ubahBtn = new JButton("UBAH");
        JButton hapusBtn = new JButton("HAPUS");
        JButton refreshBtn = new JButton("REFRESH");
        JButton backBtn = new JButton("KEMBALI");
        
        btnPanel.add(tambahBtn);
        btnPanel.add(ubahBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        tambahBtn.addActionListener(e -> tambahData());
        ubahBtn.addActionListener(e -> ubahData());
        hapusBtn.addActionListener(e -> hapusData());
        refreshBtn.addActionListener(e -> loadData());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    idKelasField.setText(tableModel.getValueAt(row, 0).toString());
                    namaKelasField.setText(tableModel.getValueAt(row, 1).toString());
                    waliKelasField.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
        
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<String[]> kelasList = Database.getAllKelas();
        for (String[] kelas : kelasList) {
            tableModel.addRow(kelas);
        }
    }
    
    private void tambahData() {
        String id = idKelasField.getText().trim();
        String nama = namaKelasField.getText().trim();
        String wali = waliKelasField.getText().trim();
        
        if (id.isEmpty() || nama.isEmpty() || wali.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        Database.dataKelasModel.addRow(new Object[]{id, nama, wali});
        loadData();
        
        idKelasField.setText("");
        namaKelasField.setText("");
        waliKelasField.setText("");
        
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String id = idKelasField.getText().trim();
        String nama = namaKelasField.getText().trim();
        String wali = waliKelasField.getText().trim();
        
        if (id.isEmpty() || nama.isEmpty() || wali.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        Database.dataKelasModel.setValueAt(id, row, 0);
        Database.dataKelasModel.setValueAt(nama, row, 1);
        Database.dataKelasModel.setValueAt(wali, row, 2);
        
        loadData();
        JOptionPane.showMessageDialog(this, "Data berhasil diubah!");
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Database.dataKelasModel.removeRow(row);
            loadData();
            JOptionPane.showMessageDialog(this, "Data dihapus!");
        }
    }
}