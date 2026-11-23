package mx.udg.cucea.andynails.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar
import java.util.Locale

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, y, m, d ->
        val formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", y, m + 1, d)
        onDateSelected(formattedDate)
    }, year, month, day).show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, h, m ->
        val formattedTime = String.format(Locale.getDefault(), "%02d:%02d:00", h, m)
        onTimeSelected(formattedTime)
    }, hour, minute, true).show()
}
