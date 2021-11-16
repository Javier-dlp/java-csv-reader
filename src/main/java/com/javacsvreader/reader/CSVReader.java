package com.javacsvreader.reader;

import com.javacsvreader.domain.UserVisit;
import com.javacsvreader.domain.UserVisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.javacsvreader.reader.FileProcessedStatus.*;
import static java.util.concurrent.CompletableFuture.runAsync;

@Slf4j
@Component
@RequiredArgsConstructor
public class CSVReader {

	public static final int FILE_PROCESSING_TIMEOUT = 2_000; //TODO: move to app config

	private static final Map<String, CompletableFuture<Void>> LOCKS = new ConcurrentHashMap<>();
	private static final Map<String, FileProcessedStatus> FILE_STATUSES = new ConcurrentHashMap<>();

	private final UserVisitRepository repository;

	public FileProcessedStatus processWithTimeout(String fileName) {
		if (IN_PROGRESS == FILE_STATUSES.get(fileName)) {
			return IN_PROGRESS;
		}

		log.info("Started processing file " + fileName);
		FILE_STATUSES.put(fileName, IN_PROGRESS);
		LOCKS.put(fileName, runAsync(() -> process(fileName)));
		synchronized (LOCKS.get(fileName)) {
			try {
				CompletableFuture<Void> future = LOCKS.get(fileName);
				future.whenComplete((K, V) -> log.info("Finished processing file " + fileName));
				future.wait(FILE_PROCESSING_TIMEOUT);
				return FILE_STATUSES.get(fileName);
			} catch (IllegalMonitorStateException | InterruptedException e) {
				log.error("Exception produced waiting for reader", e);
				return FAILED;
			}
		}
	}

	private void process(String fileName) {
		try (InputStreamReader reader = new InputStreamReader(new FileInputStream(fileName))) {
			try (CSVParser csvParser = new CSVParser(reader, getCsvFormat())) {
				csvParser.stream()
						.map(CSVReader::recordToRow)
						.filter(Objects::nonNull)
						.forEach(repository::save);
				}
			FILE_STATUSES.put(fileName, DONE);
		} catch (IOException e) {
			FILE_STATUSES.put(fileName, FAILED);
			log.error("IOException while reading input file", e);
		}
	}

	private static UserVisit recordToRow(CSVRecord record) {
		if (record.size() < 3
				|| Stream.of(record.get(0), record.get(1), record.get(2)).anyMatch(Strings::isBlank))
		{
			log.debug("Skipping incomplete record " + record.toString());
			return null;
		}
		return new UserVisit(
				record.get(0),
				record.get(1),
				record.get(2)
		);
	}

	private static CSVFormat getCsvFormat() {
		return CSVFormat.DEFAULT.builder()
				.setDelimiter(',')
				.setHeader()
				.setSkipHeaderRecord(true)
				.setNullString("")
				.build();
	}
}
