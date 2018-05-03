import javax.swing.*;
import java.awt.*;
import java.util.Date;

import javafx.scene.control.DatePicker;

public class CreateEditTaskDialog extends JDialog {
	private JLabel nameLabel = new JLabel("Name:");
	private JLabel descriptionLabel = new JLabel("Description:");
	private JLabel statusLabel = new JLabel("Status:");
	private JLabel dueDateLabel = new JLabel("Due:");
	private JTextField nameInput = new JTextField(50);
	private JTextArea descriptionInput = new JTextArea();
	private JComboBox<String> statusChoice = new JComboBox<String>();
	private JButton submitBtn = new JButton("");
	private DatePicker datePicker;
	private MainScreen parentPanel;
	private ProjectModel project;
	
	public CreateEditTaskDialog(MainScreen mainscreen, TaskModel model) {
		super(SwingUtilities.getWindowAncestor(mainscreen));
		datePicker = new DatePicker();
		project = mainscreen.getCurrProj();
		if(model == null) {
			submitBtn.setName("Create");
		}
		else {
			submitBtn.setName("Edit");
		}
		createGUI();
	}
	
	public CreateEditTaskDialog(MainScreen mainscreen) {
		this(mainscreen, null);
	}
	
	/*
	 * 
	 */
    private void createGUI()
    {
        setTitle("Something");
        submitBtn.addActionListener(e->{
            //giveTask();
        });
        this.add(submitBtn);
        this.pack();
        this.setVisible(true);
    }
    
    private void addNewTask() {
    	ProjectModel project = parentPanel.getCurrProj();
    	TaskModel model = project.createTask(nameInput.getText(), descriptionInput.getText(), new Date(datePicker.getValue().toEpochDay()), (String)statusChoice.getSelectedItem());
    	project.addTask(model);
    }

    private void editTask(TaskModel oldTask) {
    	ProjectModel project = parentPanel.getCurrProj();
    	TaskModel model = project.createTask(nameInput.getText(), descriptionInput.getText(), new Date(datePicker.getValue().toEpochDay()), (String)statusChoice.getSelectedItem());
    	project.editTask(oldTask, model);
    }
    
	/*
	 * 
	 */
	private void processInput() {
		
	}
	
	/*
	 * 
	 */
	public void updateCurrentProj() {
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("Test");
        MainScreen main = new MainScreen(frame, new TaskBoardModel());
        //CreateEditTaskDialog a = new CreateEditTaskDialog(frame);
        main.setPreferredSize(new Dimension(1080,720));
	}
}
