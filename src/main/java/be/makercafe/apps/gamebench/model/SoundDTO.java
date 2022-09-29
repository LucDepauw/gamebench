package be.makercafe.apps.gamebench.model;

public class SoundDTO {
	private String path;

	/**
	 * Create sound data
	 * @param path Path to wav file, relative to cartridge file
	 */
	public SoundDTO(String path) {
		super();
		this.path = path;
	}
	
	public SoundDTO() {
		super();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
