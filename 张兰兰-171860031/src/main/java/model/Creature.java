package model;

import Service.Field;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.*;
import java.util.concurrent.Semaphore;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Thread.sleep;
enum State_of_Creature{ALIVE,DYING,DEAD}
//记录信息的结构
class Record{
    public long time;
    public double x;
    public double y;
    public Record(double x,double y,long time){
        this.x=x;
        this.y=y;
        this.time=time;
    }

}
public class Creature extends ImageView implements Runnable {
    private Boolean has_enemy=false;
    private double x;
    private double y;
    private Field battleField;
    private Boolean identity;
    private State_of_Creature state;
    private int enemy=-1;
    private int rate;
    private int no;
    private Thread thread=new Thread();
    private Image dying;
    private long init_time;
    private State_of_Creature new_state=State_of_Creature.ALIVE;
    private List<Record> record = new ArrayList<>();//回放部分战斗信息的存储
    //初始化
    public Creature() {
    }
    public Creature(String image_route, int x, int y,int i) {
        this.state= State_of_Creature.ALIVE;
        this.setImage(new Image(image_route));
        this.x = x;
        this.y = y;
        this.no=i;
        this.setFitHeight(58.0);
        this.state= State_of_Creature.ALIVE;
        this.setFitWidth(29.0);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPickOnBounds(true);
        this.setPreserveRatio(true);
        if(i<8) {
            this.rate=10;
            this.identity = true;
        }
        else {
            this.rate=10;
            this.identity = false;
        }
        this.dying=new Image("/pics/die.png");
        this.has_enemy=false;

    }

    //辅助函数，主要返回生物体信息
    public void set_dead(){
        this.state=State_of_Creature.DYING;
        this.setImage(this.dying);

   }
    public void set_new_dead(){
        new_state=State_of_Creature.DEAD;
   }
    public void set_init_time(){
       init_time=System.currentTimeMillis();
   }
    public Boolean ret_identity(){return this.identity;}
    public Boolean ret_has_enemy(){
        return has_enemy;
    }
    public void set_enemy(int n){
        this.enemy=n;
        if(n==-1){

            has_enemy=false;
        }
        else
            has_enemy=true;
    }
    public int ret_rate(){
        return this.rate;
    }
    public Boolean isALIVE(){
        if(state== State_of_Creature.ALIVE)
            return true;
        else
            return false;
    }
    public void set_battleFiled(Field battleFiled){
        this.battleField=battleFiled;
    }
    public void set_invisible(){
        this.setVisible(false);
    }
    public void insert_thread(Thread thread){
        this.thread=thread;
    }

