package yyg.buaa.com.yygsafe;

import android.test.AndroidTestCase;

import java.util.List;

import yyg.buaa.com.yygsafe.db.dao.BlackNumberDAO;
import yyg.buaa.com.yygsafe.domain.BlackNumberInfo;

/**
 * Created by yyg on 2016/4/16.
 */
public class TestBlackNumberDao extends AndroidTestCase {

    public void testAdd() {
        BlackNumberDAO dao = new BlackNumberDAO(getContext());
        boolean result = dao.insertDao("1888", "2");
        assertEquals(true, result);
    }

    public void testDelete() {
        BlackNumberDAO dao = new BlackNumberDAO(getContext());
        boolean result = dao.deleteDao("1888");
        assertEquals(true, result);
    }

    public void testUpdate() {
        BlackNumberDAO dao = new BlackNumberDAO(getContext());
        boolean result = dao.changeMode("1888", "1");
        assertEquals(true, result);
    }

    public void testFindMode() {
        BlackNumberDAO dao = new BlackNumberDAO(getContext());
        String mode = dao.queryMode("1888");
        System.out.print(mode);
    }

    public void testFindAll() {
        BlackNumberDAO dao = new BlackNumberDAO(getContext());
        List<BlackNumberInfo> infos = dao.queryAll();
        for (BlackNumberInfo info : infos) {
            System.out.print(info.getNumber() + "-----" + info.getMode());
        }
    }
}
