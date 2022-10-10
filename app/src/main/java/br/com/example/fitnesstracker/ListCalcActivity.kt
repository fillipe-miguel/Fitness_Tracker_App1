package br.com.example.fitnesstracker


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.fitnesstracker.model.Calc
import java.lang.Thread
import java.text.SimpleDateFormat
import java.util.*


class ListCalcActivity : AppCompatActivity(), OnListClickListener {

	private lateinit var adapter: MainAdapter
	private lateinit var result: MutableList<Calc>
	private lateinit var rvListCalc: RecyclerView
	private lateinit var title: TextView

	@SuppressLint("NotifyDataSetChanged")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_list_calc)

		val type =
			intent?.extras?.getString("type") ?: throw IllegalStateException("Type not found!")

		result = mutableListOf<Calc>()
		adapter = MainAdapter(result, this)

		rvListCalc = findViewById(R.id.rv_list_calc)

		title = findViewById(R.id.list_title)


		rvListCalc.layoutManager = LinearLayoutManager(this)
		rvListCalc.adapter = adapter

		when (type) {
			"imc" -> title.text = getString(R.string.label_history_imc)
			"tmb" -> title.text = getString(R.string.label_history_tmb)
			else -> throw IllegalStateException("Type not found!")
		}


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

	override fun onLongClick(position: Int, calc: Calc) {
		AlertDialog.Builder(this).apply {
			setTitle(R.string.delete)
			setMessage(getString(R.string.delete_this))
			setNegativeButton(android.R.string.cancel) { _, _ -> }
			setPositiveButton(android.R.string.ok) { _, _ ->
				Thread {
					val app = application as App
					val dao = app.db.calcDao()

					// Passando para o banco !
					val response = dao.deleteById(id = calc.id)

					if (response > 0) {
						runOnUiThread {
							result.removeAt(position)
							adapter.notifyItemRemoved(position)
						}
					}

				}.start()
			}
			create()
			show()
		}
	}


	private inner class MainAdapter(
		private val listOfCalcs: List<Calc>,
		private val listener: OnListClickListener
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
				val view = view as LinearLayout

				tvDate = view.findViewById(R.id.tv_date)
				tvResult = view.findViewById(R.id.tv_result)

				val sdt = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
				val date = sdt.format(calcCurrent.createDate)

				tvDate.text = date
				if(calcCurrent.type == "tmb"){
					tvResult.text = getString(R.string.tmb_res, calcCurrent.res)
				}else {
					tvResult.text = getString(R.string.imc_res, calcCurrent.res)
				}


				view.setOnLongClickListener {
					listener.onLongClick(adapterPosition, calcCurrent)
					true
				}

			}

		}
	}


}





