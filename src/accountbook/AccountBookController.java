package accountbook;

import accountbook.info.*;
import accountbook.model.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class AccountBookController implements Initializable {

    private final String path = "./src/accountbook/file/";
    private final String imgPath = "D:/image/profile.png";
    private double startBalance = 0;
    private double balance = 0;

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
    private Label lblSBalance, lblNBalance, lblName;

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
    private TableView tvExpense, tvInCome, tvMonth;

    @FXML
    private ImageView imgProfile;

    @FXML
    private TableColumn tcONo, tcOCtg, tcODesc, tcOMethod, tcOAmt, tcINo, tcICtg, tcIDesc, tcIMethod, tcIAmt,
            tcMNo, tcMDate, tcMCtg, tcMDesc, tcMCash, tcMCard, tcMBank, tcMIncome, tcMExpense, tcMBal;

    /**
     * Daily, monthly result updated when selected
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void selectedDate(ActionEvent event) throws IOException {
        displayDay(datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE));
        displayMonth(datePicker.getValue().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    /**
     * today result updated when selected
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnToday(ActionEvent event) throws IOException {
        datePicker.setValue(LocalDate.now());
        displayDay(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        displayMonth(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
    }

    /**
     * clear button
     *
     * @param event
     */
    @FXML
    private void btnInfoRefresh(ActionEvent event) {
        refresh();
    }

    /**
     * save the filled daily information
     *
     * @param event
     * @throws IOException
     */
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
        } else if (!desc.matches("[a-zA-Z0-9 ]+")) {
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

            displaySaveAlert();
            refresh();
            displayDay(date);
            displayMonth(date);
        }

    }

    /**
     * make expense combo box
     *
     * @param event
     */
    @FXML
    private void btnExpenseAction(ActionEvent event) {
        typeList.clear();
        for (OutType o : OutType.values()) {
            typeList.add(o.toString());
        }
        typeCombo(typeList);
    }

    /**
     * make income combo box
     *
     * @param event
     */
    @FXML
    private void btnIncomeAction(ActionEvent event) {
        typeList.clear();
        for (InType i : InType.values()) {
            typeList.add(i.toString());
        }
        typeCombo(typeList);
    }

    /**
     * make cash combo box
     *
     * @param event
     */
    @FXML
    private void btnCashAction(ActionEvent event) {
        pMethodList.clear();
        pMethodList.add("Cash");
        paymentMethodCombo(pMethodList, true);
    }

    /**
     * make back combo box
     *
     * @param event
     */
    @FXML
    private void btnBankAction(ActionEvent event) {
        pMethodList.clear();
        for (Bank b : Bank.values()) {
            pMethodList.add(b.toString());
        }
        paymentMethodCombo(pMethodList, false);
    }

    /**
     * make card combo box
     *
     * @param event
     */
    @FXML
    private void btnCardAction(ActionEvent event) {
        pMethodList.clear();
        for (Card c : Card.values()) {
            pMethodList.add(c.toString());
        }
        paymentMethodCombo(pMethodList, false);
    }

    /**
     * monthly screen refresh
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnMRefresh(ActionEvent event) throws IOException {
        txtMdesc.setText("");
        displayMonth(date);
    }

    /**
     * monthly screen search button
     *
     * @param event
     * @throws IOException
     */
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

    /**
     * monthly screen one row delete button
     *
     * @param event
     */
    @FXML
    private void btnMDelete(ActionEvent event) {
        tvMonth.getItems().removeAll(tvMonth.getSelectionModel().getSelectedItem());
        monthchanged++;
    }

    /**
     * monthly screen delete all rows
     *
     * @param event
     */
    @FXML
    private void btnMDeleteAll(ActionEvent event) {
        tvMonth.getItems().clear();
        monthchanged++;
    }

    /**
     * monthly screen move to previous row
     *
     * @param event
     */
    @FXML
    private void btnMPrev(ActionEvent event) {
        tvMonth.getSelectionModel().selectPrevious();
    }

    /**
     * monthly move to next row
     *
     * @param event
     */
    @FXML
    private void btnMNext(ActionEvent event) {
        tvMonth.getSelectionModel().selectNext();
    }

    /**
     * move to report screen if something to changed, ask whether it need to
     * save
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void btnMReport(ActionEvent event) throws IOException {

        if (monthchanged > 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Do you want to save the changes before moving to the next page?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Alert");
            alert.setHeaderText(null);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                btnMSave(new ActionEvent());

                FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Report.fxml"));
                Parent reportParent = loader.load();

                ReportController rController = loader.getController();
                rController.transferToReport(tvMonth, lblName.getText(), lblSBalance.getText());

                Scene reportScene = new Scene(reportParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(reportScene);
                window.show();
            }

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/Report.fxml"));
            Parent reportParent = loader.load();

            ReportController rController = loader.getController();
            rController.transferToReport(tvMonth, lblName.getText(), lblSBalance.getText());

            Scene reportScene = new Scene(reportParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(reportScene);
            window.show();
        }

    }

    /**
     * save to file changed contents
     *
     * @param event
     * @throws IOException
     */
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
                type = "Expense";
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
            amt = m.getIncome() + m.getExpense();

            pw.print(makeRow(date, type, category, desc, pMethod, pMethodDetail, "" + amt));
        }

        pw.close();

        displaySaveAlert();
        refresh();
        displayDay(date);
        displayMonth(date);

    }

    /**
     * when screen open clear the input fields and display daily, monthly screen
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        datePicker.setValue(LocalDate.now());
        typeList = new ArrayList();
        btnExpenseAction(new ActionEvent());
        pMethodList = new ArrayList();
        btnCashAction(new ActionEvent());

        try {
            displayDay(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            displayMonth(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        } catch (Exception e) {
        }
    }

    /**
     * refresh method
     */
    private void refresh() {
        radioOut.setSelected(true);
        btnExpenseAction(new ActionEvent());
        txtDesc.setText("");
        radioCash.setSelected(true);
        txtAmt.setText("");
        btnCashAction(new ActionEvent());
    }

    /**
     * display type combo box from the data
     *
     * @param al
     */
    private void typeCombo(ArrayList al) {
        ObservableList<String> olType = FXCollections.observableArrayList(al);
        cmbCategory.setItems(olType);
        cmbCategory.getSelectionModel().selectFirst();
    }

    /**
     * display payment method combo box from the data
     *
     * @param al
     * @param b
     */
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

    /**
     * make file path from the date
     *
     * @param date
     * @return
     */
    private String makePath(String date) {
        return path + date.substring(0, 6) + ".csv";
    }

    /**
     * the method display daily screen
     *
     * @param day
     * @throws IOException
     */
    private void displayDay(String day) throws IOException {
        File file = new File(makePath(day));

        if (file.exists()) {
            Scanner scanner = new Scanner(file);
            ArrayList<Day> alExpense = new ArrayList();
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

                if (type.equals("Expense")) {
                    alExpense.add(new Day(outNum++, category, desc, pMethod, amt));
                } else {
                    alIncome.add(new Day(inNum++, category, desc, pMethod, amt));
                }
            }

            ObservableList<Day> olExpense = FXCollections.observableArrayList(alExpense);
            tcONo.setCellValueFactory(new PropertyValueFactory<Day, Integer>("no"));
            tcOCtg.setCellValueFactory(new PropertyValueFactory<Day, String>("category"));
            tcODesc.setCellValueFactory(new PropertyValueFactory<Day, String>("desc"));
            tcOMethod.setCellValueFactory(new PropertyValueFactory<Day, String>("pMethod"));
            tcOAmt.setCellValueFactory(new PropertyValueFactory<Day, Double>("amt"));
            tvExpense.setItems(olExpense);

            ObservableList<Day> olIncome = FXCollections.observableArrayList(alIncome);
            tcINo.setCellValueFactory(new PropertyValueFactory<Day, Integer>("no"));
            tcICtg.setCellValueFactory(new PropertyValueFactory<Day, String>("category"));
            tcIDesc.setCellValueFactory(new PropertyValueFactory<Day, String>("desc"));
            tcIMethod.setCellValueFactory(new PropertyValueFactory<Day, String>("pMethod"));
            tcIAmt.setCellValueFactory(new PropertyValueFactory<Day, Double>("amt"));
            tvInCome.setItems(olIncome);
        }
    }

    /**
     * the method display monthly screen from the date
     *
     * @param day
     * @throws IOException
     */
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

                double expense = 0;
                double income = 0;

                if (type.equals("Income")) {
                    income = amt;
                    balance += amt;
                } else {
                    expense = amt;
                    balance -= amt;
                }

                alMonth.add(new Month(monthNo++, date, category, desc, cash, card, bank, expense, income, balance));
            }

            ObservableList<Month> olMonth = FXCollections.observableArrayList(alMonth);
            tcMNo.setCellValueFactory(new PropertyValueFactory<Month, Integer>("no"));
            tcMDate.setCellValueFactory(new PropertyValueFactory<Month, String>("date"));
            tcMCtg.setCellValueFactory(new PropertyValueFactory<Month, String>("category"));
            tcMDesc.setCellValueFactory(new PropertyValueFactory<Month, String>("desc"));
            tcMCash.setCellValueFactory(new PropertyValueFactory<Month, String>("cash"));
            tcMCard.setCellValueFactory(new PropertyValueFactory<Month, String>("card"));
            tcMBank.setCellValueFactory(new PropertyValueFactory<Month, String>("bank"));
            tcMExpense.setCellValueFactory(new PropertyValueFactory<Month, Double>("expense"));
            tcMIncome.setCellValueFactory(new PropertyValueFactory<Month, Double>("income"));
            tcMBal.setCellValueFactory(new PropertyValueFactory<Month, Double>("balance"));

            tvMonth.setItems(olMonth);
            lblNBalance.setText("" + balance);
            monthchanged = 0;
        }
    }

    /**
     * the method display monthly screen from the data
     *
     * @param al
     * @throws IOException
     */
    public void displayMonthFromArray(ArrayList<Month> al) throws IOException {

        ObservableList<Month> olMonth = FXCollections.observableArrayList(al);
        tcMNo.setCellValueFactory(new PropertyValueFactory<Month, Integer>("no"));
        tcMDate.setCellValueFactory(new PropertyValueFactory<Month, String>("date"));
        tcMCtg.setCellValueFactory(new PropertyValueFactory<Month, String>("category"));
        tcMDesc.setCellValueFactory(new PropertyValueFactory<Month, String>("desc"));
        tcMCash.setCellValueFactory(new PropertyValueFactory<Month, String>("cash"));
        tcMCard.setCellValueFactory(new PropertyValueFactory<Month, String>("card"));
        tcMBank.setCellValueFactory(new PropertyValueFactory<Month, String>("bank"));
        tcMExpense.setCellValueFactory(new PropertyValueFactory<Month, Double>("expense"));
        tcMIncome.setCellValueFactory(new PropertyValueFactory<Month, Double>("income"));
        tcMBal.setCellValueFactory(new PropertyValueFactory<Month, Double>("balance"));

        tvMonth.setItems(olMonth);
    }

    /**
     * Method called when invoking accountbook screen
     *
     * @param txtName
     * @param txtBalance
     * @throws IOException
     */
    public void transferToAccountBook(String txtName, String txtBalance) throws IOException {
        lblName.setText(txtName);
        lblSBalance.setText(txtBalance);
        startBalance = Double.parseDouble(txtBalance);
        BufferedImage bf = ImageIO.read(new File(imgPath));
        Image img = SwingFXUtils.toFXImage(bf, null);
        imgProfile.setImage(img);

        try {
            displayDay(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            displayMonth(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        } catch (Exception e) {
        }
    }

    /**
     * alert method when save
     */
    public void displaySaveAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Saved!");
        alert.showAndWait();
    }
;

}
