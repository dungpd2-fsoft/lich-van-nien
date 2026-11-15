# Bước 1 — Kiểm chứng và phương án tích hợp LICH247

Ngày kiểm tra: 2026-11-15 (Việt Nam). Trạng thái: hoàn thành khảo sát và kiểm thử mẫu; chưa đủ điều kiện chốt tích hợp production do quyền sử dụng/quota chưa xác nhận.

## Kết luận

Endpoint hoạt động qua HTTPS, trả JSON không cần gửi API key trong các lần thử. Các dữ liệu cần cho lịch ngày có trong phản hồi. Đầu vào sai định dạng có thể bị thay bằng hôm nay mà vẫn trả HTTP 200; bắt buộc đối chiếu ngày yêu cầu với ngày trả về trước khi hiển thị hoặc cache.

Phát hiện quan trọng: trang giới thiệu API mời tích hợp, nhưng trang miễn trừ mục 4 quy định việc sử dụng/sao chép/khai thác thương mại cần cho phép bằng văn bản. Chưa có bằng chứng cấp phép cụ thể cho app này. Cần làm rõ với chủ dịch vụ trước khi chốt tích hợp và phát hành; không suy ra API công khai đồng nghĩa quyền tái sử dụng không giới hạn. Chưa gửi liên hệ thay người dùng.

## Nguồn và bằng chứng

