import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

//an object that holds a vector of 20 maths questions
public class Questions {

	String filename;
	Vector<String> mathbook = new Vector<String>();
	
	public Questions(String filename) {
		this.filename=filename;
	}
	
	//method save the data from the filename to a map
	public void PopulateMap() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line ="";
		while((line=reader.readLine())!=null) {
			mathbook.add(line);
		}
		
		reader.close();
	}
}
