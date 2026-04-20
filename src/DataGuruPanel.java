import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DataGuruPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nipField, namaField, mapelField;
    
    public DataGuruPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Data Guru"));
        formPanel.setBackground(Color.WHITE);
        
        nipField = new JTextField();
        namaField = new JTextField();
        mapelField = new JTextField();
        
        formPanel.add(new JLabel("NIP:"));
        formPanel.add(nipField);
        formPanel.add(new JLabel("Nama Guru:"));
        formPanel.add(namaField);
        formPanel.add(new JLabel("Mata Pelajaran:"));
        formPanel.add(mapelField);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        JButton tambahBtn = new JButton("TAMBAH");
        tambahBtn.setBackground(new Color(46, 204, 113));
        tambahBtn.setForeground(Color.WHITE);
        
        JButton ubahBtn = new JButton("UBAH");
        ubahBtn.setBackground(new Color(52, 152, 219));
        ubahBtn.setForeground(Color.WHITE);
        
        JButton hapusBtn = new JButton("HAPUS");
        hapusBtn.setBackground(new Color(231, 76, 60));
        hapusBtn.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        
        JButton backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        
        btnPanel.add(tambahBtn);
        btnPanel.add(ubahBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        String[] columns = {"NIP", "Nama Guru", "Mata Pelajaran"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
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
        for (String[] guru : Database.getAllGuru()) {
            tableModel.addRow(guru);
        }
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data guru. Silakan tambah data terlebih dahulu.");
        } else {
            JOptionPane.showMessageDialog(this, "Total " + tableModel.getRowCount() + " guru.");
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
        
        if (Database.tambahGuru(nip, nama, mapel)) {
            loadData();
            nipField.setText("");
            namaField.setText("");
            mapelField.setText("");
            JOptionPane.showMessageDialog(this, "Data guru berhasil ditambahkan!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data! NIP mungkin sudah ada.");
        }
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String nipLama = tableModel.getValueAt(row, 0).toString();
        String nipBaru = nipField.getText().trim();
        String nama = namaField.getText().trim();
        String mapel = mapelField.getText().trim();
        
        if (nipBaru.isEmpty() || nama.isEmpty() || mapel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        if (Database.updateGuru(nipLama, nipBaru, nama, mapel)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Data guru berhasil diubah!");
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
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data guru ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String nip = tableModel.getValueAt(row, 0).toString();
            if (Database.hapusGuru(nip)) {
                loadData();
                JOptionPane.showMessageDialog(this, "Data guru dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }
}