# Review 13 — feat(calendar): connect ViewModels to calendar data flows

Review ViewModel giữ selected/month trong SavedStateHandle, quan sát Room theo grid và ngày chọn riêng, ưu tiên selected, collectLatest hủy tháng cũ, lỗi refresh không xóa cache. Screen đọc lifecycle StateFlow, D01 xác nhận mới mutate. Cả APK PASS; production đã chạy và tải đúng âm7/10 cho15/11/2026 trên emulator (ảnh và dump đã kiểm tra).

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-13.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
