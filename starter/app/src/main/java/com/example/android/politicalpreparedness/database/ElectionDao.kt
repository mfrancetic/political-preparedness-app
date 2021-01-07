package com.example.android.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.flow.Flow

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(elections: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Query("SELECT * FROM election_table")
    fun getUpcomingElections(): Flow<List<Election>>

    @Query("SELECT * FROM election_table WHERE isSaved")
    fun getSavedElections(): Flow<List<Election>>

    @Query("SELECT * FROM election_table WHERE id = :electionId")
    fun getElectionById(electionId: Int): Flow<Election>

    @Query("DELETE FROM election_table WHERE id = :electionId")
    fun deleteElectionById(electionId: Int)

    @Query("DELETE FROM election_table")
    fun clearAllElections()
}