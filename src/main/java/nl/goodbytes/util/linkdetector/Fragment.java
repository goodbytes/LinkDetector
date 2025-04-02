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

/**
 * Representation of a fragment of text.
 *
 * Instances of this class are immutable and thus thread-safe.
 *
 * The 'startIndex' and 'endIndex' values as used in this class are compatible with those used in
 * {@link String#substring(int, int)}: the startIndex is inclusive, while the endIndex is exclusive. Both are
 * zero-based.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class Fragment
{
    /**
     * Creates a fragment that represents text.
     *
     * @param input The original text that the generated fragment is a part of.
     * @param start The index (0-based, inclusive) in the original text where the fragment begins.
     * @param end The index (0-based, exclusive) in the original text where the fragment ends.
     * @return The generated fragment
     */
    public static Fragment createText(final String input, int start, int end) {
        return createFragment(false, input, start, end);
    }

    /**
     * Creates a fragment that represents a link.
     *
     * @param input The original text that the generated fragment is a part of.
     * @param start The index (0-based, inclusive) in the original text where the fragment begins.
     * @param end The index (0-based, exclusive) in the original text where the fragment ends.
     * @return The generated fragment
     */
    public static Fragment createLink(final String input, int start, int end) {
        return createFragment(true, input, start, end);
    }

    private static Fragment createFragment(final boolean isLink, final String input, int start, int end)
    {
        if (input == null) {
            throw new IllegalArgumentException("Argument 'input' cannot be null (but was).");
        }
        if (start < 0) {
            throw new IndexOutOfBoundsException("Argument 'start' cannot be less than zero (but was). start " + start + ", end " + end + ", length " + input.length());
        }
        if (end > input.length()) {
            throw new IndexOutOfBoundsException("Argument 'end' cannot be larger than the length of the input string (but was). start " + start + ", end " + end + ", length " + input.length());
        }
        if (start > end) {
            throw new IndexOutOfBoundsException("Argument 'start' cannot be larger than argument 'end' (but was). start " + start + ", end " + end + ", length " + input.length());
        }

        return new Fragment(isLink, start, end, input.substring(start, end));
    }

    private final boolean isLink;
    private final int startIndex;
    private final int endIndex;
    private final String value;

    private Fragment(final boolean isLink, final int startIndex, final int endIndex, final String value)
    {
        this.isLink = isLink;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.value = value;
    }

    /**
     * Determines if this fragment is a representation of a link.
     *
     * @return true if this fragment represents a link, otherwise false.
     */
    public boolean isLink()
    {
        return isLink;
    }

    /**
     * Defines the position (inclusive) in the original string where this fragment starts.
     *
     * @return begin position of this fragment in the text it was derived from.
     */
    public int startIndex()
    {
        return startIndex;
    }

    /**
     * Defines the position (exclusive) in the original string where this fragment starts.
     *
     * @return the end position of this fragment in the text it was derived from.
     */
    public int endIndex()
    {
        return endIndex;
    }

    @Override
    public String toString()
    {
        return value;
    }
}
