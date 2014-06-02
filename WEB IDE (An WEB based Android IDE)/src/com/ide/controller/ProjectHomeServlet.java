package com.ide.controller;

import static com.ide.json.JsonInJava.createJSONFileStructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.ide.json.JSONArray;
import com.ide.json.JSONObject;
import com.ide.model.AndroidCompiler;
import com.ide.model.AndroidProject;
import com.ide.utils.IDEUtils;

@WebServlet(name = "home", urlPatterns = { "/home" })
public class ProjectHomeServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private AndroidProject androidProject;
	private AndroidCompiler androidCompiler;
	private String target, projectName, path, activity, packageName;
	private static final String GOOGLE_SERVER_KEY = "cannot be made public";
	private static final String MESSAGE_KEY = "message";
	private boolean isLogging = false;

	@Override
	public void init() throws ServletException {
		String newPath = getServletContext().getRealPath("/");
		
		File file = new File(newPath+"AndroidProjectFiles");
		file.mkdir();
		file = new File(newPath+"AndroidLogs");
		file.mkdir();
		file = new File(newPath+"ProjectBuilds");
		file.mkdir();
		file = new File(newPath+"ProjectStructures");
		file.mkdir();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if ("project".equals(request.getParameter("action"))) {
			sendProjectStucture(request, response);
			return;
		}
		
		if ("autocomplete".equals(request.getParameter("action"))) {
			response.getWriter().print(sendAutoCompleteJson(request,response));
			System.out.println("autocomplete called");
			return;
		}
		
		if ("currentproject".equals(request.getParameter("action"))) {
			String rootPath = getServletContext().getRealPath("/AndroidProjectFiles");
			File file = new File(rootPath);
			if(file.exists()) {
				File[] list = file.listFiles();
				if(list[0].isDirectory()) {
					response.getWriter().print(list[0].getName());
				}
			}			
			return;
		}
		
		if ("content".equals(request.getParameter("action"))) {
			sendTextContent(request, response);
			return;
		}
		
		if ("getlog".equals(request.getParameter("action"))) {
			try {
				sendLog(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

		if ("buildoutput".equals(request.getParameter("action"))) {
			sendBuildOutput(request, response);
			return;
		}

		if ("compile".equals(request.getParameter("action"))) {
			compileAndroidProject(request, response);
			return;
		}

		if ("errordata".equals(request.getParameter("action"))) {
			sendErrorData(request, response);
			return;
		}

		if ("downloadapk".equals(request.getParameter("action"))) {
			String projectName = request.getParameter("name");

			System.out.println("Checking if apk exists ...");
			String rootPath = getServletContext().getRealPath("/AndroidProjectFiles");
			String path = rootPath + "/" + projectName + "/bin/" + projectName+ "-debug.apk";

			if (path.equals("")) {
				System.out.println("Apk does not exist");
				// writer.println("Apk does not exist, either you haven't compiled your project, or the project does not exist");
				return;
			}

			if (new File(path).exists()) {
				response.setContentType("application/vnd.android.package-archive");
				response.setHeader("Content-Disposition",
						"attachment;filename=" + projectName + "-debug.apk");
				ServletContext context = getServletContext();
				InputStream inputStream = new FileInputStream(path);

				System.out.println("Context: " + context);
				System.out.println("InputStream: " + inputStream);
				System.out.println("Path: " + path);

				int read = 0;
				byte[] bytes = new byte[1024];

				OutputStream outputStream = response.getOutputStream();

				while ((read = inputStream.read(bytes)) != -1) {
					outputStream.write(bytes, 0, read);
				}
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				// writer.close();
				return;
			}
			System.out.println("Invalid path for APK File:" + path);
		}
		System.out.println("Get called");

		RequestDispatcher view = request.getRequestDispatcher("home.html");
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if ("gcmcreg".equals(request.getParameter("action"))) {
			String rootPath = getServletContext().getRealPath("/");
			String share = request.getParameter("shareRegId");			

			// GCM RedgId of Android device to send push notification
			String registrationID = "";
			if (share != null && !share.isEmpty()) {
				registrationID = request.getParameter("regId");
				File file = new File(rootPath+"/GCMRegistration/GCMRegId.txt");
				if (!file.exists()) {
					file.createNewFile();
				}
				
				PrintWriter writer = new PrintWriter(rootPath+"/GCMRegistration/GCMRegId.txt");
				writer.println(registrationID);
				writer.close();
			}
		}
		if ("lastbuildtime".equals(request.getParameter("action"))) {
			String projectName = request.getParameter("name");
			PrintWriter writer = response.getWriter();

			System.out.println("Last modified time requested ...");

			String androidProjectsPath = getServletContext().getRealPath("/AndroidProjectFiles");
			File file = new File(androidProjectsPath + "/" + projectName);
			if (file.exists()) {
				writer.println(file.lastModified());
			} else {
				writer.println("File not found");
			}
			writer.close();
			return;
		}

		//saving logs from the device to server
		if ("logcat".equals(request.getParameter("action"))) {
			String path = getServletContext().getRealPath("/AndroidLogs/AndroidApplication.log");
		
			BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {				
				String log = formatLog(line);
					//System.out.println(log);
					writeToLogFile(path,log);
			}
			reader.close();

			return;
		}		

		if ("save".equals(request.getParameter("action"))) {

			PrintWriter writer = response.getWriter();
			System.out.println("Saving files from client ...");

			String rootpath = getServletContext().getRealPath("/");
			String filePath = request.getParameter("filepath");
			String fileContent = request.getParameter("filecontent");

			if (writeFile(rootpath+"AndroidProjectFiles/" + filePath, fileContent)) {
				writer.println("File saved successfully");
				writer.close();
				return;
			} else {
				writer.println("An error occured while saving the file, please try again");
				writer.close();
				return;
			}
		}
		
		if ("filedelete".equals(request.getParameter("action"))) {
			String path = request.getParameter("file");
			
			File file = new File(getServletContext().getRealPath("/")+"AndroidProjectFiles/"+path);
			System.out.println("Deleting file: "+file.getAbsolutePath());
			
			if(file.exists()) {
				file.delete();
				response.getWriter().print("success");
				return;
			}
			return;
		}
		
		if ("folderdelete".equals(request.getParameter("action"))) {
			String path = request.getParameter("file");
			
			File file = new File(getServletContext().getRealPath("/")+"AndroidProjectFiles/"+path);
			System.out.println("Deleting file: "+file.getAbsolutePath());
			
			if(!file.exists()) {
				response.getWriter().print("fail");
				return;
			} else {
				StringBuffer buffer = new StringBuffer();
				IDEUtils.deleteDirectory(file,buffer);
				response.getWriter().print(buffer.toString());
				return;
			}
		}
		
		if ("filecreate".equals(request.getParameter("action"))) {
			String path = request.getParameter("file");
			
			File file = new File(getServletContext().getRealPath("/")+"AndroidProjectFiles/"+path);
			
			System.out.println("Creating file: "+file.getAbsolutePath());
			
			if(file.exists()) {
				response.getWriter().print("fail");
				return;
			} else if(file.createNewFile()) {				
				response.getWriter().print("success");
				return;
			}
		}
		
		if ("filerename".equals(request.getParameter("action"))) {
			String path = request.getParameter("file");
			String newName = request.getParameter("newname"); 
			
			String oldPath = getServletContext().getRealPath("/")+"AndroidProjectFiles/"+path;
			String newPath = oldPath.replace(oldPath.substring(oldPath.lastIndexOf(File.separator)), "/"+newName);
			
			File file = new File(oldPath);
			File newFile = new File(newPath);
			System.out.println("Renaming file: "+file.getAbsolutePath());
			System.out.println("Renamed file: "+newFile.getAbsolutePath());
			
			if(!file.exists()) {
				response.getWriter().print("fail");
				return;
			} else if(file.renameTo(newFile)) {				
				response.getWriter().print("success");
				return;
			} else {
				response.getWriter().print("fail");
				return;
			}
		}
		
		if ("foldercreate".equals(request.getParameter("action"))) {
			String path = request.getParameter("file");
			
			File file = new File(getServletContext().getRealPath("/")+"AndroidProjectFiles/"+path);
			System.out.println("Creating folder: "+file.getAbsolutePath());
			
			if(file.exists()) {
				response.getWriter().print("fail");
				return;
			} else if(file.mkdirs()) {				
				response.getWriter().print("success");
				return;
			}
		}
		
		target = request.getParameter("target");
		projectName = request.getParameter("projectName");
		path = "/" + request.getParameter("projectName");
		activity = request.getParameter("activity");
		packageName = request.getParameter("packageName");
		
		if(IDEUtils.validate(new String[]{target,projectName,path,activity,packageName})) {
			
			String targ = target.split("[ ]")[0];
			System.out.println("Package Name: "+ targ);
			
			androidProject = new AndroidProject(targ, projectName, path,activity, packageName);
			
			String newPath = getServletContext().getRealPath("/");
			androidCompiler = new AndroidCompiler(androidProject,newPath+"AndroidProjectFiles");

			System.out.println(androidProject);
			synchronized (response) {
				String buildOutput = androidCompiler.createAndroidProject();
				buildOutput.replace(newPath, androidProject.getProject_name()+"\\");
				writeFile(newPath+"ProjectBuilds/" + path + ".txt", buildOutput);
				generateProjectStructure(androidProject.getProject_name(),newPath);
			}
		}
	}

	private String formatLog(String log) {
		/* format [{\"message\":\"hahahaha\",\"type\":\"info\"},"
		 * V — Verbose (lowest priority)
		 * D — Debug
		 * I — Info
		 * W — Warning
		 * E — Error
		 * F — Fatal
		 * S — Silent (highest priority, on which nothing is ever printed)
		 * */
		StringBuffer buffer = new StringBuffer();
		JSONObject logJsonObject = new JSONObject();
		logJsonObject.put("message", log);
		//StringBuffer buffer = new StringBuffer();
		
		//buffer.append("{\"message\":\"").append(log).append("\",\"type\":\""); //{"message":"log","type":"sss"},
		
		if (log.startsWith("V/")) logJsonObject.put("type", "verbose"); 
		if (log.startsWith("D/")) logJsonObject.put("type", "debug");
		if (log.startsWith("I/")) logJsonObject.put("type", "info");
		if (log.startsWith("W/")) logJsonObject.put("type", "warning");
		if (log.startsWith("E/")) logJsonObject.put("type", "error");
		if (log.startsWith("F/")) logJsonObject.put("type", "fatal");
		if (log.startsWith("S/")) logJsonObject.put("type", "silent");

		String temp = logJsonObject.toString();
		if(temp.contains("message") && temp.contains("type")) {
			buffer.append(temp).append(",").append("\n");
			return buffer.toString();
		}		
		return "";
	}

	private void sendErrorData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String projectName = request.getParameter("name");

		String errorJson = "no errors to display";
		String buildOutput = getBuildOutput(projectName);
		if (buildOutput.contains("BUILD FAILED")) {
			try {
				errorJson = "" + getErrorFromBuild(projectName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		PrintWriter writer = response.getWriter();
		writer.println(errorJson);
		writer.close();
	}
	

	private String sendAutoCompleteJson(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String jsonPath = getServletContext().getRealPath("/json/autocomplete.json");
		StringBuffer buffer = new StringBuffer();
		
		BufferedReader reader = new BufferedReader(new FileReader(jsonPath));

		String line = "";
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		reader.close();
		//System.out.println("Sending suto complete to client: "+ buffer);
		return buffer.toString();		
	}

	private void compileAndroidProject(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String projectName = request.getParameter("name");
		String rootpath = getServletContext().getRealPath("/");

		synchronized (projectName) {
			String compilerOutput = new AndroidCompiler(rootpath).
					compileAndroidProject("AndroidProjectFiles/" + projectName);
			//compilerOutput.replace(rootpath, projectName+"\\");

			PrintWriter writer = response.getWriter();
			writer.println(compilerOutput);
			writer.close();
			
			//String jsonFile = rootpath+"/AndroidProjectFiles/"+ projectName + ".json";
			generateProjectStructure(projectName,rootpath);
			writeFile(rootpath + "ProjectBuilds/" + projectName + ".txt",compilerOutput);
			
			if(compilerOutput.contains("BUILD SUCCESSFUL")) {
				String buildPath = getServletContext().getRealPath("/ProjectBuilds");
				File errorfile = new File(buildPath +"/"+projectName + ".txt");
				if(errorfile.exists()) {
					errorfile.delete();
				}
				System.out.println("Build successful sending message to user...");
				/*try {
					BufferedReader br = new BufferedReader(new FileReader(rootpath+"/GCMRegistration/GCMRegId.txt"));
					String registrationID = br.readLine();
					br.close();
					
					Sender sender = new Sender(GOOGLE_SERVER_KEY);
					Message message = new Message.Builder().timeToLive(30)
							.delayWhileIdle(true).addData(MESSAGE_KEY, "New APK for project "+
														  projectName+" is ready.").build();
					
					System.out.println("regId: " + registrationID);
					Result result = sender.send(message, registrationID, 1);
					System.out.println("Push Message Status: "+ result);
				} catch (IOException e) {
					e.printStackTrace();

				} catch (Exception e) {
					e.printStackTrace();
				}*/
			}
		}
	}

	private void sendBuildOutput(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String projectName = request.getParameter("name");
		PrintWriter writer = response.getWriter();
		String buildOutput = getBuildOutput(projectName);
		
		writer.println(buildOutput);
		writer.close();
	}

	private void sendProjectStucture(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String projectName = request.getParameter("name");
		PrintWriter writer = response.getWriter();

		String rootPath = getServletContext().getRealPath("/AndroidProjectFiles");
		File file = new File(rootPath +"/"+projectName);
		if (file.exists()) {
			JSONArray jsonArray = new JSONArray();
			createJSONFileStructure(jsonArray, new File(rootPath +"/"+projectName),rootPath);
			writer.println(jsonArray);
		} else {
			writer.println("");
		}
		writer.close();
	}	

	private void sendTextContent(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String rootPath = getServletContext().getRealPath("/AndroidProjectFiles");
		String filePath = request.getParameter("filepath");
		//System.out.println(rootPath+"/"+filePath);
		BufferedReader reader = new BufferedReader(new FileReader((rootPath+"/"+filePath)));
		
		StringBuffer buffer = new StringBuffer("");
		String line = "";
		while ((line = reader.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		response.getWriter().println(buffer.toString());
		reader.close();
	}
	
	private void sendLog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		isLogging = true;
		if("stoplog".equals(request.getParameter("logflag"))) {
			isLogging = false;
		}
		//String projectName = request.getParameter("name");
		
		String androidLogPath = getServletContext().getRealPath("/AndroidLogs");
		File file = new File(androidLogPath + "/AndroidApplication.log");
		long fileLength = file.length();
		if (request.getParameter("bytes") == null) {
			response.getWriter().println(fileLength);
			return;
		}
		
		long bytes = new Long(request.getParameter("bytes"));
		if(bytes < fileLength) {
			System.out.println("bytes: "+ bytes+" filelength: "+ fileLength);
			//response.getWriter().println(sendLogAsJson(file, bytes));
			response.getWriter().println(sendLogAsJson(file, bytes));
			return;
		}
		while(bytes == fileLength && isLogging) {
			System.out.println("Waiting for new log entries ...");
			Thread.sleep(5000);
		}
		response.getWriter().println(sendLogAsJson(file, bytes));
	}	

	private String sendLogAsJson(File logFile, long bytesToSkip) throws Exception {
		StringBuffer buffer = new StringBuffer("[");
		
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile)))) {
			reader.skip(bytesToSkip);
			//byte[] byteBuffer = new byte[1024];
			String currentLine = "";
			
			while((currentLine = reader.readLine() ) !=null) {
				String line = currentLine;
				System.out.println("Raw data from server: "+ line);
				
				if(!IDEUtils.isEmpty(line)) {
					if(!line.endsWith("},")) {
						line = line.substring(0, line.lastIndexOf("},"))+"},";
					}
					buffer.append(line);
					System.out.println("Sending logs to client: "+ line);
				}								
			}
		}
		buffer.append("]");
		return buffer.toString();
	}

	private String getBuildOutput(String projectName) throws IOException {
		StringBuffer buffer = new StringBuffer();
		String line = "";
		String buildPath = getServletContext().getRealPath("/ProjectBuilds");
		
		if(!IDEUtils.checkFileStatus(buildPath +"/"+projectName + ".txt")) {
			return "Error while parsing build output";
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(
				buildPath +"/"+projectName + ".txt"));

		while ((line = reader.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		reader.close();

		return buffer.toString();
	}

	private JSONArray getErrorFromBuild(final String projectName)
			throws Exception {
		String line = "";
		String rootPath = getServletContext().getRealPath("/");

		BufferedReader reader = new BufferedReader(new FileReader
										(rootPath +"ProjectBuilds/"+ projectName + ".txt"));
		
		System.out.println("Error build path: "+ rootPath +"ProjectBuilds/"+ projectName + ".txt");

		JSONArray jsonArray = new JSONArray();
		while ((line = reader.readLine()) != null) {
			if (line.contains("[javac]")) {
				line = line.replace("    [javac] ", "");
				if (line.contains(".java")) {
					line = line.replace(rootPath+"AndroidProjectFiles\\","");

					// System.out.println(line);
					String[] items = line.split(" ");

					String errorType = items[1].replace(":", "");
					String file = items[0];
					String lineNumber = items[0].substring(items[0].indexOf(":"), items[0].lastIndexOf(":"))
							.replace(":", "");
					file = file.split(":")[0];
					String name = file.substring(file.lastIndexOf("\\")+1, file.length());

					String description = items[2] + " " + items[3];

					JSONObject jsonObject = new JSONObject();
					jsonObject.put("type", errorType);
					jsonObject.put("file", file);
					jsonObject.put("linenumber", lineNumber);
					jsonObject.put("description", description);
					jsonObject.put("name", name);
					jsonArray.put(jsonObject);
					System.out.println("JSON Object: "+ jsonObject);
				}
			}
		}
		reader.close();
		System.out.println("Json Error Array: " + jsonArray);
		return jsonArray;
	}

	private void generateProjectStructure(String projectName, String rootPath) {
		try {	
			String jsonFile = rootPath + "ProjectStructures/"+projectName + ".json";

			//System.out.println("File path: "+ jsonFile);
			JSONArray jsonArray = new JSONArray();
			
			createJSONFileStructure(jsonArray, new File(rootPath+"AndroidProjectFiles/" + projectName),rootPath);

			if (writeFile(jsonFile, "" + jsonArray)) {
				System.out.println("Project Structure created Successfully");
				return;
			}
			System.out.println("Error occured while writing file ...");

		} catch (IOException e) {
			System.out.println("Project Structure creation failed");
			e.printStackTrace();
		}

	}

	private boolean writeFile(final String fileName, final String fileContent) {
		//System.out.println("WRITE: Name: "+ fileName);
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
			//System.out.println("Updating file: " + file.getName());
			//System.out.println("Writing to file " + file.getPath());
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(fileContent);
			bufferedWriter.close();

			return true;

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void writeToLogFile(String filePath, String content) throws IOException {
		File logFile = new File(filePath); 
		if(!logFile.exists()) logFile.createNewFile();		
		
		if(content == null || content.length() == 0) return;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(logFile,true));
		writer.append(content);
		//System.out.print("Writing log "+content);
		writer.close();
	}

}
