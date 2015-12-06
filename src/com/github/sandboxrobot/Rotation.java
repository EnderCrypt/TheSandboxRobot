package com.github.sandboxrobot;

public enum Rotation
{
	EAST(0), SOUTH(90), WEST(180), NORTH(270);

	private int direction;
	private Coordinate movement = new Coordinate();

	private Rotation(int direction)
	{
		this.direction = direction;
		movement.x = (int) Math.round(Math.cos(Math.toRadians(direction)));
		movement.y = (int) Math.round(Math.sin(Math.toRadians(direction)));
		// System.out.println(movement);
	}

	public Coordinate getMovement()
	{
		return movement.getLocation();
	}

	public double getRotation()
	{
		return Math.toRadians(direction + 90);
	}

	public Rotation rotateBackwards()
	{
		int index = ordinal();
		index -= 2;
		if (index < 0)
			index += 4;
		return Rotation.values()[index];
	}

	public Rotation rotate(int dirs)
	{
		int index = ordinal();
		index += dirs;
		if (index < 0)
			index = 3;
		if (index > 3)
			index = 0;
		return Rotation.values()[index];
	}

	public Rotation rotateLeft()
	{
		int index = ordinal();
		index--;
		if (index < 0)
			index = 3;
		return Rotation.values()[index];
	}

	public Rotation rotateRight()
	{
		int index = ordinal();
		index++;
		if (index > 3)
			index = 0;
		return Rotation.values()[index];
	}

	public Rotation getCopy()
	{
		return Rotation.values()[ordinal()];
	}

	public static Rotation randomRotation()
	{
		return Rotation.values()[(int) (Math.random() * 3)];
	}
}
