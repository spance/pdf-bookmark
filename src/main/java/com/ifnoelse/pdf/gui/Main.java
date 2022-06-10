package com.ifnoelse.pdf.gui;

import com.ifnoelse.pdf.GeneratingRequest;
import com.ifnoelse.pdf.PDFContents;
import com.ifnoelse.pdf.PDFUtil;
import com.ifnoelse.pdf.XAlert;
import com.itextpdf.text.exceptions.BadPasswordException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by ifnoelse on 2017/3/2 0002.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PDF Bookmark");

        BorderPane bottomPane = new BorderPane();
        Button contentsGenerator = new Button("生成目录");
        Button getContents = new Button("获取目录");
        getContents.setDisable(true);

        CheckBox useBlankAsLevel = new CheckBox("空白看作分级");
        useBlankAsLevel.setSelected(true);

        HBox h = new HBox(20, useBlankAsLevel, getContents, contentsGenerator);
        h.setAlignment(Pos.CENTER);

        bottomPane.setCenter(h);

        Button fileSelectorBtn = new Button("选择文件");

        BorderPane vBox = new BorderPane();
        TextField filePath = new TextField();

        filePath.setEditable(false);
        filePath.setPromptText("请选择PDF文件");

        BorderPane topPane = new BorderPane();
        topPane.setCenter(filePath);

        TextField pageIndexOffset = new TextField();

        HBox topRight = new HBox(4, pageIndexOffset, fileSelectorBtn);
        topRight.setPadding(new Insets(0, 0, 0, 4));
        topPane.setRight(topRight);
        vBox.setTop(topPane);

        pageIndexOffset.setPromptText("页码偏移量");
        pageIndexOffset.setPrefWidth(100);


        TextArea textArea = new TextArea();
        textArea.setPromptText("请在此填入目录内容");

        textArea.setOnDragEntered(e -> {
            Dragboard dragboard = e.getDragboard();
            File file = dragboard.getFiles().get(0); //获取拖入的文件
            String fileName = file.getName();
            if (fileName.matches("[\\s\\S]+.[pP][dD][fF]$")) {
                filePath.setText(file.getPath());
            }
        });


        textArea.textProperty().addListener(event -> {
            getContents.setDisable(!textArea.getText().trim().startsWith("http"));
        });

        vBox.setCenter(textArea);


        vBox.setBottom(bottomPane);
        Scene scene = new Scene(vBox, 600, 400);
        primaryStage.setScene(scene);

        fileSelectorBtn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("pdf", "*.pdf"));
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                filePath.setText(file.getPath());
            }
        });


        pageIndexOffset.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!observable.getValue()) {
                String offset = pageIndexOffset.getText();
                if (offset != null && offset.length() > 0 && !offset.matches("[0-9]+")) {
                    XAlert.BAD_OFFSET.show();
                }
            }
        });

        getContents.setOnAction(event -> {
            String contents = PDFContents.getContentsByUrl(textArea.getText());
            textArea.setText(contents);
        });

        contentsGenerator.setOnAction(event -> {
            String fp = filePath.getText();
            String offset = pageIndexOffset.getText();
            String content = textArea.getText();
            GeneratingRequest req = GeneratingRequest.of(fp, offset, content, useBlankAsLevel.isSelected());

            if (req.getAlert() == null) {
                try {
                    PDFUtil.addBookmark(req);
                } catch (BadPasswordException e) {
                    req.setAlert(XAlert.BAD_PDF);
                } catch (Exception e) {
                    req.setAlert(XAlert.UNKNOWN_PDF.apply(e.toString()));
                }
            }

            if (req.getAlert() != null) {
                req.getAlert().show();
            }
        });
        primaryStage.show();
    }
}
