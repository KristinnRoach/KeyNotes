package keynotes.vinnsla;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;


public class PlayerTimeline {

    public static Set<PlayerTimeline> playingTimelines = new HashSet<>();

    // Static Fields
    private static int currentSliderValue;
    public static void setCurrentSliderValue(int value) {
        currentSliderValue = value;
    }
    private static int tempo;
    public static void setTempo(int tmpo) {
        tempo = tmpo;
    }
    public static double getFadeOutLength() {
        return fadeOutLength;
    }

    public static void setFadeOutLength() { // slidervalue ranges from 1 - 8

        double quarterNoteDuration = 60000.0 / tempo;

        fadeOutLength = (quarterNoteDuration / 2) * currentSliderValue;

        System.out.println("bpm: " + tempo + " length in millisec: " + fadeOutLength + " slider value " + currentSliderValue);
    }

    private static double fadeOutLength;
    private static int grooveSpacy = 3; // GUI button?
    private static boolean syncNspace;
    public static void setSyncNspace(boolean isIt){
        syncNspace = isIt;
        if(syncNspace) {
            for (PlayerTimeline pt : playingTimelines) {
                turnOnSyncNSpace(pt);
            }
        }
    }

    private static void turnOnSyncNSpace(PlayerTimeline pt) {
        pt.timeline.setCycleCount(grooveSpacy);
        // væri hægt að í staðinn f að grooveSpacy séu bara tölur að sé líka mismunandi vibe ef það er stemmari en gæti verið kanínuhola !!
    }

    // non-static Fields

    public SamplePlayer player;
    public Timeline timeline;
    private boolean loopOnStartOfFade;
    private final double initialLoopLength;
    private boolean loopReleased = false;

    // Constructor
    public PlayerTimeline(SamplePlayer player) {
        this.player = player;
        this.timeline = new Timeline();
        this.initialLoopLength = fadeOutLength;
        //addFadeKeyFrames();
    }

    // Methods

    synchronized void releaseLoop() {
        this.loopReleased = true;
    }

     synchronized void addFadeKeyFrames() { // synchronized?
        double initialVolume = player.getVolume();
        int numSteps = 80; // Number of steps for the fade-out
        double scaleFactor = Math.pow(0.001 / initialVolume, 1.5 / numSteps); // Exponential scale factor

        for (int i = 0; i < numSteps; i++) {
            if (player.getVolume() <= 0.00001) {
                break;
            }  // ensure that volume does not become negative
            double volume = initialVolume * Math.pow(scaleFactor, i);
            double stepDuration = fadeOutLength / numSteps; // duration for each step
            Duration time = Duration.millis(stepDuration * i);
            KeyFrame keyFrame = new KeyFrame(time, event -> {
                player.setVolume(volume);
            });
            timeline.getKeyFrames().add(keyFrame);
        }
    }

    synchronized void startFadeOut() {
        addFadeKeyFrames(); // could be here depending on witch gives better performance

        if(syncNspace) { turnOnSyncNSpace(this); }

        loopOnStartOfFade = Status.isLoopOn;

        /*if (initialLoopLength != fadeOutLength) { // til að láta loopur sem eru þegar spilandi
            timeline.getKeyFrames().clear();        // breytast með þegar fadeOutLength breytist
            addFadeKeyFrames();
        } */

        timeline.play();
        playingTimelines.add(this);
        player.isLooping = Status.isLoopOn; // óþarfi?
        setOnFinishedFade();
    }


    synchronized void setOnFinishedFade() { // synchronized?
        timeline.setOnFinished(event -> {
            if (loopOnStartOfFade && Status.isLoopOn && player.isLooping) {    // && !chokedPlayers.contains(player) && !loopReleased.get() henda? // check if 'Loop' is STILL on and has not been released since note started
                if(syncNspace) { turnOnSyncNSpace(this); }
                else { timeline.setCycleCount(1); }
                player.replay();
                replayFadeOut();
            } else {
                player.isLooping = false;
                player.stop();
                timeline.stop();
                playingTimelines.remove(this);
            }
        });
    }

    synchronized void replayFadeOut() {
        timeline.playFromStart();
    }
}


/*
    synchronized void LOOPTEST(SamplePlayer player){
        while (Status.isLoopOn && player.isLooping) {
            if(fadeOutLength - System.currentTimeMillis() == fadeOutLength) {
                player.replay();
                timeline.playFromStart();
            }
        }
    }
    private Timer timer;       // LONG ER EKKI KOMMU TALA BARA STÓR HEILTALA ÞANNIG DÓT FYRIR NEÐAN VAR EKKI AÐ MARKA, SKOÐA AFTUR EF NENNI

    public void startLoop() {
        long fadeLong = (long) fadeOutLength;
        timer = new Timer();
        timer.scheduleAtFixedRate(new LoopTask(), fadeLong, fadeLong);
    }

    private class LoopTask extends TimerTask {
        @Override
        public void run() {
            if (Status.isLoopOn && player.isLooping) {
                player.replay();
                replayFadeOut();
            }
        }
    }
    */
