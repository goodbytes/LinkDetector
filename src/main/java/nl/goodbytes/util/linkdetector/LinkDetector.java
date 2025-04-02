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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility that parses text for links.
 *
 * Instances of this class are thread-safe.
 *
 * The implementation is based on a regex pattern provided by Wiktor Kwapisiewicz.
 *
 * @author Guus der Kinderen, guus@goodbytes.nl
 */
public class LinkDetector
{
    final static Pattern pattern = Pattern.compile("\\b(?:https?|ftp):\\/\\/(([a-z0-9-+&@#\\/%?=~_|!:,.;]*\\([a-z0-9-+&@#\\/%?=~_|!:,.;]*(\\)[a-z0-9-+&@#\\/%?=~_|!:,.;]*[a-z0-9-+&@#\\/%=~_|]|[a-z0-9-+&@#\\/%=~_|)]))|([a-z0-9-+&@#\\/%?=~_|!:,.;]*[a-z0-9-+&@#\\/%=~_|]))", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    /**
     * Splits the provided input text in fragments that either are or are not links.
     *
     * The returned collection contains fragments that, combined, represent the entire input.
     *
     * When the provided input is an empty string (containing no characters) then the return value is an empty collection.
     *
     * @param input The text to split up.
     * @return A collection of fragments.
     */
    public static List<Fragment> parse(final String input)
    {
        if (input == null) {
            throw new IllegalArgumentException("Argument 'input' cannot be null (but was).");
        }
        final List<Fragment> result = new ArrayList<>();

        final Matcher matcher = pattern.matcher(input);
        int needle = 0;
        while (matcher.find())
        {
            final MatchResult matchResult = matcher.toMatchResult();

            // Text leading up to the match is regular text.
            if (matchResult.start() > needle) {
                result.add( Fragment.createText(input, needle, matchResult.start()) );
            }

            // The match itself is a link.
            result.add( Fragment.createLink(input, matchResult.start(), matchResult.end()) );
            needle = matchResult.end();
        }

        // Text after the last match up to the end of the input is regular text.
        if (needle < input.length()) {
            result.add( Fragment.createText(input, needle, input.length()) );
        }

        return result;
    }
}
