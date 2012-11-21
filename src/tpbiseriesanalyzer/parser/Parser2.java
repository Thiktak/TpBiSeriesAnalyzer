package tpbiseriesanalyzer.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
public class Parser2 {

    protected imdbUrl url;

    public Parser2(imdbUrl url) throws Exception {
        this.url = url;
    }

    public Document getFile(imdbUrl url) throws Exception {
        if (new File("cache/" + url.getPath() + "/" + url.getFilename()).mkdirs()) {
            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Dir 'cache' created");
        }

        // On crée le fichier de cache
        File file = new File("cache/" + url.getPath() + "/" + url.getFilename() + "/" + MD5.MD5(url.toString()) + ".cache.html");

        if (file.exists()) {
            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Return cache for {0}", url.toString());
            return Jsoup.parse(file, "UTF-8");
        }

        Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Downloading ... {0}", url.toString());

        Document jsoup = Jsoup.connect(url.toString()).userAgent("Mozilla").get();

        try (BufferedWriter out = new BufferedWriter(new FileWriter(file.getPath()))) {
            out.write(jsoup.outerHtml());
            Logger.getLogger(Parser.class.getName()).log(Level.INFO, "Write cache for {0}", url.toString());
        }

        return jsoup;
    }

    public Result parse() throws Exception {
        Document page = this.getFile(this.url);

        imdbUrl url2 = this.url.clone();

        // Parse document
        Document doc = this.getFile(url2.setFilename("episodes").setQuery("season=1"));
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
            Document docSeason = this.getFile(url2.setFilename("episodes").setQuery("season=" + seasonElement.attr("value")));
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

                /*
                 * if (first == null) { first = episode; System.out.println("\n
                 * Simil = " + episode.getSimil(first)); } else {
                 * System.out.println("\n Simil = " + episode.getSimil(first));
                }
                 */

                season.addEpisode(numberEpisode, episode);

            }
        }

        return result;
    }
}
