import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MainScreen extends JPanel implements TaskModelListener
{
	
	public static void main(String[] args)
	{
		JFrame frame = new JFrame();
		frame.add(new MainScreen(frame, null));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	
	/**
	 * constructs a MainScreen as the main window for the application.
	 * @param frame JFrame where the application will be displayed.
	 * @param model the date model. If null, the constructor makes an empty project, 
	 * else it uses the passed model.
	 */
	public MainScreen(JFrame frame, TaskBoardModel model)
	{
		this.boardMainModel = model == null ?
				new TaskBoardModel():
				model;
		frame.setTitle(this.boardMainModel.getName());
		// setup the login dialog. by calling the mathods of lok. then try to show it.
		LoginDialog.show(frame);
		// setup the navbar and the catigoris columns.
		setupMainScreen();
		if(this.boardMainModel.numProjects() > 0)
		{
			//set the current project to be the first and load its info.
			this.currentProj = this.boardMainModel.getProject(0);
			this.currentProj.addListener(this);
			this.selectedProjIndex = 0;
			updateDropDown();
			loadDataFromCurrProject();
		}
		else
		{
			this.currentProj = new ProjectModel("",null);
			loadNewProjectDialog();
		}
	}
	
	
	//constructor for testing.
	public MainScreen()
	{
	
	}
	/**
	 * Jobs covered:
	 * - instantiate all the components that should go in the navBar.
	 * - sets layout and the background color of the navBar.
	 * - Adds the navBar to mainScreen.
	 * - no strings gets added to the jcombobox
	 * @return
	 */
	private JPanel setupNavBar()
	{
		
		this.projectsDropDown = new JComboBox<>();
		this.editSelectedProjBtn = new JButton("Edit");
		this.saveTaskBoardBtn = new JButton("Save");
		this.deleteSelectedProjBtn = new JButton("Delete");
		this.loadProjBtn = new JButton("Load");
		this.createNewProjBtn = new JButton("Create new Proj");
		this.logoutBtn = new JButton("Logout");
		this.navBar = new JPanel();
		this.navBar.setLayout(new BoxLayout(this.navBar, BoxLayout.X_AXIS));
		this.navBar.setBackground(Color.red);
		this.navBar.add(new JLabel("Selected Project: "));
		this.navBar.add(this.projectsDropDown);
		this.navBar.add(this.editSelectedProjBtn);
		this.navBar.add(this.saveTaskBoardBtn);
		this.navBar.add(this.deleteSelectedProjBtn);
		this.navBar.add(this.loadProjBtn);
		this.navBar.add(this.createNewProjBtn);
		this.navBar.add(this.logoutBtn);
		this.add(this.navBar,BorderLayout.NORTH);
		this.navBar.setSize(this.navBar.getWidth(), this.navBar.getMinimumSize().height);
		return this.navBar;
	}
	/**
	 * Jobs Covered:
	 * - creates the groups component panels.
	 * - sets its layout.
	 * - add the groupscomp to the mainscreen(Center)
	 */
	private void setupGroupsComp()
	{
		this.groupsComp = new JPanel();
		this.groupsComp.setLayout(new BoxLayout(this.groupsComp, BoxLayout.X_AXIS));
		this.groupsComp.setBackground(Color.gray);

		this.add(this.groupsComp,BorderLayout.CENTER);
	}
	/**
	 * Jobs converd:
	 * - set the size of the mainScreen and its layout.
	 * - adds the navbar and the groups component.
	 * - adds the action listeners to the buttons in the navbar.
	 */
	private void setupMainScreen()
	{
		this.setLayout(new BorderLayout());
		this.setPreferredSize(new Dimension(1080, 720));
		setupNavBar();
		setupGroupsComp();
		handleActionListeners();
	}
	
	
	/**
	 * handles the actionListners of the buttons in the navbar.
	 */
	private void handleActionListeners()
	{
		this.editSelectedProjBtn.addActionListener(
				(ActionEvent e) -> loadEditProjectDialog());
		this.deleteSelectedProjBtn.addActionListener(
				(ActionEvent e) -> deleteSelectedProject());
		this.createNewProjBtn.addActionListener(
				(ActionEvent e) -> loadNewProjectDialog());
		this.saveTaskBoardBtn.addActionListener(
				(ActionEvent e)-> saveTaskBoardToFile());
		this.loadProjBtn.addActionListener(
				(ActionEvent e)-> loadTaskBoardFromFile());
		this.logoutBtn.addActionListener(
				(ActionEvent e)->
				{
					// TODO: add code to shutdown the application.
				});
		this.projectsDropDown.addActionListener(
				(ActionEvent e)->
				{
					if(this.isDropDownListnerActivated)
					{
						// TODO : pay attention to the listeners when switching bwrween projects.
						this.selectedProjIndex = this.projectsDropDown.getSelectedIndex();
						ProjectModel toBeLoaded = this.boardMainModel.getProject(this.selectedProjIndex);
						if(this.currentProj != toBeLoaded)
						{
							this.currentProj = this.boardMainModel.getProject(this.projectsDropDown.getSelectedIndex());
							this.currentProj.addListener(this);
							this.update();
						}
					}
				});
	}
	/**
	 * Adds TaskBoardModel list of projects back to jcomobox
	 */
	private void updateDropDown()
	{
		this.isDropDownListnerActivated = false;
		this.projectsDropDown.removeAllItems();
		for(int i = 0; i < this.boardMainModel.numProjects(); i++)
		{
			this.projectsDropDown.addItem(this.boardMainModel.getProject(i).getName());
		}
		this.projectsDropDown.setSelectedIndex(this.selectedProjIndex);
		this.isDropDownListnerActivated = true;
	}
	
	
	/**
	 * Jobs Covered:
	 * - add the tasks and the columns to mainscreen.
	 */
	private void loadDataFromCurrProject()
	{
		if( this.selectedProjIndex != -1)
		{
			for(int i = 0; i < this.currentProj.numStatuses(); i++)
			{
				Catergory_comp category = new Catergory_comp(
						this.currentProj.getStatus(i), 
						this.currentProj.getCategory(i));
				this.groupsComp.add(category);
			}
			this.groupsComp.setPreferredSize(getPreferredSize());
			this.deleteSelectedProjBtn.setEnabled(true);
			this.editSelectedProjBtn.setEnabled(true);
			this.saveTaskBoardBtn.setEnabled(true);
		}
		else
		{
			this.deleteSelectedProjBtn.setEnabled(false);
			this.editSelectedProjBtn.setEnabled(false);
			this.saveTaskBoardBtn.setEnabled(false);
			loadNewProjectDialog();
		}
		
	}
	
	private void loadNewProjectDialog()
	{
		//TODO: add code to show the create project dialog.
		// track the number of projects. and decide
		int oldProjectsCount = this.boardMainModel.numProjects();
		CreateEditProjectDialog.show(this, null);
		int newProjectsCount = this.boardMainModel.numProjects();

		if(oldProjectsCount < newProjectsCount)
		{
			this.selectedProjIndex = newProjectsCount -1;
			this.currentProj = this.boardMainModel.getProject(selectedProjIndex);
			this.update();
		}
		else
		{
			System.out.print("cancel got pressed for creating new project.");
			//if creating new project got canceled and there is no projects left in the mainboard.
			if(this.boardMainModel.numProjects() == 0)
			{
				this.selectedProjIndex = -1;
				this.deleteSelectedProjBtn.setEnabled(false);
				this.editSelectedProjBtn.setEnabled(false);
				this.saveTaskBoardBtn.setEnabled(false);
			}
		}
		
	}
	
	private void loadEditProjectDialog()
	{
		// TODO: add code to show the project edit dialog.
		CreateEditProjectDialog.show(this,this.currentProj);
	}
	
	private void deleteSelectedProject()
	{
		this.boardMainModel.deleteProjects(this.boardMainModel.getProject(this.projectsDropDown.getSelectedIndex()));
		if(this.boardMainModel.numProjects() > 0)
		{
			this.selectedProjIndex = 0;
			this.currentProj = this.boardMainModel.getProject(0);
			this.currentProj.addListener(this);
		}
		else
		{// signals that nomore projects exist.
			this.selectedProjIndex = -1;
		}
		this.update();
	}
	
	private void loadTaskBoardFromFile()
	{
		// TODO: (done) add the code to show the loading dialog.
		// TODO: (done) handle the null from the file handler.
		TaskBoardModel temp = FileHandler.loadTaskBoard();
		if(temp != null)
		{
			this.boardMainModel = temp;
			if(this.boardMainModel.numProjects() > 0)
			{
				this.selectedProjIndex = 0;
			}
			else
			{
				this.selectedProjIndex = -1;
			}
			this.update();
		}
	}
	
	private void saveTaskBoardToFile()
	{
		// TODO: (done) add the code to show the saving dialog.
		FileHandler.saveTaskBoard(this.boardMainModel);
	}
	
	public ProjectModel getCurrProj()
	{
		return this.currentProj;
	}
	
	public TaskBoardModel getTaskBoardModel()
	{
		return this.boardMainModel;
	}
	
	public Task_comp getTask_compTestInstance()
	{
		return new Task_comp(null);
	}
	
	public Catergory_comp getCategory_compTestInstance()
	{
		return new Catergory_comp(null,null);
	}
	
	public class Task_comp extends JPanel implements MouseListener
	{
		public Task_comp(TaskModel task)
		{
			if(task == null)
			{//return a test component in task is null;
				this.nameLabel = new JLabel("test task title for the task.");
				this.descriptionLabel = new JTextArea("test description for the task.",6,15);
				this.dueDateLabel = new JLabel("test date for the task.");
			}
			else
			{
				this.task = task;
				this.nameLabel = new JLabel(task.getName());
				this.descriptionLabel = new JTextArea(task.getDescription(),6,15);
				// TODO: (done) make the date print correctly.
				// TODO: (done) make it accept null dates. it should produce empty date field.
				SimpleDateFormat formater = new SimpleDateFormat("EEEE MMM dd-YYYY"); //thanks to stackoverflow user Rahul Tripathi.
				String date = task.getDueDate() != null ? formater.format(task.getDueDate()): "";
				this.dueDateLabel = new JLabel(date);
			}
			
			// TODO: (done) pay attention to the background color of the component.
			this.background = task.getColor();
			// TODO: (done) make the description box non editable.
			// TODO: make the description background color change.
			this.descriptionLabel.setLineWrap(true);
			this.descriptionLabel.setWrapStyleWord(true);
			this.descriptionLabel.setEditable(false);
			this.descriptionLabel.setBackground(this.background);
			// take care of alignment for all the elements
			this.nameLabel.setAlignmentX(LEFT_ALIGNMENT);
			this.descriptionLabel.setAlignmentX(LEFT_ALIGNMENT);
			this.dueDateLabel.setAlignmentX(LEFT_ALIGNMENT);
			this.setAlignmentX(Component.CENTER_ALIGNMENT);
			//add the jcomponents to the panel.
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			this.setBackground(this.background);
			this.add(this.nameLabel);
			this.add(this.descriptionLabel);
			this.add(this.dueDateLabel);
			this.addMouseListener(this);
		}
		
		private JLabel nameLabel;
		// TODO use something different than a JLabel because the text is not wraping.
		private JTextArea descriptionLabel;
		private JLabel dueDateLabel;
		private Color background;
		private TaskModel task;
		
		@Override
		public void paintComponent(Graphics g)
		{
			
			
			this.setBorder(BorderFactory.createLineBorder(this.getParent().getBackground(), 5, false));
			
			// add titled borders to these components.
			this.nameLabel.setBorder(BorderFactory.createTitledBorder("Task Name"));
			this.descriptionLabel.setBorder(BorderFactory.createTitledBorder("Description"));
			this.dueDateLabel.setBorder(BorderFactory.createTitledBorder("Due Date"));
			super.paintComponent(g);
		}
		
		//implement the mouse listener interface.
		
		@Override
		public void mouseClicked(MouseEvent e) 
		{
			// TODO create the dialog to edit the clicked task.
			CreateEditTaskDialog.show(MainScreen.this, this.task);
		}

		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
	}
	
	class Catergory_comp extends JPanel 
	{
		public Catergory_comp(String name, ArrayList<TaskModel> tasks)
		{
			this.taskPanels = new ArrayList<Task_comp>();
			this.setAlignmentY(0);
			if(name == null && tasks == null)
			{
				this.name = new JLabel("Test Catergory");
				
				for(int i = 0 ; i < 2; i++)
					this.taskPanels.add(new Task_comp(null));
			}
			else
			{
				this.name = new JLabel(name);
				this.tasks = tasks;
				for(TaskModel task: tasks)
					this.taskPanels.add(new Task_comp(task));
			}
			
			this.addTaskBtn = new JButton("+");
			// configure the action ll for the add button
			this.addTaskBtn.addActionListener((ActionEvent e) -> {
				//TODO: include the code to show adding a task dialog.
				CreateEditTaskDialog.show(MainScreen.this, null);
			});
			
			
			//add components to this jpanel
			//TODO: (done) make tasksContainer a container scrolable
			Container tasksContainer = new Container();
			tasksContainer.setLayout(new BoxLayout(tasksContainer,BoxLayout.Y_AXIS));
			for(Task_comp taskPanel: this.taskPanels)
			{
				tasksContainer.add(taskPanel);
			}
			this.add(this.name);
			this.add(this.addTaskBtn);
			// TODO: make scrollbar all the way up.
			JScrollPane scrolable = new JScrollPane(tasksContainer);
			scrolable.setPreferredSize(tasksContainer.getPreferredSize());
			this.add(scrolable);
			
			
			//set preffered size
			this.setPreferredSize(new Dimension(100,300));
		}
		
		private JLabel name;
		private JButton addTaskBtn;
		private ArrayList<TaskModel> tasks;
		private ArrayList<Task_comp> taskPanels;
		
		@Override
		public void paintComponent(Graphics g)
		{
			this.setBackground(Color.yellow);
			this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
			this.setBorder(BorderFactory.createLineBorder(this.getParent().getBackground(), 5));
			// take care of alignment issues.
			this.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.name.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.addTaskBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
			super.paintComponent(g);
		}
		
		
		
	}
	
	private TaskBoardModel boardMainModel;
	private ProjectModel currentProj;
	private int selectedProjIndex;
	private boolean isDropDownListnerActivated;

	private JPanel 
				currentView,
				navBar,
				groupsComp;

	private JButton 
				editSelectedProjBtn,
				saveTaskBoardBtn,
				deleteSelectedProjBtn,
				loadProjBtn,
				createNewProjBtn,
				logoutBtn;
	private JComboBox<String> projectsDropDown;
	
	//private FileHandler fileHandler;
	
	/**
	 * Covered Jobs:
	 * - remove categories
	 * - update the drop down.
	 * - load the current project.
	 */
	@Override
	public void update()
	{
		this.groupsComp.removeAll();
		this.groupsComp.repaint();
		updateDropDown();
		loadDataFromCurrProject();
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	}
	
}
