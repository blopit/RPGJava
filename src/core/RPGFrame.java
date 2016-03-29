package core;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Roleplaying.Background;

public class RPGFrame extends JFrame {

    public RPGFrame()
	{
    	//JPanel j = new JPanel();
    	//j.add(new Game());
        this.add(new Game());
        setTitle("Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(640, 480);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args)
	{
        new RPGFrame();
        Background b = new Background();
    }
}

