/*
 * Corona-Warn-App
 *
 * SAP SE and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package app.coronawarn.server.services.distribution.assembly.structure.file.decorators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.coronawarn.server.services.distribution.assembly.component.CryptoProvider;
import app.coronawarn.server.services.distribution.assembly.structure.directory.Directory;
import app.coronawarn.server.services.distribution.assembly.structure.file.File;
import org.junit.Rule;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.TemporaryFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CryptoProvider.class},
    initializers = ConfigFileApplicationContextInitializer.class)
public class ArchiveSigningDecoratorImplTest {

  @Rule
  private TemporaryFolder outputFolder = new TemporaryFolder();

  @Autowired
  CryptoProvider cryptoProvider;

  private static final byte[] bytes = "foo".getBytes();
  private Directory parent;
  private File decoratee;
  private File decorator;

  /*
  @BeforeEach
  public void setup() throws IOException {
    outputFolder.create();
    parent = new DirectoryImpl(outputFolder.newFolder());
    decoratee = new FileImpl("bar", bytes);
    decorator = new SigningDecoratorImpl(decoratee, cryptoProvider);

    parent.addWritable(decorator);

    prepareAndWrite(parent);
  }

  @Test
  public void checkCertificate() throws IOException, CertificateEncodingException {
    byte[] writtenBytes = Files.readAllBytes(decoratee.getFileOnDisk().toPath());
    SignedPayload signedPayload = SignedPayload.parseFrom(writtenBytes);

    assertArrayEquals(cryptoProvider.getCertificate().getEncoded(),
        signedPayload.getCertificateChain().toByteArray());
  }

  @Test
  public void checkSignature()
      throws IOException, CertificateException, NoSuchProviderException, NoSuchAlgorithmException,
      InvalidKeyException, SignatureException {
    byte[] writtenBytes = Files.readAllBytes(decoratee.getFileOnDisk().toPath());
    SignedPayload signedPayload = SignedPayload.parseFrom(writtenBytes);

    InputStream certificateByteStream = new ByteArrayInputStream(
        signedPayload.getCertificateChain().toByteArray());
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    Certificate certificate = certificateFactory.generateCertificate(certificateByteStream);

    Signature signature = Signature.getInstance("Ed25519", "BC");
    signature.initVerify(certificate);
    signature.update(signedPayload.getPayload().toByteArray());

    assertTrue(signature.verify(signedPayload.getSignature().toByteArray()));
  }
  */
}
