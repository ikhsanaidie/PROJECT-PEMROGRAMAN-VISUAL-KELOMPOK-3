public class Session {
    private static String nip;
    private static String username;
    private static String nama;
    private static String role;
    private static String mapel;
    
    public static void setLoginGuru(String nip, String nama, String mapel) {
        Session.nip = nip;
        Session.username = null;
        Session.nama = nama;
        Session.role = "guru";
        Session.mapel = mapel;
        System.out.println("=== SESSION SET ===");
        System.out.println("Role: " + Session.role);
        System.out.println("Nama: " + Session.nama);
    }
    
    public static void setLoginAdmin(String username, String nama, String role) {
        Session.nip = null;
        Session.username = username;
        Session.nama = nama;
        Session.role = role;
        Session.mapel = null;
        System.out.println("=== SESSION SET ===");
        System.out.println("Role: " + Session.role);
        System.out.println("Nama: " + Session.nama);
    }
    
    public static void clear() {
        nip = null;
        username = null;
        nama = null;
        role = null;
        mapel = null;
        System.out.println("=== SESSION CLEARED ===");
    }
    
    public static String getNip() { return nip; }
    public static String getUsername() { return username; }
    public static String getNama() { return nama; }
    public static String getRole() { return role; }
    public static String getMapel() { return mapel; }
    public static boolean isAdmin() { return "admin".equals(role); }
    public static boolean isGuru() { return "guru".equals(role); }
    public static boolean isKepsek() { return "kepsek".equals(role); }
    public static boolean isLoggedIn() { return role != null; }
}