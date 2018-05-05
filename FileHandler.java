import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

/**
 * FileHandler deals with saving and loading of TaskBoardModels through Serialization
 * FileHandler does so through static loadTaskBoard and saveTaskBoard methods.
 */
public class FileHandler {

    /**
     * Static method to load a TaskBoard.
     * loadTaskBoard will prompt a user with a JFileChooser, in which a user needs to
     * select a ".taskboard" file. If a taskboard file is selected, method will return with a
     * TaskBoardModel.
     * If the user cancels their selection, null will be returned instead.
     * @return TaskBoard chosen by user. null if Dialog is canceled.
     */
    public static TaskBoardModel loadTaskBoard() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to load");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".taskboard files", "taskboard");
        fileChooser.setFileFilter(filter);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Load file: " + file.getAbsolutePath());
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                TaskBoardModel m = (TaskBoardModel) in.readObject();
                m.setFile(file);
                return m;
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }

        } else {
            System.out.println("Cancelled Load");
            return null;
        }
    }

    /**
     * Static method to save a TaskBoard
     *
     * If the user has not saved the taskBoardModel before, a JFileChooser
     * will prompt the user for a save location.
     *
     * If the file has been saved before, saveTaskBoard will save the TaskBoard
     *
     * @param taskBoardModel
     */
    public static void saveTaskBoard(TaskBoardModel taskBoardModel) {
        File file = taskBoardModel.getFile();
        if (file == null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save");
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    ".taskboard files", "taskboard");
            fileChooser.setFileFilter(filter);
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));
            fileChooser.setSelectedFile(new File(taskBoardModel.getName() + ".taskboard"));

            int userSelection = fileChooser.showSaveDialog(null);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                taskBoardModel.setFile(file);
                taskBoardModel.setName(file.getName().split("\\.")[0]);
                System.out.println("Save as file: " + file.getAbsolutePath());
            } else {
                System.out.println("Canceled Save");
                return;
            }
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(taskBoardModel);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
