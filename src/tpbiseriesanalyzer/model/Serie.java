package tpbiseriesanalyzer.model;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class Serie implements Result {
    private String id;
    private String title;
    private String resume;
    private String url;
    private String img;

    @Override
    public String getImg() {
        return img;
    }

    @Override
    public void setImg(String img) {
        this.img = img;
    }
    private int numberOfSeasons;
    
    private HashMap<Integer, Season> seasons = new HashMap<>();
    
    @Override
    public String toString()
    {
        StringBuilder tmp = new StringBuilder();
        tmp
                .append("SERIE[")
                .append(this.getId())
                .append(" - ")
                .append(this.getTitle())
                .append("]\n")
                .append("SEASONS: ").append(this.getSeasons().size())
                .append("\n");
        
        for( Entry<Integer, Season> season : this.getSeasons().entrySet() )
            tmp.append(season.getValue()).append("\n");
        
        return tmp.toString();
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public HashMap<Integer, Season> getSeasons() {
        return seasons;
    }
    
    public Season getSeason(int season) {
        return seasons.get(season);
    }

    @Override
    public void addSeason(int number, Season season) {
        this.seasons.put(number, season);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void setNumberOfSeasons(int attr) {
        this.numberOfSeasons = attr;
    }

    @Override
    public int getNumberOfSeasons() {
        return this.numberOfSeasons;
    }

    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public void setId(String id) {
        this.id = id;
    }
}
