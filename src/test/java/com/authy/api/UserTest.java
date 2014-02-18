/*
 * The MIT License
 *
 * Copyright 2014 Authy Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.authy.api;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Krzysztof Suszyński <krzysztof.suszynski@wavesoftware.pl>
 */
public class UserTest {

    @Test
    public void testGetId() {
        User instance = new User();
        int result = instance.getId();
        assertEquals(0, result);
        instance.setId(5);
        assertEquals(5, instance.getId());
    }

    @Test
    public void testSetId() {
        int id = 6;
        User instance = new User();
        instance.setId(id);
        assertEquals(6, instance.id);
    }

    @Test
    public void testToXML() {
        User instance = new User();
        instance.setId(657);
        String expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><user><id>657</id></user>";
        String result = instance.toXML();
        assertEquals(expResult, result);

        instance.setId(457);
        expResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><user><id>457</id></user>";
        result = instance.toXML();
        assertEquals(expResult, result);
    }

    @Test
    public void testToMap() {
        User instance = new User(1, "some");
        instance.setId(456);
        Map<String, String> expResult = new HashMap<String, String>();
        expResult.put("content", "some");
        expResult.put("id", "456");
        expResult.put("status", "1");
        Map<String, String> result = instance.toMap();
        assertEquals(expResult, result);
    }

}
