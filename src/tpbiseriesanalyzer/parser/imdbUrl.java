package tpbiseriesanalyzer.parser;

/**
 *
 * @author Olivares Georges <dev@olivares-georges.fr>
 */
public class imdbUrl implements Cloneable {

    String path = "title/tt{id}";
    String page = "";
    String query = "";
    int id;

    public imdbUrl(String id) {
        this.id = Integer.parseInt(id.replace("id", ""));
    }

    public imdbUrl(String id, String page) {
        this(id);
        this.page = page;
    }

    public imdbUrl(String id, String page, String query) {
        this(id, page);
        this.query = query;
    }

    public String getId() {
        return String.format("%07d", this.id);
    }

    public String getPath() {
        return this.path.replace("{id}", this.getId());
    }

    public String getFilename() {
        return this.page;
    }

    public imdbUrl setFilename(String page) {
        this.page = page;
        return this;
    }

    public String getFilename(String def) {
        return this.page.isEmpty() ? def : this.page;
    }

    public String getUri() {
        return "http://www.imdb.com/" + this.getPath() + "/";
    }

    public String getQuery() {
        return this.query.isEmpty() ? "" : "?" + this.query;
    }

    public imdbUrl setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getUrl() {
        return (this.getUri() + this.getFilename() + this.getQuery()).replace("{id}", this.getId());
    }

    @Override
    public String toString() {
        return this.getUrl();
    }

    @Override
    public imdbUrl clone() {
        imdbUrl o = null;
        try {
            o = (imdbUrl) super.clone();
        } catch (CloneNotSupportedException cnse) {
            cnse.printStackTrace(System.err);
        }

        return o;
    }
}
