/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialogwindow;

import analysisscreen.AnalysisController;
import ca.aicml.meerkat.api.GraphAPI;
import config.LangConfig;
import config.StatusMsgsConfig;
import globalstate.MeerkatUI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.utilities.VertexTooltipShowHide;


/**
 *  Class Name      : SelectVertexTooltip
 *  Created Date    : 2016-05-27
 *  Description     : Selects a Vertex Label 
 *  Version         : 1.0
 *  @author         : Talat
 * 
 *  EDIT HISTORY (most recent at the top)
 *  Date            Author          Description
 *  
 * 
*/
public class SelectVertexTooltip {
    
    private static String strSelectedAttribute ;
    
    /**
     *  Method Name     : Display()
     *  Created Date    : 2016-05-27
     *  Description     : Displays the attributes to select the Vertex Label Attribute
     *  Version         : 1.0
     *  @author         : Talat
     * 
     *  @param pController : AnalysisController
     * 
     *  EDIT HISTORY (most recent at the top)
     *  Date            Author          Description
     *  2018-01-19      Talat           Making the ComboBox with an editable Textfield for easy search 
     * 
    */
    public static void Display (AnalysisController pController) {
        try {
            Stage stgSelectVertexTooltip = new Stage();
            stgSelectVertexTooltip.initModality(Modality.APPLICATION_MODAL);
            
            MeerkatUI UIInstance = MeerkatUI.getUIInstance();
            int intProjectID = UIInstance.getActiveProjectTab().getProjectID();
            int intGraphID = UIInstance.getActiveProjectTab().getActiveGraphID() ;
            int intTimeFrameIndex = UIInstance.getActiveProjectTab().getActiveGraphTab().getTimeFrameIndex();
                                    
            // Grid to show all the User Input controls
            GridPane gridUserControls = new GridPane();
            gridUserControls.setPadding(new Insets(5));
            gridUserControls.setHgap(5);
            gridUserControls.setVgap(5);

            // Column constraints to define the two columns
            ColumnConstraints column1 = new ColumnConstraints();
            //column1.setPercentWidth(50);
            ColumnConstraints column2 = new ColumnConstraints();
            //column2.setPercentWidth(50);
            column2.setHgrow(Priority.ALWAYS);
            gridUserControls.getColumnConstraints().addAll(column1, column2);
                       
            
            // ROW 1 - ATTRIBUTE
            Label lblAttribute = new Label(LangConfig.VERTEXTOOLTIP_MSG);            
            ComboBox<String> cmbAttributeID = new ComboBox<>() ;
            cmbAttributeID.setEditable(true);

            // Create a list with some dummy values.
            ObservableList<String> items = FXCollections.observableArrayList(
                    GraphAPI.getAllAttributesNames_Sorted(intProjectID, intGraphID, intTimeFrameIndex));

            // Create a FilteredList wrapping the ObservableList.
            FilteredList<String> filteredItems = new FilteredList<>(items, p -> true);

            // Add a listener to the textProperty of the combobox editor. The
            // listener will simply filter the list every time the input is changed
            // as long as the user hasn't selected an item in the list.
            cmbAttributeID.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
                final TextField editor = cmbAttributeID.getEditor();
                final String selected = cmbAttributeID.getSelectionModel().getSelectedItem();

                // This needs run on the GUI thread to avoid the error described
                // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
                Platform.runLater(() -> {
                    // If the no item in the list is selected or the selected item
                    // isn't equal to the current input, we refilter the list.
                    if (selected == null || !selected.equals(editor.getText())) {
                        filteredItems.setPredicate(item -> {
                            // We return true for any items that starts with the
                            // same letters as the input. We use toUpperCase to
                            // avoid case sensitivity.
                            if (item.toUpperCase().startsWith(newValue.toUpperCase())) {
                                return true;
                            } else {
                                return false;
                            }
                        });
                    }
                });
            });

