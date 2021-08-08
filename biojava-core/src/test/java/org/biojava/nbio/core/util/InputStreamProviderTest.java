package org.biojava.nbio.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InputStreamProviderTest {

    InputStreamProvider isProvider;
    final String uncompressedFasta = ">1 test test\nPEPTIDEK";
    final int UNCOMPRESSED_BYTES_LENGTH = 21;

    // obtained by running `gzip` on uncompressedFasta
    final String GZIPPED_FASTA_HEX = "1f8b0808d9c00f61000366617374612e74787400b3335428492d2e01135c01ae01219e2eaede5c0078fcb9b416000000";
    
    // obtained by running `compress` on uncompressedFasta
    final String COMPRESSED_FASTA_HEX = "1f9d903e6280a053660e9d8105e9288052040a9524448a2c5100";
    
    File createJarFile () throws IOException{
      File f = File.createTempFile("jarred", ".jar");
      try (FileOutputStream fos = new FileOutputStream(f);
            JarOutputStream out = new JarOutputStream(fos);){
        JarEntry e = new JarEntry("protein.fa");
        writeData(out, e);
      }
      return f;
    }

    File createZipFile() throws IOException {
      File f = File.createTempFile("zipped", ".zip");
      try (FileOutputStream fos = new FileOutputStream(f);
            ZipOutputStream out = new ZipOutputStream(fos);){
        ZipEntry e = new ZipEntry("protein.fa");
        writeData(out, e);
      }
      return f;
    }

    private void writeData(ZipOutputStream out, ZipEntry e) throws IOException {
      out.putNextEntry(e);
      byte[] data = uncompressedFasta.getBytes();
      out.write(data, 0, data.length);
      out.closeEntry();
    }

    @BeforeEach
    void before() {
        isProvider = new InputStreamProvider();
    }

    @Test
    void readAnyFile() throws IOException{
        File tmpFile = File.createTempFile("fasta", ".fa");
        Files.write(Path.of(tmpFile.getAbsolutePath()), uncompressedFasta.getBytes());
        assertInputStreamReadsOriginalText(tmpFile); 
    }

    @Test
    void readZipFile() throws IOException{
      File zipped = createZipFile();
      assertInputStreamReadsOriginalText(zipped);
    }

    @Test
    void readJarFile() throws IOException{
      File jarred = createJarFile();
      assertInputStreamReadsOriginalText(jarred);
    }

    @Test
    void readGzippedFile() throws IOException{
      File gzipped = getGZippedFile();
      assertInputStreamReadsOriginalText(gzipped);
    }

    private File getGZippedFile() throws IOException {
      BigInteger fromHexString = new BigInteger(GZIPPED_FASTA_HEX, 16);
      File gzipped = File.createTempFile("gzip", ".gz");
      Files.write(Path.of(gzipped.getAbsolutePath()),
              fromHexString.toByteArray());
      return gzipped;
    }

    @Test
    void readCompressedFile() throws IOException{
      File compressed = getCompressedFile();
      assertInputStreamReadsOriginalText(compressed);
    }

    void assertInputStreamReadsOriginalText(File toRead) throws IOException {
      char [] in = new char[UNCOMPRESSED_BYTES_LENGTH];
      try (InputStream is = isProvider.getInputStream(toRead);
      InputStreamReader br = new  InputStreamReader(is)) {
        int read = br.read(in);
        assertEquals(UNCOMPRESSED_BYTES_LENGTH, read);
        String contents = new String(in);
        assertEquals(contents, uncompressedFasta);
      } 
    }

    @Test
    void filesCanBeCached() throws IOException{
      File anyFile = getCompressedFile();
      isProvider.setCacheEnabled(true);
      assertInputStreamReadsOriginalText(anyFile);

      // overwrite original file and break .Z format
      Files.write(Path.of(anyFile.getAbsolutePath()), "otherData".getBytes());
      
      // we are using cache so don't detect the changed file
      assertInputStreamReadsOriginalText(anyFile);
    }

    private File getCompressedFile() throws IOException {
      BigInteger fromHexString = new BigInteger(COMPRESSED_FASTA_HEX, 16);
      File anyFile = File.createTempFile("compressed", ".Z");
      Files.write(Path.of(anyFile.getAbsolutePath()),
              fromHexString.toByteArray());
      return anyFile;
    }
        
}
