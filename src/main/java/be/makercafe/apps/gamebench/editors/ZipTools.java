package be.makercafe.apps.gamebench.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipTools {

	public static boolean zipFile(File fileToZip, String fileName) throws IOException {

		boolean result = false;
		File zipArchive = new File(fileName);
		FileOutputStream fos = new FileOutputStream(zipArchive);
		ZipOutputStream zipOut = new ZipOutputStream(fos);

		try {
			zipFile(fileToZip, fileToZip.getName(), zipOut);
			result = true;
		} finally {
			if (zipOut != null) {
				zipOut.close();
			}
			if (fos != null) {
				fos.close();
			}

		}
		return result;
	}

	public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}
		if (fileToZip.isDirectory()) {
			if (fileName.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(fileName));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(fileName + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, fileName + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(fileName);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}
}
