package basic.chemistry.formulas.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import basic.chemistry.formulas.entities.FormulaItem

@Dao
interface FormulaItemDao {
    @Insert
    suspend fun insert(formulaItem: FormulaItem)

    @Query("SELECT * FROM formula_items ORDER BY id DESC")
    suspend fun getAll(): List<FormulaItem>

    @Query("SELECT * FROM formula_items WHERE name LIKE '%' || :search || '%'")
    fun find(search: String?): List<FormulaItem>

    @Query("DELETE FROM formula_items")
    suspend fun deleteAll()
}
