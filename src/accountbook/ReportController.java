/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package accountbook;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ReportController implements Initializable {

   

    @FXML
    private void btnBack(ActionEvent event) throws IOException {
        Parent reportParent = FXMLLoader.load(getClass().getResource("AccountBook.fxml"));
        Scene reportScene = new Scene(reportParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(reportScene);
        window.show();
    }    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
