package keynotes.vidmot;

import javafx.scene.control.Button;

class ButtonHelper {
    public final int nrOfNoteButtons = 40;
    public final Button[] buttons = new Button[nrOfNoteButtons];

    public ButtonHelper(Button button){
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button();
        }
    }
}
