package Service;

import model.Creature;

import java.util.concurrent.Semaphore;

import static java.lang.Math.abs;

public class Field {
    private int x_begin=0;//0
    private int x_end=880;//880
    private int y_begin=320;
    private int y_end=480;
    private Block[] blocks;
    private int init_num=0;
    private Battle battle;
    Semaphore a = new Semaphore(1);
    //初始化
    public Field(){
        blocks=new Block[19];
        for(int i=0;i<19;i++)
            blocks[i]=new Block();

    }

    //提供给Battle的辅助函数，以返回战场的具体信息，以及对战场进行操控
    public void insert_creature(Creature c){
        blocks[init_num].insert_creature(c);
        init_num++;
    }
    public synchronized int isGameOver(){
        int flag=0;
        int res=0;
        for(int i=0;i<8;i++){
            if(blocks[i].isALIVE()== true){

                res=1;
                break;
            }

        }

        for(int i=8;i<19;i++){
            if(blocks[i].isALIVE()== true){

                flag=1;
                break;
            }
        }
       if(res==0&&flag==1){
           return -1;
       }
       else if(res==1&&flag==0){


               return 1;

       }
       else
           return 0;
    }
    public void cleanField(){
        for(int i=0;i<19;i++){
            if(blocks[i].ret_creature().isALIVE()==false)
            blocks[i].ret_creature().set_invisible();
        }
        try{
            Thread.sleep(1000);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void insert_record(int no,double x,double y,long time){
        blocks[no].insert_record(x,y,time);
    }
    public int ret_mode(){
        return this.battle.ret_mode();
    }
    public void set_dead(int i){
        blocks[i].set_dead();
    }
    public Boolean ret_identity(int i){
        return blocks[i].ret_identity();
    }
    public Boolean isALIVE(int i){
        return blocks[i].isALIVE();
    }
    public Boolean has_enemy(int i){
        return blocks[i].has_enemy();
    }
    public double ret_x(int i){
        return blocks[i].ret_x();
    }
    public double ret_y(int i){
        return blocks[i].ret_y();
    }
    public void set_enemy(int a,int b){
        blocks[a].set_enemy(b);
    }
    public double ret_rate(int i){
       return  blocks[i].ret_rate();
    }
    public synchronized void set_move(int i,double x,double y){
        blocks[i].set_move(x,y);
    }
    public void cleanAllPic() {
        for(int i=0;i<19;i++)
        blocks[i].ret_creature().setVisible(false);
    }
    public void set_battle(Battle battle){
        this.battle=battle;
    }
    public void insertFileRecord(int no,double x,double y,long time){
        battle.insertFileRecord(no,x,y,time);
    }
    public void set_new_dead(int no){
        blocks[no].set_new_dead();
    }
}
