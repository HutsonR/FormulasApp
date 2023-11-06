package basic.chemistry.formulas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "formula_items")
data class FormulaItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val text: String,
)