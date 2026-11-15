# Lịch vạn niên Android

Ứng dụng Kotlin/Jetpack Compose tra cứu lịch âm/dương từ LICH247, có minh họa 12 con giáp Việt Nam, Material 3 sáng/tối và bố cục lịch/chi tiết thích ứng kích thước cửa sổ.

## Chuẩn bị

- Android Studio với JDK tương thích Gradle 9.7.1 (máy triển khai đã build bằng JDK 25.0.4).
- Android SDK platform 37.0 và build tools tương ứng; app hỗ trợ Android 8.0/API 26 trở lên.
- Tạo `local.properties` với `sdk.dir=/duong/dan/Android/Sdk`. File này không commit.
- Nếu chạy terminal, đặt `JAVA_HOME` tới JDK. Không cần GitHub token để build/chạy app.

## Build và chạy

```bash
./gradlew :app:assembleDemoDebug
./gradlew :app:assembleProdDebug
```

- **demoDebug** (`com.lichvannien.demo`): dữ liệu fixture đã kiểm chứng ở một số ngày, có banner Demo; ngày không có mẫu báo thiếu, không tự sinh lịch âm. Có thể thử 15/11/2026, 17/02/2026, 25/07/2025.
- **prodDebug** (`com.lichvannien`): gọi HTTPS LICH247, lưu cache Room trên thiết bị. Đây là bản debug để kiểm tra, chưa phải bản release Play Store.
- APK: `app/build/outputs/apk/demo/debug/app-demo-debug.apk` và `app/build/outputs/apk/prod/debug/app-prod-debug.apk`.

Cài trên thiết bị/emulator bằng Android Studio hoặc:

```bash
adb install -r app/build/outputs/apk/prod/debug/app-prod-debug.apk
adb shell am start -n com.lichvannien/com.lichvannien.MainActivity
```

Kiểm tra build/lint:

```bash
scripts/check.sh
```

Không bổ sung unit test theo yêu cầu hiện tại. Script kiểm tra build hai biến thể, Android lint và whitespace; bằng chứng kiểm tra thủ công được lưu riêng, không thay thế bằng tuyên bố unit tests đã pass.

## Chức năng

- Lịch tháng 7 cột, ngày âm/dương, chọn ngày, về hôm nay, chuyển tháng bằng nút/vuốt; dialog tra cứu ngày Gregorian hợp lệ.
- Chi tiết ngày với tranh, con giáp của ngày/tháng/năm, tháng âm nhuận, can chi, tiết khí, trực, cờ phân loại ngày và giờ hoàng đạo.
- Cache theo nguồn/phiên bản/ngày, nhãn dữ liệu lưu khi offline hoặc cũ; retry và kiểm tra ngày API trả về. Không cam kết offline toàn bộ.
- DataStore lưu chế độ theo hệ thống/sáng/tối. Toast sau lưu thành công; lỗi link được xử lý.
- Cửa sổ rộng từ 840dp hiển thị lịch/chi tiết hai vùng; giữ ngày chọn và scroll khi resize. Cửa sổ cực hẹp cho phép cuộn ngang lưới để giữ vùng chạm. Hỗ trợ cuộn nội dung/chữ lớn.
- Animation dùng cơ chế duration scale của Compose/hệ thống, không sinh ảnh AI lúc chạy app.

## Kiến trúc

Tham khảo Now in Android local theo yêu cầu: convention plugins, feature api/impl, UI/domain/data, Hilt, UDF/StateFlow. Domain/model thuần Kotlin, repository interface ở domain; data triển khai Room/Retrofit/DataStore. Lịch tháng và ngày cùng feature calendar để chia sẻ trạng thái/adaptive layout.

Xem [kiến trúc](docs/ARCHITECTURE.md), [spec](docs/project/SPEC.md), [UX](docs/project/UX_DESIGN.md), [kế hoạch commit](docs/project/COMMIT_PLAN.md) và [nhật ký triển khai](docs/project/EXECUTION_LOG.md).

## Git và bàn giao

Từng thay đổi phát triển trên branch riêng từ develop, build và review trước merge --no-ff vào develop. Token phiên làm việc có quyền push nhưng GitHub từ chối tạo PR (403 thiếu quyền Pull requests); không tuyên bố đã tạo PR. Main giữ cho người dùng kiểm thử và merge thủ công. Credential không nằm trong repo hoặc URL remote.

## Giới hạn trước phát hành

- Quyền sử dụng dữ liệu, quota, chi phí, dải năm chính thức và độ chính xác đầy đủ của API chưa được nhà cung cấp xác nhận; xem [báo cáo API](docs/project/API_VALIDATION.md).
- Không dùng dữ liệu sai ngày khi API trả 200 cho đầu vào sai; không tự suy ra false khi trường thiếu. Hình chỉ minh họa chủ đề theo dữ liệu nguồn, không phải dự đoán.
- API lịch ngày không có endpoint ảnh hoặc tháng trong hợp đồng đã kiểm tra. Tải từng ngày trong tháng, giới hạn tối đa 2 yêu cầu đồng thời, gộp trùng và cooldown khi 429.
- Chưa có đăng nhập, nhắc ngày giỗ, widget, đồng bộ đám mây hoặc full offline.
- ApplicationId `com.lichvannien` hiện dùng cho phát triển; tên gói phát hành, signing key, chính sách quyền riêng tư và metadata cửa hàng cần chốt ở bước release.
- Thiết bị gập thật, hinge che nội dung, TalkBack đầy đủ và kiểm thử hiệu năng trên máy cấu hình thấp cần xác minh thêm trước phát hành.
