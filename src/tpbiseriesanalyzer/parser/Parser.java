package tpbiseriesanalyzer.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tpbiseriesanalyzer.model.Episode;
import tpbiseriesanalyzer.model.Result;
import tpbiseriesanalyzer.model.Season;
import tpbiseriesanalyzer.model.Serie;
import tpbiseriesanalyzer.utils.MD5;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class Parser {

    private String url;
    private boolean isHttp;

    public Parser(String url) {
        this.url = url;
        this.isHttp = this.url.contains("http:");
    }

    public Result parse() throws MalformedURLException, IOException, NoSuchAlgorithmException {

        Document page = this.getFile("serie", this.url);

        // Parse document
        Document doc = this.getFile("episode", this.url, "episodes?season=1");
        Elements docMain = doc.select("div#main");

        // New Result = SERIE
        Result result = new Serie();

        result.setId(page.select("head meta[property=og:url]").attr("content").replaceAll("http.*/(tt[0-9]+)/?.*", "$1"));
        result.setImg(page.select("img[itemprop=image]").attr("src"));
        // Set Result datas
        result.setTitle(doc.select("head meta[property=og:title]").attr("content"));
        result.setUrl(docMain.select("h3[itemprop=name] a").attr("href"));

        // @TODO setNumberOfSeasons(String) -> Integer.parseInt
        result.setNumberOfSeasons(Integer.parseInt(docMain.select("select#bySeason option").last().attr("value")));

        Episode first = null;

        for (Element seasonElement : docMain.select("select#bySeason option")) {
            Document docSeason = this.getFile("episode", this.url, "episodes?season=" + seasonElement.attr("value"));
            Elements docSeasonMain = docSeason.select("div#main");

            // New Result -> SEASON
            Season season = new Season();
            // Add to RESULT
            result.addSeason(Integer.parseInt(seasonElement.attr("value")), season);

            for (Element episodeElement : docSeasonMain.select("div[itemprop=episodes]")) {

                int numberEpisode = Integer.parseInt(episodeElement.select("meta[itemprop=episodeNumber]").attr("content"));

                Episode episode = new Episode();
                episode.setTitle(episodeElement.select("a[itemprop=name]").text());
                episode.setDescription(episodeElement.select("div[itemprop=description]").text());

                season.addEpisode(numberEpisode, episode);
            }
        }

        //System.out.println( docMain.outerHtml() );

        return result;
    }

    /*private Document getFile(String url) throws IOException, NoSuchAlgorithmException {
        return this.getFile("", url);
    }*/

    private Document getFile(String prefix, String url) throws IOException, NoSuchAlgorithmException {
        return this.getFile(prefix, url, "");
    }

    /**
     * Return cached file (and download it if necessary)
     * @param String prefix
     * @param String url
     * @param String path
     * @return Document
     * @throws IOException
     * @throws NoSuchAlgorithmException 
     */
    private Document getFile(String prefix, String url, String path) throws IOException, NoSuchAlgorithmException {

        if (!prefix.isEmpty()) {
            prefix = prefix + "-";
        }

        if (new File("cache").mkdir()) {
            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Dir 'cache' created");
        }

        File file = new File("cache/" + prefix + MD5.MD5(url + path) + ".cache.html");

        if (file.exists() || !this.isHttp) {
            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Return cache for {0}", prefix + url + path);
            return Jsoup.parse(file, "UTF-8");
        }


        Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Downloading ... {0}", url + path);

        Document jsoup = Jsoup.connect(url + path).userAgent("Mozilla").get();
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()))) {
            out.write(jsoup.outerHtml());

            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Write cache ... ");
        }
        return jsoup;
    }

    private Document getFile(File file) throws IOException {
        return Jsoup.parse(file, "UTF-8");
    }
}
