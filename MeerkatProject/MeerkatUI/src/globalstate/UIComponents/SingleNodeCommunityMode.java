/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package globalstate.UIComponents;

import analysisscreen.AnalysisController;
import ca.aicml.meerkat.api.GraphAPI;
import communitymining.dialogwindow.SiwoPlus;
import config.ModeConfig;
import config.ModeInformationConfig;
import config.StatusMsgsConfig;
import globalstate.GraphCanvas;
import globalstate.MeerkatUI;
import globalstate.ProjectStatusTracker;
import graphelements.UIEdge;
import graphelements.UIVertex;
import graphelements.VertexHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.ImageCursor;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import ui.dialogwindow.VertexDeleteConfirmationDialog;
import ui.elements.SizeToolBox;

/**
 *
 * @author Yashar Talebirad
 */
public class SingleNodeCommunityMode implements GraphCanvasMode{
    
    ImageCursor c1 ;

    @Override
    public void activateGraphCanvasMode(ScrollPane scrlCanvas, SceneGestures4 sceneGestures, ModeInformation modeInfo) {
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        scrlCanvas.addEventFilter( MouseEvent.MOUSE_CLICKED, sceneGestures.getOnMouseClickedEventHandler());
        scrlCanvas.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
        
        sceneGestures.setCurrentGraphCanvasMode(this);
        modeInfo.updateModeInformation(ModeConfig.ModeTypes.SINGLECOMMUNITYSIWOPLUS, ModeConfig.SINGLECOMMUNITYSIWOPLUS_MODE, ModeInformationConfig.SELECTSINGLECOMMUNITYSIWOPLUS);
        
        System.out.println("-=-----------------===================------------ In DELETEVERTEXMODE mode ACTIVATED ");

        // Changing the cursor of the application
//        c1 = new ImageCursor(new Image(GraphEditingToolsConfig.getDeleteVertexImageURL()));
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
        System.out.println("-=-----------------===================------------ In SIWO search mode MOUSERELEASED");
//        MeerkatUI UIInstance = MeerkatUI.getUIInstance();
//        VertexDeleteConfirmationDialog.Display(UIInstance.getController());
//        //EdgeDeleteConfirmationDialog.Display(UIInstance.getController());
    }

    @Override
    public void primaryMousePressed() {
        
    }


    @Override
    public void primaryMouseReleasedOnVertex(VertexHolder vertexHolder, ModeInformation modeInfo) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        int intProjectID = MeerkatUI.getUIInstance().getActiveProjectTab().getProjectID() ;
//        int intGraphID = MeerkatUI.getUIInstance().getActiveProjectTab().getActiveGraphID() ;
//        int intTimeFrameIndex = MeerkatUI.getUIInstance().getActiveProjectTab().getActiveGraphTab().getTimeFrameIndex() ;
//        Set<UIVertex> setSelectedVertices = MeerkatUI.getUIInstance().getProject(intProjectID).getGraphTab(intGraphID).getGraphCanvas().getSelectedVertices();
//        int theVertex = setSelectedVertices.iterator().next().getID();
//        Set<Integer> setNeighbourhoodIDs = GraphAPI.getVertexCommunity(intProjectID, intGraphID, intTimeFrameIndex, theVertex) ;
//        MeerkatUI.getUIInstance().getActiveProjectTab().getActiveGraphTab().getGraphCanvas().selectVertex(setNeighbourhoodIDs);
            SiwoPlus.Display(MeerkatUI.getUIInstance().getController(), "");
    }

    @Override
    public void primaryMouseReleasedOnEdge(UIEdge uiEdge) {
        MeerkatUI UIInstance = MeerkatUI.getUIInstance();
        //EdgeDeleteConfirmationDialog.Display(UIInstance.getController());
    }
    private static void removeVertices(AnalysisController pController) {
        
        MeerkatUI UIInstance = MeerkatUI.getUIInstance();
        int projectID = UIInstance.getActiveProjectTab().getProjectID();
        int graphID = UIInstance.getActiveProjectTab().getActiveGraphID();
        int tf = UIInstance.getActiveProjectTab().getActiveGraphTab().getTimeFrameIndex();
        
        GraphCanvas graphCanvas = UIInstance.getActiveProjectTab().getActiveGraphTab().getGraphCanvas();
         
        pController.updateStatusBar(true, StatusMsgsConfig.VERTEX_DELETING);      

                Map<UIVertex, Set<UIEdge>> vertexEdgesMap = graphCanvas.getVertexEdgesMap();
                List<Integer> vertexIDs = new ArrayList<>();
                List<Integer> edgeIDs = new ArrayList<>();

                //deleting from UI
                System.out.println("VertexDeleteConfirmationDialog removeVertices() -> Total Selected Vertices for Removal: "+ graphCanvas.getSelectedVertices().size());
                for(UIVertex uiVertex : graphCanvas.getSelectedVertices()){

                    vertexIDs.add(uiVertex.getVertexHolder().getID());

                    graphCanvas.getChildren().remove(uiVertex.getVertexHolder().getNode());
                    graphCanvas.getChildren().remove(uiVertex.getVertexHolder().getLabelHolder());
                    
                    if(vertexEdgesMap.containsKey(uiVertex)){
                        for(UIEdge edge  : vertexEdgesMap.get(uiVertex)){
                            if(edge!=null){
                                edgeIDs.add(edge.getID());
                                graphCanvas.getChildren().remove(edge.getEdgeShape().getShapeNode());
                            }
                        }
                    }
                }
                
                //update the UI data structures after vertex/edge removal
                graphCanvas.updateCanvasAfterVertexRemove(vertexIDs, edgeIDs);                
                
                //deleting edges corresponding to selected vertices from logic
                GraphAPI.removeEdges(projectID, graphID, tf, vertexIDs);
                //deleting selected vertices from logic
                GraphAPI.removeVertices(projectID, graphID, tf, vertexIDs);


                //update accordion tab values
                UIInstance.getActiveProjectTab().getActiveGraphTab().updateAccordionValues();
                
                //update minimap
                UIInstance.getActiveProjectTab().getActiveGraphTab().getMinimapDelegator().updateMinimap();
                
                //update the status of the project
                ProjectStatusTracker.updateProjectModifiedStatus(projectID, ProjectStatusTracker.eventVertexRemoved);
                
                //update UI after vertices/edges have been removed.
                UIInstance.UpdateUI();
                
                //update SizeToolBox
                SizeToolBox.getInstance().disableSizeToolbox();
    }
    
}
