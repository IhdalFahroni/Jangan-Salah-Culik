# Rengasdengklok: Jangan Salah Culik

Dokumentasi ini merangkum arsitektur, cara menjalankan, serta deskripsi setiap kelas yang ada di proyek gim cerita interaktif bertema sejarah ini. Seluruh informasi disajikan dalam bahasa Indonesia.

## Gambaran Umum

- Gim naratif bergaya Telltale yang memadukan percabangan cerita, manajemen hubungan tokoh, kuis sejarah, serta penyimpanan progres ke database MySQL.
- Antarmuka dibangun dengan Swing (`JPanel`, `JFrame`, dan komponen turunan) dan dikendalikan melalui `CardLayout` sehingga tiap layar (login, menu utama, cerita, kuis, leaderboard, ending) dapat ditampilkan bergantian.
- Data penting seperti pengguna, profil, sesi permainan, keputusan pemain, serta leaderboard disimpan menggunakan JDBC ke database `rengasdengklok_db`.


## Struktur Folder

- `src/` : Seluruh kode sumber Java.
- `bin/` : Hasil kompilasi `.class` (otomatis terbentuk setelah `javac`).
- `assets/` : Aset pendukung (musik latar, gambar, skrip SQL dalam `assets/db`).
- `lib/` : Tempat dependensi tambahan jika dibutuhkan.

## Dokumentasi Kelas

