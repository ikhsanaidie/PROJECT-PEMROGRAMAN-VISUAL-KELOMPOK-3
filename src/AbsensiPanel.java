import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.table.TableColumn;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        
        filterPanel.add(new JLabel("Kelas:"));
        filterPanel.add(kelasCombo);
        filterPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        filterPanel.add(tanggalField);
        filterPanel.add(filterBtn);
        
        String[] columns = {"No", "NISN", "Nama Siswa", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(35);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        TableColumn statusColumn = table.getColumnModel().getColumn(3);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Hadir", "Sakit", "Izin", "Alpa"});
        statusColumn.setCellEditor(new DefaultCellEditor(statusCombo));
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(Color.WHITE);
        
        JButton simpanBtn = new JButton("SIMPAN ABSENSI");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        
        JButton refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        
        JButton backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
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
            
            for (Object[] absen : Database.getAllAbsensi()) {
                if (absen[0].toString().equals(tanggal) && absen[2].toString().equals(siswa[0])) {
                    status = absen[4].toString();
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
        
        boolean allSuccess = true;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String nisn = tableModel.getValueAt(i, 1).toString();
            String nama = tableModel.getValueAt(i, 2).toString();
            String status = tableModel.getValueAt(i, 3).toString();
            
            if (!Database.simpanAbsensi(tanggal, kelas, nisn, nama, status)) {
                allSuccess = false;
            }
        }
        
        if (allSuccess) {
            JOptionPane.showMessageDialog(this, "Absensi tanggal " + tanggal + " untuk kelas " + kelas + " berhasil disimpan!");
        } else {
            JOptionPane.showMessageDialog(this, "Beberapa data absensi gagal disimpan!");
        }
    }
}