            cmbAttributeID.setItems(filteredItems);
            cmbAttributeID.setMaxWidth(100);
                        
            GridPane.setHalignment(lblAttribute, HPos.LEFT);
            gridUserControls.add(lblAttribute, 0, 0);

            GridPane.setHalignment(cmbAttributeID, HPos.LEFT);
            gridUserControls.add(cmbAttributeID, 1, 0);
        
            
            // Error Label 
            Label lblError = new Label("") ;
            lblError.setVisible(false);
            
            // Adding the OK and Cancel Buttons at the end        
            Button btnOK = new Button (LangConfig.GENERAL_OK);
            btnOK.setOnAction(e -> {
                
                boolean blnIsVertexLabelShown = UIInstance.getActiveProjectTab().getActiveGraphTab().IsVertexLabelShown();
                
                if (blnIsVertexLabelShown) {
                    pController.updateStatusBar(true, StatusMsgsConfig.VERTEXTOOLTIP_DISABLING);
                } else {
                    pController.updateStatusBar(true, StatusMsgsConfig.VERTEXTOOLTIP_ENABLING);
                }
                
                String [] arrstrTooltips = GraphAPI.getVertexAttributeValues(intProjectID, intGraphID, intTimeFrameIndex, strSelectedAttribute);
                UIInstance.getActiveProjectTab().getActiveGraphTab().setVertexTooltipAttr(strSelectedAttribute);
                UIInstance.getActiveProjectTab().getActiveGraphTab().setIsVertexTooltipShown(true);
                UIInstance.getActiveProjectTab().getActiveGraphTab().getGraphCanvas().updateVertexTooltips(arrstrTooltips);
                System.out.println("SelecteVertexTooltip.Display(): Tool tip selection");
                VertexTooltipShowHide.Execute(pController);
                               
                stgSelectVertexTooltip.close();
                
            });
            Button btnCancel = new Button (LangConfig.GENERAL_CANCEL);
            btnCancel.setOnAction(e -> {
                strSelectedAttribute = "" ;
                stgSelectVertexTooltip.close();
                pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
            });
            HBox hboxButtons = new HBox(btnOK, btnCancel);
            hboxButtons.setSpacing(10);
            hboxButtons.setPadding(new Insets(5,10,5,10));
            hboxButtons.setAlignment(Pos.CENTER);
            
            
            cmbAttributeID.valueProperty().addListener(new ChangeListener<String>() {
                @Override 
                public void changed(ObservableValue ov, String t, String t1) {
                    strSelectedAttribute = t1 ;
                    
                    if (strSelectedAttribute.length() > 0) {
                        lblError.setVisible(false);
                    }
                }    
            });
            
            // The scene consists of the following items
            // 1) Grid for Input Values
            // 2) HBox - buttons
            VBox vboxPanel = new VBox(gridUserControls,lblError, hboxButtons) ;
            
            
            Scene scnSelectVertexTooltip = new Scene(vboxPanel);
            scnSelectVertexTooltip.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent evt) -> {
                if (evt.getCode().equals(KeyCode.ESCAPE)) {
                    stgSelectVertexTooltip.close();
                    pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
                }
            });
        
            //stgSelectVertexTooltip.initModality(Modality.WINDOW_MODAL);
            stgSelectVertexTooltip.setTitle(LangConfig.VERTEXTOOLTIP_TITLE);
            stgSelectVertexTooltip.setResizable(false);
            pController.updateStatusBar(false, StatusMsgsConfig.WAITING_USERINPUT);

            stgSelectVertexTooltip.setScene(scnSelectVertexTooltip);
            stgSelectVertexTooltip.show();
            stgSelectVertexTooltip.setOnCloseRequest(e -> {
                e.consume();
                stgSelectVertexTooltip.close();
                pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
            });
        } catch (Exception ex) {
            System.out.println("SelectVertexTooltip.Display(): EXCEPTION");
            ex.printStackTrace();
        } 
    }
}
