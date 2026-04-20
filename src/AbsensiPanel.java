import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableColumn;

public class AbsensiPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JComboBox<String> kelasCombo;
    private JTextField tanggalField;
    private DefaultTableModel tableModel;
    private JTable table;
    
    public AbsensiPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadKelas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        
        kelasCombo = new JComboBox<>();
        kelasCombo.setPreferredSize(new Dimension(150, 30));
        
        tanggalField = new JTextField(15);
        tanggalField.setText(getCurrentDate());
        
        JButton filterBtn = new JButton("TAMPILKAN SISWA");
        filterBtn.setBackground(new Color(52, 152, 219));
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filterPanel.add(new JLabel("Kelas:"));
        filterPanel.add(kelasCombo);
        filterPanel.add(new JLabel("Tanggal (DD-MM-YYYY):"));
        filterPanel.add(tanggalField);
        filterPanel.add(filterBtn);
        
        String[] columns = {"No", "NISN", "Nama Siswa", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        TableColumn statusColumn = table.getColumnModel().getColumn(3);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Hadir", "Sakit", "Izin", "Alpa"});
        statusColumn.setCellEditor(new DefaultCellEditor(statusCombo));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton simpanBtn = new JButton("SIMPAN ABSENSI");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);
        simpanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        
        JButton backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        
        btnPanel.add(simpanBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        filterBtn.addActionListener(e -> loadSiswaByKelas());
        simpanBtn.addActionListener(e -> simpanAbsensi());
        refreshBtn.addActionListener(e -> {
            loadKelas();
            loadSiswaByKelas();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private String getCurrentDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(new java.util.Date());
    }
    
    private void loadKelas() {
        kelasCombo.removeAllItems();
        for (String[] kelas : Database.getAllKelas()) {
            kelasCombo.addItem(kelas[1]);
        }
    }
    
    private void loadSiswaByKelas() {
        if (kelasCombo.getSelectedIndex() == -1) return;
        
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        tableModel.setRowCount(0);
        
        int no = 1;
        for (String[] siswa : Database.getSiswaByKelas(selectedKelas)) {
            String status = "Hadir";
            String tanggal = tanggalField.getText().trim();
            
            for (int i = 0; i < Database.dataAbsensiModel.getRowCount(); i++) {
                if (Database.dataAbsensiModel.getValueAt(i, 0).toString().equals(tanggal) &&
                    Database.dataAbsensiModel.getValueAt(i, 2).toString().equals(siswa[0])) {
                    status = Database.dataAbsensiModel.getValueAt(i, 4).toString();
                    break;
                }
            }
            
            tableModel.addRow(new Object[]{no++, siswa[0], siswa[1], status});
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada siswa di kelas " + selectedKelas);
        }
    }
    
    private void simpanAbsensi() {
        String tanggal = tanggalField.getText().trim();
        String kelas = (String) kelasCombo.getSelectedItem();
        
        if (tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal!");
            return;
        }
        
        for (int i = Database.dataAbsensiModel.getRowCount() - 1; i >= 0; i--) {
            if (Database.dataAbsensiModel.getValueAt(i, 0).toString().equals(tanggal) &&
                Database.dataAbsensiModel.getValueAt(i, 1).toString().equals(kelas)) {
                Database.dataAbsensiModel.removeRow(i);
            }
        }
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String nisn = tableModel.getValueAt(i, 1).toString();
            String nama = tableModel.getValueAt(i, 2).toString();
            String status = tableModel.getValueAt(i, 3).toString();
            
            Database.dataAbsensiModel.addRow(new Object[]{tanggal, kelas, nisn, nama, status});
        }
        
        JOptionPane.showMessageDialog(this, "Absensi tanggal " + tanggal + " untuk kelas " + kelas + " berhasil disimpan!");
    }
}