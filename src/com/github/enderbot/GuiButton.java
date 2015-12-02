package com.github.enderbot;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class GuiButton
{
	private Point location;
	private Rectangle hitbox;
	private GuiGraphics graphic;
	private Clickable clicked;
	private Simulation robot;
	GuiButton(Simulation robot,Point location, GuiGraphics graphic, Clickable clicked)
	{
		this.robot = robot;
		this.location = location;
		setGraphic(graphic);
		hitbox = new Rectangle(location.x, location.y, graphic.getWidth(), graphic.getHeight());
		this.clicked = clicked;
	}
	
	public GuiGraphics getGraphic()
	{
		return graphic;
	}
	
	public void setGraphic(GuiGraphics graphic)
	{
		this.graphic = graphic;
	}
	
	protected boolean click(Point point)
	{
		if (hitbox.contains(point))
		{
			clicked.clicked(robot, this, point);
			return true;
		}
		return false;
	}
	
	public Point getLocation()
	{
		return location;
	}
	
	protected void draw(Graphics2D g2d)
	{
		g2d.drawImage(graphic.getImage(), null, location.x, location.y);
	}
}
