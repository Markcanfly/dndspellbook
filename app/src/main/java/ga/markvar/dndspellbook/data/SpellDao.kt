package ga.markvar.dndspellbook.data

import androidx.room.*

@Dao
interface SpellDao {
    @Query("SELECT * FROM spell")
    fun getAll(): List<Spell>

    @Query("SELECT * FROM spell WHERE id=:id")
    fun get(id: Long): Spell?

    @Query("SELECT * FROM spell WHERE index_name=:index_name")
    fun get(index_name: String): Spell?

    @Insert
    fun insert(spell: Spell): Long

    @Update
    fun update(spell: Spell)

    @Delete
    fun delete(spell: Spell)
}