import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.grupo10.equalizadorg10.data.AppDatabase
import com.grupo10.equalizadorg10.data.Profile
import com.grupo10.equalizadorg10.data.ProfileDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {

    private lateinit var database: AppDatabase
    private lateinit var profileDao: ProfileDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        profileDao = database.profileDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndRetrieveProfile() = runBlocking {
        val profile = Profile(
            id = 1,
            name = "Rock",
            volume = 80f,
            bass = 70f,
            middle = 60f,
            treble = 50f,
            isLastUsed = false
        )
        profileDao.insert(profile)
        val retrievedProfile = profileDao.getProfileByName("Rock")
        assertEquals(profile, retrievedProfile)
    }

    @Test
    fun testGetAllProfiles() = runBlocking {
        val profiles = listOf(
            Profile(1, "Rock", 80f, 70f, 60f, 50f, false),
            Profile(2, "Jazz", 70f, 60f, 80f, 70f, false)
        )
        profiles.forEach { profileDao.insert(it) }
        val retrievedProfiles = profileDao.getAllProfiles()
        assertEquals(profiles.size, retrievedProfiles.size)
        assertEquals(profiles, retrievedProfiles)
    }

    @Test
    fun testSetAndClearLastUsedProfile() = runBlocking {
        val profile = Profile(1, "Rock", 80f, 70f, 60f, 50f, false)
        profileDao.insert(profile)
        profileDao.setLastUsed(1)
        val lastUsedProfile = profileDao.getLastUsedProfile()
        assertEquals(profile.copy(isLastUsed = true), lastUsedProfile)

        profileDao.clearLastUsed()
        val clearedLastUsedProfile = profileDao.getLastUsedProfile()
        assertNull(clearedLastUsedProfile)
    }

    @Test
    fun testUpdateProfile() = runBlocking {
        val profile = Profile(1, "Rock", 80f, 70f, 60f, 50f, false)
        profileDao.insert(profile)
        val updatedProfile = profile.copy(volume = 90f)
        profileDao.update(updatedProfile)
        val retrievedProfile = profileDao.getProfileByName("Rock")
        assertEquals(updatedProfile, retrievedProfile)
    }
}
