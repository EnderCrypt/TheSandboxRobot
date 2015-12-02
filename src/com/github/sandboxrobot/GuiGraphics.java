package com.github.sandboxrobot;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public enum GuiGraphics
{
	// misc
	SELECTED("selected"),
	
	// buttons
	PAUSE("pause"),
	PLAY("play"),
	FOLLOW("follow"),
	MOVE_SCREEN("move_screen"),
	FIND("find"),
	ERASER("eraser"),
	
	// stuff
	ROBOT("robot"),
	WALL("wall"),
	CRATE("crate"),
	CLONE("cog"),
	;
	private static final boolean IS_JAR = GuiGraphics.class.getResource(GuiGraphics.class.getSimpleName()+".class").toString().startsWith("jar:");
	
	private static final String DIRECTORY = "resources/";
	private static final String IMAGE_EXTENSION = ".png";
	private String filename;
	private BufferedImage bufferedImage;
	private int width;
	private int height;
	private boolean isLoaded = false;
	
	private GuiGraphics(String filename)
	{
		this.filename = filename;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	protected BufferedImage getImage()
	{
		return bufferedImage;
	}
	
	public static void loadAll()
	{
		for (GuiGraphics graphic : values())
		{
			if (graphic.isLoaded == false)
			{
				if (IS_JAR)
				{
					try (InputStream in = GuiGraphics.class.getClassLoader().getResourceAsStream(DIRECTORY + graphic.filename + IMAGE_EXTENSION))
					{
						Image image = null;
						image = ImageIO.read(in);
						graphic.bufferedImage = (BufferedImage) image;
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					File file = new File(DIRECTORY + graphic.filename + IMAGE_EXTENSION);
					if (file.exists() == false)
					{
						JOptionPane.showMessageDialog(null,"Error: Missing file! ("+file.getPath()+")","Image error!",JOptionPane.ERROR_MESSAGE);
					}
					try
					{
						graphic.bufferedImage = ImageIO.read(file);
					}
					catch (IOException e)
					{
						JOptionPane.showMessageDialog(null,"Error: Loading file! ("+file.getPath()+")","Image error!",JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
				graphic.width = graphic.bufferedImage.getWidth();
				graphic.height = graphic.bufferedImage.getHeight();
				graphic.isLoaded = true;
			}
		}
	}
}
