package keynotes.vinnsla;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.*;

import static keynotes.vinnsla.Status.*;

public class Playback {

    private static String samplePackPath;
    public static void setSamplePackPath(String path) {
        samplePackPath = path;
    }
    private static final int nrOfSamples = 76;
    private static final Media[] samples = new Media[nrOfSamples];
    private static final SamplePlayer[] samplePack = new SamplePlayer[nrOfSamples]; // prófa
    public static Media[] getSamples() {
        return samples;
    }
    private static final int MAX = 40; // number of allowed active media players
    private static final int keyboardRollOver = 6; // number of keys that can be pressed at the same time on a macbook keyboard

    public static Set<SamplePlayer> getPlayingPlayersSet() {
        return playingPlayersSet;
    }

    private static final Set<SamplePlayer> playingPlayersSet = new HashSet<>(); // set of currently playing SamplePlayers
    private static final Map<Media, SamplePlayer> currentlyPlayingMap = new HashMap<>(); // set of currently playing SamplePlayers
    private static final Queue<SamplePlayer> playingFIFO = new LinkedList<>();
    public static void addToCurrentlyPlaying(SamplePlayer samplePlayer) {
        playingPlayersSet.add(samplePlayer);
        currentlyPlayingMap.put(samplePlayer.media, samplePlayer);
        playingFIFO.add(samplePlayer);
    }
    public static void removeFromCurrentlyPlaying(SamplePlayer samplePlayer) {
        playingPlayersSet.remove(samplePlayer);
        currentlyPlayingMap.remove(samplePlayer.media);
        playingFIFO.remove(samplePlayer);
    }


    private static double masterVolume; // volume of all MediaPlayers
    public static double getMasterVolume() {
        return masterVolume;
    }

    private static MediaPlayer metronome;

    private Playback() { throw new IllegalStateException("Utility class"); }

    public static void importSamplePack() {
        File folder = new File(samplePackPath);
        File[] files = folder.listFiles((dir, name)
                -> name.endsWith(".wav") || name.endsWith(".mp3"));
        assert files != null;
        Arrays.sort(files);
        for (int i = 0; i < files.length; i++) {
            Media media = new Media(files[i].toURI().toString());
            samples[i] = media;
            samplePack[i] = new SamplePlayer(samples[i], i);
            samplePack[i].isPlaying = false;
        }
    }

    public static synchronized void setMasterVolume(double volume) {

        SamplePlayer.setMasterVolume(volume);

        for (SamplePlayer player : playingPlayersSet) {
            if (player.getVolume() == masterVolume) { // && masterVolume != 0 // er þetta besta leiðin?
                player.setVolume(masterVolume);
            }
        }
        masterVolume = volume; // ætti þetta að vera fyrir ofan for loopuna?
    }

    public static SamplePlayer mostRecentPlayer;
    public static SamplePlayer previousPlayer;

    public static synchronized void playMedia(int mediaIndex, int nrOfKeysPressed) {

        if (isSustainOn && isLoopLocked) {
            releaseCurrentlyLooping();
            //previousPlayer.isLooping = false;
        }

        if (nrOfKeysPressed <= keyboardRollOver && currentlyPlayingMap.size() < MAX) {
            SamplePlayer player;

            if ( !samplePack[mediaIndex].isPlaying ) { // use an existing mediaplayer if it is not already playing
                player = samplePack[mediaIndex];
            }
            else {
                player = new SamplePlayer(samples[mediaIndex], mediaIndex);
                samplePack[mediaIndex] = player;
            }
            player.setVolume(masterVolume);
            player.play();
            player.isPlaying = true;
            player.setStartTime();
            previousPlayer = mostRecentPlayer;
            mostRecentPlayer = player;

            if(!isSustainOn) {
                player.playerTimeline.startFadeOut();
            } else if (Status.isLoopLocked) {
                releaseCurrentlyLooping();
            }

            addToCurrentlyPlaying(player);

            System.out.println(currentlyPlayingMap.size());

        }
    }
/*
    private static synchronized void chokePreviousPlayers() {
        double timeSinceNoteStart = System.currentTimeMillis() - previousPlayer.startTime;
        if (timeSinceNoteStart >= 100) {
            chokedPlayers.add(previousPlayer);
        }
    } */

