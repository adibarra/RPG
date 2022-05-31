import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The AudioLoader class manages all audio. It allows control over volume of all clips.
 * It will also automatically load and import audioclips which are added into the clips arraylist.
 * 
 * @author Alec Ibarra
 */
public class AudioLoader {
	
	private static float volume = 1f;
	private static ArrayList<SoundClip> clips = new ArrayList<SoundClip>();
	private static ArrayList<SoundClip> BGMusic = new ArrayList<SoundClip>();
	
	/**
	 * Loads all audio clips in arraylist
	 */
	//TODO Clips that will be used must be added here, must have .au extension, add music tracks to BGMusic array
	public AudioLoader()
	{
		clips.add(new SoundClip("Bubbles"));
		clips.add(new SoundClip("Step1"));
		clips.add(new SoundClip("Step2"));
		clips.add(new SoundClip("SVSong1","StardewValleyOST-Spring(It's a Big World Outside)"));
		clips.add(new SoundClip("SVSong2","StardewValleyOST-Spring(The Valley Comes Alive)"));
		clips.add(new SoundClip("SVSong3","StardewValleyOST-Spring(Wild Horseradish Jam)"));
		
		BGMusic.add(clips.get(3));//SVSong1
		BGMusic.add(clips.get(4));//SVSong2
		BGMusic.add(clips.get(5));//SVSong3
	}
	
	public static void startBGMusic()
	{	
		new Thread()
		{
			public void run()
			{
				SoundClip clip = null;
				
				while(true)
				{
					if(clip == null)
					{
						clip = BGMusic.get(Tracker.r.nextInt(BGMusic.size()));
						play(clip.getClipName(),true);
					}
					else
					{
						if(clip.getAudioClip().getFramePosition() >= clip.getAudioClip().getFrameLength())
							clip = null;
						
					}
					Logic.delay(5000);
				}
			}
			
		}.start();
	}
	
	/**
	 * Plays the requested clip, if currently playing uses value of restartFromBeginning.
	 */
	public static void play(String clipName, boolean restartFromBeginning)
	{
		Clip clip = getAudio(clipName);
		
		((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f*(float)Math.log10(getVolume()));
		
		if(clip.isRunning())
			clip.stop();
		if(restartFromBeginning)//dont restart clip from begining if simply changing volume
			clip.setFramePosition(0);
		clip.start();
	}
	
	/**
	 * Stops the requested clip if currently playing
	 */
	public static void stop(String clipName)
	{
		Clip clip = getAudio(clipName);
		
		((FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(20f*(float)Math.log10(getVolume()));
		
		if(clip.isRunning())
		{
			clip.stop();
			clip.setFramePosition(0);
		}
	}
	
	/**
	 * Utility Method. Retrieves requested clip
	 * @return Requested SoundClip
	 */
	private static Clip getAudio(String clipName)
	{
		for(int k = 0; k < clips.size(); k++)
		{
			if(clipName.equalsIgnoreCase(clips.get(k).getClipName()))
				return clips.get(k).getAudioClip();
		}
		return null;
	}
	
	/**
	 * Closes all AudioClips to prevent memory leaks
	 */
	public static void cleanUp()
	{
		for(int k = 0; k < clips.size(); k++)
		{
			clips.get(k).getAudioClip().close();
		}
	}
	
	/**
	 * Sets AudioLoader clip player volume
	 */
	public static void setVolume(float volume) 
	{
		if(AudioLoader.volume != volume)
		{
			AudioLoader.volume = volume;
			
			for(int k = 0; k < clips.size(); k++)
			{
				if(clips.get(k).getAudioClip().isRunning())
				{
					play(clips.get(k).getClipName(),false);
				}
			}
		}
	}
	
	/**
	 * @return Current AudioLoader clip player volume
	 */
	public static float getVolume() 
	{
		return volume;
	}
	
	/**
	 * Utility Class. Used to keep clipName and audioClip tied together.
	 */
	private class SoundClip 
	{
		private Clip audioClip;
		private String clipName;
		
		public SoundClip(String clipName)
		{
			try
			{
				setClipName(clipName);
				setAudioClip(AudioSystem.getClip());
				getAudioClip().open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(clipName+".au")));
				
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | IllegalArgumentException e)
			{
				System.out.println("Failed to load: "+clipName+".au");
			}
		}
		
		public SoundClip(String clipName, String fileName)
		{
			try
			{
				setClipName(clipName);
				setAudioClip(AudioSystem.getClip());
				getAudioClip().open(AudioSystem.getAudioInputStream(getClass().getClassLoader().getResource(fileName+".au")));
				
			} catch (LineUnavailableException | IOException | UnsupportedAudioFileException | IllegalArgumentException e)
			{
				System.out.println("Failed to load: "+fileName+".au");
			}
		}

		public Clip getAudioClip() {
			return audioClip;
		}

		public void setAudioClip(Clip audioClip) {
			this.audioClip = audioClip;
		}

		public String getClipName() {
			return clipName;
		}

		public void setClipName(String clipName) {
			this.clipName = clipName;
		}
	}
}