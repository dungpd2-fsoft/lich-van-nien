# Review 12 — feat(data): implement cache-first calendar repositories

Review repository local source of truth, cache300s, semaphore2, Deferred dedup và CancellationException, retry một lần chỉ network/service, Retry-After cooldown429, chỉ lưu remote đã validate. Room và settings bind singleton. Demo/prod dùng module DI riêng, fixture chỉdemo; không fallback fixture ởprod. Cả APK demo/prod build PASS. Kiểm tra runtime/cache tiếp ở13.

| Mã lỗi | Vị trí | Yêu cầu và hành vi mong đợi | Hành vi thực tế và cách tái hiện |
|---|---|---|---|

Không phát hiện lỗi chặn trong phạm vi review sau các sửa đã kiểm chứng.

Kiểm chứng: build thành công, log phiên làm việc `/tmp/lich-build-12.log`. Không bổ sung unit test theo chỉ dẫn người dùng. Các kiểm tra UI/runtime chỉ ghi đã chạy khi có bằng chứng trong EXECUTION_LOG.md.
