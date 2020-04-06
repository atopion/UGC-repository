package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ContentListsRecordsEntity;
import com.atopion.UGC_repository.repositories.ContentListsRecordsRepository;
import com.atopion.UGC_repository.util.CSVSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest/contentListsRecords")
public class ContentListsRecordsController {

	private final List<String> table_headers = Arrays.asList("list_content_id", "entry_created", "list_id", "record_id");

	private final ContentListsRecordsRepository repository;

	ContentListsRecordsController(ContentListsRecordsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentListsRecordsEntity> getAllJSON(@RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date entry_created, @RequestParam(name = "id", required = false) Integer list_id, @RequestParam(name = "id", required = false) Integer record_id) {
		return repository.findByParams(null, entry_created, list_id, record_id);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentListsRecordsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentListsRecordsEntity> getAllXML(@RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date entry_created, @RequestParam(name = "id", required = false) Integer list_id, @RequestParam(name = "id", required = false) Integer record_id) {
		return repository.findByParams(null, entry_created, list_id, record_id);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentListsRecordsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date entry_created, @RequestParam(name = "id", required = false) Integer list_id, @RequestParam(name = "id", required = false) Integer record_id) {
		return CSVSerializer.serialize(repository.findByParams(null, entry_created, list_id, record_id), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentListsRecordsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date entry_created, @RequestParam(name = "id", required = false) Integer list_id, @RequestParam(name = "id", required = false) Integer record_id, Model model) {

		List<ContentListsRecordsEntity> entities = repository.findByParams(null, entry_created, list_id, record_id);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getList_content_id()), format.format(e.getEntry_created()), Integer.toString(e.getList_id()), Integer.toString(e.getRecord_id())))
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
		ContentListsRecordsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getList_content_id()), format.format(e.getEntry_created()), Integer.toString(e.getList_id()), Integer.toString(e.getRecord_id())));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsRecordsEntity postNewContentListsRecordsJSON(@RequestBody ContentListsRecordsEntity contentListsRecordsEntity) {
		try {
			return repository.save(new ContentListsRecordsEntity(contentListsRecordsEntity.getEntry_created(), contentListsRecordsEntity.getList_id(), contentListsRecordsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsRecordsEntity postNewContentListsRecordsXML(@RequestBody ContentListsRecordsEntity contentListsRecordsEntity) {
		try {
			return repository.save(new ContentListsRecordsEntity(contentListsRecordsEntity.getEntry_created(), contentListsRecordsEntity.getList_id(), contentListsRecordsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsRecordsEntity postNewContentListsRecordsCSV(@RequestBody String data) {
		ContentListsRecordsEntity contentListsRecordsEntity = readCSV(data);

		if(contentListsRecordsEntity != null) {
			try {
				return repository.save(new ContentListsRecordsEntity(contentListsRecordsEntity.getEntry_created(), contentListsRecordsEntity.getList_id(), contentListsRecordsEntity.getRecord_id()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsRecordsEntity postNewContentListsRecordsXFORM(ContentListsRecordsEntity contentListsRecordsEntity) {
		try {
			return repository.save(new ContentListsRecordsEntity(contentListsRecordsEntity.getEntry_created(), contentListsRecordsEntity.getList_id(), contentListsRecordsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordJSONById(@PathVariable int id, @RequestBody ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordJSON(@RequestBody ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, param.getList_content_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordXMLById(@PathVariable int id, @RequestBody ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordXML(@RequestBody ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, param.getList_content_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordCSVById(@PathVariable int id, @RequestBody String data) {
		ContentListsRecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentListsRecordInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordCSV(@RequestBody String data) {
		ContentListsRecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentListsRecordInternal(param, param.getList_content_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordXFORMById(@PathVariable int id, ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> putContentListsRecordXFORM(ContentListsRecordsEntity param) {
		try {
			return putContentListsRecordInternal(param, param.getList_content_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentListsRecordsEntity> putContentListsRecordInternal(ContentListsRecordsEntity param, int id) {
		Optional<ContentListsRecordsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentListsRecordsEntity entity = opt.get();
			entity.setEntry_created(param.getEntry_created());
			entity.setList_id(param.getList_id());
			entity.setRecord_id(param.getRecord_id());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentListsRecordsEntity entity;
			if(id != 0)
				entity = new ContentListsRecordsEntity(id, param.getEntry_created(), param.getList_id(), param.getRecord_id());
			else
				entity = new ContentListsRecordsEntity(param.getEntry_created(), param.getList_id(), param.getRecord_id());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordJSONById(@PathVariable int id, @RequestBody ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordJSON(@RequestBody ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, param.getList_content_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordXMLById(@PathVariable int id, @RequestBody ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordXML(@RequestBody ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, param.getList_content_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordCSVById(@PathVariable int id, @RequestBody String data) {
		ContentListsRecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentListsRecordInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordCSV(@RequestBody String data) {
		ContentListsRecordsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentListsRecordInternal(param, param.getList_content_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordXFORMById(@PathVariable int id, ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordXFORM(ContentListsRecordsEntity param) {
		try {
			return patchContentListsRecordInternal(param, param.getList_content_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentListsRecordsEntity> patchContentListsRecordInternal(ContentListsRecordsEntity param, int id) {
		ContentListsRecordsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getEntry_created() != null)
			entity.setEntry_created(param.getEntry_created());
		if(param.getList_id() != -1)
			entity.setList_id(param.getList_id());
		if(param.getRecord_id() != -1)
			entity.setRecord_id(param.getRecord_id());

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

	private ContentListsRecordsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int list_content_id = -1;
		Date entry_created = null;
		int list_id = -1;
		int record_id = -1;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("list_content_id"))
				try {
					list_content_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					list_content_id = -1;
				}
			else if(headers[i].equals("entry_created"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					entry_created = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					entry_created = null;
				}
			else if(headers[i].equals("list_id"))
				try {
					list_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					list_id = -1;
				}
			else if(headers[i].equals("record_id"))
				try {
					record_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					record_id = -1;
				}
		}

		if(entry_created == null || list_id == -1 || record_id == -1)
			return null;
		else if(list_content_id == -1)
			return new ContentListsRecordsEntity(entry_created, list_id, record_id);
		else
			return new ContentListsRecordsEntity(list_content_id, entry_created, list_id, record_id);
	}
}
