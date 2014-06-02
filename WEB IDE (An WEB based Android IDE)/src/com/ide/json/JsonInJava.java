package com.ide.json;

import java.io.File;
import java.io.IOException;

public class JsonInJava {

	public static void createJSONFileStructure(JSONArray jsonArray, File directory, String rootPath) 
			throws IOException {
		
		if (directory.isDirectory()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("text", directory.getName());
			jsonObject.put("id", directory.getPath().replace(rootPath+"\\", ""));
			JSONArray subDirectory = new JSONArray();

			for (File file : directory.listFiles()) {
				createJSONFileStructure(subDirectory, file, rootPath);
			}

			jsonObject.put("children", subDirectory);
			// jsonObject.put("path", directory.getPath());
			jsonArray.put(jsonObject);
		} 
		else {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", directory.getPath().replace(rootPath+"\\", ""));
			jsonObject.put("text", directory.getName());
			jsonObject.put("leaf", true);
			jsonArray.put(jsonObject);
		}
	}
}
