package br.com.example.fitnesstracker.model

import androidx.room.*

@Dao
interface CalcDao {

	@Insert
	fun insert(calc: Calc)

	/*
	@Query
	@Update
	@Delete
	*/

}