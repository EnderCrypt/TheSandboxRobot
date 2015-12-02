package com.github.enderbot;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;

public class GamePanelListener implements MouseMotionListener, MouseListener
{
	Point mousePosition = new Point();
	Simulation simulation;
	GamePanel gamePanel;
	
	GamePanelListener(Simulation simulation, GamePanel gamePanel)
	{
		this.simulation = simulation;
		this.gamePanel = gamePanel;
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		tryTools(e);
	}
	
	@Override
	public void mouseMoved(MouseEvent e)
	{
		mousePosition = e.getPoint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if (e.getY() <= 50)
		{
			for (GuiButton button : gamePanel.buttons)
			{
				if (button.click(e.getPoint()))
				{
					return;
				}
			}
		}
		else
		{
			tryTools(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// TODO Auto-generated method stub
	}
	
	public void tryTools(MouseEvent e)
	{
		if (e.getY() <= 50)
			return;
		if (gamePanel.focusedButton == gamePanel.moveScreenButton)
		{
			Point drag = new Point();
			drag.x = mousePosition.x - e.getX();
			drag.y = mousePosition.y - e.getY();
			simulation.viewDragged(drag);
			mousePosition = e.getPoint();
			return;
		}
		
		if (gamePanel.paintBrushing)
		{
			Coordinate tile = new Coordinate();
			tile.x = simulation.getGameXTile(e.getX());
			tile.y = simulation.getGameYTile(e.getY());
			if (gamePanel.paintClass == null)
			{
				simulation.entities.remove(tile);
			}
			else
			{
				if (simulation.isFreeTile(tile))
				{
					try
					{
						simulation.createEntity(gamePanel.paintClass, tile);
					}
					catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1)
					{
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
