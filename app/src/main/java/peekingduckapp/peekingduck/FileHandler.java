package peekingduckapp.peekingduck;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {
    private static final String FOLDER_NAME = "PeekingDuck";
    private static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/";

    public static String load_from_external_storage(String name) {
        String file_path = path + "/" + name;
        File file = new File(file_path);
        StringBuilder sb = new StringBuilder();

        if (file.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
                br.close();

                return sb.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        return sb.toString();
    }

    public static void save_file_to_external_storage(String name, String content) {
        File file = new File(path + "/" + name);
        File folder = file.getParentFile();
        if(!folder.isDirectory()) folder.mkdirs();
        if(file.exists()) file.delete();
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
