# Review16 — các điểm sửa trước tích hợp

Phạm vi: ranh giới module, UI state, dữ liệu không biết, offline và hành vi cỡ chữ; đối chiếu REQ01–10 và F01–F10. Một vòng review code, sau đó kiểm chứng phần sửa bằng build/lint/runtime.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|
| BUG-16-01 | CalendarRoutes / repository | Offline dùng cache phải có nhãn dù dữ liệu còn mới | Trước sửa chỉ dùng tuổi cache/lỗi refresh nên fresh-cache offline không gắn nhãn. Đã bổ sung Flow kết nối qua repository và B01. |
| BUG-16-02 | ChooseDateDialog | Lỗi ngày đưa focus về trường cần sửa, giữ nhập liệu | Trước sửa chỉ hiện text lỗi. Đã thêm FocusRequester cho ngày/tháng/năm. |
| BUG-16-03 | MonthScreen toolbar | Chữ lớn vẫn truy cập được Cài đặt | Text tiêu đề không có weight có thể chiếm chỗ nút. Đã giới hạn trọng số và chia đều nút Hôm nay/Chọn ngày. |
| BUG-16-04 | DayHero | Ngày dương đã biết vẫn hiện khi API chưa có dữ liệu | Trước sửa hero chỉ đọc ngày qua cache nên hiện —. Đã truyền ngày yêu cầu độc lập với CalendarDay nullable. |

| BUG-16-05 | MainActivity system bars | Icon hệ thống phải tương phản theo theme app | Ảnh QA theme Tối cho thấy status bar còn icon đen do dựa theme hệ thống. Đã đồng bộ WindowInsetsController với luminance của theme app, build/lint và kiểm tra ảnh sau sửa. |

Đã sửa trước commit. KSP không resolve contract NetworkMonitor mới trong lượt build đầu; đã gom trạng thái kết nối vào CalendarRepository, làm mới output liên quan và build/lint lại thành công. Domain export Coroutines API vì contract công khai Flow. Không để build lỗi được merge.

Hạn chế runtime và kết quả cuối theo QA_REPORT.md. Không khẳng định review thay thế testing trên mọi thiết bị.
