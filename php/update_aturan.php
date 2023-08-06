<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['id_penyakit']) && isset($input['daftar_gejala']) && isset($input['nilai_cf'])) {

    $id_penyakit = $input['id_penyakit'];
    $daftar_gejala = $input['daftar_gejala'];
    $nilai_cf = $input['nilai_cf'];

    // Delete existing rules for the given penyakit
    mysqli_query($con, "DELETE FROM aturan WHERE id_penyakit='" . $id_penyakit . "'");

    // Insert new rules for the given penyakit, gejala, and nilai_cf
    $hsl = explode("#", $daftar_gejala);
    $nilai_cf_arr = explode("#", $nilai_cf);
    foreach ($hsl as $key => $val) {
        // Split the $val into kode_gejala and nama_gejala
        $valParts = explode(" - ", $val);
        $kode_gejala = $valParts[0];
        $nama_gejala = $valParts[1];
        
        $q2 = mysqli_query($con, "SELECT id_gejala FROM gejala WHERE kode_gejala='$kode_gejala' AND nama_gejala='$nama_gejala'");
        if (mysqli_num_rows($q2) > 0) {
            $r2 = mysqli_fetch_array($q2);
            $id_gejala = $r2['id_gejala'];
            $cf_value = floatval($nilai_cf_arr[$key]);
            $q3 = "INSERT INTO aturan (id_penyakit, id_gejala, nilai_cf) VALUES ('$id_penyakit', '$id_gejala', '$cf_value')";
            mysqli_query($con, $q3);
        }
    }

    $response["status"] = 0;
    $response["message"] = "Data berhasil disimpan";
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}

echo json_encode($response);
