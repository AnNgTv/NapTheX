# NapTheX - Plugin Nạp Thẻ Cho Minecraft Server

NapTheX là một plugin Minecraft mạnh mẽ hỗ trợ tích hợp hệ thống nạp thẻ từ [thesieutoc.net](https://thesieutoc.net). Plugin hỗ trợ nhiều loại thẻ cào, ngân hàng (VietQR) và hệ thống phần thưởng linh hoạt.

## ✨ Tính năng chính

- 💳 **Hỗ trợ đa dạng thẻ:** Viettel, Vinaphone, Mobifone, Zing, Garena, v.v.
- 🏦 **Nạp Ngân Hàng/QR:** Tự động tạo mã QR VietQR giúp người chơi thanh toán nhanh chóng.
- 📊 **Cơ sở dữ liệu:** Hỗ trợ MySQL (HikariCP) và Flatfile (YAML).
- 🏆 **Hệ thống mốc nạp:** Tự động trao thưởng khi người chơi đạt các mốc tích lũy.
- 📬 **Phần thưởng Offline:** Đảm bảo người chơi nhận được quà ngay cả khi không online lúc thẻ được duyệt.
- 🔗 **Tích hợp PlaceholderAPI:** Cung cấp đầy đủ placeholder cho top nạp thẻ và tổng nạp.
- 🛠 **Giao diện đa dạng:** Hỗ trợ cả nạp qua Chat và Anvil GUI (Giao diện cái đe).

## 🚀 Cài đặt

1. Tải plugin về và bỏ vào thư mục `plugins` của server.
2. Khởi động lại server để tạo file cấu hình.
3. Mở `config.yml` và nhập `API Key` & `API Secret` từ [thesieutoc.net](https://thesieutoc.net).
4. Cấu hình các lệnh phần thưởng trong `config.yml` và mốc nạp trong `milestone.yml`.
5. Sử dụng `/ntx reload` để cập nhật cấu hình.

## 💻 Lệnh & Quyền hạn

| Lệnh | Mô tả | Quyền |
| :--- | :--- | :--- |
| `/napthe` | Mở menu nạp thẻ | Không cần |
| `/ntx reload` | Tải lại cấu hình | `napthex.admin` |
| `/ntx top` | Cập nhật bảng xếp hạng | `napthex.admin` |
| `/ntx version` | Kiểm tra phiên bản | `napthex.admin` |

## 📊 Placeholders

- `%ntx_total_alltime%`: Tổng số tiền đã nạp toàn bộ thời gian.
- `%ntx_total_daily%`: Tổng số tiền đã nạp trong ngày.
- `%ntx_top_name_1%`: Tên người chơi đứng đầu bảng xếp hạng.
- `%ntx_top_value_1%`: Số tiền của người chơi đứng đầu bảng xếp hạng.

## 🛠 Yêu cầu

- Java 21 trở lên.
- Minecraft Server 1.21.x (Hỗ trợ cả Folia).
- (Tùy chọn) [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/).

## 📞 Hỗ trợ

Nếu có bất kỳ lỗi nào, vui lòng liên hệ hỗ trợ tại [thesieutoc.net](https://thesieutoc.net).
