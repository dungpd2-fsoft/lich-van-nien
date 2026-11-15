# Review 07 — feat(calendar): implement date picker and validation dialog

Review D01 giữ bản nháp trong dialog, xác thực LocalDate trước xác nhận, năm 4 chữ số 1..9999, 30/02 không đổi ngày; Hủy/Back/outside chỉ đóng, nhập lại bỏ lỗi. Nối nút chọn ngày và tiêu đề tháng ở S01. Đã build PASS; request API chưa ghép trước13.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-07.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
