package tpbiseriesanalyzer.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class Episode {

    private String title;
    private String description;

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        try {
            tmp.append(this.getTitle()).append(" - ").append(this.getDescription());
            tmp.append("\n\t\t\tTags: ").append(this.getLucenedTags()).append("\n");
        } catch (IOException ex) {
            Logger.getLogger(Episode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Episode.class.getName()).log(Level.SEVERE, null, ex);
        }

        return tmp.toString();
    }

    /**
     * Get description
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description
     *
     * @param String description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get title
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title
     *
     * @param String title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Return tags from a text param
     *
     * @param text
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> getTags(String text) {
        HashMap<String, Integer> list = new HashMap<>();
        for (String word : text.split("\\s*[^a-zA-Z]+\\s*")) {
            if (word.length() < 3) {
                continue;
            }

            if (list.containsKey(word)) {
                list.put(word, list.get(word) + 1);
            } else {
                list.put(word, 1);
            }
        }

        return list;
    }

    /**
     * Return tags from current description
     *
     * @return HashMap<String, Integer>
     */
    public HashMap<String, Integer> getTags() {
        return this.getTags(this.getDescription());
    }

    /**
     * Return Lucened Tags
     *
     * @return HashMap<String, Integer>
     * @throws IOException
     * @throws Exception
     */
    public HashMap<String, Integer> getLucenedTags() throws Exception {
        return this.getTags(tpbiseriesanalyzer.parser.MyAnalyzer.getLucenedText(this.getDescription()));
    }

    /**
     * Get Similitude between two Episode with Porter method
     * @param episode
     * @return
     * @throws Exception 
     */
    public double getSimil(Episode episode) throws Exception {
        int prod = 0, sum1 = 0, sum2 = 0;
        
        HashMap<String, Integer> tags1 = this.getLucenedTags();
        HashMap<String, Integer> tags2 = episode.getLucenedTags();

        HashMap<String, Integer> words = new HashMap<>();
        
        for (Entry<String, Integer> entry : tags1.entrySet()) {
            if( tags2.containsKey( entry.getKey() ) )
                prod += entry.getValue() * tags2.get(entry.getKey());
            
            sum1 += entry.getValue() * entry.getValue();
        }
        
        for (Entry<String, Integer> entry : tags2.entrySet()) {
            sum2 += entry.getValue() * entry.getValue();
        }
        
        return sum1 * sum2 == 0 ? 0 : prod / Math.sqrt(sum1 * sum2);
    }
}
