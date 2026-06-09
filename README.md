
## Demo Video
[Click here to watch demo video](https://www.youtube.com/watch?v=X1Aq5usLhq0)

# 🏘️ Hệ thống Quản lý Dân cư và Nhà văn hóa (Community Management System)

> Ứng dụng Desktop quản lý toàn diện thông tin nhân khẩu, hộ khẩu, cơ sở vật chất và các sự kiện cộng đồng dành cho tổ dân phố / ban quản lý khu dân cư.

## 📖 Giới thiệu (About The Project)

Dự án được xây dựng nhằm mục đích số hóa quy trình quản lý thông tin cư dân và các hoạt động tại nhà văn hóa. Thay vì quản lý trên sổ sách thủ công, phần mềm cung cấp một giao diện trực quan để theo dõi biến động nhân khẩu, phê duyệt sự kiện, và quản lý mượn/trả cơ sở vật chất một cách chính xác, minh bạch.

## ✨ Các tính năng chính (Key Features)

Phần mềm được chia thành các phân hệ chính với cơ chế phân quyền rõ ràng giữa **Admin** (Cán bộ quản lý) và **Customer** (Người dân):

* 👥 **Quản lý Nhân khẩu & Hộ khẩu:**
    * Thêm mới, chỉnh sửa thông tin cư dân, đăng ký tạm trú/tạm vắng.
    * Quản lý hộ khẩu: Tách hộ, chuyển hộ, ghi nhận lịch sử thay đổi (Citizen Change / Household Change).
    * Quản lý nhóm người vô gia cư / không nơi nương tựa.
* 🎉 **Quản lý Sự kiện (Events):**
    * Người dân có thể đăng ký tổ chức sự kiện.
    * Ban quản lý duyệt/từ chối sự kiện (Event Approval).
    * Lưu trữ và thống kê các sự kiện đã diễn ra (Past Events).
* 📦 **Quản lý Cơ sở vật chất (Assets):**
    * Kiểm kê tài sản hiện có tại nhà văn hóa.
    * Quy trình mượn/trả đồ đạc (Rent/Return Asset).
* 📊 **Thống kê & Báo cáo (Statistics):**
    * Tổng hợp số liệu nhân khẩu, thiết bị và các hoạt động định kỳ.
* 🔐 **Bảo mật & Phân quyền:**
    * Đăng nhập, đăng ký, cấp phát tài khoản (Assign Account) theo vai trò.

## 💻 Công nghệ sử dụng (Built With)

* **Ngôn ngữ:** Java 11/17+
* **Giao diện:** JavaFX, FXML, CSS
* **Quản lý dự án & Build:** Maven
* **Cơ sở dữ liệu:** SQL Server (cung cấp sẵn file `.bacpac`)
* **Architecture Pattern:** MVC (Model-View-Controller) kết hợp Data Access Object (DAO) pattern.

## 🚀 Hướng dẫn cài đặt (Getting Started)

Làm theo các bước sau để chạy dự án trên máy cá nhân của bạn:

### Yêu cầu hệ thống (Prerequisites)
* [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) hoặc mới hơn.
* IDE: IntelliJ IDEA hoặc Eclipse (khuyên dùng IntelliJ).
* Microsoft SQL Server & SQL Server Management Studio (SSMS).

### Cài đặt (Installation)
1. **Clone repository:**
   ```sh
   git clone [https://github.com/your-username/project_ktpm.git](https://github.com/your-username/project_ktpm.git)
