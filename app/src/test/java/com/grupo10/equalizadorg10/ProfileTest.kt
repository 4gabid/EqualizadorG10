package com.grupo10.equalizadorg10

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.grupo10.equalizadorg10.data.Profile
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Test


class ProfileTest {

    @Test
    fun `should create Profile with default values`() {
        val profile = Profile(
            name = "Test Profile",
            volume = 0.5f,
            bass = 0.5f,
            middle = 0.5f,
            treble = 0.5f
        )
        assertEquals(0, profile.id)
        assertFalse(profile.isLastUsed)
    }

    @Test
    fun `should create Profile with specified values`() {
        val profile = Profile(
            id = 1,
            name = "Custom Profile",
            volume = 0.8f,
            bass = 0.7f,
            middle = 0.6f,
            treble = 0.9f,
            isLastUsed = true
        )
        assertEquals(1, profile.id)
        assertEquals("Custom Profile", profile.name)
        assertEquals(0.8f, profile.volume)
        assertEquals(0.7f, profile.bass)
        assertEquals(0.6f, profile.middle)
        assertEquals(0.9f, profile.treble)
        assertEquals(true, profile.isLastUsed)
    }


    @Test
    fun `should allow modifying mutable fields`() {
        val profile = Profile(
            name = "Initial Name",
            volume = 0.5f,
            bass = 0.5f,
            middle = 0.5f,
            treble = 0.5f
        )
        profile.name = "Updated Name"
        profile.volume = 0.7f
        profile.bass = 0.6f
        profile.middle = 0.8f
        profile.treble = 0.9f

        assertEquals("Updated Name", profile.name)
        assertEquals(0.7f, profile.volume)
        assertEquals(0.6f, profile.bass)
        assertEquals(0.8f, profile.middle)
        assertEquals(0.9f, profile.treble)
    }

    @Test
    fun `should correctly compare equality of Profiles`() {
        val profile1 = Profile(
            id = 1,
            name = "Test Profile",
            volume = 0.5f,
            bass = 0.5f,
            middle = 0.5f,
            treble = 0.5f
        )
        val profile2 = Profile(
            id = 1,
            name = "Test Profile",
            volume = 0.5f,
            bass = 0.5f,
            middle = 0.5f,
            treble = 0.5f
        )
        assertEquals(profile1, profile2)
    }

}

