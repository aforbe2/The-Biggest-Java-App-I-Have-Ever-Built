package c195;


public class Type {

    private String title;
    private String amount;
    

    public Type(String title, String amount) {

        this.title = title;
        this.amount = amount;
        
    }

    //getter
    
    public String getTitle() { return title; }
    public String getAmount() { return amount; }

    
    //setter
    
    public void setTitle(String title){ this.title = title; }
    public void setAmount(String amount){ this.amount = amount; }

}
    

