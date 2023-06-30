package keynotes.vidmot;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import keynotes.vinnsla.Playback;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import keynotes.vinnsla.PlayerTimeline;
import keynotes.vinnsla.ToolKeys;

import java.net.URL;
import java.util.*;

/**
 The KeysUI class is the controller class for the graphical user interface for the keyboard keys and its functionalities.
 It implements the Initializable interface.
 */
public class Controller implements Initializable {

    //private final ToolKeys toolKeys = new ToolKeys();
    @FXML
    private Button fxTransUp, fxTransDown, fxTransReset;
    @FXML
    private ToggleButton fxShowNotes, fxMinorMajor, fxLoopLock, fxDelay, fxSustain, fxMetronome;

    // private ImageView fxMetronome;

    @FXML
    private Label fxRootNote, fxTempo;
    @FXML
    private Button fxZ, fxX, fxC, fxV, fxB, fxN, fxM, fxComma, fxDot, fxÞ, fxA, fxS, fxD, fxF, fxG, fxH, fxJ, fxK, fxL, fxÆ, fxQ, fxW, fxE, fxR, fxT, fxY, fxU, fxI, fxO, fxP, fx1, fx2, fx3, fx4, fx5, fx6, fx7, fx8, fx9, fx0;
    public static Button[] getButtons() {
        return buttons;
    }
    private static Button[] buttons;
    private final HashMap<KeyCode, Button> keycode_button_map = new HashMap<>();
    private final int[] keyIndicesMajor = { 0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 12, 14, 16, 17, 19, 21, 23, 24, 26, 28, 24, 26, 28, 29, 31, 33, 35, 36, 38, 40, 36, 38, 40,41, 43, 45, 47, 48, 50, 52 };
    private final Set<Integer> minor = new HashSet<>(Arrays.asList(4, 9, 11, 16, 21, 23, 28, 33, 35, 40, 45, 47, 52));
    private final HashMap<KeyCode, Integer> keycode_int_map = new HashMap<>();
   // private HashMap<Integer, String> int_noteNames_map = new HashMap<>();
    private final ObservableList<KeyCode> pressedNoteKeys = FXCollections.observableArrayList(); // Set<> frekar? // currently held down keyboard keys - henda?
    private String[] keyboardKeysIDs; // button ids
    @FXML
    private Button fxQuit, fxMinimize;
    @FXML
    private HBox topBar;
    @FXML
    private Slider fxVolSlide;
    @FXML
    private Slider fxLengthSlide;
    private final KeyCode[] noteKeyCodes = { KeyCode.Z, KeyCode.X, KeyCode.C, KeyCode.V, KeyCode.B, KeyCode.N, KeyCode.M, KeyCode.COMMA, KeyCode.PERIOD, KeyCode.SLASH, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.F, KeyCode.G, KeyCode.H, KeyCode.J, KeyCode.K, KeyCode.L, KeyCode.SEMICOLON, KeyCode.Q, KeyCode.W, KeyCode.E, KeyCode.R, KeyCode.T, KeyCode.Y, KeyCode.U, KeyCode.I, KeyCode.O, KeyCode.P, KeyCode.DIGIT1, KeyCode.DIGIT2, KeyCode.DIGIT3, KeyCode.DIGIT4, KeyCode.DIGIT5, KeyCode.DIGIT6, KeyCode.DIGIT7, KeyCode.DIGIT8, KeyCode.DIGIT9, KeyCode.DIGIT0 };
    private final Set<KeyCode> toolKeySet = Set.of(KeyCode.TAB, KeyCode.SHIFT, KeyCode.CAPS, KeyCode.SPACE, KeyCode.BACK_QUOTE);

    private final Media[] samples = Playback.getSamples(); // óþarfi að vera með samplepack sem klasa!
    public static IntegerProperty transpositionProperty() {
        return transposition;
    }

