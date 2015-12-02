package com.github.test;

import com.github.enderbot.Robot;

public class Main
{

	public static void main(String[] args) throws InterruptedException
	{
		Robot robot = new Robot(1.0);
		robot.pauseSimulation();
		while (true)
		{
			robot.rotateLeft();
			if (robot.grab())
			{
				robot.rotateRight();
				robot.rotateRight();
				robot.place();
				robot.rotateLeft();
			}
			else
			{
				robot.rotateRight();
			}
			if (robot.forwards() == false)
			{
				robot.rotateLeft();
				robot.rotateLeft();
			}
		}
	}

}
