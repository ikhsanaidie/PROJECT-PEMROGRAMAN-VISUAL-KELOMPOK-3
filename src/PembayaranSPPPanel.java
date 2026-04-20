import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PembayaranSPPPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField nisnField, namaField, nominalField, tglBayarField;
    private JComboBox<String> bulanCombo, statusCombo;
    private JButton cariBtn, simpanBtn, refreshBtn, backBtn;
    private DefaultTableModel tableModel;
    private JTable table;
    private String currentNISN = "";
    
    public PembayaranSPPPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadDataSPP();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createTitledBorder("Form Pembayaran SPP"));
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
        inputPanel.add(new JLabel("Bulan Pembayaran:"), gbc);
        gbc.gridx = 1;
        bulanCombo = new JComboBox<>(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        inputPanel.add(bulanCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        inputPanel.add(new JLabel("Nominal SPP:"), gbc);
        gbc.gridx = 1;
        nominalField = new JTextField(15);
        nominalField.setText("200000");
        inputPanel.add(nominalField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        inputPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        statusCombo = new JComboBox<>(new String[]{"Lunas", "Belum"});
        inputPanel.add(statusCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        inputPanel.add(new JLabel("Tanggal Bayar:"), gbc);
        gbc.gridx = 1;
        tglBayarField = new JTextField(15);
        tglBayarField.setText(getCurrentDate());
        inputPanel.add(tglBayarField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        
        simpanBtn = new JButton("SIMPAN PEMBAYARAN");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        
        btnPanel.add(simpanBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        inputPanel.add(btnPanel, gbc);
        
        String[] columns = {"NISN", "Nama Siswa", "Bulan", "Nominal", "Status", "Tanggal Bayar"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Pembayaran SPP"));
        
        cariBtn.addActionListener(e -> cariSiswa());
        nisnField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    cariSiswa();
                }
            }
        });
        
        simpanBtn.addActionListener(e -> simpanPembayaran());
        refreshBtn.addActionListener(e -> {
            loadDataSPP();
            resetForm();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    nisnField.setText(tableModel.getValueAt(row, 0).toString());
                    namaField.setText(tableModel.getValueAt(row, 1).toString());
                    bulanCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    nominalField.setText(tableModel.getValueAt(row, 3).toString());
                    statusCombo.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                    tglBayarField.setText(tableModel.getValueAt(row, 5).toString());
                    currentNISN = tableModel.getValueAt(row, 0).toString();
                }
            }
        });
        
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
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
            JOptionPane.showMessageDialog(this, "Siswa ditemukan: " + siswa[1]);
            
            bulanCombo.setSelectedIndex(0);
            nominalField.setText("200000");
            statusCombo.setSelectedIndex(0);
            tglBayarField.setText(getCurrentDate());
        } else {
            JOptionPane.showMessageDialog(this, "Siswa dengan NISN " + nisn + " tidak ditemukan!");
            namaField.setText("");
            currentNISN = "";
        }
    }
    
    private void simpanPembayaran() {
        if (currentNISN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari siswa terlebih dahulu!");
            return;
        }
        
        String nisn = currentNISN;
        String nama = namaField.getText();
        String bulan = (String) bulanCombo.getSelectedItem();
        double nominal;
        String status = (String) statusCombo.getSelectedItem();
        String tglBayar = tglBayarField.getText().trim();
        
        try {
            nominal = Double.parseDouble(nominalField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nominal harus berupa angka!");
            return;
        }
        
        if (tglBayar.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal bayar!");
            return;
        }
        
        if (Database.simpanSPP(nisn, nama, bulan, nominal, status, tglBayar)) {
            loadDataSPP();
            JOptionPane.showMessageDialog(this, "Pembayaran SPP untuk " + nama + " bulan " + bulan + " berhasil disimpan!");
            bulanCombo.setSelectedIndex(0);
            nominalField.setText("200000");
            statusCombo.setSelectedIndex(0);
            tglBayarField.setText(getCurrentDate());
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pembayaran!");
        }
    }
    
    private void resetForm() {
        nisnField.setText("");
        namaField.setText("");
        bulanCombo.setSelectedIndex(0);
        nominalField.setText("200000");
        statusCombo.setSelectedIndex(0);
        tglBayarField.setText(getCurrentDate());
        currentNISN = "";
        nisnField.requestFocus();
    }
    
    private void loadDataSPP() {
        tableModel.setRowCount(0);
        for (Object[] spp : Database.getAllSPP()) {
            tableModel.addRow(spp);
        }
    }
}