    //生物体运动的时候，将运动信息写入文件
    public void insert_record(double x,double y,long time){
        Record p=new Record(x,y,time);
        record.add(p);
   }
   //根据生物体的运动更新UI界面
    public void change_place(double x, double y) {
        try {

                final Semaphore semaphore = new Semaphore(1);
                try {
                    semaphore.acquire();
                    if(battleField.ret_mode()==1) {
                        battleField.insertFileRecord(no, x, y,System.currentTimeMillis()-init_time);
                    }
                    Platform.runLater(() -> {


                        Path p = new Path();
                        PathTransition pt = new PathTransition();
                       pt.setDuration(Duration.millis(20));
                      //  pt.setCycleCount(1);


                        pt.setAutoReverse(false);
                        pt.setDelay(Duration.seconds(.5));


                       /* if(x<=this.getLayoutX()&&y<=this.getLayoutY()) {
                            p.getElements().add(new MoveTo(0, 0));
                            p.getElements().add(new LineTo(abs(x - this.getLayoutX()), abs(this.getLayoutY() - y)));

                            pt.setPath(p);
                            this.setLayoutX(x);
                            this.setLayoutY(y);
                            pt.setNode(this);
                        }
                        else {
                            this.setLayoutX(x);
                            this.setLayoutY(y);
                            p.getElements().add(new MoveTo(abs(this.getLayoutX() - this.x), abs(this.getLayoutY() - this.y)));
                            p.getElements().add(new LineTo(0, 0));
                            pt.setPath(p);

                            pt.setNode(this);
                        }*/

                        this.x = x;
                        this.y = y;
                        //System.out.println(x+" , "+y);
                       // p.getElements().add(new MoveTo(abs(this.getLayoutX() - this.x), abs(this.getLayoutY() - this.y)));
                        p.getElements().add(new MoveTo(abs(this.getLayoutX() - x), abs(this.getLayoutY() - y)));
                        this.setLayoutX(x);
                        this.setLayoutY(y);
                        pt.setPath(p);

                        pt.setNode(this);
                        pt.setDuration(Duration.millis(20));
                        pt.play();


                    });

                } catch (InterruptedException e) {
                }
                semaphore.release();



            //Thread.sleep(200);


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    //查找敌人
    public synchronized void find_enemy() {
        if(this.state==State_of_Creature.ALIVE) {
            int enemy_find = -1;
            double min_distance = pow(2, 30);
            int i = 0;
            for (; i < 19; i++) {

                if (battleField.ret_identity(i) != identity && battleField.isALIVE(i) == true && battleField.has_enemy(i) == false) {
                    double distance = pow((battleField.ret_x(i) - this.x), 2) + pow((battleField.ret_y(i) - this.y), 2);
                    if (distance < min_distance) {
                        min_distance = distance;
                        enemy_find = i;
                    }
                }

            }


            if (enemy_find != -1) {
                battleField.set_enemy(enemy_find, no);
                this.set_enemy(enemy_find);
                plan_fight(enemy_find);

            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //找到敌人后设置运动路线以及who lose
    public synchronized void plan_fight(int enemy){

        double ret_x_one = this.getLayoutX();
        double ret_y_one = this.getLayoutY();
        double ret_x_two = battleField.ret_x(enemy);
        double ret_y_two = battleField.ret_y(enemy);
        double x_route = ret_x_one - ret_x_two;
        double y_route = ret_y_one - ret_y_two;
        double one_rate = rate;
        double two_rate = battleField.ret_rate(enemy);

        if(ret_x_one<0||ret_x_one>880||ret_y_one<320||ret_y_one>480||ret_x_two<0||ret_x_two>880||ret_y_two<320||ret_y_two>480){
            if(ret_x_one<0||ret_x_one>880||ret_y_one<320||ret_y_one>480) {
                System.out.println("error1 "+this.no+" x: "+ret_x_one+" y: "+ret_y_one+" "+battleField.isALIVE(enemy));
                battleField.set_enemy(enemy,-1);
                set_dead();

            }
            if(ret_x_two<0||ret_x_two>880||ret_y_two<320||ret_y_two>480) {
                System.out.println("error2 "+enemy+" x: "+ret_x_two    +" y: "+ret_y_two+" "+battleField.isALIVE(enemy));
                this.set_enemy(-1);
                battleField.set_dead(enemy);
            }
            return;
        }






        int flag_x = 1;
        int flag_y = 1;
        if (x_route > 0)
            flag_x = -1;
        if (y_route > 0)
            flag_y = -1;
        int times_x = (int) ((abs(x_route) / (one_rate+two_rate)));
        int times_y = (int) ((abs(y_route) / (one_rate+two_rate)));
        Random r = new Random();
        int number = r.nextInt(2);
        if(x_route!=0) {
            for (int i = 1; i <= times_x; i++) {
                if (number == 0) {

                    battleField.set_move(enemy, ret_x_two + (-flag_x) * i * two_rate, ret_y_two);
                    this.change_place(ret_x_one + flag_x * i * one_rate, ret_y_one);
                } else {
                    this.change_place(ret_x_one + flag_x * i * one_rate, ret_y_one);
                    battleField.set_move(enemy, ret_x_two + (-flag_x) * i * two_rate, ret_y_two);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        double last_x_one = ret_x_one + times_x * flag_x * one_rate;
        double last_x_two = ret_x_two + times_x * flag_x * two_rate * -1;
        if(y_route!=0) {
            for (int i = 1; i <= times_y; i++) {
                if (number == 0) {
                    this.change_place(last_x_one, ret_y_one+flag_y * i * one_rate);
                    battleField.set_move(enemy, last_x_two, ret_y_two + (-flag_y) * i * two_rate);
                } else {

                    battleField.set_move(enemy, last_x_two, ret_y_two + (-flag_y) * i * two_rate);
                    this.change_place(last_x_one, ret_y_one+flag_y * i * one_rate);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }


        if (number == 0) {

            battleField.set_enemy(enemy, -1);
            set_dead();


        } else {

            battleField.set_dead(enemy);
            set_enemy(-1);

        }




    }

    //线程启动
    @Override
    public synchronized void run() {
        if(battleField.ret_mode()==1) {
            while (battleField.isGameOver() == 0) {
                if (this.state == State_of_Creature.ALIVE && this.has_enemy == false) {

                    this.find_enemy();

                }
                if (this.state != State_of_Creature.ALIVE) {

                    try {

                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.state = State_of_Creature.DEAD;

                    this.thread.interrupt();
                    break;

                }

            }
        }
        else{
            int size = record.size();

            for(int i=0;i<size;){

                Record p= record.get(i);
                if(this.init_time+p.time<=System.currentTimeMillis()||abs(this.init_time+p.time-System.currentTimeMillis())<100) {
                    change_place(p.x, p.y);


                    i++;
                }


            }
            if (this.new_state != State_of_Creature.ALIVE) {
                set_dead();
                try {

                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.state = State_of_Creature.DEAD;
                this.thread.interrupt();
                return;

            }


        }

    }




}
