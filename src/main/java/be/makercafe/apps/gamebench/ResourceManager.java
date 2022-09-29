package be.makercafe.apps.gamebench;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;

import be.makercafe.apps.gamebench.model.GameCartridge;


public class ResourceManager {
	private final static ObjectMapper objectMapper = new ObjectMapper();

	public static GameCartridge loadCartridge(String fileName) {
		GameCartridge cartridge = null;
		try {
			cartridge = objectMapper.readValue(new File(fileName), GameCartridge.class);  
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return cartridge;
	}
	
	public static boolean saveCartridge(String fileName, GameCartridge cartridge) {
		boolean result = false;
		try {
			objectMapper.writeValue(new File(fileName), cartridge);
			result = true;
		} catch(Exception ex){
			ex.printStackTrace();
		}
		return result;
	}
	

}