    public static BooleanProperty isMajorProperty() {
        return isMajor;
    }
    private static final IntegerProperty transposition = new SimpleIntegerProperty(12);
    public static int getTransposition() {
        return transposition.get();
    }
    private static final BooleanProperty isMajor = new SimpleBooleanProperty(true);

    /*
    private static IntegerProperty transposition = new SimpleIntegerProperty(0);
    public static IntegerProperty transpositionProperty() {
        return transposition;
    }
  /*  private static BooleanProperty isMajor = new SimpleBooleanProperty(true);
    public static BooleanProperty isMajorProperty() {
        return isMajor;
    } */

    /* public int getTransposition() {
        return transposition;
    }
    public boolean isMajor() {
        return isMajor;
    } */


    /**
     Initializes the GUI components and calls other necessary set-up methods.
     @param url URL location of the FXML file used to build the interface
     @param resourceBundle ResourceBundle used to localize the GUI components
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //fxZ.getScene();
        initializeButtons();
        initializeMaps();
        setStyleClasses();
        addKeyListeners();
        addNoteNameListener();

        TxtMethods.initialize();
        Playback.initialize();

        setUpVolumeSlider(fxVolSlide);
        setUpLengthSlider(fxLengthSlide);
        //createNoteKeySet();
        //playback.createMetronome();
        setUpFocus();
    }

    private void addNoteNameListener() { // má vera í TxtMethods?
        transposition.addListener((observableValue, oldValue, newValue) -> {
            TxtMethods.setNoteNames();
        });
        isMajor.addListener((observableValue, oldValue, newValue) -> {
            TxtMethods.setNoteNames();
        });
    }



  /*  private static final Set<NoteKeys> NOTE_KEYS_SET = new HashSet<>();

    private void createNoteKeySet(){
        for (NoteKeys noteKeys : NOTE_KEYS_SET) {
            noteKeys.setNoteNumber();
        }
    } */

    private void addKeyListeners() { // ætli þetta séggí lajji? ekki vesen með að vera með listeners á mörgum stöðum?
        /*
        ToolKeys.isCapsOn.addListener((observableValue, oldValue, newValue) -> {
            fxLoopLock.setSelected(newValue); // betra að nota styleclass svo verði ekki ruglingur? var svona: fxLoopLock.setSelected(!fxLoopLock.isSelected()); breytti í new value því toggle er nú þegar í toolkeys
        });
        ToolKeys.isBackQuoteOn.addListener((observableValue, oldValue, newValue) -> {
            // fxDelay.setSelected(newValue); // kannski betra að nota bara style class svo verði ekki ruglingur?
        });
        ToolKeys.isTabOn.addListener((observableValue, oldValue, newValue) -> {
            // fxMetronome.setSelected(newValue);
            fxSustain.setSelected(newValue);
        });
        */
    }

    /**
     * Sets up the focus traversable for certain GUI components.
     */
    private void setUpFocus() {
        fxShowNotes.setFocusTraversable(false);
        fxTransUp.setFocusTraversable(false);
        fxTransDown.setFocusTraversable(false);
        fxTransDown.setFocusTraversable(false);
        fxTransReset.setFocusTraversable(false);
        fxLoopLock.setFocusTraversable(false);
        fxDelay.setFocusTraversable(false);
        fxQuit.setFocusTraversable(false);
        fxMinimize.setFocusTraversable(false);
        fxMetronome.setFocusTraversable(false);
        fxVolSlide.setFocusTraversable(false);
        fxLengthSlide.setFocusTraversable(false);
        fxSustain.setFocusTraversable(false);
    }

    /**
     Initializes the Button[] buttons array and HashMap<KeyCode, Button> buttonMap used to map the KeyCodes to the buttons.
     */
    private void initializeButtons(){
        buttons = new Button[]{fxZ, fxX, fxC, fxV, fxB, fxN, fxM, fxComma, fxDot, fxÞ, fxA, fxS, fxD, fxF, fxG, fxH, fxJ, fxK, fxL, fxÆ, fxQ, fxW, fxE, fxR, fxT, fxY, fxU, fxI, fxO, fxP, fx1, fx2, fx3, fx4, fx5, fx6, fx7, fx8, fx9, fx0};
        int cnt = 0;
        keyboardKeysIDs = new String[buttons.length];
        for(Button b : buttons){
            keyboardKeysIDs[cnt] = b.getText();
            b.setId(keyboardKeysIDs[cnt]);
            cnt++;
        }
    }

