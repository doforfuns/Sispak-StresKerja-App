<?php
$response = array();
include 'koneksi.php';

$inputJSON = file_get_contents('php://input');
$input = json_decode($inputJSON, TRUE);

if (isset($input['hasil']) && isset($input['id_pengguna'])) {

    $hasil = $input['hasil'];
    $id_pengguna = $input['id_pengguna'];
    $metode = $input['metode'];
    $tanggal = date('Y-m-d');

    $cf_user = explode("#", $hasil);
    $arr_hasil = array();
    
    $i = 0;
    $sql = mysqli_query($con, "SELECT id_gejala FROM gejala ORDER BY kode_gejala");
    while ($rgejala = mysqli_fetch_array($sql)) {
        $id_gejala = $rgejala['id_gejala'];
        $arr_hasil[] = array(
            "id_gejala" => $id_gejala,
            "cf_user" => $cf_user[$i],
        );
        $i++;
    }
    
    $hasil = array();
    $x = 0;
    
    $sqlpenyakit = mysqli_query($con, "SELECT id_penyakit, nama_penyakit FROM penyakit ORDER BY id_penyakit");
    while ($rpenyakit = mysqli_fetch_array($sqlpenyakit)) {
        $id_penyakit = $rpenyakit['id_penyakit'];
        $cf_gabungan = 0;
        $i = 0;
        $sql = mysqli_query($con, "SELECT id_gejala FROM aturan WHERE id_penyakit='$id_penyakit'");
        while ($rgejala = mysqli_fetch_array($sql)) {
            $id_gejala = $rgejala['id_gejala'];
            $r_gejala = mysqli_fetch_array(mysqli_query($con, "SELECT nilai_cf FROM aturan WHERE id_gejala='$id_gejala' AND id_penyakit='$id_penyakit'"));
            $cf_gejala = $r_gejala['nilai_cf'];
            $cf_condition = ''; // Variabel untuk menyimpan kondisi
            
            foreach ($arr_hasil as $row) {
                if ($id_gejala == $row['id_gejala']) {
                    $cf = $row['cf_user'] * $cf_gejala;
            
                    if ($cf < 0) {
                        $cf_condition = 'minus';
                    } elseif ($cf > 0) {
                        $cf_condition = 'plus';
                    }
            
                    if ($cf_condition == 'plus') {
                        $cf_gabungan = $cf_gabungan + ($cf * (1 - $cf_gabungan));
                    } elseif ($cf_condition == 'minus') {
                        $cf_gabungan = $cf_gabungan + ($cf * (1 + $cf_gabungan));
                    } elseif ($cf_condition == 'plus_minus') {
                        $cf_gabungan = ($cf_gabungan + $cf) / (1 - min(abs($cf_gabungan), abs($cf)));
                    }
                    $i++;
                }
            }

        }
        $persentase = $cf_gabungan * 100;
        $hasil[$x]["id_penyakit"] = $id_penyakit;
        $hasil[$x]["nama_penyakit"] = $rpenyakit['nama_penyakit']; // Add the disease name to the result
        $hasil[$x]["nilai"] = number_format($persentase, 2);
        $x++;
    }

    array_sort_by_column($hasil, 'nilai');

    $hasil_penyakit_id = $hasil[0]["id_penyakit"];
    $nama_penyakit_terbesar = $hasil[0]["nama_penyakit"];
    $hasil_nilai_terbesar = $hasil[0]["nilai"];

    $response["hasil_terbesar"] = [
        "id_penyakit" => $hasil_penyakit_id,
        "nama_penyakit" => $nama_penyakit_terbesar,
        "nilai" => $hasil_nilai_terbesar
    ];

    $response["status"] = 0;
    $response["hasil_diagnosis"] = $hasil; // Seluruh daftar hasil diagnosis

    $arr_all_penyakit = array();
    foreach ($hasil as $hasil_penyakit) {
        $id_penyakit = $hasil_penyakit["id_penyakit"];
        $nama_penyakit = $hasil_penyakit["nama_penyakit"];
        $nilai = $hasil_penyakit["nilai"];
        $arr_all_penyakit[] = "#$nama_penyakit:$nilai\n";
    }
    $penyakit_values = implode($arr_all_penyakit);
        
    // Simpan data gejala yang dipilih dan nilai CF pengguna ke dalam variabel
    $data_gejala = '';
    foreach ($arr_hasil as $gejala) {
        $id_gejala = $gejala['id_gejala'];
        $cf_user = $gejala['cf_user'];
        // Ubah query untuk mengambil kode_gejala dari tabel gejala berdasarkan id_gejala
        $r_gejala = mysqli_fetch_array(mysqli_query($con, "SELECT kode_gejala FROM gejala WHERE id_gejala='$id_gejala'"));
        $kode_gejala = $r_gejala['kode_gejala'];
    
        // Tambahkan kondisi untuk memeriksa apakah nilai CF dari pengguna tidak sama dengan 0
        if ($cf_user != 0) {
            $data_gejala .= "#{$kode_gejala}:{$cf_user}\n"; // Contoh format: "kode_gejala:cf_user #"
        }
    }
    $data_gejala = rtrim($data_gejala, "#"); // Hilangkan karakter '#' di akhir string



   // Simpan data riwayat ke dalam database
    $q = "INSERT INTO riwayat (id_pengguna, id_penyakit, tanggal, metode, nilai, penyakit, gejala)
        VALUES ('$id_pengguna', '$hasil_penyakit_id', '$tanggal', '$metode', '$hasil_nilai_terbesar', '$penyakit_values', '$data_gejala')";
    mysqli_query($con, $q);
    
    $response["status"] = 0;
    $response["id_penyakit"] = $hasil_penyakit_id;
    $response["nama_penyakit"] = $nama_penyakit_terbesar;
    $response["nilai"] = $hasil_nilai_terbesar;
    $response["gejala_terpilih"] = $arr_hasil; // Tambahkan data gejala terpilih ke dalam response

} else {
    $response["status"] = 2;
    $response["message"] = "Parameter ada yang kosong";
}

// fungsi untuk mengurutkan nilai berdasarkan nilai terbesar
function array_sort_by_column(&$arr, $col, $dir = SORT_DESC)
{
    $sort_col = array();
    foreach ($arr as $key => $row) {
        $sort_col[$key] = $row[$col];
    }
    array_multisort($sort_col, $dir, $arr);
}

echo json_encode($response);
