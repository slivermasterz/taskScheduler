

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.BoxLayout;
import javax.swing.JFrame;


public class FinalProjTest 
{
	public void Task_compTest()
	{
		
	}
	
	public static void main(String[] args)
	{
		
		JFrame frame = new JFrame();
		frame.add(new MainScreen(frame, setupProjectModel()));
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static TaskBoardModel setupProjectModel()
	{
		TaskBoardModel model = new TaskBoardModel();
		ArrayList<String> groups = new ArrayList<>();
		groups.add("todo");
		groups.add("in progress");
		groups.add("done");
		
		for(int i = 0; i < 2; i++)
		{
			ProjectModel project = new ProjectModel("test prject" + i,groups);
			TaskModel task1 = new TaskModel(
					"test task 1"+ i, 
					"this is the description of the test task1", 
					new GregorianCalendar(2014,Calendar.FEBRUARY, 12).getTime(),
					"todo",
					Color.blue);
			TaskModel task2 = new TaskModel(
					"test task 2"+ i, 
					"this is the description of the test task2 this is a super losng like task description because i want to test the sizing "
					+ "of hte ekement and i want to test the my ability of typing without looking ar the keyboard and i dont want to correct nyself"
					+ "if there was a mistake at all.", 
					new GregorianCalendar(2015,Calendar.FEBRUARY, 12).getTime(),
					"todo");
			TaskModel task3 = new TaskModel(
					"test task 3"+ i, 
					"this is the description of the test task3", 
					new GregorianCalendar(2016,Calendar.FEBRUARY, 12).getTime(),
					"todo");
			TaskModel task4 = new TaskModel(
					"test task 4"+ i, 
					"this is the description of the test task4", 
					new GregorianCalendar(2017,Calendar.FEBRUARY, 12).getTime(),
					"todo",
					Color.RED);
			project.addTask(task1);
			project.addTask(task2);
			project.addTask(task3);
			project.addTask(task4);
			TaskModel task5 = new TaskModel(
					"test task 5", 
					"this is the description of the test task5", 
					new GregorianCalendar(2001,Calendar.FEBRUARY, 12).getTime(),
					"in progress");
			TaskModel task6 = new TaskModel(
					"test task 6", 
					"this is the description of the test task6", 
					new GregorianCalendar(2002,Calendar.FEBRUARY, 12).getTime(),
					"in progress");
			TaskModel task7 = new TaskModel(
					"test task 7", 
					"this is the description of the test task7", 
					new GregorianCalendar(2003,Calendar.FEBRUARY, 12).getTime(),
					"in progress");
			TaskModel task8 = new TaskModel(
					"test task 8", 
					"this is the description of the test task", 
					new GregorianCalendar(2004,Calendar.FEBRUARY, 12).getTime(),
					"in progress");
			project.addTask(task5);
			project.addTask(task6);
			project.addTask(task7);
			project.addTask(task8);
			model.addProjects(project);
		}
		
		return model;
	}
}
