package basic.chemistry.formulas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import basic.chemistry.formulas.adapter.FormulasAdapter
import basic.chemistry.formulas.databinding.ActivityMainBinding
import basic.chemistry.formulas.db.AppDatabase
import basic.chemistry.formulas.dialogs.AddFormulaDialog
import basic.chemistry.formulas.entities.FormulaItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AddFormulaItemListener {
    private val TAG = "debugTag"
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FormulasAdapter
    private var dataList: MutableList<FormulaItem> = mutableListOf()
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getInstance(this)
        setFormulasRecycler()
        GlobalScope.launch {
            // Добавление при запуске всех элементов в историю
            val historyItems = database.formulaItemDao().getAll()

            runOnUiThread {
                dataList.clear()
                dataList.addAll(historyItems)
                adapter.notifyDataSetChanged()
                Log.v(TAG, "Restore")
            }
        }
        addFormulaButton()
        search()
    }

    private fun search() {
        val search = binding.searchInput
        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                GlobalScope.launch {
                    val searchItems = database.formulaItemDao().find(text)
                    dataList.clear()
                    dataList.addAll(searchItems)
                    runOnUiThread {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }

    private fun addFormulaItem(name: String, text: String) {
        val formulaItem = FormulaItem(name = name, text = text)
        // Save the element in the database on a background thread
        GlobalScope.launch {
            database.formulaItemDao().insert(formulaItem)
        }

        // Update the UI elements on the main thread
        runOnUiThread {
            dataList.add(0, formulaItem)
            adapter.notifyItemInserted(0)
        }
    }

    override fun onUpdateResult(name: String, text: String) {
        // Implement this function to add a new formula item with the received data
        addFormulaItem(name, text)
    }

    private fun addFormulaButton() {
        binding.addFormulaButton.setOnClickListener {
            AddFormulaDialog(R.layout.dialog_add_formula).show(supportFragmentManager, "add fragment")
        }
    }

    private fun showFormulaPopup(formulaItem: FormulaItem) {
        val dialogView = layoutInflater.inflate(R.layout.popup_formula_item, null)
        val formulaText = dialogView.findViewById<TextView>(R.id.popup_formula_text)
        formulaText.text = formulaItem.text

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun setFormulasRecycler() {
        recyclerView = binding.recycleFormulas
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = FormulasAdapter(dataList)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : FormulasAdapter.OnItemClickListener {
            override fun onItemClick(item: FormulaItem) {
                showFormulaPopup(item)
            }
        })
    }
}