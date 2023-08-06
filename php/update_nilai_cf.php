<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['id_aturan']) && isset($input['nilai_cf'])) {

    $id_aturan = $input['id_aturan'];
    $nilai_cf = $input['nilai_cf'];

    // Sanitize the input to prevent SQL injection (optional but recommended)
    $id_aturan = mysqli_real_escape_string($con, $id_aturan);
    $nilai_cf = mysqli_real_escape_string($con, $nilai_cf);

    // Prepare the statement
    $stmt = $con->prepare("UPDATE aturan SET nilai_cf=? WHERE id_aturan=?");

    if ($stmt) {
        // Bind the parameters
        $stmt->bind_param("di", $nilai_cf, $id_aturan);

        // Execute the update
        if ($stmt->execute()) {
            $response["status"] = 0;
            $response["message"] = "Nilai CF berhasil diubah";
        } else {
            $response["status"] = 1;
            $response["message"] = "Gagal mengubah nilai CF";
        }

        // Close the statement
        $stmt->close();
    } else {
        $response["status"] = 1;
        $response["message"] = "Gagal menyiapkan pernyataan (statement)";
    }

} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}

echo json_encode($response);
?>
