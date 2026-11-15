# Review 15 — feat(ui): add adaptive list-detail layouts and motion

Review ranh giới840dp, state/scroll hoist qua resize, Day route không reset ngày đã chọn khi recreate, menu/callback wide không mở fullpage thừa. Motion month theo hướng, chọn ngày animateColor, sử dụng duration scale Compose hệ thống. Grid giới hạn336dp và cuộn ngang ở cực hẹp, nút/ô ở biên năm vô hiệu. Build 2variant PASS và xem ảnh tablet emulator. Chưa xác minh hinge vật lý/TalkBack.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-15.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
