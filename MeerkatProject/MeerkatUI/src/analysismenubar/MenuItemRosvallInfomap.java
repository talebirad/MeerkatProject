/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analysismenubar;

import analysisscreen.AnalysisController;
import communitymining.dialogwindow.RosvallInfomap;
import javafx.scene.control.MenuItem;


/**
 *  Class Name      : MenuItemRosvallInfomap
 *  Created Date    : 2016-04-12
 *  Description     : Functionalities when the user chooses RosvallInfomap Community Mining Algorithm from the MenuBar
 *  Version         : 1.0
 *  @author         : Talat
 * 
 *  EDIT HISTORY (most recent at the top)
 *  Date            Author          Description
 *  
 * 
*/
public class MenuItemRosvallInfomap extends MenuItemGeneric implements IMenuItem {

    /**
     *  Constructor Name: MenuItemRosvallInfomap()
     *  Created Date    : 2016-04-12
     *  Description     : Calls the super constructor to initialize the values
     *  Version         : 1.0
     *  @author         : Talat
     * 
     *  @param pstrMenuItemDisplay : String
     *  @param pstrMenuItemClass : String
     *  @param pstrMenuItemIconPath : String
     *  
     *  EDIT HISTORY (most recent at the top)
     *  Date            Author          Description
     * 
    */
    public MenuItemRosvallInfomap(String pstrMenuItemDisplay, String pstrMenuItemClass, String pstrMenuItemIconPath) {
        super(pstrMenuItemDisplay, pstrMenuItemClass, pstrMenuItemIconPath);
    }
    
       /**
     *  Method Name     : Click
     *  Created Date    : 2016-04-12
     *  Description     : Displays the parameters required for the RosvallInfomap community mining algorithm 
     *  Version         : 1.0
     *  @author         : Talat
     * 
     *  @param pController: AnalysisController
     *  @param pMenuItem : MenuItem
     * 
     *  EDIT HISTORY (most recent at the top)
     *  Date            Author          Description
     * 
    */
    @Override
    public void Click(AnalysisController pController, MenuItem pMenuItem, Object pobjParameter) {
        
        // Display the Parameters dialog box
        RosvallInfomap.Display(pController, pobjParameter.toString());                
    }
}
