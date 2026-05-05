import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LaporanGuruPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton refreshBtn, cetakBtn, backBtn;
    
    public LaporanGuruPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(240, 242, 245));
        
        // ============ PANEL JUDUL ============
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("LAPORAN DATA GURU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(41, 128, 185));
        titlePanel.add(titleLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        
        // ============ PANEL TABEL ============
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Kolom tabel
        String[] columns = {"No", "NIP", "Nama Guru", "Mata Pelajaran"};
        
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        
        // Set lebar kolom agar rapi
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // No
        table.getColumnModel().getColumn(1).setPreferredWidth(150);  // NIP
        table.getColumnModel().getColumn(2).setPreferredWidth(250);  // Nama Guru
        table.getColumnModel().getColumn(3).setPreferredWidth(200);  // Mata Pelajaran
        
        // Header tabel
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));
        
        // ScrollPane (bisa scroll horizontal & vertikal)
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.CENTER);
        
        // ============ PANEL TOMBOL ============
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        
        refreshBtn = createStyledButton("REFRESH DATA", new Color(52, 152, 219));
        cetakBtn = createStyledButton("CETAK PDF", new Color(46, 204, 113));
        backBtn = createStyledButton("KEMBALI", new Color(231, 76, 60));
        
        btnPanel.add(refreshBtn);
        btnPanel.add(cetakBtn);
        btnPanel.add(backBtn);
        
        add(btnPanel, BorderLayout.SOUTH);
        
        // ============ EVENT HANDLERS ============
        refreshBtn.addActionListener(e -> loadData());
        cetakBtn.addActionListener(e -> cetakPDF());
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 25, 8, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        int no = 1;
        for (String[] guru : Database.getAllGuru()) {
            tableModel.addRow(new Object[]{
                no++,                           // No
                guru[0],                        // NIP
                guru[1],                        // Nama Guru
                guru[2]                         // Mata Pelajaran
            });
        }
        
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data guru.");
        } else {
            JOptionPane.showMessageDialog(this, "Total " + tableModel.getRowCount() + " guru.");
        }
    }
    
    private void cetakPDF() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk dicetak!");
            return;
        }
        
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File("Laporan_Guru_SMA_PGRI_4.pdf"));
            int result = fileChooser.showSaveDialog(this);
            
            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.endsWith(".pdf")) filePath += ".pdf";
                
                com.itextpdf.text.Document document = new com.itextpdf.text.Document(com.itextpdf.text.PageSize.A4);
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 18, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font subTitleFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 14, com.itextpdf.text.Font.BOLD);
                com.itextpdf.text.Font normalFont = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.TIMES_ROMAN, 11, com.itextpdf.text.Font.NORMAL);
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
                
                // Garis
                com.itextpdf.text.pdf.PdfPTable lineTable = new com.itextpdf.text.pdf.PdfPTable(1);
                lineTable.setWidthPercentage(100);
                com.itextpdf.text.pdf.PdfPCell lineCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(" "));
                lineCell.setBorder(com.itextpdf.text.pdf.PdfPCell.BOTTOM);
                lineCell.setBorderWidthBottom(2f);
                lineTable.addCell(lineCell);
                document.add(lineTable);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Judul
                com.itextpdf.text.Paragraph judul = new com.itextpdf.text.Paragraph("LAPORAN DATA GURU", headerFont);
                judul.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                document.add(judul);
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", new Locale("id", "ID"));
                com.itextpdf.text.Paragraph tanggal = new com.itextpdf.text.Paragraph("Dicetak: " + sdf.format(new Date()), normalFont);
                tanggal.setAlignment(com.itextpdf.text.Element.ALIGN_RIGHT);
                document.add(tanggal);
                
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Tabel dengan 4 kolom
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(4);
                pdfTable.setWidthPercentage(100);
                pdfTable.setWidths(new float[]{0.5f, 1.8f, 2.5f, 2.0f});
                
                String[] headers = {"No", "NIP", "Nama Guru", "Mata Pelajaran"};
                
                for (String header : headers) {
                    com.itextpdf.text.pdf.PdfPCell headerCell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(header, headerFont));
                    headerCell.setBackgroundColor(new com.itextpdf.text.BaseColor(41, 128, 185));
                    headerCell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < 4; j++) {
                        String value = tableModel.getValueAt(i, j) != null ? tableModel.getValueAt(i, j).toString() : "";
                        com.itextpdf.text.pdf.PdfPCell cell = new com.itextpdf.text.pdf.PdfPCell(new com.itextpdf.text.Phrase(value, normalFont));
                        cell.setPadding(6);
                        pdfTable.addCell(cell);
                    }
                }
                
                document.add(pdfTable);
                document.add(new com.itextpdf.text.Paragraph(" "));
                
                // Footer
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
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage() + "\n\nPastikan library iTextPDF sudah ditambahkan.");
        }
    }
}