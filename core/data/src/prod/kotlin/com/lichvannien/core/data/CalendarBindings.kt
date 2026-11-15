package com.lichvannien.core.data
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import com.lichvannien.core.database.CalendarDatabase
import com.lichvannien.core.domain.CalendarRepository
import com.lichvannien.core.network.Lich247DataSource
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton
@Module @InstallIn(SingletonComponent::class) object CalendarBindings {
 @Provides @Singleton fun calendar(db: CalendarDatabase, clock: Clock, @ApplicationContext context: Context): CalendarRepository = OfflineCalendarRepository(db.days(), Lich247DataSource(), clock, AndroidNetworkMonitor(context))
}
