package keynotes.vinnsla;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

class Status {

    static boolean isLoopOn;
    static boolean isLoopLocked;
    static boolean isSustainOn;
    static boolean isDelayOn;


                        // PROPERTIES

  /*  public static BooleanProperty getIsLoopOn() {
        return isLoopOnProperty;
    }

    static final BooleanProperty isLoopOnProperty = new SimpleBooleanProperty(isLoopOn);
*/
    public static BooleanProperty getLoopReleased() {
        return loopReleased;
    }

    static final BooleanProperty loopReleased = new SimpleBooleanProperty();


    // HANDLER METHODS


    static void handleCaps(boolean b) {
        isLoopLocked = b;
        isLoopOn = b;
        if (!b) {
            Playback.releaseCurrentlyLooping();
        }
        System.out.println("CapsOn : Loop Locked" + b);
    }

     static void handleSpace(boolean b) {
        if (!isLoopLocked) {
            isLoopOn = b;
            System.out.println("SpaceOn : Loop On" + b);
        } else if (b) {
            Playback.releaseCurrentlyLooping();
        }
    }

    static void handleShift(boolean b) { // bara prófa að nota shift í þetta fyrst það var laust
        isDelayOn = b;
    }
    static void handleTab(boolean b) {
        isSustainOn = b;
        System.out.println("sustain: " + b);
    }
    static void handleBackQuote(boolean b) { // hvað er náttúrulegast að nota í? ekki bæta við einhverju sem flækir fyrir notendanum

    }
}

/*
    static void listenForLoopReleases(AtomicBoolean loopReleased) {
        isLoopOnProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                loopReleased.set(true);
            }
        });
        isSustainOnProperty.addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                loopReleased.set(true);
            }
        });
    } */

