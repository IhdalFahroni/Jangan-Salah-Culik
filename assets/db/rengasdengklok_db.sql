-- phpMyAdmin SQL Dump
-- version 5.2.2
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 23, 2025 at 11:29 AM
-- Server version: 8.4.3
-- PHP Version: 8.1.32

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
  `decision_id` int NOT NULL,
  `session_id` int DEFAULT NULL,
  `scene_number` int DEFAULT NULL,
  `decision_code` varchar(10) DEFAULT NULL,
  `decision_text` varchar(255) DEFAULT NULL,
  `made_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `leaderboard`
--

CREATE TABLE `leaderboard` (
  `leaderboard_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `profile_id` int DEFAULT NULL,
  `score` int NOT NULL,
  `time_taken_seconds` int NOT NULL,
  `attempted_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `leaderboard`
--

INSERT INTO `leaderboard` (`leaderboard_id`, `user_id`, `profile_id`, `score`, `time_taken_seconds`, `attempted_at`) VALUES
(1, 1, 1, 11, 97, '2025-11-23 15:19:18'),
(2, 1, 1, 11, 122, '2025-11-23 15:29:10'),
(3, 1, 1, 0, 5, '2025-11-23 19:17:23');

-- --------------------------------------------------------

--
-- Table structure for table `profiles`
--

CREATE TABLE `profiles` (
  `profile_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `character_name` varchar(100) NOT NULL,
  `gender` enum('Male','Female','Other') NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `profiles`
--

INSERT INTO `profiles` (`profile_id`, `user_id`, `character_name`, `gender`, `created_at`) VALUES
(1, 1, 'mutia', 'Female', '2025-11-22 22:02:27'),
(2, 1, 'mutia', 'Female', '2025-11-23 15:46:36'),
(3, 1, 'mutia', 'Female', '2025-11-23 17:28:32');

-- --------------------------------------------------------

--
-- Table structure for table `questions`
--

CREATE TABLE `questions` (
  `question_id` int NOT NULL,
  `question_text` text NOT NULL,
  `option_a` varchar(255) NOT NULL,
  `option_b` varchar(255) NOT NULL,
  `option_c` varchar(255) DEFAULT NULL,
  `option_d` varchar(255) DEFAULT NULL,
  `correct_answer` char(1) NOT NULL,
  `difficulty` enum('Easy','Medium','Hard') DEFAULT 'Medium',
  `scene_reference` int DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
  `attempt_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `score` int NOT NULL,
  `total_questions` int NOT NULL,
  `time_taken_seconds` int NOT NULL,
  `attempted_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `quiz_attempts`
--

INSERT INTO `quiz_attempts` (`attempt_id`, `user_id`, `score`, `total_questions`, `time_taken_seconds`, `attempted_at`) VALUES
(1, 1, 11, 15, 97, '2025-11-23 15:19:18'),
(2, 1, 11, 15, 122, '2025-11-23 15:29:10'),
(3, 1, 0, 15, 5, '2025-11-23 19:17:23');

-- --------------------------------------------------------

--
-- Table structure for table `sessions`
--

CREATE TABLE `sessions` (
  `session_id` int NOT NULL,
  `profile_id` int DEFAULT NULL,
  `current_scene` int DEFAULT '1',
  `relationship_soekarno` int DEFAULT '50',
  `relationship_hatta` int DEFAULT '50',
  `trust_level` int DEFAULT '50',
  `ending_achieved` varchar(100) DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `sessions`
--

INSERT INTO `sessions` (`session_id`, `profile_id`, `current_scene`, `relationship_soekarno`, `relationship_hatta`, `trust_level`, `ending_achieved`, `created_at`, `last_updated`) VALUES
(1, 1, 1, 50, 50, 50, NULL, '2025-11-23 15:27:03', '2025-11-23 15:27:03'),
(2, 1, 1, 50, 50, 50, NULL, '2025-11-23 18:00:04', '2025-11-23 18:00:04');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `username`, `password`, `created_at`) VALUES
(1, 'mutia', 'mutia', '2025-11-22 22:02:19');

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
  ADD KEY `user_id` (`user_id`),
  ADD KEY `profile_id` (`profile_id`);

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
  MODIFY `decision_id` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `leaderboard`
--
ALTER TABLE `leaderboard`
  MODIFY `leaderboard_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `profiles`
--
ALTER TABLE `profiles`
  MODIFY `profile_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `questions`
--
ALTER TABLE `questions`
  MODIFY `question_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT for table `quiz_attempts`
--
ALTER TABLE `quiz_attempts`
  MODIFY `attempt_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `sessions`
--
ALTER TABLE `sessions`
  MODIFY `session_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

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
  ADD CONSTRAINT `leaderboard_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
  ADD CONSTRAINT `leaderboard_ibfk_2` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`profile_id`);

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
-- Constraints for table `sessions`
--
ALTER TABLE `sessions`
  ADD CONSTRAINT `sessions_ibfk_1` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`profile_id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
