package com.isamoilovs.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.isamoilovs.mygdx.game.TanksInJava;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Dimension sSize = Toolkit.getDefaultToolkit ().getScreenSize ();
		config.width = 1280;
		config.height = 720;
		new LwjglApplication(new TanksInJava(), config);
	}
}
