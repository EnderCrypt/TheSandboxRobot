package com.github.test;

import com.github.enderbot.Robot;
import com.github.enderbot.entites.Crate;

public class Main
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot robot = new Robot(1.0);
		robot.pauseSimulation();
		while (true)
		{
			if (robot.lookWhat() == Crate.class)
			{
				while (robot.look() > 0)
				{
					robot.forwards();
				}
				robot.grab();
			}
		}
	}

}
