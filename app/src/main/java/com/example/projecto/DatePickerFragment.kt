package com.example.projecto

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Locale

class DatePickerFragment(private val myView: TextView?,private val amountBtn:Button) : DialogFragment() , DatePickerDialog.OnDateSetListener{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker.
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it.
        return DatePickerDialog(requireContext(), this, year, month, day)

    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        myView?.text = formatDate(year, month + 1, day)
        val rupee = String.format(Locale.US,"Donate %.2f",100.00)
        amountBtn.text = rupee
    }

    private fun formatDate(year: Int, month: Int, day: Int): String {
        // Format the date as "yyyy-MM-dd"
        return "$year-${String.format("%02d", month)}-${String.format("%02d", day)}"
    }
}
