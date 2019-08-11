package accountbook.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Month {

    private SimpleIntegerProperty no;
    private SimpleStringProperty date;
    private SimpleStringProperty category;
    private SimpleStringProperty desc;
    private SimpleStringProperty cash;
    private SimpleStringProperty card;
    private SimpleStringProperty bank;
    private SimpleDoubleProperty expense;
    private SimpleDoubleProperty income;
    private SimpleDoubleProperty balance;

    public Month(int no, String date, String category, String desc, String cash, String card, String bank, double expense, double income, double balance) {
        this.no = new SimpleIntegerProperty(no);
        this.date = new SimpleStringProperty(date);
        this.category = new SimpleStringProperty(category);
        this.desc = new SimpleStringProperty(desc);
        this.cash = new SimpleStringProperty(cash);
        this.bank = new SimpleStringProperty(bank);
        this.card = new SimpleStringProperty(card);
        this.expense = new SimpleDoubleProperty(expense);
        this.income = new SimpleDoubleProperty(income);
        this.balance = new SimpleDoubleProperty(balance);
    }

    public int getNo() {
        return no.get();
    }

    public void setNo(int no) {
        this.no.set(no);
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
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

    public String getCash() {
        return cash.get();
    }

    public void setCash(String cash) {
        this.cash.set(cash);
    }

    public String getCard() {
        return card.get();
    }

    public void setCard(String card) {
        this.card.set(card);
    }

    public String getBank() {
        return bank.get();
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public double getExpense() {
        return expense.get();
    }

    public void setExpense(double expense) {
        this.expense.set(expense);
    }

    public double getIncome() {
        return income.get();
    }

    public void setIncome(double income) {
        this.income.set(income);
    }

    public double getBalance() {
        return balance.get();
    }

    public void setBalance(double balance) {
        this.balance.set(balance);
    }

}
