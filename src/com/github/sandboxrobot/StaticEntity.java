package com.github.sandboxrobot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

public class StaticEntity extends Entity
{

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
