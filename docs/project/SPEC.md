# Spec — Lịch vạn niên Android

Trạng thái: Spec triển khai v0.3. Người dùng đã chấp nhận phạm vi/thiết kế và cho triển khai; app được phát triển trên develop. Các giới hạn API và phát hành vẫn cần xác minh.

## Mục tiêu

App Android Kotlin + Jetpack Compose tra cứu lịch âm/dương với dữ liệu LICH247; thiết kế Material 3, thích ứng điện thoại, máy tính bảng và cửa sổ thay đổi kích thước.

## Phạm vi

Đã xác nhận: Android, Kotlin, Jetpack Compose, nguồn dữ liệu LICH247, giao diện thích ứng, chốt spec trước thiết kế và code. File Figma: https://www.figma.com/design/DBfrO6T6QXhbRho7yUz9uJ/Untitled.

Đã xác nhận thêm: bố cục hài hòa; có animation ở một số thao tác chuyển màn và nhấn chọn (REQ-08, REQ-09). Các tính năng và hiệu ứng là căn cứ triển khai sau khi người dùng cho chuyển bước5; kiểm chứng trên app theo QA_REPORT.md.

## Yêu cầu và tiêu chí nghiệm thu

| Mã | Yêu cầu | Tiêu chí nghiệm thu đề xuất |
|---|---|---|
| REQ-01 | Lịch tháng âm/dương | Chuyển tháng/năm; mỗi ô có ngày dương và ngày âm khi có dữ liệu; phân biệt hôm nay, ngày chọn và ngoài tháng; dữ liệu thiếu có trạng thái rõ ràng. |
| REQ-02 | Chi tiết ngày | Chọn ngày hiển thị âm/dương, thứ, tháng âm nhuận, can chi, tiết khí, trực, phân loại ngày và giờ hoàng đạo theo dữ liệu API; thiếu trường không hiển thị thành kết luận phủ định. |
| REQ-03 | Chọn nhanh ngày | Chọn ngày/tháng/năm dương lịch, báo ngày không hợp lệ; nút Hôm nay quay về ngày hiện tại và đồng bộ lịch với chi tiết. |
| REQ-04 | Giao diện thích ứng | Cửa sổ hẹp dùng một cột; rộng dùng lịch và chi tiết hai vùng; xoay hoặc resize giữ ngày/tháng đang chọn, không chồng lấn hoặc cắt nội dung. Phân loại theo chiều rộng cửa sổ khả dụng, không theo tên thiết bị. |
| REQ-05 | Material 3 và khả năng tiếp cận | Sáng/tối/theo hệ thống; chữ tăng kích thước vẫn đọc và cuộn được; không dùng riêng màu để truyền đạt trạng thái; có nhãn trợ năng cho thao tác chính. |
| REQ-06 | Cache và xử lý lỗi | Ngày đã tải có thể xem lại khi mất mạng; ngày chưa có dữ liệu báo rõ và có Thử lại; đánh dấu dữ liệu lưu; không cam kết offline toàn bộ. |
| REQ-07 | Nguồn dữ liệu | Có thông tin nguồn LICH247 và phần giới thiệu; nội dung tốt/xấu hiển thị theo nguồn, không tự suy diễn. |
| REQ-08 | Bố cục hài hòa (đã xác nhận) | Đề xuất nghiệm thu: phân cấp rõ lịch, ngày chọn và thông tin phụ; ngày dương nổi bật hơn ngày âm nhưng cả hai dễ đọc; căn lề, khoảng cách, kiểu chữ nhất quán; giới hạn chiều rộng nội dung trên màn rộng; không kéo giãn ô lịch tùy tiện, không chồng lấn hoặc cắt chữ khi resize/tăng cỡ chữ. Duyệt mẫu điện thoại và tablet trong Figma trước code. |
| REQ-09 | Animation có chọn lọc (đã xác nhận) | Có phản hồi khi nhấn chọn và chuyển màn. Đề xuất: phản hồi nhấn Material, chuyển vùng chọn ngày, trượt khi đổi tháng, chuyển tiếp lịch sang chi tiết; dùng cùng quy tắc chuyển động trên toàn app. Không chặn thao tác, không làm mất trạng thái khi nhấn liên tục, không nhấp nháy hoặc tự lặp trang trí; tôn trọng thiết lập giảm/tắt animation của hệ thống. Thời lượng và hiệu ứng cụ thể duyệt ở bước UX/UI bằng mẫu tương tác. |
| REQ-10 | Minh họa con giáp và chủ đề ngày (đã yêu cầu bổ sung) | UI có bộ 12 con giáp Việt Nam (Mão là mèo), dùng con giáp theo địa chi ngày/tháng/năm. Màn ngày có tranh chủ đề và số ngày nổi bật theo ảnh tham khảo người dùng. Ảnh vận khí phải gắn với cờ phân loại API, không tự tạo kết luận; dữ liệu thiếu/mâu thuẫn dùng tranh trung tính. Phương án bộ chủ đề tái sử dụng, chưa phải ảnh độc nhất từng ngày, đã được chấp nhận để triển khai UI v2. |

