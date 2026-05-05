import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    
    // ==================== SISWA ====================
    public static List<String[]> getAllSiswa() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT nisn, nis, nama, jk, tempat_lahir, tgl_lahir, agama, alamat, no_hp, email, kelas, jurusan, tahun_masuk, nama_ayah, nama_ibu FROM tbl_siswa ORDER BY nisn";
        
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String[] row = {
                    rs.getString("nisn"),
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("tempat_lahir"),
                    rs.getString("tgl_lahir") != null ? rs.getString("tgl_lahir") : "",
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("no_hp"),
                    rs.getString("email"),
                    rs.getString("kelas"),
                    rs.getString("jurusan"),
                    rs.getString("tahun_masuk"),
                    rs.getString("nama_ayah"),
                    rs.getString("nama_ibu")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static String[] getSiswaByNISN(String nisn) {
        String query = "SELECT nisn, nis, nama, jk, tempat_lahir, tgl_lahir, agama, alamat, no_hp, email, kelas, jurusan, tahun_masuk, nama_ayah, nama_ibu FROM tbl_siswa WHERE nisn = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new String[]{
                    rs.getString("nisn"),
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("tempat_lahir"),
                    rs.getString("tgl_lahir") != null ? rs.getString("tgl_lahir") : "",
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("no_hp"),
                    rs.getString("email"),
                    rs.getString("kelas"),
                    rs.getString("jurusan"),
                    rs.getString("tahun_masuk"),
                    rs.getString("nama_ayah"),
                    rs.getString("nama_ibu")
                };
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean tambahSiswa(String nisn, String nis, String nama, String jk, 
                                      String tempatLahir, String tglLahir, String agama, 
                                      String alamat, String noHp, String email, String kelas, 
                                      String jurusan, String tahunMasuk, String namaAyah, String namaIbu) {
        String query = "INSERT INTO tbl_siswa (nisn, nis, nama, jk, tempat_lahir, tgl_lahir, agama, alamat, no_hp, email, kelas, jurusan, tahun_masuk, nama_ayah, nama_ibu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            pstmt.setString(2, nis);
            pstmt.setString(3, nama);
            pstmt.setString(4, jk);
            pstmt.setString(5, tempatLahir);
            pstmt.setString(6, tglLahir);
            pstmt.setString(7, agama);
            pstmt.setString(8, alamat);
            pstmt.setString(9, noHp);
            pstmt.setString(10, email);
            pstmt.setString(11, kelas);
            pstmt.setString(12, jurusan);
            pstmt.setString(13, tahunMasuk);
            pstmt.setString(14, namaAyah);
            pstmt.setString(15, namaIbu);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateSiswa(String nisnLama, String nisnBaru, String nis, String nama, 
                                      String jk, String tempatLahir, String tglLahir, 
                                      String agama, String alamat, String noHp, String email, 
                                      String kelas, String jurusan, String tahunMasuk, 
                                      String namaAyah, String namaIbu) {
        String query = "UPDATE tbl_siswa SET nisn = ?, nis = ?, nama = ?, jk = ?, tempat_lahir = ?, tgl_lahir = ?, agama = ?, alamat = ?, no_hp = ?, email = ?, kelas = ?, jurusan = ?, tahun_masuk = ?, nama_ayah = ?, nama_ibu = ? WHERE nisn = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisnBaru);
            pstmt.setString(2, nis);
            pstmt.setString(3, nama);
            pstmt.setString(4, jk);
            pstmt.setString(5, tempatLahir);
            pstmt.setString(6, tglLahir);
            pstmt.setString(7, agama);
            pstmt.setString(8, alamat);
            pstmt.setString(9, noHp);
            pstmt.setString(10, email);
            pstmt.setString(11, kelas);
            pstmt.setString(12, jurusan);
            pstmt.setString(13, tahunMasuk);
            pstmt.setString(14, namaAyah);
            pstmt.setString(15, namaIbu);
            pstmt.setString(16, nisnLama);
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
        String query = "SELECT nisn, nis, nama, jk, tempat_lahir, tgl_lahir, agama, alamat, no_hp, email, kelas, jurusan, tahun_masuk, nama_ayah, nama_ibu FROM tbl_siswa WHERE kelas = ? ORDER BY nama";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, kelas);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nisn"),
                    rs.getString("nis"),
                    rs.getString("nama"),
                    rs.getString("jk"),
                    rs.getString("tempat_lahir"),
                    rs.getString("tgl_lahir") != null ? rs.getString("tgl_lahir") : "",
                    rs.getString("agama"),
                    rs.getString("alamat"),
                    rs.getString("no_hp"),
                    rs.getString("email"),
                    rs.getString("kelas"),
                    rs.getString("jurusan"),
                    rs.getString("tahun_masuk"),
                    rs.getString("nama_ayah"),
                    rs.getString("nama_ibu")
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
        String query = "SELECT nip, jk, nama_guru, mapel FROM tbl_guru ORDER BY nip";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nip"),
                    rs.getString("jk") != null ? rs.getString("jk") : "",
                    rs.getString("nama_guru"),
                    rs.getString("mapel")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean tambahGuru(String nip, String jk, String nama, String mapel) {
        String query = "INSERT INTO tbl_guru (nip, jk, nama_guru, mapel) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nip);
            pstmt.setString(2, jk);
            pstmt.setString(3, nama);
            pstmt.setString(4, mapel);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean updateGuru(String nipLama, String nipBaru, String jk, String nama, String mapel) {
        String query = "UPDATE tbl_guru SET nip = ?, jk = ?, nama_guru = ?, mapel = ? WHERE nip = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nipBaru);
            pstmt.setString(2, jk);
            pstmt.setString(3, nama);
            pstmt.setString(4, mapel);
            pstmt.setString(5, nipLama);
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
        String query = "SELECT nisn, nama_siswa, mata_pelajaran, guru_pengajar, tugas, uts, uas, nilai_akhir FROM tbl_nilai";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("mata_pelajaran") != null ? rs.getString("mata_pelajaran") : "",
                    rs.getString("guru_pengajar") != null ? rs.getString("guru_pengajar") : "",
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
    
    public static boolean simpanNilai(String nisn, String nama, String mataPelajaran, String guruPengajar,
                                      double tugas, double uts, double uas, double akhir) {
        String cekQuery = "SELECT COUNT(*) FROM tbl_nilai WHERE nisn = ? AND mata_pelajaran = ?";
        try (PreparedStatement cekStmt = KoneksiDB.getConnection().prepareStatement(cekQuery)) {
            cekStmt.setString(1, nisn);
            cekStmt.setString(2, mataPelajaran);
            ResultSet rs = cekStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                String updateQuery = "UPDATE tbl_nilai SET guru_pengajar = ?, tugas = ?, uts = ?, uas = ?, nilai_akhir = ? WHERE nisn = ? AND mata_pelajaran = ?";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(updateQuery)) {
                    pstmt.setString(1, guruPengajar);
                    pstmt.setDouble(2, tugas);
                    pstmt.setDouble(3, uts);
                    pstmt.setDouble(4, uas);
                    pstmt.setDouble(5, akhir);
                    pstmt.setString(6, nisn);
                    pstmt.setString(7, mataPelajaran);
                    return pstmt.executeUpdate() > 0;
                }
            } else {
                String insertQuery = "INSERT INTO tbl_nilai (nisn, nama_siswa, mata_pelajaran, guru_pengajar, tugas, uts, uas, nilai_akhir) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(insertQuery)) {
                    pstmt.setString(1, nisn);
                    pstmt.setString(2, nama);
                    pstmt.setString(3, mataPelajaran);
                    pstmt.setString(4, guruPengajar);
                    pstmt.setDouble(5, tugas);
                    pstmt.setDouble(6, uts);
                    pstmt.setDouble(7, uas);
                    pstmt.setDouble(8, akhir);
                    return pstmt.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String[]> getAllMataPelajaranFromGuru() {
        List<String[]> list = new ArrayList<>();
        String query = "SELECT nama_guru, mapel FROM tbl_guru ORDER BY mapel";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("nama_guru"),
                    rs.getString("mapel")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
        String query = "INSERT INTO tbl_absensi (tanggal, kelas, nisn, nama_siswa, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
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
    
    public static boolean hapusAbsensiByTanggalDanKelas(String tanggal, String kelas) {
        String query = "DELETE FROM tbl_absensi WHERE tanggal = ? AND kelas = ?";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, tanggal);
            pstmt.setString(2, kelas);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== PEMBAYARAN SPP ====================
    public static List<Object[]> getAllPembayaran() {
        List<Object[]> list = new ArrayList<>();
        String query = "SELECT id_pembayaran, nisn, nama_siswa, jenis_tagihan, bulan, total_tagihan, dibayar, sisa_tagihan, status, tgl_bayar FROM tbl_pembayaran ORDER BY tgl_bayar DESC";
        try (Statement stmt = KoneksiDB.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("id_pembayaran"),
                    rs.getString("nisn"),
                    rs.getString("nama_siswa"),
                    rs.getString("jenis_tagihan"),
                    rs.getString("bulan") != null ? rs.getString("bulan") : "",
                    rs.getDouble("total_tagihan"),
                    rs.getDouble("dibayar"),
                    rs.getDouble("sisa_tagihan"),
                    rs.getString("status"),
                    rs.getDate("tgl_bayar") != null ? rs.getDate("tgl_bayar").toString() : ""
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static boolean simpanPembayaran(String nisn, String nama, String jenisTagihan, String bulan, 
                                           double totalTagihan, double dibayar, double sisaTagihan, 
                                           String status, String tglBayar, String catatan) {
        String query = "INSERT INTO tbl_pembayaran (nisn, nama_siswa, jenis_tagihan, bulan, total_tagihan, dibayar, sisa_tagihan, status, tgl_bayar, catatan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            pstmt.setString(2, nama);
            pstmt.setString(3, jenisTagihan);
            pstmt.setString(4, bulan);
            pstmt.setDouble(5, totalTagihan);
            pstmt.setDouble(6, dibayar);
            pstmt.setDouble(7, sisaTagihan);
            pstmt.setString(8, status);
            pstmt.setString(9, tglBayar);
            pstmt.setString(10, catatan);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static double getTotalTunggakanSiswa(String nisn) {
        double total = 0;
        String query = "SELECT SUM(sisa_tagihan) as sisa FROM tbl_pembayaran WHERE nisn = ? AND status != 'Lunas'";
        try (PreparedStatement pstmt = KoneksiDB.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nisn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("sisa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
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
    
    public static double getTotalSPP() {
        double total = 0;
        String query = "SELECT SUM(dibayar) as total FROM tbl_pembayaran WHERE status = 'Lunas'";
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