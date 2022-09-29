package be.makercafe.apps.gamebench.model;

public class NpcDTO {
	int[] texture = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	public double x;
	public double y;
	int type;
	
	public NpcDTO(int[] texture, double x, double y, int type) {
		super();
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public NpcDTO() {
		super();
	}
	
	public int[] getTexture() {
		return texture;
	}
	public void setTexture(int[] texture) {
		this.texture = texture;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
