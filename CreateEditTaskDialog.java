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

    public static void show(MainScreen parent, TaskModel model)
    {
        new CreateEditTaskDialog(parent, model, null);
    }

    public static void show(MainScreen parent, String status)
    {
        new CreateEditTaskDialog(parent,null, status);
    }

    private CreateEditTaskDialog(MainScreen parent, TaskModel model, String status) {
        super(SwingUtilities.getWindowAncestor(parent));
        this.parent = parent;
        mainModel = model;
        taskModel = mainModel == null?parent.getCurrProj().createTask():new TaskModel(mainModel);
        taskModel.setStatus(status);
        statusChoice = new JComboBox<Object>(parent.getCurrProj().getStatuses().toArray());
        createGUI();
    }

    private void createGUI() {

        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        this.setResizable(false);

        this.setTitle((mainModel==null?"Create":"Edit")+ " " + (mainModel==null?"Task":mainModel.getName()));
        confirmButton.setText(mainModel==null?"Create":"Edit");
        descriptionInput.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        descriptionInput.setLineWrap(true);


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

        System.out.println(descriptionInput.getSize());
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
