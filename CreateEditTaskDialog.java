import javax.swing.*;
import org.jdatepicker.JDatePicker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateEditTaskDialog extends JDialog {
    private JLabel nameLabel = new JLabel("Name: ");
    private JTextField nameInput = new JTextField(15);
    private JLabel descriptionLabel = new JLabel("Description: ");
    private JTextArea descriptionInput = new JTextArea(5,15);
    JScrollPane descriptionScroll = new JScrollPane(descriptionInput);
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

    public static void show(MainScreen parent, TaskModel model)
    {
        new CreateEditTaskDialog(parent, model);
    }

    private CreateEditTaskDialog(MainScreen parent, TaskModel model) {
        super(SwingUtilities.getWindowAncestor(parent));
        this.parent = parent;
        mainModel = model;
        taskModel = mainModel == null?parent.getCurrProj().createTask():new TaskModel(mainModel);
        statusChoice = new JComboBox<Object>(parent.getCurrProj().getStatuses().toArray());
        createGUI();
    }

    private void createGUI() {

        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);

        this.setTitle((mainModel==null?"Create":"Edit")+ " " + (mainModel==null?"Task":mainModel.getName()));
        confirmButton.setText(mainModel==null?"Create":"Edit");
        descriptionInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        descriptionInput.setLineWrap(true);


        nameInput.setText(taskModel.getName());
        descriptionInput.setText(taskModel.getDescription());
        colorButton.setBackground(taskModel.getColor());
        statusChoice.setSelectedIndex(mainModel==null?0:parent.getCurrProj().getStatuses().indexOf(taskModel.getStatus()));
        datePicker = (mainModel==null || mainModel.getDueDate() == null)?new JDatePicker():new JDatePicker(mainModel.getDueDate());

        int width = 274;
        //nameInput.setHorizontalAlignment();
        datePicker.setMaximumSize(new Dimension(width,95));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(nameInput);
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.add(descriptionLabel);
        descriptionPanel.add(descriptionScroll);
        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);
        statusPanel.add(statusChoice);
        JPanel datePanel = new JPanel();
        datePanel.add(dueDateLabel);
        datePanel.add(datePicker);
        JPanel colorPanel = new JPanel();
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

        System.out.println(descriptionPanel.getSize());
    }

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
