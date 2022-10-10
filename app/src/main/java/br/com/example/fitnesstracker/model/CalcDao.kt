package br.com.example.fitnesstracker.model

import androidx.room.*

@Dao
interface CalcDao {

	@Insert
	fun insert(calc: Calc)

	@Query("SELECT * FROM Calc WHERE type = :type")
	fun getRegisterByType(type: String): List<Calc>


	@Query("DELETE FROM Calc WHERE id = :id")
	fun deleteById(id: Int): Int

	/*
	@Query
	@Update
	@Delete
	*/

}