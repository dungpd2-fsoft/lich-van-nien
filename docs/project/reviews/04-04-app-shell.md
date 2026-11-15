# Review 04 — feat(app): add typed navigation and composition root

Review routes typed Calendar/Day/Settings/About, app chỉ ghép feature, Hilt Application/Activity. Build PASS; cài demo và am start -W trên emulator API35 Status ok, không có AndroidRuntime error lúc khởi động. Các entry hiện là màn tối thiểu, UI chi tiết sẽ làm ở commit 05-09; demo repository sẽ ghép khi tầng data hoàn tất.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-04.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
