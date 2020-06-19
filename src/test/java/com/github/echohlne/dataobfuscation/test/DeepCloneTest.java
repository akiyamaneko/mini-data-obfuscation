package com.github.echohlne.dataobfuscation.test;


import com.github.echohlne.dataobfuscation.utils.CloneUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeepCloneTest {
    @Test
    public void testDeepCopyWithArray() {
        String[] src = {"hello", "world"};
        String[] dst = CloneUtils.deepClone(src);
        assertNotEquals(src, dst);
        assertEquals(src[0], dst[0]);
        assertEquals(src[1], dst[1]);
        assertNotSame(src, dst);
        src[0] = "hello1";
        assertNotEquals(src[0], dst[0]);
    }

    @Test
    public void testDeepCopyWithPrimitive() {
        int a = 12;
        int b = CloneUtils.deepClone(a);
        assertEquals(a, b);
    }
}
