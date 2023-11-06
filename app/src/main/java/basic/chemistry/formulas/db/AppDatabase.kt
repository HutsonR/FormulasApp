package basic.chemistry.formulas.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import basic.chemistry.formulas.entities.FormulaItem

@Database(entities = [FormulaItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun formulaItemDao(): FormulaItemDao

    companion object {
        private const val DATABASE_NAME = "formulas_database"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }
    }
}
