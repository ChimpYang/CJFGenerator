package org.cjf.util.generator.test;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Test;

public class TestSaveFile {

	@Test
	public void testSave() {
		String content = "hello,\nWorld.\n中文描述未尝不可。";
		saveFile(content, "/Users/Chimp/Temp/abc.txt");
	}

	private static void saveFile(String content, String path) {
		FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(content.getBytes("UTF-8"));
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            	fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
}
