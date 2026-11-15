package com.lichvannien.core.data

import android.content.Context
import com.lichvannien.core.database.CalendarDatabase
import com.lichvannien.core.datastore.ThemeStore
import com.lichvannien.core.domain.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object DataModule {
 @Provides @Singleton fun clock(): Clock = Clock.systemUTC()
 @Provides @Singleton fun database(@ApplicationContext context: Context) = CalendarDatabase.create(context)
 @Provides @Singleton fun themeStore(@ApplicationContext context: Context) = ThemeStore(context)
 @Provides @Singleton fun settings(store: ThemeStore): SettingsRepository = object : SettingsRepository {
  override val theme = store.theme
  override suspend fun setTheme(mode: com.lichvannien.core.model.ThemeMode) = store.set(mode)
 }
}
