package com.lichvannien

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.*
import androidx.navigation3.ui.NavDisplay
import com.lichvannien.core.designsystem.LvnTheme
import com.lichvannien.feature.calendar.*
import com.lichvannien.feature.calendar.api.*
import com.lichvannien.feature.settings.SettingsRoute
import com.lichvannien.feature.settings.api.SettingsKey
import com.lichvannien.feature.about.AboutRoute
import com.lichvannien.feature.about.api.AboutKey
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 private val calendarModel by viewModels<CalendarViewModel>()
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState); enableEdgeToEdge()
  setContent { LvnTheme {
   val stack = rememberNavBackStack(CalendarKey)
   val back: () -> Unit = { if(stack.size > 1) stack.removeLastOrNull() }
   Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(Modifier.safeDrawingPadding()) {
     if(BuildConfig.FLAVOR == "demo") Text("Bản demo · chỉ có dữ liệu mẫu ở một số ngày", style = MaterialTheme.typography.labelSmall)
     NavDisplay(backStack = stack, onBack = back, entryProvider = entryProvider {
      entry<CalendarKey> { CalendarRoute({stack.add(SettingsKey)}, {stack.add(DayKey(it))}, calendarModel) }
      entry<DayKey> { DayRoute(it.date, back, calendarModel) }
      entry<SettingsKey> { SettingsRoute(back, {stack.add(AboutKey)}) }
      entry<AboutKey> { AboutRoute(back) }
     })
    }
   }
  } }
 }
}
