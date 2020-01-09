package model;

import static org.junit.jupiter.api.Assertions.*;

class CreatureTest {
    //测试所有可测试的辅助函数
    @org.junit.jupiter.api.Test
    public void initializeTest1() {
        Creature a=new Creature();
        assert(a.ret_has_enemy()==false);
        assert(a.ret_identity()==null);
        assert(a.ret_rate()==0);
        assert(a.isALIVE()==false);
        a.set_dead();
        assert(a.isALIVE()==false);

    }


}