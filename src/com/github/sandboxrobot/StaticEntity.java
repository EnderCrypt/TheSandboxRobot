package com.github.sandboxrobot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

public class StaticEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = -637717681446526876L;
	/**
	 * 
	 */

	public StaticEntity(Simulation simulation, Coordinate position, GuiGraphics graphic)
	{
		super(simulation, position, graphic);
	}
	
	@Override
	protected void draw(AffineTransform affineTransform, Graphics2D g2d)
	{
		affineTransform.translate(position.x*32, position.y*32);
		super.draw(affineTransform, g2d);
	}

}
