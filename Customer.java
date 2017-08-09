package c195;

public class Customer {
    
    private String customerId;
    private String customerName;
    private String streetAddress;
    private String city;
    private String addressId;
    private String zipCode;
    private String phoneNumber;
    private String country;
    

    public Customer(String customerId, String customerName, String phoneNumber, String streetAddress, String city, String addressId, String zipCode) {

        this.customerId = customerId;
        this.customerName = customerName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.addressId = addressId;
        this.zipCode = zipCode;
        this.phoneNumber = phoneNumber;
        country = new String();
    }
    
    public Customer(String customerId){
    
        this.customerId = customerId;
    }
    
    public Customer(String customerId, String customerName, String phoneNumber){
    
        this.customerId = customerId;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
    }

    //getter
    
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getStreetAddress() { return streetAddress; }
    public String getCity() { return city; }
    public String getAddressId() { return addressId; }
    public String getZipCode() { return zipCode; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getCountry() { return country; }
    
    //setter
    
    public void setCustomerId(String customerId){ this.customerId = customerId; }
    public void setCustomerName(String customerName){ this.customerName = customerName; }
    public void setStreetAddress(String streetAddress){ this.streetAddress = streetAddress; }
    public void setCity(String city){ this.city = city; }
    public void setAddressId(String addressId){ this.addressId = addressId; }
    public void setZipCode(String zipCode){ this.zipCode = zipCode; }
    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber; }
    public void setCountry(String country) { this.country = country; }
    
}
