package be.intecbrussel.FileIOGroepswerk;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class App {
    public static void main(String[] args) {
        try {
            File unsorted = new File("C:/data/unsorted_folder");
            File sorted = new File("C:/data/sorted_folder");
            List<File> files = new ArrayList<>();

            toList(unsorted, files);
            Set extentionSet = hashSetFiles(files);
            createFoldersByExtention(extentionSet, sorted, files);


            writeSummary(files, sorted);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    private static void writeSummary(List<File> files, File sorted) throws IOException {
        try {

            List<File> sortedList = new ArrayList<>();
            toList(sorted, sortedList);
            Set<String> extentions = new HashSet<>();

            for (File file : files) {
                extentions.add(getFileExtention(file));
                extentions.add("hidden");


            }
            /*bf.write("        name    |       readable    |       writeable    |");
            bf.write("\n");
            for (String ext : extentions) {
                bf.write(ext + ": ");
                bf.write("\n");
                bf.write("---");
                bf.write("\n");
                for (File s : sortedList) {
                    if (ext.equals(s.getParentFile().getName())) {
                        bf.write(String.format("%-52s%5s%10s%5s%10s%5s%10s%5s", s.getName(), "|",
                                s.canRead() ? "x" : "/", "|", s.canWrite() ? "x" : "/", "|",
                                s.isHidden() ? "x" : "/", "|"));
                    }
                }
            }*/


            List<String> summary = new ArrayList<>();
            summary.add(String.format("%-52s%5s%10s%5s%10s%5s%10s%5s", "name", "|", "readable", "|", "writeable",
                    "|", "hidden", "|"));

            for (String s : extentions) {
                summary.add("\n");
                summary.add(s + ":");
                summary.add("------");
                for (File file : sortedList) {
                    if (s.equals(file.getParentFile().getName())) {
                        summary.add(String.format("%-52s%5s%10s%5s%10s%5s%10s%5s", file.getName(), "|",
                                file.canRead() ? "x" : "/", "|", file.canWrite() ? "x" : "/", "|",
                                file.isHidden() ? "x" : "/", "|"));
                    }
                }
            }

            if (!Paths.get(sorted + "/summary.txt").toFile().exists()) {
                Files.createFile(Paths.get(sorted + "/summary.txt"));
            }
            Files.write(Paths.get(sorted + "/summary.txt"), summary);


        } catch (IOException e) {
            System.out.println("Something went wrong in making summary " + e.getMessage());
        }
    }


    //aanmaken van de nodige mappen op basis van de extentions hashset, copy de files naar de juist map
    private static void createFoldersByExtention(Set<String> extentionSet, File sorted, List<File> files) throws IOException {
        String directory = sorted.getAbsolutePath();

        for (String s : extentionSet) {
            if (!new File(directory + "/" + s).exists()) {
                new File(directory + "/" + s).mkdir();
            }

        }
        try {

            for (File file : files) {
                Path destination = Paths.get(directory + "/" + getFileExtention(file) + "/" + file.getName());
                if (!file.isHidden()) {
                    if (getFileExtention(file) != null) {
                        if (!destination.toFile().exists()) {
                            Files.copy(Paths.get(file.getAbsolutePath()), destination);
                        }

                    }
                } else {

                    if (!destination.toFile().exists()) {
                        new File(directory + "/hidden").mkdir();
                        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(directory + "/" + "hidden" + "/" + file.getName()));
                    }else{
                        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(directory + "/" + "hidden" + "/" + file.getName()));
                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Whoops something went wrong with copying the files, " + e.getCause());
            e.printStackTrace();
        }

    }

    //alle soorten extentions in een hashset steken zodat er geen mappen dubbel kunnen worden aangemaakt op basis van de extention
    private static Set hashSetFiles(List<File> files) {
        Set<String> hashset = new HashSet<>();
        for (File f : files) {
            hashset.add(getFileExtention(f));
        }
        return hashset;
    }

    //filter extention uit het object en returned de string waarde terug
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

    }

}


