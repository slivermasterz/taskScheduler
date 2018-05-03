import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class TaskBoardModel implements Serializable {

    private String name;
    private ArrayList<ProjectModel> projects;
    private File file;

    /**
     * Constructor of TaskBoardModel
     * Assigns default values to all fields
     */
    public TaskBoardModel()
    {
        name = "TaskBoard1";
        projects = new ArrayList<ProjectModel>();
        file = null;
    }

    /**
     * Set method for TaskBoardModel name
     * @param name new String name for TaskBoardModel
     */
    public void setName(String name) {
        this.name = name;
    }


    public String getName()
    {
        return name;
    }

    public void addProjects(ProjectModel projectModel)
    {
        projects.add(projectModel);
    }

    public void deleteProjects(ProjectModel projectModel)
    {
        projects.remove(projectModel);
    }

    public int numProjects()
    {
        return projects.size();
    }

    public ProjectModel getProject(int index)
    {
        return projects.get(index);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
