package Akash;

import javax.sound.sampled.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// ==================== OPTIONAL SOUND ====================
public class SoundEffects implements AutoCloseable {
    public boolean enabled = false;
    private boolean musicWanted;
    private boolean tickingWanted;
    private boolean audioUnavailable;
    private Clip music;
    private Clip ticking;
    private final List<Clip> effects = new CopyOnWriteArrayList<>();

    public void setEnabled(boolean value) {
        enabled = value;
        if (!enabled) {
            stopLoop(music);
            stopLoop(ticking);
            for (Clip effect : effects) {
                effect.stop();
                effect.close();
            }
            effects.clear();
            return;
        }
        if (musicWanted) startMusic();
        if (tickingWanted) startTicking();
    }

    // Quiet background melody that repeats during gameplay.
    public void startMusic() {
        musicWanted = true;
        if (!enabled || audioUnavailable) return;
        if (music == null) {
            music = makeClip(new double[]{330, 392, 440, 392, 294, 330, 262, 0, 330, 392, 494, 440, 392, 294, 330, 0}, new int[]{400, 400, 400, 400, 400, 400, 600, 200, 400, 400, 400, 400, 400, 400, 600, 200}, 0.07);
        }
        startLoop(music);
    }

    public void stopMusic() {
        musicWanted = false;
        stopLoop(music);
    }

    // One tick each second during the final 10 seconds.
    public void startTicking() {
        tickingWanted = true;
        if (!enabled || audioUnavailable) return;
        if (ticking == null) {
            ticking = makeClip(new double[]{1200, 0}, new int[]{45, 955}, 0.18);
        }
        startLoop(ticking);
    }

    public void stopTicking() {
        tickingWanted = false;
        stopLoop(ticking);
    }

    // Existing correct/wrong-answer sounds.
    public void play(boolean correct) {
        if (correct) {
            playOnce(new double[]{660, 880}, new int[]{100, 150}, 0.22);
        } else {
            playOnce(new double[]{220, 165}, new int[]{130, 180}, 0.22);
        }
    }

    // Rising celebration tune.
    public void victory() {
        playOnce(new double[]{523, 659, 784, 1047, 0, 784, 1047}, new int[]{140, 140, 140, 400, 100, 180, 600}, 0.25);
    }

    // Descending game-over tune.
    public void gameOver() {
        playOnce(new double[]{392, 330, 294, 247, 196}, new int[]{220, 220, 220, 300, 650}, 0.24);
    }

    private void playOnce(double[] notes, int[] durations, double volume) {
        if (!enabled || audioUnavailable) return;
        Clip clip = makeClip(notes, durations, volume);
        if (clip == null) return;
        effects.add(clip);
        clip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.STOP) {
                effects.remove(clip);
                clip.close();
            }
        });
        clip.start();
    }

    private void startLoop(Clip clip) {
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    private void stopLoop(Clip clip) {
        if (clip != null) {
            clip.stop();
        }
    }

    private Clip makeClip(double[] notes, int[] durations, double volume) {
        final int sampleRate = 22050;
        int totalSamples = 0;
        for (int duration : durations) {
            totalSamples += duration * sampleRate / 1000;
        }
        // Two bytes per sample: 16-bit mono sound.
        byte[] data = new byte[totalSamples * 2];
        int position = 0;
        for (int note = 0; note < notes.length; note++) {
            int count = durations[note] * sampleRate / 1000;
            for (int i = 0; i < count; i++) {
                // Fade note edges to avoid clicking.
                double fade = Math.min(1.0, Math.min(i, count - 1 - i) / 150.0);
                double wave = notes[note] == 0
                        ? 0
                        : Math.sin(2 * Math.PI * notes[note] * i / sampleRate);
                short sample = (short) (wave * 32767 * volume * fade);
                data[position++] = (byte) (sample & 0xff);
                data[position++] = (byte) ((sample >> 8) & 0xff);
            }
        }
        AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
        Clip clip = null;
        try {
            clip = AudioSystem.getClip();
            clip.open(format, data, 0, data.length);
            return clip;
        } catch (LineUnavailableException | IllegalArgumentException | SecurityException e) {
            if (clip != null) {
                clip.close();
            }
            if (!audioUnavailable) {
                ConsoleUI.message("Audio is unavailable. The game will continue.", Colors.YELLOW);
            }
            audioUnavailable = true;
            return null;
        }
    }

    @Override
    public void close() {
        setEnabled(false);
        if (music != null) music.close();
        if (ticking != null) ticking.close();
    }
}
