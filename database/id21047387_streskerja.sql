-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Waktu pembuatan: 06 Agu 2023 pada 12.14
-- Versi server: 10.5.20-MariaDB
-- Versi PHP: 7.3.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id21047387_streskerja`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `aturan`
--

CREATE TABLE `aturan` (
  `id_aturan` int(11) NOT NULL,
  `id_penyakit` int(11) NOT NULL,
  `id_gejala` int(11) NOT NULL,
  `nilai_cf` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `aturan`
--

INSERT INTO `aturan` (`id_aturan`, `id_penyakit`, `id_gejala`, `nilai_cf`) VALUES
(632, 13, 60, 0.2),
(633, 13, 61, 0.4),
(634, 13, 64, 0.4),
(635, 13, 71, 0.8),
(636, 13, 72, 0.7),
(637, 13, 74, 0.4),
(638, 13, 77, 0.4),
(639, 13, 78, 0.4),
(640, 13, 80, 0.4),
(641, 13, 86, 0.4),
(642, 13, 89, 0.4),
(643, 13, 93, 0.3),
(644, 13, 94, 0.4),
(645, 14, 54, 1),
(646, 14, 55, 1),
(647, 14, 56, 1),
(648, 14, 57, 0.7),
(649, 14, 58, 1),
(650, 14, 59, 0.7),
(651, 14, 60, 0.5),
(652, 14, 61, 1),
(653, 14, 62, 0.6),
(654, 14, 63, 0.8),
(655, 14, 64, 0.5),
(656, 14, 65, 0.2),
(657, 14, 66, 0.2),
(658, 14, 67, 0.2),
(659, 14, 68, 0.4),
(660, 14, 69, 0.6),
(661, 14, 70, 0.4),
(662, 14, 72, 0.4),
(663, 14, 73, 0.4),
(664, 14, 74, 0.4),
(665, 14, 75, 0.2),
(666, 14, 76, 0.4),
(667, 14, 77, 0.4),
(668, 14, 78, 0.3),
(669, 14, 79, 0.4),
(670, 14, 80, 0.8),
(671, 14, 81, 0.7),
(672, 14, 82, 0.3),
(673, 14, 83, 0.2),
(674, 14, 84, 0.3),
(675, 14, 85, 0.4),
(676, 14, 88, 0.6),
(677, 14, 89, 0.3),
(678, 14, 90, 0.4),
(679, 14, 91, 1),
(680, 14, 94, 0.8),
(681, 14, 95, 0.8),
(682, 14, 96, 0.2),
(683, 16, 83, 1),
(684, 16, 92, 0.2),
(685, 16, 96, 0.7),
(686, 15, 55, 0.4),
(687, 15, 56, 0.4),
(688, 15, 57, 0.4),
(689, 15, 59, 0.4),
(690, 15, 62, 0.4),
(691, 15, 65, 0.4),
(692, 15, 66, 0.4),
(693, 15, 67, 0.6),
(694, 15, 68, 0.4),
(695, 15, 69, 0.5),
(696, 15, 70, 0.4),
(697, 15, 72, 0.4),
(698, 15, 73, 0.6),
(699, 15, 74, 0.5),
(700, 15, 75, 0.4),
(701, 15, 78, 0.4),
(702, 15, 79, 0.4),
(703, 15, 81, 0.4),
(704, 15, 83, 0.4),
(705, 15, 84, 1),
(706, 15, 85, 1),
(707, 15, 87, 1),
(708, 15, 88, 0.2),
(709, 15, 89, 0.2),
(710, 15, 90, 0.4),
(711, 15, 93, 0.4),
(712, 15, 95, 0.4);

-- --------------------------------------------------------

--
-- Struktur dari tabel `gejala`
--

CREATE TABLE `gejala` (
  `id_gejala` int(11) NOT NULL,
  `kode_gejala` varchar(5) NOT NULL,
  `nama_gejala` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `gejala`
--

INSERT INTO `gejala` (`id_gejala`, `kode_gejala`, `nama_gejala`) VALUES
(54, 'G01', 'Tugas yang diberikan perusahaan berlebihan'),
(55, 'G02', 'Tanggung jawab yang diberikan perusahaan sangat memberatkan'),
(56, 'G03', 'Dikejar waktu dalam menyelesaikan pekerjaan'),
(57, 'G04', 'Tugas yang dilakukan tidak terjadwal dengan baik'),
(58, 'G05', 'Mengalami kesulitan memenuhi target perusahaan'),
(59, 'G06', 'Mendapat waktu istirahat yang kurang untuk menjalankan pekerjaan berlebihan'),
(60, 'G07', 'Tidak mampu menyelesaikan pekerjaan tepat waktu'),
(61, 'G08', 'Bekerja dengan peralatan yang tidak memadai'),
(62, 'G09', 'Lingkungan kerja yang banyak gangguan'),
(63, 'G10', 'Mengerjakan tugas yang berbeda-beda'),
(64, 'G11', 'Melakukan pekerjaan yang dirasakan tidak dimengerti/tidak cocok'),
(65, 'G12', 'Menerima tugas yang bertentangan satu sama lain'),
(66, 'G13', 'Tujuan yang ditetapkan perusahaan tidak sesuai dengan harapan'),
(67, 'G14', 'Ditekan dengan banyak peraturan dalam menjalankan tugas'),
(68, 'G15', 'Mengalami konflik dari tugas yang dibebankan atasan yang berlainan'),
(69, 'G16', 'Merasakan konflik dari tugas yang dibebankan atasan langsung saya'),
(70, 'G17', 'Menerima penugasan yang berbeda-beda dari dua atasan/lebih'),
(71, 'G18', 'Hubungan yang tidak harmonis dengan rekan kerja'),
(72, 'G19', 'Mengalami konflik dengan rekan kerja'),
(73, 'G20', 'Mengalami kesulitan berkomunikasi dengan atasan'),
(74, 'G21', 'Kurangnya dukungan dari atasan'),
(75, 'G22', 'Ada hubungan yang tidak baik antara atasan dan karyawan'),
(76, 'G23', 'Merasa kurang jelas dengan informasi dari perusahaan mengenai pekerjaan'),
(77, 'G24', 'Tidak tahu apa yang menjadi tanggung jawab pekerjaan yang saya jalankan'),
(78, 'G25', 'Merasa tidak jelas dalam hal ruang lingkup pekerjaan'),
(79, 'G26', 'Merasa sulit memperoleh informasi yang dibutuhkan untuk menjalankan pekerjaan'),
(80, 'G27', 'Merasa tidak tahu harus bertanggung jawab kepada siapa dalam bekerja'),
(81, 'G28', 'Prosedur/instruksi kerja kurang jelas'),
(82, 'G29', 'Alur komunikasi tidak jelas'),
(83, 'G30', 'Atasan terlalu banyak mengatur'),
(84, 'G31', 'Atasan bertindak kurang adil dalam pembagian pekerjaan kepada bawahan'),
(85, 'G32', 'Merasa tidak mengetahui bagaimana penilaian atasan terhadap hasil kerja saya'),
(86, 'G33', 'Merasa tidak mempunyai peranan dalam pengambilan keputusan'),
(87, 'G34', 'Merasa tidak ada kesempatan untuk berpartisipasi dalam mencapai tujuan perusahaan'),
(88, 'G35', 'Atasan tidak memberitahu dengan jelas perubahan-perubahan kebijaksanaan di perusahaan'),
(89, 'G36', 'Atasan tidak memberitahu tugas yang harus saya lakukan'),
(90, 'G37', 'Peluang yang kecil untuk mendapat promosi'),
(91, 'G38', 'Mendapat pekerjaan baru yang memerlukan keterampilan berbeda dari sebelumnya'),
(92, 'G39', 'Merasa tidak mempunyai kesempatan untuk lebih maju dalam bekerja'),
(93, 'G40', 'Mengalami promosi kerja ke jabatan yang lebih rendah dari kemampuan yang dimiliki'),
(94, 'G41', 'Mengalami promosi kerja ke jabatan yang lebih tinggi dari kemampuan yang dimiliki'),
(95, 'G42', 'Umpan balik terhadap hasil kerja tidak sesuai dengan harapan'),
(96, 'G43', 'Pemberhentian karyawan menjadi pemicu kecemasan saya untuk bekerja dengan baik');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengguna`
--

