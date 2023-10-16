import com.example.playlistmaker.AppSharedPreferences
import com.example.playlistmaker.Track

class SearchHistory(private val appSharedPreferences: AppSharedPreferences) {

    private val tracks: ArrayList<Track> = appSharedPreferences.getSearchHistory()

    fun getTracks(): ArrayList<Track> {
        return tracks
    }

    fun addTrack(track: Track) {
        if (tracks.isNotEmpty()) {
            for (item in tracks) {
                if (item.trackId == track.trackId) {
                    tracks.remove(item)
                }
            }
        }
        if (tracks.size >= TRACK_HISTORY_SIZE) {
            tracks.removeLast()
        }

        tracks.add(0, track)
        appSharedPreferences.putSearchHistory(tracks)
    }

    fun clear() {
        tracks.clear()
        appSharedPreferences.putSearchHistory(tracks)
    }

    private companion object {
        const val TRACK_HISTORY_SIZE = 10
    }
}