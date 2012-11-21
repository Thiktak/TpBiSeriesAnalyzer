package tpbiseriesanalyzer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import tpbiseriesanalyzer.model.Result;
import tpbiseriesanalyzer.parser.Parser2;
import tpbiseriesanalyzer.parser.Process;
import tpbiseriesanalyzer.parser.imdbUrl;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class TpBiSeriesAnalyzer {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws MalformedURLException, IOException, NoSuchAlgorithmException, Exception {
        Process.main(new String[] {});
    }
    
    static public Result getResult(String source) throws Exception {
        Parser2 crawler = new Parser2(new imdbUrl(source));
        return crawler.parse();
    }
    
    static public File[] findSeries() {
        return TpBiSeriesAnalyzer.findFiles(new File("cache/title/"));
    }
    
    /**
     * find all directory cache/tt([0-9]+)/ (= Serie)
     * @param root
     * @return File[]
     */
    static public File[] findFiles(File root) {
        return root.listFiles(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();// && f.getName().contains(type);
            }});
    }
}