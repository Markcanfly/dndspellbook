package ga.markvar.dndspellbook.data

import androidx.room.*

@Dao
interface SpellDao {
    @Query("SELECT * FROM spell")
    fun getAll(): List<Spell>

    @Insert
    fun insert(spell: Spell): Long

    @Update
    fun update(spell: Spell)

    @Delete
    fun delete(spell: Spell)
}