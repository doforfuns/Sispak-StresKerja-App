<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['username']) && isset($input['id_pengguna'])) {

    $id_pengguna = $input['id_pengguna'];
    $nama_lengkap = $input['nama_lengkap'];
    $username = $input['username'];
    $password = $input['password'];

    $check_username = mysqli_query($con, "SELECT * FROM pengguna WHERE username='" . $username . "'");
    $check_id_pengguna = mysqli_query($con, "SELECT * FROM pengguna WHERE id_pengguna='" . $id_pengguna . "'");

    if (mysqli_num_rows($check_username) > 0) {
        $response["status"] = 1;
        $response["message"] = "Username sudah digunakan";
    } elseif (mysqli_num_rows($check_id_pengguna) > 0) {
        $response["status"] = 2;
        $response["message"] = "NIK sudah digunakan";
    } else {
        $q = "INSERT INTO pengguna(id_pengguna,nama_lengkap,username,password,level) VALUES ('$id_pengguna','$nama_lengkap','$username','$password','User')";
        mysqli_query($con, $q);

        $response["status"] = 0;
        $response["message"] = "Pendaftaran berhasil";
    }
} else {
    $response["status"] = 3;
    $response["message"] = "Parameter ada yang kosong";
}
echo json_encode($response);
