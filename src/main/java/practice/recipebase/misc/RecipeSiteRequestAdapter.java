package practice.recipebase.misc;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class RecipeSiteRequestAdapter {
    public static Document getRecipeSite(String URL) throws IOException {
        String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0";
        return Jsoup.connect(URL).userAgent(userAgent).get();
    }
}
