# Quy trình thực hiện app Lịch vạn niên Android

Cập nhật: 2026-11-15.

## Ngữ cảnh và trạng thái

- Người dùng đã chấp nhận spec, UX/UI và cho chuyển triển khai. Không cần duyệt lại Figma để code.
- Android Kotlin/Jetpack Compose, Material3 sáng/tối, tranh chủ đề và12 con giáp; local UI nguồn tại design/ui-v2. Figma UX vẫn ở file DBfrO6T6QXhbRho7yUz9uJ; phần UI Figma bị quota, không ngăn triển khai đã được duyệt.
- Đã tạo/push develop từ main195b1e1. Phát triển mỗi phần trên branch riêng, review/build trước merge --no-ff vào develop. Main không thay đổi.
- GitHub token phiên làm việc cho push nhưng API tạo PR trả403; chưa tạo được PR. Giữ branch/commit/review trên remote và tích hợp bằng Git theo ủy quyền. Không chứa credential trong repo/remote URL.
- Đã khảo sát mẫu nowinandroid local, triển khai kiến trúc api/impl, domain/data/UI, Hilt, Room, DataStore, Retrofit và Navigation3. Toolchain/giới hạn theo README và ARCHITECTURE.md.
- Build demo/prod và lint PASS; đã kiểm tra các luồng chính trên emulator, xem QA_REPORT.md. Không viết unit test theo chỉ dẫn.
- Người dùng sẽ kiểm thử app một lượt rồi tự merge main. Chưa release. Quyền sử dụng/quota/dải ngày API vẫn theo API_VALIDATION.md.

## Các bước thực hiện

| Bước | Công việc | Kết quả và điều kiện hoàn thành | Trạng thái |
|---|---|---|---|
| 1. Kiểm chứng API | Thử dữ liệu thực tế, trường thiếu, tháng nhuận, lỗi đầu vào, giới hạn truy cập và phạm vi ngày hỗ trợ; xác minh quyền sử dụng, múi giờ; xác định chiến lược tải lịch tháng và cache. | Tài liệu tích hợp ghi rõ dữ liệu thực có, giới hạn, bằng chứng kiểm tra và điểm còn chưa xác minh. | Hoàn thành báo cáo và 12 ca mẫu; xem API_VALIDATION.md. Còn xác nhận bên ngoài về quyền sử dụng/quota, dải ngày và độ chính xác đầy đủ trước tích hợp production. |
| 2. Thiết kế UX | Xác định màn hình, luồng chọn ngày, chuyển tháng, xem chi tiết, trở về hôm nay và xử lý mất mạng; vẽ wireframe cho cửa sổ hẹp/rộng. | Luồng sử dụng và bố cục được người dùng duyệt. | Đã sửa UX v3 thành hai phần: màn chính/cấp 2 (6:2), luồng tương tác/dialog/toast (8:91); 10 flow, 2 dialog; bổ sung 6 sơ đồ, 31 mũi tên chuyển bước và show dialog/toast. Người dùng đã chấp nhận chuyển triển khai; xem UX_DESIGN.md. |
| 3. Thiết kế UI trong Figma | Thiết lập màu Material 3, chữ, khoảng cách, component; dựng giao diện sáng/tối, trạng thái tải/lỗi và bố cục điện thoại/tablet. | Bộ giao diện trong file Figma đã chỉ định; nội dung dễ đọc, căn chỉnh nhất quán, không chồng lấn. | Đang thực hiện; Figma Starter hết lượt MCP, chưa ghi UI vào file. Đã chuẩn bị 21 SVG v1; thêm UI v2 minh họa lịch ngày/tháng, bộ 12 con giáp và 3 tranh chủ đề theo góp ý. Xem UI_DESIGN.md và design/ui-v2/index.html. Bản local đã được người dùng chấp nhận và triển khai; chưa đồng bộ UI mới lên Figma. |
| 4. Mẫu tương tác và duyệt thiết kế | Thể hiện chuyển màn, chọn ngày, chuyển tháng; kiểm tra bố cục khi đổi kích thước; nhận góp ý và chỉnh sửa. | Người dùng duyệt thiết kế và hành vi trước khi code. Ảnh tĩnh không thay thế kiểm tra chuyển động; ghi rõ hiệu ứng cần xác minh tiếp trên app. | Người dùng xác nhận bước4 đạt và cho triển khai; animation tiếp tục được kiểm chứng trên app. Chưa có prototype Figma bấm thử. |
| 5. Chuẩn bị kiến trúc | Tổ chức Kotlin + Compose, lớp API, cache, trạng thái, điều hướng; chốt Android tối thiểu và ma trận thiết bị kiểm thử. | Cấu trúc dự án và kế hoạch triển khai rõ ràng, phù hợp spec và thiết kế đã duyệt. | Đã triển khai kiến trúc theo ARCHITECTURE.md, toolchain/minSdk đã khóa trong code và README. |
| 6. Lập trình | Triển khai dữ liệu → lịch tháng → chi tiết ngày → tra cứu nhanh → giao diện thích ứng → animation và thiết lập. Kiểm thử logic quan trọng trong quá trình code. | App chạy được, đáp ứng spec và thiết kế; các kiểm tra phù hợp đạt yêu cầu. | Đã triển khai16 phần theo COMMIT_PLAN.md, mỗi phần trên branch riêng và có review/build trước tích hợp develop; bản debug bàn giao kiểm thử. |
| 7. Review code | Đối chiếu spec/Figma; kiểm tra tính đúng đắn, trạng thái, hiệu năng, kiến trúc và khả năng bảo trì; sửa và kiểm chứng lỗi phát hiện. | Không còn lỗi chặn chuyển sang kiểm thử tổng thể; có kết quả review và xử lý lỗi. | Đã review từng diff/commit và sửa điểm chặn; kết quả tại docs/project/reviews. Không thay thế kiểm thử toàn bộ thiết bị. |
| 8. Testing tổng thể | Kiểm tra âm/dương, chuyển tháng/năm, tháng nhuận, offline/lỗi API, xoay máy, chia màn hình, resize, tăng cỡ chữ, trợ năng và thao tác liên tục. | Có báo cáo kiểm thử; sửa và kiểm thử lại lỗi; đạt tiêu chí nghiệm thu, không còn lỗi chặn phát hành. | Đã smoke-test emulator API35: dữ liệu thật, dialog, tháng nhuận, theme/restart, resize, chữ200%, offline cache. Giới hạn tại QA_REPORT.md; người dùng kiểm thử thêm trước main. |
| 9. Release | Tạo bản ký, phát hành thử nội bộ; chuẩn bị icon, ảnh giới thiệu, nội dung cửa hàng, thông tin riêng tư; phát hành chính thức sau khi người dùng duyệt. | Bản phát hành được duyệt, có kế hoạch theo dõi lỗi và cập nhật. | Chưa thực hiện. Chờ người dùng kiểm thử/merge main và chốt signing, quyền dữ liệu, package, privacy. |

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

1. Người dùng cài APK production debug, kiểm thử app, xem QA_REPORT.md và tự merge develop vào main khi đạt.
2. Xác minh các giới hạn API và yêu cầu phát hành trước release.
3. Nếu cần PR/Actions tự động, cấp quyền tương ứng; không khai báo PR/CI remote đã tồn tại khi chưa có bằng chứng.
