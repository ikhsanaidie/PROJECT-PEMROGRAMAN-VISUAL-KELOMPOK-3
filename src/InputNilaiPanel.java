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
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Input Nilai"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("NISN:"), gbc);
        gbc.gridx = 1;
        nisnField = new JTextField(15);
        inputPanel.add(nisnField, gbc);
        
        gbc.gridx = 2;
        cariBtn = new JButton("CARI SISWA");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        inputPanel.add(cariBtn, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Nama Siswa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        namaField = new JTextField(20);
        namaField.setEditable(false);
        namaField.setBackground(new Color(240, 240, 240));
        inputPanel.add(namaField, gbc);
        gbc.gridwidth = 1;
        
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("Nilai Tugas (20%):"), gbc);
        gbc.gridx = 1;
        tugasField = new JTextField(10);
        inputPanel.add(tugasField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Nilai UTS (30%):"), gbc);
        gbc.gridx = 1;
        utsField = new JTextField(10);
        inputPanel.add(utsField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Nilai UAS (50%):"), gbc);
        gbc.gridx = 1;
        uasField = new JTextField(10);
        inputPanel.add(uasField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Nilai Akhir:"), gbc);
        gbc.gridx = 1;
        akhirField = new JTextField(10);
        akhirField.setEditable(false);
        akhirField.setBackground(new Color(240, 240, 240));
        inputPanel.add(akhirField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        hitungBtn = new JButton("HITUNG NILAI");
        hitungBtn.setBackground(new Color(52, 152, 219));
        hitungBtn.setForeground(Color.WHITE);
        
        simpanBtn = new JButton("SIMPAN NILAI");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        
        resetBtn = new JButton("RESET");
        resetBtn.setBackground(new Color(231, 76, 60));
        resetBtn.setForeground(Color.WHITE);
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        
        btnPanel.add(hitungBtn);
        btnPanel.add(simpanBtn);
        btnPanel.add(resetBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        inputPanel.add(btnPanel, gbc);
        
        String[] columns = {"NISN", "Nama Siswa", "Tugas", "UTS", "UAS", "Nilai Akhir"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Nilai Siswa"));
        
        cariBtn.addActionListener(e -> cariSiswa());
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
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    nisnField.setText(tableModel.getValueAt(row, 0).toString());
                    namaField.setText(tableModel.getValueAt(row, 1).toString());
                    tugasField.setText(tableModel.getValueAt(row, 2).toString());
                    utsField.setText(tableModel.getValueAt(row, 3).toString());
                    uasField.setText(tableModel.getValueAt(row, 4).toString());
                    akhirField.setText(tableModel.getValueAt(row, 5).toString());
                    currentNISN = tableModel.getValueAt(row, 0).toString();
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
            namaField.setText(siswa[1]);
            currentNISN = nisn;
            
            List<Object[]> nilaiList = Database.getAllNilai();
            for (Object[] nilai : nilaiList) {
                if (nilai[0].toString().equals(nisn)) {
                    tugasField.setText(nilai[2].toString());
                    utsField.setText(nilai[3].toString());
                    uasField.setText(nilai[4].toString());
                    akhirField.setText(nilai[5].toString());
                    JOptionPane.showMessageDialog(this, "Data nilai sudah ada, silakan edit jika perlu.");
                    return;
                }
            }
            
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
        double tugas = Double.parseDouble(tugasField.getText());
        double uts = Double.parseDouble(utsField.getText());
        double uas = Double.parseDouble(uasField.getText());
        double akhir = Double.parseDouble(akhirField.getText());
        
        if (Database.simpanNilai(nisn, nama, tugas, uts, uas, akhir)) {
            loadDataNilai();
            JOptionPane.showMessageDialog(this, "Nilai berhasil disimpan untuk " + nama);
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan nilai!");
        }
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
        for (Object[] nilai : Database.getAllNilai()) {
            tableModel.addRow(nilai);
        }
    }
}