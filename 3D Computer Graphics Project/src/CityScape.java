
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

import com.sun.j3d.utils.geometry.Primitive;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.picking.*;


@SuppressWarnings("serial")
public class CityScape extends Objloads implements KeyListener, MouseListener, ActionListener{
	
	//add in more action like opening doors,
	
	// Declaring and initialising constants and variables
	public static final int RED = 0xffff0000;
	public static final int GREEN = 0xff00ff00;
	public static final int BLUE = 0xff0000ff; 
	public static final int WHITE = 0xffffffff;
	public static final int BLACK = 0xff000000;
	public static final int YELLOW = 0xffffff00;
	public static final int CYAN = 0xff00ffff;
	public static final int MAGENTA = 0xffff00ff;
		
	private Vector<Vector3f> barriers;
	private Vector<Vector3f> textblocks;
	private float speed = 0.40f;
	private boolean inside;
	private int extra = 10;
	Questions questions;
	private	String challenge,result;
	boolean solved = false;
	private Vector3f text;
	private int num=0;
	public AxisAngle4f rotation = new AxisAngle4f(0.0f, 1.0f, 0.0f, 0.0f);
	public float angle = 0.0f;
	private Transform3D transform;
	private Vector3f translation;
	private Shape3D pickshape;
	private PickCanvas pickCanvas;
	private Appearance pickappearance;
	private Appearance notpickedappearance;
		
	
	JPanel panel1 = new JPanel();
	JPanel panel2 = new JPanel();
	JButton solve,clear,close;
	JLabel label1,label2,label3,question;
	JTextField solution;
	long start = System.currentTimeMillis();
	BranchGroup contentRoot;
	
	public BranchGroup createContentBranch() {
		contentRoot = new BranchGroup();
		
		contentRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		contentRoot.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
		barriers = new Vector<Vector3f>(); 		//vector that holds the locations all objects
		textblocks = new Vector<Vector3f>(); 	//vector that holds the location of the 3d text objects
		
		// Creating and configuring the PickCanvas
	    pickCanvas = new PickCanvas(canvas, contentRoot);
	    pickCanvas.setMode(PickTool.GEOMETRY); 
	    pickCanvas.setTolerance(5.0f);
	    
	    //making a custom appearance to apply when the object is being picked
	    ColoringAttributes ca = new ColoringAttributes(new Color3f(1.0f,0,0), ColoringAttributes.FASTEST);
	    pickappearance = new Appearance();
	    pickappearance.setColoringAttributes(ca);		
	    
		view.setBackClipDistance(100);
		
		//initialising a switch to implement level of detail
		//Switch swit = new Switch(0);
		//swit.setCapability(Switch.ALLOW_SWITCH_READ);
		//swit.setCapability(Switch.ALLOW_SWITCH_WRITE);
		
		
		//reading the map from the image------------------------
	    BufferedImage plan = null;
		try {
			plan = ImageIO.read(new File("res/map.gif"));
		}
		catch(Exception e) {	
				System.out.println(e.toString());		
		}
				
		//getting the length and width of the image
		int width = plan.getWidth();
		int height = plan.getHeight();
		
		for(int y=0; y<height; y++)
			for(int x=0; x<width; x++) {
				int pixel = plan.getRGB(x, y);
				
				if(pixel == BLACK) {
					//contentRoot.addChild(Create3dtext("MATHS IS FUN!",x*10, y*10));
					if(num==0)	contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/bart.png","medium"));
					if(num==1) 	contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/apu.png","medium"));
					if(num==2)  contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/bender.png","medium"));
					if(num==3) //contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/burns.png","medium"));
					if(num==4) contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/lisa.png","medium"));
					if(num==5) contentRoot.addChild(MakeBillboard(x*10, y*10, vtg, "res/billboards/shrek.png","medium"));
						
					contentRoot.addChild(createRoad(x*10, y*10, "res/lawn.jpg"));
					
					barriers.add(new Vector3f(x,0,y));
					System.out.println("BLACK P"+"X: "+x*10+" Z: "+y*10+" CREATING BILLBOARDS");					
					num++;
				}
				else if(pixel == WHITE) {
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					System.out.println("WHITE P"+"X: "+x*10+" Z: "+y*10+" CREATING LAWN");
				}
				else if(pixel == RED) {				
					contentRoot.addChild(MakeBillboard(x*10, y*10, new TransformGroup(), "res/billboards/tree1.png","large"));
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					barriers.add(new Vector3f(x,0,y));
					System.out.println("RED P"+"X: "+x*10+" Z: "+y*10+" CREATING BILLBOARD TREES");
				}
				else if(pixel == GREEN) {
					contentRoot.addChild(Create3dtext("MATHS!", x*10, y*10));
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					barriers.add(new Vector3f(x, 0, y));
					textblocks.add(new Vector3f(x, 0, y));
					System.out.println("GREEN P"+"X: "+x*10+" Z: "+y*10+" CREATING 3D TEXT");
				}
				else if(pixel == BLUE) {
					contentRoot.addChild(MakeTree(x*10, y*10));
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					barriers.add(new Vector3f(x,0,y));
					System.out.println("BLUE P"+"X: "+x*10+" Y: "+y*10+"");
				}
				else if(pixel == YELLOW) {
					contentRoot.addChild(MakeCylinder(x*10, y*10));
					barriers.add(new Vector3f(x,0,y));
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					System.out.println("YELLOW P"+"X: "+x*10+" Z: "+y*10+" CREATING CYLINDER");				
				}
				else if(pixel == CYAN) {
					contentRoot.addChild(CreateWall(x*10, y*10));
					contentRoot.addChild(createRoad(x*10, y*10,"res/lawn.jpg"));
					barriers.add(new Vector3f(x,0,y));					
					System.out.println("CYAN P"+"X: "+x*10+" Z: "+y*10+" CREATING WALL");
				}
				else{
					contentRoot.addChild(createRoad(x*10, y*10, "res/lawn.jpg"));
					System.out.println("OTHER P"+"X: "+x*10+" Z: "+y*10+"CREATING FLOOR");
				}
			}
		
		//setting the background
		TextureLoader bload = new TextureLoader("res/background.jpg",this);
		ImageComponent2D image = bload.getImage();
		Background background = new Background(image);
		background.setApplicationBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
		contentRoot.addChild(background);
		
		// set up the DistanceLOD behavior
		/*float[] distances = new float[3];
		distances[0] = 3.0f;
		distances[1] = 6.0f;
		distances[2] = 15.0f;
		
		DistanceLOD lod = new DistanceLOD(distances);
		lod.addSwitch(swit);
		lod.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0));
		contentRoot.addChild(lod);*/
		
		contentRoot.compile();
		return contentRoot;
	}
	
