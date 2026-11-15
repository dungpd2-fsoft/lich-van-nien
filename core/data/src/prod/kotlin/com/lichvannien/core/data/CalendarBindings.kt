package com.lichvannien.core.data
import com.lichvannien.core.database.CalendarDatabase
import com.lichvannien.core.domain.CalendarRepository
import com.lichvannien.core.network.Lich247DataSource
import dagger.*
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.Clock
import javax.inject.Singleton
@Module @InstallIn(SingletonComponent::class) object CalendarBindings {
 @Provides @Singleton fun calendar(db: CalendarDatabase, clock: Clock): CalendarRepository = OfflineCalendarRepository(db.days(), Lich247DataSource(), clock)
}
