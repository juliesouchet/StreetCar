package main.java.gui.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

public class Resources {

	// Strings

	private static Hashtable<String, Hashtable<String, String>> stringsTable = null;

	private static String localizationFilePath() {
		UserDefaults ud = UserDefaults.getSharedInstance();
		String language = ud.getString(Constants.LANGUAGE_KEY);
		return Constants.LOCALIZATION_FOLDER_PATH + language + ".txt";
	}

	private static void parseLocalizedString(BufferedReader br) throws IOException {
		String comment = br.readLine();
		if (comment != null && comment.startsWith("// ")) {
			comment = comment.substring(3);
		} else {
			throw new IOException();
		}

		String string = br.readLine();
		if (string == null || string.isEmpty()) {
			throw new IOException();
		}

		String localizedString = br.readLine();
		if (localizedString == null || localizedString.isEmpty()) {
			throw new IOException();
		}

		Hashtable<String, String> commentedStrings = Resources.stringsTable.get(string);
		if (commentedStrings == null) {
			commentedStrings = new Hashtable<String, String>();
		}
		commentedStrings.put(comment, localizedString);
		Resources.stringsTable.put(string, commentedStrings);
	}

	private static void loadLocalizationFile() {
		Resources.stringsTable = new Hashtable<String, Hashtable<String, String>>();

		String filePath = Resources.localizationFilePath();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: localization file not found");
			System.out.println("Localization file ==> " + filePath);
			return;
		}

		int lineCount = 0;
		try {
			String line = "";
			while (line != null && line.isEmpty()) {
				Resources.parseLocalizedString(br);
				line = br.readLine();
				lineCount++;
			}
		} catch (IOException e) {
			System.out.println("ERROR: localization file is corrupted at line " + lineCount);
			System.out.println("Localization file ==> " + filePath);
			return;
		}
	}

	public static String localizedString(String string, String comment) {
		if (string == null) {
			return null;
		}
		if (Resources.stringsTable == null) {
			Resources.loadLocalizationFile();
		}
		
		comment = (comment == null) ? "no comment" : comment;
		Hashtable<String, String> strings = Resources.stringsTable.get(string);
		if (strings != null && strings.containsKey(comment)) {
			return strings.get(comment);
		} else {
			return string;
		}
	}

	// Images
	
	public static BufferedImage imageNamed(String imageName) {
		String[] extensions = { ".png", ".jpg", "tiff", "gif", ""};
		for (String fileExtension : extensions) {
			String filename = imageName + fileExtension;
			BufferedImage image = Resources.loadImageWithFilename(filename);
			if (image != null) {
				return image;
			}
		}
		return null;
	}

	public static BufferedImage localizedImageNamed(String imageName) {
		UserDefaults ud = UserDefaults.getSharedInstance();
		String userLanguage = ud.getString(Constants.LANGUAGE_KEY);
		BufferedImage image = Resources.imageNamed(imageName + "_" + userLanguage);
		return (image != null) ? image : Resources.imageNamed(imageName);
	}

	public static BufferedImage loadImageWithFilename(String imageFileName) {
		String imagePath = Constants.IMAGES_FOLDER_PATH + imageFileName;
		try {
			return ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			return null;
		}
	}
}