	public TransformGroup Create3dtext(String text, int x , int z) {
	TransformGroup tg = new TransformGroup();
	
	Transform3D t3d = new Transform3D();
	t3d.setScale(4);
  	t3d.setTranslation(new Vector3f(x,-5.0f,z));
	TransformGroup tg2 = new TransformGroup(t3d);	
	
	tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	
	 // use a customized FontExtrusion object to control the depth of the text
    double X1 = 0,Y1 = 0,X2 = 0.8,Y2 = 0;
    Shape extrusionShape = new java.awt.geom.Line2D.Double(X1, Y1, X2, Y2);

    FontExtrusion fontEx = new FontExtrusion(extrusionShape);

    Font3D f3d = new Font3D(new Font("Tes", Font.BOLD, 1),fontEx);
				
	Text3D text3d = new Text3D(f3d, text);
	text3d.setAlignment(Text3D.ALIGN_CENTER);
		
	notpickedappearance = new Appearance();
	
	Material material = new Material();
    material.setDiffuseColor(0.49f, 0.34f, 0.0f);
    material.setSpecularColor(0.89f, 0.79f, 0.0f);
    material.setShininess(100.0f);
    material.setEmissiveColor(0.49f, 0.34f, 0.0f);
    notpickedappearance.setMaterial(material);
			
    pickshape = new Shape3D(text3d);
    pickshape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    pickshape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
    pickshape.setAppearance(notpickedappearance);
	
	PointLight light1 = new PointLight(new Color3f(1.0f,0.5f,0.5f), new Point3f(), new Point3f(1.0f,0.0f,0.0f));
	PointLight light2 = new PointLight(new Color3f(0.5f,1.0f,0.5f), new Point3f(), new Point3f(1.0f,0.0f,0.0f));
	
	light1.setPosition(x-1.0f, 0, z-1.0f);
	light2.setPosition(x-1.0f, 0, z-1.0f);
	
	light1.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
	light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
	
	float angle = (float)Math.toRadians(180);
	Alpha rotAlpha = new Alpha(-1, 20000);
	Interpolator rotator = new RotationInterpolator(rotAlpha,tg, new Transform3D(),-angle,angle);
	rotator.setSchedulingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
	
	tg.addChild(pickshape);
	tg.addChild(light1);
	tg.addChild(light2);
	tg.addChild(rotator);
	tg2.addChild(tg);
	
	return tg2;
}
	
