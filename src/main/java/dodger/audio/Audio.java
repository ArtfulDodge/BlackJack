package dodger.audio;


import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

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
    public void Play(String snd)
    {
        try
        {
            ClassLoader classLoader = new Audio().getClass().getClassLoader();

            URL sndUrl = classLoader.getResource(snd + ".wav");
            String fileLocation = sndUrl.toString().substring(6);

            File audioFile = new File(fileLocation);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip)AudioSystem.getLine(info);

            audioClip.open(audioStream);
            audioClip.start();
            Thread.sleep(700);
            audioClip.close();
            audioStream.close();

        } catch (Exception e)
        {
            System.err.println("Problem with audio player.");
            e.printStackTrace();
        }
    }
}
