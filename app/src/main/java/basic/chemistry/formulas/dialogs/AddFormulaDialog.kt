package basic.chemistry.formulas.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import basic.chemistry.formulas.AddFormulaItemListener
import basic.chemistry.formulas.R

class AddFormulaDialog(private val layoutResourceId: Int) : DialogFragment() {
    private val TAG = "debugTag"

    private var editTextNameValue: String = ""
    private var editTextTextValue: String = ""
    private lateinit var editTextName: TextView
    private lateinit var editTextText: TextView
    private lateinit var dialogListener: AddFormulaItemListener

    //  проверка, что активити, вызывающая DialogFragment, реализует интерфейс DialogListener
    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dialogListener = context as AddFormulaItemListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DialogListener")
        }
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getDialog()!!.window?.setBackgroundDrawableResource(R.drawable.round_corner_10)
        val view = inflater.inflate(layoutResourceId, container, false)

        val saveButton = view.findViewById<Button>(R.id.saveChangeButton)
        val cancelButton = view.findViewById<Button>(R.id.cancelChangeButton)
        saveButton.setOnClickListener {
            handleSaveButtonClicked()
            dismiss()
        }
        cancelButton.setOnClickListener {
            dismiss()
        }

        editTextName = view.findViewById(R.id.addChangeName)
        editTextName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextNameValue = s.toString()
                updateSaveButtonState(saveButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editTextText = view.findViewById(R.id.addChangeText)
        editTextText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editTextTextValue = s.toString()
                updateSaveButtonState(saveButton)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Изначально деактивируем кнопку "Сохранить"
        updateSaveButtonState(saveButton)

        return view
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    private fun updateSaveButtonState(saveButton: Button) {
        val isEditTextNameFilled = editTextNameValue.isNotEmpty()
        val isEditTextTextFilled = editTextTextValue.isNotEmpty()

        saveButton.isEnabled = isEditTextNameFilled && isEditTextTextFilled
    }


    private fun handleSaveButtonClicked() {
        if (editTextNameValue.isNotEmpty() && editTextTextValue.isNotEmpty()) {
            dialogListener.onUpdateResult(editTextNameValue, editTextTextValue)
        }
    }
}