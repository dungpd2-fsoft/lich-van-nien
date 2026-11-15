package com.lichvannien

import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.compose.ui.graphics.luminance
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.lichvannien.feature.settings.SettingsViewModel
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
 private val calendarModel by viewModels<CalendarViewModel>()
 private val settingsModel by viewModels<SettingsViewModel>()
 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState); enableEdgeToEdge()
  setContent {
   val theme by settingsModel.theme.collectAsStateWithLifecycle()
   val messages by settingsModel.messages.collectAsStateWithLifecycle()
   val first = messages.firstOrNull()
   val context = LocalContext.current
   LaunchedEffect(first?.id) { if(first != null) { Toast.makeText(context, first.text, Toast.LENGTH_SHORT).show(); settingsModel.handled(first.id) } }
   val owner = LocalLifecycleOwner.current
   DisposableEffect(owner) {
    val observer = LifecycleEventObserver { _, event -> if(event == Lifecycle.Event.ON_RESUME) calendarModel.refreshCurrent() }
    owner.lifecycle.addObserver(observer)
    onDispose { owner.lifecycle.removeObserver(observer) }
   }
   LvnTheme(theme) {
   val darkBars = MaterialTheme.colorScheme.background.luminance() < 0.5f
   SideEffect {
    WindowCompat.getInsetsController(window,window.decorView).apply {
     isAppearanceLightStatusBars = !darkBars
     isAppearanceLightNavigationBars = !darkBars
    }
   }
   val stack = rememberNavBackStack(CalendarKey)
   val back: () -> Unit = { if(stack.size > 1) stack.removeLastOrNull() }
   Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
    Column(Modifier.safeDrawingPadding()) {
     if(BuildConfig.FLAVOR == "demo") Text("Bản demo · chỉ có dữ liệu mẫu ở một số ngày", style = MaterialTheme.typography.labelSmall)
     NavDisplay(modifier = Modifier.weight(1f), backStack = stack, onBack = back, entryProvider = entryProvider {
      entry<CalendarKey> { CalendarRoute({stack.add(SettingsKey)}, {stack.add(DayKey(it))}, calendarModel) }
      entry<DayKey> { DayRoute(it.date, back, calendarModel, {stack.add(SettingsKey)}) }
      entry<SettingsKey> { SettingsRoute(back, {stack.add(AboutKey)}, settingsModel) }
      entry<AboutKey> { AboutRoute(back) }
     })
    }
   }
  } }
 }
}
