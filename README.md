
## Demo Video
[Click here to watch demo video](https://www.youtube.com/watch?v=X1Aq5usLhq0)

# 🏘️ Hệ thống Quản lý Dân cư và Nhà văn hóa (Community Management System)

Ứng dụng Desktop quản lý toàn diện thông tin nhân khẩu, hộ khẩu, cơ sở vật chất và các sự kiện cộng đồng dành cho tổ dân phố và ban quản lý khu dân cư. Dự án được thiết kế chuẩn theo mô hình kiến trúc MVC phối hợp cùng DAO Pattern để tối ưu hóa hiệu năng và đảm bảo tính mở rộng.

## 📖 Giới thiệu dự án (About The Project)

Hệ thống được xây dựng nhằm số hóa quy trình quản lý thông tin cư dân và điều phối các hoạt động tại nhà văn hóa. Thay vì ghi chép và lưu trữ sổ sách thủ công dễ thất lạc, phần mềm cung cấp giải pháp lưu trữ tập trung dữ liệu biến động nhân khẩu, tự động hóa quy trình mượn trả cơ sở vật chất và chuẩn hóa luồng phê duyệt sự kiện cộng đồng.

## ✨ Các tính năng cốt lõi (Key Features)

Hệ thống cung cấp giao diện trực quan và phân quyền xử lý chặt chẽ giữa ban quản lý (**Admin**) và cư dân (**Customer**):

### 👥 Phân hệ Quản lý Cư dân & Hộ khẩu
* **Biến động nhân khẩu:** Đăng ký cư dân mới, chỉnh sửa thông tin cá nhân, cập nhật trạng thái tạm trú, tạm vắng hoặc khai báo biến động nhân khẩu.
* **Nghiệp vụ hộ khẩu:** Quản lý sổ hộ khẩu, thực hiện tách hộ, chuyển hộ khẩu và lưu vết toàn bộ lịch sử thay đổi nhân khẩu (`CitizenChange` / `HouseholdChange`).
* **Hỗ trợ cộng đồng:** Phân loại và quản lý thông tin các trường hợp đặc biệt như cư dân vô gia cư, không nơi nương tựa.

### 🎉 Phân hệ Điều phối Sự kiện & Hoạt động
* **Đăng ký sự kiện:** Cư dân gửi yêu cầu tổ chức sự kiện tại nhà văn hóa kèm theo thời gian, địa điểm và mục đích cụ thể.
* **Luồng phê duyệt chuyên sâu:** Ban quản lý tiếp nhận, đánh giá tính khả thi và thực hiện phê duyệt hoặc từ chối thông qua giao diện điều hướng tác vụ (`EventApprovalController`).
* **Lịch sử hoạt động:** Lưu trữ thông tin và thống kê các sự kiện đã diễn ra phục vụ công tác kiểm toán và báo cáo định kỳ.

### 📦 Phân hệ Quản lý Cơ sở vật chất (Assets)
* **Kiểm kê tài sản:** Theo dõi số lượng, tình trạng sử dụng và vị trí của các trang thiết bị bên trong nhà văn hóa.
* **Mượn/Trả tài sản:** Số hóa quy trình mượn đồ, tự động kiểm tra tính khả dụng của thiết bị theo thời gian thực và ghi nhận trạng thái hoàn trả (`RentAsset` / `ReturnAsset`).

### 📊 Phân hệ Thống kê & Bảo mật
* **Báo cáo trực quan:** Thống kê số liệu về mật độ dân cư, tần suất tổ chức sự kiện và hiệu suất sử dụng trang thiết bị cơ sở vật chất.
* **Xác thực và phân quyền:** Hệ thống đăng nhập, đăng ký tài khoản mới bảo mật và tính năng cấp phát quyền hạn (`Assign Account`) nghiêm ngặt.

## 💻 Công nghệ sử dụng (Built With)

* **Ngôn ngữ:** Java 11 / Java 17
* **Giao diện:** JavaFX, FXML, CSS
* **Quản lý dự án & Build Tool:** Maven
* **Cơ sở dữ liệu:** Microsoft SQL Server
* **Kiến trúc:** Model-View-Controller (MVC) kết hợp với Data Access Object (DAO) Pattern để tách biệt hoàn toàn tầng xử lý giao diện, logic nghiệp vụ và tầng lưu trữ dữ liệu.

## 🚀 Hướng dẫn cài đặt & Triển khai (Getting Started)

### Yêu cầu hệ thống (Prerequisites)
* Java Development Kit (JDK) phiên bản 11 hoặc mới hơn.
* IDE phát triển: IntelliJ IDEA (khuyên dùng) hoặc Eclipse.
* Microsoft SQL Server đi kèm công cụ quản lý SQL Server Management Studio (SSMS).

### Các bước triển khai chi tiết

#### 1. Tải mã nguồn về máy cục bộ
    git clone [https://github.com/your-username/project_ktpm.git](https://github.com/your-username/project_ktpm.git)
    cd project_ktpm

#### 2. Khởi tạo và Import Cơ sở dữ liệu
* Khởi động công cụ **SQL Server Management Studio (SSMS)** và kết nối vào Database Server của bạn.
* Nhấp chuột phải vào mục **Databases**, chọn **Import Data-tier Application...**
* Nhấp **Next**, sau đó chọn **Browse...** và tìm đến file `ktpm.bacpac` nằm ngay trong thư mục gốc của source code dự án.
* Tiến hành nhấn **Next** và **Finish** để hệ thống tự động khôi phục toàn bộ cấu trúc bảng và dữ liệu mẫu của dự án.

#### 3. Cấu hình kết nối Cơ sở dữ liệu
* Mở thư mục dự án bằng **IntelliJ IDEA**.
* Tìm và mở file cấu hình kết nối tại đường dẫn: `src/main/java/com/example/demo4/Database.java`.
* Tiến hành chỉnh sửa các tham số trong chuỗi kết nối (`URL`, `Username`, `Password`) sao cho trùng khớp với tài khoản cấu hình SQL Server trên máy cá nhân của bạn.
    // Ví dụ cấu hình kết nối trong Database.java
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=ktpm;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

#### 4. Cài đặt phụ thuộc và Khởi chạy ứng dụng
* Chờ đợi **Maven** tự động nhận diện file `pom.xml` và tải xuống toàn bộ các phụ thuộc cần thiết (JavaFX, SQL JDBC Driver...).
* Định vị file khởi chạy chính của ứng dụng tại đường dẫn: `src/main/java/com/example/demo4/Main.java`.
* Nhấp chuột phải vào file `Main.java` và chọn **Run 'Main.main()'** để khởi động giao diện phần mềm.

