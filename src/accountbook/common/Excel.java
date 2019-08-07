package accountbook.common;

import accountbook.model.ExcelData;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class Excel {

    public Excel() {
    }

    public void makeExcel(ArrayList<ExcelData> al, String path) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        Workbook wb = new HSSFWorkbook();
        Sheet s = wb.createSheet();
        Row r = null;
        Cell c = null;
        CellStyle cs = wb.createCellStyle();

        Font f = wb.createFont();

        f.setFontHeightInPoints((short) 12);
        f.setColor((short) 0xc);

        cs.setFont(f);

        wb.setSheetName(0, "Report");
        for (int row = 0; row < al.size(); row++) {

            r = s.createRow(row);

            String[] tmp = new String[10];
            if (row == 0) {
                tmp[0] = "No";
                tmp[1] = "Date";
                tmp[2] = "Category";
                tmp[3] = "Description";
                tmp[4] = "Cash";
                tmp[5] = "Card";
                tmp[6] = "Bank";
                tmp[7] = "Expense";
                tmp[8] = "Income";
                tmp[9] = "Balance";
            } else {
                tmp[0] = al.get(row).getNo();
                tmp[1] = al.get(row).getDate();
                tmp[2] = al.get(row).getCategory();
                tmp[3] = al.get(row).getDesc();
                tmp[4] = al.get(row).getCash();
                tmp[5] = al.get(row).getCard();
                tmp[6] = al.get(row).getBank();
                tmp[7] = "" + al.get(row).getExpense();
                tmp[8] = "" + al.get(row).getIncome();
                tmp[9] = "" + al.get(row).getBalance();
            }

            for (int cell = 0; cell < 10; cell++) {
                c = r.createCell(cell);
                if (row == 0) {
                    c.setCellStyle(cs);
                }
                c.setCellValue(tmp[cell]);
            }
        }

        s.autoSizeColumn(2);
        s.autoSizeColumn(3);
        wb.write(out);
        out.close();

        Desktop.getDesktop().open(new File(path));
    }
}
