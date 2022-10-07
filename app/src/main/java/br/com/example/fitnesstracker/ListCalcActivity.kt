package br.com.example.fitnesstracker


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.fitnesstracker.model.Calc
import java.lang.Thread
import java.text.SimpleDateFormat
import java.util.*


class ListCalcActivity : AppCompatActivity() {
	@SuppressLint("NotifyDataSetChanged")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_list_calc)

		val type =
			intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found!")

		val result = mutableListOf<Calc>()

		val rvListCalc: RecyclerView = findViewById(R.id.rv_list_calc)

		val adapter = MainAdapter(result)

		rvListCalc.layoutManager = LinearLayoutManager(this)
		rvListCalc.adapter = adapter


		Thread {

			val app = application as App
			val dao = app.db.calcDao()
			val response = dao.getRegisterByType(type)


			runOnUiThread {
				result.addAll(response)
				adapter.notifyDataSetChanged()
			}
		}.start()

	}

	private inner class MainAdapter(
		private val listOfCalcs: List<Calc>
	) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
			val view = layoutInflater.inflate(R.layout.list_calc_item, parent, false)
			return MainViewHolder(view)
		}

		override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
			val calcCurrent = listOfCalcs[position]
			holder.bind(calcCurrent)
		}

		override fun getItemCount(): Int {
			return listOfCalcs.size
		}

		private inner class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

			private lateinit var tvDate: TextView
			private lateinit var tvResult: TextView

			fun bind(calcCurrent: Calc) {
				// aqui mudar as coisas de forma dinamica!
				tvDate = view.findViewById(R.id.tv_date)
				tvResult = view.findViewById(R.id.tv_result)

				val sdt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
				val date = sdt.format(calcCurrent.createDate)

				tvDate.text = date
				tvResult.text = getString(R.string.imc_res, calcCurrent.res)

			}
		}
	}
}


