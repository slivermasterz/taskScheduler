import javax.swing.*;
import org.jdatepicker.JDatePicker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * CreateEditTaskDialog is a JDialog that handles creating and editing tasks.
 * Depending on the constructor used, the JDialog shown will create or edit a task.
 * This JDialog allows for a user to modify a Task's Name, Description, Status, Due Date,
 * and color.
 * Name and Description are inputted through JTextField and TTextArea respectively.
 *
 * Status is selected through a JComboBox, with the default selection being depending on whether
 * a new Task is being created or if a task is being edited. If a new task is being created,
 * the default status given is the one from which the Add Task button was pressed from.
 *
 * Due Date is selected through a JDatePicker. If a date is not selected, no due date is assigned.
 *
 * Color is selected through a JColorChooser Dialog. Default color is pink.
 *
 * Created by Lok Man Chu
 */
public class CreateEditTaskDialog extends JDialog {
    private JLabel nameLabel = new JLabel("Name: ");
    private JTextField nameInput = new JTextField(15);
    private JLabel descriptionLabel = new JLabel("Description: ");
    private JTextArea descriptionInput = new JTextArea(5,15);
    private JScrollPane descriptionScroll = new JScrollPane(descriptionInput);
    private JLabel statusLabel = new JLabel("Status: ");
    private JComboBox<Object> statusChoice;
    private JLabel dueDateLabel = new JLabel("Due Date: ");
    private JDatePicker datePicker;
    private JButton confirmButton = new JButton("Confirm");
    private JButton cancelButton = new JButton("Cancel");
    private JLabel colorLabel = new JLabel("Color: ");
    private JButton colorButton = new JButton();

    private MainScreen parent;
    private TaskModel taskModel;
    private TaskModel mainModel;

    /**
     * Static method for creating CreateEditTaskDialog.
     * This call is used for editing a Task.
     * @param parent MainScreen calling the Dialog
     * @param model TaskModel to be edited
     */
    public static void show(MainScreen parent, TaskModel model)
    {
        new CreateEditTaskDialog(parent, model, null);
    }

    /**
     * Static method for creating CreateEditTaskDialog.
     * This call is used for creating a task
     * @param parent MainScreen calling the Dialog
     * @param status default status for new TaskModel
     */
    public static void show(MainScreen parent, String status)
    {
        new CreateEditTaskDialog(parent,null, status);
    }

    /**
     * Private constructor of CreateEditTaskDialog. Use static CreateEditTaskDialog.show to create.
     *
     * Jobs covered:
     * - Creates JDialog
     * - Creates a TaskModel to make changes on.
     * - Creates a Model for JComboBox
     * - Calls GUI creation method
     * @param parent
     * @param model
     * @param status
     */
    private CreateEditTaskDialog(MainScreen parent, TaskModel model, String status) {
        super(SwingUtilities.getWindowAncestor(parent));
        this.parent = parent;
        mainModel = model;
        taskModel = mainModel == null?parent.getCurrProj().createTask():new TaskModel(mainModel);
        taskModel.setStatus(status);
        statusChoice = new JComboBox<Object>(parent.getCurrProj().getStatuses().toArray());
        createGUI();
    }

    /**
     * Creates the GUI for CreateEditTaskDialog
     *
     * Jobs covered:
     * - Sets Dialog settings for modality and resizing
     * - Sets text to be for Creating Task or Editing Task
     * - Sets default values of all input fields
     * - Adds all elements to Dialog
     * - Assigns ActionListeners to all buttons
     */
    private void createGUI() {

        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setResizable(false);

        this.setTitle((mainModel==null?"Create":"Edit")+ " " + (mainModel==null?"Task":mainModel.getName()));
        confirmButton.setText(mainModel==null?"Create":"Edit");
        descriptionInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        descriptionInput.setLineWrap(true);
        colorButton.setToolTipText("Click to change color");


        nameInput.setText(taskModel.getName());
        descriptionInput.setText(taskModel.getDescription());
        colorButton.setBackground(taskModel.getColor());
        statusChoice.setSelectedIndex(parent.getCurrProj().getStatuses().indexOf(taskModel.getStatus()));
        datePicker = (mainModel==null || mainModel.getDueDate() == null)?new JDatePicker():new JDatePicker(mainModel.getDueDate());

        //nameInput.setHorizontalAlignment();
        nameLabel.setPreferredSize(new Dimension(73,20));
        statusLabel.setPreferredSize(new Dimension(70,20));
        dueDateLabel.setPreferredSize(new Dimension(69,20));
        colorLabel.setPreferredSize(new Dimension(69,20));
        colorButton.setPreferredSize(new Dimension(73,20));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        namePanel.add(nameInput);
        JPanel descriptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(descriptionScroll);
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(statusLabel);
        statusPanel.add(statusChoice);
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(dueDateLabel);
        datePanel.add(datePicker);
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        colorPanel.add(colorLabel);
        colorPanel.add(colorButton);
        JPanel confirmCancelPanel = new JPanel();
        confirmCancelPanel.add(confirmButton);
        confirmCancelPanel.add(cancelButton);

        mainPanel.add(namePanel);
        mainPanel.add(descriptionPanel);
        mainPanel.add(statusPanel);
        mainPanel.add(datePanel);
        mainPanel.add(colorPanel);
        mainPanel.add(confirmCancelPanel);

        colorButton.addActionListener((ActionEvent e) -> {
            Color color = JColorChooser.showDialog(this,"Select Task Color", colorButton.getBackground());
            colorButton.setBackground(color!=null?color:colorButton.getBackground());
        });
        confirmButton.addActionListener((ActionEvent e ) -> {
            confirm();
            this.dispose();
        });
        cancelButton.addActionListener((ActionEvent e) -> {
            this.dispose();
        });

        this.add(mainPanel);
        pack();
        setVisible(true);
    }

    /**
     * Method called when Create or Edit button is pressed.
     *
     * If Create, a new Task will be created with the values inputted into the Dialog's
     * fields. Then it will add the new Task into the ProjectModel.
     *
     * If Edit, the given Task will have its values changed to the values inputted
     * into the Dialog's field.
     */
    private void confirm()
    {
        taskModel.setName(nameInput.getText());
        taskModel.setDescription(descriptionInput.getText());
        taskModel.setStatus((String)statusChoice.getSelectedItem());
        Date date;
        if (mainModel == null)
        {
            GregorianCalendar c = (GregorianCalendar) datePicker.getModel().getValue();
            date = c==null?null:c.getTime();
        }
        else
        {
            date =(Date)datePicker.getModel().getValue();
        }
        taskModel.setDueDate(date);
        taskModel.setColor(colorButton.getBackground());

        if (confirmButton.getText()=="Create")
        {
            parent.getCurrProj().addTask(taskModel);
        }
        else
        {
            parent.getCurrProj().editTask(mainModel,taskModel);
        }
    }

}
