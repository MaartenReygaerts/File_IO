package be.intecbrussel.FileIOGroepswerk;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {

        File unsorted = new File("C:/data/unsorted_folder");
        File sorted = new File("C:/data/sorted_folder");
        List<File> files = new ArrayList<>();

        toList(unsorted, files);
        for (File fileList : files) {
            getFileExtention(fileList);
            Set extentionSet = hashSetFiles(files);
            createFoldersByExtention(extentionSet, sorted, files);


        }


    }

    private static void createFoldersByExtention(Set<String> extentionSet, File sorted, List<File> files) throws IOException {
        String directory = sorted.getAbsolutePath();

        for (String s : extentionSet) {
            if (!new File(directory + "/" + s).exists()) {
                new File(directory + "/" + s).mkdir();
            }
        }
        try {
            for (File file : files) {
                if (getFileExtention(file) != null) {
                    Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(directory + "/" + getFileExtention(file) + "/" + file.getName()));
                }
            }
        } catch (IOException e) {
            System.out.println("Whoops something went wrong with copying the files, " + e.getCause());
        }

    }


    private static Set hashSetFiles(List<File> files) {
        Set<String> hashset = new HashSet<>();
        for (File f : files) {
            hashset.add(getFileExtention(f));
        }
        return hashset;
    }


    private static String getFileExtention(File unsorted) {
        String extention = "";
        try {

            if (unsorted != null && unsorted.exists()) {
                String name = unsorted.getName();
                extention = name.substring(name.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            System.out.println("Whoops something went wrong with extention toString" + e.getMessage());
        }
        return extention;
    }

    //files is een list weergeven
    private static void toList(File unsorted, List<File> files) {
        if (!unsorted.isDirectory()) {
            System.out.println("No files found");

        } else {
            File[] folderArray = unsorted.listFiles();
            if (folderArray != null) {

                for (File file : folderArray) {
                    if (file.isFile()) {
                        files.add(file);


                    } else if (file.isDirectory()) {
                        toList(file.getAbsoluteFile(), files);
                    }
                }
            }
        }

    /*public static void makeDirectories(){
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\jpg").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\exe").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\zip").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\csv").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\gif").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\database").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\gitignore").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\json").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\hidden").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\pdf").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\png").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\py").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\summary").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\txt").mkdirs();
        new File("C:\\Users\\maart\\IdeaProjects\\File_IO\\src\\recourse_sorted\\wma").mkdirs();
    }

    public static void sortFiles(){*/

    }

}


