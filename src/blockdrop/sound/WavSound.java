package blockdrop.sound;

import blockdrop.utils.GLog;

import javax.sound.sampled.*;
import java.util.concurrent.locks.ReentrantLock;

public class WavSound extends Sound {

    private ReentrantLock mutex = new ReentrantLock();

    public WavSound(String path) {
        super(path);
    }

    @Override
    public void setUp() {
        //Do nothing.
    }

    private Clip setUpClip() {
        mutex.lock();
        AudioInputStream audioIn = null;
        try {
            audioIn = AudioSystem.getAudioInputStream(getResource());
        } catch (Exception e) {
            GLog.info("Unabl to load audio wav.");
        }
        if (audioIn == null) {
            mutex.unlock();
            return null;
        }
        final Clip clip;
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            GLog.info("Line unavailable launching wav.");
            mutex.unlock();
            return null;
        }
        try {
            clip.open(audioIn);
        } catch (Exception e) {
            GLog.info("Unable to open clip for wav.");
            mutex.unlock();
            return null;
        }
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                mutex.unlock();
                clip.close();
            }
        });
        return clip;
    }

    @Override
    public synchronized void play() {
        Clip clip = setUpClip();
        if (clip == null) {
            return;
        }
        clip.start();
    }
}
