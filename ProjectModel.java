import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * ProjectModel is a collection of TaskModels representing a Project
 * ProjectModel will automatically sort all TaskModels added to it.
 *
 * Created by Lok Chu
 */
public class ProjectModel implements Serializable, ListModel<String>{

    /**
     * Name of Project
     */
    private String name;
    /**
     * TaskMap data structure holding all TaskModels
     */
    private TaskMap map;
    /**
     * Listeners of ProjectModel
     */
    private ArrayList<TaskModelListener> listeners = new ArrayList<TaskModelListener>();

    /**
     * Constructor of ProjectModel
     * @param name Name of Project
     * @param statuses Statuses to add to ProjectModel
     */
    public ProjectModel(String name, ArrayList<String> statuses) {
        this.name = name;
        map = new TaskMap(statuses);
    }

    /**
     * Copies all values from given ProjectModel to this ProjectModel
     * @param projectModel ProjectModel to copy data from
     */
    public void copyFrom(ProjectModel projectModel)
    {
        if (projectModel == null)
        {
            projectModel = new ProjectModel("",null);
        }
        this.name = projectModel.name;
        this.map = new TaskMap(projectModel.map);
    }

    /**
     * Adds status to ProjectModel. Also adds respective TaskModel list
     * @param status String name of status being added
     */
    public void addStatus(String status) {
        map.addStatus(status);
        update();
    }

    /**
     * Changes the value of status at index
     * @param index index of Status to be changed
     * @param newStatus new String name of Status
     */
    public void editStatus(int index, String newStatus)
    {
        map.editStatus(index,newStatus);
        update();
    }

    /**
     * Removes status from ProjectModel
     * Also removes Tasks within status
     * @param index index of status
     */
    public void deleteStatus(int index)
    {
        map.removeStatus(index);
        update();
    }

    /**
     * Adds TaskModel to ProjectModel
     * @param taskModel TaskModel to be added
     * @return true if added successfully. false if not
     */
    public Boolean addTask(TaskModel taskModel) {
        Boolean result = map.addTask(taskModel);
        update();
        return result;
    }

    /**
     * Creates a new TaskModel
     * @return TaskModel with default parameters
     */
    public TaskModel createTask() {
        String taskName = "Task" + (map.size() + 1);
        return new TaskModel(taskName, "", null, topStatus());
    }

    /**
     * Creates a TaskModel with given parameters
     * @param name String name of TaskModel
     * @param des String description of TaskModel
     * @param date Date due date of TaskModel
     * @param status String status of TaskModel
     * @return TaskModel with given parameters.
     */
    public TaskModel createTask(String name, String des, Date date, String status) {
        return new TaskModel(name, des, date, status);
    }


    /**
     * Changes oldTask to newTask
     * @param oldTask TaskModel to be changed
     * @param newTask modified TaskModel
     * @return false if oldTask cannot be found
     */
    public Boolean editTask(TaskModel oldTask, TaskModel newTask) {
        if (map.removeTask(oldTask)) {
            map.addTask(newTask);
            update();
            return true;
        }
        return false;
    }

    /**
     * Deletes TaskModel from ProjectModel
     * @param taskModel TaskModel to be removed
     * @return true if removed, false if not
     */
    public Boolean deleteTask(TaskModel taskModel) {
        if (map.removeTask(taskModel)) {
            update();
            return true;
        }
        return false;
    }

    /**
     * Swaps status positions of given indexes.
     * @param x index of statusA
     * @param y index of statusB
     */
    public void swap(int x, int y)
    {
        map.swapStatusIndex(x,y);
        update();
    }

    /**
     * gets the ArrayList of TaskModels within a status
     * @param index Index of status to get
     * @return ArrayList of TaskModels of status index
     */
    public ArrayList<TaskModel> getCategory(int index)
    {
        return map.taskGroups.get(index);
    }

    /**
     * Gets status name at index
     * @param index index of status
     * @return String status name
     */
    public String getStatus(int index)
    {
        return map.statuses.get(index);
    }

    /**
     * Gives the name of the first status
     * @return index 0 of statuses
     */
    public String topStatus() {
        return map.statuses.get(0);
    }

    /**
     * Returns number of Statuses in Project
     * @return numStatus of ProjectModel
     */
    public int numStatuses()
    {
        return map.statuses.size();
    }

    /**
     * Get the ArrayList of Statuses
     * @return ArrayList<String> of statuses
     */
    public ArrayList<String> getStatuses()
    {
        return map.statuses;
    }

    /**
     * Updates all TaskModelListeners
     */
    public void update() {
        for (TaskModelListener l : listeners) {
            l.update();
        }
    }

    /**
     * Add TaskModelListener to listerners
     * @param l TaskModelListener to be added
     */
    public void addListener(TaskModelListener l)
    {
        listeners.add(l);
    }

    /**
     * Get's name of ProjectModel
     * @return String name of ProjectModel
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of ProjectModel
     * @param name new String name of ProjectModel
     */
    public void setName(String name) {
        this.name = name;
    }

   @Override
   /**
    * method required for ListModel
    */
    public int getSize() {
        return numStatuses();
    }

    @Override
    /**
     * method required for ListModel
     */
    public String getElementAt(int index) {
        return map.statuses.get(index);
    }

    @Override
    /**
     * Unused
     */
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    /**
     * Unused
     */
    public void removeListDataListener(ListDataListener l) {

    }
}
