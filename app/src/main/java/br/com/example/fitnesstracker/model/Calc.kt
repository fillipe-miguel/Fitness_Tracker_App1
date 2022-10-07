package br.com.example.fitnesstracker.model

import androidx.room.*
import java.util.*

@Entity
data class Calc(
	@PrimaryKey(autoGenerate = true) val id: Int = 0,
	@ColumnInfo(name = "type") val type: String,
	@ColumnInfo(name = "res") val res: Double,
	@ColumnInfo(name = "created_date") val createDate: Date = Date()
)
