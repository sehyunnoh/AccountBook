package accountbook;

import accountbook.common.Excel;
import accountbook.model.ExcelData;
import accountbook.model.Month;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReportController implements Initializable {

    private final String path = "report.xls";
    private String date;
    private String type;
    private String category;
    private String desc;
    private String pMethod;
    private String pMethodDetail;
    private double amt;
    private ArrayList<ExcelData> al;
    private String name;
    private String sBalance;

    @FXML
    private PieChart pieReport;

    @FXML
    private Label lblExpense;

    @FXML
    private void btnBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AccountBook.fxml"));
        Parent reportParent = loader.load();

        AccountBookController aController = loader.getController();
        aController.transferToAccountBook(name, sBalance);

        Scene reportScene = new Scene(reportParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(reportScene);
        window.show();
    }

    @FXML
    private void btnExcel(ActionEvent event) throws IOException {
        Excel excel = new Excel();
        excel.makeExcel(al, path);
        Desktop.getDesktop().open(new File(path));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void transferToReport(TableView tvMonth, String name, String sBalance) {
        this.name = name;
        this.sBalance = sBalance;
        displayData(tvMonth);

    }

    public void displayData(TableView tvMonth) {
        ObservableList<Month> changedData = tvMonth.getItems();

        al = new ArrayList<>();
        al.add(new ExcelData());
        String str = "";
        double expense = 0;
        double income = 0;
        for (int i = 0; i < changedData.size(); i++) {
            Month m = changedData.get(i);

            ExcelData ex = new ExcelData("" + m.getNo(), m.getDate(), m.getCategory(), m.getDesc(), m.getCash(), m.getCard(), m.getBank(), m.getExpense(), m.getIncome(), m.getBalance());
            al.add(ex);

            date = m.getDate();
            if (m.getIncome() != 0) {
                type = "Income";
                income += m.getIncome();
            } else {
                type = "Expense";
                expense += m.getExpense();
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

            str += makeRow(date, type, category, desc, pMethod, pMethodDetail, "" + amt);
        }

        ObservableList<PieChart.Data> pData = FXCollections.observableArrayList();
        
        String tmpEx = String.format("%.2f",expense / (expense + income) * 100);
        double expenseRate = Double.parseDouble(tmpEx);
        double incomeRate = 100-expenseRate;

        pData.add(new PieChart.Data("Expense", expenseRate));
        pData.add(new PieChart.Data("Income", incomeRate));
        pieReport.setTitle("Monthly Report");
        pieReport.setLegendSide(Side.LEFT);
        pieReport.setData(pData);

        lblExpense.setTextFill(Color.WHITE);
        lblExpense.setStyle("-fx-font: 24 arial;");

        for (final PieChart.Data data : pieReport.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    lblExpense.setTranslateX(e.getSceneX());
                    lblExpense.setTranslateY(e.getSceneY());
                    lblExpense.setText(String.valueOf(data.getPieValue()) + "%");
                }
            });
        }
    }

    private String makeRow(String... str) {
        String result = "";
        for (int i = 0; i < str.length - 1; i++) {
            result += str[i] + ",";
        }
        return result += str[str.length - 1] + "\n";
    }

}
