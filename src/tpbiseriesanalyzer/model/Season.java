package tpbiseriesanalyzer.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class Season {

    private HashMap<Integer, Episode> episodes = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("\tEpisodes: ").append(this.episodes.size()).append("\n");

        for (Entry<Integer, Episode> episode : this.getEpisodes().entrySet()) {
            tmp.append("\t\t#[").append(episode.getKey()).append("] ").append(episode.getValue());
        }

        return tmp.toString();
    }

    public HashMap<Integer, Episode> getEpisodes() {
        return episodes;
    }

    public Episode getEpisode(int episode) {
        return episodes.get(episode);
    }

    public void addEpisode(int number, Episode episode) {
        this.episodes.put(number, episode);
    }

    public TreeMap<Float, Integer> getSimilEpisodes(Episode episode, int nb) throws Exception {
        TreeMap<Float, Integer> map = new TreeMap<>();

        for (Entry<Integer, Episode> epi : this.getEpisodes().entrySet()) {
            try {
                map.put((float) epi.getValue().getSimil(episode), epi.getKey());
            } catch (IOException ex) {
                Logger.getLogger(Season.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return map;
    }
}
