package tpbiseriesanalyzer.parser;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class Process {

    public Process(String UmdbId) throws Exception {
        Parser2 parser = new Parser2(new imdbUrl(UmdbId));

        System.out.println(parser.parse());
        //Document document = parser.getDocument();
    }

    public static void main(String[] args) throws Exception {
        Process process = new Process("0944947");
    }
}
