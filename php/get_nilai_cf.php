<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['id_aturan'])) {

    $id_aturan = $input['id_aturan'];

    $query = "SELECT penyakit.kode_penyakit, penyakit.nama_penyakit, gejala.kode_gejala, gejala.nama_gejala, aturan.nilai_cf 
              FROM aturan 
              JOIN penyakit ON aturan.id_penyakit = penyakit.id_penyakit 
              JOIN gejala ON aturan.id_gejala = gejala.id_gejala 
              WHERE aturan.id_aturan = '$id_aturan'";

    $result = mysqli_query($con, $query);

    if ($result) {
        $row = mysqli_fetch_assoc($result);
        if ($row) {
            $response["status"] = 0;
            $response["message"] = "Get data berhasil";
            $response["nama_penyakit"] = $row['kode_penyakit'] . ' - ' .  $row['nama_penyakit'];
            $response["nama_gejala"] = $row['kode_gejala'] . ' - ' .   $row['nama_gejala'];
            $response["nilai_cf"] = $row['nilai_cf'];
        } else {
            $response["status"] = 1;
            $response["message"] = "Data tidak ditemukan";
        }
    } else {
        $response["status"] = 1;
        $response["message"] = "Error dalam permintaan SQL";
    }

} else {
    $response["status"] = 2;
    $response["message"] = "Parameter id_aturan ada yang kosong";
}

echo json_encode($response);
?>
