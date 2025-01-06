/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analysismenubar;

import analysisscreen.AnalysisController;
import communitymining.dialogwindow.SiwoPlus;
import config.ModeConfig;
import globalstate.MeerkatUI;
import javafx.scene.control.MenuItem;
import ui.elements.EditingToolBox;

/**
 *
 * @author Yashar Talebirad
 */
public class MenuItemSiwoSingleCommunity extends MenuItemGeneric implements IMenuItem {

    public MenuItemSiwoSingleCommunity(String pstrMenuItemDisplay, String pstrMenuItemClass, String pstrMenuItemIconPath) {
        super(pstrMenuItemDisplay, pstrMenuItemClass, pstrMenuItemIconPath);
    }

    @Override
    public void Click(AnalysisController pController, MenuItem pMenuItem, Object pobjParameter) {
//      SiwoPlus.Display(pController, pobjParameter.toString());
        MeerkatUI UIInstance = MeerkatUI.getUIInstance();
//        EditingToolBox.getInstance().activateDeleteVertex();
        UIInstance.getActiveProjectTab().getActiveGraphTab().setGraphCanvasMode(ModeConfig.ModeTypes.SINGLECOMMUNITYSIWOPLUS);
    }
    
}
