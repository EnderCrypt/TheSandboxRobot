package com.github.sandboxrobot;

import javax.swing.JFrame;


@SuppressWarnings("serial")
public class GameFrame extends JFrame
{
	GamePanel gamePanel;
	GameFrame(Simulation robot)
	{
		setTitle("Robot coder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// add panel
		gamePanel = new GamePanel(robot);
		getContentPane().add(gamePanel);
		pack();
		
		// finalize
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
