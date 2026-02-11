import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class fileHandler {

    public String[] readFile(){
        //returns String aray of all files in the data directory by name
        String path = "./data";
        File directory = new File(path);

        String[] files = directory.list();

        return files;
    }

    public String readFile(String fileName){
        //returns the contents of the specified file using regular decrypting key
        String path = "./data/" + fileName;
        String contents = "";

        try{
            File file = new File(path);
            Scanner readIn = new Scanner(file);

            while(readIn.hasNextLine()){
                contents += readIn.nextLine();
            }

            readIn.close();
        }
        catch(FileNotFoundException e){
            System.out.println("No such file found");
            System.exit(1);
        }

        //deciphers the contents of the specified file using regular decrypting key
        Cipher cipher = new Cipher();
        contents = cipher.decrypt(contents);

        return contents;
    }

    public String readFile(String fileName, String key){
        //returns the contents of the specified files using unique decrypting key
        String path = "./data/" + fileName;
        String contents = "";

        try{
            File file = new File(path);
            Scanner readIn = new Scanner(file);

            while(readIn.hasNextLine()){
                contents += readIn.nextLine();
            }

            readIn.close();
        }
        catch(FileNotFoundException e){
            System.out.println("No such file found");
            System.exit(1);
        }

        //deciphers the contents of the specified file using unique decrypting key
        Cipher cipher = new Cipher(key);
        contents = cipher.decrypt(contents);

        //Chipher cipher = new Cipher();
        //contents = cipher.decrypt(contents, key);

        return contents;
    }
}
