# Review 02 — feat(domain): define calendar models and repository contracts

Đã đọc model/contracts và lần theo grid 42 ngày bắt đầu thứ Hai, parse ngày Gregorian, Clock UTC+7, cờ nullable và chủ đề trung tính khi thiếu/mâu thuẫn. Sửa Java/Kotlin target về 17; domain/model không phụ thuộc Android.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-02.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
