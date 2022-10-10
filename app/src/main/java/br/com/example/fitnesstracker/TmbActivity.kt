package br.com.example.fitnesstracker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import br.com.example.fitnesstracker.model.Calc

class TmbActivity : AppCompatActivity() {

	private lateinit var lifestyle: AutoCompleteTextView
	private lateinit var editWeight: EditText
	private lateinit var editHeight: EditText
	private lateinit var editAge: EditText

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_tmb)

		editHeight = findViewById(R.id.edit_tmb_height)
		editWeight = findViewById(R.id.edit_tmb_weight)
		editAge = findViewById(R.id.edit_tmb_age)
		lifestyle = findViewById(R.id.auto_lifestyle)

		val btnSend: Button = findViewById(R.id.btn_tmb_send)

		val items = resources.getStringArray(R.array.tmb_lifestyle)
		lifestyle.setText(items.first())
		val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
		lifestyle.setAdapter(adapter)

		btnSend.setOnClickListener {
			if (!validate()) {
				Toast.makeText(this, R.string.fields_messages, Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}

			val weight = editWeight.text.toString().toDouble()
			val height = editHeight.text.toString().toInt()
			val age = editAge.text.toString().toInt()

			val result = calculateTmb(weight, height, age)

			val response = tmbRequest(result)


			AlertDialog.Builder(this@TmbActivity)
				.setMessage(getString(R.string.tmb_response, response))
				.setPositiveButton(android.R.string.ok) { _, _ -> /* TODO */ }
				.setNegativeButton(R.string.save) { _, _ ->
					Thread {
						val app = application as App
						val dao = app.db.calcDao()

						// Passando para o banco !
						dao.insert(Calc(type = "tmb", res = response))

						runOnUiThread {
							openListActivity()
						}
					}.start()
				}
				.create()
				.show()

			// quote of code to hide keyboard !!
			val service = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			service.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
		}

	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.menu, menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.menu_search) {
			finish()
			openListActivity()
		}
		return super.onOptionsItemSelected(item)
	}

	private fun openListActivity() {
		val i = Intent(this@TmbActivity, ListCalcActivity::class.java)
		i.putExtra("type", "tmb")
		startActivity(i)
	}

	private fun calculateTmb(weight: Double, height: Int, age: Int): Double {
		return 66 + (13.8 * weight) + (5 * height) - (6.8 * age)
	}

	private fun tmbRequest(result: Double): Double {
		val items = resources.getStringArray(R.array.tmb_lifestyle)

		return when {
			lifestyle.text.toString() == items[0] -> result * 1.2
			lifestyle.text.toString() == items[1] -> result * 1.55
			lifestyle.text.toString() == items[2] -> result * 1.725
			lifestyle.text.toString() == items[3] -> result * 1.9
			else -> 0.0
		}

	}

	private fun validate(): Boolean {
		return editWeight.text.toString().isNotEmpty() &&
				editHeight.text.toString().isNotEmpty() &&
				editAge.text.toString().isNotEmpty() &&
				!editWeight.text.toString().startsWith("0") &&
				!editHeight.text.toString().startsWith("0") &&
				!editAge.text.toString().startsWith("0")
	}

}