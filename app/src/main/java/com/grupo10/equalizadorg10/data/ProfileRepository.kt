import android.content.Context
import com.grupo10.equalizadorg10.data.AppDatabase
import com.grupo10.equalizadorg10.data.Profile
import com.grupo10.equalizadorg10.data.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(private val context: Context) {
    private val profileDao: ProfileDao = AppDatabase.getDatabase(context).profileDao()

    suspend fun getAllProfiles(): List<Profile> {
        return withContext(Dispatchers.IO) {
            profileDao.getAllProfiles()
        }
    }

    suspend fun getProfileByName(profileName: String): Profile? {
        return withContext(Dispatchers.IO) {
            profileDao.getProfileByName(profileName)
        }
    }

    suspend fun insert(profile: Profile) {
        withContext(Dispatchers.IO) {
            profileDao.insert(profile)
        }
    }

    suspend fun update(profile: Profile) {
        withContext(Dispatchers.IO) {
            profileDao.update(profile)
        }
    }

    suspend fun getLastUsedProfile(): Profile? {
        return withContext(Dispatchers.IO) {
            profileDao.getLastUsedProfile()
        }
    }

    suspend fun setLastUsedProfile(profileId: Int) {
        withContext(Dispatchers.IO) {
            profileDao.clearLastUsed()
            profileDao.setLastUsed(profileId)
        }
    }


    suspend fun deleteAllProfiles() {
        withContext(Dispatchers.IO) {
            profileDao.deleteAllProfiles()
        }
    }
}