## Ràng buộc

- Nguồn: https://lich247.com/api và https://lich247.com/openapi.json, đọc ngày 2026-11-15.
- OpenAPI mô tả GET /api/v1/calendar?date=YYYY-MM-DD; dữ liệu solar, lunar (leap, jd), can_chi, day_of_week, tiet_khi, truc, isHoangDao, isBlackDay, isDaiCat, gioHoangDao; lỗi 400 cho ngày không hợp lệ. Schema không đánh dấu các trường phản hồi là bắt buộc.
- Đã kiểm chứng 12 ca mẫu qua curl ngày 2026-11-15: API hoạt động; sai định dạng hoặc thiếu date có thể trả hôm nay với HTTP 200. Phải kiểm tra solar khớp ngày yêu cầu. Kết quả và bằng chứng: [API_VALIDATION.md](./API_VALIDATION.md).
- Chưa thấy endpoint tháng, đổi âm sang dương, danh sách lễ hoặc việc nên/không nên làm trong OpenAPI. Không coi các mục này là dữ liệu chắc chắn có.
- Lịch tháng cần chiến lược tải nhiều ngày/cache, xác minh quota trước khi triển khai; không mặc định gửi đồng thời hàng chục request.
- Phải xác minh ngày hỗ trợ, múi giờ, ý nghĩa leap, tính đúng đắn, quyền tái sử dụng dữ liệu, giới hạn truy cập, auth và chi phí trước khi chốt giải pháp tích hợp/phát hành.
- Trang giới thiệu nguồn nêu UTC+7; mẫu nhuận trả leap=1, thường=0. Trang chính sách mục 4 yêu cầu cho phép bằng văn bản khi sử dụng dữ liệu; quyền dùng cho app và quota chưa được xác nhận. Không coi API công khai là giấy phép sử dụng không giới hạn.
- Quy trình: chốt spec → UX/UI và duyệt Figma → code → review → testing tổng thể → release. Kiểm thử logic quan trọng diễn ra trong lúc code. Chỉ code khi người dùng xác nhận.

## Ngoài phạm vi bản đầu — đề xuất

Đăng nhập, đồng bộ đám mây, ngày giỗ/sự kiện cá nhân, thông báo, widget, quảng cáo, nội dung phong thủy mở rộng, đổi trực tiếp âm sang dương và lịch lễ chưa có nguồn xác nhận.

## Quyết định triển khai và việc còn mở

- Bản đầu tra cứu lịch tháng/ngày, cache các ngày đã tải; chưa có widget, nhắc lịch hoặc full offline.
- Kotlin/Compose, minSdk26, lịch Việt Nam Asia/Ho_Chi_Minh, màu Material3 cố định sáng/tối/theo hệ thống; không đổi màu theo hình nền.
- Code xác thực đầu vào năm1..9999, không coi đó là dải năm API được bảo đảm. Chưa có xác nhận dải năm, quota, chi phí hoặc quyền dùng dữ liệu cho phát hành.
- ApplicationId com.lichvannien hiện dùng phát triển; package/signing/privacy phát hành cần chốt trước release.
- Main do người dùng kiểm thử và merge thủ công; không tự phát hành.
