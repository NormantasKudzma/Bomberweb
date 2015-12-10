package testing;

import java.applet.Applet;

import javax.swing.JApplet;
import javax.swing.JFrame;

import junit.framework.TestCase;
import applet.main.Bomberapplet;

public class AppletTest extends TestCase{
	public Bomberapplet startApplet(){
		try {
			JApplet panel = new JApplet();
			JFrame frame = new JFrame();
			frame.setSize(640, 640);
			frame.add(panel);
			panel.setVisible(true);
			frame.setVisible(true);
			Bomberapplet a = new Bomberapplet();
			Class applet2 = a.getClass();
			Applet appletToLoad = (Applet)applet2.newInstance();
			panel.add(appletToLoad);
			Thread.sleep(2000);
			appletToLoad.init();
			appletToLoad.start();
			Thread.sleep(2000);
			return a;
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
