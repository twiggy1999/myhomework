package Service;

import model.Creature;

public class Block {
    private double x;
    private double y;
    public Creature creature;
    //提供给Field的辅助函数，用以返回Creature的信息以及给Creature传递信息
    public void insert_creature(Creature c){
        this.creature=new Creature();
        creature=c;
        this.x=c.getLayoutX();
        this.y=c.getLayoutY();


    }
    public  Boolean ret_identity(){
        return creature.ret_identity();
    }
    public Boolean has_enemy(){
        return this.creature.ret_has_enemy();
    }
    public double ret_x(){
        if(creature.isALIVE()==true)
            return creature.getLayoutX();
        else
            return 0;
    }
    public double ret_y(){
        if(creature.isALIVE()==true)
            return creature.getLayoutY();
        else
            return 0;
    }
    public Creature ret_creature(){
        return creature;
    }
    public void set_enemy(int no){
        this.creature.set_enemy(no);

    }
    public int ret_rate(){
        return this.creature.ret_rate();
    }
    public synchronized void set_move(double x,double y){
        this.x=x;
        this.y=y;
        this.creature.change_place(x,y);

    }
    public void set_dead(){
        this.creature.set_dead();
    }
    public Boolean isALIVE(){
        return creature.isALIVE();
    }
    public void insert_record(double x,double y,long time){
       creature.insert_record(x,y,time);
    }
    public void set_new_dead(){
        creature.set_new_dead();
    }

}