	//main method
	public static void main(String args[]) {
		new CityScape();		
	}
	
	//no arg constructor
	public CityScape() {
		super();
		
		canvas.addKeyListener(this);
		canvas.addMouseListener(this);
		this.setTitle("MATHS MADE FUN");
		this.add(panel1,BorderLayout.SOUTH);		
		this.add(panel2,BorderLayout.NORTH);
				
		//two panels for top and bottom
		panel1.setLayout(new GridLayout(2, 3));
		panel2.setLayout(new GridLayout(1, 1));		
		
		question = new JLabel("");
		question.setBackground(Color.orange);
		panel2.add(question);
		
		label1 = new JLabel("Enter your answer here:");
		label2 = new JLabel("");
		solution = new JTextField("", 5);
		solution.setBackground(Color.yellow);
		
		Font f = new Font("Dialog", Font.BOLD, 24);
		question.setFont(f);
		question.setBackground(Color.orange);
		
		solve = new JButton("SOLVE..."); solve.setBackground(Color.GREEN); solve.addActionListener(this);
		clear = new JButton("CLEAR..."); clear.setBackground(Color.WHITE); clear.addActionListener(this);
		close = new JButton("GO BACK..."); close.setBackground(Color.RED); close.addActionListener(this);
		
		panel1.add(label1,0); panel1.add(solution,1);panel1.add(label2,2);
		panel1.add(solve,3); panel1.add(clear,4); panel1.add(close,5);
		
		questions = new Questions("res/mathquestions.txt");
		try {	questions.PopulateMap(); } catch (Exception e)	{	e.printStackTrace();	}
		//panel1.setVisible(true);
		//panel2.setVisible(true);
	}
	
	public void keyPressed(final KeyEvent ke) {
		transform = new Transform3D();
		vtg.getTransform(transform);
		translation = new Vector3f();
		transform.get(translation);
										
		if(ke.getKeyCode() == KeyEvent.VK_UP) {
		  translation.z -= Math.cos(angle)*speed;	
		  translation.x -= Math.sin(angle)*speed;
		  System.out.println("KEY PRESSED: UP KEY X: "+translation.x+" Z: "+translation.z);
		}
				
		if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
			translation.z += Math.cos(angle)*speed;
		    translation.x += Math.sin(angle)*speed;
		    System.out.println("KEY PRESSED: DOWN KEY X: "+translation.x+" Z: "+translation.z);
		}
		
