package com.github.sandboxrobot;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class DynamicEntity extends Entity
{
	private Point2D.Double animationOffset = new Point2D.Double();
	private Rotation rotation;
	private double radRotation;
	protected Action action;
	public DynamicEntity(Coordinate position, Rotation rotation, GuiGraphics graphic)
	{
		super(position, graphic);
		setRotation(rotation);
	}
	
	public void setAnimationOffset(double x, double y)
	{
		animationOffset.x = x;
		animationOffset.y = y;
	}
	
	public Double getAnimationOffset()
	{
		return new Point2D.Double(animationOffset.x , animationOffset.y);
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
	protected void draw(AffineTransform affineTransform, Graphics2D g2d)
	{
		affineTransform.translate((animationOffset.x+position.x)*32, (animationOffset.y+position.y)*32);
		affineTransform.rotate(radRotation);
		super.draw(affineTransform, g2d);
	}

}
