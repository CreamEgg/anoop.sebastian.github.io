import java.util.Map;
import java.util.Vector;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.image.TextureLoader;


@SuppressWarnings("serial")
public class Objloads extends BasicScene{
	
	Map<String,Shape3D> shapes;
	BranchGroup broot;
	
	//Methods that create different 3D objects and locate them in the specified location---------------
	
		
	//method to make a tree with leaves and trunk
	public TransformGroup MakeTree(float x, float z) {
		Transform3D t3d = new Transform3D();
	  	t3d.setTranslation(new Vector3f(x,4.0f,z));
	  	t3d.setScale(20);
		TransformGroup tg = new TransformGroup(t3d);
		
		Vector<Shape3D> parts = GetShapes("res/tree/tree.obj"); //System.out.println(parts);
		
		//making shape3D objects to represent the shapes retieved
		Shape3D trunk = parts.firstElement();
		Shape3D leaves = parts.lastElement();
		
		//adding the shapes to transformgroup
		tg.addChild(trunk);
		tg.addChild(leaves);
		
		//getting the appearance using the method GetAppearance(String)
		trunk.setAppearance(GetAppearance("res/tree/Bark.jpg"));
		leaves.setAppearance(GetAppearance("res/tree/leaves.jpg"));
		
		return tg;
	}
	
