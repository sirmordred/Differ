package app.mordred.diffgenerator.util;

import static app.mordred.diffgenerator.util.Constants.BYTE_BUFFER_SIZE_DETECT_BINARY;
import static org.apache.commons.lang3.StringUtils.CR;
import static org.apache.commons.lang3.StringUtils.LF;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class FileHelper {

	private FileHelper( ) {}
	
	private static final Map<String, Boolean> binaryFilesMap = new HashMap<>();
		
	// Imitates Unix diff's behavior in determining if a file is binary or text by checking the first few thousand bytes for zero values. See <a href="http://www.gnu.org/software/diffutils/manual/html_node/Binary.html">http://www.gnu.org/software/diffutils/manual/html_node/Binary.html</a>
	public static boolean isFileBinary(String filePath) throws IOException {
		if(binaryFilesMap.containsKey(filePath)) {
			return binaryFilesMap.get(filePath);
		} else {
			boolean isBinary = isFileBinaryCheck(filePath);
			binaryFilesMap.put(filePath, isBinary);
			return isBinary;
		}
	}
	
	private static boolean isFileBinaryCheck(String filePath) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(filePath));
		byte[] buffer = new byte[BYTE_BUFFER_SIZE_DETECT_BINARY];
		int bytesRead = IOUtils.read(fileInputStream, buffer, 0, BYTE_BUFFER_SIZE_DETECT_BINARY);
		fileInputStream.close();

		if(bytesRead == 0) {
			return false;
		}

		for(int i = 0; i < bytesRead; i++) {
			if(buffer[i] == 0) {
				return true;
			}
		}
		return false;
	}

	public static String normalizeLineSeparators(String s) {
		return s.replaceAll(CR + LF, LF);
	}

	public static boolean isFileSizeDifferenceTooBig(String inputLeftPath, String inputRightPath, long maxAllowedDifferenceInByte) {
        long fileSizeLeft = new File (inputLeftPath).length();
        long fileSizeRight = new File(inputRightPath).length();
		long difference = Math.abs(fileSizeLeft-fileSizeRight);

		return difference > maxAllowedDifferenceInByte;
	}
}
