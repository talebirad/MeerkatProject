/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globalstate.UIComponents;

import config.GraphEditingToolsConfig;
import config.ModeConfig;
import config.ModeInformationConfig;
import globalstate.MeerkatUI;
import graphelements.UIEdge;
import graphelements.VertexHolder;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import ui.dialogwindow.AddVertexDialog;

/**
 *
 * @author AICML Administrator
 */
public class AddVertexMultipleTimeFramesMode implements GraphCanvasMode{
    
    ImageCursor c1 ;
    
    @Override
    public void activateGraphCanvasMode(ScrollPane scrlCanvas, SceneGestures4 sceneGestures, ModeInformation modeInfo) {
        
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());  
        
        sceneGestures.setCurrentGraphCanvasMode(this);
        modeInfo.updateModeInformation(ModeConfig.ModeTypes.VERTEXMULTIFRAMEADD, ModeConfig.VERTEXADD_MULTIFRAME_MODE, ModeInformationConfig.SELECTVERTEX_ADD);
        
        
        System.out.println("-=-----------------===================------------ In ADDVERTEXMODEMULTIPLETIMEFRAMES ");
        
        // Changing the cursor of the application
//        c1 = new ImageCursor(new Image(GraphEditingToolsConfig.getAddVertexMultipleTimeFramesImageURL()));
//        scrlCanvas.setOnMouseEntered(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//                scrlCanvas.setCursor(c1); 
//            }
//        });
//        scrlCanvas.setOnMouseExited(new EventHandler() {
//            @Override
//            public void handle(Event event) {
//                scrlCanvas.setCursor(Cursor.DEFAULT); 
//            }
//        });
    }

    @Override
    public void primaryMouseReleased() {
        System.out.println("-=-----------------===================------------ In ADDVERTEXMODEMULTIPLETIMEFRAMES MOUSERELEASED");
        MeerkatUI UIInstance = MeerkatUI.getUIInstance();
        AddVertexDialog.DisplayMultipleTimeFrameOption(UIInstance.getController());    }

    @Override
    public void primaryMousePressed() {
            
    }

    @Override
    public void primaryMouseReleasedOnVertex(VertexHolder vertexHolder, ModeInformation modeInfo) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void primaryMouseReleasedOnEdge(UIEdge uiEdge) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
