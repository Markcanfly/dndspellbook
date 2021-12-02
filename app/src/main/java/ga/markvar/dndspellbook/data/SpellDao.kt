package ga.markvar.dndspellbook.data

import androidx.room.*

@Dao
interface SpellDao {
    companion object {
        val ALPHABETICAL = "name"
        val LEVEL = "level"
    }

    @Query("SELECT * FROM spell")
    fun getAll(): List<Spell>

    @Query("SELECT * FROM spell ORDER BY level ASC")
    fun getAllSortedByLevel(): List<Spell>

    @Query("SELECT * FROM spell WHERE id=:id")
    fun get(id: Long): Spell?

    @Query("SELECT * FROM spell WHERE index_name=:index_name")
    fun get(index_name: String): Spell?

    @Query("SELECT * FROM spell WHERE (classes LIKE '%'||:query||'%') OR (name LIKE '%'||:query||'%') OR (level LIKE :query)")
    fun search(query: String): List<Spell>

    @Query("SELECT * FROM spell WHERE (classes LIKE '%'||:query||'%') OR (name LIKE '%'||:query||'%') OR (level LIKE :query) ORDER BY level ASC")
    fun searchSortedByLevel(query: String): List<Spell>

    @Insert
    fun insert(spell: Spell): Long

    @Update
    fun update(spell: Spell)

    @Delete
    fun delete(spell: Spell)
}