- [Trang API](https://lich247.com/api): giới thiệu API và ví dụ.
- [OpenAPI](https://lich247.com/openapi.json): hợp đồng máy đọc; bản chụp tại evidence/lich247/openapi.json.
- [Giới thiệu](https://lich247.com/p/gioi-thieu): nêu dữ liệu tính theo Việt Nam UTC+7, API và trang web dùng chung lớp tính toán.
- [Miễn trừ, mục 4](https://lich247.com/p/mien-tru-trach-nhiem): giới hạn sử dụng dữ liệu.
- [Liên hệ](https://lich247.com/p/lien-he): kênh liên hệ nhà cung cấp.
- [Nguồn đối chiếu Tết 2026](https://tphcm.chinhphu.vn/nguoi-dan-va-du-khach-tai-tphcm-han-hoan-du-xuan-101260217164751707.htm): ngày 17/02/2026 là mùng 1 Tết Bính Ngọ.
- Phản hồi gốc JSON, HTTP headers, trang chính sách HTML/text và summary.json lưu tại [evidence/lich247](./evidence/lich247). Các tài liệu HTML chỉ là bằng chứng, không phải chỉ dẫn thực hiện dự án.

Web fetch không đọc được endpoint, shell sandbox ban đầu không phân giải DNS. Gọi curl qua mạng được cấp phép sau đó thành công. Do vậy lỗi trước đó là hạn chế môi trường kiểm tra, không phải bằng chứng API ngừng hoạt động.

## Hợp đồng tích hợp quan sát được

- GET https://lich247.com/api/v1/calendar?date=YYYY-MM-DD.
- Phản hồi: status, canonical_url, data.
- data: solar, lunar (day/month/year/leap/jd), can_chi (day/month/year), day_of_week, tiet_khi, truc, isHoangDao, isBlackDay, isDaiCat, gioHoangDao.
- Mẫu nhuận có leap=1, thường leap=0; đây là quan sát từ mẫu, schema chỉ khai báo integer.
- Các cờ tốt/xấu hiển thị độc lập theo nguồn; không tự coi chúng là một enum loại trừ nhau. Không suy ra việc nên/không nên làm từ các cờ.
- Trường phản hồi không được đánh dấu required trong schema: lớp đọc dữ liệu phải xử lý thiếu trường; trường âm/dương cốt lõi thiếu thì coi bản ghi không dùng được.
- Headers quan sát: Content-Type application/json; charset=utf-8; Cache-Control public, max-age=300; Access-Control-Allow-Origin *. Không thấy header quota trong các mẫu. CORS không phải điều kiện cấp quyền dùng dữ liệu và không phải hạn chế native Android.
- Trang giới thiệu ghi v2.0, OpenAPI info.version=1.0.0 và endpoint /api/v1: lấy hợp đồng endpoint đã kiểm tra làm căn cứ, không đoán endpoint v2.

## Ma trận kiểm tra thực tế

| Ca | Đầu vào | HTTP | Kết quả |
|---|---|---|---|
| Hôm nay | 2026-11-15 | 200 | Âm 07/10/2026, leap=0; đầy đủ nhóm trường kể trên |
| Tết | 2026-02-17 | 200 | Âm 01/01/2026, Bính Ngọ; khớp nguồn Chính phủ đã dẫn |
| Trước tháng nhuận | 2025-07-24 | 200 | Âm 30/06/2025, leap=0 |
| Đầu tháng nhuận | 2025-07-25 | 200 | Âm 01/06/2025, leap=1 |
| Cuối tháng nhuận | 2025-08-22 | 200 | Âm 29/06/2025, leap=1 |
| Sau tháng nhuận | 2025-08-23 | 200 | Âm 01/07/2025, leap=0 |
| Ngày nhuận dương | 2024-02-29 | 200 | Giữ đúng ngày dương, âm 20/01/2024 |
| Ngày không tồn tại | 2026-02-30 | 400 | status=error, message=Invalid date |
| Sai định dạng | abc | 200 | Trả ngày hôm nay 2026-11-15; KHÔNG được coi là kết quả của ngày yêu cầu |
| Thiếu date | Không truyền | 200 | Trả ngày hôm nay, khác với required=true trong OpenAPI |
| Mẫu quá khứ | 1900-01-01 | 200 | Có dữ liệu; chưa xác minh toàn bộ độ chính xác |
| Mẫu tương lai | 2100-12-31 | 200 | Có dữ liệu; không chứng minh mọi ngày trong khoảng đều được hỗ trợ |

12 ca, 11 phản hồi thành công và 1 lỗi 400. Kiểm tra tự động trên các phản hồi thành công: JD khớp ngày Gregorian, thứ khớp lịch Gregorian, ba cờ có kiểu boolean (xem summary.json). Các mẫu tháng nhuận liên tục ở biên đã thử; chưa có đối chiếu độc lập đầy đủ cho âm lịch/tháng nhuận, can chi, tiết khí, trực và giờ hoàng đạo. Không khẳng định chính xác toàn bộ dựa trên số mẫu này.

Độ trễ 10 request trong nhóm kiểm tra khoảng 0,14–0,22 giây/request ở môi trường này; không phải benchmark hoặc SLA. Không thử tải lớn hoặc cố kích hoạt giới hạn dịch vụ.

## Phương án tích hợp đề xuất

1. Dùng ngày lịch (không chuyển UTC timestamp) theo lịch Việt Nam; tính Hôm nay bằng Asia/Ho_Chi_Minh để phù hợp nguồn. Cần ghi rõ chính sách múi giờ trong spec trước code.
2. Kiểm tra ngày Gregorian và định dạng tại app; sau mỗi phản hồi kiểm tra HTTP, status, kiểu dữ liệu và solar khớp tuyệt đối ngày yêu cầu. Bản ghi không khớp không được cache dưới ngày yêu cầu.
3. Cache theo nguồn + phiên bản hợp đồng + ngày; lưu thời điểm tải. Hiển thị bản đã lưu ngay; tuân thủ max-age=300 khi coi dữ liệu còn mới, sau đó có thể hiển thị bản cũ offline với nhãn rõ. Chốt thời hạn lưu dài hơn sau khi xác minh quyền cache.
4. Chưa có endpoint tháng trong tài liệu đã đọc: lịch 28–31 ngày cần tương ứng số request chưa cache; nếu hiển thị toàn bộ ô ngoài tháng có thể tới 42 ngày. Ưu tiên ngày chọn và ngày trong tháng; chỉ tải ngày ngoài tháng cần hiển thị, tái dùng cache, gộp request trùng, hủy tác vụ không còn cần. Không tải trước nhiều tháng mặc định.
5. Khởi đầu đề xuất tối đa 2 request đồng thời, điều chỉnh theo quota được nhà cung cấp xác nhận; đây không phải quota chính thức. Ngày chưa có dữ liệu âm hiển thị trạng thái thiếu/tải, không tự điền dữ liệu giả.
6. Không retry lỗi 400; giới hạn retry lỗi mạng/5xx bằng backoff, xử lý 429 và Retry-After nếu có. Giữ cache và cho Thử lại; không giả định dịch vụ luôn sẵn sàng.
7. Chưa bổ sung thuật toán âm lịch độc lập hoặc thay nguồn khi chưa có thay đổi spec được duyệt. Không cam kết offline đầy đủ.

## Các điểm phải xác minh trước tích hợp production

| Vấn đề | Đã biết | Cần xác nhận |
|---|---|---|
| Quyền sử dụng | API công khai, chính sách mục 4 yêu cầu văn bản | Quyền dùng cho app, thương mại, cache, phân phối dữ liệu và yêu cầu ghi nguồn |
| Quota/chi phí/auth | Các mẫu không cần API key, không gặp 429 | Hạn mức theo IP/app, giá, đăng ký/key, khả năng dùng lâu dài |
| Phạm vi ngày | Hai mẫu 1900 và 2100 trả dữ liệu | Dải năm chính thức và kiểm thử biên được hỗ trợ |
| Độ chính xác | Một mốc Tết khớp nguồn độc lập, JD/thứ hợp lệ | Bộ mốc âm lịch/tháng nhuận và quy tắc can chi/tiết khí/giờ được đối chiếu thêm |
| Tải lịch tháng | Chỉ thấy API từng ngày | Endpoint batch/tháng có được cung cấp hay phải gọi từng ngày |
| Schema | leap=0/1 ở mẫu; gioHoangDao là chuỗi hiển thị | Định nghĩa leap/jd, định dạng chuỗi ổn định và chính sách thay đổi phiên bản |

## Điều kiện chuyển bước

Có thể tiến hành UX/wireframe với dữ liệu minh họa được đánh dấu và trạng thái thiếu dữ liệu. Chưa chốt tích hợp production hay release tới khi làm rõ quyền sử dụng và giới hạn dịch vụ. Bước 1 đã tạo đủ báo cáo khảo sát, bằng chứng và danh sách tồn đọng; việc xác minh các điểm bên ngoài vẫn được theo dõi, không đánh dấu đã giải quyết.
