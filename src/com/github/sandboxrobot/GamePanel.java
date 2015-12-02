package com.github.sandboxrobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import com.github.sandboxrobot.entites.Clone;
import com.github.sandboxrobot.entites.Crate;
import com.github.sandboxrobot.entites.Wall;

@SuppressWarnings("serial")
public class GamePanel extends JPanel
{
	private GamePanelListener gamePanelListener;
	private final Simulation simulation;
	
	protected GuiButton playAndPauseButton;
	protected GuiButton moveScreenButton;
	protected GuiButton folowRobotButton;
	protected boolean paintBrushing = false;
	
	protected Class<? extends Entity> paintClass;
	
	protected Set<GuiButton> buttons = new HashSet<>();
	
	protected GuiButton focusedButton = null;
	
	private final int FPS = 60;
	
	GamePanel(Simulation simulation)
	{
		this.simulation = simulation;
		
		gamePanelListener = new GamePanelListener(simulation, this);
		addMouseMotionListener(gamePanelListener);
		addMouseListener(gamePanelListener);
		
		setPreferredSize(new Dimension(1200, 600));
		setBackground(Color.WHITE);
		
		registerButtons();
		focusedButton = moveScreenButton;
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				repaint();
			}
		}, (1000/FPS), (1000/FPS));
	}
	
	private void registerButtons()
	{
		// play and pause button
		playAndPauseButton = new GuiButton(simulation, new Point(10, 10), simulation.isPlaying()?GuiGraphics.PAUSE:GuiGraphics.PLAY, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				if (source.getGraphic().equals(GuiGraphics.PLAY))
					simulation.play();
				else
					simulation.pause();
			}
		});
		buttons.add(playAndPauseButton);
		
		
		// move screen button
		moveScreenButton = new GuiButton(simulation, new Point(32+(32*1), 10), GuiGraphics.MOVE_SCREEN, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = false;
			}
		});
		buttons.add(moveScreenButton);
		
		// move screen button
		buttons.add(new GuiButton(simulation, new Point(32+(32*2), 10), GuiGraphics.FIND, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				simulation.centerCamera();
			}
		}));
		
		// follow robot button
		folowRobotButton = new GuiButton(simulation, new Point(32+(32*3), 10), GuiGraphics.FOLLOW, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = false;
			}
		});
		buttons.add(folowRobotButton);
		
		// eraser button
		buttons.add(new GuiButton(simulation, new Point(32+(32*5), 10), GuiGraphics.ERASER, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = true;
				paintClass = null;
			}
		}));
		
		// place clone
		buttons.add(new GuiButton(simulation, new Point(32+(32*6), 10), GuiGraphics.CLONE, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = true;
				paintClass = Clone.class;
			}
		}));
		
		// place wall
		buttons.add(new GuiButton(simulation, new Point(32+(32*7), 10), GuiGraphics.WALL, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = true;
				paintClass = Wall.class;
			}
		}));
		
		// place box
		buttons.add(new GuiButton(simulation, new Point(32+(32*8), 10), GuiGraphics.CRATE, new Clickable()
		{
			@Override
			public void clicked(Simulation simulation, GuiButton source, Point point)
			{
				focusedButton = source;
				paintBrushing = true;
				paintClass = Crate.class;
			}
		}));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Dimension screenSize = simulation.getScreenSize();
		
		//follow cam
		if (focusedButton == folowRobotButton)
		{
			simulation.centerCamera();
		}
		//
		
		simulation.draw(g2d, screenSize.getSize());
		
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, screenSize.width, 50);
		g2d.setColor(Color.BLACK);
		g2d.drawLine(0, 50, screenSize.width, 50);
		
		for (GuiButton button : buttons)
		{
			button.draw(g2d);
			if (focusedButton == button)
			{
				Point location = button.getLocation();
				g2d.drawImage(GuiGraphics.SELECTED.getImage(), null, location.x, location.y);
			}
		}
	}
}
