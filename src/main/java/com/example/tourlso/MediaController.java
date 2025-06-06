package com.example.tourlso;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class MediaController {

    private String currentPlace;
    private final Map<String, String> videoPaths = new HashMap<>();
    private final Map<String, String> audioPaths = new HashMap<>();
    private final Map<String, List<Question>> quizData = new HashMap<>();

    public MediaController() {
        videoPaths.put("Katse Dam", "/assets/Katse dam.mp4");
        videoPaths.put("Maletsunyane Fall", "/assets/Maletsunyane.mp4");
        videoPaths.put("Thaba Bosiu", "/assets/thaba bosiu.mp4");


        audioPaths.put("Katse Dam", "/assets/audios/Katse dam.mp3");
        audioPaths.put("Maletsunyane Fall", "/assets/audios/Maletsunyane.mp3");
        audioPaths.put("Thaba Bosiu", "/assets/audios/Thaba bosiu.mp3");


        quizData.put("Katse Dam", List.of(
                new Question("Where is Katse Dam located?", "Lesotho", List.of("South Africa", "Lesotho", "Namibia", "Zimbabwe")),
                new Question("What river is dammed by Katse Dam?", "Malibamat'so", List.of("Senqu", "Orange", "Malibamat'so", "Liphofung")),
                new Question("Katse Dam is part of which project?", "Lesotho Highlands Water Project", List.of("Hydro SA", "LHDA", "Lesotho Highlands Water Project", "Dam Solutions Africa")),
                new Question("What is Katse Dam known for?", "Water storage and hydroelectricity", List.of("Fishing", "Swimming", "Water storage and hydroelectricity", "Irrigation only")),
                new Question("Which town is closest to Katse Dam?", "Thaba-Tseka", List.of("Maseru", "Butha-Buthe", "Thaba-Tseka", "Quthing"))
        ));
        quizData.put("Maletsunyane Fall", List.of(
                new Question("How high is Maletsunyane Falls?", "192 meters", List.of("80 meters", "150 meters", "192 meters", "210 meters")),
                new Question("Which river forms Maletsunyane Falls?", "Maletsunyane River", List.of("Senqu River", "Makhaleng River", "Maletsunyane River", "Orange River")),
                new Question("In which town is Maletsunyane Falls located?", "Semonkong", List.of("Thaba-Tseka", "Mokhotlong", "Semonkong", "Maseru")),
                new Question("Maletsunyane Falls is famous for what sport?", "Abseiling", List.of("Kayaking", "Bungee Jumping", "Abseiling", "Paragliding")),
                new Question("What type of waterfall is Maletsunyane?", "Plunge waterfall", List.of("Cataract", "Plunge waterfall", "Tiered", "Cascade"))
        ));
        quizData.put("Thaba Bosiu", List.of(
                new Question("What does Thaba Bosiu mean?", "Mountain at Night", List.of("Mountain at Night", "Valley of Kings", "Stone Fortress", "Sacred Peak")),
                new Question("Who was the leader that used Thaba Bosiu as a stronghold?", "Moshoeshoe I", List.of("Moshoeshoe I", "Letsie III", "Mohlomi", "Lepoqo")),
                new Question("What was the strategic advantage of Thaba Bosiu?", "Inaccessibility at night", List.of("Water source", "Gold deposits", "Inaccessibility at night", "Flat terrain")),
                new Question("Thaba Bosiu is considered the birthplace of?", "Basotho Nation", List.of("Zulu Kingdom", "Boer Republics", "Basotho Nation", "Sotho Language")),
                new Question("Which colonial power attempted to conquer Thaba Bosiu?", "Boers", List.of("British", "Zulu", "Boers", "Portuguese"))
        ));

    }

    public void setCurrentPlace(String place) {
        this.currentPlace = place;
    }

    public void showVideoDialog() {
        String videoPath = videoPaths.get(currentPlace);
        URL resource = getClass().getResource(videoPath);
        if (resource == null) {
            showAlert("Video Not Found", "Video file not found: " + videoPath);
            return;
        }

        MediaPlayer mediaPlayer = new MediaPlayer(new Media(resource.toString()));
        MediaView mediaView = new MediaView(mediaPlayer);

        Button playPauseBtn = new Button("Pause");
        playPauseBtn.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseBtn.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseBtn.setText("Pause");
            }
        });

        VBox root = new VBox(10, mediaView, playPauseBtn);
        root.setStyle("-fx-background-color: #1e1e1e; -fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(root, 640, 480);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(currentPlace + " - Video Tour");
        stage.setScene(scene);
        stage.show();

        mediaPlayer.play();
    }

    public void showAudioDialog() {
        String audioPath = audioPaths.get(currentPlace);
        URL resource = getClass().getResource(audioPath);
        if (resource == null) {
            showAlert("Audio Not Found", "Audio file not found: " + audioPath);
            return;
        }

        MediaPlayer mediaPlayer = new MediaPlayer(new Media(resource.toString()));
        mediaPlayer.play();

        Label playingLabel = new Label("Now Playing: " + currentPlace);
        playingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button playPauseBtn = new Button("Pause");
        playPauseBtn.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPauseBtn.setText("Play");
            } else {
                mediaPlayer.play();
                playPauseBtn.setText("Pause");
            }
        });

        VBox layout = new VBox(10, playingLabel, playPauseBtn);
        layout.setStyle("-fx-background-color: #333333; -fx-padding: 20; -fx-alignment: center;");
        Scene scene = new Scene(layout, 300, 150);

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(currentPlace + " - Audio Guide");
        stage.setScene(scene);
        stage.show();
    }

    public void showQuizDialog() {
        List<Question> questions = quizData.get(currentPlace);
        if (questions == null || questions.isEmpty()) {
            showAlert("No Quiz", "No quiz available for this location.");
            return;
        }

        Iterator<Question> iterator = questions.iterator();
        int[] score = {0};
        showQuestion(iterator, score, questions.size());
    }

    private void showQuestion(Iterator<Question> iterator, int[] score, int total) {
        if (!iterator.hasNext()) {
            showAlert("Quiz Complete", "Your Score: " + score[0] + " / " + total);
            return;
        }

        Question q = iterator.next();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Quiz: " + currentPlace);

        Label questionLabel = new Label(q.question());
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ToggleGroup group = new ToggleGroup();
        VBox optionsBox = new VBox(10);
        for (String option : q.options()) {
            RadioButton rb = new RadioButton(option);
            rb.setToggleGroup(group);
            rb.setStyle("-fx-font-size: 14px;");
            optionsBox.getChildren().add(rb);
        }

        Button nextBtn = new Button("Next");
        nextBtn.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white;");
        nextBtn.setOnAction(e -> {
            RadioButton selected = (RadioButton) group.getSelectedToggle();
            if (selected != null && selected.getText().equals(q.correct())) {
                score[0]++;
            }
            stage.close();
            showQuestion(iterator, score, total);
        });

        VBox root = new VBox(20, questionLabel, optionsBox, nextBtn);
        root.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 20;");
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 450, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private record Question(String question, String correct, List<String> options) {}
}
