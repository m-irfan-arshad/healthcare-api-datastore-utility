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

package snippets.healthcare;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import snippets.healthcare.datasets.DatasetCreate;
import snippets.healthcare.datasets.DatasetDelete;
import snippets.healthcare.fhir.FhirStoreCreate;
import snippets.healthcare.fhir.resources.FhirResourceCreate;
import snippets.healthcare.fhir.resources.FhirResourceDelete;
import snippets.healthcare.fhir.resources.FhirResourceDeletePurge;
import snippets.healthcare.fhir.resources.FhirResourceGet;
import snippets.healthcare.fhir.resources.FhirResourceGetHistory;
import snippets.healthcare.fhir.resources.FhirResourceGetPatientEverything;
import snippets.healthcare.fhir.resources.FhirResourceListHistory;
import snippets.healthcare.fhir.resources.FhirResourcePatch;
import snippets.healthcare.fhir.resources.FhirResourceSearchGet;
import snippets.healthcare.fhir.resources.FhirResourceSearchPost;
import snippets.healthcare.fhir.resources.FhirResourceUpdate;

@RunWith(JUnit4.class)
public class FhirResourceTests {
  private static final String PROJECT_ID = System.getenv("GOOGLE_CLOUD_PROJECT");
  private static final String REGION_ID = "us-central1";
  private static final Gson gson = new Gson();

  private static String fhirStoreName;
  private static String datasetName;

  private String fhirResourceId;
  private String fhirResourceName;

  private static String resourcePath;
  private static String resourceType = "Bundle";

  private final PrintStream originalOut = System.out;
  private ByteArrayOutputStream bout;

  @Test
  public void test_FhirResourceCreate() throws Exception {
    FhirResourceCreate.fhirResourceCreate("https://healthcare.googleapis.com/v1/projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir", resourceType);
    String output = bout.toString();
    assertThat(output, containsString("FHIR resource created:"));
  }

  @Test
  public void test_FhirResourceSearchGet() throws Exception {

    FhirResourceSearchGet.fhirResourceSearchGet("projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient");

  }


  @Test
  public void test_FhirResourceGet() throws Exception {
    FhirResourceGet.fhirResourceGet("projects/medtel-349114/locations/us-central1/datasets/datastore/fhirStores/fhirstore/fhir/Patient");
    String output = bout.toString();
    assertThat(output, containsString("FHIR resource retrieved:"));
  }



  @Test
  public void test_FhirResourceGetPatientEverything() throws Exception {
    FhirResourceGetPatientEverything.fhirResourceGetPatientEverything(fhirResourceName);

    String output = bout.toString();
    assertThat(output, containsString("Patient compartment results:"));
  }

  @Test
  public void test_FhirResourceGetHistory() throws Exception {
    JsonObject json = new JsonObject();
    json.add("op", new JsonPrimitive("add"));
    json.add("path", new JsonPrimitive("/active"));
    json.add("value", new JsonPrimitive(false));
    JsonArray jarray = new JsonArray();
    jarray.add(json);
    FhirResourcePatch.fhirResourcePatch(fhirResourceName, jarray.toString());
    // Get versionId from results of fhirResourcePatch.
    String versionId;
    Matcher idMatcher = Pattern.compile("\"versionId\": \"(.*)\"").matcher(bout.toString());
    assertTrue(idMatcher.find());
    versionId = idMatcher.group(1);
    FhirResourceGetHistory.fhirResourceGetHistory(fhirResourceName, versionId);

    String output = bout.toString();
    assertThat(output, containsString("FHIR resource retrieved from version:"));
  }

  @Test
  public void test_FhirResourceListHistory() throws Exception {
    JsonObject json = new JsonObject();
    json.add("op", new JsonPrimitive("add"));
    json.add("path", new JsonPrimitive("/active"));
    json.add("value", new JsonPrimitive(false));
    JsonArray jarray = new JsonArray();
    jarray.add(json);
    FhirResourcePatch.fhirResourcePatch(fhirResourceName, jarray.toString());
    FhirResourceListHistory.fhirResourceListHistory(fhirResourceName);
    String output = bout.toString();

    assertThat(output, containsString("FHIR resource history retrieved:"));
  }

  @Test
  public void test_DeletePurgeFhirResource() throws Exception {
    JsonObject json = new JsonObject();
    json.add("op", new JsonPrimitive("add"));
    json.add("path", new JsonPrimitive("/active"));
    json.add("value", new JsonPrimitive(false));
    JsonArray jarray = new JsonArray();
    jarray.add(json);
    FhirResourcePatch.fhirResourcePatch(fhirResourceName, jarray.toString());
    FhirResourceDeletePurge.fhirResourceDeletePurge(fhirResourceName);

    String output = bout.toString();
    assertThat(output, containsString("FHIR resource history purged (excluding current version)."));
  }

  @Test
  public void test_FhirResourceDelete() throws Exception {
    FhirResourceDelete.fhirResourceDelete(fhirResourceName);

    String output = bout.toString();
    assertThat(output, containsString("FHIR resource deleted."));
  }
}
