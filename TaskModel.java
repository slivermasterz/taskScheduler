import javafx.concurrent.Task;

import java.awt.*;
import java.io.Serializable;
import java.util.Date;

/**
 * TaskModel is a model for a Task that contains the name, description, due date, and status of the Task.
 *
 * Created by Lok Chu
 */
public class TaskModel implements Comparable<TaskModel>, Serializable{

    /**
     * Name of Task
     */
    private String name;
    /**
     * Description of Task
     */
    private String description;
    /**
     * Due Date of Task
     */
    private Date dueDate;
    /**
     * Status of Task
     */
    private String status;

    /**
     * Color of Task
     */
    private Color color;

    /**
     * Constructor for TaskModel
     * @param name name of Task
     * @param description description of Task
     * @param date dueDate of Task
     * @param status status of Task
     * @param color color of Task
     */
    public TaskModel(String name, String description, Date date, String status, Color color)
    {
        this.name = name;
        this.description = description;
        this.dueDate = date;
        this.status = status;
        this.color = color == null?Color.pink:color;
    }

    /**
     * Constructor for TaskModel
     * @param name name of Task
     * @param description description of Task
     * @param date dueDate of Task
     * @param status status of Task
     */
    public TaskModel(String name, String description, Date date, String status)
    {
        this(name,description,date,status,null);
    }

    public TaskModel(TaskModel model)
    {
        this.name = model.name;
        this.description = model.description;
        this.status = model.status;
        this.dueDate = model.dueDate;
        this.color = model.color;
    }

    /**
     * Compares this TaskModel to other TaskModel.
     * returning >1 if greater, 0 if equal, and <-1 if less than
     * Compares dueDates of TaskModels, and if the dueDates are the same, compares the names.
     * If dueDate is null and other dueDate isn't null, null is greater
     * @param other TaskModel to compare to
     * @return positive int if greater, 0 if equal, and negative int if less than
     */
    public int compareTo(TaskModel other) {
        System.out.println();
        if (dueDate == null && other.dueDate != null) {
            return 1;
        } else if (dueDate != null && other.dueDate == null) {
            return -1;
        }
        if (dueDate == other.dueDate || dueDate.equals(other.dueDate)) {
            return name.compareTo(other.name);
        } else {
            return dueDate.compareTo(other.dueDate);
        }
    }

    /**
     * Set method for Task name
     * @param name new name for Task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set method for Task description
     * @param description new description for Task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set method for Task dueDate
     * @param dueDate new dueDate for Task
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Set method for Task status
     * @param status new status for Task
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Set method for Task color
     * @param color new Color of Task
     */
    public void setColor(Color color){
        this.color = color;
    }

    /**
     * Get method for Task name
     * @return String name of Task
     */
    public String getName() {
        return name;
    }

    /**
     * Get method for Task dueDate
     * @return Date dueDate of Task
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * Get method for Task description
     * @return String description of Task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get method for Task status
     * @return String status of Task
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get method for Task Color
     * @return color of Task
     */
    public Color getColor()
    {
        return color;
    }

    /**
     * String representation of TaskModel
     * @return TaskModel name + dueDate + status
     */
    public String toString() {
        return name + " " + dueDate + " " + status;
    }
}
