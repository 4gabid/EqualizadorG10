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
            volume = 0f,
            bass = -12f,
            middle = -12f,
            treble = -12f
        )
        assertEquals(0, profile.id)
        assertFalse(profile.isLastUsed)
    }

    @Test
    fun `should create Profile with specified values`() {
        val profile = Profile(
            id = 1,
            name = "Custom Profile",
            volume = 80f,
            bass = 7f,
            middle = 6f,
            treble = 9f,
            isLastUsed = true
        )
        assertEquals(1, profile.id)
        assertEquals("Custom Profile", profile.name)
        assertEquals(80f, profile.volume)
        assertEquals(7f, profile.bass)
        assertEquals(6f, profile.middle)
        assertEquals(9f, profile.treble)
        assertEquals(true, profile.isLastUsed)
    }


    @Test
    fun `should allow modifying mutable fields`() {
        val profile = Profile(
            name = "Initial Name",
            volume = 20f,
            bass = 0f,
            middle = 0f,
            treble = 0f
        )
        profile.name = "Updated Name"
        profile.volume = 70f
        profile.bass = 6f
        profile.middle = 8f
        profile.treble = 9f

        assertEquals("Updated Name", profile.name)
        assertEquals(70f, profile.volume)
        assertEquals(6f, profile.bass)
        assertEquals(8f, profile.middle)
        assertEquals(9f, profile.treble)
    }

    @Test
    fun `should correctly compare equality of Profiles`() {
        val profile1 = Profile(
            id = 1,
            name = "Test Profile",
            volume = 50f,
            bass = 0f,
            middle =.0f,
            treble = 0f
        )
        val profile2 = Profile(
            id = 1,
            name = "Test Profile",
            volume = 50f,
            bass = 0f,
            middle = 0f,
            treble = 0f
        )
        assertEquals(profile1, profile2)
    }

}

