package com.javacsvreader.reader;

import com.javacsvreader.domain.UserVisitRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import static com.javacsvreader.reader.FileProcessedStatus.DONE;
import static com.javacsvreader.reader.FileProcessedStatus.FAILED;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CSVReaderTest {

    @Mock
    private UserVisitRepository repository;

    @Autowired
    private CSVReader csvReader;

    @Test
    void testNonExistingFile_returnsFailed() {
        // Given
        String filename = "testFile";
        // When
        FileProcessedStatus fileProcessedStatus = csvReader.processWithTimeout(filename);
        // Then
        assertEquals(FAILED, fileProcessedStatus);
    }

    @Test
    void testExistingFile_worksOK() throws URISyntaxException {
        // Given
        URL resource = getClass().getClassLoader().getResource("V1__Created_database.sql");
        assert resource != null;
        String filename = Paths.get(resource.toURI()).toString();
        // When
        FileProcessedStatus fileProcessedStatus = csvReader.processWithTimeout(filename);
        // Then
        assertEquals(DONE, fileProcessedStatus);
    }

}
