package br.com.example.fitnesstracker


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

	//	private lateinit var btnImc: LinearLayout
	private lateinit var rvMain: RecyclerView

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		setContentView(R.layout.activity_main)

		val listOfItems = mutableListOf<ItemOfMain>()

		listOfItems.add(
			ItemOfMain(
				id = 1,
				drawableId = R.drawable.ic_reshot_icon_health,
				textStringId = R.string.label_imc,
				color = R.color.FFF0C48A
			)
		)

		listOfItems.add(
			ItemOfMain(
				id = 2,
				drawableId = R.drawable.ic_reshot_icon_health,
				textStringId = R.string.label_tmb,
				color = R.color.FFF0C48A
			)
		)

		listOfItems.add(
			ItemOfMain(
				id = 3,
				drawableId = R.drawable.ic_reshot_icon_health,
				textStringId = R.string.label_history_imc,
				color = R.color.FFF0C48A
			)
		)

		listOfItems.add(
			ItemOfMain(
				id = 4,
				drawableId = R.drawable.ic_reshot_icon_health,
				textStringId = R.string.label_history_tmb,
				color = R.color.FFF0C48A
			)
		)

		rvMain = findViewById(R.id.rv_main)

		rvMain.adapter = MainAdapter(listOfItems){
			when (it) {
				1 -> {
					Log.d("click", it.toString())
					val i = Intent(this@MainActivity, ImcActivity::class.java)
					startActivity(i)
				}
				2 -> {
					val i = Intent(this@MainActivity, TmbActivity::class.java)
					startActivity(i)
				}
				3 -> {
					val i = Intent(this, ListCalcActivity::class.java)
					i.putExtra("type", "imc")
					startActivity(i)
				}
				4 -> {
					val i = Intent(this, ListCalcActivity::class.java)
					i.putExtra("type", "tmb")
					startActivity(i)
				}
			}
		}

		rvMain.layoutManager = GridLayoutManager(this, 2)

	}


	private inner class MainAdapter(
		private val listOfItems: MutableList<ItemOfMain>,
		private val onItemClickListener: (Int) -> Unit
	) :
		RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

		//Responsavel para informar o xml da celula especifica
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
			val view = layoutInflater.inflate(R.layout.main_item, parent, false)
			return MainViewHolder(view)
		}

		//Disparado toda vez que ouver uma rlagem na tela e for necessario trocar o conteudo da celula
		override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
			val itemCurrent = listOfItems[position]
			holder.bind(itemCurrent)
		}

		// Informar quantas celulas esse item terá
		override fun getItemCount(): Int {
			return listOfItems.size
		}

		// a classe da celula em si
		private inner class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

			private lateinit var layout: LinearLayout
			private lateinit var img: ImageView
			private lateinit var text: TextView


			fun bind(itemCurrent: ItemOfMain) {

				layout = view.findViewById(R.id.item_container)
				img = view.findViewById(R.id.item_img_icon)
				text = view.findViewById(R.id.item_txt_name)

				layout.setBackgroundColor(itemCurrent.color)
				img.setImageResource(itemCurrent.drawableId)
				text.setText(itemCurrent.textStringId)

				layout.setOnClickListener {
					onItemClickListener.invoke(itemCurrent.id)
				}

			}
		}
	}


}