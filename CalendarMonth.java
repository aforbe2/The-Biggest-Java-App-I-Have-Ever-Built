package c195;

public class CalendarMonth {
    
    private String appointmentId;
    private String customerId;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String url;
    private String start;
    private String end;
    

    public CalendarMonth(String appointmentId, String customerId, String title, String description, String location, String contact, String url, String start, String end) {

        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.url = url;
        this.start = start;
        this.end = end;
        
    }
    

    //getter
    
    public String getAppointmentId() { return appointmentId; }
    public String getCustomerId() { return customerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getContact() { return contact; }
    public String getUrl() { return url; }
    public String getStart() { return start; }
    public String getEnd() { return end; }
    
    
    //setter
    
    public void setAppointmentId(String appointmentId){ this.appointmentId = appointmentId; }
    public void setCustomerId(String customerId){ this.customerId = customerId; }
    public void setTitle(String title){ this.title = title; }
    public void setDescription(String description){ this.description = description; }
    public void setLocation(String location){ this.location = location; }
    public void setContact(String contact){ this.contact = contact; }
    public void setUrl(String url){ this.url = url; }
    public void setStart(String start) { this.start = start; }
    public void setEnd(String end) { this.end = end; }
    
}