	//method to make a cylinder and add two directional light, pointlights and an ambient light
	public TransformGroup MakeCylinder(int x, int z ){
		Transform3D t3d = new Transform3D();
	  	t3d.setTranslation(new Vector3f(x,4.0f,z));
	  	t3d.setScale(5);
		TransformGroup tg = new TransformGroup(t3d);
		
		Appearance rapp = new Appearance();
		Material material = new Material();
	    material.setDiffuseColor(0.37f, 0.37f, 0.37f);
	    material.setSpecularColor(0.89f, 0.89f, 0.89f);
	    material.setShininess(17.0f);
	    material.setEmissiveColor(0.37f, 0.37f, 0.37f);
	    rapp.setMaterial(material);
		
	    DirectionalLight dirLight = new DirectionalLight(new Color3f(0.37f, 0.37f, 0.37f ), new Vector3f( x*5,0,z*5) );
		dirLight.setInfluencingBounds( new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ),Double.MAX_VALUE ) );
		tg.addChild( dirLight );
		
		DirectionalLight dirLight2 = new DirectionalLight(new Color3f(0.37f, 0.37f, 0.37f), new Vector3f( x*10,0,z*10));
		dirLight.setInfluencingBounds( new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ),Double.MAX_VALUE ) );
		tg.addChild( dirLight2 );
		
		AmbientLight ambientLight = new AmbientLight(new Color3f(0.37f, 0.37f, 0.37f));
	    ambientLight.setInfluencingBounds(new BoundingSphere( new Point3d( 0.0, 0.0, 0.0 ),Double.MAX_VALUE ));
	    tg.addChild(ambientLight);
		
		PointLight light1 = new PointLight(new Color3f(0.37f, 0.37f, 0.37f), new Point3f(), new Point3f(1.0f,0.0f,0f));
		PointLight light2 = new PointLight(new Color3f(0.37f, 0.37f, 0.37f), new Point3f(), new Point3f(1.0f,0.0f,0f));
		
		light1.setPosition(x*5,0,z*5);
		light2.setPosition( x*10,0,z*10);
		
		light1.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
		light2.setInfluencingBounds(new BoundingSphere(new Point3d(), Double.MAX_VALUE));
		tg.addChild(light1);
		tg.addChild(light2);
		
		Cylinder left = new Cylinder(1, 5, Cylinder.GENERATE_NORMALS, 50, 50, rapp);
	    
		tg.addChild(left);
		return tg;
	}	
	
	//creates a wall model
	public TransformGroup CreateWall(int x,int z) {
		//------------------locates the transformgroup
		Transform3D translate = new Transform3D();
		translate.setTranslation(new Vector3f(x,-4f,z));
		TransformGroup tg = new TransformGroup(translate);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		
		Box wall = new Box(5f, 5f, 5f, Box.GENERATE_TEXTURE_COORDS, GetAppearance("res/stonewall.jpg"));
		tg.addChild(wall);
		
		return tg;
	}
	
	//method to create texture mapped floor
	public TransformGroup createRoad(float x, float z, String texture) {		
		Box box = new Box(5f, 0.1f, 5f, Box.GENERATE_TEXTURE_COORDS,GetAppearance(texture));		
		
		Transform3D transform3D = new Transform3D();
		transform3D.setTranslation(new Vector3f(x, -10.0f, z));
		TransformGroup transformGroup = new TransformGroup(transform3D);
		transformGroup.addChild(box);		
		
		return transformGroup;		
	}	

	//method to billboards
	public TransformGroup MakeBillboard(float x, float z,TransformGroup tg2,String texture,String size) {
		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
		Transform3D t3d = new Transform3D();
		
		if(size.equals("small")) {
			t3d.setTranslation(new Vector3f(x,-5,z));
		  	t3d.setScale(new Vector3d(5,10,5));
		}
		if(size.equals("large")) {
			t3d.setTranslation(new Vector3f(x,25f,z));
		  	t3d.setScale(new Vector3d(70,90,70));
		}
		if(size.equals("medium")) {
			t3d.setTranslation(new Vector3f(x,0f,z));
		  	t3d.setScale(20);
		}
	  	
		tg2 = new TransformGroup(t3d);
	  	tg1.addChild(tg2);
	  	
	  	//transformgroup to use with the billboard
	  	TransformGroup tg3 = new TransformGroup();
	    tg3.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
	    tg2.addChild(tg3);
		    
	    //dimentions for the quad
	    float[] coordinates = {	-0.5f, -0.5f,  0.0f, 0.5f, -0.5f,  0.0f,
								 0.5f,  0.5f,  0.0f, -0.5f,  0.5f,  0.0f};

	    // Define the texture coordinates for the vertices
	    float[] texCoords = {0.0f, 0.0f, 1.0f, 0.0f, 1.0f,  1.0f, 0.0f, 1.0f};    						

	    // Create a geometry array from the specified coordinates
	    GeometryArray geometryArray = new QuadArray(4,
	    GeometryArray.COORDINATES|GeometryArray.TEXTURE_COORDINATE_2);

	    geometryArray.setCoordinates(0, coordinates);
	    geometryArray.setTextureCoordinates(0,0,texCoords);

	    Appearance app = GetAppearance(texture);
	    TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.BLENDED, 0f);
	    app.setTransparencyAttributes(ta);
	    
	    // Create a Shape3D object using the GeometryArray
	    Shape3D shape = new Shape3D(geometryArray, app);
	    tg3.addChild(shape);
		    
		Billboard billboard = new Billboard(tg3, 
		Billboard.ROTATE_ABOUT_AXIS,new Point3f(0.0f, 0.0f, 0.0f));
		billboard.setSchedulingBounds( new BoundingSphere(new Point3d(), Double.POSITIVE_INFINITY));
		tg3.addChild(billboard);	
			
		return tg1;
	}
	//method that loads an obj file and return
	@SuppressWarnings("unchecked")
	public Vector<Shape3D> GetShapes(String objfile) {
		
		Vector<Shape3D> temp = new Vector<>();
		//loading the 3d file Resize the scene to fit the display
		int flags = ObjectFile.RESIZE;
							    
		// Create a new .obj loader
		ObjectFile f = new ObjectFile(flags);
		Scene s = null;
				
		try {	s = f.load(objfile);	}
		catch(Exception e) {	System.out.println("error:"+e.toString());	}
				
		//getting the named pbjects stored in the obj file
		shapes = s.getNamedObjects();
				
		broot = s.getSceneGroup(); //getting the scenegroup to a branchgroup
				
		for(String name: shapes.keySet()) {
			System.out.println("Name: "+name); 
			Shape3D shape = shapes.get(name); temp.add(shape);
			broot.removeChild(shape);
		}
		return temp;
	}
		
	//method that returns an appearance object when a texture is supplied
	public Appearance GetAppearance(String texture) {
		
		Appearance appearance = new Appearance();
		
		//texture attributes of the appearance
		Texture text = null; 		
		TextureLoader loader = new TextureLoader(texture, this);		
		text = loader.getTexture();
		appearance.setTexture(text);
		
		//texture attributes
		TextureAttributes textattributes = new TextureAttributes();
		textattributes.setTextureMode(TextureAttributes.MODULATE);
		appearance.setTextureAttributes(textattributes);
			    
		return appearance;
	}

}
