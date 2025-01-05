/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.dialogwindow;

import analysisscreen.AnalysisController;
import ca.aicml.meerkat.api.ProjectAPI;
import config.AppConfig;
import config.GraphConfig;
import config.ImportFileFilters;
import config.StatusMsgsConfig;
import globalstate.MeerkatUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import meerkat.Utilities;
import globalstate.MeerkatUI;
import globalstate.ProjectStatusTracker;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *  Class Name      : NewProjectWizard
 *  Created Date    : 2015-08-04
 *  Description     : Some of the Language specific parameters
 *  Version         : 1.0
 *  @author         : Talat
 * 
 *  EDIT HISTORY (most recent at the top)
 *  Date            Author          Description
 *  
 * 
*/
public class NewProjectGraphWizard {
        
    /* *************************************************************** */
    /* *******************       VARIABLES     *********************** */
    /* *************************************************************** */
    private static String strNewProjectWizard ;
    private static String strProjectNameLabel; 
    private static String strPromptText;
    private static String strCreateProject ;
    private static String strCancel ;
    private static String strError_FileFound;

    
    /* *************************************************************** */
    /* ********************       METHODS     ************************ */
    /* *************************************************************** */
    /**
     *  Method Name     : setParameters()
     *  Created Date    : 2015-08-04
     *  Description     : Sets the parameters of the New Project Wizard
     *  Version         : 1.0
     *  @author         : Talat
     * 
     *  @param pstrNewProjectWizard : String
     *  @param pstrCreateProject : String
     *  @param pstrCancel : String
     *  @param pstrProjectNameLabel : String
     *  @param pstrPromptText : String
     *  @param pstrError_FileFound : String
     * 
     *  EDIT HISTORY (most recent at the top)
     *  Date            Author          Description
     *  
     * 
    */
    public static void setParameters (String pstrNewProjectWizard, String pstrCreateProject, String pstrCancel
            , String pstrProjectNameLabel, String pstrPromptText, String pstrError_FileFound) {
        strNewProjectWizard = pstrNewProjectWizard;
        strCreateProject = pstrCreateProject;
        strCancel = pstrCancel;
        strProjectNameLabel = pstrProjectNameLabel;
        strPromptText = pstrPromptText;
        strError_FileFound = pstrError_FileFound;
    }
    
    
    /**
     *  Method Name     : Display()
     *  Created Date    : 2015-08-04
     *  Description     : Displays the new project wizard
     *  Version         : 1.0
     *  @author         : Talat
     * 
     *  @param pController : AnalysisController
     * 
     *  EDIT HISTORY (most recent at the top)
     *  Date            Author          Description
     *  
     * 
    */
    public static void Display (AnalysisController pController) {
        
        Stage stgNewProjectWizard = new Stage();
        stgNewProjectWizard.initModality(Modality.APPLICATION_MODAL);
        
        HBox project = new HBox();
        Label lblProjectName = new Label("New graph name");
        Label lblError_FileFound = new Label(strError_FileFound);
        Label lblError_ProjectNameNotValid = new Label("graph name is not valid");
        Button btnCreate = new Button("New Graph");
        Button btnCancel = new Button("Cancel");

        
        lblError_FileFound.setVisible(false);
        lblError_ProjectNameNotValid.setVisible(false);
        
        
        TextField txtProjectName = new TextField ();
        txtProjectName.setPromptText(strPromptText);
        txtProjectName.setPrefColumnCount(15);
        project.getChildren().addAll(lblProjectName, txtProjectName);
        project.setPadding(new Insets(5,5,5,5));
        project.setSpacing(10);
        project.setAlignment(Pos.CENTER);
        
        txtProjectName.setOnKeyPressed((KeyEvent event) -> {
            if(event.getCode() == KeyCode.ENTER) {
                btnCreate.fire();
            }
        });

      
        HBox hboxButtons = new HBox(5);
        hboxButtons.setPadding(new Insets(5,5,5,5));
        
        // Button btnCreate = new Button(strCreateProject);
        btnCreate.disableProperty().bind(
            Bindings.isEmpty(txtProjectName.textProperty())
                
        );
        System.out.println("path in Display :" + MeerkatUI.getUIInstance().getActiveProjectTab().getProjectDirectory());

        btnCreate.setOnAction(e -> {
            if(true == true){
                String strPrgDir =  AppConfig.DIR_PROJECT  + File.separator + MeerkatUI.getUIInstance().getActiveProjectTab().getProjectName() + File.separator;
                String strProjectDir = strPrgDir;
                String strGraphDir = strProjectDir + File.separator + txtProjectName.getText() + ".meerkat" ;
                String strProjectRootDirectory = AppConfig.DIR_PROJECT ;       
                String strNewGraph = strProjectRootDirectory + "Default" + File.separator + "Default.meerkat" ;
                File file = new File(strGraphDir);
                System.out.println("abs Path in display :" + file.getAbsolutePath());

                try {
                    System.out.println("abs Path in display in try :" + file.getAbsolutePath());
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(NewProjectGraphWizard.class.getName()).log(Level.SEVERE, null, ex);
                }
                copyFileUsingStream(new File(strNewGraph), file);
                String strReaderID = ImportFileFilters.getReaderID(file.getAbsolutePath());
                GraphConfig.GraphType graphType = ImportFileFilters.getGraphType(file.getName());
                MeerkatUI.getUIInstance().getActiveProjectTab().addFileToProject(file.getAbsolutePath(), strReaderID, graphType);
                stgNewProjectWizard.close();

                /* Update the UI components in the Project Tab */
                pController.updateProjectUI(MeerkatUI.getUIInstance().getActiveProjectTab());
            }else{
                
                lblError_ProjectNameNotValid.setVisible(true);
            }
            
        });
        
        btnCancel.setOnAction(e -> {
            stgNewProjectWizard.close();
        });
        btnCancel.setCancelButton(true);
        
        
        hboxButtons.getChildren().addAll(btnCreate, btnCancel);
        hboxButtons.setSpacing(20);
        hboxButtons.setPadding(new Insets(0, 10, 10, 10));
        hboxButtons.setAlignment(Pos.CENTER);
      
        // Build a VBox
        VBox vboxRows = new VBox(5);
        vboxRows.setPadding(new Insets(5,5,5,5)); 
        vboxRows.setAlignment(Pos.CENTER);
        vboxRows.setSpacing(10);
        vboxRows.getChildren().addAll(project, lblError_ProjectNameNotValid, lblError_FileFound, hboxButtons);
        
        vboxRows.setMinWidth(600);
        Scene scnNewProjectWizard = new Scene(vboxRows);
        
        scnNewProjectWizard.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent evt) -> {
            if (evt.getCode().equals(KeyCode.ESCAPE)) {
                stgNewProjectWizard.close();
                pController.updateStatusBar(false, StatusMsgsConfig.OPERATION_CANCEL);
            }
        });
        
        //stgNewProjectWizard.initModality(Modality.WINDOW_MODAL);
        stgNewProjectWizard.setTitle(strNewProjectWizard);        
        stgNewProjectWizard.setResizable(false);
        
        stgNewProjectWizard.setScene(scnNewProjectWizard);
        stgNewProjectWizard.show();
    }

    public static Boolean checkIfValidDirectoryName(String pstrFileName){
         if(pstrFileName!=null){
            String trimmed = pstrFileName.trim();
            String regex = ProjectAPI.getGraphNameProjectNameRegex();
            if(trimmed.length()>0 && trimmed.matches(regex)){
                return true;
            }
         }
         return false;
    }
    private static void copyFileUsingStream(File source, File dest){
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        }
        catch (Exception e){}
    }
}
