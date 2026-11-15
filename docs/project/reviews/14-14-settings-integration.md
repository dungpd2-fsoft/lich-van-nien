# Review 14 — feat(settings): persist theme and deliver UI feedback

Review Hilt SettingsViewModel/DataStore, app theme lifecycle Flow, draft không mutate trước Áp dụng, tránh save đồng thời, toast chỉ sau edit thành công và có ID/ack tránh lặp recomposition. Lỗi ghi hiện thông báo thay vì toast thành công; Cancellation không bị nuốt. Build cả2variant PASS; kiểm tra lưu/restart ở bước16.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-14.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
