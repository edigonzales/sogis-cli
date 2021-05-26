package ch.so.agi.cli;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest.Builder;

import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name = "oereb", description = "Search parcels by number and town and retrieve links to the plr extract.", 
    mixinStandardHelpOptions = true)
public class OerebCommand implements Callable<Integer> {
    @Spec 
    CommandSpec spec;
    
    private final static String SEARCH_SERVICE_URL = "https://geo.so.ch/api/search/v2/";
    private final static String FILTER = "ch.so.agi.av.grundstuecke.rechtskraeftig";
    
    public Integer call() {
        System.out.println("Run subcommand...");

        HttpResponse<String> response = null;
        try {

            String searchText = "messen 168";
            String encodedSearchText = URLEncoder.encode(searchText, StandardCharsets.UTF_8.toString());
            String requestUrl = SEARCH_SERVICE_URL + "?searchtext="+encodedSearchText+"&filter="+FILTER+"&limit=5";
            System.out.println(requestUrl);
        
            HttpClient httpClient = HttpClient.newBuilder().version(Version.HTTP_1_1).followRedirects(Redirect.ALWAYS)
                    .build();
            Builder requestBuilder = HttpRequest.newBuilder();
            requestBuilder.GET().uri(URI.create(requestUrl));
            
            HttpRequest request = requestBuilder.build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            // TODO: anschliessender Request mit einem AND-filter? In diesem speziellen Fall
            // gibt es nur ein dataset, das man durchsuchen muss.
            // Wenn wir auch noch die Adressen verwenden würden, gäbe es halt zwei.
            
            // display verwenden für resultate-anzeige.
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 1;
        }
        System.out.println(response.body());
        
        System.out.println("Hallo ÖREB");
        
        spec.commandLine().getOut().println("Your output is abc...");
        
        return 0;
    }
}
