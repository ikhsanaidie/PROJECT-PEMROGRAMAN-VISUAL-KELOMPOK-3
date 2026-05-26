import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LaporanSIswaPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton refreshBtn, cetakBtn, cetakPerKelasBtn, backBtn;
    private JComboBox<String> kelasCombo;
    private JTextField searchField;
    private boolean isLoading = false;
    
    // Index dari Database.getAllSiswa()
    // [0]nisn, [1]nis, [2]nama, [3]jk, [4]tempat_lahir, [5]tgl_lahir, 
    // [6]agama, [7]alamat, [8]no_hp, [9]email, [10]kelas, [11]jurusan, 
    // [12]tahun_masuk, [13]nama_ayah, [14]nama_ibu
    
    public LaporanSIswaPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadKelas();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 242, 245));
        
        // ============ PANEL JUDUL & FILTER ============
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("LAPORAN DATA SISWA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setBackground(Color.WHITE);
        
        filterPanel.add(new JLabel("Filter Kelas:"));
        kelasCombo = new JComboBox<>();
        kelasCombo.setPreferredSize(new Dimension(180, 30));
        kelasCombo.addActionListener(e -> {
            if (!isLoading) {
                filterByKelas();
            }
        });
        filterPanel.add(kelasCombo);
        
        filterPanel.add(Box.createHorizontalStrut(20));
        
        filterPanel.add(new JLabel("Cari (NISN/Nama):"));
        searchField = new JTextField(20);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filterByKelas();
            }
        });
        filterPanel.add(searchField);
        
        JButton cariBtn = new JButton("CARI");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.addActionListener(e -> filterByKelas());
        filterPanel.add(cariBtn);
        
        JButton resetBtn = new JButton("RESET");
        resetBtn.setBackground(new Color(108, 117, 125));
        resetBtn.setForeground(Color.WHITE);
        resetBtn.addActionListener(e -> {
            searchField.setText("");
            kelasCombo.setSelectedIndex(0);
            loadData();
        });
        filterPanel.add(resetBtn);
        
        topPanel.add(filterPanel, BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        refreshBtn = createStyledButton("REFRESH DATA", new Color(52, 152, 219));
        cetakBtn = createStyledButton("CETAK PDF (SEMUA)", new Color(46, 204, 113));
        cetakPerKelasBtn = createStyledButton("CETAK PDF (PER KELAS)", new Color(241, 196, 15));
        backBtn = createStyledButton("KEMBALI KE DASHBOARD", new Color(231, 76, 60));
        
        btnPanel.add(refreshBtn);
        btnPanel.add(cetakBtn);
        btnPanel.add(cetakPerKelasBtn);
        btnPanel.add(backBtn);
        
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        
        // ============ PANEL TABEL (16 KOLOM LENGKAP) ============
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // 16 KOLOM LENGKAP sesuai database
        String[] columns = {
            "No", "NISN", "NIS", "Nama Lengkap", "JK", "Tempat Lahir", 
            "Tgl Lahir", "Agama", "Alamat", "No HP", "Email", 
            "Kelas", "Jurusan", "Thn Masuk", "Nama Ayah", "Nama Ibu"
        };
        
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Lebar kolom
        int[] widths = {35, 85, 85, 150, 45, 100, 85, 60, 180, 90, 120, 85, 70, 70, 120, 120};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 10));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 30));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // ============ EVENT HANDLERS ============
        refreshBtn.addActionListener(e -> {
            kelasCombo.setSelectedIndex(0);
            searchField.setText("");
            loadData();
        });
        cetakBtn.addActionListener(e -> cetakPDF("semua"));
        cetakPerKelasBtn.addActionListener(e -> {
            if (kelasCombo.getSelectedItem() == null || kelasCombo.getSelectedItem().toString().equals("-- Semua Kelas --")) {
                JOptionPane.showMessageDialog(this, "Pilih kelas terlebih dahulu!");
                return;
            }
            cetakPDF("perkelas");
        });
        backBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "menuUtama");
            for (Component comp : mainPanel.getComponents()) {
                if (comp instanceof MenuUtamaPanel) {
                    ((MenuUtamaPanel) comp).showDashboard();
                    break;
                }
            }
        });
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void loadKelas() {
        isLoading = true;
        kelasCombo.removeAllItems();
        kelasCombo.addItem("-- Semua Kelas --");
        List<String[]> kelasList = Database.getAllKelas();
        for (String[] k : kelasList) {
            if (k.length > 1 && k[1] != null && !k[1].isEmpty()) {
                kelasCombo.addItem(k[1]);
            }
        }
        isLoading = false;
    }
    
    private void filterByKelas() {
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String keyword = searchField.getText().trim().toLowerCase();
        
        tableModel.setRowCount(0);
        int no = 1;
        
        List<String[]> siswaList = Database.getAllSiswa();
        for (String[] siswa : siswaList) {
            if (siswa.length < 15) continue;
            
            // Index dari Database.getAllSiswa()
            String nisn = siswa[0] != null ? siswa[0] : "";
            String nis = siswa[1] != null ? siswa[1] : "";
            String nama = siswa[2] != null ? siswa[2] : "";
            String jk = siswa[3] != null ? siswa[3] : "";
            String tempatLahir = siswa[4] != null ? siswa[4] : "";
            String tglLahir = siswa[5] != null ? siswa[5] : "";
            String agama = siswa[6] != null ? siswa[6] : "";
            String alamat = siswa[7] != null ? siswa[7] : "";
            String noHp = siswa[8] != null ? siswa[8] : "";
            String email = siswa[9] != null ? siswa[9] : "";
            String kelas = siswa[10] != null ? siswa[10] : "";
            String jurusan = siswa[11] != null ? siswa[11] : "";
            String thnMasuk = siswa[12] != null ? siswa[12] : "";
            String ayah = siswa[13] != null ? siswa[13] : "";
            String ibu = siswa[14] != null ? siswa[14] : "";
            
            boolean matchKelas = selectedKelas == null || selectedKelas.equals("-- Semua Kelas --") || kelas.equals(selectedKelas);
            boolean matchCari = keyword.isEmpty() || nisn.toLowerCase().contains(keyword) || nama.toLowerCase().contains(keyword);
            
            if (matchKelas && matchCari) {
                tableModel.addRow(new Object[]{
                    no++, nisn, nis, nama, jk, tempatLahir, tglLahir,
                    agama, alamat, noHp, email, kelas, jurusan, thnMasuk, ayah, ibu
                });
            }
        }
        
        if (tableModel.getRowCount() == 0 && !selectedKelas.equals("-- Semua Kelas --")) {
            // Tidak perlu pesan, biarkan kosong
        }
    }
    
    private void loadData() {
        isLoading = true;
        tableModel.setRowCount(0);
        int no = 1;
        
        List<String[]> siswaList = Database.getAllSiswa();
        for (String[] siswa : siswaList) {
            if (siswa.length < 15) continue;
            
            String nisn = siswa[0] != null ? siswa[0] : "";
            String nis = siswa[1] != null ? siswa[1] : "";
            String nama = siswa[2] != null ? siswa[2] : "";
            String jk = siswa[3] != null ? siswa[3] : "";
            String tempatLahir = siswa[4] != null ? siswa[4] : "";
            String tglLahir = siswa[5] != null ? siswa[5] : "";
            String agama = siswa[6] != null ? siswa[6] : "";
            String alamat = siswa[7] != null ? siswa[7] : "";
            String noHp = siswa[8] != null ? siswa[8] : "";
            String email = siswa[9] != null ? siswa[9] : "";
            String kelas = siswa[10] != null ? siswa[10] : "";
            String jurusan = siswa[11] != null ? siswa[11] : "";
            String thnMasuk = siswa[12] != null ? siswa[12] : "";
            String ayah = siswa[13] != null ? siswa[13] : "";
            String ibu = siswa[14] != null ? siswa[14] : "";
            
            tableModel.addRow(new Object[]{
                no++, nisn, nis, nama, jk, tempatLahir, tglLahir,
                agama, alamat, noHp, email, kelas, jurusan, thnMasuk, ayah, ibu
            });
        }
        isLoading = false;
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data siswa.");
        } else {
            // Optional: tampilkan jumlah data
            // System.out.println("Total siswa: " + tableModel.getRowCount());
        }
    }
    
    private void cetakPDF(String tipe) {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak!");
            return;
        }
        
        try {
            String selectedKelas = (String) kelasCombo.getSelectedItem();
            String fileName;
            
            if (tipe.equals("perkelas") && selectedKelas != null && !selectedKelas.equals("-- Semua Kelas --")) {
                fileName = "Laporan_Siswa_Kelas_" + selectedKelas.replace("/", "_") + ".pdf";
            } else {
                fileName = "Laporan_Siswa_SMA_PGRI_4.pdf";
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File(fileName));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 8, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 9, com.itextpdf.text.Font.BOLD);
                
                // Logo
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/smapgri4.png");
                    if (imgUrl != null) {
                        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgUrl);
                        logo.scaleToFit(45, 45);
                        logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                        document.add(logo);
                    }
                } catch (Exception e) {}
                
                // Kop
                com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("SMA PGRI 4 JAKARTA", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                
                com.itextpdf.text.Paragraph subTitle = new com.itextpdf.text.Paragraph("SISTEM INFORMASI AKADEMIK", subTitleFont);
                subTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(subTitle);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Judul
                com.itextpdf.text.Paragraph judul;
                if (tipe.equals("perkelas") && selectedKelas != null && !selectedKelas.equals("-- Semua Kelas --")) {
                    judul = new com.itextpdf.text.Paragraph("LAPORAN DATA SISWA - KELAS " + selectedKelas, headerFont);
                } else {
                    judul = new com.itextpdf.text.Paragraph("LAPORAN DATA SISWA", headerFont);
                }
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph tanggal = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                tanggal.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(tanggal);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel 16 kolom
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(16);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{0.25f, 0.8f, 0.8f, 1.3f, 0.4f, 0.9f, 0.8f, 0.6f, 1.6f, 0.8f, 1.0f, 0.8f, 0.6f, 0.6f, 1.0f, 1.0f});
                
                String[] headers = {
                    "No", "NISN", "NIS", "Nama", "JK", "Tempat Lahir", "Tgl Lahir", "Agama",
                    "Alamat", "No HP", "Email", "Kelas", "Jurusan", "Thn Masuk", "Ayah", "Ibu"
                };
                
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(4);
                    pdfTable.addCell(headerCell);
                }
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < 16; j++) {
                        String value = tableModel.getValueAt(i, j) != null ? tableModel.getValueAt(i, j).toString() : "";
                        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(value, normalFont));
                        cell.setPadding(3);
                        pdfTable.addCell(cell);
                    }
                }
                
                document.add(pdfTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Footer
                com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph("Dicetak dari Sistem Informasi Akademik SMA PGRI 4 Jakarta", normalFont);
                footer.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(footer);
                
                com.itextpdf.text.Paragraph signature = new com.itextpdf.text.Paragraph("\n\nMengetahui,\nKepala Sekolah\n\n\n\n(.................................)", normalFont);
                signature.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(signature);
                
                document.close();
                JOptionPane.showMessageDialog(this, "PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}