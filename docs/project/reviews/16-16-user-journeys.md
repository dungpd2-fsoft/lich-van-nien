# Review 16 — test: validate calendar journeys and configure quality gates

Review một vòng theo spec và F01-F10; điểm sửa và chứng cứ tại16-qa-findings.md/QA_REPORT.md. Đã kiểm tra production thực trên emulator: API, cache offline/restart, D01 invalid/Hủy, tháng nhuận, D02 Hủy/persist, About, Back, wide→phone và font200%. System bars đã sửa theo theme và xem ảnh cuối. Build demo/prod + lint PASS0errors; không thêm unit tests, CI remote chưa chạy và PR API403. Không coi emulator là mọi thiết bị. Không merge main.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-16.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
