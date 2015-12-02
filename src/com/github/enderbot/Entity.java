package com.github.enderbot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public abstract class Entity // do not extend this, use static or dynamic entity instead
{
	protected Coordinate position;
	private GuiGraphics graphic;
	private boolean grabAble = false;
	Entity(Coordinate position, GuiGraphics graphic)
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
	
	protected void draw(AffineTransform affineTransform, Graphics2D g2d)
	{
		affineTransform.translate(-(graphic.getWidth()/2), -(graphic.getHeight()/2));
		g2d.drawImage(graphic.getImage(), affineTransform, null);
	}
}
