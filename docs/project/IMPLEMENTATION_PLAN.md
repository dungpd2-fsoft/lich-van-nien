# Quy trình thực hiện app Lịch vạn niên Android

Cập nhật: 2026-11-15.

## Ngữ cảnh và trạng thái

- Đã tạo develop cục bộ từ origin/main tại 195b1e1. Push origin develop chưa thành công vì Git thiếu đăng nhập HTTPS (`could not read Username`); hiện develop vẫn đang track origin/main từ lúc tạo. Khi đăng nhập được, chạy push -u origin develop và xác minh tracking. Main chưa thay đổi.

- Cập nhật mới nhất: người dùng chấp nhận thiết kế và cho chuyển bước 5, yêu cầu đề xuất commit trước code; không cần quay lại xin duyệt Figma. Đã khảo sát repo mẫu `/home/dungpd2/Project/Android/nowinandroid`, tạo [kiến trúc đề xuất](../ARCHITECTURE.md) và [16 commit triển khai](./COMMIT_PLAN.md). Chưa tạo code Android hoặc commit Git. Các mô tả lịch sử chưa hoàn tất Figma bên dưới ghi nhận giới hạn công cụ, không phải điều kiện chặn code hiện tại.

- Người dùng xác nhận đã hoàn tất giai đoạn thống nhất spec.
- Căn cứ yêu cầu: [SPEC.md](./SPEC.md). File spec hiện vẫn mang nhãn bản nháp 0.2 và có câu hỏi mở; cần đối chiếu trạng thái này khi cập nhật tài liệu, không tự coi các đề xuất chưa xác nhận là yêu cầu đã duyệt.
- Nền tảng: Android, Kotlin, Jetpack Compose.
- Nguồn dữ liệu: https://lich247.com/api và https://lich247.com/openapi.json.
- Thiết kế: Material 3, bố cục hài hòa, thích ứng kích thước cửa sổ điện thoại/tablet, có animation chọn lọc khi chuyển màn và nhấn chọn.
- File Figma: https://www.figma.com/design/DBfrO6T6QXhbRho7yUz9uJ/Untitled.
- Đã kiểm tra đọc được file Figma; tại thời điểm kiểm tra có Page 1 trống. Chưa kiểm chứng quyền ghi bằng thao tác thiết kế.
- Chưa thiết kế chi tiết và chưa code app. Đã hoàn thành khảo sát API và 12 ca kiểm tra mẫu; kết quả tại [API_VALIDATION.md](./API_VALIDATION.md). Quyền sử dụng, quota và độ chính xác đầy đủ vẫn cần xác minh. Bước tiếp theo: dựng UX/wireframe; chưa chốt tích hợp production.
- Phạm vi làm việc tạm theo đề xuất gần nhất: tra cứu lịch và cache ngày đã tải; chưa thêm widget, nhắc ngày giỗ, sự kiện cá nhân hoặc offline đầy đủ. Đây là giả định đã trình bày, cần xác nhận nếu phát sinh công việc phụ thuộc.

## Các bước thực hiện

