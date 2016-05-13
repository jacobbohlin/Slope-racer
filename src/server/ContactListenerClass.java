package server;

import java.io.File;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

import javafx.scene.media.AudioClip;

public class ContactListenerClass implements ContactListener {
	private ClientConnector c;

	public ContactListenerClass(ClientConnector c) {
		this.c = c;
	}

	@Override
	public void beginContact(Contact contact) {
		c.sendSoundEffectCue("boinggg");

	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}
