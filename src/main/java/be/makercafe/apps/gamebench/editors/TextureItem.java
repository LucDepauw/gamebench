package be.makercafe.apps.gamebench.editors;

public class TextureItem {
	
	private int index;
	private String path;
	private String name;
		
	public TextureItem(int index, String path, String name) {
		super();
		this.index = index;
		this.path = path;
		this.name = name;
	}
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
