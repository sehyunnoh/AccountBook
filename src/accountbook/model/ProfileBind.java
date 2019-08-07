package accountbook.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProfileBind {

    private final StringProperty name = new SimpleStringProperty("friend");
    private final DoubleProperty balance = new SimpleDoubleProperty(0.00);

    public ProfileBind() {
    }

    public final void setBalance(double balance) {
        if (balance > 0) {
            this.balance.set(balance);
        } else {
            throw new IllegalArgumentException("Error: balance must be lager than 0");
        }
    }

    public final double getBalance() {
        return balance.get();
    }

    public final void setName(String name){
        this.name.set(name);
    }
    
    public final String getName(){
        return name.get();
    }
    
    public final StringProperty nameProperty(){
        return name;
    }
    
    public final DoubleProperty balanceProperty(){
        return balance;
    }
}
