package tpbiseriesanalyzer.parser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

/**
 * Example of analyser which simplifies the summaries
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class MyAnalyzer extends Analyzer {

    final Set<String> stopWords;

    public MyAnalyzer() {
        String[] abc = {"when", "while", "who", "thi", "up", "have",
            "don", "ha", "hi", "him", "so", "out", "an", "that", "is",
            "in", "the", "he", "she", "her", "s", "i", "m", "t",
            "after", "from", "all", "can", "do", "which", "doesn", "go", "of", "to", "a", "but", "no", "yes", "at", "on", "form", "for", "they", "with", "and", "one", "two", "ask", "also", "not", "did", "into"};
        stopWords = new HashSet<>(Arrays.asList(abc));
    }

    @Override
    public final TokenStream tokenStream(String fieldName, Reader reader) {
        TokenStream ts = new LowerCaseTokenizer(Version.LUCENE_36, reader);
        ts = new StandardFilter(Version.LUCENE_36, ts);
        ts = new PorterStemFilter(ts);
        ts = new StopFilter(Version.LUCENE_36, ts, stopWords);
        return ts;
    }
    
    public static String getLucenedText(String text) throws Exception  {
        Reader reader = new StringReader(text);
        Analyzer a = new MyAnalyzer();

        TokenStream ts = a.tokenStream(text, reader);
        boolean hasnext = ts.incrementToken();

        StringBuilder newSummary = new StringBuilder();
        while (hasnext) {
            CharTermAttribute ta = ts.getAttribute(CharTermAttribute.class);
            newSummary.append(ta.toString()).append(" ");
            hasnext = ts.incrementToken();
        }

        return newSummary.toString();
    }
}

