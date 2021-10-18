

public class Packet {
	private String character;
	private int index;
	
	public Packet(String character, int index) {
		this.character = character;
		this.index = index;
	}
	
	public String getCharacter() {
		return character;
	}
	
	public int getIndex() {
		return index;
	}
}
