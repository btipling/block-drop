package blockdrop.sound;

import blockdrop.utils.GLog;

import javax.sound.sampled.*;

public class WavSound extends Sound {

    public WavSound(String path) {
        super(path);
    }

    @Override
    public void setUp() {
        //Do nothing.
    }


    @Override
    public  void play() {
        Thread playThread = new Thread() {
            @Override
            public void run() {
                AudioInputStream audioIn = null;
                try {
                    audioIn = AudioSystem.getAudioInputStream(getResource());
                } catch (Exception e) {
                    GLog.info("Unabl to load audio wav.");
                }
                if (audioIn == null) {
                    return;
                }
                final Clip clip;
                try {
                    clip = AudioSystem.getClip();
                } catch (LineUnavailableException e) {
                    GLog.info("Line unavailable launching wav.");
                    return;
                }
                try {
                    clip.open(audioIn);
                } catch (Exception e) {
                    GLog.info("Unable to open clip for wav.");
                    return;
                }
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        GLog.info("clip stopped");
                        clip.close();
                    }
                });
                clip.start();
            }
        };
        playThread.start();

    }
}
