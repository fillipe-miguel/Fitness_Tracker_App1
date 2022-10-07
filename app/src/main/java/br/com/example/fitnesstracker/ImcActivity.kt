package br.com.example.fitnesstracker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import kotlin.math.pow

class ImcActivity : AppCompatActivity() {

	private lateinit var editWeight: EditText
	private lateinit var editHeight: EditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_imc)

		editHeight = findViewById(R.id.edit_height)
		editWeight = findViewById(R.id.edit_weight)

		val btnSend: Button = findViewById(R.id.btn_send)

		btnSend.setOnClickListener {
			if (!validate()) {
				Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			val weight = editWeight.text.toString().toDouble()
			val height = editHeight.text.toString().toInt()

			val result = calculateImc(weight, height)

			val imcResponseId = imcResponse(result)

			AlertDialog.Builder(this).apply {
				setTitle(getString(R.string.imc_response, result))
				setMessage(imcResponseId)
				setPositiveButton(android.R.string.ok) { a, b -> /* TODO */ }
				create()
			}.show()

			// quote of code to hide keyboard !!
			val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

		}
	}

	@StringRes
	private fun imcResponse(result: Double): Int {
		return when {
			result < 15.0 -> R.string.imc_severely_low_weight
			result < 16.0 -> R.string.imc_very_low_weight
			result < 18.5 -> R.string.imc_low_weight
			result < 25.0 -> R.string.normal
			result < 30.0 -> R.string.imc_high_weight
			result < 35.0 -> R.string.imc_so_high_weight
			result < 40.0 -> R.string.imc_severely_high_weight
			else -> R.string.imc_extreme_weight
		}
	}

	private fun calculateImc(weight: Double, height: Int): Double {
		return weight / ((height / 100.0).pow(2.0))
	}

	private fun validate(): Boolean {
		return editWeight.text.toString().isNotEmpty() &&
				editHeight.text.toString().isNotEmpty() &&
				!editWeight.text.toString().startsWith("0") &&
				!editHeight.text.toString().startsWith("0")
	}

}