-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 05, 2025 at 04:18 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.1.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rengasdengklok_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `decisions`
--

CREATE TABLE `decisions` (
  `decision_id` int(11) NOT NULL,
  `session_id` int(11) DEFAULT NULL,
  `scene_number` int(11) DEFAULT NULL,
  `decision_code` varchar(10) DEFAULT NULL,
  `decision_text` varchar(255) DEFAULT NULL,
  `made_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `decisions`
--

INSERT INTO `decisions` (`decision_id`, `session_id`, `scene_number`, `decision_code`, `decision_text`, `made_at`) VALUES
(1, 3, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 14:28:01'),
(2, 3, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 14:28:03'),
(3, 4, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 16:52:38'),
(4, 4, 2, 'B', 'B: Pendekatan keras dengan ancaman.', '2025-12-05 16:52:41'),
(5, 4, 3, 'A', 'A: Terobos barikade!', '2025-12-05 16:52:43'),
(6, 4, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 16:52:45'),
(7, 4, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 16:52:47'),
(8, 4, 6, 'B', 'B: Tidak percaya, tahan Soebardjo.', '2025-12-05 16:52:48'),
(9, 4, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 16:52:51'),
(10, 4, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 16:52:53'),
(11, 4, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 16:52:54'),
(12, 4, 1, 'B', 'B: Mending tunggu konfirmasi resmi dulu.', '2025-12-05 16:53:02'),
(13, 4, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 16:53:07'),
(14, 4, 3, 'A', 'A: Terobos barikade!', '2025-12-05 16:53:10'),
(15, 4, 4, 'B', 'B: Mengaku tidak punya rencana lain.', '2025-12-05 16:53:11'),
(16, 4, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 16:53:13'),
(17, 4, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 16:53:16'),
(18, 4, 7, 'B', 'B: Bermain diam-diam, susun naskah secara rahasia.', '2025-12-05 16:53:17'),
(19, 4, 8, 'B', 'B: Kalimat revolusioner yang provokatif.', '2025-12-05 16:53:19'),
(20, 5, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 17:04:24'),
(21, 5, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 17:04:25'),
(22, 5, 3, 'A', 'A: Terobos barikade!', '2025-12-05 17:04:25'),
(23, 5, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 17:04:25'),
(24, 5, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 17:04:26'),
(25, 5, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:04:26'),
(26, 5, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:04:26'),
(27, 5, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:04:27'),
(28, 5, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 17:04:27'),
(29, 5, 1, 'B', 'B: Mending tunggu konfirmasi resmi dulu.', '2025-12-05 17:04:32'),
(30, 5, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 17:04:33'),
(31, 5, 3, 'A', 'A: Terobos barikade!', '2025-12-05 17:04:33'),
(32, 5, 4, 'B', 'B: Mengaku tidak punya rencana lain.', '2025-12-05 17:04:35'),
(33, 5, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 17:04:36'),
(34, 5, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:04:36'),
(35, 5, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:04:37'),
(36, 5, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:04:37'),
(37, 5, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 17:04:37'),
(38, 5, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 17:05:08'),
(39, 6, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 17:15:09'),
(40, 6, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 17:15:10'),
(41, 6, 3, 'A', 'A: Terobos barikade!', '2025-12-05 17:15:10'),
(42, 6, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 17:15:10'),
(43, 6, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 17:15:11'),
(44, 6, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:15:11'),
(45, 6, 7, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:15:11'),
(46, 6, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:15:11'),
(47, 6, 8, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:15:12'),
(48, 6, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:15:12'),
(49, 6, 9, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:15:13'),
(50, 6, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 17:15:13'),
(51, 6, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 17:15:15'),
(52, 6, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 17:15:15'),
(53, 6, 3, 'A', 'A: Terobos barikade!', '2025-12-05 17:15:16'),
(54, 6, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 17:15:16'),
(55, 6, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 17:15:16'),
(56, 6, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:15:17'),
(57, 6, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:15:17'),
(58, 6, 8, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 17:15:17'),
(59, 6, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:15:18'),
(60, 6, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 17:15:19'),
(61, 7, 1, 'B', 'B: Mending tunggu konfirmasi resmi dulu.', '2025-12-05 17:19:57'),
(62, 7, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 17:19:59'),
(63, 7, 3, 'B', 'B: Menyamar sebagai tentara PETA.', '2025-12-05 17:20:00'),
(64, 7, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 17:20:01'),
(65, 7, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 17:20:02'),
(66, 7, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 17:20:03'),
(67, 7, 7, 'B', 'B: Bermain diam-diam, susun naskah secara rahasia.', '2025-12-05 17:20:06'),
(68, 7, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 17:20:08'),
(69, 7, 9, 'B', 'B: Rumah Soekarno - aman dan terkontrol.', '2025-12-05 17:20:09'),
(70, 8, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 18:54:07'),
(71, 8, 2, 'B', 'B: Pendekatan keras dengan ancaman.', '2025-12-05 18:54:09'),
(72, 8, 3, 'A', 'A: Terobos barikade!', '2025-12-05 18:54:10'),
(73, 8, 4, 'B', 'B: Mengaku tidak punya rencana lain.', '2025-12-05 18:54:11'),
(74, 8, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 18:54:12'),
(75, 8, 6, 'B', 'B: Tidak percaya, tahan Soebardjo.', '2025-12-05 18:54:13'),
(76, 8, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 18:54:15'),
(77, 8, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 18:54:16'),
(78, 8, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 18:54:17'),
(79, 9, 1, 'B', 'B: Mending tunggu konfirmasi resmi dulu.', '2025-12-05 18:59:49'),
(80, 9, 2, 'A', 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', '2025-12-05 18:59:51'),
(81, 9, 3, 'B', 'B: Menyamar sebagai tentara PETA.', '2025-12-05 18:59:52'),
(82, 9, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 18:59:54'),
(83, 9, 5, 'A', 'A: Gunakan fakta militer untuk meyakinkan.', '2025-12-05 18:59:55'),
(84, 9, 6, 'B', 'B: Tidak percaya, tahan Soebardjo.', '2025-12-05 18:59:56'),
(85, 9, 7, 'A', 'A: Konfrontasi langsung dengan Nishimura.', '2025-12-05 18:59:57'),
(86, 9, 8, 'B', 'B: Kalimat revolusioner yang provokatif.', '2025-12-05 18:59:59'),
(87, 9, 9, 'B', 'B: Rumah Soekarno - aman dan terkontrol.', '2025-12-05 19:00:01'),
(88, 10, 1, 'A', 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', '2025-12-05 19:01:17'),
(89, 10, 2, 'B', 'B: Pendekatan keras dengan ancaman.', '2025-12-05 19:01:22'),
(90, 10, 3, 'A', 'A: Terobos barikade!', '2025-12-05 19:01:28'),
(91, 10, 4, 'A', 'A: Jelaskan strategi persembunyian.', '2025-12-05 19:01:30'),
(92, 10, 5, 'B', 'B: Gunakan ancaman emosional.', '2025-12-05 19:01:32'),
(93, 10, 6, 'A', 'A: Percaya pada Soebardjo.', '2025-12-05 19:01:34'),
(94, 10, 7, 'B', 'B: Bermain diam-diam, susun naskah secara rahasia.', '2025-12-05 19:01:36'),
(95, 10, 8, 'A', 'A: Kalimat diplomatis yang aman.', '2025-12-05 19:01:38'),
(96, 10, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 19:01:39'),
(97, 11, 1, 'B', 'B: Mending tunggu konfirmasi resmi dulu.', '2025-12-05 22:20:58'),
(98, 11, 2, 'B', 'B: Pendekatan keras dengan ancaman.', '2025-12-05 22:21:15'),
(99, 11, 3, 'B', 'B: Menyamar sebagai tentara PETA.', '2025-12-05 22:21:30'),
(100, 11, 4, 'B', 'B: Mengaku tidak punya rencana lain.', '2025-12-05 22:21:41'),
(101, 11, 5, 'B', 'B: Gunakan ancaman emosional.', '2025-12-05 22:21:55'),
(102, 11, 6, 'B', 'B: Tidak percaya, tahan Soebardjo.', '2025-12-05 22:22:07'),
(103, 11, 7, 'B', 'B: Bermain diam-diam, susun naskah secara rahasia.', '2025-12-05 22:22:32'),
(104, 11, 8, 'B', 'B: Kalimat revolusioner yang provokatif.', '2025-12-05 22:22:43'),
(105, 11, 9, 'A', 'A: Lapangan Ikada - heroik tapi berbahaya.', '2025-12-05 22:22:55');

-- --------------------------------------------------------

--
-- Table structure for table `leaderboard`
--

CREATE TABLE `leaderboard` (
  `leaderboard_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `time_taken_seconds` int(11) NOT NULL,
  `attempted_at` datetime DEFAULT current_timestamp(),
  `calculated_score` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `leaderboard`
--

INSERT INTO `leaderboard` (`leaderboard_id`, `user_id`, `score`, `time_taken_seconds`, `attempted_at`, `calculated_score`) VALUES
(1, 1, 11, 97, '2025-11-23 15:19:18', 0),
(2, 1, 11, 122, '2025-11-23 15:29:10', 0),
(3, 1, 0, 5, '2025-11-23 19:17:23', 0),
(4, 2, 10, 119, '2025-12-05 22:25:13', 541);

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `profile_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `character_name` varchar(100) NOT NULL,
  `gender` enum('Male','Female','Other') NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`profile_id`, `user_id`, `character_name`, `gender`, `created_at`) VALUES
(1, 1, 'mutia', 'Female', '2025-11-22 22:02:27'),
(2, 1, 'mutia', 'Female', '2025-11-23 15:46:36'),
(3, 1, 'mutia', 'Female', '2025-11-23 17:28:32'),
(4, 2, 'ihdal', 'Male', '2025-12-05 14:27:47');

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `question_id` int(11) NOT NULL,
  `question_text` text NOT NULL,
  `option_a` varchar(255) NOT NULL,
  `option_b` varchar(255) NOT NULL,
  `option_c` varchar(255) DEFAULT NULL,
  `option_d` varchar(255) DEFAULT NULL,
  `correct_answer` char(1) NOT NULL,
  `difficulty` enum('Easy','Medium','Hard') DEFAULT 'Medium',
  `scene_reference` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `questions`
--

INSERT INTO `questions` (`question_id`, `question_text`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_answer`, `difficulty`, `scene_reference`, `created_at`) VALUES
(1, 'Siapa yang memimpin kelompok pemuda dalam peristiwa Rengasdengklok?', 'Chaerul Saleh', 'Soekarno', 'Ahmad Soebardjo', 'Wikana', 'A', 'Easy', 1, '2025-11-22 21:44:43'),
(2, 'Mengapa para pemuda menculik Soekarno-Hatta?', 'Karena dendam pribadi', 'Untuk memaksa proklamasi kemerdekaan', 'Atas perintah Jepang', 'Keduanya menolak bekerja sama', 'B', 'Easy', 1, '2025-11-22 21:44:43'),
(3, 'Kapan peristiwa Rengasdengklok terjadi?', '15 Agustus 1945', '16 Agustus 1945', '17 Agustus 1945', '18 Agustus 1945', 'B', 'Easy', 1, '2025-11-22 21:44:43'),
(4, 'Di mana lokasi Rengasdengklok?', 'Jakarta', 'Bogor', 'Karawang', 'Bekasi', 'C', 'Easy', 1, '2025-11-22 21:44:43'),
(5, 'Apa yang terjadi setelah Jepang menyerah kepada Sekutu?', 'Indonesia langsung merdeka', 'Kekosongan kekuasaan', 'Belanda langsung kembali', 'Jepang tetap berkuasa', 'B', 'Medium', 1, '2025-11-22 21:44:43'),
(6, 'Pukul berapa Soekarno-Hatta dibawa ke Rengasdengklok?', 'Pukul 03.00 pagi', 'Pukul 08.00 pagi', 'Pukul 12.00 siang', 'Pukul 18.00 sore', 'A', 'Easy', 2, '2025-11-22 21:44:43'),
(7, 'Siapa yang membuka pintu rumah Soekarno?', 'Fatmawati', 'Guntur Soekarnoputra', 'Pelayan', 'Tidak ada yang membuka', 'A', 'Easy', 2, '2025-11-22 21:44:43'),
(8, 'Bagaimana reaksi Soekarno saat dibangunkan?', 'Sangat senang', 'Marah', 'Tidak peduli', 'Langsung setuju', 'B', 'Easy', 2, '2025-11-22 21:44:43'),
(9, 'Mengapa Soekarno menolak dibawa ke Rengasdengklok?', 'Takut pada pemuda', 'Sedang sakit malaria', 'Menunggu perintah Jepang', 'Tidak percaya pada pemuda', 'B', 'Medium', 2, '2025-11-22 21:44:43'),
(10, 'Apa alasan pemuda membawa Soekarno-Hatta?', 'Untuk melindungi dari Jepang', 'Untuk menghukum mereka', 'Untuk liburan', 'Untuk rapat PPKI', 'A', 'Medium', 2, '2025-11-22 21:44:43'),
(11, 'Apa tantangan dalam perjalanan ke Rengasdengklok?', 'Banjir', 'Pos pemeriksaan Jepang', 'Jalan rusak', 'Kendaraan mogok', 'B', 'Easy', 3, '2025-11-22 21:44:43'),
(12, 'Siapa Kempeitai?', 'Tentara Sekutu', 'Polisi militer Jepang', 'Laskar pemuda', 'Tentara Belanda', 'B', 'Medium', 3, '2025-11-22 21:44:43'),
(13, 'Strategi apa yang digunakan untuk melewati pos Jepang?', 'Menyamar', 'Menyerang langsung', 'Mencari jalan lain', 'Menunggu malam', 'A', 'Medium', 3, '2025-11-22 21:44:43'),
(14, 'Mengapa perjalanan dilakukan dini hari?', 'Karena lebih sepi', 'Karena perintah Soekarno', 'Karena hujan', 'Karena janji dengan Jepang', 'A', 'Easy', 3, '2025-11-22 21:44:43'),
(15, 'Apa kendaraan yang digunakan?', 'Mobil pribadi', 'Truk', 'Dokar', 'Sepeda', 'B', 'Hard', 3, '2025-11-22 21:44:43'),
(16, 'Siapa pemilik rumah di Rengasdengklok?', 'Djiaw Kie Siong', 'Chaerul Saleh', 'Ahmad Soebardjo', 'Laksamana Maeda', 'A', 'Easy', 4, '2025-11-22 21:44:43'),
(17, 'Mengapa dipilih rumah Djiaw Kie Siong?', 'Karena mewah', 'Karena tidak dicurigai Jepang', 'Karena dekat markas', 'Karena milik keluarga Soekarno', 'B', 'Easy', 4, '2025-11-22 21:44:43'),
(18, 'Bagaimana reaksi Hatta?', 'Langsung setuju', 'Bingung dan bertanya', 'Marah-marah', 'Diam saja', 'B', 'Medium', 4, '2025-11-22 21:44:43'),
(19, 'Apa pekerjaan Djiaw Kie Siong?', 'Pengusaha', 'Petani', 'Tentara', 'Guru', 'B', 'Hard', 4, '2025-11-22 21:44:43'),
(20, 'Berapa lama Soekarno-Hatta di Rengasdengklok?', '1 hari', '2 hari', '3 hari', 'Beberapa jam saja', 'A', 'Medium', 4, '2025-11-22 21:44:43'),
(21, 'Apa argumen Soekarno menolak proklamasi?', 'Takut pada Belanda', 'Menunggu PPKI', 'Tidak siap mental', 'Perintah Jepang', 'B', 'Easy', 5, '2025-11-22 21:44:43'),
(22, 'Apa yang terjadi di Hiroshima?', 'Gempa bumi', 'Bom atom', 'Kebakaran', 'Tsunami', 'B', 'Easy', 5, '2025-11-22 21:44:43'),
(23, 'Siapa yang meyakinkan Soekarno?', 'Hatta saja', 'Para pemuda', 'Ahmad Soebardjo', 'Fatmawati', 'B', 'Medium', 5, '2025-11-22 21:44:43'),
(24, 'Apa ancaman pemuda jika tidak proklamasi?', 'Akan menyerah pada Jepang', 'Akan terjadi pertumpahan darah', 'Akan mengangkat pemimpin lain', 'Akan bekerja dengan Belanda', 'B', 'Medium', 5, '2025-11-22 21:44:43'),
(25, 'Mengapa Soekarno akhirnya setuju?', 'Karena dipaksa', 'Karena melihat situasi genting', 'Karena janji Jepang', 'Karena sakit', 'B', 'Hard', 5, '2025-11-22 21:44:43'),
(26, 'Siapa Ahmad Soebardjo?', 'Pemimpin pemuda', 'Perwira Jepang', 'Diplomat senior', 'Tentara PETA', 'C', 'Easy', 6, '2025-11-22 21:44:43'),
(27, 'Apa jaminan yang diberikan Soebardjo?', 'Uang', 'Proklamasi besok', 'Perlindungan Jepang', 'Pengakuan Belanda', 'B', 'Easy', 6, '2025-11-22 21:44:43'),
(28, 'Mengapa Soekarno sakit?', 'Karena malaria', 'Karena stres', 'Karena kecelakaan', 'Karena dipukul', 'B', 'Medium', 6, '2025-11-22 21:44:43'),
(29, 'Apa yang terjadi jika Soebardjo ditahan?', 'Proklamasi tertunda', 'Jepang menyerang', 'Soekarno semakin sakit', 'Semua benar', 'D', 'Hard', 6, '2025-11-22 21:44:43'),
(30, 'Kapan mereka kembali ke Jakarta?', '16 Agustus sore', '17 Agustus pagi', '16 Agustus pagi', '15 Agustus malam', 'A', 'Medium', 6, '2025-11-22 21:44:43'),
(31, 'Siapa Laksamana Maeda?', 'Pemimpin pemuda', 'Perwira Jepang pro-Indonesia', 'Tentara Belanda', 'Diplomat Sekutu', 'B', 'Easy', 7, '2025-11-22 21:44:43'),
(32, 'Apa sikap Jenderal Nishimura?', 'Mendukung kemerdekaan', 'Melarang perubahan status quo', 'Netral', 'Langsung menyerah', 'B', 'Easy', 7, '2025-11-22 21:44:43'),
(33, 'Mengapa rumah Maeda dipilih?', 'Karena mewah', 'Karena netral dan aman', 'Karena dekat markas', 'Karena perintah Soekarno', 'B', 'Medium', 7, '2025-11-22 21:44:43'),
(34, 'Apa yang terjadi jika melawan Nishimura?', 'Kemenangan pemuda', 'Kegagalan proklamasi', 'Mediasi berhasil', 'Belanda membantu', 'B', 'Hard', 7, '2025-11-22 21:44:43'),
(35, 'Perwira Jepang mana yang membantu?', 'Nishimura', 'Yamamoto', 'Maeda', 'Tidak ada', 'C', 'Medium', 7, '2025-11-22 21:44:43'),
(36, 'Siapa yang mengetik naskah proklamasi?', 'Soekarno', 'Sayuti Melik', 'Hatta', 'Ahmad Soebardjo', 'B', 'Easy', 8, '2025-11-22 21:44:43'),
(37, 'Di mana naskah proklamasi dirumuskan?', 'Rengasdengklok', 'Rumah Maeda', 'Rumah Soekarno', 'Markas pemuda', 'B', 'Easy', 8, '2025-11-22 21:44:43'),
(38, 'Apa isi kalimat pertama proklamasi?', 'Pengusiran penjajah', 'Pemindahan kekuasaan', 'Kami bangsa Indonesia', 'Dengan ini menyatakan', 'C', 'Medium', 8, '2025-11-22 21:44:43'),
(39, 'Mengapa kalimat provokatif dihindari?', 'Agar tidak diserang Jepang', 'Karena tidak disetujui', 'Karena waktu terbatas', 'Karena perintah Sekutu', 'A', 'Medium', 8, '2025-11-22 21:44:43'),
(40, 'Siapa yang menyusun konsep naskah?', 'Soekarno, Hatta, Ahmad Soebardjo', 'Soekarno saja', 'Pemuda radikal', 'Kelompok PPKI', 'A', 'Hard', 8, '2025-11-22 21:44:43'),
(41, 'Di mana proklamasi awalnya direncanakan?', 'Lapangan Ikada', 'Rumah Soekarno', 'Rengasdengklok', 'Rumah Maeda', 'A', 'Easy', 9, '2025-11-22 21:44:43'),
(42, 'Mengapa lokasi diubah?', 'Karena hujan', 'Karena Lapangan Ikada dijaga Jepang', 'Karena Soekarno sakit', 'Karena tidak ada massa', 'B', 'Easy', 9, '2025-11-22 21:44:43'),
(43, 'Di mana proklamasi akhirnya dibacakan?', 'Jalan Pegangsaan Timur 56', 'Lapangan Ikada', 'Rumah Maeda', 'Rengasdengklok', 'A', 'Easy', 9, '2025-11-22 21:44:43'),
(44, 'Tanggal berapa proklamasi dibacakan?', '16 Agustus 1945', '17 Agustus 1945', '18 Agustus 1945', '19 Agustus 1945', 'B', 'Easy', 9, '2025-11-22 21:44:43'),
(45, 'Pukul berapa proklamasi dibacakan?', 'Pukul 05.00', 'Pukul 10.00', 'Pukul 17.00', 'Pukul 20.00', 'B', 'Medium', 9, '2025-11-22 21:44:43'),
(46, 'Apa nama organisasi pemuda radikal?', 'Menteng 31', 'PPKI', 'PETA', 'Bud Utomo', 'A', 'Hard', 1, '2025-11-22 21:44:43'),
(47, 'Siapa tokoh pemuda dari PETA?', 'Singgih', 'Wikana', 'Chaerul Saleh', 'Semua benar', 'D', 'Hard', 2, '2025-11-22 21:44:43'),
(48, 'Apa peran Fatmawati dalam proklamasi?', 'Menjahit bendera', 'Mengetik naskah', 'Meyakinkan pemuda', 'Menjaga rumah', 'A', 'Medium', 9, '2025-11-22 21:44:43'),
(49, 'Berapa orang yang hadir dalam proklamasi?', 'Less than 100', '100-500', '500-1000', 'More than 1000', 'C', 'Hard', 9, '2025-11-22 21:44:43'),
(50, 'Apa yang terjadi setelah proklamasi?', 'Jepang langsung menyerah', 'Belanda langsung kembali', 'PPKI mengesahkan UUD', 'Pemuda bubar', 'C', 'Medium', 9, '2025-11-22 21:44:43');

