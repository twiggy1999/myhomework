package controller;


import com.sun.javaws.ui.SplashScreen;
import controller.MyController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Main extends Application {
    @FXML
    private Stage s;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane root;
    //游戏入口
    public static void main(String[] args) {
        launch(args);
    }
    /*
    Space: 开始游戏
    L: 回放游戏
    Esc: 退出游戏
     */
    public void addKeyListener() {
        KeyCombination L=new KeyCodeCombination(KeyCode.L);
        KeyCombination Space=new KeyCodeCombination(KeyCode.SPACE);
        KeyCombination Esc=new KeyCodeCombination(KeyCode.ESCAPE);
        scene.getAccelerators().put(L, new Runnable() {


            public void run() {


                ActionEvent event = null;
                try {
                    MyController m=new MyController();
                    m.restart(event,s,scene,root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        scene.getAccelerators().put(Space, new Runnable() {


            public void run() {


                ActionEvent event = null;
                try {
                    MyController m=new MyController();
                    m.start(event,s,scene,root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        scene.getAccelerators().put(Esc, new Runnable() {


            public void run() {


                ActionEvent event = null;

                System.exit(0);

            }
        });

    }
    //重写start函数，展示游戏初始场景
    @Override
    public void start(Stage stage) throws Exception {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            root = fxmlLoader.load();
            scene = new Scene(root);
            addKeyListener();
            stage.setScene(scene);
            s = stage;
            stage.show();
            return;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


