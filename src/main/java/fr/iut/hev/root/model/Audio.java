package fr.iut.hev.root.model;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;

/**
 * The {@code Audio} class allows creating and storing {@code Audio} objects
 * along with some important features like playing or pausing.
 *
 * @author Akram BARRA
 */
public class Audio {
    private final MediaPlayer mediaPlayer;

    /**
     * Prevents the garbage collector from removing {@code Audio} objects by storing them.
     * Also saves them here to be ready for each scene instead of loading everytime.
     */
    private static final ArrayList<Audio> registeredAudios = new ArrayList<>();

    /**
     * Initializes a newly created {@code Audio} object with an audio file attached to it.
     * Each new {@code Audio} object is automatically stored into the {@code registeredAudios} list.
     *
     * @param resourcePathFromAudioDir the file name or file path starting from the audio directory in resources
     * @author Akram BARRA
     */
    public Audio(String resourcePathFromAudioDir) {
        String path = getClass().getResource("/fr/iut/hev/terraria/terraria/audio/"
                .concat(resourcePathFromAudioDir)).toExternalForm();
        Media media = new Media(path);
        this.mediaPlayer = new MediaPlayer(media);
        registeredAudios.add(this);
    }

    /**
     * Starts the audio playback on the {@code MediaPlayer}.
     *
     * @param isLooped a boolean determining whether the audio should be played in loop
     * @author Akram BARRA
     */
    public void play(boolean isLooped) {
        if (isLooped) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        this.mediaPlayer.play();
    }

    /**
     * Stops completly the audio without deleting it from {@code registeredAudios}.
     * If it is being played again, it will restart from the beginning.
     *
     * @author Akram BARRA
     */
    public void stop() {
        mediaPlayer.stop();
    }

    /**
     * Pauses the audio so it can be resumed later.
     *
     * @author Akram BARRA
     */
    public void pause() {
        mediaPlayer.pause();
    }

    /**
     * Resumes the audio when it has been paused.
     *
     * @author Akram BARRA
     */
    public void resume() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    public void setVolume(double volume) {
        this.mediaPlayer.setVolume(volume);
    }

    /**
     * Returns a string with useful information about the audio.
     *
     * @return source, volume
     * @author Akram BARRA
     */
    @Override
    public String toString() {
        Media media = mediaPlayer.getMedia();
        StringBuilder sb = new StringBuilder();
        return sb.append("Source: ").append(media.getSource())
                .append("\nVolume: ").append(Double.valueOf(mediaPlayer.getVolume() * 100).intValue()).append("%")
                .toString();
    }

    /**
     * Removes the {@code Audio} object from the list of {@code registeredAudios}.
     *
     * @param audio
     * @author Akram BARRA
     */
    public static void remove(Audio audio) {
        audio.stop();
        registeredAudios.remove(audio);
    }
}