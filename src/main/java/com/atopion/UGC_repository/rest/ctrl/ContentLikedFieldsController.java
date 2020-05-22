package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.ContentLikedFieldsEntity;
import com.atopion.UGC_repository.rest.repositories.ContentLikedFieldsRepository;
import com.atopion.UGC_repository.util.CSVSerializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest/contentLikedFields")
public class ContentLikedFieldsController {

	private final List<String> table_headers = Arrays.asList("field_id", "field_name", "field_like_count", "record_id");

	private final ContentLikedFieldsRepository repository;

	ContentLikedFieldsController(ContentLikedFieldsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentLikedFieldsEntity> getAllJSON(@RequestParam(name = "name", required = false) String field_name, @RequestParam(name = "like_count", required = false) Integer field_like_count, @RequestParam(name = "id", required = false) Integer record_id) {
		return repository.findByParams(null, field_name, field_like_count, record_id);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentLikedFieldsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentLikedFieldsEntity> getAllXML(@RequestParam(name = "name", required = false) String field_name, @RequestParam(name = "like_count", required = false) Integer field_like_count, @RequestParam(name = "id", required = false) Integer record_id) {
		return repository.findByParams(null, field_name, field_like_count, record_id);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentLikedFieldsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "name", required = false) String field_name, @RequestParam(name = "like_count", required = false) Integer field_like_count, @RequestParam(name = "id", required = false) Integer record_id) {
		return CSVSerializer.serialize(repository.findByParams(null, field_name, field_like_count, record_id), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentLikedFieldsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "name", required = false) String field_name, @RequestParam(name = "like_count", required = false) Integer field_like_count, @RequestParam(name = "id", required = false) Integer record_id, Model model) {

		List<ContentLikedFieldsEntity> entities = repository.findByParams(null, field_name, field_like_count, record_id);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getField_id()), e.getField_name(), Integer.toString(e.getField_like_count()), Integer.toString(e.getRecord_id())))
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
		ContentLikedFieldsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getField_id()), e.getField_name(), Integer.toString(e.getField_like_count()), Integer.toString(e.getRecord_id())));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikedFieldsEntity postNewContentLikedFieldsJSON(@RequestBody ContentLikedFieldsEntity contentLikedFieldsEntity) {
		try {
			return repository.save(new ContentLikedFieldsEntity(contentLikedFieldsEntity.getField_name(), contentLikedFieldsEntity.getField_like_count(), contentLikedFieldsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikedFieldsEntity postNewContentLikedFieldsXML(@RequestBody ContentLikedFieldsEntity contentLikedFieldsEntity) {
		try {
			return repository.save(new ContentLikedFieldsEntity(contentLikedFieldsEntity.getField_name(), contentLikedFieldsEntity.getField_like_count(), contentLikedFieldsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikedFieldsEntity postNewContentLikedFieldsCSV(@RequestBody String data) {
		ContentLikedFieldsEntity contentLikedFieldsEntity = readCSV(data);

		if(contentLikedFieldsEntity != null) {
			try {
				return repository.save(new ContentLikedFieldsEntity(contentLikedFieldsEntity.getField_name(), contentLikedFieldsEntity.getField_like_count(), contentLikedFieldsEntity.getRecord_id()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikedFieldsEntity postNewContentLikedFieldsXFORM(ContentLikedFieldsEntity contentLikedFieldsEntity) {
		try {
			return repository.save(new ContentLikedFieldsEntity(contentLikedFieldsEntity.getField_name(), contentLikedFieldsEntity.getField_like_count(), contentLikedFieldsEntity.getRecord_id()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldJSONById(@PathVariable int id, @RequestBody ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldJSON(@RequestBody ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, param.getField_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldXMLById(@PathVariable int id, @RequestBody ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldXML(@RequestBody ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, param.getField_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldCSVById(@PathVariable int id, @RequestBody String data) {
		ContentLikedFieldsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentLikedFieldInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldCSV(@RequestBody String data) {
		ContentLikedFieldsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentLikedFieldInternal(param, param.getField_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldXFORMById(@PathVariable int id, ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldXFORM(ContentLikedFieldsEntity param) {
		try {
			return putContentLikedFieldInternal(param, param.getField_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentLikedFieldsEntity> putContentLikedFieldInternal(ContentLikedFieldsEntity param, int id) {
		Optional<ContentLikedFieldsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentLikedFieldsEntity entity = opt.get();
			entity.setField_name(param.getField_name());
			entity.setField_like_count(param.getField_like_count());
			entity.setRecord_id(param.getRecord_id());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentLikedFieldsEntity entity;
			if(id != 0)
				entity = new ContentLikedFieldsEntity(id, param.getField_name(), param.getField_like_count(), param.getRecord_id());
			else
				entity = new ContentLikedFieldsEntity(param.getField_name(), param.getField_like_count(), param.getRecord_id());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldJSONById(@PathVariable int id, @RequestBody ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldJSON(@RequestBody ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, param.getField_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldXMLById(@PathVariable int id, @RequestBody ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldXML(@RequestBody ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, param.getField_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldCSVById(@PathVariable int id, @RequestBody String data) {
		ContentLikedFieldsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentLikedFieldInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldCSV(@RequestBody String data) {
		ContentLikedFieldsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentLikedFieldInternal(param, param.getField_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldXFORMById(@PathVariable int id, ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldXFORM(ContentLikedFieldsEntity param) {
		try {
			return patchContentLikedFieldInternal(param, param.getField_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentLikedFieldsEntity> patchContentLikedFieldInternal(ContentLikedFieldsEntity param, int id) {
		ContentLikedFieldsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getField_name() != null)
			entity.setField_name(param.getField_name());
		if(param.getField_like_count() != -1)
			entity.setField_like_count(param.getField_like_count());
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

	private ContentLikedFieldsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int field_id = -1;
		String field_name = null;
		int field_like_count = -1;
		int record_id = -1;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("field_id"))
				try {
					field_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					field_id = -1;
				}
			else if(headers[i].equals("field_name"))
				try {
					field_name = lines[1].split(",")[i];
				} catch (Exception e) {
					field_name = null;
				}
			else if(headers[i].equals("field_like_count"))
				try {
					field_like_count = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					field_like_count = -1;
				}
			else if(headers[i].equals("record_id"))
				try {
					record_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					record_id = -1;
				}
		}

		if(field_name == null || field_like_count == -1 || record_id == -1)
			return null;
		else if(field_id == -1)
			return new ContentLikedFieldsEntity(field_name, field_like_count, record_id);
		else
			return new ContentLikedFieldsEntity(field_id, field_name, field_like_count, record_id);
	}
}
