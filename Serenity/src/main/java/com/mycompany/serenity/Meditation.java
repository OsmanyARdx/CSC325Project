package com.mycompany.serenity;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

public class Meditation {


    @FXML
    private MediaView mediaView, mediaView2, mediaView3, mediaView4;

    private File file, file2, file3, file4;
    private Media media, media2, media3, media4;
    private MediaPlayer mediaPlayer, mediaPlayer2, mediaPlayer3, mediaPlayer4;

    @FXML
    public void initialize() {
        file = new File("videos/V1.mp4");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        file2 = new File("videos/V2.mp4");
        media2 = new Media(file2.toURI().toString());
        mediaPlayer2 = new MediaPlayer(media2);
        mediaView2.setMediaPlayer(mediaPlayer2);

        file3 = new File("videos/V3.mp4");
        media3 = new Media(file3.toURI().toString());
        mediaPlayer3 = new MediaPlayer(media3);
        mediaView3.setMediaPlayer(mediaPlayer3);

    }

    public void playMedia() {
        mediaPlayer.play();
    }

    public void pauseMedia() {
        mediaPlayer.pause();
    }

    public void resetMedia() {

        if (mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer.seek(Duration.seconds(0.0));
        }
    }

    public void playMedia2() {
        mediaPlayer2.play();
    }

    public void pauseMedia2() {
        mediaPlayer2.pause();
    }

    public void resetMedia2() {

        if (mediaPlayer2.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer2.seek(Duration.seconds(0.0));
        }
    }

    public void playMedia3() {
        mediaPlayer3.play();
    }

    public void pauseMedia3() {
        mediaPlayer3.pause();
    }

    public void resetMedia3() {

        if (mediaPlayer3.getStatus() != MediaPlayer.Status.READY) {
            mediaPlayer3.seek(Duration.seconds(0.0));
        }
    }

}
