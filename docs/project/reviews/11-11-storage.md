# Review 11 — feat(storage): add Room calendar cache and theme DataStore

Review khóa cache source/contract/date, DAO chỉ đọc cùng nguồn/phiên bản và thứ tự ISO; fetchedAt lưu millis, lunar leap/cờ nullable/giờ null giữ nguyên roundtrip. Room schema v1 export, không destructive migration. DataStore dùng applicationContext, default SYSTEM khi thiếu/corrupt enum, IOException đọc fallback; lỗi ghi không nuốt. Build codegen Room và DataStore PASS.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-11.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
