# Aplikasi Sistem Reservasi Hotel (Java Swing)

## Deskripsi Umum
Aplikasi Sistem Reservasi Hotel merupakan aplikasi desktop berbasis Java Swing yang digunakan oleh pengguna (user) untuk melakukan proses reservasi kamar hotel secara terkomputerisasi. Aplikasi ini membantu pengguna dalam melakukan pencatatan data reservasi secara lebih terstruktur, efisien, dan mudah digunakan.

Aplikasi dikembangkan menggunakan konsep **Object Oriented Programming (OOP)**, menerapkan operasi **CRUD (Create, Read, Update, Delete)** pada data reservasi, serta menggunakan penyimpanan data berbasis file sebagai media penyimpanan data.

------
## Teknologi yang Digunakan
- Bahasa Pemrograman : Java
- GUI               : Java Swing
- Build Tool        : Maven
- Penyimpanan Data  : File berbasis teks (TXT)
- Java API          : `LocalDate`

---

## Struktur Project
```* UAP_PemrogramanLanjut-reservasi-hotel
* ├── .idea
* ├── data
* ├── src
* │   └── main
* │       └── java
* │           └── com.hotel
* │               ├── auth
* │               │   └── LoginScreen.java
* │               ├── database
* │               │   └── AuthManager.java
* │               ├── gui
* │               │   ├── ManageReservationsScreen.java
* │               │   ├── ProfileScreen.java
* │               │   ├── ReservationScreen.java
* │               │   └── UserDashboardScreen.java
* │               ├── model
* │               │   └── User.java
* │               └── Main.java
* ├── target
* ├── .gitignore
* ├── pom.xml
* └── README.md```


---

---

## Cara Menjalankan Program

### 1. Persiapan Lingkungan
Pastikan perangkat telah terpasang:
- Java Development Kit (JDK) minimal versi 8
- IDE Java (IntelliJ IDEA / NetBeans / Eclipse)
- Maven (terintegrasi otomatis di IntelliJ)

---

### 2. Menjalankan Program melalui IDE
1. Buka IDE Java.
2. Import project sebagai **Maven Project**.
3. Pastikan file `pom.xml` terdeteksi dengan benar.
4. Buka file:
   src/main/java/com/hotel/Main.java

yaml
Copy code
5. Jalankan program dengan klik **Run**.
6. Aplikasi akan menampilkan **Halaman Login User**.

----

## Akun Login User
Data akun user disimpan pada file:
data/users.txt

yaml
Copy code

Contoh akun:
- Username : user
- Password : user123

---

## Fitur Aplikasi

### 1. Login User
- User melakukan login menggunakan username dan password.
- Sistem memvalidasi data login sebelum mengakses aplikasi.

---

### 2. Dashboard User
Setelah login berhasil, user akan masuk ke halaman dashboard yang menyediakan menu:
- Reservasi Kamar
- Lihat Data Reservasi
- Logout

---

### 3. Reservasi Kamar
- User mengisi form reservasi kamar.
- Data yang diinput meliputi:
    - Tipe kamar
    - Tanggal menginap
    - Durasi menginap
- Sistem melakukan validasi input.
- Data reservasi disimpan ke dalam file penyimpanan.

---

### 4. Lihat Data Reservasi
- User dapat melihat data reservasi yang telah dibuat.
- Data ditampilkan dalam bentuk tabel.
- Sistem membaca data langsung dari file penyimpanan.

---

### 5. Logout
- User dapat keluar dari aplikasi melalui menu logout.
- Sistem akan kembali ke halaman login.

---

## Penyimpanan Data
- Data user disimpan pada:
  data/users.txt

diff
Copy code
- Data reservasi disimpan pada:
  data/reservations.txt

yaml
Copy code

Semua data disimpan dalam format file teks dan akan dimuat kembali saat aplikasi dijalankan.

---

## Konsep yang Diterapkan
- Object Oriented Programming (OOP)
- CRUD (Create, Read, Update, Delete)
- Java Swing GUI
- File Handling
- Exception Handling
- Java API (`LocalDate`)

---

## Catatan
- Aplikasi ini hanya memiliki **1 jenis pengguna (User)**.
- Tidak terdapat fitur admin atau manajemen sistem.
- Penyimpanan data menggunakan file, bukan database.
- Password masih disimpan dalam bentuk teks karena tujuan pembelajaran.

---

##  Tim Pengembang
Nama  : Moch Rafly Ilhamsyah & Muh Rifqi Palettei

Program Studi : Informatika