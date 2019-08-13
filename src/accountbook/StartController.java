package accountbook;

import accountbook.model.ProfileBind;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class StartController implements Initializable {

    // assumed as file server
    String imgPath = "D:/image/profile.png";
    String name = "";
    double balance = 0;

    @FXML
    private Label lblName, lblBalance;

    @FXML
    private TextField txtName, txtBalance;

    @FXML
    private ImageView profileImg;

    @FXML
    private void btnImgChange(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        File file = fileChooser.showOpenDialog(null);

        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            File outputfile = new File(imgPath);
            ImageIO.write(bufferedImage, "png", outputfile);

            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            profileImg.setImage(image);
        } catch (IOException ex) {

        }
    }

    @FXML
    private void btnStart(ActionEvent event) throws IOException {

        boolean check = true;
        String errorMsg = "";
        name = txtName.getText();

        try {
            balance = Double.parseDouble(txtBalance.getText());
        } catch (Exception e) {
            errorMsg = "balance field is accepted the only number";
            check = false;
        }

        if ("".equals(name) || "".equals(txtBalance.getText())) {
            errorMsg = "Empty field is not accepted";
            check = false;
        } else if (!name.matches("[a-zA-Z ]+")) {
            errorMsg = "The name field is only letters are allowed";
            check = false;
        } else if (name.length() > 10) {
            errorMsg = "The name field is allowed up to 10 letters";
            check = false;
        }

        if (!check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Data Entry Error");
            alert.setHeaderText("Invalid Value Entered");
            alert.setContentText(errorMsg);
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/AccountBook.fxml"));
            Parent reportParent = loader.load();

            AccountBookController aController = loader.getController();
            aController.transferToAccountBook(txtName.getText(), txtBalance.getText());

            Scene reportScene = new Scene(reportParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(reportScene);
            window.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            BufferedImage bf = ImageIO.read(new File(imgPath));
            Image img = SwingFXUtils.toFXImage(bf, null);
            profileImg.setImage(img);
        } catch (Exception e) {
        }

        ProfileBind pb = new ProfileBind();

        txtName.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                pb.setName(txtName.getText());
            }
        });
        lblName.textProperty().bind(new SimpleStringProperty("Welcome ").concat(pb.nameProperty()));

        txtBalance.textProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                pb.setBalance(Double.parseDouble(txtBalance.getText()));
            }
        });
        lblBalance.textProperty().bind(new SimpleStringProperty("Your balance $").concat(pb.balanceProperty()));
    }
}