| Kelas                      | Tanggung Jawab Utama                                                                                                                                                                                                                                          |
| -------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `App.java`                 | Titik masuk aplikasi: mengatur look & feel Swing lalu menjalankan `RengasdengklokGame` pada Event Dispatch Thread.                                                                                                                                            |
| `RengasdengklokGame.java`  | Orkestrator utama. Menyimpan state pengguna/profil/sesi, membuat panel-panel Swing, mengelola `CardLayout`, menginisialisasi manager (scene, quiz, audio, database, UI), dan menyediakan API seperti `startGame`, `showPanel`, `logout`, serta `replayStory`. |
| `UIManager.java`           | Penghubung logika ↔ UI. Menyimpan referensi `StoryPanel`, `GameOverPanel`, map komponen, menangani pergantian layar, pembaruan relasi, countdown keputusan, serta delegasi pemilihan pilihan.                                                                 |
| `StoryPanel.java`          | Panel utama untuk menampilkan scene: judul, deskripsi, daftar tokoh, statistik hubungan, pilihan keputusan, dan indikator timer. Menerima update dari `UIManager`.                                                                                            |
| `GameSession.java`         | Menyimpan progres cerita (scene aktif, relasi Soekarno/Hatta/Pemuda, level kepercayaan, ending). Mengelola awal permainan, pemrosesan keputusan, perhitungan ending, sinkronisasi UI, serta penyimpanan ke database.                                          |
| `SceneManager.java`        | Memuat data setiap scene berdasarkan ID (judul, deskripsi, tokoh, pilihan, aset latar) dan mengirimkannya ke UI. Menyimpan riwayat scene serta daftar pilihan yang tersedia.                                                                                  |
| `Scene.java`               | Model data satu scene: ID, teks, latar gambar/musik, daftar `Character`, dan daftar `Choice`.                                                                                                                                                                 |
| `Character.java`           | Representasi tokoh di scene (ID, nama, peran, path gambar, nilai hubungan). Menyediakan stub `displayCharacter` dan `updateRelationship`.                                                                                                                     |
| `Choice.java`              | Model satu pilihan. Menyimpan teks, scene lanjutan, dampak relasi (map), serta kebutuhan level trust. Memiliki helper `executeChoice` dan `calculateImpact` untuk logging cepat.                                                                              |
| `Timer.java`               | Kelas dasar penghitung mundur berbasis thread dengan hook `onTick` dan `onTimerFinished`. Dipakai sebagai pondasi timer keputusan.                                                                                                                            |
| `DecisionTimer.java`       | Turunan `Timer` yang mengirim update detik tersisa ke `UIManager` melalui `SwingUtilities` dan otomatis memilih pilihan default saat waktu habis dengan berkoordinasi bersama `SceneManager` dan `GameSession`.                                               |
| `RelationshipManager.java` | Mempertahankan map hubungan antar faksi/tokoh (0-100). Menyediakan operasi `updateRelationship`, `getRelationship`, dan pengecekan syarat nilai minimum.                                                                                                      |
| `AudioClip.java`           | Struktur metadata audio (nama, path, durasi, looping) berikut stub `play/stop/pause`.                                                                                                                                                                         |
| `AudioManager.java`        | Mengelola musik latar dan efek suara. Menyimpan clip aktif, map efek suara, flag mute, volume, serta utilitas pembersihan sumber daya.                                                                                                                        |
| `DatabaseManager.java`     | Lapisan akses data JDBC: koneksi, query, update, autentikasi user, CRUD profil, sesi game, penyimpanan keputusan, kuis, leaderboard, serta utilitas konversi enum gender.                                                                                     |
| `User.java`                | Entitas pengguna (ID, username, password, tanggal pembuatan) ditambah stub `login`, `register`, `updateProfile`.                                                                                                                                              |
| `PlayerProfile.java`       | Entitas profil pemain (ID profil, user ID, nama karakter, gender, waktu dibuat) beserta stub CRUD.                                                                                                                                                            |
| `Question.java`            | Model soal kuis. Menyimpan teks, opsi A-D, jawaban benar, tingkat kesulitan, dan referensi scene. Memiliki helper `checkAnswer`.                                                                                                                              |
| `QuizManager.java`         | Mengatur siklus kuis: memuat soal acak dari DB, mem-tracking jawaban dan skor, navigasi antar soal, penentuan waktu, hingga penyimpanan hasil ke tabel `quiz_attempts` dan `leaderboard`.                                                                     |
| `QuizPanel.java`           | Panel UI kuis lengkap dengan hitung mundur pra-quiz, timer kuis, teks soal, opsi radio, navigasi, konfirmasi selesai, serta panel hasil. Mengelola thread countdown dengan aman.                                                                              |
| `LeaderboardPanel.java`    | Panel leaderboard bergaya vintage yang menampilkan hingga 10 skor terbaik dengan ikon medali serta tombol kembali ke menu. Memanggil `DatabaseManager#getTopScores`.                                                                                          |
| `LoginPanel.java`          | Form login/registrasi dengan validasi dasar, efek hover, serta dekorasi. Memanggil `RengasdengklokGame` untuk menyetel user aktif dan memuat profil.                                                                                                          |
| `MainMenuPanel.java`       | Menu utama berisi tombol Mulai Game, Kuis, Leaderboard, Logout, plus dialog edit profil (mengubah nama karakter & gender menggunakan radio button).                                                                                                           |
| `GameOverPanel.java`       | Panel ending yang menampilkan judul ending, deskripsi naratif, snapshot relasi akhir, serta tombol kembali ke menu atau main lagi (callback dari `RengasdengklokGame`).                                                                                       |
| `ScoreEntry.java`          | DTO untuk satu baris leaderboard (username, nama karakter, skor, durasi, waktu percobaan). Dipakai `LeaderboardPanel` dan `QuizManager`.                                                                                                                      |

> **Catatan:** Jika menambah kelas baru, ikuti pola dokumentasi di atas: jelaskan peran, state penting, dan alur interaksi antar komponen.

## Pengembangan Lanjutan

- Lengkapi stub audio pada `AudioManager`/`AudioClip` agar imersi lebih maksimal.
- Tambahkan validasi lanjutan untuk pembuatan akun serta profil pemain.
- Perluas data scene pada `SceneManager` bila ingin menambah tokoh atau percabangan baru.
- Sediakan pengujian otomatis (mis. JUnit) untuk memastikan logika ending dan dampak pilihan tetap konsisten setelah perubahan.
