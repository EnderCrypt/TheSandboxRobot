package com.github.sandboxrobot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.sandboxrobot.entites.SandboxRobot;

public class SaveModule implements Serializable
{
	private static final long serialVersionUID = 8591903798568300671L;
	/**
	 * 
	 */
	
	public static final String saveDirectory = "Scenarios";
	
	private static final String extension = "scen";
	
	private static JFileChooser createFileChooser()
	{
		JFileChooser fileChooser = new JFileChooser();
		File baseDirectory = new File(System.getProperty("user.dir")+"/"+saveDirectory);
		baseDirectory.mkdirs();
		fileChooser.setCurrentDirectory(baseDirectory);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Scenario files", extension));
		return fileChooser;
	}
	
	protected static void askSave(Simulation simulation)
	{
		JFileChooser fileChooser = createFileChooser();
		fileChooser.setDialogTitle("Save Scenario");
		if (fileChooser.showSaveDialog(simulation.gameFrame) == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			String fileString = selectedFile.getPath();
			if (!fileString.endsWith("."+extension))
			{
				fileString = fileString+"."+extension;
			}
			selectedFile = new File(fileString);
			try
			{
				save(simulation, selectedFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected static void askLoad(Simulation simulation)
	{
		JFileChooser fileChooser = createFileChooser();
		fileChooser.setDialogTitle("Load Scenario");
		if (fileChooser.showOpenDialog(simulation.gameFrame) == JFileChooser.APPROVE_OPTION)
		{
			File selectedFile = fileChooser.getSelectedFile();
			try
			{
				load(simulation, selectedFile);
			}
			catch (ClassNotFoundException | IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	protected static void save(Simulation simulation, File file) throws IOException
	{
		// open file
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		// write
		saveData sd = new SaveModule().new saveData();
		sd.save(simulation);
		oos.writeObject(sd);
		// close operation
		fos.close();
		oos.close();
	}
	
	protected static void load(Simulation simulation, File file) throws IOException, ClassNotFoundException
	{
		// open file
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);
		// read
		saveData sd = (saveData) ois.readObject();
		sd.load(simulation);
		// close operation
		fis.close();
		ois.close();
	}
	
	class saveData implements Serializable
	{
		private static final long serialVersionUID = -8246707245527875540L;
		/**
		 * 
		 */
		
		private HashMap<Coordinate, Entity> entities;
		private SandboxRobot robotEntity;
		
		public void save(Simulation simulation)
		{
			this.entities = simulation.entities;
			this.robotEntity = simulation.robotEntity;
		}
		
		public void load(Simulation simulation)
		{
			simulation.entities = this.entities;
			simulation.robotEntity = this.robotEntity;
		}
	}
}
