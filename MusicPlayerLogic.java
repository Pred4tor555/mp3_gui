import javazoom.jl.player.advanced.AdvancedPlayer;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerLogic {
    private List<Song> playlist;
    private int currentIndex;
    private AdvancedPlayer advancedPlayer;
    private Thread musicThread;

    public MusicPlayerLogic() {
        playlist = new ArrayList<>();
        currentIndex = -1;
    }

    public void addSong(Song song) {
        playlist.add(song);
    }

    public void loadSong(int index) {
        stopCurrentSong();

        currentIndex = index;
        Song currentSong = playlist.get(currentIndex);

        if (currentSong != null) {
            playCurrentSong(currentSong);
        }
    }

    private void stopCurrentSong() {
        if (advancedPlayer != null) {
            advancedPlayer.close();
            advancedPlayer = null;
            if (musicThread != null && musicThread.isAlive()) {
                musicThread.interrupt();
            }
        }
    }

    private void playCurrentSong(Song song) {
        try {
            FileInputStream fileInputStream = new FileInputStream(song.getFilePath());
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            advancedPlayer = new AdvancedPlayer(bufferedInputStream);
            startMusicThread();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startMusicThread() {
        musicThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    advancedPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        musicThread.start();
    }

    public void playNext() {
        if (currentIndex + 1 < playlist.size()) {
            loadSong(currentIndex + 1);
        }
    }

    public void playPrevious() {
        if (currentIndex >= 0) {
            loadSong(currentIndex - 1);
        }
    }

    public Song getCurrentSong() {
        if (currentIndex >= 0 && currentIndex < playlist.size()) {
            return playlist.get(currentIndex);
        }
        return null;
    }
}
