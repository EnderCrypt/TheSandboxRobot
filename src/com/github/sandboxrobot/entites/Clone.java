package com.github.sandboxrobot.entites;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import com.github.sandboxrobot.Coordinate;
import com.github.sandboxrobot.GuiGraphics;
import com.github.sandboxrobot.Rotation;
import com.github.sandboxrobot.Simulation;
import com.github.sandboxrobot.StaticEntity;

public class Clone extends StaticEntity implements Serializable
{
	private static final long serialVersionUID = 2804239022879065427L;
	/**
	 * 
	 */

	public Clone(Simulation simulation, Coordinate position)
	{
		super(simulation, position, GuiGraphics.CLONE);
		spawnClones(simulation);
	}
	
	@Override
	public void update(Simulation simulation)
	{
		super.update(simulation);
		spawnClones(simulation);
	}
	
	private void spawnClones(Simulation simulation)
	{
		Coordinate myCoord = getPosition();
		for (Rotation rotation : Rotation.values())
		{
			Coordinate coordinate = rotation.getMovement();
			Coordinate relative = myCoord.getLocation();
			relative.translate(coordinate.x, coordinate.y);
			if (simulation.isFreeTile(relative))
			{
				try
				{
					simulation.createEntity(Crate.class, relative);
				}
				catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
}
