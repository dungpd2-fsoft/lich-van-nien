package com.lichvannien.core.database

import android.content.Context
import androidx.room.*
import com.lichvannien.core.model.*
import java.time.Instant
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray

@Entity(tableName = "calendar_days", primaryKeys = ["source", "contract", "date"])
data class CalendarEntity(
    val date: String, val lunarDay: Int, val lunarMonth: Int, val lunarYear: Int, val leap: Boolean,
    val chiDay: String?, val chiMonth: String?, val chiYear: String?, val term: String?, val duty: String?,
    val hoangDao: Boolean?, val blackDay: Boolean?, val daiCat: Boolean?, val hours: String?, val fetchedAt: Long,
    val source: String = "lich247", val contract: Int = 1,
)
@Dao interface CalendarDao {
    @Query("SELECT * FROM calendar_days WHERE source='lich247' AND contract=1 AND date BETWEEN :start AND :end ORDER BY date")
    fun observe(start: String, end: String): Flow<List<CalendarEntity>>
    @Query("SELECT * FROM calendar_days WHERE source='lich247' AND contract=1 AND date=:date")
    suspend fun get(date: String): CalendarEntity?
    @Upsert suspend fun upsert(day: CalendarEntity)
}
@Database(entities = [CalendarEntity::class], version = 1, exportSchema = true)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun days(): CalendarDao
    companion object {
        fun create(context: Context): CalendarDatabase = Room.databaseBuilder(context,CalendarDatabase::class.java,"calendar.db").build()
    }
}
fun CachedDay.toEntity(): CalendarEntity = with(day) {
    CalendarEntity(solar.toString(),lunar.day,lunar.month,lunar.year,lunar.leap,canChi.day,canChi.month,canChi.year,solarTerm,duty,hoangDao,blackDay,daiCat,goodHours?.let { JSONArray(it).toString() },fetchedAt.toEpochMilli())
}
fun CalendarEntity.toModel(): CachedDay {
    val list = hours?.let { s -> val a = JSONArray(s); List(a.length()) { a.getString(it) } }
    return CachedDay(CalendarDay(LocalDate.parse(date),LunarDate(lunarDay,lunarMonth,lunarYear,leap),CanChi(chiDay,chiMonth,chiYear),term,duty,hoangDao,blackDay,daiCat,list),Instant.ofEpochMilli(fetchedAt))
}
