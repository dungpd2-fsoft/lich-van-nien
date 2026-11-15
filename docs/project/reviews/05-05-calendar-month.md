# Review 05 — feat(calendar): implement month screen UI

Review grid 42 ngày/thứ Hai, 7 cột, ngày ngoài tháng, tách hôm nay và ngày chọn, chữ âm chưa tải dùng dấu —. Screen chỉ nhận state/callback; state month/selection saveable ở route tạm. D01 sẽ nối ở commit07, dữ liệu thật ở13; không tự tính ngày âm.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-05.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
