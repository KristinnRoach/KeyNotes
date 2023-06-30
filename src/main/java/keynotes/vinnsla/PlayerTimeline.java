package keynotes.vinnsla;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;


public class PlayerTimeline {

    public Set<PlayerTimeline> playingTimelines = new HashSet<>();

    // Static Fields

    private static int tempo;
    public static void setTempo(int tmpo) {
        tempo = tmpo;
    }
    public static double getFadeOutLength() {
        return fadeOutLength;
    }

    public static void setFadeOutLength(int fadeLength) {
        System.out.println(fadeLength);

        int tempoToMs = tempo * 1000;

        int sixteenthNoteDuration = tempoToMs / 4;
        int eighthNoteTripletDuration = tempoToMs / 3;
        int eighthNoteDuration = tempoToMs / 2;
        int quarterNoteDuration = tempoToMs;
        int halfNoteDuration = tempoToMs * 2;
        int wholeNoteDuration = tempoToMs * 4;


        if (fadeLength <= sixteenthNoteDuration) { // sixteenths
            fadeOutLength = sixteenthNoteDuration;

        } else if (fadeLength <= eighthNoteTripletDuration) { // 8th triplets
            fadeOutLength = eighthNoteTripletDuration;

        } else if (fadeLength <= eighthNoteDuration) {  // 8th
            fadeOutLength = eighthNoteDuration;

        } else if (fadeLength <= quarterNoteDuration) { // 4th
            fadeOutLength = quarterNoteDuration;

        } else if (fadeLength <= halfNoteDuration) {
            fadeOutLength = halfNoteDuration;

        } else {
            fadeOutLength = wholeNoteDuration;
        }

        System.out.println(fadeOutLength);
    }

    private static double fadeOutLength;


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

    void releaseLoop() {
        this.loopReleased = true;
    }

    synchronized void addFadeKeyFrames() { // synchronized?
        double initialVolume = player.getVolume();
        int numSteps = 100; // Number of steps for the fade-out
        double scaleFactor = Math.pow(0.001 / initialVolume, 1.0 / numSteps); // Exponential scale factor

        for (int i = 0; i < numSteps; i++) {
            if (player.getVolume() <= 0.0001) {
                break;
            }  // ensure that volume does not become negative
            double volume = initialVolume * Math.pow(scaleFactor, i);
            Duration time = Duration.millis(fadeOutLength * i / numSteps);
            KeyFrame keyFrame = new KeyFrame(time, event -> {
                player.setVolume(volume);
            });
            timeline.getKeyFrames().add(keyFrame);
        }
    }

    synchronized void startFadeOut() {
        addFadeKeyFrames(); // could be here depending on witch gives better performance
        // timeline.setCycleCount(3);  // INTERESTING

        loopOnStartOfFade = Status.isLoopOn;

        /*if (initialLoopLength != fadeOutLength) {
            timeline.getKeyFrames().clear();
            addFadeKeyFrames();
        } */

        timeline.play();
        playingTimelines.add(this);
        player.isLooping = Status.isLoopOn; // óþarfi?
        setOnFinishedFade();

/*
        if(Status.isLoopOn){
            startLoop();
        } */
        //LOOPTEST(player);
    }


    synchronized void setOnFinishedFade() { // synchronized?
        timeline.setOnFinished(event -> {
            if (loopOnStartOfFade && Status.isLoopOn && player.isLooping) {    // && !chokedPlayers.contains(player) && !loopReleased.get() henda? // check if 'Loop' is STILL on and has not been released since note started
                player.replay();
                replayFadeOut();
            } else {
                player.isLooping = false;
                player.stop();
                //player.dispose(); //?
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
    private Timer timer;

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
