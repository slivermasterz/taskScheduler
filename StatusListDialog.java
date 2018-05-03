import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class StatusListDialog extends JDialog{

    private JList<String> list;
    private ProjectModel projectModel = new ProjectModel("",null);
    private MainScreen parent;
    private JButton upButton = new JButton("Move Up");
    private JButton downButton = new JButton("Move Down");
    private JButton addButton = new JButton("+");
    private JButton removeButton = new JButton("-");
    private JButton confirm = new JButton("Confirm");
    private JButton cancel = new JButton("Cancel");
    private JButton editButton = new JButton("Edit");


    public StatusListDialog(MainScreen parent)
    {
        super(SwingUtilities.getWindowAncestor(parent));
        this.parent = parent;
        projectModel.copyFrom(parent.getCurrProj());
        list = new JList<String>(projectModel);
        createGUI();
    }

    private void createGUI(){
        this.setLayout(new BorderLayout());
        this.add(list,BorderLayout.WEST);
        this.setModalityType(ModalityType.APPLICATION_MODAL);
        this.setModal(true);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1,BoxLayout.Y_AXIS));
        upButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        downButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p1.add(upButton);
        p1.add(Box.createRigidArea(new Dimension(0,5)));
        p1.add(downButton);

        removeButton.setToolTipText("Deletes selected status. Use with care");
        addButton.setToolTipText("Adds new status");

        JPanel p2 = new JPanel();
        p2.add(addButton);
        p2.add(removeButton);
        p1.add(p2);

        editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        p1.add(editButton);

        JPanel p3 = new JPanel();
        p3.add(confirm);
        p3.add(cancel);
        this.add(p3,BorderLayout.SOUTH);


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
            parent.getCurrProj().copyFrom(projectModel);
            this.dispose();
        });
        cancel.addActionListener((ActionEvent e)-> {
            this.dispose();
        });
        this.add(p1);

        pack();
        setVisible(true);
    }

    private void addStatus()
    {
        String name = JOptionPane.showInputDialog(this,"Enter new status name:",null );
        if (name != null)
        {
            System.out.println(projectModel.numStatuses());
            projectModel.addStatus(name);
            list.setSelectedIndex(projectModel.numStatuses()-1);
            list.updateUI();
        }
    }

    private void changeName()
    {
        String name = JOptionPane.showInputDialog(this,"Enter new status name:",null );
        if (name != null)
        {
            projectModel.editStatus(list.getSelectedIndex(),name);
            list.updateUI();
        }
    }

    private void deleteStatus()
    {
        int index = list.getSelectedIndex();
        if (index == -1)
        {
            Toolkit.getDefaultToolkit().beep();
        }
        else
        {
            projectModel.deleteStatus(index);
            list.setSelectedIndex(-1);
            list.updateUI();
        }
    }

    private void moveUp()
    {
        int index = list.getSelectedIndex();
        if (index <= 0)
        {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        else
        {
            projectModel.swap(index,index-1);
            list.setSelectedIndex(index-1);
            list.updateUI();
        }
    }

    private void moveDown()
    {
        int index = list.getSelectedIndex();
        if (index == -1 || index == projectModel.numStatuses()-1)
        {
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        else
        {
            projectModel.swap(index,index+1);
            list.setSelectedIndex(index+1);
            list.updateUI();
        }
    }
}
