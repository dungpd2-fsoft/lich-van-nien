# Nhật ký triển khai Android

Quy trình: feature branch → build/kiểm tra → review một vòng → commit/push branch → merge và push develop. Không tự merge main. Không bổ sung unit test theo chỉ dẫn người dùng.

## GitHub

- Đã push develop từ main 195b1e1.
- Token dùng được Git push nhưng tạo PR qua API trả 403 Resource not accessible by personal access token. Vì vậy tích hợp bằng Git merge --no-ff sau kiểm chứng, giữ branch và commit; chưa tạo được PR. Token chỉ ở file tạm ngoài repo, không cấu hình vào URL remote.
- 01 bootstrap: branch build/01-bootstrap, commit db xem Git log; assembleDemoDebug PASS, review 01. Đã merge develop.
- 02 domain: branch feat/02-domain; core:domain:build PASS sau khi thống nhất Java/Kotlin target 17. Không có test source; không tuyên bố đã chạy unit test. Đã merge develop.
- 03 design system: đang triển khai. Artwork local, chọn theo ngày/địa chi, palette Material 3 sáng/tối; kiểm tra build library.

## Môi trường

- JDK: /home/dungpd2/android-studio/jbr, phiên bản 25.0.4; Gradle 9.7.1; AGP 9.3.2; Kotlin compiler plugin 2.3.0.
- Android SDK: /home/dungpd2/Android/Sdk; compile API 37.0; minSdk 26.
- Emulator Medium_Phone_API_35 đã boot (emulator-5554). Chưa kiểm tra hành trình nghiệp vụ ở thời điểm bootstrap/domain.
- Các lỗi build đã sửa trước tích hợp: Java target JVM model/domain khác Kotlin; CircleShape là object không phải constructor.
