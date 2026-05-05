import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class DataGuruPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nipField, namaField, mapelField;
    private JComboBox<String> jkCombo;
    private JButton tambahBtn, ubahBtn, hapusBtn, refreshBtn, backBtn;
    private JTextField searchField;
    private JPanel topStatsPanel;
    
    // Label untuk statistik
    private JLabel totalGuruLabel;
    private JLabel totalLakiLabel;
    private JLabel totalPerempuanLabel;
    
    public DataGuruPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 242, 245));
        
        // ============ TOP STATS CARDS ============
        topStatsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        topStatsPanel.setOpaque(false);
        topStatsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Card 1: Total Guru (Gradasi Biru)
        JPanel card1 = createStatCard("👨‍🏫", "TOTAL GURU", "0", "Guru Aktif");
        totalGuruLabel = (JLabel) card1.getClientProperty("valueLabel");
        topStatsPanel.add(card1);
        
        // Card 2: Guru Laki-laki (Gradasi Hijau)
        JPanel card2 = createStatCard("👨", "LAKI-LAKI", "0", "Orang");
        totalLakiLabel = (JLabel) card2.getClientProperty("valueLabel");
        topStatsPanel.add(card2);
        
        // Card 3: Guru Perempuan (Gradasi Ungu)
        JPanel card3 = createStatCard("👩", "PEREMPUAN", "0", "Orang");
        totalPerempuanLabel = (JLabel) card3.getClientProperty("valueLabel");
        topStatsPanel.add(card3);
        
        add(topStatsPanel, BorderLayout.NORTH);
        
        // ============ SPLIT PANE ============
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplit.setResizeWeight(0.35);
        mainSplit.setDividerSize(8);
        mainSplit.setBorder(BorderFactory.createEmptyBorder());
        
        // ============ FORM PANEL ============
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(Color.WHITE);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel formTitle = new JLabel("FORM DATA GURU");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(new Color(41, 128, 185));
        formContainer.add(formTitle, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        // Baris 1 - NIP
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("NIP:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        nipField = new JTextField();
        formPanel.add(nipField, gbc);
        
        // Baris 2 - Nama Guru
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama Guru:"), gbc);
        gbc.gridx = 1;
        namaField = new JTextField();
        formPanel.add(namaField, gbc);
        
        // Baris 3 - Jenis Kelamin
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Jenis Kelamin:"), gbc);
        gbc.gridx = 1;
        jkCombo = new JComboBox<>(new String[]{"Laki-laki", "Perempuan"});
        formPanel.add(jkCombo, gbc);
        
        // Baris 4 - Mata Pelajaran
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Mata Pelajaran:"), gbc);
        gbc.gridx = 1;
        mapelField = new JTextField();
        formPanel.add(mapelField, gbc);
        
        // Button Panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        
        tambahBtn = createStyledButton("TAMBAH", new Color(46, 204, 113));
        ubahBtn = createStyledButton("UBAH", new Color(52, 152, 219));
        hapusBtn = createStyledButton("HAPUS", new Color(231, 76, 60));
        refreshBtn = createStyledButton("REFRESH", new Color(52, 152, 219));
        backBtn = createStyledButton("KEMBALI", new Color(52, 73, 94));
        
        btnPanel.add(tambahBtn);
        btnPanel.add(ubahBtn);
        btnPanel.add(hapusBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        formPanel.add(btnPanel, gbc);
        
        JScrollPane formScrollPane = new JScrollPane(formPanel);
        formScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formContainer.add(formScrollPane, BorderLayout.CENTER);
        
        mainSplit.setTopComponent(formContainer);
        
        // ============ TABEL PANEL ============
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(Color.WHITE);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel searchIcon = new JLabel("Cari (NIP/Nama):");
        searchIcon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchPanel.add(searchIcon);
        
        searchField = new JTextField(25);
        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cariData(searchField.getText().trim());
            }
        });
        searchPanel.add(searchField);
        
        JButton cariBtn = new JButton("CARI");
        cariBtn.setBackground(new Color(52, 152, 219));
        cariBtn.setForeground(Color.WHITE);
        cariBtn.addActionListener(e -> cariData(searchField.getText().trim()));
        searchPanel.add(cariBtn);
        
        JButton resetCariBtn = new JButton("RESET");
        resetCariBtn.setBackground(new Color(108, 117, 125));
        resetCariBtn.setForeground(Color.WHITE);
        resetCariBtn.addActionListener(e -> {
            searchField.setText("");
            loadData();
        });
        searchPanel.add(resetCariBtn);
        
        tableContainer.add(searchPanel, BorderLayout.NORTH);
        
        // Tabel
        String[] columns = {"No", "NIP", "Nama Guru", "Jenis Kelamin", "Mata Pelajaran"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(41, 128, 185));
        header.setForeground(Color.WHITE);
        
        JScrollPane tableScrollPane = new JScrollPane(table);
        tableContainer.add(tableScrollPane, BorderLayout.CENTER);
        
        mainSplit.setBottomComponent(tableContainer);
        
        add(mainSplit, BorderLayout.CENTER);
        
        // ============ EVENT HANDLERS ============
        tambahBtn.addActionListener(e -> tambahData());
        ubahBtn.addActionListener(e -> ubahData());
        hapusBtn.addActionListener(e -> hapusData());
        refreshBtn.addActionListener(e -> {
            loadData();
            searchField.setText("");
        });
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "menuUtama"));
        
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    nipField.setText(tableModel.getValueAt(row, 1).toString());
                    namaField.setText(tableModel.getValueAt(row, 2).toString());
                    jkCombo.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    mapelField.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });
        
        updateTopStats();
    }
    
    private JPanel createStatCard(String icon, String title, String defaultValue, String subtitle) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color startColor, endColor;
                if (title.contains("TOTAL GURU")) {
                    startColor = new Color(52, 152, 219);
                    endColor = new Color(41, 128, 185);
                } else if (title.contains("LAKI-LAKI")) {
                    startColor = new Color(46, 204, 113);
                    endColor = new Color(39, 174, 96);
                } else {
                    startColor = new Color(155, 89, 182);
                    endColor = new Color(142, 68, 173);
                }
                GradientPaint gp = new GradientPaint(0, 0, startColor, w, h, endColor);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fillRoundRect(2, 4, w, h, 15, 15);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, w, h, 15, 15);
            }
        };
        
        card.setLayout(new BorderLayout(8, 5));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLabel.setForeground(new Color(255, 255, 255, 220));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(defaultValue);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subLabel = new JLabel(subtitle);
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        subLabel.setForeground(new Color(255, 255, 255, 200));
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(iconLabel, BorderLayout.WEST);
        topPanel.add(titleLabel, BorderLayout.CENTER);
        
        card.add(topPanel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subLabel, BorderLayout.SOUTH);
        
        card.putClientProperty("valueLabel", valueLabel);
        
        return card;
    }
    
    private void updateTopStats() {
        try {
            List<String[]> guruList = Database.getAllGuru();
            int total = guruList.size();
            int laki = 0, perempuan = 0;
            for (String[] g : guruList) {
                if (g.length > 1 && g[1].equalsIgnoreCase("Laki-laki")) {
                    laki++;
                } else if (g.length > 1 && g[1].equalsIgnoreCase("Perempuan")) {
                    perempuan++;
                } else {
                    // Jika JK tidak ada di database, deteksi dari nama
                    String nama = g[2].toLowerCase();
                    if (nama.contains("ahmad") || nama.contains("budi") || nama.contains("agus")) {
                        laki++;
                    } else {
                        perempuan++;
                    }
                }
            }
            
            if (totalGuruLabel != null) totalGuruLabel.setText(String.valueOf(total));
            if (totalLakiLabel != null) totalLakiLabel.setText(String.valueOf(laki));
            if (totalPerempuanLabel != null) totalPerempuanLabel.setText(String.valueOf(perempuan));
        } catch (Exception e) {}
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
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
    
    private void cariData(String keyword) {
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        tableModel.setRowCount(0);
        int no = 1;
        for (String[] guru : Database.getAllGuru()) {
            if (guru[0].toLowerCase().contains(keyword.toLowerCase()) || 
                guru[2].toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{no++, guru[0], guru[2], jkCombo.getSelectedItem(), guru[3]});
            }
        }
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Data tidak ditemukan!");
        }
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        int no = 1;
        for (String[] guru : Database.getAllGuru()) {
            String jk = (guru.length > 1 && !guru[1].isEmpty()) ? guru[1] : jkCombo.getSelectedItem().toString();
            tableModel.addRow(new Object[]{no++, guru[0], guru[2], jk, guru[3]});
        }
        updateTopStats();
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Belum ada data guru.");
        }
    }
    
    private void tambahData() {
        String nip = nipField.getText().trim();
        String nama = namaField.getText().trim();
        String jk = (String) jkCombo.getSelectedItem();
        String mapel = mapelField.getText().trim();
        
        if (nip.isEmpty() || nama.isEmpty() || mapel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        for (String[] g : Database.getAllGuru()) {
            if (g[0].equals(nip)) {
                JOptionPane.showMessageDialog(this, "NIP sudah ada!");
                return;
            }
        }
        
        if (Database.tambahGuru(nip, jk, nama, mapel)) {
            loadData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Data guru berhasil ditambahkan!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data!");
        }
    }
    
    private void ubahData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan diubah!");
            return;
        }
        
        String nipLama = tableModel.getValueAt(row, 1).toString();
        String nipBaru = nipField.getText().trim();
        String nama = namaField.getText().trim();
        String jk = (String) jkCombo.getSelectedItem();
        String mapel = mapelField.getText().trim();
        
        if (nipBaru.isEmpty() || nama.isEmpty() || mapel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return;
        }
        
        if (Database.updateGuru(nipLama, nipBaru, jk, nama, mapel)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Data guru berhasil diubah!");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data!");
        }
    }
    
    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus data guru ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String nip = tableModel.getValueAt(row, 1).toString();
            if (Database.hapusGuru(nip)) {
                loadData();
                resetForm();
                JOptionPane.showMessageDialog(this, "Data guru dihapus!");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }
    
    private void resetForm() {
        nipField.setText("");
        namaField.setText("");
        jkCombo.setSelectedIndex(0);
        mapelField.setText("");
        nipField.requestFocus();
    }
}