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
package org.apache.tiles.request.jsp;

import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;

import javax.servlet.jsp.JspWriter;

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
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.write(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(1);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArray() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result.toCharArray());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(char[], int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArrayIntInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(aryEq(result.toCharArray()), eq(0),
                eq(STRING_LENGTH));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result.toCharArray(), 0, STRING_LENGTH);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#flush()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testFlush() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.flush();
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#close()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testClose() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.close();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.close();
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintBoolean() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(true);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(true);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintChar() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print('c');
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print('c');
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintLong() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1L);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1L);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintFloat() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1f);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1f);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintDouble() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1d);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1d);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintCharArray() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.print(aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(result.toCharArray());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintln() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println();
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnBoolean() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(true);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(true);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnChar() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println('c');
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println('c');
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnLong() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1L);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1L);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnFloat() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1f);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1f);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnDouble() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1d);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1d);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnCharArray() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.println(aryEq(result.toCharArray()));
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(result.toCharArray());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#getJspWriter()}.
     */
    public void testGetJspWriter() {
        JspWriter writer = createMock(JspWriter.class);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        assertEquals(writer, adapter.getJspWriter());
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendChar() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        expect(writer.append('c')).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append('c'));
        verify(writer);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(java.lang.CharSequence, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequenceIntInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        CharSequence sequence = createMock(CharSequence.class);
        expect(writer.append(sequence, 0, STRING_LENGTH)).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append(sequence, 0, STRING_LENGTH));
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(java.lang.CharSequence)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequence() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        CharSequence sequence = createMock(CharSequence.class);
        expect(writer.append(sequence)).andReturn(writer);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append(sequence));
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintObject() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        Object obj = createMock(Object.class);
        writer.print(obj);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(obj);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintString() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print("this is a string");
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print("this is a string");
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnObject() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        Object obj = createMock(Object.class);
        writer.println(obj);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(obj);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnString() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println("this is a string");
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println("this is a string");
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(java.lang.String, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteStringIntInt() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result, 0, STRING_LENGTH);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result, 0, STRING_LENGTH);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteString() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result);
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result);
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.write(1);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(1);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArrayEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(aryEq(result.toCharArray()));
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result.toCharArray());
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(char[], int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteCharArrayIntIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(aryEq(result.toCharArray()), eq(0),
                eq(STRING_LENGTH));
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result.toCharArray(), 0, STRING_LENGTH);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#flush()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testFlushEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.flush();
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.flush();
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#close()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testCloseEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.close();
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.close();
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintBooleanEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(true);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(true);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintCharEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print('c');
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print('c');
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintLongEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1L);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1L);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintFloatEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1f);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1f);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintDoubleEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print(1d);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(1d);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintCharArrayEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.print(aryEq(result.toCharArray()));
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(result.toCharArray());
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println()}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println();
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println();
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(boolean)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnBooleanEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(true);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(true);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnCharEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println('c');
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println('c');
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(long)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnLongEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1L);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1L);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(float)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnFloatEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1f);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1f);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(double)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnDoubleEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println(1d);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(1d);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(char[])}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnCharArrayEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.println(aryEq(result.toCharArray()));
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(result.toCharArray());
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(char)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        expect(writer.append('c')).andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append('c'));
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for
     * {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(java.lang.CharSequence, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequenceIntIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        CharSequence sequence = createMock(CharSequence.class);
        expect(writer.append(sequence, 0, STRING_LENGTH)).andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append(sequence, 0, STRING_LENGTH));
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#append(java.lang.CharSequence)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testAppendCharSequenceEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        CharSequence sequence = createMock(CharSequence.class);
        expect(writer.append(sequence)).andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        assertEquals(adapter, adapter.append(sequence));
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintObjectEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        Object obj = createMock(Object.class);
        writer.print(obj);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print(obj);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#print(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintStringEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.print("this is a string");
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.print("this is a string");
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(java.lang.Object)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnObjectEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        Object obj = createMock(Object.class);
        writer.println(obj);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println(obj);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#println(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testPrintlnStringEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        writer.println("this is a string");
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.println("this is a string");
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(java.lang.String, int, int)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteStringIntIntEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result, 0, STRING_LENGTH);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result, 0, STRING_LENGTH);
        assertTrue(adapter.checkError());
        verify(writer);
    }

    /**
     * Test method for {@link org.apache.tiles.request.jsp.JspPrintWriterAdapter#write(java.lang.String)}.
     *
     * @throws IOException If something goes wrong.
     */
    public void testWriteStringEx() throws IOException {
        JspWriter writer = createMock(JspWriter.class);
        String result = "this is a test";
        writer.write(result);
        expectLastCall().andThrow(new IOException());
        writer.flush();
        JspPrintWriterAdapter adapter = new JspPrintWriterAdapter(writer);
        replay(writer);
        adapter.write(result);
        assertTrue(adapter.checkError());
        verify(writer);
    }
}
