import java.io.Serializable;
import java.util.ArrayList;

/**
 * TaskMap is data structure that automatically sorts TaskModels into
 * due date order. Also automatically sorts TaskModels into respective
<<<<<<< HEAD
 *
 * Created by Lok Man Chu
=======
>>>>>>> abd75ee3e3dbde855016b7eb20c194e573234aab
 */
public class TaskMap implements Serializable
{
    /**
     * String ArrayList of status names
     */
    public ArrayList<String> statuses;
    /**
     * ArrayList of ArrayLists of TaskModels
     * Index of ArrayLists mirror those of statuses
     */
    public ArrayList<ArrayList<TaskModel>> taskGroups;
    private int size;

    /**
     * Constructor of TaskMap
     * Creates map based on String statuses given
     * @param statuses ArrayList of Strings that are the names of the statuses
     */
    public TaskMap(ArrayList<String> statuses)
    {
        this.statuses = new ArrayList<String>();
        taskGroups = new ArrayList<ArrayList<TaskModel>>();
        size = 0;
        statuses = statuses==null?new ArrayList<String>():statuses;
        for (String s : statuses)
        {
            addStatus(s);
        }
    }

    /**
     * Copy constructor for TaskMap
     * @param t TaskMap to copy from
     */
    public TaskMap(TaskMap t)
    {
        this.statuses = (ArrayList<String>) t.statuses.clone();
        this.taskGroups = (ArrayList<ArrayList<TaskModel>>) t.taskGroups.clone();
        this.size = t.size;
    }


    /**
     * Adds a status to end of TaskMap.
     * Also creates an ArrayList for TaskModels
     * @param status status String to add to TaskMap
     */
    public void addStatus(String status)
    {
        addStatus(statuses.size(),status);
    }

    /**
     * Adds a status to TaskMap at int Index
     * Also creates an ArrayList for TaskModels
     * @param index index of where to add status
     * @param status status String to add to TaskMap
     */
    public void addStatus(int index, String status)
    {
        statuses.add(index,status);
        taskGroups.add(index,new ArrayList<TaskModel>());
    }

    /**
     * Changes status name of status at index of statusArray to newStatus String
     * @param index index of status
     * @param newStatus new String name for status
     */
    public void editStatus(int index, String newStatus)
    {
        statuses.remove(index);
        statuses.add(index,newStatus);
        for (TaskModel t : taskGroups.get(index))
        {
            t.setStatus(newStatus);
        }
    }

    /**
     * Removes status from TaskMap
     * Also removes respective TaskGroup
     * @param index of status to remove from TaskMap
     */
    public void removeStatus(int index)
    {
        taskGroups.remove(index);
        statuses.remove(index);
    }

    /**
     * Adds TaskModel to TaskMap
     * TaskModel is inserted in ascending order
     * @param taskModel TaskModel to be added into TaskMap
     * @return true if added, false if not added
     */
    public Boolean addTask(TaskModel taskModel) {
        int groupIndex = statuses.indexOf(taskModel.getStatus());
        if (groupIndex == -1) {
            return false;
        }

        ArrayList<TaskModel> a = taskGroups.get(groupIndex);
        int index = -1;
        for (TaskModel other : a) {
            index = taskModel.compareTo(other) > 0 ? a.indexOf(other) : index;
        }
        a.add(index + 1, taskModel);
        size++;
        return true;
    }

    /**
     * Removes TaskModel from TaskMap
     * @param taskModel TaskModel to be removed
     * @return true if removed, false if not
     */
    public Boolean removeTask(TaskModel taskModel)
    {
        int groupIndex = statuses.indexOf(taskModel.getStatus());
        if (groupIndex == -1)
        {
            return false;
        }
        if (taskGroups.get(groupIndex).remove(taskModel))
        {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Swaps the position of two statuses
     * @param x index of first status
     * @param y index of second status
     * @return true if swapped, false if not
     */
    public Boolean swapStatusIndex(int x, int y)
    {
        if (x >= statuses.size() || y >= statuses.size())
        {
            return false;
        }
        String temp = statuses.get(x);
        statuses.add(x,statuses.get(y));
        statuses.remove(x+1);
        statuses.add(y,temp);
        statuses.remove(y+1);

        ArrayList<TaskModel> tempArr = taskGroups.get(x);
        taskGroups.add(x,taskGroups.get(y));
        taskGroups.remove(x+1);
        taskGroups.add(y,tempArr);
        taskGroups.remove(y+1);

        return true;
    }

    /**
     * Number of TaskModels in TaskMap
     * @return int number TaskModels
     */
    public int size()
    {
        return size;
    }

}