CREATE TABLE `pengguna` (
  `id_pengguna` int(11) NOT NULL,
  `nama_lengkap` varchar(30) NOT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL,
  `level` enum('Admin','User') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `pengguna`
--

INSERT INTO `pengguna` (`id_pengguna`, `nama_lengkap`, `username`, `password`, `level`) VALUES
(1, 'Administrator', 'admin', 'admin', 'Admin'),
(5, 'zaky', 'zaky', 'zaky', 'User'),
(10, 'naufal', 'naufal', 'naufal', 'User'),
(25, 'marfat', 'marfat', 'marfat', 'User'),
(26, 'rona', 'rona', 'rona', 'User'),
(27, 'ajid', 'ajid', 'ajid12345', 'User'),
(28, 'Muhammad Brimstone', 'brimstone', '123456789', 'User'),
(33, 'Baronah', 'rona97', 'Baronah97', 'User'),
(34, 'soeltan', 'soeltan', 'soeltan', 'User'),
(35, 'samsul', 'samsul', 'password', 'User');

-- --------------------------------------------------------

--
-- Struktur dari tabel `penyakit`
--

CREATE TABLE `penyakit` (
  `id_penyakit` int(11) NOT NULL,
  `kode_penyakit` varchar(5) NOT NULL,
  `nama_penyakit` varchar(50) NOT NULL,
  `deskripsi` text NOT NULL,
  `solusi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

--
-- Dumping data untuk tabel `penyakit`
--

INSERT INTO `penyakit` (`id_penyakit`, `kode_penyakit`, `nama_penyakit`, `deskripsi`, `solusi`) VALUES
(13, 'P01', 'Tidak Stres', 'Tidak stres bukan berarti tidak memiliki tantangan atau tanggung jawab dalam kehidupan, tetapi lebih mengacu pada kemampuan seseorang dalam mengelola dan menanggapi stres dengan cara yang sehat. Orang yang tidak stres mungkin memiliki mekanisme koping yang efektif, kemampuan mengatur waktu yang baik, dan kemampuan untuk menjaga keseimbangan antara pekerjaan dan kehidupan pribadi.', 'Alhamdulillah, pertahankan.'),
(14, 'P02', 'Stres Rendah', 'Stres rendah mengacu pada tingkat stres yang relatif rendah atau minimal dalam kehidupan seseorang. Ini berarti individu tersebut tidak mengalami tekanan yang signifikan atau beban yang berlebihan dari situasi atau tuntutan sehari-hari.', '• Mencari waktu jeda sejenak, entah bertemu teman kantor dan bertukar pikiran atau mengambil breaktime dengan pergi ke kantin.\n• Melakukan meditasi sebentar untuk menenangkan diri.\n• Mencuci muka sebentar karena air yang mengenai muka membuat segar dan itu berkaitan dengan relaksasi.\n• Melakukan olahraga ringan ditempat duduk.\n• Melakukan hobi, misalnya mendengarkan musik sebentar atau menggambar, lalu lanjutkan lagi pekerjaan.\n• Mencari tau sumber stres, lalu diskusikan dengan orang terdekat atau bertanya kepada diri sendiri/intropeksi diri.\n• Membuat prioritas untuk dikerjakan satu per satu.\n• Memberikan apresiasi kecil pada diri sendiri.\n• Makan makanan yang bergizi dan menghindari junk food.'),
(15, 'P03', 'Stres Sedang', 'Stres sedang mengacu pada tingkat stres yang berada di antara stres ringan dan stres tinggi. Ini menunjukkan bahwa seseorang mengalami tekanan yang moderat akibat tuntutan atau situasi dalam kehidupan mereka.', '• Memilah mana yang menjadi masalah utama, lalu membuat daftar prioritas.\n• Membuat jadwal rutinitas sehari-hari.\n• Melakukan meditasi untuk menenangkan diri.\n• Membuat rencana liburan sebagai hadiah untuk diri sendiri.\n• Bertukar pikiran dengan teman atau ahli jika diperlukan.\n• Berpikir positif bahwa setiap masalah pasti ada solusinya.'),
(16, 'P04', 'Stres Tinggi', 'Stres tinggi mengacu pada tingkat stres yang signifikan atau berat yang dialami seseorang. Stres tinggi terjadi ketika seseorang merasa tidak mampu mengatasi tuntutan atau tekanan yang dihadapi dalam kehidupan mereka.', '• Mengambil breaktime.\n• Membuat prioritas masalah dan menentukan mana yang akan diselesaikan terlebih dahulu.\n• Makan makanan yang bergizi dan menghindari junk food.\n• Lakukan meditasi/olahraga/yoga.\n• Meminta bantuan ke ahli.\n• Berpikir positif dan memiliki tekat, yang pasti bisa diselesaikan masalahnya bukan hanya berkutat dimasalah tersebut.');

-- --------------------------------------------------------

--
-- Struktur dari tabel `profil`
--

CREATE TABLE `profil` (
  `Id` int(11) NOT NULL,
  `name` text DEFAULT NULL,
  `picture` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Struktur dari tabel `riwayat`
--

CREATE TABLE `riwayat` (
  `id_riwayat` int(11) NOT NULL,
  `id_pengguna` int(11) DEFAULT NULL,
  `tanggal` date NOT NULL,
  `id_penyakit` int(11) DEFAULT NULL,
  `metode` enum('Forward Chaining','Certainty Factor') NOT NULL,
  `nilai` float DEFAULT NULL,
  `penyakit` text NOT NULL,
  `gejala` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `riwayat`
--

INSERT INTO `riwayat` (`id_riwayat`, `id_pengguna`, `tanggal`, `id_penyakit`, `metode`, `nilai`, `penyakit`, `gejala`) VALUES
(318, 5, '2023-07-31', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:99.97\n#Tidak Stres:97.15\n#Stres Tinggi:80.00\n', '#G01:1.0\n#G02:0.8\n#G03:0.8\n#G04:1.0\n#G05:0.6\n#G06:0.4\n#G07:0.6\n#G08:0.6\n#G09:1.0\n#G10:0.6\n#G11:0.6\n#G12:0.8\n#G13:0.6\n#G14:0.6\n#G15:0.8\n#G16:1.0\n#G17:1.0\n#G18:0.6\n#G19:0.6\n#G20:0.6\n#G21:1.0\n#G22:1.0\n#G23:0.6\n#G24:0.6\n#G25:1.0\n#G26:0.6\n#G27:0.8\n#G28:0.6\n#G29:0.8\n#G30:0.8\n'),
(319, 5, '2023-07-31', 16, 'Certainty Factor', 60, '#Stres Tinggi:60.00\n#Stres Sedang:24.00\n#Stres Rendah:12.00\n#Tidak Stres:0.00\n', '#G30:0.6\n'),
(320, 5, '2023-07-31', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:98.97\n#Tidak Stres:94.97\n#Stres Tinggi:77.73\n', '#G01:0.6\n#G02:0.8\n#G06:0.8\n#G08:1.0\n#G09:0.2\n#G10:1.0\n#G11:0.8\n#G13:0.4\n#G18:0.2\n#G19:0.2\n#G20:0.6\n#G21:0.8\n#G22:0.2\n#G23:0.2\n#G24:0.8\n#G25:1.0\n#G26:0.4\n#G27:0.2\n#G28:0.2\n#G29:0.4\n#G30:0.6\n#G31:0.2\n#G32:0.2\n#G33:0.4\n#G34:0.4\n#G35:0.2\n#G36:0.2\n#G37:0.2\n#G38:0.2\n#G39:0.2\n#G40:0.2\n#G41:0.2\n#G42:0.4\n#G43:0.6\n'),
(321, 5, '2023-07-31', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:100.00\n#Stres Tinggi:82.80\n#Tidak Stres:79.26\n', '#G01:0.8\n#G02:0.4\n#G03:0.8\n#G04:0.4\n#G05:0.8\n#G06:0.2\n#G07:0.2\n#G08:1.0\n#G09:0.2\n#G12:1.0\n#G13:0.4\n#G14:0.4\n#G15:0.8\n#G17:0.6\n#G21:0.2\n#G22:0.8\n#G23:0.6\n#G25:0.2\n#G26:0.6\n#G27:0.8\n#G30:0.8\n#G31:1.0\n#G32:0.4\n#G35:0.4\n#G36:0.8\n#G37:0.8\n#G38:0.2\n#G41:0.2\n#G42:0.8\n#G43:0.2\n'),
(322, 5, '2023-07-31', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Tidak Stres:0.00\n#Stres Sedang:0.00\n#Stres Tinggi:0.00\n', '#G01:1.0\n'),
(323, 5, '2023-07-31', 14, 'Certainty Factor', 99.99, '#Stres Rendah:99.99\n#Stres Sedang:99.95\n#Tidak Stres:79.29\n#Stres Tinggi:28.00\n', '#G02:0.4\n#G03:0.6\n#G04:0.8\n#G05:0.2\n#G07:0.6\n#G09:0.8\n#G11:0.8\n#G13:0.8\n#G15:0.8\n#G17:0.8\n#G18:0.4\n#G19:0.2\n#G20:1.0\n#G21:0.2\n#G23:0.8\n#G24:0.2\n#G26:0.6\n#G28:1.0\n#G29:0.4\n#G31:0.2\n#G32:0.4\n#G34:0.8\n#G35:0.2\n#G37:1.0\n#G40:0.8\n#G41:0.2\n#G43:0.4\n'),
(324, 5, '2023-07-31', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:99.37\n#Tidak Stres:90.69\n#Stres Tinggi:88.86\n', '#G01:1.0\n#G02:0.6\n#G03:0.4\n#G04:0.6\n#G05:0.8\n#G06:0.4\n#G07:0.8\n#G08:0.4\n#G09:0.2\n#G10:0.8\n#G11:0.4\n#G13:0.6\n#G14:0.4\n#G16:0.6\n#G17:0.8\n#G18:0.4\n#G20:0.6\n#G21:0.6\n#G22:0.4\n#G23:0.2\n#G25:0.6\n#G26:0.6\n#G27:0.2\n#G29:0.4\n#G30:0.8\n#G31:0.2\n#G33:0.8\n#G34:0.2\n#G36:0.8\n#G38:0.8\n#G39:0.2\n#G40:0.2\n#G42:0.4\n#G43:0.6\n'),
(325, 5, '2023-07-31', 14, 'Certainty Factor', 99.94, '#Stres Rendah:99.94\n#Stres Sedang:97.23\n#Tidak Stres:72.99\n#Stres Tinggi:31.20\n', '#G01:0.4\n#G02:0.6\n#G03:0.4\n#G04:0.2\n#G05:0.6\n#G06:0.4\n#G07:0.2\n#G08:0.6\n#G09:0.4\n#G10:0.2\n#G11:0.6\n#G12:0.8\n#G13:0.4\n#G14:0.2\n#G16:0.4\n#G17:0.6\n#G18:0.2\n#G21:0.2\n#G22:0.4\n#G24:0.2\n#G26:0.2\n#G27:0.2\n#G30:0.2\n#G32:0.4\n#G33:0.2\n#G36:0.2\n#G37:0.4\n#G40:0.4\n#G42:0.4\n#G43:0.2\n'),
(326, 5, '2023-08-01', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:81.13\n#Tidak Stres:28.90\n#Stres Tinggi:28.00\n', '#G01:0.8\n#G03:0.8\n#G04:0.8\n#G05:1.0\n#G06:0.8\n#G07:0.4\n#G08:0.2\n#G23:0.8\n#G32:0.4\n#G33:0.4\n#G43:0.4\n'),
(327, 5, '2023-08-01', 14, 'Certainty Factor', 99.46, '#Stres Rendah:99.46\n#Stres Sedang:84.82\n#Tidak Stres:40.00\n#Stres Tinggi:0.00\n', '#G01:0.6\n#G02:0.4\n#G03:0.2\n#G04:0.8\n#G05:0.8\n#G06:0.6\n#G14:0.4\n#G21:1.0\n'),
(328, 33, '2023-08-02', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:100.00\n#Tidak Stres:97.37\n#Stres Tinggi:73.50\n', '#G01:0.4\n#G02:0.6\n#G03:0.4\n#G04:0.6\n#G05:0.6\n#G06:0.6\n#G07:0.4\n#G08:0.2\n#G09:0.6\n#G10:0.6\n#G11:0.2\n#G12:0.6\n#G13:0.4\n#G14:0.6\n#G15:0.6\n#G16:0.6\n#G17:0.6\n#G18:0.8\n#G19:0.8\n#G20:0.6\n#G21:0.6\n#G22:0.8\n#G23:0.6\n#G24:0.6\n#G25:0.6\n#G26:0.6\n#G27:0.6\n#G28:0.4\n#G29:0.4\n#G30:0.6\n#G31:1.0\n#G32:0.8\n#G33:0.6\n#G34:0.6\n#G35:0.4\n#G36:0.4\n#G37:0.4\n#G38:0.2\n#G39:0.4\n#G42:0.8\n#G43:0.4\n'),
(329, 34, '2023-08-04', 13, 'Certainty Factor', 92.8, '#Tidak Stres:92.80\n#Stres Rendah:64.00\n#Stres Sedang:50.00\n#Stres Tinggi:0.00\n', '#G18:1.0\n#G21:1.0\n#G24:1.0\n'),
(330, 34, '2023-08-04', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:40.00\n#Tidak Stres:0.00\n#Stres Tinggi:0.00\n', '#G01:1.0\n#G03:1.0\n'),
(331, 34, '2023-08-04', 15, 'Certainty Factor', 100, '#Stres Sedang:100.00\n#Stres Rendah:30.00\n#Tidak Stres:0.00\n#Stres Tinggi:0.00\n', '#G31:1.0\n'),
(332, 34, '2023-08-04', 16, 'Certainty Factor', 100, '#Stres Tinggi:100.00\n#Stres Rendah:84.00\n#Stres Sedang:78.40\n#Tidak Stres:30.00\n', '#G30:1.0\n#G40:1.0\n#G42:1.0\n'),
(333, 34, '2023-08-04', 16, 'Certainty Factor', 70, '#Stres Tinggi:70.00\n#Stres Rendah:44.00\n#Tidak Stres:40.00\n#Stres Sedang:20.00\n', '#G36:1.0\n#G43:1.0\n'),
(335, 34, '2023-08-04', 15, 'Certainty Factor', 100, '#Stres Sedang:100.00\n#Stres Rendah:60.00\n#Tidak Stres:0.00\n#Stres Tinggi:0.00\n', '#G34:1.0\n#G35:1.0\n'),
(336, 34, '2023-08-04', 13, 'Certainty Factor', 82, '#Tidak Stres:82.00\n#Stres Sedang:76.00\n#Stres Rendah:52.00\n#Stres Tinggi:0.00\n', '#G14:1.0\n#G19:1.0\n#G33:1.0\n'),
(337, 34, '2023-08-04', 14, 'Certainty Factor', 91.6, '#Stres Rendah:91.60\n#Tidak Stres:78.40\n#Stres Sedang:40.00\n#Stres Tinggi:0.00\n', '#G24:1.0\n#G25:1.0\n#G27:1.0\n'),
(339, 34, '2023-08-04', 13, 'Certainty Factor', 64, '#Tidak Stres:64.00\n#Stres Rendah:58.00\n#Stres Sedang:40.00\n#Stres Tinggi:0.00\n', '#G24:1.0\n#G25:1.0\n'),
(341, 34, '2023-08-04', 15, 'Certainty Factor', 100, '#Stres Sedang:100.00\n#Stres Rendah:71.20\n#Tidak Stres:40.00\n#Stres Tinggi:0.00\n', '#G21:1.0\n#G22:1.0\n#G32:1.0\n'),
(343, 34, '2023-08-05', 14, 'Certainty Factor', 84, '#Stres Rendah:84.00\n#Stres Tinggi:76.00\n#Tidak Stres:0.00\n#Stres Sedang:0.00\n', '#G01:0.8\n#G39:1.0\n#G43:1.0\n'),
(344, 34, '2023-08-05', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Tinggi:76.00\n#Stres Sedang:40.00\n#Tidak Stres:0.00\n', '#G03:1.0\n#G39:1.0\n#G43:1.0\n'),
(345, 34, '2023-08-05', 16, 'Certainty Factor', 76, '#Stres Tinggi:76.00\n#Stres Rendah:20.00\n#Tidak Stres:0.00\n#Stres Sedang:0.00\n', '#G39:1.0\n#G43:1.0\n'),
(346, 34, '2023-08-05', 13, 'Certainty Factor', 80, '#Tidak Stres:80.00\n#Stres Rendah:0.00\n#Stres Sedang:0.00\n#Stres Tinggi:0.00\n', '#G18:1.0\n'),
(347, 34, '2023-08-05', 14, 'Certainty Factor', 95.8, '#Stres Rendah:95.80\n#Tidak Stres:40.00\n#Stres Sedang:40.00\n#Stres Tinggi:0.00\n', '#G27:1.0\n#G28:1.0\n#G29:1.0\n'),
(348, 34, '2023-08-05', 15, 'Certainty Factor', 80, '#Stres Sedang:80.00\n#Stres Rendah:68.00\n#Tidak Stres:0.00\n#Stres Tinggi:0.00\n', '#G14:1.0\n#G16:1.0\n'),
(349, 34, '2023-08-05', 16, 'Certainty Factor', 100, '#Stres Tinggi:100.00\n#Stres Rendah:90.40\n#Stres Sedang:78.40\n#Tidak Stres:0.00\n', '#G06:1.0\n#G09:1.0\n#G30:1.0\n'),
(350, 34, '2023-08-05', 13, 'Certainty Factor', 93.52, '#Tidak Stres:93.52\n#Stres Sedang:89.40\n#Stres Rendah:87.45\n#Stres Tinggi:20.00\n', '#G19:1.0\n#G21:1.0\n#G24:1.0\n#G25:1.0\n#G30:0.2\n#G31:0.2\n#G32:0.2\n'),
(351, 34, '2023-08-05', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:64.00\n#Tidak Stres:40.00\n#Stres Tinggi:0.00\n', '#G04:1.0\n#G05:1.0\n#G08:1.0\n#G09:1.0\n'),
(352, 34, '2023-08-05', 15, 'Certainty Factor', 92, '#Stres Sedang:92.00\n#Stres Rendah:71.20\n#Tidak Stres:40.00\n#Stres Tinggi:0.00\n', '#G14:1.0\n#G20:1.0\n#G21:1.0\n'),
(353, 34, '2023-08-05', 14, 'Certainty Factor', 100, '#Stres Rendah:100.00\n#Stres Sedang:40.00\n#Tidak Stres:20.00\n#Stres Tinggi:0.00\n', '#G02:1.0\n#G07:1.0\n'),
(354, 34, '2023-08-05', 14, 'Certainty Factor', 98.8, '#Stres Rendah:98.80\n#Stres Sedang:64.00\n#Tidak Stres:0.00\n#Stres Tinggi:0.00\n', '#G10:1.0\n#G28:1.0\n#G42:1.0\n'),
(355, 5, '2023-08-05', 13, 'Certainty Factor', 0, '#Tidak Stres:0.00\n#Stres Rendah:0.00\n#Stres Sedang:0.00\n#Stres Tinggi:0.00\n', '');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `aturan`
--
ALTER TABLE `aturan`
  ADD PRIMARY KEY (`id_aturan`) USING BTREE,
  ADD KEY `id_penyakit` (`id_penyakit`),
  ADD KEY `id_gejala` (`id_gejala`);

--
-- Indeks untuk tabel `gejala`
--
ALTER TABLE `gejala`
  ADD PRIMARY KEY (`id_gejala`);

--
-- Indeks untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  ADD PRIMARY KEY (`id_pengguna`) USING BTREE;

--
-- Indeks untuk tabel `penyakit`
--
ALTER TABLE `penyakit`
  ADD PRIMARY KEY (`id_penyakit`);

--
-- Indeks untuk tabel `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`Id`);

--
-- Indeks untuk tabel `riwayat`
--
ALTER TABLE `riwayat`
  ADD PRIMARY KEY (`id_riwayat`),
  ADD KEY `id_penyakit` (`id_penyakit`),
  ADD KEY `id_pengguna` (`id_pengguna`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `aturan`
--
ALTER TABLE `aturan`
  MODIFY `id_aturan` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=719;

--
-- AUTO_INCREMENT untuk tabel `gejala`
--
ALTER TABLE `gejala`
  MODIFY `id_gejala` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=102;

--
-- AUTO_INCREMENT untuk tabel `pengguna`
--
ALTER TABLE `pengguna`
  MODIFY `id_pengguna` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT untuk tabel `penyakit`
--
ALTER TABLE `penyakit`
  MODIFY `id_penyakit` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT untuk tabel `profil`
--
ALTER TABLE `profil`
  MODIFY `Id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT untuk tabel `riwayat`
--
ALTER TABLE `riwayat`
  MODIFY `id_riwayat` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=356;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `aturan`
--
ALTER TABLE `aturan`
  ADD CONSTRAINT `aturan_ibfk_1` FOREIGN KEY (`id_penyakit`) REFERENCES `penyakit` (`id_penyakit`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `aturan_ibfk_2` FOREIGN KEY (`id_gejala`) REFERENCES `gejala` (`id_gejala`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Ketidakleluasaan untuk tabel `riwayat`
--
ALTER TABLE `riwayat`
  ADD CONSTRAINT `riwayat_ibfk_1` FOREIGN KEY (`id_penyakit`) REFERENCES `penyakit` (`id_penyakit`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `riwayat_ibfk_2` FOREIGN KEY (`id_pengguna`) REFERENCES `pengguna` (`id_pengguna`) ON DELETE CASCADE ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
