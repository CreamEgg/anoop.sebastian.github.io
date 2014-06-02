package com.ide.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AndroidCompiler {
	private AndroidProject androidProject;
	private static String buildDir = null;
	volatile String output = "";

	public AndroidCompiler(String executionPath) {
		buildDir = executionPath;
	}

	public AndroidCompiler(AndroidProject androidProject, String executionPath) {
		//if (IDEUtils.checkFileStatus(executionPath)) 
		buildDir = executionPath;
		this.androidProject = androidProject;
	}

	public String createAndroidProject() throws IOException {
		makeDirectory();
		String[] androidCreate = getCreateCommand();
		CommandExecutor commandExecutor = new CommandExecutor(buildDir);
		return commandExecutor.executeCommand(androidCreate);
	}

	private void makeDirectory() {
		File androidProjectsFolder = new File(buildDir);

		if (androidProjectsFolder.mkdir()) {
			System.out.println("AndroidFiles Directory Created");
			return;
		}
		System.out.println("AndroidFiles Directory already exits");
	}

	private String[] getCreateCommand() {
		return new String[] { "cmd", "/c", "android create project","--target", 
							  androidProject.getProject_target(), "--name",
							  androidProject.getProject_name(), "--path",
							  androidProject.getProject_path(), "--activity",
							  androidProject.getProject_activity(), "--package",
							  androidProject.getProject_package() };
	}

	public String compileAndroidProject(final String projectPath) throws IOException {
		System.out.println("Compiling android project ...");
		
		String[] androidCompile = { "cmd", "/c", " ant", "debug" };
		
		CommandExecutor commandExecutor = new CommandExecutor(buildDir + projectPath);
		
		System.out.println("Executing command in: " + buildDir + projectPath);
		try {
			output = commandExecutor.executeCommand(androidCompile);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Successfully compiled project" + output);
		return output;
	}

	public void updateFile(final String filePath, final String fileContent) {
		writeFile(filePath, fileContent);
	}

	private void writeFile(String filePath, String content) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("New file created: " + file.getPath());
			}

			BufferedWriter writer = new BufferedWriter(new FileWriter(
					file.getAbsolutePath()));
			System.out.println("Path: " + file.getAbsolutePath());
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("File written sucessfully");
	}

	public static String getApkFile(final String projectName) {
		// String projectName = androidProject.getProject_name();
		String path = buildDir + "\\" + projectName + "\\bin\\" + projectName
				+ "-debug.apk";
		// System.out.println("APK File: "+path);

		return (new File(path).exists()) ? path : "";
	}
}
