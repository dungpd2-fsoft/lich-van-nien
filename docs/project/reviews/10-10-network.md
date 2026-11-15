# Review 10 — feat(network): add validated Lich247 remote data source

Review API contract theo bằng chứng12ca: HTTPS, timeout30s, status/solar khớp request, kiểm tra lunar/leap trước dùng, giữ các trường phụ nullable. 400/429/5xx/network tách lỗi, Retry-After parse số/HTTP date, CancellationException không nuốt. Không gửi token GitHub hoặc credential nào tới API lịch. Build prod network PASS; runtime sẽ kiểm tra ở bước ghép.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-10.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
