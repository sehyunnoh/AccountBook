package accountbook.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Day implements Comparable<Day>{

    private SimpleIntegerProperty no;
    private SimpleStringProperty category;
    private SimpleStringProperty desc;
    private SimpleStringProperty pMethod;
    private SimpleDoubleProperty amt;
    private SimpleStringProperty date;
    private SimpleStringProperty type;
    private SimpleStringProperty pMethodDetail;

    public Day(int no, String category, String desc, String pMethod, Double amt) {
        this.no = new SimpleIntegerProperty(no);
        this.category = new SimpleStringProperty(category);
        this.desc = new SimpleStringProperty(desc);
        this.pMethod = new SimpleStringProperty(pMethod);
        this.amt = new SimpleDoubleProperty(amt);
    }
    
    public Day(int no, String date, String type, String category, String desc, String pMethod, String pMethodDetail, Double amt) {
        this.no = new SimpleIntegerProperty(no);
        this.category = new SimpleStringProperty(category);
        this.desc = new SimpleStringProperty(desc);
        this.pMethod = new SimpleStringProperty(pMethod);
        this.amt = new SimpleDoubleProperty(amt);
        this.date = new SimpleStringProperty(date);
        this.type = new SimpleStringProperty(type);
        this.pMethodDetail = new SimpleStringProperty(pMethodDetail);
    }

    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public String getCategory() {
        return category.get();
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getDesc() {
        return desc.get();
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public String getPMethod() {
        return pMethod.get();
    }

    public void setPMethod(String pMethod) {
        this.pMethod.set(pMethod);
    }

    public double getAmt() {
        return amt.get();
    }

    public void setAmt(double amt) {
        this.amt.set(amt);
    }
    
    public String getDate(){
        return date.get();
    }
    
    public void setDate(String date){
        this.date.set(date);
    }

    public String getType(){
        return type.get();
    }
    
    public void setType(String type){
        this.type.set(type);
    }
    
    public String getPMethodDetail(){
        return pMethodDetail.get();
    }
    
    public void setPMethodDetail(String pMethodDetail){
        this.pMethodDetail.set(pMethodDetail);
    }

    @Override
    public int compareTo(Day day) {
        return this.date.get().compareTo(day.getDate());
    }

}