    private void initializeMaps() {
        for(int i = 0; i < buttons.length; i++) {
            keycode_button_map.put(noteKeyCodes[i], buttons[i]);
            keycode_int_map.put(noteKeyCodes[i], keyIndicesMajor[i]);
            // keycode_media_map.put(noteKeyCodes[i], media[i]);
        }
    }


    private void setStyleClasses() {
        for (int i = 0; i < 10; i++) {
            buttons[i].getStyleClass().add("oct1");
        }
        for (int i = 10; i < 20; i++) {
            buttons[i].getStyleClass().add("oct2");
        }
        for (int i = 20; i < 30; i++) {
            buttons[i].getStyleClass().add("oct3");
        }
        for (int i = 30; i < 40; i++) {
            buttons[i].getStyleClass().add("oct4");
        }
    }
    /**
     * Called when a key is pressed in the GUI. Determines the corresponding index of the media to be played
     * and plays the corresponding note. Updates the GUI to indicate that the key has been pressed.
     * The keyboard rolloff on many common keyboards is limited to six keys pressed at a time so the
     * pressedKeys size is limited to six.
     *
     * @param event KeyEvent corresponding to the key that was pressed
     */
    @FXML
    protected void onKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        // System.out.println("Keycode: " + code);

        if (keycode_button_map.containsKey(keyCode) && !pressedNoteKeys.contains(keyCode)) {

            int keyIndex = keycode_int_map.get(keyCode) + transposition.get(); // trans byrja í 12?

            if (!isMajor.get() && (minor.contains(keyIndex - transposition.get()))) {
                keyIndex -= 1;
            }

            Playback.playMedia(keyIndex, pressedNoteKeys.size()); // þarf ekki pressed note keys út af rolloff, nema noti í öðru skyni

            Button button = keycode_button_map.get(keyCode);
            button.getStyleClass().add("buttonPressed");

            pressedNoteKeys.add(keyCode);
            event.consume();
        }
        else if (toolKeySet.contains(keyCode)) {
            ToolKeys.handleToolKeyPressed(keyCode);
            event.consume();
        } else {
            event.consume(); // no default key press functions needed?
        }
    }


    @FXML
    protected void onKeyReleased(KeyEvent event) {
        KeyCode keyCode = event.getCode();

        if (pressedNoteKeys.contains(keyCode)) {

            Button button = keycode_button_map.get(keyCode);
            button.getStyleClass().remove("buttonPressed");
            pressedNoteKeys.remove(keyCode);

            Playback.noteKeyReleased(keycode_int_map.get(keyCode));

            event.consume();
        }
        else if (toolKeySet.contains(keyCode)) {
            ToolKeys.handleToolKeyReleased(keyCode);
            event.consume();
        } else {
            event.consume(); // no default key press functions needed?
        }
    }

    @FXML
    private void mousePressedSample(MouseEvent e) {
/*
        Button button = (Button) e.getSource();
        int keyIndex = -1;
        if (button != null) {
            keyIndex = keycode_int_map.get(KeyCode.valueOf(button.getId())) + transposition;
        }
        if (keyIndex != -1) {
            if (pressedKeys.contains(KeyCode.valueOf(button.getId()))) { return; }
            if (!isMajor) {
                if (minor.contains(keyIndex - transposition)) {
                    keyIndex -= 1;
                }
            }
            Playback.playMedia(keyIndex, pressedKeys.size());
        } */
    }
    @FXML
    private void fxLoopLockHandler(ActionEvent event) {
        // playback.setLoopLocked(fxLoopLock.isSelected()); // virkar ekki, finna þægilegt system til að vinna með mouseclick vs keypress og listeners
    }
    @FXML
    private void fxDelayHandler(ActionEvent event) {
       // Status.setDelayOn(fxDelay.isSelected());
    }
    @FXML
    private void onFxMetronomeMouseClick(MouseEvent e) {
        if (fxMetronome.isSelected()) {
         //   playback.stopMetronome();
        } else {
       // playback.playMetronome();
        }
}
    /*
    @FXML
    private void onFxMetronomeReleased(KeyEvent e) {
        if (e.getSource().equals(fxMetronome)) {
            if (fxMetronome.isPressed()) {
                playback.stopMetronome();
                // fxMetronome.getStyleClass().remove("buttonPressed");
            } else {
                playback.playMetronome();
                // fxMetronome.getStyleClass().add("buttonPressed");
            }
        }
    } */


    @FXML
    private void transpose(ActionEvent e) {
       // if (e.getSource() instanceof KeyEvent) {}

        if (e.getSource().equals(fxTransUp)) {
            transposition.set((transposition.get() + 1) % 12);

        } else if (e.getSource().equals(fxTransDown)) {

            transposition.set((transposition.get() - 1) % (-12));
        } else if (e.getSource().equals(fxTransReset)) {
            transposition.set(12);
            if (!isMajor.get()) { minorMajorButton(e); }
        }
        fxRootNote.setText("Root Note: " + TxtMethods.getRootNote());
    }

    @FXML
    private void minorMajorButton(ActionEvent e) {
        if (isMajor.get()) {
            fxMinorMajor.setText("Switch to Major");
            isMajor.set(false);
        } else {
            fxMinorMajor.setText("Switch to Minor");
            isMajor.set(true);
        }
        fxRootNote.setText("Root Note: " + TxtMethods.getRootNote());
    }


    @FXML
    private void noteNamesHandler(ActionEvent e) { // óþarfi
        if (!fxShowNotes.isSelected()) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setText(keyboardKeysIDs[i]);
            }
            TxtMethods.setShowNotes(false);
            fxShowNotes.setText("Show Notes");

        } else {
            TxtMethods.setShowNotes(true);
            TxtMethods.setNoteNames();
            fxShowNotes.setText("Show Keyboard");
        }
    }

    /**
    Sets up a Volume slider to control the main volume of all media-players
     */
    private void setUpVolumeSlider(Slider fxVolSlide) {
        fxVolSlide = new Slider();
        fxVolSlide.setValue(80.0);
        Playback.setMasterVolume(0.8);

        fxVolSlide.valueProperty().addListener((observable, oldValue, newValue) -> {
            double volume = newValue.doubleValue() / 100.0;
            Playback.setMasterVolume(volume);
        });
    }