| Bước | Công việc | Kết quả và điều kiện hoàn thành | Trạng thái |
|---|---|---|---|
| 1. Kiểm chứng API | Thử dữ liệu thực tế, trường thiếu, tháng nhuận, lỗi đầu vào, giới hạn truy cập và phạm vi ngày hỗ trợ; xác minh quyền sử dụng, múi giờ; xác định chiến lược tải lịch tháng và cache. | Tài liệu tích hợp ghi rõ dữ liệu thực có, giới hạn, bằng chứng kiểm tra và điểm còn chưa xác minh. | Hoàn thành báo cáo và 12 ca mẫu; xem API_VALIDATION.md. Còn xác nhận bên ngoài về quyền sử dụng/quota, dải ngày và độ chính xác đầy đủ trước tích hợp production. |
| 2. Thiết kế UX | Xác định màn hình, luồng chọn ngày, chuyển tháng, xem chi tiết, trở về hôm nay và xử lý mất mạng; vẽ wireframe cho cửa sổ hẹp/rộng. | Luồng sử dụng và bố cục được người dùng duyệt. | Đã sửa UX v3 thành hai phần: màn chính/cấp 2 (6:2), luồng tương tác/dialog/toast (8:91); 10 flow, 2 dialog; bổ sung 6 sơ đồ, 31 mũi tên chuyển bước và show dialog/toast. Chờ duyệt; xem UX_DESIGN.md. |
| 3. Thiết kế UI trong Figma | Thiết lập màu Material 3, chữ, khoảng cách, component; dựng giao diện sáng/tối, trạng thái tải/lỗi và bố cục điện thoại/tablet. | Bộ giao diện trong file Figma đã chỉ định; nội dung dễ đọc, căn chỉnh nhất quán, không chồng lấn. | Đang thực hiện; Figma Starter hết lượt MCP, chưa ghi UI vào file. Đã chuẩn bị 21 SVG v1; thêm UI v2 minh họa lịch ngày/tháng, bộ 12 con giáp và 3 tranh chủ đề theo góp ý. Xem UI_DESIGN.md và design/ui-v2/index.html. Chưa hoàn tất bước 3. |
| 4. Mẫu tương tác và duyệt thiết kế | Thể hiện chuyển màn, chọn ngày, chuyển tháng; kiểm tra bố cục khi đổi kích thước; nhận góp ý và chỉnh sửa. | Người dùng duyệt thiết kế và hành vi trước khi code. Ảnh tĩnh không thay thế kiểm tra chuyển động; ghi rõ hiệu ứng cần xác minh tiếp trên app. | Chưa bắt đầu. |
| 5. Chuẩn bị kiến trúc | Tổ chức Kotlin + Compose, lớp API, cache, trạng thái, điều hướng; chốt Android tối thiểu và ma trận thiết bị kiểm thử. | Cấu trúc dự án và kế hoạch triển khai rõ ràng, phù hợp spec và thiết kế đã duyệt. | Đã khảo sát NIA local, lập ARCHITECTURE.md và COMMIT_PLAN.md theo yêu cầu đề xuất trước code. Toolchain/minSdk cần khóa khi bootstrap. |
| 6. Lập trình | Triển khai dữ liệu → lịch tháng → chi tiết ngày → tra cứu nhanh → giao diện thích ứng → animation và thiết lập. Kiểm thử logic quan trọng trong quá trình code. | App chạy được, đáp ứng spec và thiết kế; các kiểm tra phù hợp đạt yêu cầu. | Chưa bắt đầu. |
| 7. Review code | Đối chiếu spec/Figma; kiểm tra tính đúng đắn, trạng thái, hiệu năng, kiến trúc và khả năng bảo trì; sửa và kiểm chứng lỗi phát hiện. | Không còn lỗi chặn chuyển sang kiểm thử tổng thể; có kết quả review và xử lý lỗi. | Chưa bắt đầu. |
| 8. Testing tổng thể | Kiểm tra âm/dương, chuyển tháng/năm, tháng nhuận, offline/lỗi API, xoay máy, chia màn hình, resize, tăng cỡ chữ, trợ năng và thao tác liên tục. | Có báo cáo kiểm thử; sửa và kiểm thử lại lỗi; đạt tiêu chí nghiệm thu, không còn lỗi chặn phát hành. | Chưa bắt đầu. |
| 9. Release | Tạo bản ký, phát hành thử nội bộ; chuẩn bị icon, ảnh giới thiệu, nội dung cửa hàng, thông tin riêng tư; phát hành chính thức sau khi người dùng duyệt. | Bản phát hành được duyệt, có kế hoạch theo dõi lỗi và cập nhật. | Chưa bắt đầu. |

## Nguyên tắc thực hiện và bàn giao ngữ cảnh

- Quy trình mới nhất: develop tách từ main; mỗi commit triển khai trên branch riêng từ develop. Agent tự review theo spec, sửa/kiểm chứng rồi tự merge vào develop khi đạt; không bắt buộc viết unit test, vẫn chạy kiểm tra phù hợp. PR triển khai nhắm develop. Main chỉ do người dùng verify/test và merge thủ công; agent không push/merge main. Chi tiết tại COMMIT_PLAN.md.

- Thực hiện theo thứ tự; nếu phát hiện vấn đề, quay lại bước liên quan, cập nhật tài liệu và kiểm chứng lại phần bị ảnh hưởng.
- Chỉ bắt đầu code sau khi người dùng duyệt thiết kế và xác nhận triển khai.
- Bố cục phải thích ứng theo không gian cửa sổ khả dụng; giữ ngày/tháng đang chọn khi xoay hoặc resize.
- Animation phải nhanh, không chặn thao tác hoặc làm sai trạng thái; tôn trọng thiết lập giảm/tắt chuyển động của hệ thống.
- Không hứa dữ liệu hoặc tính năng API chưa được kiểm chứng. Phân biệt lỗi công cụ/mạng kiểm tra với lỗi dịch vụ.
- Testing diễn ra cả trong lúc code; bước 8 là kiểm thử tổng thể trước phát hành.
- Khi tiếp tục công việc, đọc file này cùng SPEC.md; cập nhật trạng thái, kết quả, quyết định đã xác nhận và việc còn lại sau mỗi mốc đáng kể.
- Các thay đổi phạm vi phải cập nhật SPEC.md và kế hoạch tương ứng; giữ nguyên mã yêu cầu hiện có khi chỉ chỉnh nội dung.

## Việc cần làm tiếp

1. Theo dõi các điểm chưa xác minh trong API_VALIDATION.md; cần sự cho phép sử dụng dữ liệu và thông tin quota từ nhà cung cấp trước khi chốt tích hợp production.
2. Đối chiếu các lựa chọn còn mở trong spec trước công việc phụ thuộc; không tự mở rộng phạm vi.
3. Trình bày kế hoạch 16 commit và kiến trúc bước 5. Tiếp theo triển khai từ bootstrap theo kế hoạch; người dùng đã chấp nhận thiết kế, không yêu cầu duyệt lại vì Figma MCP.
