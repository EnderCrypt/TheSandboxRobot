package com.github.sandboxrobot;

import java.awt.Point;

public class Coordinate extends Point
{
	private static final long serialVersionUID = 4475659348797056469L;
	/**
	 * 
	 */
	
	public Coordinate()
	{
		x = 0;
		y = 0;
	}
	
	public Coordinate(Point point)
	{
		x = point.x;
		y = point.y;
	}
	
	public Coordinate(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override
	public Coordinate getLocation()
	{
		return new Coordinate(x, y);
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (!super.equals(obj)) return false;
		if (getClass() != obj.getClass()) return false;
		Coordinate other = (Coordinate) obj;
		if (x != other.x) return false;
		if (y != other.y) return false;
		return true;
	}
}
