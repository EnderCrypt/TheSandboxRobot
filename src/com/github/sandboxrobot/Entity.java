package com.github.sandboxrobot;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public abstract class Entity implements Serializable // do not extend this, use
														// static or dynamic
														// entity instead
{
	private static final long serialVersionUID = 5470870075421239827L;
	/**
	 * 
	 */
	protected Coordinate position;
	private GuiGraphics graphic;
	private boolean grabAble = false;

	Entity(Simulation simulation, Coordinate position, GuiGraphics graphic)
	{
		this.position = position.getLocation();
		setGraphic(graphic);
	}

	public void setPosition(Simulation simulation, Coordinate position)
	{
		synchronized (simulation.entities)
		{
			simulation.entities.remove(this.position);
			this.position = position;
			simulation.entities.put(position, this);
		}
	}

	public void placeAt(Simulation simulation, Coordinate position)
	{
		synchronized (simulation.entities)
		{
			this.position = position;
			simulation.entities.put(position, this);
		}
	}

	public void makeGrabAble()
	{
		grabAble = true;
	}

	public boolean isGrabAble()
	{
		return grabAble;
	}

	public Coordinate getPosition()
	{
		return position.getLocation();
	}

	protected void setGraphic(GuiGraphics graphic)
	{
		this.graphic = graphic;
	}

	public void update(Simulation simulation)
	{
		// to be overridden
	}

	protected void draw(Simulation simulation, Dimension screenSize, AffineTransform affineTransform, Graphics2D g2d)
	{
		affineTransform.translate(-(graphic.getWidth() / 2), -(graphic.getHeight() / 2));
		g2d.drawImage(graphic.getImage(), affineTransform, null);
	}
}
