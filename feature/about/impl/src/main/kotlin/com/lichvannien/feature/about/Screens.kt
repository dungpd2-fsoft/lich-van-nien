package com.lichvannien.feature.about

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun AboutRoute(onBack: () -> Unit) {
    val context = LocalContext.current
    val version = remember { runCatching { context.packageManager.getPackageInfo(context.packageName, 0).versionName }.getOrNull() ?: "Chưa có thông tin" }
    LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item { TextButton(onBack) { Text("‹ Cài đặt") }; Text("Thông tin ứng dụng", style = MaterialTheme.typography.headlineMedium) }
        item { Text("Lịch vạn niên", style = MaterialTheme.typography.headlineLarge); Text("Phiên bản $version") }
        item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Nguồn dữ liệu", style = MaterialTheme.typography.titleLarge)
            Text("LICH247 · Lịch Việt Nam UTC+7")
            Text("Ngày âm, can chi, tiết khí, trực và giờ hoàng đạo hiển thị theo nguồn. Thông tin tốt/xấu chỉ mang tính tham khảo.")
            Text("Minh họa con giáp và phong cảnh do AI tạo; ảnh không phải dự đoán vận khí.")
        } } }
        item { Button(onClick = {
            try { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://lich247.com/api")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)) }
            catch (_: ActivityNotFoundException) { Toast.makeText(context, "Không tìm thấy ứng dụng để mở liên kết", Toast.LENGTH_SHORT).show() }
            catch (_: SecurityException) { Toast.makeText(context, "Không thể mở liên kết trên thiết bị này", Toast.LENGTH_SHORT).show() }
        }) { Text("Mở trang nguồn LICH247") } }
        item { Text("Quyền riêng tư", style = MaterialTheme.typography.titleMedium); Text("App lưu dữ liệu lịch và chế độ giao diện trên thiết bị. Khi dùng dữ liệu trực tuyến, yêu cầu tra cứu ngày được gửi tới LICH247. Chưa có đăng nhập hoặc đồng bộ tài khoản. Chính sách phát hành sẽ được hoàn thiện trước release.") }
    }
}
