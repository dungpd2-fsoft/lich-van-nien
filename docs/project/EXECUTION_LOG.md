# Nhật ký triển khai Android

Quy trình: feature branch → build/kiểm tra → review một vòng → commit/push branch → merge và push develop. Không tự merge main. Không bổ sung unit test theo chỉ dẫn người dùng.

## GitHub

- Đã push develop từ main 195b1e1.
- Token dùng được Git push nhưng tạo PR qua API trả 403 Resource not accessible by personal access token. Vì vậy tích hợp bằng Git merge --no-ff sau kiểm chứng, giữ branch và commit; chưa tạo được PR. Token chỉ ở file tạm ngoài repo, không cấu hình vào URL remote.
- 01 bootstrap: branch build/01-bootstrap, commit bd9c18f; assembleDemoDebug PASS, review 01. Đã merge develop.
- 02 domain: branch feat/02-domain; core:domain:build PASS sau khi thống nhất Java/Kotlin target 17. Không có test source; không tuyên bố đã chạy unit test. Đã merge develop.
- 03 design system: đang triển khai. Artwork local, chọn theo ngày/địa chi, palette Material 3 sáng/tối; kiểm tra build library.

## Môi trường

- JDK: /home/dungpd2/android-studio/jbr, phiên bản 25.0.4; Gradle 9.7.1; AGP 9.3.2; Kotlin compiler plugin 2.3.0.
- Android SDK: /home/dungpd2/Android/Sdk; compile API 37.0; minSdk 26.
- Emulator Medium_Phone_API_35 đã boot (emulator-5554). Chưa kiểm tra hành trình nghiệp vụ ở thời điểm bootstrap/domain.
- Các lỗi build đã sửa trước tích hợp: Java target JVM model/domain khác Kotlin; CircleShape là object không phải constructor.

## Commit 13 runtime

APK production cài và mở trên emulator API35 Status ok. Đã dump UI và chụp ảnh: tháng 11/2026 tải được ngày âm cho ngày trong tháng; 15/11/2026 hiển thị âm 7/10/2026 và Tân Tỵ. Ảnh tại screenshots/13-prod-calendar.png. ADB có lúc offline tạm thời, kết nối lại và thu được bằng chứng; không ghi là crash app. Build demo/prod PASS.

## Commit 15 runtime

Build demo/prod PASS. Cài production và đổi emulator sang 1280x800, density160: UI hiển thị lịch bên trái và chi tiết bên phải cùng ngày15/11; tranh và 3 con giáp rõ, từng vùng có thể cuộn. Đã xem ảnh screenshots/15-prod-wide.png. Chưa coi là đã thử thiết bị gập thật/TalkBack; tiếp tục QA ở16. Artwork chuyển drawable-nodpi để tránh Android tự phóng bitmap theo density.


## Tiến độ cuối

Đã tích hợp commit01–15 vào develop bằng merge --no-ff, giữ từng branch và review riêng. Commit16 gồm QA, sửa lỗi đã phát hiện, README, CI template và script checks. Main vẫn dành người dùng merge thủ công. Xem QA_REPORT.md cho runtime; không tuyên bố có PR hoặc CI remote vì token API tạo PR trả403.


QA cuối: font200% vẫn mở Cài đặt; offline tắt Wi-Fi/data + restart đọc được Room âm7/10 và B01. Mạng/font emulator được khôi phục. System bar theo theme tối đã sửa và chụp lại. Build/lint cuối PASS,0 lỗi lint; các giới hạn ghi trong QA_REPORT.md.
