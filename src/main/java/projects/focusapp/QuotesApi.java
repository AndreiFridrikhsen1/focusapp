package projects.focusapp;
import com.google.gson.Gson;
import java.io.IOException;

public class QuotesApi extends APIUtility {
    public Quote getQuote() throws IOException, InterruptedException {
        String json = sendReq("http://api.forismatic.com/api/1.0/?method=getQuote&format=json&lang=en");
        Gson gson = new Gson();
        return gson.fromJson(json,Quote.class);
    }
}
