import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class PageEntry implements Comparable<PageEntry>, Serializable {
    private static final long serialVersionUID = 3L;
    private final String pdfName;
    private final int page;
    // private final int count;
    private int count;

    public PageEntry(String pdfName, int page, int count) {
        this.pdfName = pdfName;
        this.page = page;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int compareTo(PageEntry pageEntry) {
        // сначала анализ количества слов, потом название
        //не понимаю, почему в примере название 1. DevOps_MLops.pdf считается больше названий по русски...
        if (this.count - pageEntry.count != 0) {
            return this.count - pageEntry.count;
        } else {
            return this.pdfName.compareTo(pageEntry.pdfName);
        }

    }

    @Override
    public String toString() {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        return "\n" + gson.toJson(this);
   }
}
