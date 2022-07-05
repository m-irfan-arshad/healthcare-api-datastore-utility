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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
import snippets.healthcare.hl7v2.Hl7v2StoreCreate;
import snippets.healthcare.hl7v2.messages.HL7v2MessageCreate;
import snippets.healthcare.hl7v2.messages.HL7v2MessageDelete;
import snippets.healthcare.hl7v2.messages.HL7v2MessageGet;
import snippets.healthcare.hl7v2.messages.HL7v2MessageIngest;
import snippets.healthcare.hl7v2.messages.HL7v2MessageList;
import snippets.healthcare.hl7v2.messages.HL7v2MessagePatch;

@RunWith(JUnit4.class)
public class Hl7v2MessageTests {

  private static void requireEnvVar(String varName) {
    assertNotNull(
        System.getenv(varName),
        String.format("Environment variable '%s' is required to perform these tests.", varName));
  }

  @BeforeClass
  public static void checkRequirements() {
    requireEnvVar("GOOGLE_APPLICATION_CREDENTIALS");
    requireEnvVar("GOOGLE_CLOUD_PROJECT");
  }



  @Test
  @SuppressWarnings("checkstyle:AbbreviationAsWordInName")
  public void test_DeleteHL7v2MessageFromHL7Store() throws Exception {
    HL7v2MessageDelete.hl7v2MessageDeleteFromHL7Store("projects/medtel-349114/locations/us-central1/datasets/datastore/hl7V2Stores/hl7v2store");

  }
}
