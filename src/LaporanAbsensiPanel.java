import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LaporanAbsensiPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> kelasCombo;
    private JComboBox<String> bulanCombo;
    private JTextField tanggalField;
    private JButton filterBtn, refreshBtn, cetakHariBtn, cetakBulanBtn, backBtn;
    private JLabel judulLaporan;
    
    public LaporanAbsensiPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
        loadKelas();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 242, 245));
        
        // ============ PANEL FILTER ============
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel filterTitle = new JLabel("FILTER LAPORAN ABSENSI");
        filterTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        filterTitle.setForeground(new Color(41, 128, 185));
        filterPanel.add(filterTitle, BorderLayout.NORTH);
        
        JPanel filterInputPanel = new JPanel(new GridBagLayout());
        filterInputPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Baris 1 - Kelas
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        filterInputPanel.add(new JLabel("Kelas:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        kelasCombo = new JComboBox<>();
        kelasCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterInputPanel.add(kelasCombo, gbc);
        
        // Baris 1 - Tanggal (untuk laporan harian)
        gbc.gridx = 2; gbc.weightx = 0.1;
        filterInputPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.3;
        tanggalField = new JTextField(15);
        tanggalField.setText(getCurrentDate());
        tanggalField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterInputPanel.add(tanggalField, gbc);
        
        // Baris 2 - Bulan (untuk laporan rekap bulanan)
        gbc.gridx = 0; gbc.gridy = 1;
        filterInputPanel.add(new JLabel("Bulan (untuk Rekap):"), gbc);
        gbc.gridx = 1;
        bulanCombo = new JComboBox<>(new String[]{
            "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        bulanCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        filterInputPanel.add(bulanCombo, gbc);
        
        // Tombol Filter
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.2;
        filterBtn = new JButton("TAMPILKAN");
        filterBtn.setBackground(new Color(52, 152, 219));
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        filterInputPanel.add(filterBtn, gbc);
        
        filterPanel.add(filterInputPanel, BorderLayout.CENTER);
        
        // ============ BUTTON PANEL ============
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        cetakHariBtn = new JButton("CETAK PDF (PER HARI)");
        cetakHariBtn.setBackground(new Color(46, 204, 113));
        cetakHariBtn.setForeground(Color.WHITE);
        cetakHariBtn.setFocusPainted(false);
        cetakHariBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        cetakBulanBtn = new JButton("CETAK PDF (REKAP BULANAN)");
        cetakBulanBtn.setBackground(new Color(52, 152, 219));
        cetakBulanBtn.setForeground(Color.WHITE);
        cetakBulanBtn.setFocusPainted(false);
        cetakBulanBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(108, 117, 125));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        
        btnPanel.add(cetakHariBtn);
        btnPanel.add(cetakBulanBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        filterPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(filterPanel, BorderLayout.NORTH);
        
        // ============ TABEL PANEL ============
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        judulLaporan = new JLabel("LAPORAN ABSENSI", SwingConstants.CENTER);
        judulLaporan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        judulLaporan.setForeground(new Color(41, 128, 185));
        tableContainer.add(judulLaporan, BorderLayout.NORTH);
        
        // Tabel (dinamis tergantung filter)
        String[] columns = {"No", "NISN", "Nama Siswa", "Kelas", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);
        
        // ============ EVENT HANDLERS ============
        filterBtn.addActionListener(e -> tampilkanAbsensiHarian());
        cetakHariBtn.addActionListener(e -> cetakPDFHarian());
        cetakBulanBtn.addActionListener(e -> cetakPDFRekapBulanan());
        refreshBtn.addActionListener(e -> {
            loadKelas();
            loadData();
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
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
    
    private void loadData() {
        tableModel.setRowCount(0);
        judulLaporan.setText("LAPORAN ABSENSI");
    }
    
    private void tampilkanAbsensiHarian() {
        if (kelasCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kelas terlebih dahulu!");
            return;
        }
        
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String tanggal = tanggalField.getText().trim();
        
        if (tanggal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan tanggal!");
            return;
        }
        
        tableModel.setRowCount(0);
        judulLaporan.setText("LAPORAN ABSENSI HARI " + tanggal + " - KELAS " + selectedKelas);
        
        int no = 1;
        for (String[] siswa : Database.getSiswaByKelas(selectedKelas)) {
            String nisn = siswa[0];
            String nama = siswa[2];
            String kelas = siswa[10];
            String status = "-";
            
            for (Object[] absen : Database.getAllAbsensi()) {
                if (absen[0].toString().equals(tanggal) && absen[2].toString().equals(nisn)) {
                    status = absen[4].toString();
                    break;
                }
            }
            
            tableModel.addRow(new Object[]{no++, nisn, nama, kelas, status});
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data absensi untuk tanggal " + tanggal);
        } else {
            JOptionPane.showMessageDialog(this, "Menampilkan " + tableModel.getRowCount() + " data absensi");
        }
    }
    
    private void cetakPDFHarian() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak!\nKlik TAMPILKAN terlebih dahulu.");
            return;
        }
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            String selectedKelas = (String) kelasCombo.getSelectedItem();
            String tanggal = tanggalField.getText().trim();
            fileChooser.setSelectedFile(new java.io.File("Laporan_Absensi_Harian_" + selectedKelas + "_" + tanggal + ".pdf"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD);
                
                // Logo
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/smapgri4.png");
                    if (imgUrl != null) {
                        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgUrl);
                        logo.scaleToFit(60, 60);
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
                
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("LAPORAN ABSENSI SISWA", headerFont);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                com.itextpdf.text.Paragraph info = new com.itextpdf.text.Paragraph("Kelas: " + selectedKelas + " | Tanggal: " + tanggalField.getText(), normalFont);
                info.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(info);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph cetak = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                cetak.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(cetak);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(5);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{0.5f, 1.2f, 2f, 1.2f, 1.2f});
                
                String[] headers = {"No", "NISN", "Nama Siswa", "Kelas", "Status"};
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < 5; j++) {
                        String value = tableModel.getValueAt(i, j) != null ? tableModel.getValueAt(i, j).toString() : "";
                        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(value, normalFont));
                        cell.setPadding(6);
                        pdfTable.addCell(cell);
                    }
                }
                
                document.add(pdfTable);
                
                com.itextpdf.text.Paragraph signature = new com.itextpdf.text.Paragraph("\n\nMengetahui,\nKepala Sekolah\n\n\n\n(.................................)", normalFont);
                signature.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(signature);
                
                document.close();
                JOptionPane.showMessageDialog(this, "PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void cetakPDFRekapBulanan() {
        if (kelasCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kelas terlebih dahulu!");
            return;
        }
        
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String selectedBulan = (String) bulanCombo.getSelectedItem();
        int bulanNumber = getBulanNumber(selectedBulan);
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("Rekap_Absensi_" + selectedKelas + "_" + selectedBulan + ".pdf"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD);
                
                // Logo
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/smapgri4.png");
                    if (imgUrl != null) {
                        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgUrl);
                        logo.scaleToFit(60, 60);
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
                
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("REKAP ABSENSI SISWA", headerFont);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                com.itextpdf.text.Paragraph info = new com.itextpdf.text.Paragraph("Kelas: " + selectedKelas + " | Bulan: " + selectedBulan, normalFont);
                info.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(info);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph cetak = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                cetak.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(cetak);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel Rekap
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(6);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{0.5f, 1.2f, 2f, 1f, 1f, 1f});
                
                String[] headers = {"No", "NISN", "Nama Siswa", "Hadir", "Sakit", "Izin", "Alpa"};
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }
                
                int no = 1;
                for (String[] siswa : Database.getSiswaByKelas(selectedKelas)) {
                    String nisn = siswa[0];
                    String nama = siswa[2];
                    
                    int hadir = 0, sakit = 0, izin = 0, alpa = 0;
                    
                    for (Object[] absen : Database.getAllAbsensi()) {
                        String tgl = absen[0].toString();
                        String[] tglParts = tgl.split("-");
                        if (tglParts.length >= 2) {
                            int bulanAbsen = Integer.parseInt(tglParts[1]);
                            if (bulanAbsen == bulanNumber && absen[2].toString().equals(nisn)) {
                                String status = absen[4].toString();
                                switch (status) {
                                    case "Hadir": hadir++; break;
                                    case "Sakit": sakit++; break;
                                    case "Izin": izin++; break;
                                    case "Alpa": alpa++; break;
                                }
                            }
                        }
                    }
                    
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(no++), normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(nisn, normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(nama, normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(hadir), normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(sakit), normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(izin), normalFont)));
                    pdfTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(String.valueOf(alpa), normalFont)));
                }
                
                document.add(pdfTable);
                
                com.itextpdf.text.Paragraph signature = new com.itextpdf.text.Paragraph("\n\nMengetahui,\nKepala Sekolah\n\n\n\n(.................................)", normalFont);
                signature.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(signature);
                
                document.close();
                JOptionPane.showMessageDialog(this, "PDF Rekap berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private int getBulanNumber(String bulan) {
        switch (bulan) {
            case "Januari": return 1;
            case "Februari": return 2;
            case "Maret": return 3;
            case "April": return 4;
            case "Mei": return 5;
            case "Juni": return 6;
            case "Juli": return 7;
            case "Agustus": return 8;
            case "September": return 9;
            case "Oktober": return 10;
            case "November": return 11;
            case "Desember": return 12;
            default: return 0;
        }
    }
}