import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PembayaranSPPPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nisnField, namaField, totalTagihanField, dibayarField, sisaTagihanField, tunggakanField;
    private JTextField searchField;
    private JComboBox<String> jenisTagihanCombo, bulanCombo, statusCombo, filterJenisCombo;
    private JButton cariBtn, simpanBtn, cetakNotaBtn, refreshBtn, backBtn, filterBtn;
    private String currentNISN = "";
    private String currentIdPembayaran = "";
    private List<Object[]> allPembayaranCache;
    private NumberFormat rupiahFormat;
    
    public PembayaranSPPPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
        initComponents();
        loadDataPembayaran();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 242, 245));
        
        // ============ FORM PANEL ============
        JPanel formPanel = new JPanel(new BorderLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel formTitle = new JLabel("FORM PEMBAYARAN");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(41, 128, 185));
        formPanel.add(formTitle, BorderLayout.NORTH);
        
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Baris 1 - NISN & CARI
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        gridPanel.add(new JLabel("NISN:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        nisnField = new JTextField();
        nisnField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(nisnField, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        cariBtn = new JButton("CARI SISWA");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.setFocusPainted(false);
        gridPanel.add(cariBtn, gbc);
        
        gbc.gridx = 3; gbc.weightx = 0.15;
        gridPanel.add(new JLabel("Total Tunggakan:"), gbc);
        gbc.gridx = 4; gbc.weightx = 0.25;
        tunggakanField = new JTextField();
        tunggakanField.setEditable(false);
        tunggakanField.setBackground(new Color(255, 255, 200));
        tunggakanField.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gridPanel.add(tunggakanField, gbc);
        
        // Baris 2 - Nama Siswa
        gbc.gridx = 0; gbc.gridy = 1;
        gridPanel.add(new JLabel("Nama Siswa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 4;
        namaField = new JTextField();
        namaField.setEditable(false);
        namaField.setBackground(new Color(240, 240, 240));
        namaField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(namaField, gbc);
        gbc.gridwidth = 1;
        
        // Baris 3 - Jenis Tagihan & Bulan
        gbc.gridx = 0; gbc.gridy = 2;
        gridPanel.add(new JLabel("Jenis Tagihan:"), gbc);
        gbc.gridx = 1;
        jenisTagihanCombo = new JComboBox<>(new String[]{"SPP", "DAFTAR_ULANG", "UANG_GEDUNG"});
        jenisTagihanCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(jenisTagihanCombo, gbc);
        
        gbc.gridx = 2;
        gridPanel.add(new JLabel("Bulan (untuk SPP):"), gbc);
        gbc.gridx = 3;
        bulanCombo = new JComboBox<>(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        bulanCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(bulanCombo, gbc);
        
        // Baris 4 - Total Tagihan & Dibayar
        gbc.gridx = 0; gbc.gridy = 3;
        gridPanel.add(new JLabel("Total Tagihan:"), gbc);
        gbc.gridx = 1;
        totalTagihanField = new JTextField();
        totalTagihanField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(totalTagihanField, gbc);
        
        gbc.gridx = 2;
        gridPanel.add(new JLabel("Jumlah Dibayar:"), gbc);
        gbc.gridx = 3;
        dibayarField = new JTextField();
        dibayarField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(dibayarField, gbc);
        
        // Baris 5 - Sisa Tagihan & Status
        gbc.gridx = 0; gbc.gridy = 4;
        gridPanel.add(new JLabel("Sisa Tagihan:"), gbc);
        gbc.gridx = 1;
        sisaTagihanField = new JTextField();
        sisaTagihanField.setEditable(false);
        sisaTagihanField.setBackground(new Color(240, 240, 240));
        sisaTagihanField.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gridPanel.add(sisaTagihanField, gbc);
        
        gbc.gridx = 2;
        gridPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 3;
        statusCombo = new JComboBox<>(new String[]{"Belum Bayar", "Cicilan", "Lunas"});
        statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        gridPanel.add(statusCombo, gbc);
        
        formPanel.add(gridPanel, BorderLayout.CENTER);
        
        // ============ BUTTON PANEL ============
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        simpanBtn = new JButton("SIMPAN PEMBAYARAN");
        simpanBtn.setBackground(new Color(46, 204, 113));
        simpanBtn.setForeground(Color.WHITE);
        simpanBtn.setFocusPainted(false);
        
        cetakNotaBtn = new JButton("CETAK NOTA");
        cetakNotaBtn.setBackground(new Color(52, 152, 219));
        cetakNotaBtn.setForeground(Color.WHITE);
        cetakNotaBtn.setFocusPainted(false);
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(108, 117, 125));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        
        btnPanel.add(simpanBtn);
        btnPanel.add(cetakNotaBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        formPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(formPanel, BorderLayout.NORTH);
        
        // ============ TABEL RIWAYAT ============
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("RIWAYAT PEMBAYARAN");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(41, 128, 185));
        tableContainer.add(tableTitle, BorderLayout.NORTH);
        
        // Filter Panel
        JPanel filterTablePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterTablePanel.setBackground(Color.WHITE);
        filterTablePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        filterTablePanel.add(new JLabel("Cari (NISN/Nama):"));
        searchField = new JTextField(20);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                filterTableData();
            }
        });
        filterTablePanel.add(searchField);
        
        filterTablePanel.add(new JLabel("Jenis:"));
        filterJenisCombo = new JComboBox<>(new String[]{"Semua", "SPP", "DAFTAR_ULANG", "UANG_GEDUNG"});
        filterJenisCombo.addActionListener(e -> filterTableData());
        filterTablePanel.add(filterJenisCombo);
        
        JButton resetFilterBtn = new JButton("RESET FILTER");
        resetFilterBtn.addActionListener(e -> {
            searchField.setText("");
            filterJenisCombo.setSelectedIndex(0);
            loadDataPembayaran();
        });
        filterTablePanel.add(resetFilterBtn);
        
        tableContainer.add(filterTablePanel, BorderLayout.NORTH);
        
        String[] columns = {"ID", "NISN", "Nama", "Jenis", "Bulan", "Total", "Dibayar", "Sisa", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(90);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);
        table.getColumnModel().getColumn(8).setPreferredWidth(80);
        table.getColumnModel().getColumn(9).setPreferredWidth(100);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        // ============ LAYOUT UTAMA ============
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formPanel, tableContainer);
        splitPane.setResizeWeight(0.45);
        splitPane.setDividerSize(8);
        
        add(splitPane, BorderLayout.CENTER);
        
        // ============ EVENT HANDLERS ============
        cariBtn.addActionListener(e -> cariSiswa());
        nisnField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) cariSiswa();
            }
        });
        
        jenisTagihanCombo.addActionListener(e -> setDefaultTagihan());
        dibayarField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { hitungSisa(); }
        });
        totalTagihanField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { hitungSisa(); }
        });
        
        simpanBtn.addActionListener(e -> simpanPembayaran());
        cetakNotaBtn.addActionListener(e -> cetakNota());
        refreshBtn.addActionListener(e -> {
            loadDataPembayaran();
            resetForm();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    currentIdPembayaran = tableModel.getValueAt(row, 0).toString();
                    nisnField.setText(tableModel.getValueAt(row, 1).toString());
                    namaField.setText(tableModel.getValueAt(row, 2).toString());
                    jenisTagihanCombo.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    String bulan = tableModel.getValueAt(row, 4).toString();
                    if (!bulan.equals("-") && !bulan.isEmpty()) {
                        bulanCombo.setSelectedItem(bulan);
                    }
                    
                    // Parsing angka dengan benar
                    String totalStr = tableModel.getValueAt(row, 5).toString();
                    String dibayarStr = tableModel.getValueAt(row, 6).toString();
                    String sisaStr = tableModel.getValueAt(row, 7).toString();
                    
                    double total = parseRupiahToDouble(totalStr);
                    double dibayar = parseRupiahToDouble(dibayarStr);
                    double sisa = parseRupiahToDouble(sisaStr);
                    
                    totalTagihanField.setText(String.format("%.0f", total));
                    sisaTagihanField.setText(String.format("%,.0f", sisa));
                    statusCombo.setSelectedItem(tableModel.getValueAt(row, 8).toString());
                    currentNISN = tableModel.getValueAt(row, 1).toString();
                    
                    dibayarField.setText(String.format("%.0f", sisa));
                }
            }
        });
    }
    
    private double parseRupiahToDouble(String rupiahStr) {
        try {
            // Hilangkan "Rp", spasi, titik, dan koma
            String clean = rupiahStr.replace("Rp", "").replace(".", "").replace(",00", "").trim();
            return Double.parseDouble(clean);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    
    private String formatRupiah(double amount) {
        return rupiahFormat.format(amount);
    }
    
    private void filterTableData() {
        String keyword = searchField.getText().trim().toLowerCase();
        String selectedJenis = (String) filterJenisCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (Object[] p : allPembayaranCache) {
            String nisn = p[1].toString().toLowerCase();
            String nama = p[2].toString().toLowerCase();
            String jenis = p[3].toString();
            
            boolean matchKeyword = keyword.isEmpty() || nisn.contains(keyword) || nama.contains(keyword);
            boolean matchJenis = selectedJenis.equals("Semua") || jenis.equals(selectedJenis);
            
            if (matchKeyword && matchJenis) {
                double total = (double) p[5];
                double dibayar = (double) p[6];
                double sisa = (double) p[7];
                
                tableModel.addRow(new Object[]{
                    p[0], p[1], p[2], p[3], 
                    p[4] != null && !p[4].toString().isEmpty() ? p[4] : "-",
                    formatRupiah(total), formatRupiah(dibayar), formatRupiah(sisa),
                    p[8], p[9]
                });
            }
        }
    }
    
    private void setDefaultTagihan() {
        String jenis = (String) jenisTagihanCombo.getSelectedItem();
        switch (jenis) {
            case "SPP":
                totalTagihanField.setText("250000");
                bulanCombo.setEnabled(true);
                break;
            case "DAFTAR_ULANG":
                totalTagihanField.setText("300000");
                bulanCombo.setEnabled(false);
                bulanCombo.setSelectedIndex(0);
                break;
            case "UANG_GEDUNG":
                totalTagihanField.setText("1500000");
                bulanCombo.setEnabled(false);
                bulanCombo.setSelectedIndex(0);
                break;
        }
        hitungSisa();
    }
    
    private void hitungSisa() {
        try {
            double total = totalTagihanField.getText().isEmpty() ? 0 : Double.parseDouble(totalTagihanField.getText());
            double dibayar = dibayarField.getText().isEmpty() ? 0 : Double.parseDouble(dibayarField.getText());
            double sisa = total - dibayar;
            sisaTagihanField.setText(String.format("%,.0f", sisa));
            
            if (sisa <= 0) {
                statusCombo.setSelectedItem("Lunas");
            } else if (dibayar > 0 && sisa > 0) {
                statusCombo.setSelectedItem("Cicilan");
            } else {
                statusCombo.setSelectedItem("Belum Bayar");
            }
        } catch (NumberFormatException e) {
            sisaTagihanField.setText("0");
        }
    }
    
    private void cariSiswa() {
        String nisn = nisnField.getText().trim();
        if (nisn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NISN terlebih dahulu!");
            return;
        }
        
        String[] siswa = Database.getSiswaByNISN(nisn);
        if (siswa != null) {
            namaField.setText(siswa[2]);
            currentNISN = nisn;
            
            double totalTunggakan = 0;
            StringBuilder tagihanList = new StringBuilder();
            
            for (Object[] p : allPembayaranCache) {
                if (p[1].toString().equals(nisn)) {
                    double sisa = (double) p[7];
                    String status = p[8].toString();
                    if (!status.equals("Lunas") && sisa > 0) {
                        totalTunggakan += sisa;
                        tagihanList.append("• ").append(p[3].toString());
                        String bulan = p[4] != null ? p[4].toString() : "";
                        if (!bulan.isEmpty() && !bulan.equals("-")) {
                            tagihanList.append(" (").append(bulan).append(")");
                        }
                        tagihanList.append(": Rp ").append(String.format("%,.0f", sisa)).append("\n");
                    }
                }
            }
            
            tunggakanField.setText(formatRupiah(totalTunggakan));
            setDefaultTagihan();
            
            if (tagihanList.length() > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Siswa: " + siswa[2] + " (NISN: " + nisn + ")\n\n" +
                    "TAGIHAN YANG BELUM LUNAS:\n" + tagihanList.toString() +
                    "\nTotal tunggakan: " + formatRupiah(totalTunggakan) + "\n\n" +
                    "Silakan pilih tagihan dari tabel di bawah untuk melakukan pembayaran.", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Siswa: " + siswa[2] + " (NISN: " + nisn + ")\n\n" +
                    "Tidak ada tagihan yang belum lunas.", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Siswa dengan NISN " + nisn + " tidak ditemukan!");
            resetForm();
        }
    }
    
    private void simpanPembayaran() {
        if (currentIdPembayaran.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Pilih data pembayaran dari tabel terlebih dahulu!");
            return;
        }
        
        double dibayarBaru;
        try {
            dibayarBaru = Double.parseDouble(dibayarField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Masukkan nominal yang valid!");
            return;
        }
        
        if (dibayarBaru <= 0) {
            JOptionPane.showMessageDialog(this, "Jumlah dibayar harus lebih dari 0!");
            return;
        }
        
        Object[] selectedPembayaran = null;
        for (Object[] p : allPembayaranCache) {
            if (p[0].toString().equals(currentIdPembayaran)) {
                selectedPembayaran = p;
                break;
            }
        }
        
        if (selectedPembayaran == null) {
            JOptionPane.showMessageDialog(this, "Data pembayaran tidak ditemukan!");
            return;
        }
        
        double totalTagihan = (double) selectedPembayaran[5];
        double existingDibayar = (double) selectedPembayaran[6];
        double sisaLama = (double) selectedPembayaran[7];
        
        if (dibayarBaru > sisaLama) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Jumlah dibayar melebihi sisa tagihan!\n" +
                "Sisa tagihan: " + formatRupiah(sisaLama) + "\n" +
                "Apakah Anda ingin membayar sesuai sisa tagihan?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dibayarBaru = sisaLama;
                dibayarField.setText(String.format("%.0f", dibayarBaru));
            } else {
                return;
            }
        }
        
        double totalDibayarBaru = existingDibayar + dibayarBaru;
        double sisaBaru = totalTagihan - totalDibayarBaru;
        if (sisaBaru < 0) sisaBaru = 0;
        String statusBaru = (sisaBaru <= 0) ? "Lunas" : "Cicilan";
        String tglBayar = getCurrentDate();
        
        boolean success = updatePembayaranById(currentIdPembayaran, totalDibayarBaru, sisaBaru, statusBaru, tglBayar);
        
        if (success) {
            loadDataPembayaran();
            JOptionPane.showMessageDialog(this, 
                "PEMBAYARAN BERHASIL!\n\n" +
                "Jumlah dibayar: " + formatRupiah(dibayarBaru) + "\n" +
                "Sisa tagihan: " + formatRupiah(sisaBaru) + "\n" +
                "Status: " + statusBaru);
            
            double totalTunggakan = 0;
            for (Object[] p : allPembayaranCache) {
                if (p[1].toString().equals(currentNISN)) {
                    double sisa = (double) p[7];
                    String status = p[8].toString();
                    if (!status.equals("Lunas") && sisa > 0) {
                        totalTunggakan += sisa;
                    }
                }
            }
            tunggakanField.setText(formatRupiah(totalTunggakan));
            
            int cetak = JOptionPane.showConfirmDialog(this, 
                "Apakah ingin mencetak nota pembayaran?", 
                "Cetak Nota", JOptionPane.YES_NO_OPTION);
            if (cetak == JOptionPane.YES_OPTION) {
                cetakNotaDenganData(currentIdPembayaran, totalTagihan, totalDibayarBaru, sisaBaru, statusBaru, tglBayar, dibayarBaru);
            }
            
            resetForm();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pembayaran!");
        }
    }
    
    private boolean updatePembayaranById(String id, double totalDibayar, double sisa, String status, String tglBayar) {
        String query = "UPDATE tbl_pembayaran SET dibayar = ?, sisa_tagihan = ?, status = ?, tgl_bayar = ? WHERE id_pembayaran = ?";
        try (java.sql.PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setDouble(1, totalDibayar);
            pstmt.setDouble(2, sisa);
            pstmt.setString(3, status);
            pstmt.setString(4, tglBayar);
            pstmt.setString(5, id);
            return pstmt.executeUpdate() > 0;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void cetakNotaDenganData(String id, double total, double dibayar, double sisa, String status, String tgl, double pembayaranIni) {
        try {
            String nisn = nisnField.getText();
            String nama = namaField.getText();
            String jenis = (String) jenisTagihanCombo.getSelectedItem();
            String bulan = (String) bulanCombo.getSelectedItem();
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("Nota_Pembayaran_" + nisn + "_" + System.currentTimeMillis() + ".pdf"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.BOLD);
                
                com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("SMA PGRI 4 JAKARTA", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                
                com.itextpdf.text.Paragraph subTitle = new com.itextpdf.text.Paragraph("BUKTI PEMBAYARAN", boldFont);
                subTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(subTitle);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("=================================================="));
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("in", "ID"));
                document.add(new com.itextpdf.text.Paragraph("No. Transaksi: " + id, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Tanggal: " + sdf.format(new Date()), normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("NISN: " + nisn, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Nama Siswa: " + nama, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Jenis Tagihan: " + jenis, normalFont));
                if (bulan != null && !bulan.isEmpty() && !bulan.equals("null")) {
                    document.add(new com.itextpdf.text.Paragraph("Periode: " + bulan, normalFont));
                }
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("--------------------------------------------------"));
                document.add(new com.itextpdf.text.Paragraph("Total Tagihan: " + formatRupiah(total), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Pembayaran Sebelumnya: " + formatRupiah(dibayar - pembayaranIni), normalFont));
                document.add(new com.itextpdf.text.Paragraph("Pembayaran Kali Ini: " + formatRupiah(pembayaranIni), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Total Dibayar: " + formatRupiah(dibayar), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Sisa Tagihan: " + formatRupiah(sisa), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Status: " + status, boldFont));
                document.add(new com.itextpdf.text.Paragraph("Tanggal Bayar: " + tgl, normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("=================================================="));
                document.add(new com.itextpdf.text.Paragraph("Terima kasih atas pembayaran Anda.", normalFont));
                document.add(new com.itextpdf.text.Paragraph("Simpan bukti ini sebagai arsip.", normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("Petugas", normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("(.........................)", normalFont));
                
                document.close();
                JOptionPane.showMessageDialog(this, "Nota PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error mencetak nota: " + e.getMessage());
        }
    }
    
    private void cetakNota() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data pembayaran dari tabel terlebih dahulu!");
            return;
        }
        
        String id = tableModel.getValueAt(row, 0).toString();
        String nisn = tableModel.getValueAt(row, 1).toString();
        String nama = tableModel.getValueAt(row, 2).toString();
        String jenis = tableModel.getValueAt(row, 3).toString();
        String bulan = tableModel.getValueAt(row, 4).toString();
        double total = parseRupiahToDouble(tableModel.getValueAt(row, 5).toString());
        double dibayar = parseRupiahToDouble(tableModel.getValueAt(row, 6).toString());
        double sisa = parseRupiahToDouble(tableModel.getValueAt(row, 7).toString());
        String status = tableModel.getValueAt(row, 8).toString();
        String tgl = tableModel.getValueAt(row, 9).toString();
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("Nota_Pembayaran_" + nisn + "_" + System.currentTimeMillis() + ".pdf"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.BOLD);
                
                com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("SMA PGRI 4 JAKARTA", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                
                com.itextpdf.text.Paragraph subTitle = new com.itextpdf.text.Paragraph("BUKTI PEMBAYARAN", boldFont);
                subTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(subTitle);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("=================================================="));
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("in", "ID"));
                document.add(new com.itextpdf.text.Paragraph("No. Transaksi: " + id, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Tanggal: " + sdf.format(new Date()), normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("NISN: " + nisn, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Nama Siswa: " + nama, normalFont));
                document.add(new com.itextpdf.text.Paragraph("Jenis Tagihan: " + jenis, normalFont));
                if (!bulan.equals("-") && !bulan.isEmpty()) {
                    document.add(new com.itextpdf.text.Paragraph("Periode: " + bulan, normalFont));
                }
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("--------------------------------------------------"));
                document.add(new com.itextpdf.text.Paragraph("Total Tagihan: " + formatRupiah(total), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Total Dibayar: " + formatRupiah(dibayar), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Sisa Tagihan: " + formatRupiah(sisa), boldFont));
                document.add(new com.itextpdf.text.Paragraph("Status: " + status, boldFont));
                document.add(new com.itextpdf.text.Paragraph("Tanggal Bayar: " + tgl, normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("=================================================="));
                document.add(new com.itextpdf.text.Paragraph("Terima kasih atas pembayaran Anda.", normalFont));
                document.add(new com.itextpdf.text.Paragraph("Simpan bukti ini sebagai arsip.", normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("Petugas", normalFont));
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph("(.........................)", normalFont));
                
                document.close();
                JOptionPane.showMessageDialog(this, "Nota PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error mencetak nota: " + e.getMessage());
        }
    }
    
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }
    
    private void resetForm() {
        nisnField.setText("");
        namaField.setText("");
        tunggakanField.setText("");
        jenisTagihanCombo.setSelectedIndex(0);
        bulanCombo.setSelectedIndex(0);
        totalTagihanField.setText("");
        dibayarField.setText("");
        sisaTagihanField.setText("");
        statusCombo.setSelectedIndex(0);
        currentNISN = "";
        currentIdPembayaran = "";
        nisnField.requestFocus();
    }
    
    private void loadDataPembayaran() {
        tableModel.setRowCount(0);
        allPembayaranCache = Database.getAllPembayaran();
        
        for (Object[] p : allPembayaranCache) {
            if (p.length >= 10) {
                double total = (double) p[5];
                double dibayar = (double) p[6];
                double sisa = (double) p[7];
                
                tableModel.addRow(new Object[]{
                    p[0], p[1], p[2], p[3], 
                    p[4] != null && !p[4].toString().isEmpty() ? p[4] : "-",
                    formatRupiah(total), formatRupiah(dibayar), formatRupiah(sisa),
                    p[8], p[9]
                });
            }
        }
        
        if (!currentNISN.isEmpty()) {
            double totalTunggakan = 0;
            for (Object[] p : allPembayaranCache) {
                if (p[1].toString().equals(currentNISN)) {
                    double sisa = (double) p[7];
                    String status = p[8].toString();
                    if (!status.equals("Lunas") && sisa > 0) {
                        totalTunggakan += sisa;
                    }
                }
            }
            tunggakanField.setText(formatRupiah(totalTunggakan));
        }
    }
}