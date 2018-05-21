package dodger.audio;

import javax.sound.sampled.*;

public class Audio
{
    // ---------------------------------------------------------------
    // Audio
    // ---------------------------------------------------------------
    public Audio()
    {

    }

    // ---------------------------------------------------------------
    // Plays the .wav with the given name
    // ---------------------------------------------------------------
    public static void Play(String snd)
    {
        try
        {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Audio.class.getResourceAsStream(snd + ".wav"));
            clip.open(inputStream);
            clip.start();
            clip.addLineListener(new LineListener() {
                public void update(LineEvent le) {
                    if (le.getType() == LineEvent.Type.STOP)
                        clip.close();
                }
            });
        } catch (Exception e)
        {
            System.err.println("Problem with audio player.");
            e.printStackTrace();
        }
    }
}
