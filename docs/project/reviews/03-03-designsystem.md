# Review 03 — feat(designsystem): add Material themes and zodiac artwork

Review theme sáng/tối, atlas 4x3 đúng thứ tự Mão mèo, srcOffset theo enum, hero có rắn chỉ cho ngày Tỵ hoàng đạo, fallback trung tính và chữ trên nền sáng đọc được. UI không gọi API; đã sửa CircleShape trước build lại.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-03.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
