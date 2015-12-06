package com.github.sandboxrobot;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.Serializable;

public class DynamicEntity extends Entity implements Serializable
{
	private static final long serialVersionUID = 903973447436729599L;
	/**
	 * 
	 */
	private Point2D.Double animationOffset = new Point2D.Double();
	private Rotation rotation;
	private double radRotation;
	protected transient Action action;

	public DynamicEntity(Simulation simulation, Coordinate position, Rotation rotation, GuiGraphics graphic)
	{
		super(simulation, position, graphic);
		setRotation(rotation);
	}

	public void setAnimationOffset(double x, double y)
	{
		animationOffset.x = x;
		animationOffset.y = y;
	}

	public Double getAnimationOffset()
	{
		return new Point2D.Double(animationOffset.x, animationOffset.y);
	}

	public void setRotation(Rotation rotation)
	{
		this.rotation = rotation;
		radRotation = rotation.getRotation();
	}

	public void setAnimationRotation(double radRotation)
	{
		this.radRotation = radRotation;
	}

	public Rotation getRotation()
	{
		return rotation;
	}

	@Override
	protected void draw(Simulation simulation, Dimension screenSize, AffineTransform affineTransform, Graphics2D g2d)
	{
		affineTransform.translate((animationOffset.x + position.x) * 32, (animationOffset.y + position.y) * 32);
		if (simulation.isInsideView((int) affineTransform.getTranslateX(), (int) affineTransform.getTranslateY(), screenSize, 32))
		{
			affineTransform.rotate(radRotation);
			super.draw(simulation, screenSize, affineTransform, g2d);
		}
	}

}
