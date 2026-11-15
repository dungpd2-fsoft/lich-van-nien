# Kiểm chứng bản phát triển

Ngày: 2026-11-15. Bản debug để người dùng cài/test trước khi merge main. Không phải phát hành Play Store.

## Build và lint

- `scripts/check.sh --offline`: assembleDemoDebug, assembleProdDebug, lintProdDebug PASS.
- Android lint: 0 error; 30 warning thông báo phiên bản dependency/toolchain mới hơn. Không tắt lỗi lint hoặc dùng baseline để che lỗi.
- Không viết unit test theo chỉ dẫn người dùng. Lint có tác vụ phân tích source set test nhưng không có nghĩa đã chạy test suite.
- CI có script chạy được và template `ci/android-ci.yml`; chưa kích hoạt GitHub Actions, không tuyên bố CI remote đã pass.

## Runtime trên emulator API 35

| Ca | Kết quả / bằng chứng |
|---|---|
| Mở APK production | PASS: am start Status ok; tải ngày âm trong tháng11/2026; ảnh 13-prod-calendar.png |
| Dữ liệu ngày15/11/2026 | PASS: âm7/10, Tân Tỵ; dữ liệu thật từ API, không fixture production |
| Rộng1280x800 density160 | PASS: lịch trái, chi tiết phải; ảnh 15-prod-wide.png |
| Thu hẹp về điện thoại | PASS: giữ ngày chọn25/07/2025; không bị đặt lại hôm nay khi resize |
| S01 → S11 | PASS: mở trang chi tiết trên màn hẹp |
| Back từ S11 | PASS: quay về lịch tại vị trí cuộn cũ; Xem chi tiết vẫn trong viewport, không đặt lại lên đầu |
| D02 Hủy | PASS: đặt Sáng làm baseline, chọn Tối rồi Hủy vẫn giữ Sáng |
| D02 Áp dụng/restart | PASS: lưu Tối; force-stop/mở lại, S02 vẫn hiển thị Tối |
| S02 → S12 | PASS: có phiên bản và thông tin nguồn; link handler được review, chưa fault-inject trường hợp không có trình duyệt |
| D01 ngày sai30/02/2026 | PASS: báo V01, dialog vẫn mở; ảnh16-invalid-date.png |
| D01 Hủy | PASS: giữ ngày chọn15/11/2026 |
| D01 chọn25/07/2025 | PASS: chuyển đúng tháng/ngày, UI nhận âm1/6 nhuận và can chiQuý Mùi; ảnh16-leap-wide.png |
| Chữ200% | PASS: font200% vẫn truy cập Cài đặt, ảnh16-font200-settings.png |
| Offline và restart | PASS: tắt Wi-Fi/data, force-stop/mở lại, Room trả âm7/10 và hiện B01; ảnh16-offline.png |

Công cụ tự động thao tác UI dùng ADB/uiautomator để đọc nhãn, không chỉ dựa vào tọa độ cố định. Một số lượt thao tác ban đầu được sửa vì Back đóng dialog trên emulator dùng bàn phím cứng và vì nhãn nằm ngoài viewport; không ghi các lượt đó là lỗi app hoặc pass giả.

## Phạm vi chưa xác minh

- Chưa kiểm tra toàn bộ Android26–37, tablet thật/thiết bị gập thật/hinge vật lý, TalkBack đầy đủ hoặc benchmark trên máy yếu.
- HTTP429/5xx, response sai ngày/thiếu trường đã review code theo hợp đồng và phản hồi mẫu, chưa fault-inject qua UI app.
- Chưa kiểm tra mọi tổ hợp mất mạng giữa nhiều request hoặc process death ở tất cả màn. Không có unit/instrumentation suite mới.
- Quyền dữ liệu/quota/dải năm API, signing release, package chính thức và chính sách cửa hàng còn phải xác nhận trước phát hành.
- Người dùng vẫn kiểm thử app một lượt và merge main thủ công.


## Bản bàn giao

APK debug0.1.0 tại artifacts/lich-van-nien-0.1.0-prod-debug.apk (không commit binary). SHA-256: `bb3d02d866aa112d3f492efa10e8395c97b6f822b0bbb9a4a5c384a6c62253ce`. Ảnh sau sửa system bars: screenshots/16-final-day-dark.png. Lần mở APK cuối Status ok, không có AndroidRuntime error trong log kiểm tra.
