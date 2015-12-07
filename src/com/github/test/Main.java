package com.github.test;

import java.awt.Color;

import com.github.sandboxrobot.Robot;

public class Main
{
	public static void main(String[] args)
	{
		Robot robot = new Robot();
		// TODO: test code
		while (true)
		{
			robot.move.forwards();
			robot.move.forwards();
			robot.rotate.left();
			robot.mark.set(robot.getPosition(), Color.GREEN);
		}
	}
}
