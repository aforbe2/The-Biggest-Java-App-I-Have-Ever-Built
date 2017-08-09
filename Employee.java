package c195;


public class Employee {
    
    private String userId;
    private String userName;
    private String password;
    private String active;

    

    public Employee(String userId, String userName, String password, String active) {

        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }
    

    //getter
    
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getActive() { return active; }

    
    //setter
    
    public void setUserId(String userId){ this.userId = userId; }
    public void setUserName(String userName){ this.userName = userName; }
    public void setPassword(String password){ this.password = password; }
    public void setActive(String active){ this.active = active; }

}
