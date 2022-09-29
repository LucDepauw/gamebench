package be.makercafe.apps.gamebench.model;

import java.util.ArrayList;
import java.util.List;

public class GameCartridge {
	private String name;
	private String author;
	private String description;
	private String frontScreen;
	private int version;
	
	private List<LevelDTO> levels = new ArrayList<LevelDTO>();
	
	public GameCartridge() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFrontScreen() {
		return frontScreen;
	}

	public void setFrontScreen(String frontScreen) {
		this.frontScreen = frontScreen;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<LevelDTO> getLevels() {
		return levels;
	}

	public void setLevels(List<LevelDTO> levels) {
		this.levels = levels;
	}
	
	
}
