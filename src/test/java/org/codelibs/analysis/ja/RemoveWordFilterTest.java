/*
 * Copyright 2009-2015 the CodeLibs Project and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.codelibs.analysis.ja;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.BaseTokenStreamTestCase;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.junit.Test;

public class RemoveWordFilterTest extends BaseTokenStreamTestCase {

    @Test
    public void testBasic() throws IOException {
        List<String> list = new ArrayList<>();
        list.add("bbb");
        list.add("ddd");
        final CharArraySet words = new CharArraySet(list, false);
        Analyzer analyzer = new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
                final Tokenizer tokenizer = new WhitespaceTokenizer(reader);
                return new TokenStreamComponents(tokenizer, new RemoveWordFilter(tokenizer, words));
            }
        };

        assertAnalyzesTo(analyzer, "aaa bbb ccc ddd eee", //
                new String[] { "aaa", "ccc", "eee" }, //
                new int[] { 0, 8, 16 },//
                new int[] { 3, 11, 19 },//
                new int[] { 1, 2, 2 });
        assertAnalyzesTo(analyzer, "aaa", //
                new String[] { "aaa" });
        assertAnalyzesTo(analyzer, "bbb", //
                new String[0]);

    }

}