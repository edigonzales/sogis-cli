package ch.so.agi.cli;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.geojson.FeatureCollection;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.cli.model.search.Feature;
import ch.so.agi.cli.model.search.Response;
import ch.so.agi.cli.model.search.Result;

import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;

import picocli.CommandLine.Command;
import picocli.CommandLine.Help.Ansi;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Spec;

@Command(name = "oereb", description = "Search parcels by number and town and retrieve links to the plr extract.", 
    mixinStandardHelpOptions = true)
public class OerebCommand implements Callable<Integer> {
    @Spec 
    CommandSpec spec;
    
    private final static String SEARCH_SERVICE_URL = "https://geo.so.ch/api/search/v2/";
    private final static String DATA_SERVICE_URL = "https://geo.so.ch/api/data/v1/";
    private final static String OEREB_HTML_SERVICE_URL = "https://geo.so.ch/map/?oereb_egrid=";
    private final static String DATAPRODUCT_ID = "ch.so.agi.av.grundstuecke.rechtskraeftig";
    
    @Option(names = {"-q", "--query"}, required = true, description = "Search phrase.") 
    String query = "";
    
    public Integer call() {
        HttpResponse<String> response = null;
        try {
            String searchText = query;
            String encodedSearchText = URLEncoder.encode(searchText, StandardCharsets.UTF_8.toString());
            String searchRequestUrl = SEARCH_SERVICE_URL + "?searchtext="+encodedSearchText+"&filter="+DATAPRODUCT_ID+"&limit=5";
        
            HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.ALWAYS)
                    .build();
            Builder requestBuilder = HttpRequest.newBuilder();
            requestBuilder.GET().uri(URI.create(searchRequestUrl));
            
            HttpRequest request = requestBuilder.build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
   
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            
            ch.so.agi.cli.model.search.Response searchResponseObj = objectMapper.readValue(response.body(), Response.class);
            
            if (searchResponseObj.getResults().size() == 0) {
                String str = Ansi.AUTO.string(String.format("@|bold %s|@\n", "No parcel found."));
                spec.commandLine().getOut().printf(str);
                return 0;
            }
            
            Map<String,String> displayNameMap = new HashMap<String,String>();
            String dataFilter = "filter=";
            for (int i=0; i<searchResponseObj.getResults().size(); i++) {
                Result result = searchResponseObj.getResults().get(i);
                Feature feature = result.getFeature();
                String featureId = feature.getFeatureId();
                String idFieldName = feature.getIdFieldName();
                
                displayNameMap.put(featureId, feature.getDisplay());
                
                if (i==0) {
                    dataFilter += "[";
                }
                
                dataFilter += "[%22"+idFieldName+"%22,%22=%22,"+featureId+"]";
                
                if (i<searchResponseObj.getResults().size()-1) {
                    dataFilter += ",%22or%22,";
                }
                
                if (i==searchResponseObj.getResults().size()-1) {
                    dataFilter += "]";
                }
            }
            
            String dataRequestUrl = DATA_SERVICE_URL + DATAPRODUCT_ID + "/?" + dataFilter; 
            
            requestBuilder.GET().uri(URI.create(dataRequestUrl));
            request = requestBuilder.build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            FeatureCollection featureCollection = objectMapper.readValue(response.body(), FeatureCollection.class);
            for (org.geojson.Feature feature : featureCollection.getFeatures()) {
                String id = feature.getId();
                String egrid = (String) feature.getProperties().get("egrid");
                String url = OEREB_HTML_SERVICE_URL + egrid;
                
                String str = Ansi.AUTO.string(String.format("\n@|bold %s|@\n      %s\n", displayNameMap.get(id), url));
                spec.commandLine().getOut().printf(str);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
        
        return 0;
    }
}
