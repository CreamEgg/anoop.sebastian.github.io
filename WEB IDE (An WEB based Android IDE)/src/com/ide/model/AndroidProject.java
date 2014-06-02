package com.ide.model;

public class AndroidProject {
	private String project_target;
	private String project_name;
	private String project_path;
	private String project_activity;
	private String project_package;

	public AndroidProject() {
	}

	public AndroidProject(String project_target, String project_name,
			String project_path, String project_activity, String project_package) {
		this.project_target = project_target;
		this.project_name = project_name;
		this.project_path = project_path;
		this.project_activity = project_activity;
		this.project_package = project_package;
	}

	public String getProject_target() {
		return project_target;
	}

	public String getProject_name() {
		return project_name;
	}

	public String getProject_path() {
		return project_path;
	}

	public String getProject_activity() {
		return project_activity;
	}

	public String getProject_package() {
		return project_package;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AndroidProject [project_target=");
		builder.append(project_target);
		builder.append(", project_name=");
		builder.append(project_name);
		builder.append(", project_path=");
		builder.append(project_path);
		builder.append(", project_activity=");
		builder.append(project_activity);
		builder.append(", project_package=");
		builder.append(project_package);
		builder.append("]");
		return builder.toString();
	}
}
