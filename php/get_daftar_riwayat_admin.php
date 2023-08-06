<?php
include 'koneksi.php';

$q = mysqli_query($con, "SELECT r.id_penyakit, DATE_FORMAT(r.tanggal, '%d-%m-%Y') as tanggal, p.nama_lengkap as metode, r.nilai, pk.nama_penyakit, pk.deskripsi, pk.solusi 
                        FROM riwayat r
                        LEFT JOIN penyakit pk ON r.id_penyakit = pk.id_penyakit
                        LEFT JOIN pengguna p ON r.id_pengguna = p.id_pengguna
                        ORDER BY r.id_riwayat DESC");

$response = array();
if (mysqli_num_rows($q) == 0) {
    $response["status"] = 1;
    $response["message"] = "Tidak ada riwayat diagnosa";
} else {
    $response["riwayat"] = array();
    while ($r = mysqli_fetch_array($q)) {
        $riwayat = array();
        if (is_null($r['id_penyakit'])) {
            $riwayat["nama_penyakit"] = 'Penyakit tidak terdiagnosa';
        } else {
            $riwayat["nama_penyakit"] = $r['nama_penyakit'];
            $riwayat["deskripsi"] = $r['deskripsi'];
            $riwayat["solusi"] = $r['solusi'];
        }
        $riwayat["metode"] = $r['metode'];
        $riwayat["nilai"] = $r['nilai'];
        $riwayat["tanggal"] = $r['tanggal'];
        $riwayat["id_penyakit"] = is_null($r['id_penyakit']) ? '' : $r['id_penyakit'];
        array_push($response["riwayat"], $riwayat);
    }
    $response["status"] = 0;
    $response["message"] = "Get list riwayat berhasil";
}

echo json_encode($response);

?>
