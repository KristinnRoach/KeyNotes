package Geymsla.unusedClasses;

import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

class NoteHelper {

    public static Button button;
    public static KeyCode keyCode;
    public static String noteName;
    public static int keyIndex;
    public static int scaleDegree;
    public boolean isMajor = true;

    public NoteHelper(String noteName, int keyIndex){
        NoteHelper.noteName = noteName;
        this.keyIndex = keyIndex;
    }
}
