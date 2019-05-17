package be.intecbrussel.FileIOGroepswerk;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: Ik denk dat het beter zou zijn geweest om een util klasse te maken
// en je main beter in een andere klasse.
// Dit zou ervoor zorgen dat je deze code gemakkelijk voor andere zaken kan gebruiken dan alleen deze opdracht.

//TODO: Ik heb een .gitignore gemaakt met daarin de nodige excludes voor user/project specific files.
// Deze zal de getrackte files niet hiden. Maar dan hebben jullie een template voor het volgende project.

public class App {
    public static void main(String[] args) {
        try {
            File unsorted = new File("C:/data/unsorted");
            File sorted = new File("C:/data/sorted");
            List<File> files = new ArrayList<>();

            toList(unsorted, files);
            //TODO: Ik heb hier het type gedefiniëerd van je Set,
            // probeer hierop te letten kan anders voor problemen zorgen.
            Set<String> extentionSet = hashSetFiles(files);
            createFoldersByExtention(extentionSet, sorted, files);


            writeSummary(files, sorted);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }

    //TODO: Probeer de methode in kleinere stukken te verdelen,
    // deze heeft een te hoge complexiteit wat onderhoud ervan moeilijker zal maken.
    private static void writeSummary(List<File> files, File sorted) throws IOException {
        try {

            List<File> sortedList = new ArrayList<>();
            toList(sorted, sortedList);
            Set<String> extentions = new HashSet<>();

            for (File file : files) {
                extentions.add(getFileExtention(file));
                //TODO: Deze String word op 4 plaatsen gebruikt probeer hardcoded String literals te vermijden.
                // Maak er een variable van die je waar nodig kan gebruiken.

                extentions.add("hidden");  //TODO: Je kan deze beter naar de forloop zetten.


            }

            //TODO: Probeer erop te letten wanneer je code in je repo stored, dat er geen dead code meer aanwezig is.
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

            //TODO: s zou beter extention noemen. zorgt voor iets meer duidelijkheid in je code.
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

            //TODO: Path summaryPath = Paths.get(sorted + "/summary.txt")
            // kan refactoring en onderhoud makkelijker maken.
            if (!Paths.get(sorted + "/summary.txt").toFile().exists()) {
                Files.createFile(Paths.get(sorted + "/summary.txt"));
            }
            Files.write(Paths.get(sorted + "/summary.txt"), summary);


        } catch (IOException e) {
            System.out.println("Something went wrong in making summary " + e.getMessage());
        }
    }


    //aanmaken van de nodige mappen op basis van de extentions hashset, copy de files naar de juist map
    //TODO: Je gaat hier in je methode de IOException opvangen,
    // dus er is geen nood meer om throws IOException in je methode signatuur te vermelden.
    private static void createFoldersByExtention(Set<String> extentionSet, File sorted, List<File> files) throws IOException {

        //TODO: Ik zou van directory een Path object gemaakt hebben,
        // Hierdoor kan je in onderstaand code onder andere gebruik maken van de resolve methode.
        // Er wordt voorrang gegeven aan de klasse Path bij het gebruik in nieuwe Java code.
        // Indien nodig kan je het Path object omzetten naar een File object.
        String directory = sorted.getAbsolutePath();

        //TODO: Indien je directory een Path object was:
//        Path directory = sorted.toPath();
        for (String s : extentionSet) {
            //TODO: Heeft hetzelfde effect als onderstaande if: Files.createDirectories(directory);
            if (!new File(directory + "/" + s).exists()) {
                new File(directory + "/" + s).mkdir();
            }

        }
        try {

            for (File file : files) {

                //TODO: Hier kon:
//                Path destination = directory.resolve(getFileExtention(file)).resolve(file.getName());
                Path destination = Paths.get(directory + "/" + getFileExtention(file) + "/" + file.getName());
                if (!file.isHidden()) {
                    //TODO: !destination.toFile().exists() kon in onderstaande if statement toegevoegd worden.
                    if (getFileExtention(file) != null) {
                        if (!destination.toFile().exists()) {
                            Files.copy(Paths.get(file.getAbsolutePath()), destination);
                        }

                    }
                } else {
                    //TODO: ook een mogelijkheid !Files.exists(destination)
                    if (!destination.toFile().exists()) {
                        new File(directory + "/hidden").mkdir();
                        Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(directory + "/" + "hidden" + "/" + file.getName()));
                    } else {
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
    //TODO: Slim gebruik van een HashSet
    private static Set<String> hashSetFiles(List<File> files) {
        Set<String> hashset = new HashSet<>();
        for (File f : files) {
            //TODO: een nullcheck op deze plaats kan nooit kwaad.
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
                //TODO: In dit geval kan je beter de lastIndexOf(char c) implementatie gebruiken voor sneller resultaat.
                extention = name.substring(name.lastIndexOf(".") + 1);
            }
        } catch (Exception e) {
            System.out.println("Whoops something went wrong with extention toString" + e.getMessage());
        }
        return extention;
    }

    //files is een list weergeven
    //TODO: Mooi gebruik recursive method.
    private static void toList(File unsorted, List<File> files) {
        if (!unsorted.isDirectory()) {
            //TODO: Probeer hier specifieker te zijn naar je feedback.
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


