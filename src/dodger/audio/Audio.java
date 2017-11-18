package dodger.audio;

import java.io.InputStream;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

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
	    InputStream inputStream = getClass().getResourceAsStream(snd + ".wav");

	    AudioStream audioStream = new AudioStream(inputStream);

	    AudioPlayer.player.start(audioStream);
	} catch (Exception e)
	{
	    System.out.println("Problem with audio player.");
	    e.printStackTrace();
	}
    }
}