		// Handle the left arrow
		if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
		     angle += Math.toRadians(5);	//System.out.println("Current position:"+angle);
		}

		// handle the right arrow
		if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
		      angle -= Math.toRadians(5);
		      //System.out.println("Current position:"+angle);
		}
		inside=false;
		
		//collision detection
		for(int i=0; i<barriers.size(); i++) {
			Vector3f block = barriers.get(i);
						
			if( translation.x > block.x*10-extra  && 
				translation.x < block.x*10+extra  &&
				translation.z > block.z*10-extra  &&
				translation.z < block.z*10+extra) {
						
				inside = true; 
				System.out.println("block detected at "+translation.x+" and "+translation.z+" "+inside);
			}
		}
		
		for(int j=0; j<textblocks.size(); j++) {
			text = textblocks.get(j);
					
			if( translation.x > text.x*10-extra  && 
				translation.x < text.x*10+extra  &&
				translation.z > text.z*10-extra  &&
				translation.z < text.z*10+extra) {
				panel1.setVisible(true); panel2.setVisible(true);
				String[] temp = questions.mathbook.firstElement().split("==");
				challenge = temp[0]; result = temp[1];
				System.out.println("challenge"+challenge+" result:" +result);
				question.setText(challenge);
				System.out.println("Vector size:"+questions.mathbook.size());
						
			}
		}

		if(!inside) {
			rotation.set(0,1,0,angle);
			transform.setTranslation(translation);
			transform.setRotation(rotation);
			vtg.setTransform(transform);
			System.out.println("not inside "+translation.x+" and "+translation.z+" "+inside);
		 }	
				
	}
	
	public void keyReleased(final KeyEvent ke) {
		
		// Handle the up arrow
		if(ke.getKeyCode() == KeyEvent.VK_UP) {
			translation.z -= Math.cos(angle)*0.10f;
		    translation.x -= Math.sin(angle)*0.10f;
		    System.out.println("KEY RELEASED: UP KEY X: "+translation.x+" Z: "+translation.z);
		}

		// Handle the down arrow
		else if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
		   translation.z += Math.cos(angle)*0.10f;
		   translation.x += Math.sin(angle)*0.10f;
		   System.out.println("KEY RELEASED: DOWN KEY X: "+translation.x+" Z: "+translation.z);
		}

		// Handle the left arrow
		else if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
			angle += Math.toRadians(5);
		}
		// handle the right arrow
		else if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
		    angle -= Math.toRadians(5);
		}
		
		inside=false;
		//collision detection
		for(int i=0; i<barriers.size(); i++) {
			Vector3f block = barriers.get(i);
				
			if( translation.x > block.x*10-extra && 
				translation.x < block.x*10+extra  &&
				translation.z > block.z*10-extra  &&
				translation.z < block.z*10+extra) {
				
				inside = true; 
				System.out.println("block detected at "+translation.x+" and "+translation.z+" "+inside);
				
			}
		}
		    if(inside==false) {
			    rotation.set(0,1,0,angle);
				transform.setTranslation(translation);
				transform.setRotation(rotation);
				vtg.setTransform(transform);
				System.out.println("not inside "+translation.x+" and "+translation.z+" "+inside);
			}		
	}
	
	public void keyTyped(KeyEvent e) {	
		
	}
	
	public void mouseClicked(MouseEvent me) {
		pickCanvas.setShapeLocation(me);
		PickResult result = pickCanvas.pickClosest();
		 
		if(result==null) {
			 System.out.println("Nothing picked so far");
		 }
		else {
			Primitive p = (Primitive)result.getNode(result.PRIMITIVE);
			Shape3D s = (Shape3D)result.getNode(result.SHAPE3D);
			if(p!=null) {
				System.out.println(p.getClass().getName());
			}
			else if (s != null) { 
				System.out.println(s.getClass().getName());
				s.setAppearance(pickappearance);
				System.out.println("after"+s.getAppearance().toString());
			}
			 else {
				 System.out.println("null");
			 }
		 }		 
	  }

	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(clear)) {
			solution.setText(""); System.out.println("Clear button pressed");
		}
		if(e.getSource().equals(close)) {
			panel1.setVisible(false); System.out.println("Close button pressed");
			panel2.setVisible(false); 
			solution.setText("");	
			label2.setText("");
			question.setText("");
		}
		if(e.getSource().equals(solve)) {
			System.out.println("Solve button pressed");
			
			if(textblocks.size()==0 || questions.mathbook.size()==0) {
				panel1.setVisible(true); panel2.setVisible(true);
				question.setText("CONGRATULATIONS\n****YOU FINISHED LEVEL 1****\n To be more good at Maths your have to practice it more ofter.");
				label1.setText("****YOU FINISHED LEVEL 1****");
				label2.setText("****YOU FINISHED LEVEL 1****");
				this.setTitle("****YOU FINISHED LEVEL 1****");	
			}
			else if(solution.getText().equals(result)) {
				questions.mathbook.removeElementAt(0);				
				label2.setText("Well done you answered it correctly...");
				solution.setText("");
				challenge = ""; result = "";
				solved = true;
				textblocks.remove(text);
				
				System.out.println("Vector size:"+questions.mathbook.size());
				System.out.println("challenge"+challenge+" result:" +result);
			}
			else {
				label2.setText("You haven't got the correct answer yet, try again...");
				solved = false;
			}
		}		
	}
}
