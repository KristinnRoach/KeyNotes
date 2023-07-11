package keynotes.vidmot;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Button;

public final class TxtMethods {
    private TxtMethods(){}
    public static void initialize() { // óþarfi?
        setScale();
    }
    private static final Button[] buttons = Controller.getButtons();

    public static void setShowNotes(boolean showNotes) {
        TxtMethods.showNotes = showNotes;
    }

    private static boolean showNotes = false;
    public static final String[] noteNamesSharp = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
    public static final String[] noteNamesFlat = {"C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"};
    public static final int[] keyIndicesMajor = { 0, 2, 4, 5, 7, 9, 11, 12, 14, 16};
    public static final int[] keyIndicesMinor = { 0, 2, 3, 5, 7, 8, 10, 12, 14, 15};
    public static String rootNote = "C";
    public static String getRootNote() { return rootNote; }
    private static int transposition = 0;
    public static void setTransposition(int transpo) {
        transposition = transpo;
    }
    private static final BooleanProperty isMajor = Controller.isMajorProperty();
    public static boolean isFlatKey = false;

    public static void setNoteNames() {
        setScale();
        int index = 0;
        String noteName = "";

        for (int i = 0; i < buttons.length; i++) {
            if (isMajor.get()) {
                index = keyIndicesMajor[i % 10];
            } else {
                index = keyIndicesMinor[i % 10];
            }
            index += (transposition % 12); // óþarfi?
            if (index < 0) { index += 12; }

            if (isFlatKey) {
                noteName = noteNamesFlat[index % 12];
            } else {
                noteName = noteNamesSharp[index % 12];
            }
            if (showNotes) {
                buttons[i].setText(noteName);
            }
        }
    }

    public static void setScale() {

        // get verið með takka í GUI til með b/# til að skipta ef fólk vill, líklega óþarfi

        rootNote = noteNamesSharp[(transposition % noteNamesSharp.length + noteNamesSharp.length) % noteNamesSharp.length];

        if (isMajor.get()) {
            switch (rootNote) {
                case "D#", "G#", "A#" -> rootNote = noteNamesFlat[(transposition % noteNamesFlat.length + noteNamesFlat.length) % noteNamesFlat.length];
            }
        } else {
            switch (rootNote) {
                case "D#", "A#" -> rootNote = noteNamesFlat[(transposition % noteNamesFlat.length + noteNamesFlat.length) % noteNamesFlat.length];
            }
        }
        setIsFlat();
    }


    public static void setIsFlat() { // setScale
        if (isMajor.get()) {
            switch (rootNote) {
                case "Eb", "F", "Ab", "Bb" -> isFlatKey = true;
                default -> isFlatKey = false;
            }
        } else {
            switch (rootNote) {
                case "C", "D", "Eb", "F", "G", "Bb" -> isFlatKey = true;
                default -> isFlatKey = false;
            }
        }
    }
}


/*    public static void setIsFlat() { // setScale
        int index = transposition.get();
        if (index < 0) {
            index += 12;
        }
        if (isMajor.get()) {
            switch (index) {
                case 3, 5, 8, 10 -> isFlatKey = true;
                default -> isFlatKey = false;
            }
        } else {
            switch (index) {
                case 0, 2, 5, 7 -> isFlatKey = true;
                default -> isFlatKey = false;
            }
        }
    }

 */


    /*
    public static void resetNoteNames() {
        setRootNote();
        setIsFlat();
        setNoteNames();
    }
*/

//public static final String[] noteNames = {"C", "D", "E", "F", "G", "A", "B"};


/*            noteNameindex = keyIndicesMajor[i % 10];
            // noteNameindex = i % 10;  // because there are 10 notes in each row
            noteNameindex += transposition.get();
            //if (noteNameindex < 0) { noteNameindex += 8; }
            //noteNameindex = noteNameindex % 7;
            // System.out.println(buttons[i].getId() + "  " + noteNames[noteNameindex]);

            //String thisNoteName = noteNames[noteNameindex];

 */


            /*
            noteNameindex = keyIndicesMajor[i];
            if (!isMajor.get() && minor.contains(index - transposition.get())) {
                noteNameindex -= 1;
            }
            */


/*


            index = keyIndicesMajor[i % 10];
            // index = i % 10;  // because there are 10 notes in each row
            index += transposition.get();
            //if (index < 0) { index += 8; }
            //index = index % 7;
            // System.out.println(buttons[i].getId() + "  " + noteNames[index]);

            //String thisNoteName = noteNames[index];
            if (isFlatKey && isMajor.get()) {
                switch (transposition.get()) {

                }
            }

            if (isFlatKey) {
                buttons[i].setText(noteNamesFlat[index % 12]);
            } else {
                buttons[i].setText(noteNamesSharp[index % 12]);
            }
        }
 */