/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package communitymining.dialogwindow;

import analysisscreen.AnalysisController;
import communitymining.parameters.SiwoPlusMiningParam;
import config.LangConfig;
import config.StatusMsgsConfig;
import globalstate.MeerkatUI;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;

/**
 *
 * @author mahdi
 */
public class SiwoPlus {
    
    
    private static final String strSelectedValue = "" ;
    private static final BooleanProperty blnIsAlgorithmNotSelected = new SimpleBooleanProperty(false);
    
//    public static void Display (AnalysisController pController, String pstrMappingID) {
//        try {
//            boolean commMiningStatus = ClearMiningResults.checkExistingCommMining(Boolean.FALSE);
//            if(commMiningStatus){
//                Stage stgSiwoPlus = new Stage();
//                stgSiwoPlus.initModality(Modality.APPLICATION_MODAL);
//
//                // Grid to show all the User Input controls
//                GridPane gridUserControls = new GridPane();
//                gridUserControls.setPadding(new Insets(5));
//                gridUserControls.setHgap(5);
//                gridUserControls.setVgap(5);
//
//                // Column constraints to define the two columns
//                ColumnConstraints column1 = new ColumnConstraints();
//                ColumnConstraints column2 = new ColumnConstraints();
//                column2.setHgrow(Priority.ALWAYS);
//                gridUserControls.getColumnConstraints().addAll(column1, column2);
//
//
//                Label lblAttribute = new Label(" Confirm if you want to run SIWO+? ") ;
//                GridPane.setHalignment(lblAttribute, HPos.LEFT);
//                gridUserControls.add(lblAttribute, 0, 0);
//                
//                // Adding the OK and Cancel Buttons at the end        
//                Button btnOK = new Button (LangConfig.GENERAL_OK);
//                btnOK.setOnAction((ActionEvent e) -> {
//                    ClearMiningResults.clearCommunityAssignment();
//                    
//                    pController.updateStatusBar(true, StatusMsgsConfig.MINING_RESULTSCOMPUTING);
//                    stgSiwoPlus.close();
//                    
//                    // Call the Community corresponding Mining API
//                    MeerkatUI UIInstance = MeerkatUI.getUIInstance() ;
//                    int intProjectID = UIInstance.getActiveProjectTab().getProjectID() ;
//                    int intGraphID = UIInstance.getActiveProjectTab().getActiveGraphTab().getGraphID() ;
//                    int intTimeFrameIndex = UIInstance.getActiveProjectTab().getActiveGraphTab().getTimeFrameIndex() ;
//                    CommunityMiningAlgorithmDialog cmad = new CommunityMiningAlgorithmDialog();
//                    cmad.runAlgorithm(intProjectID, intGraphID, intTimeFrameIndex, pstrMappingID, null, pController);
//                });
//
//                Button btnCancel = new Button (LangConfig.GENERAL_CANCEL);
//                btnCancel.setOnAction(e ->{
//                    stgSiwoPlus.close();
//                    pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
//                });
//
//                HBox hboxButtons = new HBox(btnOK, btnCancel);
//                hboxButtons.setSpacing(10);
//                hboxButtons.setPadding(new Insets(5,10,5,10));
//                hboxButtons.setAlignment(Pos.CENTER);
//
//                // The scene consists of the following items
//                // 1) Grid for Input Values
//                // 2) HBox - buttons
//                VBox vboxPanel = new VBox(gridUserControls, hboxButtons) ;
//
//                Scene scnSiwoPlus = new Scene(vboxPanel);
//
//                scnSiwoPlus.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent evt) -> {
//                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
//                        stgSiwoPlus.close();
//                        pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
//                    }
//                });
//
//                stgSiwoPlus.setTitle(SiwoPlusMiningParam.getTitle());
//                stgSiwoPlus.setResizable(false);
//
//                stgSiwoPlus.setScene(scnSiwoPlus);
//                stgSiwoPlus.show();
//                stgSiwoPlus.setOnCloseRequest(e -> {
//                    e.consume();
//                    stgSiwoPlus.close();
//                    pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
//                });
//            }
//        } catch (Exception ex) {
//            System.out.println(".Display(): EXCEPTION");
//            ex.printStackTrace();
//        } 
//    }
    public static void Display(AnalysisController pController, String pstrMappingID) {
        try {
            boolean commMiningStatus = ClearMiningResults.checkExistingCommMining(Boolean.FALSE);
            if (commMiningStatus) {
                Stage stgSiwoPlus = new Stage();
                stgSiwoPlus.initModality(Modality.APPLICATION_MODAL);

                // Grid to show all the User Input controls
                GridPane gridUserControls = new GridPane();
                gridUserControls.setPadding(new Insets(5));
                gridUserControls.setHgap(5);
                gridUserControls.setVgap(5);

                // Column constraints to define the two columns
                ColumnConstraints column1 = new ColumnConstraints();
                column1.setPercentWidth(60);
                ColumnConstraints column2 = new ColumnConstraints();
                column2.setPercentWidth(40);
                column2.setHgrow(Priority.ALWAYS);
                gridUserControls.getColumnConstraints().addAll(column1, column2);

                // ROW 1 - STRENGTH TYPE
                Label lblStrengthType = new Label("Strength Type");
                ComboBox<String> cmbStrengthType = new ComboBox<>();
                cmbStrengthType.getItems().addAll("1", "2");
                cmbStrengthType.setValue("2");
                cmbStrengthType.setMaxWidth(100);

                GridPane.setHalignment(lblStrengthType, HPos.LEFT);
                gridUserControls.add(lblStrengthType, 0, 0);
                GridPane.setHalignment(cmbStrengthType, HPos.LEFT);
                gridUserControls.add(cmbStrengthType, 1, 0);

                // ROW 2 - OVERLAP
                Label lblOverlap = new Label("Overlap");
                CheckBox chbOverlap = new CheckBox();
                chbOverlap.setSelected(true);

                GridPane.setHalignment(lblOverlap, HPos.LEFT);
                gridUserControls.add(lblOverlap, 0, 1);
                GridPane.setHalignment(chbOverlap, HPos.LEFT);
                gridUserControls.add(chbOverlap, 1, 1);

                // ROW 3 - MERGE OUTLIERS
                Label lblMergeOutliers = new Label("Merge Outliers");
                CheckBox chbMergeOutliers = new CheckBox();
                chbMergeOutliers.setSelected(false);

                GridPane.setHalignment(lblMergeOutliers, HPos.LEFT);
                gridUserControls.add(lblMergeOutliers, 0, 2);
                GridPane.setHalignment(chbMergeOutliers, HPos.LEFT);
                gridUserControls.add(chbMergeOutliers, 1, 2);

                // Error Label
                Label lblError = new Label("");
                lblError.setVisible(false);

                // Adding the OK and Cancel Buttons at the end
                Button btnOK = new Button(LangConfig.GENERAL_OK);
                btnOK.setOnAction(e -> {
                    ClearMiningResults.clearCommunityAssignment();

                    // Collecting parameters
                    String[] arrstrParameters = new String[3];
                    arrstrParameters[0] = "StrengthType:" + cmbStrengthType.getValue();
                    arrstrParameters[1] = "Overlap:" + chbOverlap.isSelected();
                    arrstrParameters[2] = "MergeOutliers:" + chbMergeOutliers.isSelected();

                    stgSiwoPlus.close();
                    pController.updateStatusBar(true, StatusMsgsConfig.MINING_RESULTSCOMPUTING);

                    // Call the Community corresponding Mining API
                    MeerkatUI UIInstance = MeerkatUI.getUIInstance();
                    int intProjectID = UIInstance.getActiveProjectTab().getProjectID();
                    int intGraphID = UIInstance.getActiveProjectTab().getActiveGraphTab().getGraphID();
                    int intTimeFrameIndex = UIInstance.getActiveProjectTab().getActiveGraphTab().getTimeFrameIndex();
                    CommunityMiningAlgorithmDialog cmad = new CommunityMiningAlgorithmDialog();
                    cmad.runAlgorithm(intProjectID, intGraphID, intTimeFrameIndex, pstrMappingID, arrstrParameters, pController);
                });

                Button btnCancel = new Button(LangConfig.GENERAL_CANCEL);
                btnCancel.setOnAction(e -> {
                    stgSiwoPlus.close();
                    pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
                });

                HBox hboxButtons = new HBox(btnOK, btnCancel);
                hboxButtons.setSpacing(10);
                hboxButtons.setPadding(new Insets(5, 10, 5, 10));
                hboxButtons.setAlignment(Pos.CENTER);

                // The scene consists of the following items
                // 1) Grid for Input Values
                // 2) HBox - buttons
                VBox vboxPanel = new VBox(gridUserControls, lblError, hboxButtons);

                Scene scnSiwoPlus = new Scene(vboxPanel);
                scnSiwoPlus.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent evt) -> {
                    if (evt.getCode().equals(KeyCode.ESCAPE)) {
                        stgSiwoPlus.close();
                        pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
                    }
                });

                stgSiwoPlus.setTitle("SIWO+ Algorithm Parameters");
                stgSiwoPlus.setResizable(false);
                stgSiwoPlus.setScene(scnSiwoPlus);
                stgSiwoPlus.show();
                stgSiwoPlus.setOnCloseRequest(e -> {
                    e.consume();
                    stgSiwoPlus.close();
                    pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
                });
            }
        } catch (Exception ex) {
            System.out.println(".Display(): EXCEPTION");
            ex.printStackTrace();
        }
    }

    
}
