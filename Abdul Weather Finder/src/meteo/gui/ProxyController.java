/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meteo.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import meteo.http.Proxy;

/**
 * FXML Controller class
 *
 * @author majid
 */
public class ProxyController implements Initializable {
    
    @FXML
    private TextField txtUsername;
   
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private TextField txtHost;
    
    @FXML
    private TextField txtPort;
    
    @FXML
    private TitledPane tpAuthentication;
    
    @FXML
    private ProgressIndicator progressIndicator;
    
    @FXML
    private CheckBox cbAuthentication;
    
    @FXML
    private Button btnTestConnection;
    
    public static Stage dialogStage;
    private Proxy proxy;
    
    @FXML
    private void checkBoxChanged(ActionEvent e){
        if (cbAuthentication.isSelected()){
            tpAuthentication.setDisable(false);
            
        }
    }
    
    @FXML 
    private void applyProxy(ActionEvent e){
        String host = txtHost.getText();
        int port = Integer.parseInt(txtPort.getText());
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        proxy = new Proxy(host, port, username, password);
        proxy.applyProxy();
        btnTestConnection.setDisable(false);
    }
    
    @FXML 
    private void testConnection(ActionEvent e){
        
        FXMLDocumentController.executorService.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    enableLoading();
                    final boolean connection = proxy.testConnection(); 
                    loadingDone();
                } catch (IOException ex) {
                    connectionFailed();
                }
            }
        });
        
        
    }
    private void enableLoading(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                progressIndicator.setDisable(false);
                progressIndicator.setProgress(-1.0);
            }
        });
        
    }
    private void loadingDone(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                progressIndicator.setProgress(1.0);
            }
        });
    }
    private void connectionFailed(){
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                progressIndicator.setProgress(0.0);
                progressIndicator.setDisable(true);
            }
        });
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
