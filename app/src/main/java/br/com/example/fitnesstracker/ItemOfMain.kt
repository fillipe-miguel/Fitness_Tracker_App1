package br.com.example.fitnesstracker

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ItemOfMain(
	val id: Int,
	@DrawableRes val drawableId: Int,
	@StringRes val textStringId: Int,
	val color: Int
)
