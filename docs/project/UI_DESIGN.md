# Bước 3 — UI Material 3 v1

## Cập nhật UI v2 — minh họa theo góp ý

Người dùng yêu cầu thêm hình 12 con giáp và tranh lịch ngày theo ảnh tham khảo. Bản v2 tại [design/ui-v2/index.html](../../design/ui-v2/index.html), thay hướng hình ảnh của S01/S11, giữ flow UX. Ảnh tổng hợp: [preview.png](../../design/ui-v2/preview.png). Bộ UI v1 vẫn giữ cho các màn còn lại.

- Đã tạo bằng công cụ imagegen tích hợp: một atlas 12 con giáp Việt Nam, một tranh hồ sen ngày Tỵ/hoàng đạo, một tranh vườn sau mưa cho chủ đề hắc đạo và một tranh đồng quê trung tính. Lưu tại design/ui-v2/assets; không dùng ảnh của người khác hoặc nội dung trích dẫn trong ảnh mẫu.
- Màn ngày sáng/tối có ảnh lớn, số ngày và dữ liệu là text UI, không ghi chết vào ảnh. Thêm con giáp ngày Tỵ/tháng Hợi/năm Ngọ theo phản hồi ngày 15/11/2026 đã lưu.
- Atlas giữ nguyên ảnh gốc; các huy hiệu dùng CSS background-position để hiển thị ô, không cắt sửa file ảnh. Hướng artwork: watercolor/gouache, màu lavender-jade, chất giấy, phong vị Việt Nam, không emoji/3D.
- Chọn hình theo dữ liệu: isHoangDao=true và isBlackDay=false → chủ đề hoàng đạo; isBlackDay=true và isHoangDao=false → chủ đề trầm tĩnh; thiếu hoặc hai cờ mâu thuẫn/đều false → trung tính. isDaiCat hiển thị riêng, không ghi đè ý nghĩa hai cờ khác. Không diễn giải tiết khí thành kết luận vận khí mới.
- Tranh có rắn chỉ dùng cho ngày Tỵ; các ngày khác dùng tranh không có động vật + huy hiệu con giáp đúng. Không dùng một tranh có rắn cho mọi ngày hoàng đạo. Bộ chủ đề dùng lại, không cam kết mỗi ngày một tranh độc nhất.
- Ảnh chủ đề là minh họa, API chưa có endpoint ảnh. Không sinh ảnh trực tiếp mỗi lần người dùng mở app. Asset cần đóng gói/cache khi triển khai.
- Đã xem ảnh gốc và bản render Chrome, sửa căn ảnh để thấy con giáp và giữ nhãn phân loại ngày. Chưa kiểm chứng tương tác hoặc resize trên Android, chưa nhập v2 vào Figma do trở ngại MCP đã ghi bên dưới.

### Prompt set của bộ minh họa (tóm tắt)

1. Atlas 4 cột × 3 hàng, thứ tự Tý/Sửu/Dần/Mão, Thìn/Tỵ/Ngọ/Mùi, Thân/Dậu/Tuất/Hợi; trâu, mèo Việt Nam, nền giấy kem, màu nước lavender-jade, không chữ/watermark.
2. Tranh hồ sen bình minh Việt Nam, cầu gỗ, núi xa, rắn nhỏ bên phải; nửa trái thoáng để đặt số ngày; không chữ/UI.
3. Tranh vườn tre/hồ sen sau mưa, ánh đèn ấm và sương tím, trầm tĩnh nhưng thân thiện, không biểu tượng tai họa; trái thoáng; không động vật/chữ/UI.
4. Tranh đồng quê Việt Nam, sông uốn, đồng lúa và tre/hoa bên phải, trời kem tím thoáng phía trái; trung tính, không dấu hiệu vận may hoặc tôn giáo; không động vật/chữ/UI.

---

Ngày: 2026-11-15. Trạng thái: bản xem trước cục bộ đã tạo và kiểm tra; CHƯA cập nhật vào Figma, CHƯA hoàn tất bước 3 theo yêu cầu đích Figma.

## Trở ngại đã xác minh

Khi kiểm tra thư viện và canvas để bắt đầu UI, Figma MCP trả thông báo đã hết tool call limit trên Starter. whoami xác nhận tài khoản dungfigma, team DŨNG PHÙNG ĐỨC's team, tier starter, seat View. Không có thao tác ghi UI bước 3 nào thành công trong file.

