package blockdrop.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MP3Sound extends Sound {
    private MediaPlayer mediaPlayer;

    public MP3Sound(String path) {
        super(path);
    }

    @Override
    public void setUp() {
        Media media = new Media(getPath());
        mediaPlayer = new MediaPlayer(media);
    }

    @Override
    public void play() {
        if (mediaPlayer == null) {
            return;
        }
        mediaPlayer.play();
    }
}
