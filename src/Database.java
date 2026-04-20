import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class Database {
    public static DefaultTableModel dataSiswaModel;
    public static DefaultTableModel dataGuruModel;
    public static DefaultTableModel dataKelasModel;
    public static DefaultTableModel dataNilaiModel;
    public static DefaultTableModel dataAbsensiModel;
    public static DefaultTableModel dataSPPModel;
    
    public static void initDatabase() {
        // Data Siswa dengan kolom baru: NISN, Nama, Jenis Kelamin, Agama, Alamat, Kelas
        String[] siswaColumns = {"NISN", "Nama", "Jenis Kelamin", "Agama", "Alamat", "Kelas"};
        dataSiswaModel = new DefaultTableModel(siswaColumns, 0);
        dataSiswaModel.addRow(new Object[]{"12345", "Ani", "Perempuan", "Islam", "Jl. Merdeka 1", "X MIPA 1"});
        dataSiswaModel.addRow(new Object[]{"12346", "Budi", "Laki-laki", "Islam", "Jl. Merdeka 2", "X MIPA 2"});
        dataSiswaModel.addRow(new Object[]{"12347", "Cici", "Perempuan", "Kristen", "Jl. Melati 3", "X MIPA 1"});
        
        // Data Guru
        String[] guruColumns = {"NIP", "Nama Guru", "Mata Pelajaran"};
        dataGuruModel = new DefaultTableModel(guruColumns, 0);
        dataGuruModel.addRow(new Object[]{"19800101", "Dr. Ahmad", "Matematika"});
        dataGuruModel.addRow(new Object[]{"19850210", "Siti Aminah", "Bahasa Indonesia"});
        dataGuruModel.addRow(new Object[]{"19900315", "Budi Santoso", "Fisika"});
        
        // Data Kelas
        String[] kelasColumns = {"ID Kelas", "Nama Kelas", "Wali Kelas"};
        dataKelasModel = new DefaultTableModel(kelasColumns, 0);
        dataKelasModel.addRow(new Object[]{"X-MIPA-1", "X MIPA 1", "Dr. Ahmad"});
        dataKelasModel.addRow(new Object[]{"X-MIPA-2", "X MIPA 2", "Siti Aminah"});
        dataKelasModel.addRow(new Object[]{"XI-MIPA-1", "XI MIPA 1", "Budi Santoso"});
        
        // Data Nilai
        String[] nilaiColumns = {"NISN", "Nama Siswa", "Tugas", "UTS", "UAS", "Nilai Akhir"};
        dataNilaiModel = new DefaultTableModel(nilaiColumns, 0);
        
        // Data Absensi
        String[] absensiColumns = {"Tanggal", "Kelas", "NISN", "Nama Siswa", "Status"};
        dataAbsensiModel = new DefaultTableModel(absensiColumns, 0);
        
        // Data SPP
        String[] sppColumns = {"NISN", "Nama Siswa", "Bulan", "Nominal", "Status", "Tanggal Bayar"};
        dataSPPModel = new DefaultTableModel(sppColumns, 0);
    }
    
    public static List<String[]> getAllSiswa() {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < dataSiswaModel.getRowCount(); i++) {
            String[] row = {
                dataSiswaModel.getValueAt(i, 0).toString(), // NISN
                dataSiswaModel.getValueAt(i, 1).toString(), // Nama
                dataSiswaModel.getValueAt(i, 2).toString(), // Jenis Kelamin
                dataSiswaModel.getValueAt(i, 3).toString(), // Agama
                dataSiswaModel.getValueAt(i, 4).toString(), // Alamat
                dataSiswaModel.getValueAt(i, 5).toString()  // Kelas
            };
            list.add(row);
        }
        return list;
    }
    
    public static List<String[]> getAllGuru() {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < dataGuruModel.getRowCount(); i++) {
            String[] row = {
                dataGuruModel.getValueAt(i, 0).toString(),
                dataGuruModel.getValueAt(i, 1).toString(),
                dataGuruModel.getValueAt(i, 2).toString()
            };
            list.add(row);
        }
        return list;
    }
    
    public static List<String[]> getAllKelas() {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < dataKelasModel.getRowCount(); i++) {
            String[] row = {
                dataKelasModel.getValueAt(i, 0).toString(),
                dataKelasModel.getValueAt(i, 1).toString(),
                dataKelasModel.getValueAt(i, 2).toString()
            };
            list.add(row);
        }
        return list;
    }
    
    public static List<String[]> getSiswaByKelas(String kelas) {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < dataSiswaModel.getRowCount(); i++) {
            if (dataSiswaModel.getValueAt(i, 5).toString().equals(kelas)) {
                String[] row = {
                    dataSiswaModel.getValueAt(i, 0).toString(),
                    dataSiswaModel.getValueAt(i, 1).toString(),
                    dataSiswaModel.getValueAt(i, 2).toString(),
                    dataSiswaModel.getValueAt(i, 3).toString(),
                    dataSiswaModel.getValueAt(i, 4).toString(),
                    dataSiswaModel.getValueAt(i, 5).toString()
                };
                list.add(row);
            }
        }
        return list;
    }
    
    public static List<String[]> getAllAbsensi() {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < dataAbsensiModel.getRowCount(); i++) {
            String[] row = {
                dataAbsensiModel.getValueAt(i, 0).toString(),
                dataAbsensiModel.getValueAt(i, 1).toString(),
                dataAbsensiModel.getValueAt(i, 2).toString(),
                dataAbsensiModel.getValueAt(i, 3).toString(),
                dataAbsensiModel.getValueAt(i, 4).toString()
            };
            list.add(row);
        }
        return list;
    }
    
    public static String[] getSiswaByNISN(String nisn) {
        for (int i = 0; i < dataSiswaModel.getRowCount(); i++) {
            if (dataSiswaModel.getValueAt(i, 0).toString().equals(nisn)) {
                return new String[]{
                    dataSiswaModel.getValueAt(i, 0).toString(),
                    dataSiswaModel.getValueAt(i, 1).toString(),
                    dataSiswaModel.getValueAt(i, 2).toString(),
                    dataSiswaModel.getValueAt(i, 3).toString(),
                    dataSiswaModel.getValueAt(i, 4).toString(),
                    dataSiswaModel.getValueAt(i, 5).toString()
                };
            }
        }
        return null;
    }
}