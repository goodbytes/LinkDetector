/*
 * Copyright 2025 Guus der Kinderen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.goodbytes.util.linkdetector;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;

/**
 * Unit tests that verify the implementation of {@link LinkDetector}.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class LinkDetectorTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testNull() throws Exception
    {
        // Setup test fixture.
        final String input = null;

        // Execute system under test.
        LinkDetector.parse(input);
    }

    @Test
    public void testEmpty() throws Exception
    {
        // Setup test fixture.
        final String input = "";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(0, fragments.size());
    }

    @Test
    public void testText() throws Exception
    {
        // Setup test fixture.
        final String input = "foobar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(1, fragments.size());
        assertEquals(0, fragments.get(0).startIndex());
        assertEquals(input.length(), fragments.get(0).endIndex());
        assertEquals(input, fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());
    }

    @Test
    public void testLink() throws Exception
    {
        // Setup test fixture.
        final String input = "https://www.example.org";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(1, fragments.size());
        assertEquals(0, fragments.get(0).startIndex());
        assertEquals(input.length(), fragments.get(0).endIndex());
        assertEquals(input, fragments.get(0).toString());
        assertTrue(fragments.get(0).isLink());
    }

    @Test
    public void testCaseSensitivity() throws Exception
    {
        // Setup test fixture.
        final String input = "HTTPS://www.example.org";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(1, fragments.size());
        assertEquals(0, fragments.get(0).startIndex());
        assertEquals(input.length(), fragments.get(0).endIndex());
        assertEquals(input, fragments.get(0).toString());
        assertTrue(fragments.get(0).isLink());
    }

    @Test
    public void testRequiredDoubleSlash() throws Exception
    {
        // Setup test fixture.
        final String input = "https:www.example.org";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(1, fragments.size());
        assertEquals(0, fragments.get(0).startIndex());
        assertEquals(input.length(), fragments.get(0).endIndex());
        assertEquals(input, fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());
    }

    @Test
    public void testEmbeddedLink() throws Exception
    {
        // Setup test fixture.
        final String input = "Foo https://www.example.org bar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("Foo ".length(), fragments.get(0).endIndex());
        assertEquals("Foo ", fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());

        assertEquals("Foo ".length(), fragments.get(1).startIndex());
        assertEquals("Foo https://www.example.org".length(), fragments.get(1).endIndex());
        assertEquals("https://www.example.org", fragments.get(1).toString());
        assertTrue(fragments.get(1).isLink());

        assertEquals("Foo https://www.example.org".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals(" bar", fragments.get(2).toString());
        assertFalse(fragments.get(2).isLink());
    }

    @Test
    public void testEmbeddedText() throws Exception
    {
        // Setup test fixture.
        final String input = "https://www.example.org bar https://example.com";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("https://www.example.org".length(), fragments.get(0).endIndex());
        assertEquals("https://www.example.org", fragments.get(0).toString());
        assertTrue(fragments.get(0).isLink());

        assertEquals("https://www.example.org".length(), fragments.get(1).startIndex());
        assertEquals("https://www.example.org bar ".length(), fragments.get(1).endIndex());
        assertEquals(" bar ", fragments.get(1).toString());
        assertFalse(fragments.get(1).isLink());

        assertEquals("https://www.example.org bar ".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals("https://example.com", fragments.get(2).toString());
        assertTrue(fragments.get(2).isLink());
    }

    @Test
    public void testEmbeddedLinkWithParenthesis() throws Exception
    {
        // Setup test fixture.
        final String input = "Foo https://www.example.org/foo_(bar) bar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("Foo ".length(), fragments.get(0).endIndex());
        assertEquals("Foo ", fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());

        assertEquals("Foo ".length(), fragments.get(1).startIndex());
        assertEquals("Foo https://www.example.org/foo_(bar)".length(), fragments.get(1).endIndex());
        assertEquals("https://www.example.org/foo_(bar)", fragments.get(1).toString());
        assertTrue(fragments.get(1).isLink());

        assertEquals("Foo https://www.example.org/foo_(bar)".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals(" bar", fragments.get(2).toString());
        assertFalse(fragments.get(2).isLink());
    }

    @Test
    public void testEmbeddedLinkInParenthesis() throws Exception
    {
        // Setup test fixture.
        final String input = "Foo (https://www.example.org/foo_(bar)) bar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("Foo (".length(), fragments.get(0).endIndex());
        assertEquals("Foo (", fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());

        assertEquals("Foo (".length(), fragments.get(1).startIndex());
        assertEquals("Foo (https://www.example.org/foo_(bar)".length(), fragments.get(1).endIndex());
        assertEquals("https://www.example.org/foo_(bar)", fragments.get(1).toString());
        assertTrue(fragments.get(1).isLink());

        assertEquals("Foo (https://www.example.org/foo_(bar)".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals(") bar", fragments.get(2).toString());
        assertFalse(fragments.get(2).isLink());
    }

    @Test
    public void testEmbeddedLinkWithAndInParenthesis() throws Exception
    {
        // Setup test fixture.
        final String input = "Foo (https://www.example.org) bar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("Foo (".length(), fragments.get(0).endIndex());
        assertEquals("Foo (", fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());

        assertEquals("Foo (".length(), fragments.get(1).startIndex());
        assertEquals("Foo (https://www.example.org".length(), fragments.get(1).endIndex());
        assertEquals("https://www.example.org", fragments.get(1).toString());
        assertTrue(fragments.get(1).isLink());

        assertEquals("Foo (https://www.example.org".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals(") bar", fragments.get(2).toString());
        assertFalse(fragments.get(2).isLink());
    }

    @Test
    public void testEmbeddedLinkFollowedByComma() throws Exception
    {
        // Setup test fixture.
        final String input = "Foo https://www.example.org, bar";

        // Execute system under test.
        final List<Fragment> fragments = LinkDetector.parse(input);

        // Verify results.
        assertEquals(3, fragments.size());

        assertEquals(0, fragments.get(0).startIndex());
        assertEquals("Foo ".length(), fragments.get(0).endIndex());
        assertEquals("Foo ", fragments.get(0).toString());
        assertFalse(fragments.get(0).isLink());

        assertEquals("Foo ".length(), fragments.get(1).startIndex());
        assertEquals("Foo https://www.example.org".length(), fragments.get(1).endIndex());
        assertEquals("https://www.example.org", fragments.get(1).toString());
        assertTrue(fragments.get(1).isLink());

        assertEquals("Foo https://www.example.org".length(), fragments.get(2).startIndex());
        assertEquals(input.length(), fragments.get(2).endIndex());
        assertEquals(", bar", fragments.get(2).toString());
        assertFalse(fragments.get(2).isLink());
    }
}
