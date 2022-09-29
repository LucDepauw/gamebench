package be.makercafe.apps.gamebench.model;

public class TextureDTO {
	
	private String path;
	private int size;
	
	
	/**
	 * Create texture data
	 * @param path Path to the image relative to the position of the cartridge file
	 * @param size Dimension of the square texture, default = 64 (64 x 64 pixels)
	 */
	public TextureDTO(String path, int size) {
		super();
		this.path = path;
		this.size = size;
	}
	
	public TextureDTO() {
		super();
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	

}
