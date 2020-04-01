package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.RecordsEntity;
import com.atopion.UGC_repository.repositories.RecordsRepository;
import com.atopion.UGC_repository.util.CSVSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest/records")
public class RecordsController {

	private final List<String> table_headers = Arrays.asList("record_id", "record_identifier");

	private final RecordsRepository repository;

	RecordsController(RecordsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<RecordsEntity> getAllJSON(@RequestParam(name = "identifier", required = false) String record_identifier) {
		return repository.findByParams(null, record_identifier);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody RecordsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<RecordsEntity> getAllXML(@RequestParam(name = "identifier", required = false) String record_identifier) {
		return repository.findByParams(null, record_identifier);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody RecordsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "identifier", required = false) String record_identifier) {
		return CSVSerializer.serialize(repository.findByParams(null, record_identifier), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		RecordsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "identifier", required = false) String record_identifier, Model model) {

		List<RecordsEntity> entities = repository.findByParams(null, record_identifier);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getRecord_id()), e.getRecord_identifier()))
				.collect(Collectors.toList());

		if(entities.size() == 0) {
			model.addAttribute("table_headers", table_headers);
			model.addAttribute("table_rows", new String[0]);
		}
		else {
			model.addAttribute("table_headers", table_headers);
			model.addAttribute("table_rows", rows);

		}

		return "sqlResult";
	}

	@GetMapping(path = "{id}", produces = "text/html")
	public String getAllHTMLById(@PathVariable int id, Model model) {
		RecordsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getRecord_id()), e.getRecord_identifier()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody RecordsEntity postNewRecordsJSON(@RequestBody RecordsEntity recordsEntity) {
		try {
			return repository.save(new RecordsEntity(recordsEntity.getRecord_identifier()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody RecordsEntity postNewRecordsXML(@RequestBody RecordsEntity recordsEntity) {
		try {
			return repository.save(new RecordsEntity(recordsEntity.getRecord_identifier()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody RecordsEntity postNewRecordsCSV(@RequestBody String data) {
		RecordsEntity recordsEntity = readCSV(data);

		if(recordsEntity != null) {
			try {
				return repository.save(new RecordsEntity(recordsEntity.getRecord_identifier()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody RecordsEntity postNewRecordsXFORM(RecordsEntity recordsEntity) {
		try {
			return repository.save(new RecordsEntity(recordsEntity.getRecord_identifier()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordJSONById(@PathVariable int id, @RequestBody RecordsEntity param) {
		try {
			return putRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordJSON(@RequestBody RecordsEntity param) {
		try {
			return putRecordInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordXMLById(@PathVariable int id, @RequestBody RecordsEntity param) {
		try {
			return putRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordXML(@RequestBody RecordsEntity param) {
		try {
			return putRecordInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordCSVById(@PathVariable int id, @RequestBody String data) {
		RecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putRecordInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordCSV(@RequestBody String data) {
		RecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putRecordInternal(param, param.getRecord_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordXFORMById(@PathVariable int id, RecordsEntity param) {
		try {
			return putRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> putRecordXFORM(RecordsEntity param) {
		try {
			return putRecordInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<RecordsEntity> putRecordInternal(RecordsEntity param, int id) {
		Optional<RecordsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			RecordsEntity entity = opt.get();
			entity.setRecord_identifier(param.getRecord_identifier());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			RecordsEntity entity;
			if(id != 0)
				entity = new RecordsEntity(id, param.getRecord_identifier());
			else
				entity = new RecordsEntity(param.getRecord_identifier());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordJSONById(@PathVariable int id, @RequestBody RecordsEntity param) {
		try {
			return patchRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordJSON(@RequestBody RecordsEntity param) {
		try {
			return patchRecordInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordXMLById(@PathVariable int id, @RequestBody RecordsEntity param) {
		try {
			return patchRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordXML(@RequestBody RecordsEntity param) {
		try {
			return patchRecordInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordCSVById(@PathVariable int id, @RequestBody String data) {
		RecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchRecordInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordCSV(@RequestBody String data) {
		RecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchRecordInternal(param, param.getRecord_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordXFORMById(@PathVariable int id, RecordsEntity param) {
		try {
			return patchRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<RecordsEntity> patchRecordXFORM(RecordsEntity param) {
		try {
			return patchRecordInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<RecordsEntity> patchRecordInternal(RecordsEntity param, int id) {
		RecordsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getRecord_identifier() != null)
			entity.setRecord_identifier(param.getRecord_identifier());

		repository.save(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}

// ============================================== DELETE REQUEST =======================================================

	@DeleteMapping(path = "{id}")
	public ResponseEntity<String> deleteById(@PathVariable int id) {
		try {
			repository.deleteById(id);
		} catch (Exception e) {
			return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}


// ================================================ Helpers ============================================================

	private RecordsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int record_id = -1;
		String record_identifier = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("record_id"))
				try {
					record_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					record_id = -1;
				}
			else if(headers[i].equals("record_identifier"))
				try {
					record_identifier = lines[1].split(",")[i];
				} catch (Exception e) {
					record_identifier = null;
				}
		}

		if(record_identifier == null)
			return null;
		else if(record_id == -1)
			return new RecordsEntity(record_identifier);
		else
			return new RecordsEntity(record_id, record_identifier);
	}
}
