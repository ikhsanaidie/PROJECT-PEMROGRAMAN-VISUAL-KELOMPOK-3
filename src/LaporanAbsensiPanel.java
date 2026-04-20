import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LaporanAbsensiPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> kelasCombo;
    private JComboBox<String> bulanCombo;
    private JButton filterBtn, refreshBtn, cetakBtn, backBtn;
    
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
        setBackground(Color.WHITE);
        
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(Color.WHITE);
        
        kelasCombo = new JComboBox<>();
        kelasCombo.setPreferredSize(new Dimension(150, 30));
        
        bulanCombo = new JComboBox<>(new String[]{
            "Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"
        });
        bulanCombo.setPreferredSize(new Dimension(150, 30));
        
        filterBtn = new JButton("TAMPILKAN");
        filterBtn.setBackground(new Color(52, 152, 219));
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        refreshBtn = new JButton("REFRESH");
        refreshBtn.setBackground(new Color(52, 152, 219));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        cetakBtn = new JButton("CETAK PDF");
        cetakBtn.setBackground(new Color(46, 204, 113));
        cetakBtn.setForeground(Color.WHITE);
        cetakBtn.setFocusPainted(false);
        cetakBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        backBtn = new JButton("KEMBALI");
        backBtn.setBackground(new Color(231, 76, 60));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        filterPanel.add(new JLabel("Kelas:"));
        filterPanel.add(kelasCombo);
        filterPanel.add(new JLabel("Bulan:"));
        filterPanel.add(bulanCombo);
        filterPanel.add(filterBtn);
        filterPanel.add(refreshBtn);
        filterPanel.add(cetakBtn);
        filterPanel.add(backBtn);
        
        String[] columns = {"Tanggal", "Kelas", "NISN", "Nama Siswa", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        filterBtn.addActionListener(e -> filterData());
        refreshBtn.addActionListener(e -> {
            loadData();
            loadKelas();
        });
        cetakBtn.addActionListener(e -> cetakPDF());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadKelas() {
        kelasCombo.removeAllItems();
        for (String[] k : Database.getAllKelas()) {
            kelasCombo.addItem(k[1]);
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        for (String[] a : Database.getAllAbsensi()) {
            tableModel.addRow(new Object[]{a[0], a[1], a[2], a[3], a[4]});
        }
    }
    
    private void filterData() {
        String selectedKelas = (String) kelasCombo.getSelectedItem();
        String selectedBulan = (String) bulanCombo.getSelectedItem();
        
        tableModel.setRowCount(0);
        
        for (String[] a : Database.getAllAbsensi()) {
            if (!a[1].equals(selectedKelas)) continue;
            
            if (!selectedBulan.equals("Semua Bulan")) {
                String[] tglParts = a[0].split("-");
                if (tglParts.length >= 2) {
                    int bulanNum = Integer.parseInt(tglParts[1]);
                    String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni",
                        "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
                    String bulanData = namaBulan[bulanNum - 1];
                    if (!bulanData.equals(selectedBulan)) continue;
                }
            }
            
            tableModel.addRow(new Object[]{a[0], a[1], a[2], a[3], a[4]});
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data absensi untuk filter ini");
        } else {
            JOptionPane.showMessageDialog(this, "Menampilkan " + tableModel.getRowCount() + " data absensi");
        }
    }
    
    private void cetakPDF() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak!");
            return;
        }
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("Laporan_Absensi_SMA_PGRI_4.pdf"));
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
                
                // ========== LOGO DI PDF ==========
                try {
                    java.net.URL imgUrl = getClass().getResource("/images/smapgri4.png");
                    if (imgUrl != null) {
                        com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgUrl);
                        logo.scaleToFit(80, 80);
                        logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                        document.add(logo);
                    } else {
                        java.io.File imgFile = new java.io.File("src/images/smapgri4.png");
                        if (imgFile.exists()) {
                            com.itextpdf.text.Image logo = com.itextpdf.text.Image.getInstance(imgFile.getAbsolutePath());
                            logo.scaleToFit(80, 80);
                            logo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                            document.add(logo);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Logo tidak ditemukan: " + e.getMessage());
                }
                
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
                
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("LAPORAN ABSENSI SISWA", headerFont);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                String filterText = "Kelas: " + kelasCombo.getSelectedItem() + " | Bulan: " + bulanCombo.getSelectedItem();
                com.itextpdf.text.Paragraph filterInfo = new com.itextpdf.text.Paragraph(filterText, normalFont);
                filterInfo.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(filterInfo);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph tanggal = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                tanggal.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(tanggal);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(5);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{1f, 1.5f, 1.5f, 2f, 1.5f});
                
                String[] headers = {"Tanggal", "Kelas", "NISN", "Nama Siswa", "Status"};
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
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