# Review 01 — Bootstrap

Phạm vi: Gradle wrapper/toolchain, app Compose tối thiểu, version catalog, convention plugin, demo/prod, manifest và tài liệu kiến trúc.

Đã đọc cấu hình build, manifest, MainActivity và diff tài liệu; kiểm tra không chứa credential, local.properties bị ignore, HTTPS-only, minSdk 26. ApplicationId com.lichvannien là tạm cho phát triển.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi trong phạm vi review.

Kiểm chứng: assembleDemoDebug (ghi kết quả ở nhật ký triển khai trước merge). Chưa kiểm tra các màn nghiệp vụ ở bootstrap. Không viết unit test theo chỉ dẫn người dùng. Chưa coi build-logic cho library đã kiểm chứng đến khi module library đầu tiên được build.
