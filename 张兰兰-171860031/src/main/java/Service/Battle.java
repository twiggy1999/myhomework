package Service;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class Battle {
    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private AnchorPane root;
    private String file;
    private Field battleField=new Field();
    private int Mode;
    private Creature []c=new Creature[19];
    private ExecutorService exec;
    private String inFileName;
    private BufferedOutputStream out;
    //初始化
    public Battle(){

    }
    public Battle(Stage s, Scene e, AnchorPane a) {
        stage=s;
        scene=e;
        root=a;
        exec = Executors.newFixedThreadPool(20);


        for (int i = 0; i < 19; i++) {

            switch (i) {
                case 0:
                    c[i] = new Creature("/pics/grandpa.png", 0, 320,i);

                    break;
                case 1:
                    c[i]  = new Creature("/pics/hongwa.png", 0, 400,i);
                    break;
                case 2:
                    c[i]  = new Creature("/pics/chenwa.png", 0, 480,i);
                    break;
                case 3:
                    c[i]  = new Creature("/pics/huangwa.png", 40, 360,i);
                    break;
                case 4:
                    c[i]  = new Creature("/pics/lvwa.png", 40, 440,i);
                    break;
                case 5:
                    c[i]  = new Creature("/pics/lanwa.png", 80, 320,i);
                    break;
                case 6:
                    c[i]  = new Creature("/pics/dingwa.png", 80, 400,i);
                    break;
                case 7:
                    c[i]  = new Creature("/pics/ziwa.png", 80, 480,i);
                    break;
                case 8:
                    c[i]  = new Creature("/pics/shejing.png", 840, 320,i);
                    break;
                case 9:
                    c[i]  = new Creature("/pics/xiezijing.png", 840, 400,i);
                    break;
                case 10:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 840, 480,i);
                    break;
                case 11:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 800, 360,i);
                    break;
                case 12:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 800, 440,i);

                    break;
                case 13:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 760, 320,i);
                    break;
                case 14:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 760, 400,i);
                    break;
                case 15:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 760, 480,i);
                    break;
                case 16:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 720, 320,i);
                    break;
                case 17:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 720, 400,i);
                    break;
                case 18:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 720, 480,i);
                    break;
                default:
                    c[i]  = new Creature("/pics/xiaoluoluo.png", 58, 29,i);
                    break;

            }
            battleField.insert_creature(c[i]);

            root.getChildren().add(c[i]);




        }
        for(int i=0;i<19;i++){

            c[i].set_battleFiled(battleField);
        }
        battleField.set_battle(this);
        //this.display();
    }
    //打开和关闭输入文件
    public void open_outputFile(String name){
        inFileName=name;
        try {
            out = new BufferedOutputStream(new FileOutputStream(inFileName));
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public void close_outputFile(){
        try {
            out.flush();
            out.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    //将战斗结果输入到文件中
    public void insert_all_dead(){
        try{
            for(int i=0;i<19;i++) {
                if(battleField.isALIVE(i)==false) {
                    String str = String.valueOf(i);
                    str = str + " ";
                    str += "DEAD";
                    str += "\n";
                    byte[] b = new byte[str.length() + 1];
                    b = str.getBytes("GBK");
                    int length = b.length;
                    out.write(b, 0, length);
                    out.flush();
                }
            }


        }catch (Exception e){
            e.printStackTrace();

        }
    }
    //返回当前游戏的模式
    public int ret_mode(){
        return this.Mode;
    }
    //删除战场所有无用图片
    public void cleanAllPic(){
        battleField.cleanAllPic();
    }
    //检测战场上是否已经分出胜负
    public synchronized int isGameOver(){
        int res=battleField.isGameOver();
        return res;
    }
    //清理战场的尸体
    public void cleanField(){

        battleField.cleanField();

    }
    //展示战场的战斗状况
    public void display(){
        try {
            new Thread(() -> {
                final Semaphore semaphore = new Semaphore(1);
                try {
                    semaphore.acquire();
                    Platform.runLater(() -> {
                        scene.setRoot(root);
                        stage.setScene(scene);
                        stage.show();
                    });

                } catch (InterruptedException e) {
                }
                semaphore.release();


            }).start();
            //Thread.sleep(200);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    //恢复文件中的战斗信息
    public boolean getHistoryMove(String fileName) {
        try{
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileName));
            // byte []record=new byte[2048];
            int n=-1;
            Scanner inScanner=new Scanner(in);
            while(inScanner.hasNextLine()){
                String str=inScanner.nextLine();
                changeToRecord(str);

            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }
    //将文件中的记录储存到人物的战斗记录中去
    public void changeToRecord(String str){

        int no_index = str.indexOf(" ");
        String temp=str.substring(0, no_index );
        int no = Integer.parseInt(temp);
        int length = str.length();
        temp= str.substring(no_index + 1, length );
        str=temp;
        String dead=str.substring(0,4);

        if(str.length()==4){
            // System.out.println("\nfind dead\n");
            battleField.set_new_dead(no);
            return;
        }
        else {
            int x_index = str.indexOf(" ");
            temp = str.substring(0, x_index);
            double x = Double.valueOf(temp);
            length = str.length();
            temp = str.substring(x_index + 1, length);
            str = temp;
            int y_index = str.indexOf(" ");
            temp=str.substring(0, y_index );

            double y = Double.valueOf(temp);
            length = str.length();
            temp = str.substring(y_index + 1, length);
            str = temp;
            long time=Long.valueOf(str);


            battleField.insert_record(no, x, y,time);
        }

    }
    //向文件中插入战斗信息
    public void insertFileRecord(int no,double x,double y,long time){
        try{

            String str=String.valueOf(no);
            str=str+" ";
            str+=String.valueOf(x);
            str+=" ";
            str+=String.valueOf(y);
            str+=" ";
            str+=String.valueOf(time);
            str+="\n";
            byte[] b=new byte[str.length()+1];
            b=str.getBytes("GBK");
            int length=b.length;
            out.write(b,0,length);


        }catch (Exception e){
            e.printStackTrace();

        }


    }
    //按Space键，开始战斗
    public synchronized void start_battle(){
        Mode=1;

        Date date=new Date();
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getTimeInstance();
        for(int i=0;i<19;i++){
            c[i].set_init_time();

        }

       // display();
        for(int i=0;i<19;i++) {
            Thread thread=new Thread(c[i]);

            c[i].insert_thread(thread);

           exec.execute(thread);
        }
        try{
            if(battleField.isGameOver()!=0) {
                exec.shutdown();

                if (!exec.awaitTermination(20000, TimeUnit.MILLISECONDS)) {
                    // 超时的时候向线程池中所有的线程发出中断(interrupted)。
                    exec.shutdownNow();
                }
            }
        }
        catch(Exception e){
            exec.shutdownNow();
            e.printStackTrace();
        }



    }
    //按L键回复战斗
    public void restart_battle(File file,String fileName){
        //  System.out.println(fileName);
        Mode=2;

        getHistoryMove(fileName);
        for(int i=0;i<19;i++){
            c[i].set_init_time();

        }
        for(int i=0;i<19;i++) {
            Thread thread=new Thread(c[i]);

            c[i].insert_thread(thread);

            exec.execute(thread);
        }
    }



}
