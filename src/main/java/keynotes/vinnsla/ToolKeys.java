package keynotes.vinnsla;

import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;


public class ToolKeys {

    // CONSTRUCTOR


    // FIELDS

    private static boolean isSpaceOn = false;
    private static boolean isTabOn = false;
    private static boolean isBackQuoteOn = false;
    private static boolean isCapsOn = false;
    private static boolean isShiftOn = false;

    private static final Set<KeyCode> currentlyPressed = new HashSet<>();


    // KEY METHODS


    private static void toggleTab() {
        isTabOn = !isTabOn;
    }

    private static void toggleBackQuote() {
        isBackQuoteOn = !isBackQuoteOn;
    }

    private static void toggleCaps() {
        isCapsOn = !isCapsOn;
    }

    private static void toggleShift() {
        isShiftOn = !isShiftOn;
    }


    public static void handleToolKeyPressed(KeyCode keyCode) {
        if (!currentlyPressed.contains(keyCode)) {
            if (keyCode == KeyCode.SHIFT) {
                toggleShift();
                Status.handleShift(isShiftOn); // e.isShiftDown is more reliable if its being held down, if its a toggle key then use true/false ok
            } else if (keyCode == KeyCode.SPACE) {
                isSpaceOn = true;
                Status.handleSpace(isSpaceOn);
            } else if (keyCode == KeyCode.CAPS) {
                //Playback.handleCaps(Toolkit.getDefaultToolkit().getLockingKeyState(java.awt.event.KeyEvent.VK_CAPS_LOCK));
                toggleCaps();
                Status.handleCaps(isCapsOn);
            } else if (keyCode == KeyCode.TAB) {
                toggleTab();
                Status.handleTab(isTabOn);
            } else if (keyCode == KeyCode.BACK_QUOTE) {
                toggleBackQuote();
                Status.handleBackQuote(isBackQuoteOn);
            }
            currentlyPressed.add(keyCode);
        }
    }

    public static void handleToolKeyReleased(KeyCode keyCode) {
        if (currentlyPressed.contains(keyCode)) {

            if (keyCode == KeyCode.SPACE) {
                isSpaceOn = false;
                Status.handleSpace(false);

        /*} else if (keyCode == KeyCode.SHIFT) {
            handleShift(); */
            }
            currentlyPressed.remove(keyCode);
        }
    }
}













/*
    // Listen for KEY_PRESSED and KEY_RELEASED events
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
        if (event.getCode() == KeyCode.BACK_QUOTE) {
            isBackquotePressed.set(true);
        }
    });

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
        if (event.getCode() == KeyCode.BACK_QUOTE) {
            isBackquotePressed.set(false);
        }
    });

 */