File đích vẫn là https://www.figma.com/design/DBfrO6T6QXhbRho7yUz9uJ/lich-van-nien. Hai phần UX trước đó giữ nguyên. Cần khôi phục quyền gọi MCP theo hạn mức/gói Figma hoặc nhập SVG thủ công; không tự nâng cấp hoặc thanh toán.

## Thiết kế đã chuẩn bị

- [Mở bộ xem trước](../../design/ui-v1/index.html).
- [Ảnh tổng hợp](../../design/ui-v1/preview-overview.png).
- [Ảnh lịch sáng](../../design/ui-v1/preview-calendar-light.png).
- [Gói SVG nhập Figma](../../design/ui-v1/figma-import-svg.zip).
- [Danh sách màn](../../design/ui-v1/manifest.json).
- [Nguồn dựng tài liệu UI](../../design/ui-v1/generate_ui.py), không phải code Android.

21 SVG độc lập: S01 lịch tháng, S11 chi tiết ngày, S02 cài đặt, S12 thông tin nguồn (sáng/tối); D01 nhập ngày có lỗi, D02 chọn giao diện (sáng/tối); S01 tablet (sáng/tối); trạng thái tải/offline/lỗi; lịch compact 360 px; toast T01; chọn ngày khác hôm nay; mẫu tháng nhuận đã đối chiếu phản hồi lưu.

## Quy chuẩn hình ảnh đề xuất

- Bảng màu baseline Material 3 tím: primary sáng #6750A4, onPrimary #FFFFFF; primary tối #D0BCFF, onPrimary #381E72. PrimaryContainer sáng #EADDFF, tối #4F378B. Đây là palette đề xuất, chưa phải biến đã tạo trong Figma.
- Nền sáng #FFFBFE, tối #141218; surface sáng #F3EDF7, tối #211F26. Chữ chính sáng #1D1B20, tối #E6E0E9.
- Font thiết kế Roboto; ảnh render cục bộ có thể dùng Noto Sans theo fallback vì môi trường chưa có Roboto. Cần dùng Roboto khi dựng native Figma và Compose.
- Phân cấp chữ: ngày dương nổi bật, ngày âm vừa; headline 22–30, nội dung 14–16, caption 11–12. Cần kiểm tra phóng chữ ở bản UI native và app.
- Card bo góc 24–28; nút capsule cao 48; khoảng cách theo nhịp 4/8. Calendar compact 360 dùng lưới cột 48 để giữ vùng chạm ngang; icon toolbar phải được bao vùng chạm tối thiểu 48 khi dựng native.
- Hôm nay viền primary; ngày chọn nền primary; mẫu chọn ngày 16 thể hiện riêng hai trạng thái này. Chú thích trợ năng/semantics bổ sung khi dựng native.
- Màu lỗi có nội dung chữ và nút Thử lại; không chỉ dùng màu để truyền trạng thái.

## Dữ liệu và phạm vi

- Lịch tháng đang minh họa tải một phần: chỉ ngày 15/11/2026 có ngày âm đã xác minh; các ô khác dùng —, không bịa ngày âm.
- Mẫu tháng nhuận dùng ngày 25/07/2025 = 01/06 nhuận năm Ất Tỵ, lấy từ evidence/lich247/leap-start.json; can chi Quý Mùi, tiết Đại Thử, trực Kiến, đại cát Không.
- Mẫu D01 hiển thị 30/02/2026 và thông báo xác thực lỗi.
- D02 đặt trên nền S02 Cài đặt theo flow; D01 đặt trên nền S01.
- SVG có vector và text, có thể kéo thả vào Figma sau khi giải nén. Việc nhập không tự tạo Auto Layout, component variants, variable bindings hoặc prototype. Khả năng giữ text chỉnh sửa cần kiểm tra sau nhập theo font/cách Figma xử lý SVG.
- Chưa dùng màu động theo hình nền; chưa thay đổi flow, chưa code app và chưa thực hiện bước 4 animation/prototype.

## Kiểm chứng và việc còn lại

Đã parse XML đủ 21 SVG, render bằng Chrome headless, xem ảnh tổng hợp, sửa dữ liệu mẫu tháng nhuận, lỗi nhập ngày và lưới 360 px. Không coi việc này là kiểm thử resize Android.

Trước hoàn tất bước 3: đưa thiết kế vào Figma, dựng component/biến/Auto Layout từ Material 3 library, xác nhận Roboto, kiểm tra cửa sổ trung gian/chữ lớn, bổ sung đủ trạng thái dialog/feedback theo UX_DESIGN.md và xin người dùng duyệt hình ảnh. Giới hạn API vẫn theo API_VALIDATION.md.
