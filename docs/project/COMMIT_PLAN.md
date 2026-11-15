# Kế hoạch commit triển khai

Trạng thái: đã triển khai16 phần theo thứ tự dưới đây, mỗi phần có branch/review/build trước tích hợp develop. Kết quả thực tế và giới hạn tại QA_REPORT.md; bảng dưới giữ phạm vi kế hoạch để đối chiếu.

Người dùng đã chấp nhận thiết kế để chuyển bước 5. Không yêu cầu duyệt lại Figma/prototype trước code; animation sẽ kiểm chứng trên app. Không tuyên bố prototype Figma đã được tạo.

## Nguyên tắc — cập nhật theo yêu cầu mới nhất

- Nhánh tích hợp là `develop`, tạo từ `main`. Nhánh `main` chỉ do người dùng verify/test app và merge thủ công; agent không push hoặc merge vào main.
- Mỗi đơn vị triển khai có branch riêng tách từ develop mới nhất. Giữ tên 16 commit và phạm vi bên dưới; đích tích hợp đổi từ main sang develop.
- Quy trình: cập nhật develop → tạo feature branch → triển khai → build/lint/kiểm tra hành vi phù hợp → review diff đúng một vòng theo spec → sửa lỗi và kiểm chứng phần sửa → commit → mở/cập nhật PR vào develop nếu công cụ và quyền cho phép → tự merge khi đạt spec và không có lỗi chặn → cập nhật develop rồi tiếp tục phần tiếp theo. Không cần chờ người dùng review develop.
- Người dùng cho phép không viết unit test: không bắt buộc bổ sung unit test theo các dòng kiểm chứng cũ trong bảng. Không bỏ qua build/lint, kiểm tra trên emulator/thiết bị nếu có, hoặc các test đã có liên quan. Không coi tự review là bằng chứng app đã được kiểm thử trên thiết bị.
- Các yêu cầu unit test trong bảng là gợi ý kiểm chứng từ kế hoạch trước, đã được quy tắc mới này thay thế khi chưa triển khai. Vẫn kiểm tra ca biên và xử lý lỗi bằng cách phù hợp, ghi bằng chứng thực tế và giới hạn chưa xác minh.
- Khi còn lỗi build, sai spec hoặc kiểm tra thiết yếu không chạy được thì không tuyên bố đạt để tự merge; sửa hoặc ghi rõ điểm chặn. Review dùng skill-review-code, đúng một vòng và lưu kết quả theo từng thay đổi; kiểm chứng sửa lỗi không mở vòng review vô hạn.
- Mỗi commit/PR ghi rõ yêu cầu/flow liên quan, thay đổi hành vi, kiểm tra đã chạy, kết quả review và giới hạn. Giữ dữ liệu demo tách production, không thêm secret, không stage thay đổi người dùng ngoài phạm vi.
- Sau merge vào develop, xác nhận kết quả Git rồi tạo branch tiếp theo từ develop mới nhất. Không amend/force-push develop hoặc main; feature branch chỉ amend an toàn khi không ghi đè thay đổi người khác.
- Sau hoàn tất triển khai, chuẩn bị bản app và báo cáo bàn giao để người dùng test. Nếu tạo PR develop → main thì để mở, tuyệt đối không tự merge.
- Thiết kế đã được người dùng chấp nhận; hạn mức Figma không chặn code. ApplicationId chính thức/quyền dùng API trước phát hành vẫn cần xử lý theo tài liệu.

## Thứ tự commit

