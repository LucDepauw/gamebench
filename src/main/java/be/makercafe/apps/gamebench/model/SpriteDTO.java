package be.makercafe.apps.gamebench.model;

public class SpriteDTO {
	public double x;
	public double y;
	public int texture;

	/**
	 * Creates sprite data
	 * @param x Sprite x position in the map
	 * @param y Sprite y position in the map
	 * @param texture Texture number
	 */
	public SpriteDTO(double x, double y, int texture) {
		super();
		this.x = x;
		this.y = y;
		this.texture = texture;
	}
	
	public SpriteDTO() {
		super();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}
	
	
}
