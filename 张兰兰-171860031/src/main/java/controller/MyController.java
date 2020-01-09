package controller;
import Service.Battle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.event.ActionEvent;

import javafx.scene.Scene;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.FileFilterModel;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;


import java.util.*;
import java.util.Timer;

public class MyController implements Initializable {
    @FXML
    private ResourceBundle resourceBundle;
    @FXML
    private Stage stage;
    @FXML private Scene scene;
    @FXML private AnchorPane root;
    @FXML private Text GoodGuysWin;
    @FXML private Text BadGuysWin;

    private Battle battle;
    private int Winner;
    private int Mode=1;
    //初始化
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
    }
    //葫芦娃胜利页面显示
    public void showGoodWin(){
        if(GoodGuysWin==null)
            GoodGuysWin=new Text();
        GoodGuysWin.setId("GoodGuysWin");
        GoodGuysWin.setLayoutX(259.0);
        GoodGuysWin.setLayoutY(214.0);
        GoodGuysWin.setText("Huluwa Win");
        Font val=new Font("System Bold Italic",66.0);
        GoodGuysWin.setFill(Paint.valueOf("RED"));


        GoodGuysWin.setFont(val);

        GoodGuysWin.setVisible(true);
        Platform.runLater(() -> {
            root.getChildren().add(GoodGuysWin);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        });
    }
    //葫芦娃失败页面显示
    public void showBadWin(){

        Font val=new Font("System Bold Italic",66.0);
        //BadGuysWin.setId("BadGuysWin");
        if(BadGuysWin==null)
            BadGuysWin=new Text();
        BadGuysWin.setLayoutX(259.0);
        BadGuysWin.setLayoutY(214.0);
        BadGuysWin.setText("Huluwa Lose");
        BadGuysWin.setFill(Paint.valueOf("RED"));

        BadGuysWin.setFont(val);
        BadGuysWin.setVisible(true);
        Platform.runLater(() -> {
            root.getChildren().add(BadGuysWin);
            scene.setRoot(root);
            stage.setScene(scene);
            stage.show();
        });

    }
    //按L键选择恢复文件，显示游戏的回放
    public void restart(ActionEvent event,Stage s,Scene e,AnchorPane r) throws IOException {
        root=r;
        scene=e;
        stage=s;
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilterModel hlwFileFilter = new FileFilterModel();
        jFileChooser.addChoosableFileFilter(hlwFileFilter);
        jFileChooser.setFileFilter(hlwFileFilter);
        jFileChooser.setDialogTitle("选择回放文件");
        jFileChooser.showOpenDialog(null);
        final File file = jFileChooser.getSelectedFile();
        if(file != null && file.exists()) {
            //System.out.println("文件:" + file.getAbsolutePath());
            battle=new Battle(s,e,r);
            String fileName=file.getAbsolutePath();

            battle.restart_battle(file,fileName);
            battle.display();
            Mode=2;

        }
        Timer timerCheck = new Timer();
        timerCheck.schedule(new TimerTask() {
            @Override
            public void run() {
                int wins = battle.isGameOver();
                if (wins != 0) {
                    this.cancel();
                    Mode = 3;
                    Winner = wins;
                }
            }
        },0,200);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // battle.display();
                if(Mode ==3) {
                    this.cancel();
                    battle.cleanField();

                    /*battle.cleanFieldPane();
                    battle.cleanDeathPane();*/
                    if(Winner == 1) {
                        showGoodWin();

                        //System.exit(0);
                    }
                    else if(Winner == -1) {
                        showBadWin();

                        //System.exit(0);
                    }
                    try {
                        Thread.sleep(500);
                        if(GoodGuysWin!=null)
                        GoodGuysWin.setVisible(false);
                        if(BadGuysWin!=null)
                        BadGuysWin.setVisible(false);
                        battle.cleanAllPic();
                    }
                    catch(InterruptedException ee){
                        ee.printStackTrace();
                    }

                    battle.cleanAllPic();
                }
            }
        }, 0, 200);

    }
    //按Space键选择存储文件，开始游戏战斗
    public  synchronized void start(ActionEvent event, Stage s,Scene e,AnchorPane r) throws IOException{
        FileFilterModel hlwFileFilter = new FileFilterModel();
        JFileChooser fc = new JFileChooser(".");
        fc.setFileFilter(hlwFileFilter);
        fc.setMultiSelectionEnabled(false);
        String name=new String();
        int result = fc.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (!file.getPath().endsWith(".txt")) {
                file = new File(file.getPath() + ".txt");
            }
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            name=file.getAbsolutePath();
        }
        root=r;
        scene=e;
        stage=s;
        battle=new Battle(s,e,r);
        battle.open_outputFile(name);
        battle.display();
        battle.start_battle();
        Mode=1;
        Timer timerCheck = new Timer();
        timerCheck.schedule(new TimerTask() {
            @Override
            public void run() {
                int wins = battle.isGameOver();
                if (wins != 0) {
                    this.cancel();
                    Mode = 3;
                    Winner = wins;
                }
            }
        },0,200);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Mode ==3) {
                    this.cancel();
                    battle.cleanField();
                    if(Winner == 1) {
                        showGoodWin();
                    }
                    else if(Winner == -1) {
                       showBadWin();
                    }
                    try {
                        Thread.sleep(500);
                        if(GoodGuysWin!=null)
                        GoodGuysWin.setVisible(false);
                        if(BadGuysWin!=null)
                        BadGuysWin.setVisible(false);
                        battle.cleanAllPic();
                    }
                    catch(InterruptedException ee){
                        ee.printStackTrace();
                    }
                    battle.insert_all_dead();
                    battle.close_outputFile();
                }
            }
        }, 0, 200);
    }





}



