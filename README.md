# HỆ THỐNG QUẢN LÝ HỒ SƠ ĐIỆN TỬ V2.0

Dự án nâng cấp hệ thống tiếp nhận và xử lý hồ sơ điện tử, áp dụng các Mẫu thiết kế phần mềm (Design Patterns) tuân thủ nguyên tắc SOLID.

## 1. Link Video Thuyết trình & Demo

- **Video Demo:** [Link](https://drive.google.com/drive/folders/1r65M0E7cwKyf06kHSRUH3ffj22CencL5?fbclid=IwY2xjawVIAbFleHRuA2FlbQIxMQBwZG9mBHNydGMGYXBwX2lkATAAAR7DJdAdopBU4bJIDCrFWTp4JwmdtqIOrENKrvPQyJLZFcKDi16S1hbhA3Vgkw_aem_ljg7aOZaLZ-wnCAbRzBw1A)

## 2. Cấu trúc thư mục

Dự án được tái cấu trúc thành 6 module tính năng độc lập nhằm đảm bảo nguyên tắc Single Responsibility:

- `model/`: Chứa lớp thực thể và `DocumentBuilder` hỗ trợ lưu nháp (Yêu cầu 1).
- `extraction/`: Áp dụng Strategy, Adapter và Factory Pattern để trích xuất nội dung đa định dạng (.TXT, .PDF, .JPG) (Yêu cầu 2).
- `validation/`: Áp dụng Chain of Responsibility Pattern luân chuyển hồ sơ qua 3 trạm kiểm duyệt độc lập (Yêu cầu 3).
- `notification/`: Áp dụng Observer Pattern giúp cá nhân hóa cấu hình nhận thông báo qua Email, SMS, App Push (Yêu cầu 4).
- `storage/`: Áp dụng Repository/Strategy Pattern tách biệt thao tác lưu trữ Local JSON và MySQL (Yêu cầu 5).
- `service/` & `ui/`: Tầng điều phối trung tâm (`DocumentProcessor`) và Giao diện người dùng Java Swing (`MainSwingUI`).

## 3. Hướng dẫn biên dịch và khởi chạy

Yêu cầu hệ thống: Đã cài đặt Java Development Kit (JDK 11 trở lên).

**Bước 1:** Mở Terminal (hoặc Command Prompt / PowerShell) và điều hướng vào thư mục `src` của dự án.

```bash
cd duong_dan_toi_thu_muc/src

```

**Bước 2:** Biên dịch toàn bộ mã nguồn của các package bằng lệnh `javac`:

```bash
javac model/*.java extraction/*.java validation/*.java notification/*.java storage/*.java service/*.java ui/*.java

```

**Bước 3:** Khởi chạy hệ thống qua file Main của gói UI:

```bash
java ui.MainSwingUI

```