/*
    private void setUpLengthSlider(Slider fxLengthSlide) {

        final int min = 100;
        final int max = 8000;
        final int init = 4000;

        fxLengthSlide.setMin(min);
        fxLengthSlide.setMax(max);
        fxLengthSlide.setValue(init);

        PlayerTimeline.setFadeOutLength(init);

        double minLog = Math.log(min);
        double maxLog = Math.log(max);

        // Set the major tick unit to the interval between a, b, c, and d
        fxLengthSlide.setMajorTickUnit(7900);

        // Set the minor tick count to the number of values between a, b, c, and d
        fxLengthSlide.setMinorTickCount(3);

        fxLengthSlide.setSnapToTicks(true);

        fxLengthSlide.valueProperty().addListener((observable, oldValue, newValue) -> {
            double logarithmicValue = Math.exp(minLog + (maxLog - minLog) * (newValue.doubleValue() - min) / (max - min));
            PlayerTimeline.setFadeOutLength(logarithmicValue);
        });
    }
*/

    private static final double MIN_TEMPO = 40.0;  // Minimum tempo (BPM)
    private static final double MAX_TEMPO = 240.0; // Maximum tempo (BPM)
    private static final double DEFAULT_TEMPO = 120.0; // Default tempo (BPM)

    private static int tempo = 120;

    public void setUpLengthSlider(Slider fxLengthSlide) {
        // Set slider properties
        fxLengthSlide.setMin(MIN_TEMPO);
        fxLengthSlide.setMax(MAX_TEMPO);
        fxLengthSlide.setValue(DEFAULT_TEMPO);

        // Add tick marks for common note lengths
        fxLengthSlide.setShowTickMarks(true);
        fxLengthSlide.setMajorTickUnit(tempo/8.0);
        fxLengthSlide.setMinorTickCount(3);
        fxLengthSlide.setSnapToTicks(true);

        // Listener for note length changes
        fxLengthSlide.valueProperty().addListener((observable, oldValue, newValue) -> {
            PlayerTimeline.setFadeOutLength(newValue.intValue());
        });
    }

                /*
                double selectedNoteLength = newValue.doubleValue();

                if (selectedNoteLength <= sixteenthNoteLength) {
                    PlayerTimeline.setFadeOutLength(sixteenthNoteLength);

                } else if (selectedNoteLength <= eightNoteTriplets) {
                    PlayerTimeline.setFadeOutLength(eightNoteTriplets);

                } else if (selectedNoteLength <= eighthNoteLength) {
                    PlayerTimeline.setFadeOutLength(eighthNoteLength);

                } else if (selectedNoteLength <= quarterNoteLength) {
                    PlayerTimeline.setFadeOutLength(quarterNoteLength);

                } else if (selectedNoteLength <= halfNoteLength) {
                    PlayerTimeline.setFadeOutLength(halfNoteLength);

                } else {
                    PlayerTimeline.setFadeOutLength(wholeNoteLength);

                }  /*
        });
    }

    private double roundToNoteLength(double val) {
        double a = 8000;
        double b = a / 2;
        double c = a / 3;
        double d = a / 4;

        if (val <= d) {
            return d;
        } else if (val <= c) {
            return c;
        } else if (val <= b) {
            return b;
        } else {
            return a;
        }
    }

    public void onFxSustainMouseClick(MouseEvent mouseEvent) {

    }

    /**
     * Allows the window to be dragged by clicking and holding the top bar
     * @param event event from the top bar
     */
    @FXML
    private void dragWindow(MouseEvent event) {
        Stage stage = (Stage) topBar.getScene().getWindow();
        double offsetX = event.getSceneX();
        double offsetY = event.getSceneY();

        topBar.setOnMouseDragged(dragEvent -> {
            stage.setX(dragEvent.getScreenX() - offsetX);
            stage.setY(dragEvent.getScreenY() - offsetY);
        });
    }
    /**
     * Quits the application when x in the top left corner is clicked
     */
    @FXML
    private void quitApp() {
        Platform.exit();
    }
    /**
     * Minimizes the application when - in the top left corner is clicked
     */
    @FXML
    private void minimizeClick() {
        Stage stage = (Stage) fxMinimize.getScene().getWindow();
        stage.setIconified(true);
    }

    public void fxTempoMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            fxTempo.setText(DEFAULT_TEMPO + " bpm");
            tempo = (int) DEFAULT_TEMPO;
            PlayerTimeline.setTempo((int) DEFAULT_TEMPO);
            fxLengthSlide.setMajorTickUnit(tempo / 8.0);
        }
    }

    public void fxTempoMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            double deltaY = mouseEvent.getY();
            double tempoChange = -deltaY * 0.5;
            double newTempo = Math.max(MIN_TEMPO, Math.min(DEFAULT_TEMPO + tempoChange, MAX_TEMPO));
            fxTempo.setText((int) newTempo + " bpm");
            tempo = (int) newTempo;
            PlayerTimeline.setTempo((int) newTempo);
            fxLengthSlide.setMajorTickUnit(tempo / 8.0);
        }
    }

    public void onFxSustainMouseClick(MouseEvent mouseEvent) {
    }
}
