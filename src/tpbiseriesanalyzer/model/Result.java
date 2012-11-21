package tpbiseriesanalyzer.model;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public interface Result {

    public void   setTitle(String html);
    public String getTitle();

    public void   setUrl(String attr);
    public String getUrl();

    public void setNumberOfSeasons(int attr);
    public int  getNumberOfSeasons();

    public void addSeason(int number, Season season);

    public void   setImg(String attr);
    public String getImg();

    public String getId();
    public void   setId(String id);
}
