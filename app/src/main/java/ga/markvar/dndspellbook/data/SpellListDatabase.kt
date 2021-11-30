package ga.markvar.dndspellbook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Spell::class], version = 1, exportSchema = true)

abstract class SpellListDatabase: RoomDatabase() {

    abstract fun spellDao(): SpellDao

    companion object {
        fun getDatabase(applicationContext: Context): SpellListDatabase {
            return Room.databaseBuilder(
                applicationContext,
                SpellListDatabase::class.java,
                "spell-list"
            ).createFromAsset("database/spells.db")
                .build()
        }
    }
}