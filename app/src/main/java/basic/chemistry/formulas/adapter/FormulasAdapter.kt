package basic.chemistry.formulas.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import basic.chemistry.formulas.R
import basic.chemistry.formulas.entities.FormulaItem

class FormulasAdapter(private val dataList: MutableList<FormulaItem>) : RecyclerView.Adapter<FormulasAdapter.FormulasViewHolder>() {
    private val TAG = "debugTag"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormulasViewHolder  {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.formula_item, parent, false)
        return FormulasViewHolder(view)
    }

    interface OnItemClickListener {
        fun onItemClick(item: FormulaItem)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: FormulasAdapter.FormulasViewHolder, position: Int) {
        val item = dataList[position]

        holder.nameFormula.text = item.name
        holder.textFormula.text = item.text

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class FormulasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameFormula: TextView = itemView.findViewById(R.id.formula_name)
        val textFormula: TextView = itemView.findViewById(R.id.popup_formula_text)
    }
}