| # | Commit đề xuất | Phạm vi / kết quả | Kiểm chứng cần có |
|---|---|---|---|
| 01 | `build: bootstrap modular Android project and convention plugins` | Wrapper, version catalog, app, build-logic cho Kotlin/Android/Compose/Hilt; cấu hình demo/prod cơ bản, CI build tối thiểu. Khóa toolchain tương thích sau khảo sát máy và tài liệu chính thức. | Gradle sync, demo debug build, lint cơ bản. |
| 02 | `feat(domain): define calendar models and repository contracts` | core:model/domain/common/testing; LocalDate, LunarDate, CanChi, Zodiac, DayTheme, freshness/errors; repository contracts, Clock/dispatcher. Dựng lưới tháng, Hôm nay UTC+7, kiểm tra ngày, chọn chủ đề tranh. | Unit test biên tháng/năm/nhuận dương, timezone, unknown/mâu thuẫn và ánh xạ con giáp. |
| 03 | `feat(designsystem): add Material themes and zodiac artwork` | core:designsystem/ui; màu sáng/tối, typography, component chung, date hero, zodiac, giờ hoàng đạo, loading/error/banner. Đóng gói/tối ưu artwork, giữ nguồn gốc asset. | Preview sáng/tối; kiểm tra ảnh/text, resource và build. |
| 04 | `feat(app): add typed navigation and demo composition root` | core:navigation, feature calendar/settings/about api/impl cần thiết; MainActivity, Hilt, app state, back stack, fake repository demo và entry cơ bản có nội dung. | Chạy demo, vào các entry và Back; không gọi mạng. |
| 05 | `feat(calendar): implement month screen UI` | S01 lịch tháng, hôm nay/vùng chọn, tóm tắt có con giáp, nút điều hướng và chọn nhanh; state/callback, preview fixture. | Preview 360/412 px, chọn ngày khác hôm nay, trạng thái tải một phần; build. |
| 06 | `feat(calendar): implement illustrated day detail UI` | S11 tranh lịch ngày, ngày âm/dương, can chi với 3 con giáp, cờ phân loại, tiết khí/trực/giờ; phần hiển thị có thể tái dùng trên tablet. | Preview sáng/tối, tháng âm nhuận, thiếu dữ liệu và ảnh fallback. |
| 07 | `feat(calendar): implement date picker and validation dialog` | D01 nhập/chọn ngày/tháng/năm; giá trị tạm, Hủy/Back/chạm ngoài; lỗi V01; callback xác nhận. | Kiểm tra ngày không tồn tại, giữ nhập liệu và hủy không đổi ngày chọn. |
| 08 | `feat(settings): implement settings screen and theme dialog` | S02 và D02 stateless, ba chế độ giao diện, Hủy/Áp dụng, liên kết S12; demo callback. | Preview sáng/tối/chữ lớn, đóng dialog đúng hành vi. |
| 09 | `feat(about): implement app information and source screen` | S12, phiên bản từ build, nguồn và UTC+7; mở link qua handler, T02 nếu không có ứng dụng; quyền riêng tư không dùng URL giả. | Kiểm tra Back và handler link; preview/build. |
| 10 | `feat(network): add validated Lich247 remote data source` | core:network, Retrofit/OkHttp/serialization, DTO và kiểm tra HTTP/status/solar; timeout, phân loại lỗi, trường nullable, không log dữ liệu nhạy cảm. | Test với MockWebServer/fixture: 200 đúng/sai ngày, abc→hôm nay, 400/429/5xx, JSON sai/thiếu. |
| 11 | `feat(storage): add Room calendar cache and theme DataStore` | core:database/datastore; khóa nguồn+phiên bản+ngày, thời điểm tải, DAO Flow; Preferences DataStore lưu theme, schema export. | DAO roundtrip/Flow, không mất unknown/leap; đọc ghi theme. Migration test khi thật sự có thay đổi schema. |
| 12 | `feat(data): implement cache-first calendar repositories` | core:data; bind contracts domain, local source of truth, refresh, cache freshness, retry/backoff/Retry-After, gộp request trùng, giới hạn đồng thời, không lưu bản sai ngày. | Test cache/network lỗi, dữ liệu cũ, dedup và giới hạn tải; ngày không được hỗ trợ không làm hỏng cache. |
| 13 | `feat(calendar): connect ViewModels to calendar data flows` | Ghép S01/S11/D01, tháng xem/ngày chọn, SavedStateHandle, Room Flow + refresh; ưu tiên ngày chọn, không để kết quả cũ ghi đè; đầy đủ L01/B01/E01/E02. | ViewModel tests chọn nhanh, chuyển tháng, retry, offline, process restore; demo/prod build. |
| 14 | `feat(settings): persist theme and deliver UI feedback` | Ghép DataStore với app theme và S02/D02; T01 chỉ sau lưu thay đổi thành công; T02 qua handler; không lặp effect khi recomposition/rotate, không toast khi không đổi. | Kiểm tra lưu/restart, hủy không lưu, effect chỉ hiển thị một lần và không mất lỗi lưu. |
| 15 | `feat(ui): add adaptive list-detail layouts and motion` | S01/S11 một/hai vùng, cửa sổ hẹp/trung/rộng, insets/fold/chia màn hình; giữ state/back; animation chọn ngày/đổi tháng/chuyển màn/dialog, hỗ trợ giảm/tắt motion. | Emulator điện thoại/tablet, resize/xoay, font lớn, TalkBack, thao tác liên tục; đo tránh giật khi decode ảnh. |
| 16 | `test: cover calendar journeys and configure quality gates` | Bổ sung kiểm thử hành trình F01–F10, screenshot mẫu hẹp/rộng/sáng/tối cho màn trọng yếu; CI lint/unit/build/thiết bị theo khả năng môi trường. Cập nhật README chạy demo/prod và tài liệu kiến trúc thực tế. | Các kiểm tra cấu hình chạy được; ghi rõ phần không chạy được, không tạo báo cáo pass giả. |

## Các mốc review

- Sau 04: nền tảng kiến trúc và app shell chạy được.
- Sau 09: đủ các màn UI, review hình ảnh trên demo trước ghép dữ liệu thật.
- Sau 14: hành trình hoàn chỉnh với repository/cache/settings.
- Sau 16: bàn giao sang bước review code và testing tổng thể của quy trình dự án; kiểm thử trong commit không thay thế các bước này.
- Release/ký/publish thực hiện ở bước release riêng, không nằm trong 16 commit triển khai này.

## Việc cần khóa trước commit đầu

Khảo sát SDK/JDK/Gradle và dependency tương thích; minSdk 26 là đề xuất. ApplicationId/namespace tạm và tên hiển thị được ghi lại, applicationId chính thức cần chốt trước phát hành. Không dùng phiên bản alpha chỉ vì mẫu NIA có sử dụng. Dải năm và quota không suy ra từ hai ngày mẫu trả thành công.

Không chặn dựng UI và demo vì vấn đề cấp phép API; tích hợp production/phát hành vẫn phải theo các điểm chưa xác minh trong API_VALIDATION.md.
