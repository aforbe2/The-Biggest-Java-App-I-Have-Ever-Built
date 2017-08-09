package c195;


public class Schedule {

    private String appointmentId;
    private String title;
    private String description;
    private String location;
    private String start;
    private String end;

    

    public Schedule(String appointmentId, String title, String description, String location, String start, String end) {

        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
    }
    

    //getter
    
    public String getAppointmentId() { return appointmentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    
    //setter
    
    public void setAppointmentId(String appointmentId){ this.appointmentId = appointmentId; }
    public void setTitle(String title){ this.title = title; }
    public void setDescription(String description){ this.description = description; }
    public void setLocation(String location){ this.location = location; }    
    public void setStart(String start){ this.start = start; }
    public void setEnd(String end){ this.end = end; }
}
