package com.lichvannien.core.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import com.lichvannien.core.model.*

@Composable
fun ZodiacImage(zodiac: Zodiac?, modifier: Modifier = Modifier) {
    if (zodiac == null) { Box(modifier, contentAlignment = Alignment.Center) { Text("—") }; return }
    val atlas = ImageBitmap.imageResource(R.drawable.zodiac_atlas)
    val cell = atlas.width / 4
    val offset = IntOffset(zodiac.ordinal % 4 * cell, zodiac.ordinal / 4 * cell)
    Image(BitmapPainter(atlas, srcOffset = offset, srcSize = IntSize(cell, cell)), zodiac.label, modifier.clip(CircleShape), contentScale = ContentScale.Crop)
}

@Composable
fun DayHero(day: CalendarDay?, theme: DayTheme, modifier: Modifier = Modifier) {
    val asset = when {
        theme == DayTheme.HOANG_DAO && Zodiac.fromCanChi(day?.canChi?.day) == Zodiac.SNAKE -> R.drawable.day_hoang_dao_ty
        theme == DayTheme.QUIET -> R.drawable.day_quiet
        else -> R.drawable.day_neutral
    }
    Card(modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge) {
        Box(Modifier.fillMaxWidth().heightIn(min = 280.dp)) {
            Image(painterResource(asset), null, Modifier.matchParentSize(), contentScale = ContentScale.Crop, alignment = Alignment.CenterEnd)
            Surface(color = Color(0xD9FFFBFE), shape = MaterialTheme.shapes.large, modifier = Modifier.padding(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(day?.solar?.let { "THÁNG ${it.monthValue} · ${it.year}" } ?: "LỊCH VIỆT NAM", color = Color(0xFF352448), style = MaterialTheme.typography.labelLarge)
                    Text(day?.solar?.dayOfMonth?.toString() ?: "—", color = Color(0xFF352448), fontSize = 76.sp)
                    Text(day?.lunar?.let { "${it.day}/${it.month}${if (it.leap) " nhuận" else ""} âm lịch" } ?: "Chưa có thông tin", color = Color(0xFF352448))
                    Text(when(theme) { DayTheme.HOANG_DAO -> "Hoàng đạo · theo nguồn"; DayTheme.QUIET -> "Hắc đạo · theo nguồn"; DayTheme.NEUTRAL -> "Tranh minh họa" }, color = Color(0xFF352448), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
