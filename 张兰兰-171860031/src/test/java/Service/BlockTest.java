package Service;
import model.Creature;

import static org.junit.jupiter.api.Assertions.*;
public class BlockTest {
    @org.junit.jupiter.api.Test
    public void initializeTest1() {
        Block b=new Block();
        Creature c=new Creature();
        b.insert_creature(c);
        assert(b.has_enemy()==false);
        b.set_enemy(2);
        assert(b.has_enemy()==true);
        assert(b.ret_identity()==null);
        assert(b.ret_x()==0);
        assert(b.ret_y()==0);
        assert(b.ret_rate()==5);
        b.set_dead();
        assert(b.isALIVE()==false);

    }
}
