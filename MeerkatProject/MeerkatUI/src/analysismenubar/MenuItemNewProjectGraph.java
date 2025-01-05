/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analysismenubar;
import analysisscreen.AnalysisController;
import javafx.scene.control.MenuItem;
import ui.dialogwindow.NewProjectGraphWizard;
import ui.dialogwindow.NewProjectWizard;


/**
 *
 * @author Yashar
 */
public class MenuItemNewProjectGraph extends MenuItemGeneric implements IMenuItem {
    public MenuItemNewProjectGraph(String pstrMenuItemDisplay, String pstrMenuItemClass, String pstrMenuItemIconPath) {
        super(pstrMenuItemDisplay, pstrMenuItemClass, pstrMenuItemIconPath);
    }
     @Override
    public void Click(AnalysisController pController, MenuItem pMenuItem, Object pobjParameter) {
        
        // Display the New Project dialog box
        NewProjectGraphWizard.Display(pController);
    }
}


