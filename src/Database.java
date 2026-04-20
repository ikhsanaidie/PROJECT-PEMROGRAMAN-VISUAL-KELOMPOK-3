import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    // ==================== SISWA ====================
    public static List<String[]> getAllSiswa() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT nisn, nama, jk, agama, alamat, kelas FROM tbl_siswa ORDER BY nisn";
        
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String[] row = {
                    rs.getString("nisn"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("kelas")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static String[] getSiswaByNISN(String nisn) {
        String query = "SELECT nisn, nama, jk, agama, alamat, kelas FROM tbl_siswa WHERE nisn = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                    rs.getString("nisn"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("kelas")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean tambahSiswa(String nisn, String nama, String jk, String agama, String alamat, String kelas) {
        String query = "INSERT INTO tbl_siswa (nisn, nama, jk, agama, alamat, kelas) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            pstmt.setString(2, nama);
            pstmt.setString(3, jk);
            pstmt.setString(4, agama);
            pstmt.setString(5, alamat);
            pstmt.setString(6, kelas);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateSiswa(String nisnLama, String nisnBaru, String nama, String jk, String agama, String alamat, String kelas) {
        String query = "UPDATE tbl_siswa SET nisn = ?, nama = ?, jk = ?, agama = ?, alamat = ?, kelas = ? WHERE nisn = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisnBaru);
            pstmt.setString(2, nama);
            pstmt.setString(3, jk);
            pstmt.setString(4, agama);
            pstmt.setString(5, alamat);
            pstmt.setString(6, kelas);
            pstmt.setString(7, nisnLama);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean hapusSiswa(String nisn) {
        String query = "DELETE FROM tbl_siswa WHERE nisn = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String[]> getSiswaByKelas(String kelas) {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT nisn, nama, jk, agama, alamat, kelas FROM tbl_siswa WHERE kelas = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, kelas);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nisn"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("kelas")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // ==================== GURU ====================
    public static List<String[]> getAllGuru() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT nip, nama_guru, mapel FROM tbl_guru ORDER BY nip";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nip"),
                    rs.getString("nama_guru"),
                    rs.getString("mapel")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean tambahGuru(String nip, String nama, String mapel) {
        String query = "INSERT INTO tbl_guru (nip, nama_guru, mapel) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nip);
            pstmt.setString(2, nama);
            pstmt.setString(3, mapel);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateGuru(String nipLama, String nipBaru, String nama, String mapel) {
        String query = "UPDATE tbl_guru SET nip = ?, nama_guru = ?, mapel = ? WHERE nip = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nipBaru);
            pstmt.setString(2, nama);
            pstmt.setString(3, mapel);
            pstmt.setString(4, nipLama);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean hapusGuru(String nip) {
        String query = "DELETE FROM tbl_guru WHERE nip = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nip);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== KELAS ====================
    public static List<String[]> getAllKelas() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT id_kelas, nama_kelas, wali_kelas FROM tbl_kelas ORDER BY nama_kelas";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("id_kelas"),
                    rs.getString("nama_kelas"),
                    rs.getString("wali_kelas")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean tambahKelas(String id, String nama, String wali) {
        String query = "INSERT INTO tbl_kelas (id_kelas, nama_kelas, wali_kelas) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, id);
            pstmt.setString(2, nama);
            pstmt.setString(3, wali);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateKelas(String idLama, String idBaru, String nama, String wali) {
        String query = "UPDATE tbl_kelas SET id_kelas = ?, nama_kelas = ?, wali_kelas = ? WHERE id_kelas = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, idBaru);
            pstmt.setString(2, nama);
            pstmt.setString(3, wali);
            pstmt.setString(4, idLama);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean hapusKelas(String id) {
        String query = "DELETE FROM tbl_kelas WHERE id_kelas = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== NILAI ====================
    public static List<Object[]> getAllNilai() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT nisn, nama_siswa, tugas, uts, uas, nilai_akhir FROM tbl_nilai";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getDouble("tugas"),
                    rs.getDouble("uts"),
                    rs.getDouble("uas"),
                    rs.getDouble("nilai_akhir")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean simpanNilai(String nisn, String nama, double tugas, double uts, double uas, double akhir) {
        String cekQuery = "SELECT COUNT(*) FROM tbl_nilai WHERE nisn = ?";
        try (PreparedStatement cekStmt = KoneksiDB.getConnection().prepareStatement(cekQuery)) {
            cekStmt.setString(1, nisn);
            ResultSet rs = cekStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                String updateQuery = "UPDATE tbl_nilai SET tugas = ?, uts = ?, uas = ?, nilai_akhir = ? WHERE nisn = ?";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(updateQuery)) {
                    pstmt.setDouble(1, tugas);
                    pstmt.setDouble(2, uts);
                    pstmt.setDouble(3, uas);
                    pstmt.setDouble(4, akhir);
                    pstmt.setString(5, nisn);
                    return pstmt.executeUpdate() > 0;
                }
            } else {
                String insertQuery = "INSERT INTO tbl_nilai (nisn, nama_siswa, tugas, uts, uas, nilai_akhir) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(insertQuery)) {
                    pstmt.setString(1, nisn);
                    pstmt.setString(2, nama);
                    pstmt.setDouble(3, tugas);
                    pstmt.setDouble(4, uts);
                    pstmt.setDouble(5, uas);
                    pstmt.setDouble(6, akhir);
                    return pstmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== ABSENSI ====================
    public static List<Object[]> getAllAbsensi() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT tanggal, kelas, nisn, nama_siswa, status FROM tbl_absensi ORDER BY tanggal DESC";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getDate("tanggal"),
                    rs.getString("kelas"),
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean simpanAbsensi(String tanggal, String kelas, String nisn, String nama, String status) {
        String deleteQuery = "DELETE FROM tbl_absensi WHERE tanggal = ? AND nisn = ?";
        try (PreparedStatement deleteStmt = KoneksiDB.getConnection().prepareStatement(deleteQuery)) {
            deleteStmt.setString(1, tanggal);
            deleteStmt.setString(2, nisn);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        String insertQuery = "INSERT INTO tbl_absensi (tanggal, kelas, nisn, nama_siswa, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(insertQuery)) {
            pstmt.setString(1, tanggal);
            pstmt.setString(2, kelas);
            pstmt.setString(3, nisn);
            pstmt.setString(4, nama);
            pstmt.setString(5, status);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== SPP ====================
    public static List<Object[]> getAllSPP() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT nisn, nama_siswa, bulan, nominal, status, tgl_bayar FROM tbl_spp ORDER BY tgl_bayar DESC";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("bulan"),
                    rs.getDouble("nominal"),
                    rs.getString("status"),
                    rs.getDate("tgl_bayar")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean simpanSPP(String nisn, String nama, String bulan, double nominal, String status, String tglBayar) {
        String cekQuery = "SELECT COUNT(*) FROM tbl_spp WHERE nisn = ? AND bulan = ?";
        try (PreparedStatement cekStmt = KoneksiDB.getConnection().prepareStatement(cekQuery)) {
            cekStmt.setString(1, nisn);
            cekStmt.setString(2, bulan);
            ResultSet rs = cekStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                String updateQuery = "UPDATE tbl_spp SET nominal = ?, status = ?, tgl_bayar = ? WHERE nisn = ? AND bulan = ?";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(updateQuery)) {
                    pstmt.setDouble(1, nominal);
                    pstmt.setString(2, status);
                    pstmt.setString(3, tglBayar);
                    pstmt.setString(4, nisn);
                    pstmt.setString(5, bulan);
                    return pstmt.executeUpdate() > 0;
                }
            } else {
                String insertQuery = "INSERT INTO tbl_spp (nisn, nama_siswa, bulan, nominal, status, tgl_bayar) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(insertQuery)) {
                    pstmt.setString(1, nisn);
                    pstmt.setString(2, nama);
                    pstmt.setString(3, bulan);
                    pstmt.setDouble(4, nominal);
                    pstmt.setString(5, status);
                    pstmt.setString(6, tglBayar);
                    return pstmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== TOTAL SPP (DASHBOARD) ====================
    public static double getTotalSPP() {
        double total = 0;
        String query = "SELECT SUM(nominal) as total FROM tbl_spp WHERE status = 'Lunas'";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }
}