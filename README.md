# HỆ THỐNG QUẢN LÝ HỒ SƠ ĐIỆN TỬ V2.0

Dự án nâng cấp hệ thống tiếp nhận và xử lý hồ sơ điện tử, áp dụng các Mẫu thiết kế phần mềm (Design Patterns) tuân thủ nguyên tắc SOLID.

## 1. Link Video Thuyết trình & Demo

- **Video Demo (HD):** `[DÁN_LINK_YOUTUBE_HOẶC_GOOGLE_DRIVE_CÔNG_KHAI_VÀO_ĐÂY]`

## 2. Cấu trúc thư mục (Packages)

Dự án được tái cấu trúc thành 6 module tính năng độc lập nhằm đảm bảo nguyên tắc Single Responsibility (SRP):

- `model/`: Chứa lớp thực thể và `DocumentBuilder` hỗ trợ lưu nháp (Yêu cầu 1).
- `extraction/`: Áp dụng Strategy, Adapter và Factory Pattern để trích xuất nội dung đa định dạng (.TXT, .PDF, .JPG) (Yêu cầu 2).
- `validation/`: Áp dụng Chain of Responsibility Pattern luân chuyển hồ sơ qua 3 trạm kiểm duyệt độc lập (Yêu cầu 3).
- `notification/`: Áp dụng Observer Pattern giúp cá nhân hóa cấu hình nhận thông báo qua Email, SMS, App Push (Yêu cầu 4).
- `storage/`: Áp dụng Repository/Strategy Pattern tách biệt thao tác lưu trữ Local JSON và MySQL (Yêu cầu 5).
- `service/` & `ui/`: Tầng điều phối trung tâm (`DocumentProcessor`) và Giao diện người dùng Java Swing (`MainSwingUI`).


## 3. Hướng dẫn biên dịch và khởi chạy

Yêu cầu hệ thống: Đã cài đặt Java Development Kit (JDK 11 trở lên). Nhóm đã cung cấp sẵn các script chạy tự động. Script sẽ tự động biên dịch dự án vào thư mục tạm, khởi chạy giao diện và dọn dẹp sạch sẽ các file `.class` sau khi phần mềm đóng lại.

**Đối với người dùng Windows:**

Chỉ cần nhấp đúp chuột vào tệp `windows_run.bat` nằm ở thư mục gốc của dự án.

**Đối với người dùng Linux / macOS:**

Mở Terminal tại thư mục gốc của dự án, cấp quyền thực thi và chạy script:
```bash
chmod +x linux_run.sh
./run.sh
