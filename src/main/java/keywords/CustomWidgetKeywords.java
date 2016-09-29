package keywords;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.swing.context.Context;

import org.netbeans.jemmy.operators.JComponentOperator;
import org.netbeans.jemmy.operators.ContainerOperator;
import org.netbeans.jemmy.ComponentChooser;
import java.awt.Component;
import java.awt.Shape;
import java.awt.Rectangle;
import edu.jsu.mcis.CustomWidget;

@RobotKeywords
public class CustomWidgetKeywords {
    @RobotKeyword("Clicks inside the hexagon. \n")
    @ArgumentNames({"x", "y"})
    public void clickHexagon() {
        ContainerOperator context = (ContainerOperator) Context.getContext();
        ComponentChooser chooser = new CustomWidgetChooser();
        JComponentOperator operator = new JComponentOperator(context, chooser);
        CustomWidget w = (CustomWidget)operator.getSource();
        Rectangle bounds = w.getShapes()[0].getBounds();
        operator.clickMouse(bounds.x + bounds.width/2, bounds.y + bounds.height/2, 1);
    }
    
    @RobotKeyword("Clicks inside the octagon. \n")
    @ArgumentNames({})
    public void clickOctagon() {
        ContainerOperator context = (ContainerOperator) Context.getContext();
        ComponentChooser chooser = new CustomWidgetChooser();
        JComponentOperator operator = new JComponentOperator(context, chooser);
        CustomWidget w = (CustomWidget)operator.getSource();
        Rectangle bounds = w.getShapes()[1].getBounds();
        operator.clickMouse(bounds.x + bounds.width/2, bounds.y + bounds.height/2, 1);
    }

    @RobotKeyword("Clicks outside the polygons. \n")
    @ArgumentNames({})
    public void clickOutside() {
        ContainerOperator context = (ContainerOperator) Context.getContext();
        ComponentChooser chooser = new CustomWidgetChooser();
        JComponentOperator operator = new JComponentOperator(context, chooser);
        CustomWidget w = (CustomWidget)operator.getSource();
        Rectangle hexBounds = w.getShapes()[0].getBounds();
        operator.clickMouse(hexBounds.x - 10, hexBounds.y - 10, 1);
        Rectangle octBounds = w.getShapes()[1].getBounds();
        operator.clickMouse(octBounds.x - 10, octBounds.y - 10, 1);
    }
    
    class CustomWidgetChooser implements ComponentChooser {
        public CustomWidgetChooser() {}
        public boolean checkComponent(Component comp) {
            return (comp instanceof CustomWidget);
        }
        public String getDescription() {
            return "Any CustomWidget";
        }
    }
}
