package keynotes.vinnsla;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

class SamplePlayer {  // helper class for samples


    // FIELDS
/*
    public static void setIsLoopOnProperty(boolean isLoopOn) {
        isLoopOnProperty.set(isLoopOn);
    }

    public static BooleanProperty getIsLoopOnProperty() {
        return isLoopOnProperty;
    }

    private static final BooleanProperty isLoopOnProperty = new SimpleBooleanProperty();
    */

    public Media media;
    public int mediaIndex;

    public MediaPlayer mp;
    public double startTime;
    public boolean isCopy;

    public boolean isPlaying() { return mp.getStatus() == MediaPlayer.Status.PLAYING; }
    public boolean isLooping;
    public boolean isReleased;

    public SamplePlayer previousPlayer;

    public PlayerTimeline playerTimeline;

    public static void setMasterVolume(double masterVolume) {
        SamplePlayer.masterVolume = masterVolume;
    }

    private static double masterVolume;

                                            // CONSTRUCTORS


    public SamplePlayer(Media media, int mediaIndex, boolean isCopy) {
        this.media = media;
        this.mp = new MediaPlayer(media);
        this.mediaIndex = mediaIndex;
        this.setVolume(SamplePlayer.masterVolume);
        this.startTime = -1;
        this.isLooping = Status.isLoopOn;
        this.isCopy = isCopy;
        this.playerTimeline = new PlayerTimeline(this); // creates timeline for this specific mediaplayers volume fade out
        setupMediaPlayer();
        //addListeners();
    }


                                            // CUSTOM METHODS

/*
    private void addListeners() {
        Status.getIsLoopOn().addListener((observable, oldValue, newValue) -> {
            isLooping = newValue;
            System.out.println("isLooping = " + newValue);
        });


        Status.getLoopReleased().addListener((observable, oldValue, newValue) -> {
            //isLooping = !Status.getLoopReleased().get();
            if (playerTimeline != null) {
                this.playerTimeline.releaseLoop();
                System.out.println("release loop ");
            }
        });
    }
*/
    public double getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    public synchronized void setupMediaPlayer() {
        mp.setOnEndOfMedia(mp::stop);
        mp.setOnStopped(() -> {
            mp.seek(Duration.ZERO);
            if (!this.isLooping) {
                this.startTime = -1;
                Playback.removeFromCurrentlyPlaying(this);
                if (this.isCopy) {
                    mp.dispose();
                }
            } else {
                this.isLooping = Status.isLoopOn;
            }
        });
    }
                                            // METHODS

    public void disposeOnFinished() {
        mp.setOnStopped(() -> {
            if (!this.isLooping) {
                Playback.removeFromCurrentlyPlaying(this);
                mp.dispose();
            } else {
                this.isLooping = Status.isLoopOn;
            }
        });
    }


    synchronized void replay() {
        mp.stop(); // seek á að gerast í onstopped
        mp.setVolume(masterVolume);
        mp.play();
    }

                                        // CUSTOM GETTERS AND SETTERS


    public void setStartTime() {
        this.startTime = System.currentTimeMillis(); } // eða System.nanoTime()


                                        // MEDIAPLAYER METHODS


    public void play() {
        mp.play();
        //isMostRecentlyPlayed = true;
    }

    public void stop() {
        mp.stop();
    }

    public void dispose() {
        mp.dispose();
    }

    public Media getMedia() {
        return mp.getMedia();
    }

    public void setVolume(double volume) {
        mp.setVolume(volume);
    }

    public double getVolume() {
        return mp.getVolume();
    }

    public void setRate(double rate) {
        mp.setRate(rate);
    }

    public void seek(Duration duration) {
        mp.seek(duration);
    }

    public void setOnPlaying(Runnable r) {
        mp.setOnPlaying(r);
    }

    public void setOnEndOfMedia(Runnable r) { // nota?
        mp.setOnEndOfMedia(r);
    }
}
/*
    private static void setOnStopped(SamplePlayer player) {
        player.setOnEndOfMedia(player::stop);
        player.setOnStopped(() -> {
            player.seek(Duration.ZERO);
            removeFromCurrentlyPlaying(player);
        });
    }

 */

  /*
    public Media[] samples = Playback.getSamples();
    public boolean isCopy;
    public boolean isMostRecentlyPlayed;
    public boolean isSecondMostRecentlyPlayed;
    public boolean loopOnStartOfNote;
    public boolean isChoked;
    */


  /*  public SamplePlayer(int mediaIndex) {
        this.media = samples[mediaIndex];
        this.mediaIndex = mediaIndex;
        this.mp = new MediaPlayer(media);
        setupMediaPlayer();
    }*/