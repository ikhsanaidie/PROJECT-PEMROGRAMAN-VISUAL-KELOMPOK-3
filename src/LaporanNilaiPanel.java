import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LaporanNilaiPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> kelasCombo;
    private JComboBox<String> mapelCombo;
    private JButton filterBtn, refreshBtn, cetakBtn, backBtn;
    private JTextField searchField;
    private JPanel topPanel;
    
    public LaporanNilaiPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadKelas();
        loadMataPelajaran();
        loadDataNilai();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 242, 245));
        
        // ============ TOP PANEL (FILTER + TOMBOL) ============
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Judul
        JLabel titleLabel = new JLabel("LAPORAN NILAI SISWA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(15));
        
        // Panel Filter
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        filterPanel.setBackground(Color.WHITE);
        
        filterPanel.add(new JLabel("Kelas:"));
        kelasCombo = new JComboBox<>();
        kelasCombo.setPreferredSize(new Dimension(180, 30));
        filterPanel.add(kelasCombo);
        
        filterPanel.add(Box.createHorizontalStrut(15));
        
        filterPanel.add(new JLabel("Mata Pelajaran:"));
        mapelCombo = new JComboBox<>();
        mapelCombo.setPreferredSize(new Dimension(180, 30));
        filterPanel.add(mapelCombo);
        
        topPanel.add(filterPanel);
        topPanel.add(Box.createVerticalStrut(10));
        
        // Panel Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        
        searchPanel.add(new JLabel("Cari (NISN/Nama):"));
        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(searchField);
        
        JButton cariBtn = new JButton("CARI");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.setFocusPainted(false);
        cariBtn.addActionListener(e -> cariData());
        searchPanel.add(cariBtn);
        
        JButton resetCariBtn = new JButton("RESET");
        resetCariBtn.setBackground(new Color(108, 117, 125));
        resetCariBtn.setForeground(Color.WHITE);
        resetCariBtn.setFocusPainted(false);
        resetCariBtn.addActionListener(e -> {
            searchField.setText("");
            filterData();
        });
        searchPanel.add(resetCariBtn);
        
        topPanel.add(searchPanel);
        topPanel.add(Box.createVerticalStrut(10));
        
        // Panel Tombol Aksi (CETAK PDF & KEMBALI)
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        filterBtn = new JButton("TAMPILKAN");
        filterBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        filterBtn.setBackground(new Color(52, 152, 219));
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterBtn.setPreferredSize(new Dimension(120, 38));
        filterBtn.addActionListener(e -> filterData());
        btnPanel.add(filterBtn);
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setPreferredSize(new Dimension(120, 38));
        refreshBtn.addActionListener(e -> {
            loadKelas();
            loadMataPelajaran();
            loadDataNilai();
            searchField.setText("");
        });
        btnPanel.add(refreshBtn);
        
        cetakBtn = new JButton("CETAK PDF");
        cetakBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cetakBtn.setBackground(new Color(46, 204, 113));
        cetakBtn.setForeground(Color.WHITE);
        cetakBtn.setFocusPainted(false);
        cetakBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cetakBtn.setPreferredSize(new Dimension(140, 38));
        cetakBtn.addActionListener(e -> cetakPDF());
        btnPanel.add(cetakBtn);
        
        backBtn = new JButton("KEMBALI KE MENU");
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(140, 38));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        btnPanel.add(backBtn);
        
        topPanel.add(btnPanel);
        
        add(topPanel, BorderLayout.NORTH);
        
        // ============ TABEL ============
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        String[] columns = {"No", "NISN", "Nama Siswa", "Kelas", "Mata Pelajaran", "Guru Pengajar", "Tugas", "UTS", "UAS", "Nilai Akhir"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        // Set lebar kolom
        int[] widths = {40, 90, 150, 100, 120, 120, 60, 60, 60, 80};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        add(tableContainer, BorderLayout.CENTER);
    }
    
    private void loadKelas() {
        String selected = (String) kelasCombo.getSelectedItem();
        kelasCombo.removeAllItems();
        kelasCombo.addItem("Semua Kelas");
        for (String[] k : Database.getAllKelas()) {
            kelasCombo.addItem(k[1]);
        }
        if (selected != null) kelasCombo.setSelectedItem(selected);
    }
    
    private void loadMataPelajaran() {
        String selected = (String) mapelCombo.getSelectedItem();
        mapelCombo.removeAllItems();
        mapelCombo.addItem("Semua Mata Pelajaran");
        
        Set<String> mapelSet = new HashSet<>();
        List<Object[]> nilaiList = Database.getAllNilai();
        for (Object[] nilai : nilaiList) {
            String mapel = nilai[2].toString();
            if (mapel != null && !mapel.isEmpty()) {
                mapelSet.add(mapel);
            }
        }
        
        List<String> mapelList = new ArrayList<>(mapelSet);
        Collections.sort(mapelList);
        for (String mapel : mapelList) {
            mapelCombo.addItem(mapel);
        }
        
        if (selected != null && !selected.equals("Semua Mata Pelajaran")) {
            mapelCombo.setSelectedItem(selected);
        }
    }
    
    private void loadDataNilai() {
        tableModel.setRowCount(0);
        int no = 1;
        List<Object[]> nilaiList = Database.getAllNilai();
        
        for (Object[] nilai : nilaiList) {
            String nisn = nilai[0].toString();
            String nama = nilai[1].toString();
            String mapel = nilai[2].toString();
            String guru = nilai[3].toString();
            double tugas = (double) nilai[4];
            double uts = (double) nilai[5];
            double uas = (double) nilai[6];
            double akhir = (double) nilai[7];
            
            String kelas = "-";
            String[] siswa = Database.getSiswaByNISN(nisn);
            if (siswa != null && siswa.length > 10) {
                kelas = siswa[10];
            }
            
            tableModel.addRow(new Object[]{no++, nisn, nama, kelas, mapel, guru, tugas, uts, uas, akhir});
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data nilai.");
        }
    }
    
    private void cariData() {
        String keyword = searchField.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            filterData();
            return;
        }
        
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String selectedMapel = (String) mapelCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        int no = 1;
        List<Object[]> nilaiList = Database.getAllNilai();
        
        for (Object[] nilai : nilaiList) {
            String nisn = nilai[0].toString();
            String nama = nilai[1].toString();
            String mapel = nilai[2].toString();
            String guru = nilai[3].toString();
            double tugas = (double) nilai[4];
            double uts = (double) nilai[5];
            double uas = (double) nilai[6];
            double akhir = (double) nilai[7];
            
            String kelas = "-";
            String[] siswa = Database.getSiswaByNISN(nisn);
            if (siswa != null && siswa.length > 10) {
                kelas = siswa[10];
            }
            
            boolean matchKelas = selectedKelas.equals("Semua Kelas") || kelas.equals(selectedKelas);
            boolean matchMapel = selectedMapel.equals("Semua Mata Pelajaran") || mapel.equals(selectedMapel);
            boolean matchCari = nisn.toLowerCase().contains(keyword) || nama.toLowerCase().contains(keyword);
            
            if (matchKelas && matchMapel && matchCari) {
                tableModel.addRow(new Object[]{no++, nisn, nama, kelas, mapel, guru, tugas, uts, uas, akhir});
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
        }
    }
    
    private void filterData() {
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String selectedMapel = (String) mapelCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        int no = 1;
        List<Object[]> nilaiList = Database.getAllNilai();
        
        for (Object[] nilai : nilaiList) {
            String nisn = nilai[0].toString();
            String nama = nilai[1].toString();
            String mapel = nilai[2].toString();
            String guru = nilai[3].toString();
            double tugas = (double) nilai[4];
            double uts = (double) nilai[5];
            double uas = (double) nilai[6];
            double akhir = (double) nilai[7];
            
            String kelas = "-";
            String[] siswa = Database.getSiswaByNISN(nisn);
            if (siswa != null && siswa.length > 10) {
                kelas = siswa[10];
            }
            
            boolean matchKelas = selectedKelas.equals("Semua Kelas") || kelas.equals(selectedKelas);
            boolean matchMapel = selectedMapel.equals("Semua Mata Pelajaran") || mapel.equals(selectedMapel);
            
            if (matchKelas && matchMapel) {
                tableModel.addRow(new Object[]{no++, nisn, nama, kelas, mapel, guru, tugas, uts, uas, akhir});
            }
        }
        
        if (tableModel.getRowCount() == 0) {
            String msg = "Tidak ada data nilai";
            if (!selectedKelas.equals("Semua Kelas")) msg += " untuk kelas " + selectedKelas;
            if (!selectedMapel.equals("Semua Mata Pelajaran")) msg += " untuk mata pelajaran " + selectedMapel;
            JOptionPane.showMessageDialog(this, msg);
        }
    }
    
    private void cetakPDF() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak!");
            return;
        }
        
        try {
            String selectedKelas = (String) kelasCombo.getSelectedItem();
            String selectedMapel = (String) mapelCombo.getSelectedItem();
            
            String fileName = "Laporan_Nilai";
            if (!selectedKelas.equals("Semua Kelas")) fileName += "_" + selectedKelas;
            if (!selectedMapel.equals("Semua Mata Pelajaran")) fileName += "_" + selectedMapel;
            fileName += ".pdf";
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File(fileName));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4.rotate());
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 9, com.itextpdf.text.Font.NORMAL);
                com.itextpdf.text.Font headerFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 10, com.itextpdf.text.Font.BOLD);
                
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
                
                // Kop Surat
                com.itextpdf.text.Paragraph title = new com.itextpdf.text.Paragraph("SMA PGRI 4 JAKARTA", titleFont);
                title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(title);
                
                com.itextpdf.text.Paragraph subTitle = new com.itextpdf.text.Paragraph("SISTEM INFORMASI AKADEMIK", subTitleFont);
                subTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(subTitle);
                
                com.itextpdf.text.Paragraph address = new com.itextpdf.text.Paragraph("Jl. Raya Bogor KM 24, Ciracas, Jakarta Timur", normalFont);
                address.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(address);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                com.itextpdf.text.pdf.PdfPTable lineTable = new com.itextpdf.text.pdf.PdfPTable(1);
                lineTable.setWidthPercentage(100);
                com.itextpdf.text.pdf.PdfPCell lineCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(" "));
                lineCell.setBorder(com.itextpdf.text.pdf.PdfPCell.BOTTOM);
                lineCell.setBorderWidthBottom(2f);
                lineTable.addCell(lineCell);
                document.add(lineTable);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("LAPORAN NILAI SISWA", headerFont);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                String filterText = "";
                if (!selectedKelas.equals("Semua Kelas")) filterText += "Kelas: " + selectedKelas;
                if (!selectedMapel.equals("Semua Mata Pelajaran")) {
                    if (!filterText.isEmpty()) filterText += " | ";
                    filterText += "Mata Pelajaran: " + selectedMapel;
                }
                if (filterText.isEmpty()) filterText = "Semua Kelas & Semua Mata Pelajaran";
                
                com.itextpdf.text.Paragraph filterInfo = new com.itextpdf.text.Paragraph(filterText, normalFont);
                filterInfo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(filterInfo);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph tanggal = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                tanggal.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(tanggal);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel 10 kolom
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(10);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{0.4f, 0.9f, 1.5f, 0.9f, 1.2f, 1.2f, 0.6f, 0.6f, 0.6f, 0.8f});
                
                String[] headers = {"No", "NISN", "Nama Siswa", "Kelas", "Mata Pelajaran", "Guru", "Tugas", "UTS", "UAS", "Nilai Akhir"};
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(6);
                    pdfTable.addCell(headerCell);
                }
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < 10; j++) {
                        String value = tableModel.getValueAt(i, j) != null ? tableModel.getValueAt(i, j).toString() : "";
                        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(value, normalFont));
                        cell.setPadding(5);
                        pdfTable.addCell(cell);
                    }
                }
                
                document.add(pdfTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                com.itextpdf.text.Paragraph footer = new com.itextpdf.text.Paragraph("Dicetak dari Sistem Informasi Akademik SMA PGRI 4 Jakarta", normalFont);
                footer.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(footer);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                com.itextpdf.text.Paragraph signature = new com.itextpdf.text.Paragraph("Mengetahui,\nKepala Sekolah\n\n\n\n(.................................)", normalFont);
                signature.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(signature);
                
                document.close();
                JOptionPane.showMessageDialog(this, "PDF berhasil disimpan!\nLokasi: " + filePath);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}