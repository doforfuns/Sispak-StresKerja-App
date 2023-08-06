<?php
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);
$response = array();

if (isset($input['id_penyakit'])) {
    $id_penyakit = $input['id_penyakit'];

    $q_penyakit = "SELECT id_penyakit, kode_penyakit, nama_penyakit, deskripsi, solusi FROM penyakit WHERE id_penyakit='$id_penyakit'";
    $result_penyakit = mysqli_query($con, $q_penyakit);

    if (mysqli_num_rows($result_penyakit) > 0) {
        $penyakit = mysqli_fetch_assoc($result_penyakit);

        $q_gejala = "SELECT id_aturan, g.kode_gejala, g.nama_gejala, a.nilai_cf 
                     FROM aturan AS a
                     INNER JOIN gejala AS g ON a.id_gejala = g.id_gejala
                     WHERE a.id_penyakit='$id_penyakit'";

        $result_gejala = mysqli_query($con, $q_gejala);

        $gejala = array();
        $nilaiCf = array(); // New array to store nilai_cf values
        $number = 1; // Initialize the number counter
        while ($row_gejala = mysqli_fetch_assoc($result_gejala)) {
            $gejala[] = $number . ". " . $row_gejala['nama_gejala']; // Add the number to the nama_gejala
            $nilaiCf[] = $row_gejala['nilai_cf']; // Store nilai_cf values in the array
            $id_aturan[] = $row_gejala['id_aturan'];
            $number++; // Increment the number counter for the next iteration
        }

        $response["status"] = 0;
        $response["message"] = "Get aturan berhasil";
        $response["id_penyakit"] = $penyakit['id_penyakit'];
        $response["nama_penyakit"] =  $penyakit['nama_penyakit'];
        $response["deskripsi"] = $penyakit['deskripsi'];
        $response["solusi"] = $penyakit['solusi'];
        $response["daftar_gejala"] = $gejala;
        $response["nilai_cf"] = $nilaiCf; // Include nilai_cf values in the response
        $response["id_aturan"] = $id_aturan;
    } else {
        $response["status"] = 1;
        $response["message"] = "Tidak ada penyakit dengan ID tersebut.";
    }
} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}

header('Content-Type: application/json');
echo json_encode($response, JSON_PRETTY_PRINT);
?>
