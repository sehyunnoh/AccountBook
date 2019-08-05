package accountbook;

import accountbook.info.*;
import accountbook.model.*;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AccountBookController implements Initializable {

    private final String path = "./src/accountbook/file/";
    private final double startBalance = 50000;
    private double balance = startBalance;

    private String date;
    private String type;
    private String category;
    private String desc;
    private String pMethod;
    private String pMethodDetail;
    private double amt;
    private ArrayList<String> typeList;
    private ArrayList<String> pMethodList;
    private ArrayList<Month> alMonth;
    private int monthchanged;

    @FXML
    private Label lblSBalance, lblNBalance;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField txtDesc, txtAmt, txtMdesc;

    @FXML
    private ComboBox<String> cmbCategory;

    @FXML
    private ComboBox<String> cmbPayMethod;

    @FXML
    private ToggleGroup grpType, grpPayMethod;

    @FXML
    private RadioButton radioOut, radioCash;

    @FXML
    private TableView tvOutCome, tvInCome, tvMonth;

    @FXML
    private TableColumn tcONo, tcOCtg, tcODesc, tcOMethod, tcOAmt, tcINo, tcICtg, tcIDesc, tcIMethod, tcIAmt,
            tcMNo, tcMDate, tcMCtg, tcMDesc, tcMCash, tcMCard, tcMBank, tcMIncome, tcMOutcome, tcMBal;

    @FXML
    private void selectedDate(ActionEvent event) throws IOException {
        displayDay(datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE));
        displayMonth(datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @FXML
    private void btnToday(ActionEvent event) throws IOException {
        datePicker.setValue(LocalDate.now());
        displayDay(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        displayMonth(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    @FXML
    private void btnInfoRefresh(ActionEvent event) {
        refresh();
    }

    @FXML
    private void btnInfoSave(ActionEvent event) throws IOException {
        date = datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE);
        type = ((RadioButton) grpType.getSelectedToggle()).getText();
        category = cmbCategory.getSelectionModel().getSelectedItem();
        desc = txtDesc.getText();
        pMethod = ((RadioButton) grpPayMethod.getSelectedToggle()).getText();
        pMethodDetail = cmbPayMethod.getSelectionModel().getSelectedItem();

        boolean check = true;
        String erroMsg = "";

        try {
            amt = Double.parseDouble(txtAmt.getText());
        } catch (Exception e) {
            erroMsg = "Amount field is accepted the only number";
            check = false; 
        }

        if ("".equals(desc) || "".equals(txtAmt.getText())) {
            erroMsg = "Empty field is not accepted";
            check = false;
        } else if (!desc.matches("[a-zA-Z0-9]+")) {
            erroMsg = "Only letters and numbers are allowed.";
            check = false;
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Entry Error");
            alert.setHeaderText("Invalid Value Entered");
            alert.setContentText(erroMsg);
            alert.showAndWait();
        } else {
            File file = new File(makePath(date));
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(fw);

            pw.print(makeRow(date, type, category, desc, pMethod, pMethodDetail, "" + amt));
            pw.close();

            refresh();
            displayDay(date);
            displayMonth(date);
        }

    }

    @FXML
    private void btnOutcomeAction(ActionEvent event) {
        typeList.clear();
        for (OutType o : OutType.values()) {
            typeList.add(o.toString());
        }
        typeCombo(typeList);
    }

    @FXML
    private void btnIncomeAction(ActionEvent event) {
        typeList.clear();
        for (InType i : InType.values()) {
            typeList.add(i.toString());
        }
        typeCombo(typeList);
    }

    @FXML
    private void btnCashAction(ActionEvent event) {
        pMethodList.clear();
        pMethodList.add("Cash");
        paymentMethodCombo(pMethodList, true);
    }

    @FXML
    private void btnBankAction(ActionEvent event) {
        pMethodList.clear();
        for (Bank b : Bank.values()) {
            pMethodList.add(b.toString());
        }
        paymentMethodCombo(pMethodList, false);
    }

    @FXML
    private void btnCardAction(ActionEvent event) {
        pMethodList.clear();
        for (Card c : Card.values()) {
            pMethodList.add(c.toString());
        }
        paymentMethodCombo(pMethodList, false);
    }

    @FXML
    private void btnMRefresh(ActionEvent event) throws IOException {
        displayMonth(date);
    }

    @FXML
    private void btnMSearch(ActionEvent event) throws IOException {
        ArrayList<Month> tmp = new ArrayList();
        String tmpDesc = txtMdesc.getText().toLowerCase();

        for (Month m : alMonth) {
            if ("".equals(tmpDesc) || m.getDesc().trim().toLowerCase().matches(".*" + tmpDesc + ".*")) {
                tmp.add(m);
            }
        }

        displayMonthFromArray(tmp);
        monthchanged++;
    }

    @FXML
    private void btnMDelete(ActionEvent event) {
        tvMonth.getItems().removeAll(tvMonth.getSelectionModel().getSelectedItem());
        monthchanged++;
    }

    @FXML
    private void btnMDeleteAll(ActionEvent event) {
        tvMonth.getItems().clear();
        monthchanged++;
    }

    @FXML
    private void btnMPrev(ActionEvent event) {
        tvMonth.getSelectionModel().selectPrevious();
    }

    @FXML
    private void btnMNext(ActionEvent event) {
        tvMonth.getSelectionModel().selectNext();
    }

    @FXML
    private void btnMReport(ActionEvent event) throws IOException {

        if (monthchanged > 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to save your changes?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                btnMSave(new ActionEvent());
            }

        }

        Parent reportParent = FXMLLoader.load(getClass().getResource("Report.fxml"));
        Scene reportScene = new Scene(reportParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

        window.setScene(reportScene);
        window.show();

    }

    @FXML
    private void btnMSave(ActionEvent event) throws IOException {
        ObservableList<Month> changedData = tvMonth.getItems();
        PrintWriter writer = new PrintWriter(makePath(date));
        writer.print("");
        writer.close();

        File file = new File(makePath(date));
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(fw);

        for (int i = 0; i < changedData.size(); i++) {
            Month m = changedData.get(i);

            date = m.getDate();
            if (m.getIncome() != 0) {
                type = "Income";
            } else {
                type = "Outcome";
            }
            category = m.getCategory();
            desc = m.getDesc();
            if (!"".equals(m.getCash())) {
                pMethod = "Cash";
                pMethodDetail = "Cash";
            } else if (!"".equals(m.getCard())) {
                pMethod = "Card";
                pMethodDetail = m.getCard();
            } else {
                pMethod = "Bank";
                pMethodDetail = m.getBank();
            }
            amt = m.getIncome() + m.getOutcome();

            pw.print(makeRow(date, type, category, desc, pMethod, pMethodDetail, "" + amt));
        }

        pw.close();

        refresh();
        displayDay(date);
        displayMonth(date);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblSBalance.setText("" + startBalance);
        datePicker.setValue(LocalDate.now());
        typeList = new ArrayList();
        btnOutcomeAction(new ActionEvent());
        pMethodList = new ArrayList();
        btnCashAction(new ActionEvent());

        try {
            displayDay(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            displayMonth(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        } catch (Exception e) {
        }

    }

    private void refresh() {
        radioOut.setSelected(true);
        btnOutcomeAction(new ActionEvent());
        txtDesc.setText("");
        radioCash.setSelected(true);
        txtAmt.setText("");
        btnCashAction(new ActionEvent());
    }

    private void typeCombo(ArrayList al) {
        ObservableList<String> olType = FXCollections.observableArrayList(al);
        cmbCategory.setItems(olType);
        cmbCategory.getSelectionModel().selectFirst();
    }

    private void paymentMethodCombo(ArrayList al, boolean b) {
        ObservableList<String> olPayMethod = FXCollections.observableArrayList(al);
        cmbPayMethod.setItems(olPayMethod);
        cmbPayMethod.setDisable(b);
        cmbPayMethod.getSelectionModel().selectFirst();
    }

    /**
     * make row from multiple strings
     *
     * @param str
     * @return
     */
    private String makeRow(String... str) {
        String result = "";
        for (int i = 0; i < str.length - 1; i++) {
            result += str[i] + ",";
        }
        return result += str[str.length - 1] + "\n";
    }

    private String makePath(String date) {
        return path + date.substring(0, 6) + ".csv";
    }

    private void displayDay(String day) throws IOException {
        File file = new File(makePath(day));

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            ArrayList<Day> alOutcome = new ArrayList();
            ArrayList<Day> alIncome = new ArrayList();

            int outNum = 1, inNum = 1;
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] fld = str.split("\\s*,\\s*");

                date = fld[0];

                if (!date.equals(day)) {
                    continue;
                }

                type = fld[1];
                category = fld[2];
                desc = fld[3];
                pMethod = fld[4];
                pMethodDetail = fld[5];
                amt = Double.parseDouble(fld[6]);

                if (type.equals("Outcome")) {
                    alOutcome.add(new Day(outNum++, category, desc, pMethod, amt));
                } else {
                    alIncome.add(new Day(inNum++, category, desc, pMethod, amt));
                }
            }

            ObservableList<Day> olOutcome = FXCollections.observableArrayList(alOutcome);
            tcONo.setCellValueFactory(new PropertyValueFactory<Day, Integer>("no"));
            tcOCtg.setCellValueFactory(new PropertyValueFactory<Day, String>("category"));
            tcODesc.setCellValueFactory(new PropertyValueFactory<Day, String>("desc"));
            tcOMethod.setCellValueFactory(new PropertyValueFactory<Day, String>("pMethod"));
            tcOAmt.setCellValueFactory(new PropertyValueFactory<Day, Double>("amt"));
            tvOutCome.setItems(olOutcome);

            ObservableList<Day> olIncome = FXCollections.observableArrayList(alIncome);
            tcINo.setCellValueFactory(new PropertyValueFactory<Day, Integer>("no"));
            tcICtg.setCellValueFactory(new PropertyValueFactory<Day, String>("category"));
            tcIDesc.setCellValueFactory(new PropertyValueFactory<Day, String>("desc"));
            tcIMethod.setCellValueFactory(new PropertyValueFactory<Day, String>("pMethod"));
            tcIAmt.setCellValueFactory(new PropertyValueFactory<Day, Double>("amt"));
            tvInCome.setItems(olIncome);
        }
    }

    public void displayMonth(String day) throws IOException {
        balance = startBalance;
        File file = new File(makePath(day));

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            ArrayList<Day> tmp = new ArrayList();
            alMonth = new ArrayList();

            int tmpNo = 1;
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                String[] fld = str.split("\\s*,\\s*");

                date = fld[0];
                type = fld[1];
                category = fld[2];
                desc = fld[3];
                pMethod = fld[4];
                pMethodDetail = fld[5];
                amt = Double.parseDouble(fld[6]);

                tmp.add(new Day(tmpNo, date, type, category, desc, pMethod, pMethodDetail, amt));
            }

            tmp.sort(null);
            int monthNo = 1;
            for (Day d : tmp) {

                date = d.getDate();
                type = d.getType();
                category = d.getCategory();
                desc = d.getDesc();
                pMethod = d.getPMethod();
                pMethodDetail = d.getPMethodDetail();
                amt = d.getAmt();

                String cash = "";
                String card = "";
                String bank = "";

                switch (pMethod) {
                    case "Cash":
                        cash = pMethodDetail;
                        break;
                    case "Card":
                        card = pMethodDetail;
                        break;
                    case "Bank":
                        bank = pMethodDetail;
                        break;
                }

                double outcome = 0;
                double income = 0;

                if (type.equals("Income")) {
                    income = amt;
                    balance += amt;
                } else {
                    outcome = amt;
                    balance -= amt;
                }

                alMonth.add(new Month(monthNo++, date, category, desc, cash, card, bank, outcome, income, balance));
            }

            ObservableList<Month> olMonth = FXCollections.observableArrayList(alMonth);
            tcMNo.setCellValueFactory(new PropertyValueFactory<Month, Integer>("no"));
            tcMDate.setCellValueFactory(new PropertyValueFactory<Month, String>("date"));
            tcMCtg.setCellValueFactory(new PropertyValueFactory<Month, String>("category"));
            tcMDesc.setCellValueFactory(new PropertyValueFactory<Month, String>("desc"));
            tcMCash.setCellValueFactory(new PropertyValueFactory<Month, String>("cash"));
            tcMCard.setCellValueFactory(new PropertyValueFactory<Month, String>("card"));
            tcMBank.setCellValueFactory(new PropertyValueFactory<Month, String>("bank"));
            tcMOutcome.setCellValueFactory(new PropertyValueFactory<Month, Double>("outcome"));
            tcMIncome.setCellValueFactory(new PropertyValueFactory<Month, Double>("income"));
            tcMBal.setCellValueFactory(new PropertyValueFactory<Month, Double>("balance"));

            tvMonth.setItems(olMonth);
            lblNBalance.setText("" + balance);
            monthchanged = 0;
        }
    }

    public void displayMonthFromArray(ArrayList<Month> al) throws IOException {

        ObservableList<Month> olMonth = FXCollections.observableArrayList(al);
        tcMNo.setCellValueFactory(new PropertyValueFactory<Month, Integer>("no"));
        tcMDate.setCellValueFactory(new PropertyValueFactory<Month, String>("date"));
        tcMCtg.setCellValueFactory(new PropertyValueFactory<Month, String>("category"));
        tcMDesc.setCellValueFactory(new PropertyValueFactory<Month, String>("desc"));
        tcMCash.setCellValueFactory(new PropertyValueFactory<Month, String>("cash"));
        tcMCard.setCellValueFactory(new PropertyValueFactory<Month, String>("card"));
        tcMBank.setCellValueFactory(new PropertyValueFactory<Month, String>("bank"));
        tcMOutcome.setCellValueFactory(new PropertyValueFactory<Month, Double>("outcome"));
        tcMIncome.setCellValueFactory(new PropertyValueFactory<Month, Double>("income"));
        tcMBal.setCellValueFactory(new PropertyValueFactory<Month, Double>("balance"));

        tvMonth.setItems(olMonth);
    }

}
