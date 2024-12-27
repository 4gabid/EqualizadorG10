package com.grupo10.equalizadorg10.data

import androidx.room.*

@Dao
interface ProfileDao {
    @Insert
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Delete
    suspend fun delete(profile: Profile)

    @Query("SELECT * FROM profiles")
    suspend fun getAllProfiles(): List<Profile>

    @Query("SELECT * FROM profiles WHERE name = :profileName LIMIT 1")
    suspend fun getProfileByName(profileName: String): Profile?

    @Query("SELECT * FROM profiles WHERE isLastUsed = 1 LIMIT 1")
    suspend fun getLastUsedProfile(): Profile?

    @Query("UPDATE profiles SET isLastUsed = 0")
    suspend fun clearLastUsed()

    @Query("UPDATE profiles SET isLastUsed = 1 WHERE id = :profileId")
    suspend fun setLastUsed(profileId: Int)
}

