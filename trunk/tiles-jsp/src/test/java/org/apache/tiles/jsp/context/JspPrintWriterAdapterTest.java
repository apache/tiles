/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.tiles.jsp.context;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

import org.easymock.classextension.EasyMock;

import junit.framework.TestCase;

/**
 * Tests {@link JspPrintWriterAdapter}.
 *
 * @version $Rev$ $Date$
 */
public class JspPrintWriterAdapterTest extends TestCase {

    /**
     * The string length.
     */
    private static final int STRING_LENGTH = 10;

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#write(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.write(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.write(1);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#write(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArray() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(EasyMock.aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.write(result.toCharArray());
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#write(char[], int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArrayIntInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(EasyMock.aryEq(result.toCharArray()), EasyMock.eq(0),
                EasyMock.eq(STRING_LENGTH));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.write(result.toCharArray(), 0, STRING_LENGTH);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#flush()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testFlush() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.flush();
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#close()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testClose() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.close();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.close();
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintBoolean() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print(true);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(true);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintChar() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print('c');
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print('c');
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(1);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintLong() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print(1L);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(1L);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintFloat() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print(1f);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(1f);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintDouble() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print(1d);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(1d);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintCharArray() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.print(EasyMock.aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(result.toCharArray());
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintln() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println();
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnBoolean() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println(true);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(true);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnChar() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println('c');
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println('c');
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(1);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnLong() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println(1L);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(1L);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnFloat() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println(1f);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(1f);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnDouble() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println(1d);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(1d);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnCharArray() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.println(EasyMock.aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(result.toCharArray());
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#getJspWriter()}.
     */
    public void testGetJspWriter() {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        assertEquals(writer, adapter.getJspWriter());
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#append(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendChar() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        EasyMock.expect(writer.append('c')).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        assertEquals(adapter, adapter.append('c'));
        EasyMock.verify(writer);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#append(java.lang.CharSequence, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequenceIntInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        CharSequence sequence = EasyMock.createMock(CharSequence.class);
        EasyMock.expect(writer.append(sequence, 0, STRING_LENGTH)).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        assertEquals(adapter, adapter.append(sequence, 0, STRING_LENGTH));
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#append(java.lang.CharSequence)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequence() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        CharSequence sequence = EasyMock.createMock(CharSequence.class);
        EasyMock.expect(writer.append(sequence)).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        assertEquals(adapter, adapter.append(sequence));
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintObject() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        Object obj = EasyMock.createMock(Object.class);
        writer.print(obj);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print(obj);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#print(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintString() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.print("this is a string");
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.print("this is a string");
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnObject() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        Object obj = EasyMock.createMock(Object.class);
        writer.println(obj);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println(obj);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#println(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnString() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        writer.println("this is a string");
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.println("this is a string");
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#write(java.lang.String, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteStringIntInt() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result, 0, STRING_LENGTH);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.write(result, 0, STRING_LENGTH);
        EasyMock.verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.jsp.context.JspPrintWriterAdapter#write(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteString() throws IOException {
        JspWriter writer = EasyMock.createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        EasyMock.replay(writer);
        adapter.write(result);
        EasyMock.verify(writer);
    }
}
