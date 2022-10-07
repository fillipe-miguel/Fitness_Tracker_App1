package br.com.example.fitnesstracker.model

import androidx.room.TypeConverter
import java.util.*

object DateConverter{
	@TypeConverter
	fun toDate(date: Long?): Date? {
		return if (date != null) Date(date) else null
	}

	@TypeConverter
	fun fromDate(date: Date?): Long?{
		return date?.time
	}
}