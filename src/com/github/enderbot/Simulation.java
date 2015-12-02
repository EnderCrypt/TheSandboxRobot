package com.github.enderbot;

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

import com.github.enderbot.entites.RobotEntity;

public class Simulation
{
	// gui
	private GameFrame gameFrame;
	
	// game stuff
	protected HashMap<Coordinate, Entity> entities = new HashMap<>();
	protected RobotEntity robotEntity;
	protected Point view = new Point(0, 0);
	private Dimension centerOfScreen = new Dimension();
	private boolean playing = true;
	private CountDownLatch cdl = null;
	private int simulationSpeed;
	
	protected Simulation(int simulationSpeed)
	{
		GuiGraphics.loadAll();
		this.simulationSpeed = simulationSpeed;
		gameFrame = new GameFrame(this);
		try
		{
			robotEntity = (RobotEntity) createEntity(RobotEntity.class, new Coordinate(0, 0));
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
	}
	
	public void centerCamera()
	{
		Point2D.Double offset = robotEntity.getAnimationOffset();
		view.x = (int) ((robotEntity.position.x*32)+(offset.x*32));
		view.y = (int) ((robotEntity.position.y*32)+(offset.y*32));
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
		if (cdl != null)
		{
			cdl.countDown();
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
		return (x+centerOfScreen.width-view.x);
	}
	
	protected int getScreenY(int y)
	{
		return (y+centerOfScreen.height-view.y);
	}
	
	protected int getGameX(int x)
	{
		return (x+view.x-centerOfScreen.width);
	}
	
	protected int getGameY(int y)
	{
		return (y+view.y-centerOfScreen.height);
	}
	
	protected int getGameXTile(int x)
	{
		return (int) Math.floor((double)(getGameX(x)+16)/32);
	}
	
	protected int getGameYTile(int y)
	{
		return (int) Math.floor((double)(getGameY(y)+16)/32);
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
	
	protected void draw(Graphics2D g2d, Dimension screenSize)
	{
		// init
		centerOfScreen = new Dimension(screenSize.width/2, screenSize.height/2);
		// draw entities
		AffineTransform defaultAffineTransform = new AffineTransform();
		defaultAffineTransform.translate( getScreenX(0), getScreenY(0) );
		for (Entry<Coordinate, Entity> entry : entities.entrySet())
		{
			entry.getValue().draw(new AffineTransform(defaultAffineTransform), g2d);
		}
	}
	
	protected void animate(Action action) // blocking
	{
		// handle user bot
		robotEntity.action = action;
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
		for (int i=0;i<totalUpdates;i++)
		{
			progress = 1.0/totalUpdates*i;
			for (DynamicEntity entity : dynamicEntities)
			{
				entity.action.update(entity, progress);
			}
			try{Thread.sleep(1);}catch(InterruptedException e){e.printStackTrace();}
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
			cdl = new CountDownLatch(1);
			try{cdl.await();}catch(InterruptedException e){e.printStackTrace();}
		}
	}
	
	protected Entity createEntity(Class<? extends Entity> paintClass, Coordinate tile) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		Constructor<? extends Entity> constructor = paintClass.getConstructor(Coordinate.class);
		Entity entity = constructor.newInstance(new Object[] { tile });
		synchronized (entities)
		{
			entities.put(entity.getPosition(), entity);
		}
		return entity;
	}
	
}
