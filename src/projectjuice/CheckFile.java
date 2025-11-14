package projectjuice;

import java.io.File;
public class CheckFile {
    public static void main(String[] args) {
        String path = "C:\\Users\\home\\Desktop\\cjtts\\programs\\eclipseprograms\\juicebar\\assets\\juicebacjg.jpg";
        File f = new File(path);
        System.out.println("Exists: " + f.exists());
        System.out.println("Length: " + f.length());
    }
}
