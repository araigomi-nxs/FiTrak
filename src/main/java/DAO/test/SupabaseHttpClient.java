package DAO.test;

import DAO.Config;
import okhttp3.*;
import java.io.IOException;
import java.util.Map;

public class SupabaseHttpClient {
    private final OkHttpClient client = new OkHttpClient();

    // Replace with your project and keys
    private final String baseUrl = Config.get("SUPABASE_URL");
    private final String apiKey  = Config.get("SUPABASE_APIKEY");

    private Request.Builder base(String path) {
        return new Request.Builder()
                .url(baseUrl + path)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");
    }

    public Response get(String pathAndQuery) throws IOException {
        Request req = base(pathAndQuery).get().build();
        return client.newCall(req).execute();
    }

    public Response post(String path, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request req = base(path).post(body).build();
        return client.newCall(req).execute();
    }

    public Response patch(String pathAndQuery, String json) throws IOException {
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request req = base(pathAndQuery).patch(body).build();
        return client.newCall(req).execute();
    }

    public Response delete(String pathAndQuery) throws IOException {
        Request req = base(pathAndQuery).delete().build();
        return client.newCall(req).execute();
    }
}
