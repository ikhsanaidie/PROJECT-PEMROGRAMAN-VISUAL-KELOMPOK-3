import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CetakRaportPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> semesterCombo;
    private JComboBox<String> tahunAjaranCombo;
    private JTextField nisnField;
    private JLabel namaLabel, kelasLabel, waliKelasLabel;
    private JLabel absenSakitLabel, absenIzinLabel, absenAlpaLabel;
    private JButton cariBtn, generateRaportBtn, backBtn;
    private JTextArea catatanArea;
    private String currentNISN = "";
    private String currentNama = "";
    private String currentKelas = "";
    private String currentWaliKelas = "";
    
    private List<Object[]> nilaiSiswa;
    
    public CetakRaportPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 242, 245));
        
        // ============ PANEL FILTER ============
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("📄 CETAK RAPORT SISWA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        filterPanel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Baris 1 - Cari NISN
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        formPanel.add(new JLabel("NISN Siswa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.3;
        nisnField = new JTextField();
        nisnField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nisnField, gbc);
        gbc.gridx = 2; gbc.weightx = 0.15;
        cariBtn = new JButton("🔍 CARI SISWA");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.setFocusPainted(false);
        formPanel.add(cariBtn, gbc);
        
        // Baris 2 - Nama Siswa
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama Siswa:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        namaLabel = new JLabel("-");
        namaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        namaLabel.setForeground(new Color(41, 128, 185));
        formPanel.add(namaLabel, gbc);
        gbc.gridwidth = 1;
        
        // Baris 3 - Kelas
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Kelas:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        kelasLabel = new JLabel("-");
        kelasLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        kelasLabel.setForeground(new Color(41, 128, 185));
        formPanel.add(kelasLabel, gbc);
        gbc.gridwidth = 1;
        
        // Baris 4 - Wali Kelas
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Wali Kelas:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2;
        waliKelasLabel = new JLabel("-");
        waliKelasLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        waliKelasLabel.setForeground(new Color(155, 89, 182));
        formPanel.add(waliKelasLabel, gbc);
        gbc.gridwidth = 1;
        
        // Baris 5 - Semester & Tahun Ajaran
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Semester:"), gbc);
        gbc.gridx = 1;
        semesterCombo = new JComboBox<>(new String[]{"Ganjil", "Genap"});
        semesterCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(semesterCombo, gbc);
        gbc.gridx = 2;
        formPanel.add(new JLabel("Tahun Ajaran:"), gbc);
        gbc.gridx = 3;
        tahunAjaranCombo = new JComboBox<>();
        tahunAjaranCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        for (int i = 2022; i <= 2026; i++) {
            tahunAjaranCombo.addItem(i + "/" + (i + 1));
        }
        formPanel.add(tahunAjaranCombo, gbc);
        
        // Baris 6 - Preview Absensi (Sakit, Izin, Alpa saja)
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Rekap Ketidakhadiran:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        JPanel absenPreviewPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        absenPreviewPanel.setBackground(Color.WHITE);
        absenPreviewPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        
        absenSakitLabel = new JLabel("Sakit: 0 hari");
        absenSakitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        absenSakitLabel.setForeground(new Color(52, 152, 219));
        
        absenIzinLabel = new JLabel("Izin: 0 hari");
        absenIzinLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        absenIzinLabel.setForeground(new Color(241, 196, 15));
        
        absenAlpaLabel = new JLabel("Alpa: 0 hari");
        absenAlpaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        absenAlpaLabel.setForeground(new Color(231, 76, 60));
        
        absenPreviewPanel.add(absenSakitLabel);
        absenPreviewPanel.add(absenIzinLabel);
        absenPreviewPanel.add(absenAlpaLabel);
        
        formPanel.add(absenPreviewPanel, gbc);
        gbc.gridwidth = 1;
        
        // Baris 7 - Catatan Wali Kelas
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Catatan Wali Kelas:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        catatanArea = new JTextArea(3, 30);
        catatanArea.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        catatanArea.setLineWrap(true);
        catatanArea.setWrapStyleWord(true);
        catatanArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        catatanArea.setText("Pertahankan prestasinya dan terus tingkatkan belajarnya!");
        JScrollPane catatanScroll = new JScrollPane(catatanArea);
        catatanScroll.setPreferredSize(new Dimension(400, 60));
        formPanel.add(catatanScroll, gbc);
        gbc.gridwidth = 1;
        
        // Baris 8 - Tombol Generate
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 4;
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        
        generateRaportBtn = new JButton("📄 GENERATE RAPORT PDF");
        generateRaportBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        generateRaportBtn.setBackground(new Color(46, 204, 113));
        generateRaportBtn.setForeground(Color.WHITE);
        generateRaportBtn.setFocusPainted(false);
        generateRaportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        generateRaportBtn.setEnabled(false);
        
        backBtn = new JButton("◀ KEMBALI");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setBackground(new Color(52, 73, 94));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPanel.add(generateRaportBtn);
        btnPanel.add(backBtn);
        formPanel.add(btnPanel, gbc);
        
        filterPanel.add(formPanel, BorderLayout.CENTER);
        add(filterPanel, BorderLayout.NORTH);
        
        // ============ TABEL NILAI (PREVIEW) ============
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tableTitle = new JLabel("📊 PREVIEW NILAI SISWA");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(new Color(41, 128, 185));
        tableContainer.add(tableTitle, BorderLayout.NORTH);
        
        String[] columns = {"No", "Mata Pelajaran", "Guru", "Tugas", "UTS", "UAS", "Nilai Akhir", "Predikat"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(28);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        int[] widths = {40, 180, 130, 70, 70, 70, 90, 90};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);
        
        // ============ EVENT HANDLERS ============
        nisnField.addActionListener(e -> cariSiswa());
        cariBtn.addActionListener(e -> cariSiswa());
        semesterCombo.addActionListener(e -> {
            if (!currentNISN.isEmpty()) updatePreviewAbsensi();
        });
        generateRaportBtn.addActionListener(e -> generateRaportPDF());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
    }
    
    private void cariSiswa() {
        String nisn = nisnField.getText().trim();
        if (nisn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Masukkan NISN terlebih dahulu!");
            return;
        }
        
        String[] siswa = Database.getSiswaByNISN(nisn);
        if (siswa != null) {
            currentNISN = nisn;
            currentNama = siswa[2];
            currentKelas = siswa[10];
            
            currentWaliKelas = cariWaliKelas(currentKelas);
            
            namaLabel.setText(currentNama);
            kelasLabel.setText(currentKelas);
            waliKelasLabel.setText(currentWaliKelas);
            
            loadNilaiPreview();
            updatePreviewAbsensi();
            generateRaportBtn.setEnabled(true);
            
            JOptionPane.showMessageDialog(this, "Siswa ditemukan:\nNama: " + currentNama + "\nKelas: " + currentKelas + "\nWali Kelas: " + currentWaliKelas);
        } else {
            JOptionPane.showMessageDialog(this, "Siswa dengan NISN " + nisn + " tidak ditemukan!");
            namaLabel.setText("-");
            kelasLabel.setText("-");
            waliKelasLabel.setText("-");
            tableModel.setRowCount(0);
            currentNISN = "";
            generateRaportBtn.setEnabled(false);
            resetPreviewAbsensi();
        }
    }
    
    private void updatePreviewAbsensi() {
        if (currentNISN.isEmpty()) return;
        int[] kehadiran = hitungKehadiran();
        absenSakitLabel.setText("Sakit: " + kehadiran[0] + " hari");
        absenIzinLabel.setText("Izin: " + kehadiran[1] + " hari");
        absenAlpaLabel.setText("Alpa: " + kehadiran[2] + " hari");
    }
    
    private void resetPreviewAbsensi() {
        absenSakitLabel.setText("Sakit: 0 hari");
        absenIzinLabel.setText("Izin: 0 hari");
        absenAlpaLabel.setText("Alpa: 0 hari");
    }
    
    private String cariWaliKelas(String namaKelas) {
        List<String[]> kelasList = Database.getAllKelas();
        for (String[] kelas : kelasList) {
            if (kelas.length > 2 && kelas[1].equalsIgnoreCase(namaKelas)) {
                return kelas[2];
            }
        }
        return "-";
    }
    
    private void loadNilaiPreview() {
        tableModel.setRowCount(0);
        nilaiSiswa = Database.getAllNilai();
        
        int no = 1;
        double totalNilai = 0;
        int mapelCount = 0;
        
        for (Object[] nilai : nilaiSiswa) {
            String nisn = nilai[0].toString();
            if (nisn.equals(currentNISN)) {
                String mapel = nilai[2].toString();
                String guru = nilai[3].toString();
                double tugas = (double) nilai[4];
                double uts = (double) nilai[5];
                double uas = (double) nilai[6];
                double akhir = (double) nilai[7];
                
                String predikat = getPredikat(akhir);
                
                tableModel.addRow(new Object[]{no++, mapel, guru, tugas, uts, uas, akhir, predikat});
                totalNilai += akhir;
                mapelCount++;
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            tableModel.addRow(new Object[]{"-", "Belum ada data nilai", "-", "-", "-", "-", "-", "-"});
        } else {
            double rataRata = totalNilai / mapelCount;
            String predikatAkhir = getPredikat(rataRata);
            tableModel.addRow(new Object[]{"", "TOTAL / RATA-RATA", "", "", "", "", String.format("%.1f", rataRata), predikatAkhir});
        }
    }
    
    private String getPredikat(double nilai) {
        if (nilai >= 90) return "A";
        else if (nilai >= 80) return "A-";
        else if (nilai >= 70) return "B+";
        else if (nilai >= 60) return "B";
        else return "C";
    }
    
    // HITUNG KETIDAKHADIRAN (SAKIT, IZIN, ALPA) 6 BULAN TERAKHIR
    private int[] hitungKehadiran() {
        int sakit = 0, izin = 0, alpa = 0;
        
        List<Object[]> absensiList = Database.getAllAbsensi();
        
        // Hitung tanggal 6 bulan terakhir dari hari ini
        java.util.Calendar cal = java.util.Calendar.getInstance();
        java.util.Date today = new java.util.Date();
        cal.setTime(today);
        cal.add(java.util.Calendar.MONTH, -6);
        java.util.Date sixMonthsAgo = cal.getTime();
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sixMonthsAgoStr = sdf.format(sixMonthsAgo);
        
        for (Object[] absen : absensiList) {
            String tglStr = absen[0].toString();
            String nisn = absen[2].toString();
            String status = absen[4].toString();
            
            if (nisn.equals(currentNISN)) {
                if (tglStr.compareTo(sixMonthsAgoStr) >= 0) {
                    switch (status) {
                        case "Sakit": sakit++; break;
                        case "Izin": izin++; break;
                        case "Alpa": alpa++; break;
                    }
                }
            }
        }
        return new int[]{sakit, izin, alpa};
    }
    
    private void generateRaportPDF() {
        if (currentNISN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cari siswa terlebih dahulu!");
            return;
        }
        
        try {
            String semester = (String) semesterCombo.getSelectedItem();
            String tahunAjaran = (String) tahunAjaranCombo.getSelectedItem();
            String catatanWali = catatanArea.getText().trim();
            if (catatanWali.isEmpty()) {
                catatanWali = "Pertahankan prestasinya dan terus tingkatkan belajarnya!";
            }
            
            int[] kehadiran = hitungKehadiran();
            int sakit = kehadiran[0];
            int izin = kehadiran[1];
            int alpa = kehadiran[2];
            
            JFileChooser fileChooser = new JFileChooser();
            String fileName = "Raport_" + currentNISN + "_" + currentNama.replace(" ", "_") + "_" + semester + "_" + tahunAjaran + ".pdf";
            fileChooser.setSelectedFile(new java.io.File(fileName));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                // Font
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 16, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 12, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 9, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font boldFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font italicFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 9, com.itextpdf.text.Font.ITALIC);
                
                // Logo
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/smapgri4.png");
                    if (imgUrl != null) {
                        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgUrl);
                        logo.scaleToFit(50, 50);
                        logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                        document.add(logo);
                    }
                } catch (Exception e) {}
                
                // Kop Surat
                com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("SMA PGRI 4 JAKARTA", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                
                com.itextpdf.text.Paragraph subTitle = new com.itextpdf.text.Paragraph("SISTEM INFORMASI AKADEMIK", subTitleFont);
                subTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(subTitle);
                
                com.itextpdf.text.Paragraph raportTitle = new com.itextpdf.text.Paragraph("LAPORAN HASIL BELAJAR (RAPORT)", headerFont);
                raportTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(raportTitle);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // INFO SISWA - PAKAI TABEL 2 KOLOM AGAR TITIK DUA RATA
                com.itextpdf.text.pdf.PdfPTable infoTable = new com.itextpdf.text.pdf.PdfPTable(2);
                infoTable.setWidthPercentage(70);
                infoTable.setWidths(new float[]{0.25f, 0.75f});
                infoTable.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                
                // Baris 1: Nama Siswa
                com.itextpdf.text.pdf.PdfPCell label1 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Nama Siswa", normalFont));
                label1.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label1.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value1 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + currentNama, normalFont));
                value1.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value1.setPadding(3);
                infoTable.addCell(label1);
                infoTable.addCell(value1);
                
                // Baris 2: NISN
                com.itextpdf.text.pdf.PdfPCell label2 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("NISN", normalFont));
                label2.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label2.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value2 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + currentNISN, normalFont));
                value2.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value2.setPadding(3);
                infoTable.addCell(label2);
                infoTable.addCell(value2);
                
                // Baris 3: Kelas
                com.itextpdf.text.pdf.PdfPCell label3 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Kelas", normalFont));
                label3.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label3.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value3 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + currentKelas, normalFont));
                value3.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value3.setPadding(3);
                infoTable.addCell(label3);
                infoTable.addCell(value3);
                
                // Baris 4: Wali Kelas
                com.itextpdf.text.pdf.PdfPCell label4 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Wali Kelas", normalFont));
                label4.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label4.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value4 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + currentWaliKelas, normalFont));
                value4.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value4.setPadding(3);
                infoTable.addCell(label4);
                infoTable.addCell(value4);
                
                // Baris 5: Semester
                com.itextpdf.text.pdf.PdfPCell label5 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Semester", normalFont));
                label5.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label5.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value5 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + semester, normalFont));
                value5.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value5.setPadding(3);
                infoTable.addCell(label5);
                infoTable.addCell(value5);
                
                // Baris 6: Tahun Ajaran
                com.itextpdf.text.pdf.PdfPCell label6 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Tahun Ajaran", normalFont));
                label6.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                label6.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell value6 = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + tahunAjaran, normalFont));
                value6.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                value6.setPadding(3);
                infoTable.addCell(label6);
                infoTable.addCell(value6);
                
                document.add(infoTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // REKAP KETIDAKHADIRAN (Sakit, Izin, Alpa saja) - pakai tabel juga biar rapi
                com.itextpdf.text.pdf.PdfPTable absenTable = new com.itextpdf.text.pdf.PdfPTable(2);
                absenTable.setWidthPercentage(70);
                absenTable.setWidths(new float[]{0.25f, 0.75f});
                absenTable.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                
                com.itextpdf.text.pdf.PdfPCell absenTitle = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("REKAP KETIDAKHADIRAN (6 Bulan Terakhir)", headerFont));
                absenTitle.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                absenTitle.setPadding(5);
                absenTitle.setColspan(2);
                absenTable.addCell(absenTitle);
                
                com.itextpdf.text.pdf.PdfPCell sakitLabel = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Sakit", normalFont));
                sakitLabel.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                sakitLabel.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell sakitValue = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + sakit + " hari", normalFont));
                sakitValue.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                sakitValue.setPadding(3);
                absenTable.addCell(sakitLabel);
                absenTable.addCell(sakitValue);
                
                com.itextpdf.text.pdf.PdfPCell izinLabel = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Izin", normalFont));
                izinLabel.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                izinLabel.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell izinValue = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + izin + " hari", normalFont));
                izinValue.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                izinValue.setPadding(3);
                absenTable.addCell(izinLabel);
                absenTable.addCell(izinValue);
                
                com.itextpdf.text.pdf.PdfPCell alpaLabel = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase("Alpa", normalFont));
                alpaLabel.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                alpaLabel.setPadding(3);
                com.itextpdf.text.pdf.PdfPCell alpaValue = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(": " + alpa + " hari", normalFont));
                alpaValue.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                alpaValue.setPadding(3);
                absenTable.addCell(alpaLabel);
                absenTable.addCell(alpaValue);
                
                document.add(absenTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel Nilai
                com.itextpdf.text.pdf.PdfPTable nilaiTable = new com.itextpdf.text.pdf.PdfPTable(8);
                nilaiTable.setWidthPercentage(100);
                nilaiTable.setWidths(new float[]{0.4f, 2.0f, 1.3f, 0.7f, 0.7f, 0.7f, 0.8f, 0.9f});
                
                String[] headers = {"No", "Mata Pelajaran", "Guru", "Tugas", "UTS", "UAS", "Nilai Akhir", "Predikat"};
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(5);
                    nilaiTable.addCell(headerCell);
                }
                
                double totalNilai = 0;
                int mapelCount = 0;
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String no = tableModel.getValueAt(i, 0) != null ? tableModel.getValueAt(i, 0).toString() : "";
                    if (no.equals("")) continue;
                    if (no.equals("-")) continue;
                    
                    String mapel = tableModel.getValueAt(i, 1) != null ? tableModel.getValueAt(i, 1).toString() : "";
                    String guru = tableModel.getValueAt(i, 2) != null ? tableModel.getValueAt(i, 2).toString() : "";
                    String tugas = tableModel.getValueAt(i, 3) != null ? tableModel.getValueAt(i, 3).toString() : "";
                    String uts = tableModel.getValueAt(i, 4) != null ? tableModel.getValueAt(i, 4).toString() : "";
                    String uas = tableModel.getValueAt(i, 5) != null ? tableModel.getValueAt(i, 5).toString() : "";
                    String akhir = tableModel.getValueAt(i, 6) != null ? tableModel.getValueAt(i, 6).toString() : "";
                    String predikat = tableModel.getValueAt(i, 7) != null ? tableModel.getValueAt(i, 7).toString() : "";
                    
                    try {
                        totalNilai += Double.parseDouble(akhir);
                        mapelCount++;
                    } catch (NumberFormatException e) {}
                    
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(no, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(mapel, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(guru, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(tugas, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(uts, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(uas, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(akhir, normalFont)));
                    nilaiTable.addCell(new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(predikat, normalFont)));
                }
                
                document.add(nilaiTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Rata-rata
                double rataRata = mapelCount > 0 ? totalNilai / mapelCount : 0;
                String predikatAkhir = getPredikat(rataRata);
                
                com.itextpdf.text.Paragraph rataText = new com.itextpdf.text.Paragraph("Rata-rata Nilai : " + String.format("%.1f", rataRata) + "      Predikat : " + predikatAkhir, boldFont);
                rataText.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(rataText);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Catatan Wali Kelas
                document.add(new com.itextpdf.text.Paragraph("Catatan Wali Kelas :", boldFont));
                document.add(new com.itextpdf.text.Paragraph("  \"" + catatanWali + "\"", italicFont));
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // TTD (Kepala Sekolah kiri pinggir, Wali Kelas kanan pinggir)
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id", "ID"));
                String tgl = sdf.format(new Date());
                
                com.itextpdf.text.pdf.PdfPTable ttdTable = new com.itextpdf.text.pdf.PdfPTable(2);
                ttdTable.setWidthPercentage(100);
                ttdTable.setWidths(new float[]{0.5f, 0.5f});
                
                // Kiri - Kepala Sekolah (rata kiri)
                com.itextpdf.text.pdf.PdfPCell kiriCell = new com.itextpdf.text.pdf.PdfPCell();
                kiriCell.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                kiriCell.setPadding(5);
                kiriCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_LEFT);
                kiriCell.addElement(new com.itextpdf.text.Paragraph("Mengetahui,", normalFont));
                kiriCell.addElement(new com.itextpdf.text.Paragraph("Kepala Sekolah,", normalFont));
                kiriCell.addElement(new com.itextpdf.text.Paragraph("\n\n\n\n", normalFont));
                kiriCell.addElement(new com.itextpdf.text.Paragraph("(_________________________)", normalFont));
                
                // Kanan - Wali Kelas (rata kanan)
                com.itextpdf.text.pdf.PdfPCell kananCell = new com.itextpdf.text.pdf.PdfPCell();
                kananCell.setBorder(com.itextpdf.text.pdf.PdfPCell.NO_BORDER);
                kananCell.setPadding(5);
                kananCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                kananCell.addElement(new com.itextpdf.text.Paragraph(tgl, normalFont));
                kananCell.addElement(new com.itextpdf.text.Paragraph("Wali Kelas,", normalFont));
                kananCell.addElement(new com.itextpdf.text.Paragraph("\n\n\n\n", normalFont));
                kananCell.addElement(new com.itextpdf.text.Paragraph("(" + currentWaliKelas + ")", normalFont));
                
                ttdTable.addCell(kiriCell);
                ttdTable.addCell(kananCell);
                
                document.add(ttdTable);
                
                document.close();
                JOptionPane.showMessageDialog(this, "Raport PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}