package br.com.example.fitnesstracker.model

import androidx.room.*

@Dao
interface CalcDao {

	@Insert
	fun insert(calc: Calc)

	@Query("SELECT * FROM Calc WHERE type = :type")
	fun getRegisterByType(type: String): List<Calc>

	/*
	@Query
	@Update
	@Delete
	*/

}