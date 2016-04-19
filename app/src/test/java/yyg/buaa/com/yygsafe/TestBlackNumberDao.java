package yyg.buaa.com.yygsafe;

import android.test.ActivityTestCase;

import org.junit.Test;

import java.util.List;

import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;

/**
 * Created by yyg on 2016/4/16.
 */
public class TestBlackNumberDao extends ActivityTestCase{

    @Test
    public void testAdd() {
        BlackNumberDAO dao = new BlackNumberDAO(getInstrumentation().getContext() );
//        Random random = new Random(3121);
//        for (int i = 0; i < 200; i++) {
//            long number = 133212123 + i;
//            dao.insertDao(number + "", String.valueOf(random.nextInt(3) + 1));
//        }
        boolean result = dao.insertDao("1888", "2");
        assertEquals(true, result);
    }

    public void testDelete() {
        BlackNumberDAO dao = new BlackNumberDAO(getInstrumentation().getContext());
        boolean result = dao.deleteDao("1888");
        assertEquals(true, result);
    }

    public void testUpdate() {
        BlackNumberDAO dao = new BlackNumberDAO(getInstrumentation().getContext());
        boolean result = dao.changeMode("1888", "1");
        assertEquals(true, result);
    }

    public void testFindMode() {
        BlackNumberDAO dao = new BlackNumberDAO(getInstrumentation().getContext());
        String mode = dao.queryMode("1888");
        System.out.print(mode);
    }

    public void testFindAll() {
        BlackNumberDAO dao = new BlackNumberDAO(getInstrumentation().getContext());
        List<BlackNumberInfo> infos = dao.queryAll();
        for (BlackNumberInfo info : infos) {
            System.out.print(info.getNumber() + "-----" + info.getMode());
        }
    }
}
