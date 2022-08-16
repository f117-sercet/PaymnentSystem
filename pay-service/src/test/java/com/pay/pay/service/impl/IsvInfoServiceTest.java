package com.pay.pay.service.impl;

import org.junit.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Description： TODO
 *
 * @author: 段世超
 * @aate: Created in 2022/8/16 16:32
 */
public class IsvInfoServiceTest {

    @Test
    public void test01() {

        Map<String, String> map = new HashMap<>();
        map.put("凯爹", "暗影战斧");
        map.put("马克", "纯净苍穹");
        map.put("妲己", "回响之杖");
        map.put("典韦", "宗师之力");

        /*********keyset*******/

        System.out.println("-----keySet部分-----");
        Set<String> keySet = map.keySet();
        System.out.println(keySet);

        System.out.println(/************foreach*************/);
        for (String map1 : keySet) {

            String key = keySet.toString();
        }

        System.out.println("-----iterator-----");
        Iterator<String> iterator = keySet.iterator();
        for (keySet.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            String value = map.get(key);
            System.out.println(" 键= " + key + " -> " + "值 = " + value);
        }
        System.out.println("-----keySet部分-----");
        Set<Map.Entry<String, String>> entrySet = map.entrySet();
        System.out.println(entrySet);

    }
        }