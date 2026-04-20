import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DataGuruPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTextField nipField, namaField, mapelField;
    private JTable table;
    
    public DataGuruPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        nipField = new JTextField();
        namaField = new JTextField();
        mapelField = new JTextField();
        
        formPanel.add(new JLabel("NIP:"));
        formPanel.add(nipField);
        formPanel.add(new JLabel("Nama Guru:"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Mata Pelajaran:"));
        formPanel.add(mapelField);
        
        String[] columns = {"NIP", "Nama Guru", "Mata Pelajaran"};
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
                    nipField.setText(tableModel.getValueAt(row, 0).toString());
                    namaField.setText(tableModel.getValueAt(row, 1).toString());
                    mapelField.setText(tableModel.getValueAt(row, 2).toString());
                }
            }
        });
        
        add(formPanel, BorderLayout.NORTH);
        add(btnPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<String[]> guruList = Database.getAllGuru();
        for (String[] guru : guruList) {
            tableModel.addRow(guru);
        }
    }
    
    private void tambahData() {
        String nip = nipField.getText().trim();
        String nama = namaField.getText().trim();
        String mapel = mapelField.getText().trim();
        
        if (nip.isEmpty() || nama.isEmpty() || mapel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        Database.dataGuruModel.addRow(new Object[]{nip, nama, mapel});
        loadData();
        
        nipField.setText("");
        namaField.setText("");
        mapelField.setText("");
        
        JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan!");
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String nip = nipField.getText().trim();
        String nama = namaField.getText().trim();
        String mapel = mapelField.getText().trim();
        
        if (nip.isEmpty() || nama.isEmpty() || mapel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        Database.dataGuruModel.setValueAt(nip, row, 0);
        Database.dataGuruModel.setValueAt(nama, row, 1);
        Database.dataGuruModel.setValueAt(mapel, row, 2);
        
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
            Database.dataGuruModel.removeRow(row);
            loadData();
            JOptionPane.showMessageDialog(this, "Data dihapus!");
        }
    }
}