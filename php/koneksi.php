<?php
$server = isset($_SERVER['PHP_ENV']) ? $_SERVER['PHP_ENV'] : 'development';

if ($server == 'development') {
    $db_host = 'localhost';
    $db_user = 'id21047387_sultan';
    $db_password = 'Sultan123.';
    $db_name = 'id21047387_streskerja';
} elseif ($server == 'production') {
    $db_host = 'localhost';
    $db_user = 'id21047387_sultan';
    $db_password = 'Sultan123.';
    $db_name = 'id21047387_streskerja';
}

$con = @mysqli_connect($db_host, $db_user, $db_password) or die('<center><strong>Gagal koneksi ke server database</strong></center>');
mysqli_select_db($con, $db_name) or die('<center><strong>Database tidak ditemukan</strong></center>');
