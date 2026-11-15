# Bước 2 — UX v3: màn hình và sơ đồ luồng có mũi tên

Ngày: 2026-11-15. Trạng thái: đã sửa theo yêu cầu chia hai phần; chờ duyệt. Chưa UI chi tiết, chưa prototype bấm thử, chưa code app. Bản này thay thế UX v2; giữ cấu trúc hai phần và bổ sung sơ đồ có đường nối mũi tên.

## Phần 1 — Màn chính và màn cấp 2

[Figma phần 1](https://www.figma.com/design/DBfrO6T6QXhbRho7yUz9uJ/lich-van-nien?node-id=6-2).

| Mã | Cấp / loại | Node | Nội dung và điểm mở |
|---|---|---|---|
| S01 | Chính, cấp 1 | 6:20 | Lịch tháng; chọn ngày cập nhật tóm tắt; Hôm nay, chuyển tháng, mở dialog chọn ngày, mở cài đặt. |
| S02 | Chính, cấp 1 | 6:257 | Cài đặt; mở D02 để chọn giao diện; mở S12 để đọc thông tin nguồn. Truy cập từ S01, Back về S01. Không cần thanh tab cho bản đầu. |
| S11 | Cấp 2 từ S01 | 6:203 | Bấm Xem chi tiết; âm/dương, can chi, tiết khí, trực, phân loại ngày, giờ hoàng đạo. Back giữ tháng/ngày và vị trí cuộn ở S01. |
| S12 | Cấp 2 từ S02 | 8:79 | Thông tin app, phiên bản từ build, nguồn, múi giờ, liên kết nguồn/quyền riêng tư. Back về S02. Liên kết riêng tư chỉ bật khi có trang thật trước release. |
| S01-W | Biến thể rộng | 6:343 | Cùng S01; lịch trái, chi tiết phải; không thêm màn hay tính năng mới. |

D01 chọn ngày và D02 giao diện là lớp phủ, không phải màn cấp 2. Các trạng thái tải/offline/lỗi nằm trong S01 hoặc S11, không phải màn điều hướng riêng.

Layer có thể chỉnh sửa, auto-layout, Roboto, dùng nút và text style Material 3 Design Kit. Board là tài liệu wireframe ôm nội dung, chưa phải viewport/scroll prototype hay bằng chứng resize trên thiết bị. Màu trung tính; UI sáng/tối hoàn thiện ở bước 3.

## Phần 2 — Luồng người dùng, màn hình và tính năng

[Figma phần 2](https://www.figma.com/design/DBfrO6T6QXhbRho7yUz9uJ/lich-van-nien?node-id=8-91).

| Flow | Nguồn và thao tác | Kết quả / nhánh / phản hồi |
|---|---|---|
| F01 | Mở app | S01 tháng hiện tại, hôm nay theo UTC+7. Cache có: hiển thị ngay; cũ/offline thêm B01. Không cache: L01. Thành công cập nhật; lỗi E01. |
| F02 | S01 chạm ngày; bấm Xem chi tiết | Chạm cập nhật vùng chọn/tóm tắt; ngoài tháng thì chuyển tháng chứa ngày. Xem chi tiết mở S11 trên điện thoại; tablet cập nhật vùng phải. Back giữ lựa chọn. Không toast/dialog. |
| F03 | S01 vuốt hoặc Trước/Sau | Chỉ đổi tháng xem, giữ ngày chọn; không đặt dấu chọn vào ngày khác. Tải ngày chưa cache. Kết quả cũ không ghi đè lựa chọn mới. Tới giới hạn năm đã xác minh thì vô hiệu nút. Không toast. |
| F04 | S01 Hôm nay | Cập nhật cả tháng xem và ngày chọn về hôm nay; đã ở hôm nay thì giữ nguyên. Dùng cache hoặc L01/E01. Không toast. |
| F05 | S01 chạm tiêu đề tháng/năm | Mở D01. Hợp lệ: đóng, S01 tới ngày chọn. Sai: V01, giữ nhập liệu. Hủy/Back/chạm ngoài: không đổi ngày. |
| F06 | S01 mở S02; S02 chạm Giao diện | D02 chọn tạm. Áp dụng: lưu và đổi giao diện, T01 nếu có thay đổi. Hủy/Back/chạm ngoài: bỏ lựa chọn tạm. Chọn lại cùng chế độ: đóng, không toast. |
| F07 | S01/S11 tải lỗi; bấm Thử lại | Có cache: B01; không cache: E01. Retry: L01; thành công hiện dữ liệu; vẫn lỗi giữ thông báo và nút thử lại. Không xếp toast lỗi liên tục. |
| F08 | Phản hồi thiếu/sai dữ liệu | Thiếu trường phụ: Chưa có thông tin, giữ phần còn lại. Sai solar hoặc thiếu ngày âm/dương cốt lõi: E02, không cache như bản ghi hợp lệ. Không dialog chặn đọc. |
| F09 | S02 mở S12; bấm link | Mở trình duyệt ngoài; trở về app giữ S12. Không có ứng dụng xử lý URL: T02. Back S12 về S02. |
| F10 | Back, xoay, resize | Dialog đóng trước; S11/S12 về cha, S02 về S01. Back S01 theo Android, không dialog xác nhận thoát. Giữ tháng/ngày khi resize; detail chuyển giữa màn riêng và vùng cạnh lịch theo không gian khả dụng. |

## Dialog và xác thực đầu vào

| Mã | Nội dung | Hành động và đóng |
|---|---|---|
| D01 | Tiêu đề Chọn ngày dương lịch; trường Ngày/Tháng/Năm | Hủy, Xem ngày. Kiểm tra lịch Gregorian và phạm vi đã xác minh trước request. Hủy/Back/chạm ngoài không đổi lựa chọn. Điện thoại chọn dialog/sheet phù hợp chiều cao, tablet dialog giữa màn; hình thức cuối chốt bước UI. |
| D02 | Chọn giao diện; Theo hệ thống, Sáng, Tối | Hủy, Áp dụng. Chỉ lưu khi Áp dụng. Hủy/Back/chạm ngoài bỏ giá trị tạm. Focus trong dialog; khi đóng trả focus về mục mở dialog. |
| V01 | Ngày không hợp lệ. Vui lòng kiểm tra ngày, tháng, năm. | Lỗi ngay dưới nhóm trường, focus trường cần sửa; dialog vẫn mở, giữ giá trị nhập, không gọi API. |

## Toast, banner và trạng thái lỗi

| Mã | Loại | Nội dung chính xác / hành động | Điều kiện |
|---|---|---|---|
| T01 | Toast ngắn | Đã cập nhật giao diện | Áp dụng thay đổi thành công ở D02; không nút, không nhận focus, không hiện khi giá trị không đổi. |
| T02 | Toast ngắn | Không tìm thấy ứng dụng để mở liên kết | Android không có ứng dụng xử lý URL, giữ S12. |
| B01 | Banner bền trong nội dung | Bạn đang xem dữ liệu đã lưu; thời điểm lưu; nút Thử lại | Dùng cache cũ/offline; bỏ khi tải được dữ liệu mới, không tự biến mất như toast. |
| E01 | Lỗi trong nội dung | Chưa tải được thông tin ngày / Kiểm tra kết nối rồi thử lại. / Thử lại | Không cache và lỗi kết nối. Nếu xác định lỗi dịch vụ, mô tả: Dịch vụ tạm thời chưa phản hồi. Vui lòng thử lại. |
| E02 | Lỗi trong nội dung | Chưa nhận được dữ liệu hợp lệ cho ngày này / Thử lại | Ngày trả về không khớp hoặc thiếu dữ liệu cốt lõi. |
| L01 | Đang tải | Đang tải thông tin ngày… | Giữ lịch dương, vùng chọn, điều hướng; vô hiệu nút retry trong khi retry để tránh trùng; vẫn cho chọn ngày khác. |

Không show toast khi chọn ngày, đổi tháng hoặc trở về hôm nay. Không dùng dialog để báo lỗi mạng thông thường. Trường phụ thiếu không đồng nghĩa false/Không. Tháng nhuận ghi rõ nhuận. Không hiển thị ngày khác thay ngày yêu cầu.

## Dữ liệu, chuyển động và nghiệm thu

- Mẫu 15/11/2026 lấy từ phản hồi đã kiểm chứng; thời điểm cache minh họa. Dấu — trên ô khác là chưa có dữ liệu âm, không phải ngày âm thực.
- Hôm nay khác ngày đang chọn: UI bước 3 cần bổ sung hình thể hiện hai trạng thái riêng và mẫu tháng nhuận.
- Chữ lớn/cửa sổ thấp được cuộn. Ngưỡng một/hai vùng và vùng chạm xác minh ở bước UI và app.
- Phản hồi nhấn, trượt tháng theo hướng, vào/ra chi tiết và dialog: chuyển động không chặn thao tác, tôn trọng giảm/tắt animation. Hiệu ứng cụ thể và prototype ở bước 4.
- Đã kiểm tra ảnh hai board, cấu trúc có text/frame/instance chỉnh sửa được; không có ảnh raster chứa UI hoàn chỉnh.
- Quyền dùng API và quota vẫn theo API_VALIDATION.md. Không mở rộng widget, nhắc lịch hay offline đầy đủ.

Cần duyệt hai phần và các nội dung thông báo trước bước 3.

## Bổ sung UX v3 — sơ đồ có mũi tên

Phần 2 giữ board 8:91, thêm sáu sơ đồ có 31 đường nối mũi tên dạng vector chỉnh sửa được. Nhãn đường nối là hành động hoặc điều kiện. Khối dialog màu tím, toast nền đậm, lỗi màu cam; mỗi khối có mã và nội dung, không chỉ phân biệt bằng màu.

| Sơ đồ | Node | Nội dung |
|---|---|---|
| A | 10:70 | Chuyển màn S01/S11/S02/S12 và Back |
| B | 10:110 | Mở D01, xác nhận hợp lệ, hủy, lỗi V01 và sửa lại |
| C | 10:154 | Mở D02, áp dụng/hủy/không đổi, show T01 và tự đóng |
| D | 10:194 | Tải, kiểm tra dữ liệu, cache B01, lỗi E01/E02 và Thử lại |
| E | 10:239 | Mở liên kết, trình duyệt hoặc T02, trở về app |
| F | 10:279 | Chuyển tháng, hôm nay, chọn ngày liên tục và resize |

Đã kiểm tra hình ảnh từng sơ đồ và căn đường nối sát khối nguồn/đích. Đây là sơ đồ UX, không phải liên kết prototype bấm thử; các bảng flow và đặc tả thông báo bên dưới sơ đồ vẫn là tài liệu bổ sung.
