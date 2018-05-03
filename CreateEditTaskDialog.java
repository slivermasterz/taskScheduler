import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.scene.control.DatePicker;

//CreateEditTaskDialog cd = new ....

public class CreateEditTaskDialog extends JDialog {
	private JLabel nameLabel = new JLabel("Name:");
	private JLabel descriptionLabel = new JLabel("Description:");
	private JLabel statusLabel = new JLabel("Status:");
	private JLabel dueDateLabel = new JLabel("Due:");
	private JTextField nameInput = new JTextField(50);
	private JTextArea descriptionInput = new JTextArea();
	private JComboBox<String> statusChoice = new JComboBox<String>();
	private JButton submitBtn = new JButton("FSFFDSAF");
	private JButton cancelBtn = new JButton("Cancel");
	private JTextField datePicker = new JTextField("MM/DD/YYYY");
	private MainScreen parentPanel;
	private ProjectModel project;
	private JColorChooser jcc;
	private TaskModel model;
	
	//add a colorpicker
	//confirm(edit/create), calls addnewTask/editnewTask and cancel btns
	//cancel calls this.dispose
	
	/*
	 * Constructor for CreateEditTaskDialog
	 * Given a model, if the model is null set buttons to "Create" and if model exists, set buttons to "Edit"
	 * Right now, we only take in one task. Not sure how to update entire model based off this dialog, do not have access to entire model.
	 * Possible solution: Pass the entire model through this constructor? Or set the entire model as one of the parameters for the constructor
	 * "(MainScreen mainscreen, EntireModel model, TaskModel task)"
	 */
	public CreateEditTaskDialog(MainScreen mainscreen, TaskModel model) {
		super(SwingUtilities.getWindowAncestor(mainscreen));
		this.parentPanel = mainscreen;
		this.model = model;
		project = mainscreen.getCurrProj();
		JPanel entirePanel = new JPanel();
		entirePanel.setLayout(new BoxLayout(entirePanel, BoxLayout.Y_AXIS));
		JPanel textFields = new JPanel();
		textFields.setLayout(new GridLayout(0, 2));

		//Add all textfields to JPanel
		textFields.add(nameLabel);
		textFields.add(nameInput);
		textFields.add(descriptionLabel);
		textFields.add(descriptionInput);
		textFields.add(statusLabel);
		textFields.add(statusChoice);
		textFields.add(dueDateLabel);
		textFields.add(datePicker);


		if(model == null) {
			submitBtn.setText("Create");
			//setTitle("Create Task");
		}
		else {
			submitBtn.setText("Edit");
			//setTitle("Edit Task");
		}
		
		
		//createGUI();
		//Action listeners
		//Add a method to update the model after create/editing
        submitBtn.addActionListener(e->{
            giveTask();
            this.dispose();
        });
        cancelBtn.addActionListener(e->{
        	this.dispose();
        });
        
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		buttons.add(submitBtn);
		buttons.add(cancelBtn);
        
        entirePanel.add(textFields);
        entirePanel.add(buttons);
        this.add(entirePanel);
        this.pack();
        this.setVisible(true);
	}
	
	
	public void giveTask() {		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/DD/YYYY");
		Date c = null;
		try {
			c = sdf.parse(datePicker.getText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(model == null) 
		{
			model = new TaskModel(nameInput.getText(), descriptionInput.getText(), c, 
					String.valueOf(statusChoice.getSelectedItem()), Color.GRAY);
			this.parentPanel.getCurrProj().addTask(model);
			
		} 
		else 
		{
			model.setName(nameInput.getText());
			model.setDescription(descriptionInput.getText());
			model.setDueDate(c);
			model.setStatus(String.valueOf(statusChoice.getSelectedItem()));
			model.setColor(Color.GRAY);
			this.parentPanel.getCurrProj().editTask(model, model);
		}
	}
	
	public CreateEditTaskDialog(MainScreen mainscreen) {
		this(mainscreen, null);
	}
	
	/*
	 * 
	 */
//    private void createGUI()
//    {
//    	//Create and set up the window
//    	JFrame frame = new JFrame("Create Task");
//    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    	frame.setPreferredSize(new Dimension(540, 540));
//    	
//    	//Action listeners
//        submitBtn.addActionListener(e->{
//            //giveTask();
//        });
//        cancelBtn.addActionListener(e->{
//        	this.dispose();
//        });
//        
//        //Set up the pane
//        this.add(cancelBtn);
//        this.add(submitBtn);
//        this.pack();
//        this.setVisible(true);
//    }
//    
//    private void editGUI()
//    {
//    	//Create and set up the window
//    	JFrame frame = new JFrame("Edit Task");
//    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    	
//    	//Action listeners
//        submitBtn.addActionListener(e->{
//            //giveTask();
//        });
//        cancelBtn.addActionListener(e->{
//        	this.dispose();
//        });
//        
//        //Set up the pane
//        this.add(cancelBtn);
//        this.add(submitBtn);
//        this.pack();
//        this.setVisible(true);
//    }
////    
//    private void addNewTask() {
//    	ProjectModel project = parentPanel.getCurrProj();
//    	TaskModel model = project.createTask(nameInput.getText(), descriptionInput.getText(), new Date(datePicker.getValue().toEpochDay()), (String)statusChoice.getSelectedItem());
//    	project.addTask(model);
//    }
//
//    private void editTask(TaskModel oldTask) {
//    	ProjectModel project = parentPanel.getCurrProj();
//    	TaskModel model = project.createTask(nameInput.getText(), descriptionInput.getText(), new Date(datePicker.getText()), (String)statusChoice.getSelectedItem());
//    	project.editTask(oldTask, model);
//    }
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
//        MainScreen main = new MainScreen(frame, new TaskBoardModel());
//        CreateEditTaskDialog cd = new CreateEditTaskDialog(main);
//        main.setPreferredSize(new Dimension(1080,720));
//        frame.add(cd);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	}
}
