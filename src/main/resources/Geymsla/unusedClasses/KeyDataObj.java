package Geymsla.unusedClasses;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KeyDataObj {
    // implements Comparable<KeyData> ? pæling að prófa ef ég finn not fyrir - public int compareTo(KeyData keyData)
    // Comparator interface er hægt að nota til að geta flokkað eða borið saman á fleiri en einn hátt
    private int nrOfNotes = 65;
    private Button button; // the button corresponding to this key
    private KeyCode keyCode; // the keycode corresponding to this key
    private int noteNumber; // the MIDI note number corresponding to this key
    private String[] noteNames; // the name of the note corresponding to this key
    private Media media; // the audio corresponding to this key
    private int[] keyIndicesMajor = {0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 12, 14, 16, 17, 19, 21, 23, 24, 26, 28, 24, 26, 28, 29, 31, 33, 35, 36, 38, 40, 36, 38, 40, 41, 43, 45, 47, 48, 50, 52};
    private Set<Integer> minor = new HashSet<>(Arrays.asList(4, 9, 11, 16, 21, 23, 28, 33, 35, 40, 45, 47, 52));

    public KeyDataObj() {
        setNoteNames();
    }


    // Methods that require logic conditions
    public int getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNames() {

        String fileName = "noteNames.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            noteNames = new String[nrOfNotes];
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println("Line " + lineCount + ": " + line);
                noteNames[lineCount] = line;
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String[] getNoteNames(){
        return noteNames;
    }
}


