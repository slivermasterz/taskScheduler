import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class FileHandler {

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
                return (TaskBoardModel) in.readObject();
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }

        } else {
            System.out.println("Cancelled Load");
            return null;
        }
    }

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
