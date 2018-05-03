import javax.swing.*;
import java.awt.*;
import java.util.*;

public class CreateEditProjectDialog extends JDialog{
	
	ArrayList<JTextField> columns;
	ArrayList<JButton> columnDelete;
	
	private JPanel newProjPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel editProjPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JButton createBtn = new JButton("Create");
	private JButton cancelBtn = new JButton("Cancel");
	private JButton createRow = new JButton("Create Row");
	private JTextField nameBar = new JTextField(50);

	/*
	 * Constructor for CreateEditProjectDialog. Uses MainScreen and ProjectModel
	 */
	public CreateEditProjectDialog(MainScreen, ProjectModel) {
		
	}
	
	public CreateEditProjectDialog(MainScreen) {
		
	}
	
	public void createNewProject() {
		
	}
	
	public void editExistingProject() {
		
	}
	
	public void reuseExistingProject(JPanel) {
		
	}
	
	public void addNewColumn(JPanel) {
		
	}
	
}
