package dodger.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
        } catch (Exception e)
        {
            System.err.println("Problem with audio player.");
            e.printStackTrace();
        }
    }
}
