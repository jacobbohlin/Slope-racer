package client;

import java.io.File;
import java.util.TreeMap;

import javafx.scene.media.AudioClip;

public class SoundEffectHandler {
	private static TreeMap<String, AudioClip> audioClips;
	
	public SoundEffectHandler() {
		audioClips = new TreeMap<>();
		loadSoundEffects();
	}
	
	public static void playSound(String sound) {
//		System.out.println("inside soundeffecthandlerclass" + sound);
		if(sound.equals("boostpd") || sound.equals("conball") || sound.equals("expball") || sound.equals("soundtr")) {
			if(!audioClips.get(sound).isPlaying()) {
				audioClips.get(sound).play();
			}
		} else {
			audioClips.get(sound).play();
		}
	}
	
	private void loadSoundEffects() {
		audioClips.put("boostpd", new AudioClip(new File("boostpadspeedup.wav").toURI().toString()));
		audioClips.put("bwdeath", new AudioClip(new File("deathbybarbedwire1.wav").toURI().toString()));
		audioClips.put("conball", new AudioClip(new File("contractingball.wav").toURI().toString()));
		audioClips.put("expball", new AudioClip(new File("expandingball.wav").toURI().toString()));
		audioClips.put("boinggg", new AudioClip(new File("boing2.wav").toURI().toString()));
		audioClips.put("soundtr", new AudioClip(new File("soundtrack.wav").toURI().toString()));

	}
}
