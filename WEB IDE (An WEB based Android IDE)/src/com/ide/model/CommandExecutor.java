package com.ide.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.lang.Process;

public class CommandExecutor {
	
	private ProcessBuilder processBuilder = null;
	private Process process = null;
	private String executionDirectory = null;
	
	public CommandExecutor(String executionDirectory) {
		this.executionDirectory = executionDirectory;
	}

	public String executeCommand(String[] command) throws IOException {
		processBuilder = new ProcessBuilder(command);
		processBuilder.directory(new File(executionDirectory));

		// correlate error messages with the corresponding output
		processBuilder.redirectErrorStream(true);		
		process = processBuilder.start();
		return getOutputFromProcess();
	}

	private String getOutputFromProcess() throws IOException {
		StringBuffer buffer = new StringBuffer("------ Executing ");
		buffer.append(processBuilder.command())
		.append("------\n");
		
		String line = "";
		BufferedReader reader = getInputStreamFromProcess();

		while ((line = reader.readLine()) != null) {
			buffer.append(line).append("\n");
		}
		reader.close();
		process.destroy();

		buffer.append("Process exited with value: ")
		.append(process.exitValue())
		.append("\n");

		return buffer.toString();
	}

	private BufferedReader getInputStreamFromProcess() {
		return new BufferedReader(new InputStreamReader(
				process.getInputStream()));
	}
}
