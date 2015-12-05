package com.github.sandboxrobot;

import java.io.File;
import java.io.IOException;

import com.github.sandboxrobot.actions.Stay;

public class Robot
{
	private final Simulation simulation;

	public final Robot_move move;
	public final Robot_eye eye;
	public final Robot_strafe strafe;
	public final Robot_rotate rotate;
	public final Robot_mark mark;

	public Robot()
	{
		simulation = new Simulation();
		// finalize
		move = new Robot_move(simulation);
		eye = new Robot_eye(simulation);
		strafe = new Robot_strafe(simulation);
		rotate = new Robot_rotate(simulation);
		mark = new Robot_mark(simulation);
		simulation.awaitGui();
	}

	public Robot(String scenario)
	{
		simulation = new Simulation();
		try
		{
			SaveModule.load(simulation, new File(SaveModule.saveDirectory + "/" + scenario));
		}
		catch (ClassNotFoundException | IOException e)
		{
			e.printStackTrace();
		}
		// finalize
		move = new Robot_move(simulation);
		eye = new Robot_eye(simulation);
		strafe = new Robot_strafe(simulation);
		rotate = new Robot_rotate(simulation);
		mark = new Robot_mark(simulation);
		simulation.awaitGui();
	}

	// ADVANCED //

	/**
	 * sets the animation speed of the simulation
	 * 
	 * @param simulationSpeed
	 */
	public void setSpeed(double simulationSpeed)
	{
		simulation.setSpeed(simulationSpeed);
	}

	/**
	 * pauses the simulation, mildly usefull for debugging
	 */
	public void pauseSimulation()
	{
		simulation.pause();
		simulation.blockIfPause(); // needs to be tested
	}

	/**
	 * makes the bot show debug messages about what happends
	 */
	public void activateDebug()
	{
		simulation.robotEntity.setDebug(true);
	}

	// PUBLIC COMMANDS //

	/**
	 * @return the rotation of the robot
	 */
	public Rotation getRotation()
	{
		return simulation.robotEntity.getRotation();
	}

	/**
	 * checks if the bot has available storage in it
	 * 
	 * @return
	 */
	public boolean hasFreeStorage()
	{
		return simulation.robotEntity.hasFreeCarryStorage();
	}

	// PUBLIC ROBOT ACTION COMMANDS //

	/**
	 * attempts to grab the item infront of it
	 * 
	 * @return if grab was successful
	 */
	public boolean grab()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.grab(simulation);
	}

	/**
	 * attempts to place the carried item infront of it
	 * 
	 * @return if place was successful
	 */
	public boolean place()
	{
		simulation.blockIfPause();
		return simulation.robotEntity.place(simulation);
	}

	/**
	 * makes the robot do nothing for 1 turn
	 */
	public void stay()
	{
		simulation.blockIfPause();
		simulation.animate(new Stay());
	}

}
