package br.com.example.fitnesstracker

import br.com.example.fitnesstracker.model.Calc

interface OnListClickListener {
	fun onLongClick(position: Int, calc: Calc)
}