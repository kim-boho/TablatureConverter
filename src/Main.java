
import Converter.Measures;
import Converter.NoteUtility;
import Converter.Validation;
import Exceptions.NotSupportedNoteException;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        boolean isValidInput = false;
        ArrayList<String> lines = new ArrayList<>();
        while(!isValidInput){
            Scanner sc = new Scanner(System.in);
            System.out.println("Put path of the file to convert: ");
            String path = sc.nextLine();
            lines = IOClass.textToStr(path);
            if(lines != null && !lines.isEmpty()){
                isValidInput = true;
            }
            else if(lines != null && lines.isEmpty()){
                System.out.println("The file is empty. Try again.\n");
            }
        }
        //Extract string information from text file.

        lines = Validation.validLines(lines);
        //Check if there is note form that is not supported.

        if(lines.size() !=  0){
            if(!Validation.isValidNotes(lines)){
                try{
                    throw new NotSupportedNoteException("This note is not supported");
                }
                catch (NotSupportedNoteException e){
                    e.printStackTrace();
                }
            }
        }
        //Extract only supported string information from text file

        String allScripts = "";

        int i = 0;
        while(i < lines.size()){
            String eachMeasure = "";
            int numOfGuitar = 6;
            int j =0;
            while(j < numOfGuitar){
                eachMeasure += lines.get(i)+"\n";
                i++;
                j++;
            }
            //Extract each 6 lines to make the whole score

            Measures converter = new Measures(eachMeasure);
            String noteScripts = "";
            for(String noteScript : converter.getScriptsPerMeasrue()){
                noteScripts += noteScript;
            }
            allScripts += noteScripts;
            //Extract note scripts from each measure and add all.
        }

        allScripts = NoteUtility.getMXML(allScripts);
        // Wrap note scripts with basic xml information.

        boolean isValid = false;
        //To check if the file's saved.

        Scanner sc = new Scanner(System.in);
        System.out.println("Converting's done, successfully.\n");
        while(!isValid){
            System.out.println("Put file name that you want to save: ");
            String fileName = sc.nextLine();

            System.out.println("Put path where you want to save file (without file name)" +
                    "\n(e.g : C:\\Users\\Desktop\\myFolder ) :");
            String filePath = sc.nextLine();

            String outputPathName = filePath + "\\" + fileName + ".musicxml";
            isValid = IOClass.isValid(outputPathName, allScripts);
        }

        System.out.println("Your musicXML file's been saved!");
    }
}
