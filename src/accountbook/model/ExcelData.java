package accountbook.model;

public class ExcelData {

    private String no = "";
    private String date = "";
    private String category = "";
    private String desc = "";
    private String cash = "";
    private String card = "";
    private String bank = "";
    private double expense = 0;
    private double income = 0;
    private double balance = 0;

    public ExcelData() {
    }

    public ExcelData(String no, String date, String category, String desc, String cash, String card, String bank, double expense, double income, double balance) {
        this.no = no;
        this.date = date;
        this.category = category;
        this.desc = desc;
        this.cash = cash;
        this.card = card;
        this.bank = bank;
        this.expense = expense;
        this.income = income;
        this.balance = balance;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
