package Service;
import model.Creature;

import static org.junit.jupiter.api.Assertions.*;
public class FeildTest {
    @org.junit.jupiter.api.Test
    public void initializeTest1() {
        Field f=new Field();
        Creature c=new Creature();
        f.insert_creature(c);
        assert(f.ret_x(0)==0);
        assert(f.ret_y(0)==0);
        assert(f.ret_identity(0)==null);
        f.set_dead(0);
        assert(f.isALIVE(0)==false);


    }
}