    public static synchronized void noteKeyReleased(int mediaIndex) {
        if (isSustainOn) {
            /*SamplePlayer player = samplePack[mediaIndex];
            //System.out.println( player.mediaIndex);
            //SamplePlayer player = currentlyPlayingMap.get(samples[mediaIndex]);

            //PlayerTimeline playerTimeline = player.playerTimeline;
            assert playerTimeline != null;
            playerTimeline.startFadeOut();
            */

            samplePack[mediaIndex].playerTimeline.startFadeOut();
        }
    } // eitthvað kúl með delay?

    public static void releaseCurrentlyLooping() {
        for (SamplePlayer player : playingPlayersSet) {
            player.isLooping = false;
            player.isReleased = true;
            System.out.println("Momentary Release : player.isLooping : false ");
        }
    }

    public static void initialize() {
        setSamplePackPath("src/main/resources/keynotes/vidmot/Audio/mp3/mp3_C2-C7");
        importSamplePack();
    }

    /*

    blablabla git breytasrga

    public static void handleShift(boolean b) { // bara prófa að nota shift í þetta fyrst það var laust
        isDelayOn = b;
    }

    public static void handleSpace(boolean spaceDown) {
        if (!isLoopLocked) {
            isLoopOn = spaceDown;
            isLoopOnProperty.set(spaceDown);
        } else if (spaceDown) {
            for (SamplePlayer player : playingPlayersSet) {
                player.isReleased = true;
            }
        }
    }

    public static void handleTab(boolean isTabOn) {
        isSustainOn = isTabOn;
        isSustainOnProperty.set(isTabOn);
        System.out.println("sustain: " + isTabOn);
    }

    public static void handleCaps(boolean isCapsOn) {
        isLoopLocked = isCapsOn;
        isLoopOn = isCapsOn;
        isLoopOnProperty.set(isCapsOn);
    }

    public static void handleBackQuote(boolean isBackQuoteOn) { // hvað er náttúrulegast að nota í? ekki bæta við einhverju sem flækir fyrir notendanum
    }

    */
}

   /* public static void handleCaps(boolean lockingKeyState) {
        isLoopLocked = lockingKeyState;
    } */

/*
    public static void addListeners() { // breyta manually í handle aðferðum í playback

        ToolKeys.isSpaceDown.addListener((observable, oldValue, newValue) -> {
            if (!isLoopLocked.get()) {
                isLoopOn.set(newValue);
            } else if (newValue){
                momentaryLoopRelease();
            }
        });

        ToolKeys.isCapsOn.addListener((observable, oldValue, newValue) -> {
            isLoopLocked.set(newValue);
            isLoopOn.set(newValue);
        });


        ToolKeys.isBackQuoteOn.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                isDelayOn.set(true);
                System.out.println("delay on");
                // startDelay();
            } else {  // if (!isDelayLocked)
                isDelayOn.set(false);
                System.out.println("delay off");
                //stopDelay();
            }
        });
    }
    */
/*
    public static void setCurrentPlayer(SamplePlayer player) {
        currentPlayer = player;
    }

    public static void setPreviousPlayer(SamplePlayer player) {
        previousPlayer = player;
    }

    private static SamplePlayer currentPlayer;
    private static SamplePlayer previousPlayer; */
/*
    private static synchronized void momentaryLoopRelease() {
        isLoopOn = false;  // if loop is locked releases the loop so that notes can be replaced
        PauseTransition pause = new PauseTransition(Duration.millis(20));
        pause.setOnFinished(event -> {
            isLoopOn = true;
        });
        pause.play();
    }
*/