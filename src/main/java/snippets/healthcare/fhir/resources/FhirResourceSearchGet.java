/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package snippets.healthcare.fhir.resources;

// [START healthcare_search_resources_get]
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.healthcare.v1.CloudHealthcare;
import com.google.api.services.healthcare.v1.CloudHealthcareScopes;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class FhirResourceSearchGet {
  private static final String FHIR_NAME =
      "projects/%s/locations/%s/datasets/%s/fhirStores/%s/fhir/%s";
  // The endpoint URL for the Healthcare API. Required for HttpClient.
  private static final String API_ENDPOINT = "https://healthcare.googleapis.com";
  private static final JsonFactory JSON_FACTORY = new JacksonFactory();
  private static final NetHttpTransport HTTP_TRANSPORT = new NetHttpTransport();

  public static void fhirResourceSearchGet(String resourceName)
      throws IOException, URISyntaxException {
    // String resourceName =
    //    String.format(
    //        FHIR_NAME, "project-id", "region-id", "dataset-id", "fhir-store-id");
    // String resourceType = "Patient";

    // Instantiate the client, which will be used to interact with the service.
    HttpClient httpClient = HttpClients.createDefault();
    String uri = String.format("%s/v1/%s", API_ENDPOINT, resourceName);
    URIBuilder uriBuilder = new URIBuilder(uri).setParameter("access_token", getAccessToken());
    // To set additional parameters for search filtering, add them to the URIBuilder. For
    // example, to search for a Patient with the family name "Smith", specify the following:
    // uriBuilder.setParameter("family:exact", "Smith");

    HttpUriRequest request =
        RequestBuilder.get()
            .setUri(uriBuilder.build())
            .build();

    // Execute the request and process the results.
    HttpResponse response = httpClient.execute(request);
    HttpEntity entity = response.getEntity();

    if (entity != null) {
      String retSrc = EntityUtils.toString(entity);
      // parsing JSON
      JSONObject result = new JSONObject(retSrc);

     JSONArray a = (JSONArray) result.get("entry");//Convert String to JSON Object

      for(int i=0;i<a.length();i++) {
        JSONObject l1 = (JSONObject) a.get(i);
        System.out.println(l1.get("fullUrl"));
       /* URIBuilder uriBuilder1 = new URIBuilder("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient/c34df70e-a9ee-407f-90ef-9ba3c2756b47").setParameter("access_token", getAccessToken());
        HttpUriRequest deleterequest = RequestBuilder.delete()
                .setUri(uriBuilder1.build())
                .addHeader("Content-Type", "application/fhir+json;charset=utf-8")
                .addHeader("Accept-Charset", "utf-8")
                .addHeader("Accept", "application/fhir+json; charset=utf-8")
                .build();
        HttpResponse putResponse = httpClient.execute(deleterequest);
      break;*/
      }

    }
  }

  private static CloudHealthcare createClient() throws IOException {
    // Use Application Default Credentials (ADC) to authenticate the requests
    // For more information see https://cloud.google.com/docs/authentication/production
    GoogleCredentials credential =
        GoogleCredentials.getApplicationDefault()
            .createScoped(Collections.singleton(CloudHealthcareScopes.CLOUD_PLATFORM));
    // Create a HttpRequestInitializer, which will provide a baseline configuration to all requests.
    HttpRequestInitializer requestInitializer =
        request -> {
          new HttpCredentialsAdapter(credential).initialize(request);
          request.setConnectTimeout(60000); // 1 minute connect timeout
          request.setReadTimeout(60000); // 1 minute read timeout
        };
    // Build the client for interacting with the service.
    return new CloudHealthcare.Builder(HTTP_TRANSPORT, JSON_FACTORY, requestInitializer)
        .setApplicationName("your-application-name")
        .build();
  }

  private static String getAccessToken() throws IOException {
    GoogleCredentials credential =
        GoogleCredentials.getApplicationDefault()
            .createScoped(Collections.singleton(CloudHealthcareScopes.CLOUD_PLATFORM));

    return credential.refreshAccessToken().getTokenValue();
  }
}
// [END healthcare_search_resources_get]
