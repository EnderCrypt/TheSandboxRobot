package com.github.sandboxrobot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import com.github.sandboxrobot.entites.SandboxRobot;

public class Simulation
{
	// gui
	protected GameFrame gameFrame;

	// game stuff
	protected HashMap<Coordinate, Entity> entities = new HashMap<>();
	protected HashMap<Coordinate, Mark> marks = new HashMap<>();
	protected SandboxRobot robotEntity;
	protected Point view = new Point(0, 0);
	private Dimension centerOfScreen = new Dimension();
	private boolean playing = true;
	private CountDownLatch startCdl = new CountDownLatch(1);
	private CountDownLatch pauseCdl = null;
	private int simulationSpeed;
	private double simulationSpeedmultiplier = 1.0;

	protected Simulation()
	{
		GuiGraphics.loadAll();
		gameFrame = new GameFrame(this);
		setSpeed(simulationSpeedmultiplier);
		try
		{
			robotEntity = (SandboxRobot) createEntity(SandboxRobot.class, new Coordinate(0, 0));
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}

	public void awaitGui()
	{
		try
		{
			startCdl.await();
			Thread.sleep(250);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void setSpeed(double multiplier)
	{
		multiplier = Math.min(multiplier, 1000);
		multiplier = Math.max(multiplier, 0.1);
		this.simulationSpeedmultiplier = multiplier;
		this.simulationSpeed = (int) (250 / multiplier);
	}

	public double getSpeed()
	{
		return simulationSpeedmultiplier;
	}

	public void centerCamera()
	{
		Point2D.Double offset = robotEntity.getAnimationOffset();
		view.x = (int) ((robotEntity.position.x * 32) + (offset.x * 32));
		view.y = (int) ((robotEntity.position.y * 32) + (offset.y * 32));
	}

	public Dimension getScreenSize()
	{
		return gameFrame.getContentPane().getSize();
	}

	public Dimension getCenterOfScreenSize()
	{
		return centerOfScreen.getSize();
	}

	protected boolean isPlaying()
	{
		return playing;
	}

	protected void play()
	{
		playing = true;
		if (pauseCdl != null)
		{
			pauseCdl.countDown();
		}
		gameFrame.gamePanel.playAndPauseButton.setGraphic(GuiGraphics.PAUSE);
	}

	public void pause()
	{
		playing = false;
		gameFrame.gamePanel.playAndPauseButton.setGraphic(GuiGraphics.PLAY);
	}

	protected int getScreenX(int x)
	{
		return (x + centerOfScreen.width - view.x);
	}

	protected int getScreenY(int y)
	{
		return (y + centerOfScreen.height - view.y);
	}

	protected boolean isInsideView(int screenX, int screenY, Dimension screenSize, int tolerance)
	{
		return (screenX > -tolerance)
				&& (screenY > -tolerance)
				&& (screenX < (screenSize.width + tolerance))
				&& (screenY < (screenSize.height + tolerance));
	}

	protected int getGameX(int x)
	{
		return (x + view.x - centerOfScreen.width);
	}

	protected int getGameY(int y)
	{
		return (y + view.y - centerOfScreen.height);
	}

	protected int getGameXTile(int x)
	{
		return (int) Math.floor((double) (getGameX(x) + 16) / 32);
	}

	protected int getGameYTile(int y)
	{
		return (int) Math.floor((double) (getGameY(y) + 16) / 32);
	}

	public boolean isFreeTile(Coordinate tile)
	{
		return (getEntity(tile) == null);
	}

	public Entity getEntity(Coordinate tile)
	{
		synchronized (entities)
		{
			return entities.get(tile);
		}
	}

	public Entity removeEntity(Coordinate tile)
	{
		synchronized (entities)
		{
			return entities.remove(tile);
		}
	}

	public void addEntity(Entity entity)
	{
		synchronized (entities)
		{
			if (isFreeTile(entity.getPosition()) == false)
			{
				throw new RuntimeException("cannot add entity at a position already occupied by something else");
			}
			entities.put(entity.getPosition(), entity);
		}
	}

	protected void viewDragged(Point drag)
	{
		view.x += drag.x;
		view.y += drag.y;
	}

	protected void draw(Graphics2D g2d, Dimension screenSize, Point mousePosition)
	{
		// init
		Coordinate mouseGamePosition = new Coordinate(getGameXTile(mousePosition.x), getGameYTile(mousePosition.y));
		centerOfScreen = new Dimension(screenSize.width / 2, screenSize.height / 2);
		// draw entities
		AffineTransform defaultAffineTransform = new AffineTransform();
		defaultAffineTransform.translate(getScreenX(0), getScreenY(0));
		synchronized (entities)
		{
			for (Entry<Coordinate, Entity> entry : entities.entrySet())
			{
				entry.getValue().draw(this, screenSize, new AffineTransform(defaultAffineTransform), g2d);
			}
		}
		// draw marks
		Mark mouseMark = null;
		synchronized (marks)
		{
			for (Entry<Coordinate, Mark> markEntry : marks.entrySet())
			{
				// init
				Coordinate coordinate = markEntry.getKey();
				Mark mark = markEntry.getValue();
				// draw mark
				int x = getScreenX(coordinate.x * 32);
				int y = getScreenY(coordinate.y * 32);
				if (isInsideView(x, y, screenSize, 16))
				{
					mark.draw(g2d, x, y);
				}
				// draw info
				if (mouseGamePosition.equals(coordinate))
				{
					mouseMark = mark;
				}
			}
		}
		// draw text info
		if (mouseMark != null)
		{
			mouseMark.drawText(g2d, mousePosition.x, mousePosition.y);
		}
		// speed info
		String speedString = "Speed: " + getSpeed();
		int length = g2d.getFontMetrics().stringWidth(speedString);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(5, 55, length + 10, 20);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(5, 55, length + 10, 20);
		g2d.drawString(speedString, 10, 70);

		// finish
		startCdl.countDown();
	}

	protected void animate(Action action) // blocking
	{
		// handle user bot
		robotEntity.action = action;
		// update all entities
		Set<Entity> entitiesToUpdate = new HashSet<>();
		for (Entry<Coordinate, Entity> entity : entities.entrySet())
		{
			entitiesToUpdate.add(entity.getValue());
		}
		for (Entity entity : entitiesToUpdate)
		{
			entity.update(this);
		}
		// collect all dynamic entities
		Set<DynamicEntity> dynamicEntities = new HashSet<>();
		for (Entry<Coordinate, Entity> entry : entities.entrySet())
		{
			Entity entity = entry.getValue();
			if (entity instanceof DynamicEntity)
			{
				dynamicEntities.add((DynamicEntity) entity);
			}
		}
		// init
		int totalUpdates = simulationSpeed;
		for (DynamicEntity entity : dynamicEntities)
		{
			entity.action.init(this, entity, totalUpdates);
		}
		// update
		double progress;
		for (int i = 0; i < totalUpdates; i++)
		{
			progress = 1.0 / totalUpdates * i;
			for (DynamicEntity entity : dynamicEntities)
			{
				entity.action.update(entity, progress);
			}
			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		// finish
		for (DynamicEntity entity : dynamicEntities)
		{
			entity.action.finish(entity);
		}
	}

	public void blockIfPause()
	{
		if (playing == false)
		{
			pauseCdl = new CountDownLatch(1);
			try
			{
				pauseCdl.await();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	public Entity createEntity(Class<? extends Entity> paintClass, Coordinate tile)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Constructor<? extends Entity> constructor = paintClass.getConstructor(Simulation.class, Coordinate.class);
		Entity entity = constructor.newInstance(new Object[] { this, tile });
		synchronized (entities)
		{
			entities.put(entity.getPosition(), entity);
		}
		return entity;
	}

}
