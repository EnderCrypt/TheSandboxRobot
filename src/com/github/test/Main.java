package com.github.test;

import com.github.enderbot.Robot;

public class Main
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot robot = new Robot(2.0);
		robot.pauseSimulation();
		while (true)
		{
			boolean success = robot.forwards();
			if (success == false)
			{
				robot.rotateLeft();
			}
		}
	}

}
