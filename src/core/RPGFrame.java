package core;

import javax.swing.JFrame;

import roleplay.Background;

public class RPGFrame extends JFrame {

	public RPGFrame() {
		// JPanel j = new JPanel();
		// j.add(new Game());
		this.add(new Game());
		setTitle("Game");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
	}

	public static void main(String[] args) {
		new RPGFrame();
		Background b = new Background();
	}
}
