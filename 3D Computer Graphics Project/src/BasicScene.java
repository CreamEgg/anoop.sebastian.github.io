import javax.vecmath.*;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;

@SuppressWarnings("serial")
public class BasicScene extends CustomFrame {
	public final BoundingSphere INFINITE_BOUNDS = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
	public Canvas3D canvas;
	public TransformGroup vtg;
	public View view;
	public ViewingPlatform viewingPlatform = null;
	private BranchGroup scene = null;
	Transform3D t3d = new Transform3D();
	
	public static void main(String[] args) {
		new BasicScene();
	} 
  
  public BasicScene() {
	  getContentPane().setLayout(new BorderLayout());
	  GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

	  // Create a Canvas3D object and add it to the frame
	  canvas = new Canvas3D(config);
	  canvas.addMouseListener(this);
	  getContentPane().add(canvas, BorderLayout.CENTER);

	  // Create a SimpleUniverse object to manage the "view" branch
	  SimpleUniverse u = new SimpleUniverse(canvas);
	  viewingPlatform = u.getViewingPlatform();
    
	  vtg = u.getViewingPlatform().getViewPlatformTransform();   
	  t3d.setTranslation(new Vector3f(50f,0.0f,50f));
	  AxisAngle4f angle = new AxisAngle4f(0, 1,0, (float)3.14);
	  t3d.setRotation(angle);vtg.setTransform(t3d);
	  
	  view = u.getViewer().getView();
	  view.setFrontClipDistance(0.001d);
	  view.setViewPolicy(View.NOMINAL_HEAD);
	    
	  // Add the "content" branch to the SimpleUniverse
	  setSize(640, 480);
	  // modifyViewBranch();
	  scene = createContentBranch();
    
	  u.addBranchGraph(scene);  
	  setVisible(true);
    }  
  
  public BranchGroup getContentRoot() {
	  return scene;
  }
  
  public void modifyViewBranch() {
	  
  }
  
  public TransformGroup getViewPlatformTransform() {
	return vtg;
  }  
  
  public BranchGroup createContentBranch() 
  {
    BranchGroup root = new BranchGroup();
  
    // Create the transform group
    TransformGroup transformGroup = new TransformGroup();
    //transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(transformGroup);
    
    // Create the mouse rotate behaviour
    MouseRotate rotate = new MouseRotate();
    rotate.setTransformGroup(transformGroup);
    rotate.setSchedulingBounds(new BoundingSphere(new Point3d(), 1000.0));
    transformGroup.addChild(rotate);
    
    // The colour cube geometry
    ColorCube colorCube = new ColorCube(0.3);
    transformGroup.addChild(colorCube);

    root.compile();

    return root;
  }

}
