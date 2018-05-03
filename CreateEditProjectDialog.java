import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class CreateEditProjectDialog extends JDialog {


    private JLabel nameLabel = new JLabel("Project Name: ");
    private JTextField projectName;
    private JList<String> list;
    private ProjectModel projectModel = new ProjectModel("", null);
    private ProjectModel mainModel;
    private MainScreen parent;
    private JButton upButton = new JButton("Move Up");
    private JButton downButton = new JButton("Move Down");
    private JButton addButton = new JButton("Add Status");
    private JButton removeButton = new JButton("Remove Status");
    private JButton confirm = new JButton("Confirm");
    private JButton cancel = new JButton("Cancel");
    private JButton editButton = new JButton("Edit Status");


    /**
     * Private Constructor for CreateEditProjectDialog. Construct new Dialog from show method
     *
     * @param parent MainScreen of TaskBoard
     * @param model  ProjectModel to be edited, null if new Project is to be constructed
     */
    private CreateEditProjectDialog(MainScreen parent, ProjectModel model) {
        super(SwingUtilities.getWindowAncestor(parent));
        this.parent = parent;
        projectModel.copyFrom(model);
        mainModel = model;
        list = new JList<String>(projectModel);
        createGUI();
    }

    public static void show(MainScreen parent, ProjectModel projectModel) {
        new CreateEditProjectDialog(parent, projectModel);
    }

    private void createGUI() {
        this.setTitle((mainModel == null ? "Create" : "Edit") + " " + (mainModel == null ? "Project" : mainModel.getName()));
        confirm.setText(mainModel == null ? "Create" : "Edit");
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        projectName = new JTextField(mainModel == null ? ("Project " + (parent.getTaskBoardModel().numProjects() + 1)) : mainModel.getName());
        projectName.setPreferredSize(new Dimension(125, 26));

        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.add(projectName);
        panel.add(namePanel);

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        Dimension size = new Dimension(120, 26);
        upButton.setMinimumSize(size);
        downButton.setMinimumSize(size);
        addButton.setMinimumSize(size);
        removeButton.setMinimumSize(size);
        editButton.setMinimumSize(size);
        upButton.setMaximumSize(size);
        downButton.setMaximumSize(size);
        addButton.setMaximumSize(size);
        removeButton.setMaximumSize(size);
        editButton.setMaximumSize(size);
        list.setPreferredSize(new Dimension(90, 150));

        boxPanel.add(upButton);
        boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        boxPanel.add(downButton);
        boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        boxPanel.add(addButton);
        boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        boxPanel.add(removeButton);
        boxPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        boxPanel.add(editButton);


        JPanel statusPanel = new JPanel();
        statusPanel.add(boxPanel);
        statusPanel.add(list);
        panel.add(statusPanel);

        JPanel confirmCancelPanel = new JPanel();
        confirmCancelPanel.add(confirm);
        confirmCancelPanel.add(cancel);
        panel.add(confirmCancelPanel);


        upButton.addActionListener((ActionEvent e) -> {
            moveUp();
        });
        downButton.addActionListener((ActionEvent e) -> {
            moveDown();
        });
        addButton.addActionListener((ActionEvent e) -> {
            addStatus();
        });
        removeButton.addActionListener((ActionEvent e) -> {
                deleteStatus();
        });
        editButton.addActionListener((ActionEvent e) -> {
            changeName();
        });
        confirm.addActionListener((ActionEvent e) -> {
            projectModel.setName(projectName.getText());
            projectModel.addListener(parent);
            if (mainModel == null) {
                parent.getTaskBoardModel().addProjects(projectModel);
            }
            parent.getCurrProj().copyFrom(projectModel);
            parent.getCurrProj().update();
            this.dispose();
        });
        cancel.addActionListener((ActionEvent e) -> {
            this.dispose();
        });

        this.add(panel);


        pack();
        setVisible(true);
    }

    private void addStatus() {
        String name = JOptionPane.showInputDialog(this, "Enter new status name:", null);
        if (!"".equals(name)) {
            projectModel.addStatus(name);
            list.setSelectedIndex(projectModel.numStatuses() - 1);
            list.updateUI();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void changeName() {
        String name = JOptionPane.showInputDialog(this, "Enter new status name:", null);
        if (name != null) {
            projectModel.editStatus(list.getSelectedIndex(), name);
            list.updateUI();
        }
    }

    private void deleteStatus() {
        int index = list.getSelectedIndex();
        if (index == -1) {
            Toolkit.getDefaultToolkit().beep();
        } else if (JOptionPane.showConfirmDialog(this, "Are You Sure You Want to " +
                "Delete This Status?", "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            projectModel.deleteStatus(index);
            list.clearSelection();
            list.updateUI();
        }
    }

    private void moveUp() {
        int index = list.getSelectedIndex();
        if (index <= 0) {
            Toolkit.getDefaultToolkit().beep();
            return;
        } else {
            projectModel.swap(index, index - 1);
            list.setSelectedIndex(index - 1);
            list.updateUI();
        }
    }

    private void moveDown() {
        int index = list.getSelectedIndex();
        if (index == -1 || index == projectModel.numStatuses() - 1) {
            Toolkit.getDefaultToolkit().beep();
            return;
        } else {
            projectModel.swap(index, index + 1);
            list.setSelectedIndex(index + 1);
            list.updateUI();
        }
    }
}
