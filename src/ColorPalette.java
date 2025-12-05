import java.awt.Color;

//kelas untuk nyimpen palette warna biar gampang dipake ulang di ui
public class ColorPalette {

    //warna dasar yang sering dipakai untuk elemen ui
    public static final Color PLUM_WINE = new Color(117, 75, 77); //warna merah tua yang dipakai di background dan tombol
    public static final Color COPPER_ROSE = new Color(168, 106, 101); //warna coklat kemerahan buat border dan gradient
    public static final Color DUSTY_ROSE = new Color(171, 136, 130); //warna pink kusam buat highlight ui
    public static final Color ROSEWATER = new Color(216, 166, 148); //warna rose lembut untuk teks dan detail
    public static final Color CHINA_DOLL = new Color(224, 203, 185); //warna putih pink pastel untuk elemen terang
    public static final Color ACCENT_RED = new Color(0xA8, 0x6A, 0x65);

    //warna dark background buat layer background uii
    public static final Color DARK_BG_1 = new Color(42, 31, 31); //warna gelap dasar paling utama
    public static final Color DARK_BG_2 = new Color(61, 46, 40); //warna gelap kedua buat transisi gradient
    public static final Color DARK_BG_3 = new Color(26, 19, 16); //warna gelap paling pekat untuk depth ui
    
    // Warna tabel 
    public static final Color TABLE_BG = new Color(0x3A, 0x34, 0x32);
    public static final Color ROW_EVEN = new Color(0x40, 0x3A, 0x38);
    public static final Color ROW_ODD = new Color(0x3A, 0x34, 0x32);
    public static final Color ROW_HOVER = new Color(0x4A, 0x44, 0x42);
    public static final Color TEXT_LIGHT = new Color(0xE0, 0xCB, 0xB9);
    public static final Color TEXT_SOFT = new Color(0xD0, 0xBB, 0xA9);
    
    // Warna untuk top 3
    public static final Color GOLD_COLOR = new Color(0xFF, 0xE8, 0x6E); // Kuning
    public static final Color SILVER_COLOR = new Color(0xD1, 0xD5, 0xDB); // Silver 
    public static final Color BRONZE_COLOR = new Color(0xCD, 0x95, 0x6E); // Bronze 
    
    // Warna latar top 3 yang sesuai tema
    public static final Color GOLD_BG = new Color(0x4A, 0x3C, 0x20, 80);
    public static final Color SILVER_BG = new Color(0x3C, 0x3C, 0x3C, 80);
    public static final Color BRONZE_BG = new Color(0x3C, 0x28, 0x20, 80);
    
}