-- --------------------------------------------------------

--
-- Table structure for table `quiz_attempts`
--

CREATE TABLE `quiz_attempts` (
  `attempt_id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `score` int(11) NOT NULL,
  `total_questions` int(11) NOT NULL,
  `time_taken_seconds` int(11) NOT NULL,
  `attempted_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `quiz_attempts`
--

INSERT INTO `quiz_attempts` (`attempt_id`, `user_id`, `score`, `total_questions`, `time_taken_seconds`, `attempted_at`) VALUES
(1, 1, 11, 15, 97, '2025-11-23 15:19:18'),
(2, 1, 11, 15, 122, '2025-11-23 15:29:10'),
(3, 1, 0, 15, 5, '2025-11-23 19:17:23'),
(4, 2, 10, 15, 119, '2025-12-05 22:25:13');

-- --------------------------------------------------------

--
-- Table structure for table `scenes`
--

CREATE TABLE `scenes` (
  `scene_id` int(11) NOT NULL,
  `title` varchar(100) DEFAULT NULL,
  `description` text DEFAULT NULL,
  `bg_image` varchar(100) DEFAULT NULL,
  `bg_music` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scenes`
--

INSERT INTO `scenes` (`scene_id`, `title`, `description`, `bg_image`, `bg_music`) VALUES
(1, 'Markas Menteng 31', 'Malam hari di markas pemuda Menteng 31. Radio menyala, asap rokok tebal, Chaerul Saleh mendesak aksi cepat.\nChaerul memukul meja, Wikana menatap Sutan Sjahrir, dan kabar menyerahnya Jepang mengguncang semua orang.', 'scene1.png', 'radio_static.ogg'),
(2, 'Rumah Soekarno', 'Pukul 03.00 pagi di Pegangsaan Timur. Soekarno terserang malaria, Fatmawati membuka pintu dengan cemas.\nKetukan keras di pintu membuat suasana semakin genting.', 'scene2.png', 'night_ambience.ogg'),
(3, 'Perjalanan ke Rengasdengklok', 'Truk tua melaju di jalan gelap menuju Karawang. Kabut pagi dan pos Kempeitai menambah ketegangan.\nSoekarno menggigil demam, Hatta khawatir, dan teriakan \"Tomare!\" menggema.', 'scene3.png', 'engine_rumble.ogg'),
(4, 'Rumah Djiaw Kie Siong', 'Pagi buta di rumah petani sederhana. Djiaw terkejut menerima tamu penting, Soekarno masih lemah, Hatta ragu.', 'scene4.png', 'morning_rooster.ogg'),
(5, 'Debat Kemerdekaan', 'Ruang tamu rumah Djiaw dipenuhi debat sengit. Soekarno dan Hatta masih ragu, pemuda mendesak.\nChaerul Saleh menuntut proklamasi segera.', 'scene5.png', 'heated_debate.ogg'),
(6, 'Kedatangan Ahmad Soebardjo', 'Sore hari di Rengasdengklok. Ahmad Soebardjo tiba dengan kabar bahwa proklamasi bisa dilakukan besok.\nSoekarno lega, pemuda masih curiga.', 'scene6.png', 'relief_theme.ogg'),
(7, 'Konfrontasi dengan Jepang', 'Rumah Laksamana Maeda dipenuhi ketegangan. Nishimura melarang proklamasi, Maeda menengahi.\nSoekarno-Hatta harus memutuskan sikap.', 'scene7.png', 'tension_strings.ogg'),
(8, 'Perumusan Naskah', 'Dini hari di ruang belakang rumah Maeda. Soekarno, Hatta, dan Sayuti Melik merumuskan naskah proklamasi.', 'scene8.png', 'typewriter_loop.ogg'),
(9, 'Momen Bersejarah', 'Pagi 17 Agustus 1945. Massa berkumpul, tentara Jepang siaga. Pilih lokasi pembacaan proklamasi.', 'scene9.png', 'anthem_build.ogg');

-- --------------------------------------------------------

--
-- Table structure for table `scene_characters`
--

CREATE TABLE `scene_characters` (
  `id` int(11) NOT NULL,
  `scene_id` int(11) DEFAULT NULL,
  `character_name` varchar(100) DEFAULT NULL,
  `character_role` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scene_characters`
--

INSERT INTO `scene_characters` (`id`, `scene_id`, `character_name`, `character_role`) VALUES
(1, 1, 'Chaerul Saleh', 'Pemuda Revolusioner'),
(2, 1, 'Wikana', 'Penggerak Pemuda'),
(3, 1, 'Sutan Sjahrir', 'Tokoh Politik'),
(4, 2, 'Soekarno', 'Pemimpin Bangsa'),
(5, 2, 'Mohammad Hatta', 'Pemimpin Bangsa'),
(6, 2, 'Fatmawati', 'Pendamping Soekarno'),
(7, 3, 'Supir PETA', 'Pengemudi Rombongan'),
(8, 3, 'Sukarni', 'Pengawal Pemuda'),
(9, 3, 'Penjaga Kempeitai', 'Penutup Jalan'),
(10, 4, 'Djiaw Kie Siong', 'Tuan Rumah'),
(11, 4, 'Wangsa Mulia', 'Tetangga Pendukung'),
(12, 4, 'Shodanco Singgih', 'Pengawas Pemuda'),
(13, 5, 'Chaerul Saleh', 'Pemimpin Pemuda'),
(14, 5, 'Sukarni', 'Pemicu Perdebatan'),
(15, 5, 'Latief Hendraningrat', 'Pengaman Rencana'),
(16, 6, 'Ahmad Soebardjo', 'Diplomat Penghubung'),
(17, 6, 'Soekarno', 'Tokoh Sentral'),
(18, 6, 'Mohammad Hatta', 'Negosiator Rasional'),
(19, 7, 'Laksamana Maeda', 'Pelindung Diam-Diam'),
(20, 7, 'Jenderal Nishimura', 'Utusan Jepang'),
(21, 7, 'Soekarno', 'Tokoh Utama'),
(22, 7, 'Mohammad Hatta', 'Penengah Diplomatis'),
(23, 8, 'Soekarno', 'Penyusun Utama'),
(24, 8, 'Mohammad Hatta', 'Pengarah Kalimat'),
(25, 8, 'Sayuti Melik', 'Pengetik Proklamasi'),
(26, 9, 'Soekarno', 'Pembaca Proklamasi'),
(27, 9, 'Mohammad Hatta', 'Pendamping Pembacaan'),
(28, 9, 'Fatmawati', 'Penjahit Merah Putih'),
(29, 9, 'Latief Hendraningrat', 'Pengibar Bendera');

-- --------------------------------------------------------

--
-- Table structure for table `scene_choices`
--

CREATE TABLE `scene_choices` (
  `choice_id` int(11) NOT NULL,
  `scene_id` int(11) DEFAULT NULL,
  `choice_text` varchar(255) DEFAULT NULL,
  `next_scene_id` int(11) DEFAULT NULL,
  `impact_soekarno` int(11) DEFAULT 0,
  `impact_hatta` int(11) DEFAULT 0,
  `impact_pemuda` int(11) DEFAULT 0,
  `impact_trust` int(11) DEFAULT 0,
  `required_trust` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `scene_choices`
--

INSERT INTO `scene_choices` (`choice_id`, `scene_id`, `choice_text`, `next_scene_id`, `impact_soekarno`, `impact_hatta`, `impact_pemuda`, `impact_trust`, `required_trust`) VALUES
(1, 1, 'A: Setuju dengan Chaerul! Desak Soekarno-Hatta sekarang juga!', 2, 0, 0, 20, 10, 0),
(2, 1, 'B: Mending tunggu konfirmasi resmi dulu.', 2, 0, 0, -15, -10, 0),
(3, 2, 'A: Pendekatan diplomatis untuk membujuk Bung Karno.', 3, 20, 15, 0, 15, 0),
(4, 2, 'B: Pendekatan keras dengan ancaman.', 3, -30, -20, 0, -20, 0),
(5, 3, 'A: Terobos barikade!', 4, -10, 0, 15, 5, 0),
(6, 3, 'B: Menyamar sebagai tentara PETA.', 4, 10, 15, 0, 20, 0),
(7, 4, 'A: Jelaskan strategi persembunyian.', 5, 10, 20, 0, 15, 0),
(8, 4, 'B: Mengaku tidak punya rencana lain.', 5, 0, -15, 0, -10, 0),
(9, 5, 'A: Gunakan fakta militer untuk meyakinkan.', 6, 15, 0, 0, 10, 0),
(10, 5, 'B: Gunakan ancaman emosional.', 6, -5, 0, 25, 15, 0),
(11, 6, 'A: Percaya pada Soebardjo.', 7, 20, 20, 0, 25, 0),
(12, 6, 'B: Tidak percaya, tahan Soebardjo.', 7, -20, 0, 15, -30, 0),
(13, 7, 'A: Konfrontasi langsung dengan Nishimura.', 8, -40, 0, 30, -50, 0),
(14, 7, 'B: Bermain diam-diam, susun naskah secara rahasia.', 8, 25, 20, 0, 30, 0),
(15, 8, 'A: Kalimat diplomatis yang aman.', 9, 15, 15, 0, 20, 0),
(16, 8, 'B: Kalimat revolusioner yang provokatif.', 9, 0, 0, 20, -15, 0),
(17, 9, 'A: Lapangan Ikada - heroik tapi berbahaya.', 9, 0, 0, 40, -60, 0),
(18, 9, 'B: Rumah Soekarno - aman dan terkontrol.', 9, 30, 25, 0, 40, 0);

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `session_id` int(11) NOT NULL,
  `profile_id` int(11) DEFAULT NULL,
  `current_scene` int(11) DEFAULT 1,
  `relationship_soekarno` int(11) DEFAULT 50,
  `relationship_hatta` int(11) DEFAULT 50,
  `trust_level` int(11) DEFAULT 50,
  `ending_achieved` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `last_updated` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`session_id`, `profile_id`, `current_scene`, `relationship_soekarno`, `relationship_hatta`, `trust_level`, `ending_achieved`, `created_at`, `last_updated`) VALUES
(1, 1, 1, 50, 50, 50, NULL, '2025-11-23 15:27:03', '2025-11-23 15:27:03'),
(2, 1, 1, 50, 50, 50, NULL, '2025-11-23 18:00:04', '2025-11-23 18:00:04'),
(3, 4, 3, 80, 65, 80, NULL, '2025-12-05 14:27:53', '2025-12-05 14:28:05'),
(4, 4, 9, 100, 100, 65, NULL, '2025-12-05 16:52:34', '2025-12-05 16:53:20'),
(5, 4, 9, 75, 100, 0, 'BAD_ENDING_DIHANCURKAN_JEPANG', '2025-12-05 17:04:23', '2025-12-05 17:05:08'),
(6, 4, 9, 35, 100, 0, 'BAD_ENDING_DIHANCURKAN_JEPANG', '2025-12-05 17:15:08', '2025-12-05 17:15:19'),
(7, 4, 9, 100, 100, 100, 'GOOD_ENDING_KEMERDEKAAN_TEGANG', '2025-12-05 17:19:55', '2025-12-05 17:20:12'),
(8, 4, 9, 15, 40, 0, 'BAD_ENDING_DIHANCURKAN_JEPANG', '2025-12-05 18:54:05', '2025-12-05 18:54:17'),
(9, 4, 9, 70, 100, 45, 'ENDING_TIDAK_PASTI', '2025-12-05 18:59:47', '2025-12-05 19:00:01'),
(10, 4, 9, 80, 100, 40, 'ENDING_TIDAK_PASTI', '2025-12-05 19:01:12', '2025-12-05 19:01:39'),
(11, 4, 9, 25, 50, 0, 'BAD_ENDING_DIHANCURKAN_JEPANG', '2025-12-05 22:20:40', '2025-12-05 22:22:55'),
(12, 4, 1, 50, 50, 50, NULL, '2025-12-05 23:09:12', '2025-12-05 23:09:13');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `created_at`) VALUES
(1, 'mutia', 'mutia', '2025-11-22 22:02:19'),
(2, 'ihdal', 'ihdal', '2025-12-05 14:27:47');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `decisions`
--
ALTER TABLE `decisions`
  ADD PRIMARY KEY (`decision_id`),
  ADD KEY `session_id` (`session_id`);

--
-- Indexes for table `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD PRIMARY KEY (`leaderboard_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `profiles`
--
ALTER TABLE `profiles`
  ADD PRIMARY KEY (`profile_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`question_id`);

--
-- Indexes for table `quiz_attempts`
--
ALTER TABLE `quiz_attempts`
  ADD PRIMARY KEY (`attempt_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `scenes`
--
ALTER TABLE `scenes`
  ADD PRIMARY KEY (`scene_id`);

--
-- Indexes for table `scene_characters`
--
ALTER TABLE `scene_characters`
  ADD PRIMARY KEY (`id`),
  ADD KEY `scene_id` (`scene_id`);

--
-- Indexes for table `scene_choices`
--
ALTER TABLE `scene_choices`
  ADD PRIMARY KEY (`choice_id`),
  ADD KEY `scene_id` (`scene_id`);

--
-- Indexes for table `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`session_id`),
  ADD KEY `profile_id` (`profile_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `decisions`
--
ALTER TABLE `decisions`
  MODIFY `decision_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;

--
-- AUTO_INCREMENT for table `leaderboard`
--
ALTER TABLE `leaderboard`
  MODIFY `leaderboard_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `profiles`
--
ALTER TABLE `profiles`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `question_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `quiz_attempts`
--
ALTER TABLE `quiz_attempts`
  MODIFY `attempt_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `scene_characters`
--
ALTER TABLE `scene_characters`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `scene_choices`
--
ALTER TABLE `scene_choices`
  MODIFY `choice_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
  MODIFY `session_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `decisions`
--
ALTER TABLE `decisions`
  ADD CONSTRAINT `decisions_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `sessions` (`session_id`);

--
-- Constraints for table `leaderboard`
--
ALTER TABLE `leaderboard`
  ADD CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

--
-- Constraints for table `profiles`
--
ALTER TABLE `profiles`
  ADD CONSTRAINT `profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `quiz_attempts`
--
ALTER TABLE `quiz_attempts`
  ADD CONSTRAINT `quiz_attempts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `scene_characters`
--
ALTER TABLE `scene_characters`
  ADD CONSTRAINT `scene_characters_ibfk_1` FOREIGN KEY (`scene_id`) REFERENCES `scenes` (`scene_id`) ON DELETE CASCADE;

--
-- Constraints for table `scene_choices`
--
ALTER TABLE `scene_choices`
  ADD CONSTRAINT `scene_choices_ibfk_1` FOREIGN KEY (`scene_id`) REFERENCES `scenes` (`scene_id`) ON DELETE CASCADE;

--
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`profile_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
