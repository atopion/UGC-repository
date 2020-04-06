package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ContentAnnotationsEntity;
import com.atopion.UGC_repository.repositories.ContentAnnotationsRepository;
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
@RequestMapping("/rest/contentAnnotations")
public class ContentAnnotationsController {

	private final List<String> table_headers = Arrays.asList("annotation_id", "annotation_url", "annotation_content", "annotation_canvas", "annotation_created", "application_id", "record_id", "user_token");

	private final ContentAnnotationsRepository repository;

	ContentAnnotationsController(ContentAnnotationsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentAnnotationsEntity> getAllJSON(@RequestParam(name = "url", required = false) String annotation_url, @RequestParam(name = "content", required = false) String annotation_content, @RequestParam(name = "canvas", required = false) Integer annotation_canvas, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date annotation_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentAnnotationsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentAnnotationsEntity> getAllXML(@RequestParam(name = "url", required = false) String annotation_url, @RequestParam(name = "content", required = false) String annotation_content, @RequestParam(name = "canvas", required = false) Integer annotation_canvas, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date annotation_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentAnnotationsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "url", required = false) String annotation_url, @RequestParam(name = "content", required = false) String annotation_content, @RequestParam(name = "canvas", required = false) Integer annotation_canvas, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date annotation_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return CSVSerializer.serialize(repository.findByParams(null, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentAnnotationsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "url", required = false) String annotation_url, @RequestParam(name = "content", required = false) String annotation_content, @RequestParam(name = "canvas", required = false) Integer annotation_canvas, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date annotation_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token, Model model) {

		List<ContentAnnotationsEntity> entities = repository.findByParams(null, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getAnnotation_id()), e.getAnnotation_url(), e.getAnnotation_content(), Integer.toString(e.getAnnotation_canvas()), format.format(e.getAnnotation_created()), Integer.toString(e.getApplication_id()), Integer.toString(e.getRecord_id()), e.getUser_token()))
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
		ContentAnnotationsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getAnnotation_id()), e.getAnnotation_url(), e.getAnnotation_content(), Integer.toString(e.getAnnotation_canvas()), format.format(e.getAnnotation_created()), Integer.toString(e.getApplication_id()), Integer.toString(e.getRecord_id()), e.getUser_token()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentAnnotationsEntity postNewContentAnnotationsJSON(@RequestBody ContentAnnotationsEntity contentAnnotationsEntity) {
		try {
			return repository.save(new ContentAnnotationsEntity(contentAnnotationsEntity.getAnnotation_url(), contentAnnotationsEntity.getAnnotation_content(), contentAnnotationsEntity.getAnnotation_canvas(), contentAnnotationsEntity.getAnnotation_created(), contentAnnotationsEntity.getApplication_id(), contentAnnotationsEntity.getRecord_id(), contentAnnotationsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentAnnotationsEntity postNewContentAnnotationsXML(@RequestBody ContentAnnotationsEntity contentAnnotationsEntity) {
		try {
			return repository.save(new ContentAnnotationsEntity(contentAnnotationsEntity.getAnnotation_url(), contentAnnotationsEntity.getAnnotation_content(), contentAnnotationsEntity.getAnnotation_canvas(), contentAnnotationsEntity.getAnnotation_created(), contentAnnotationsEntity.getApplication_id(), contentAnnotationsEntity.getRecord_id(), contentAnnotationsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentAnnotationsEntity postNewContentAnnotationsCSV(@RequestBody String data) {
		ContentAnnotationsEntity contentAnnotationsEntity = readCSV(data);

		if(contentAnnotationsEntity != null) {
			try {
				return repository.save(new ContentAnnotationsEntity(contentAnnotationsEntity.getAnnotation_url(), contentAnnotationsEntity.getAnnotation_content(), contentAnnotationsEntity.getAnnotation_canvas(), contentAnnotationsEntity.getAnnotation_created(), contentAnnotationsEntity.getApplication_id(), contentAnnotationsEntity.getRecord_id(), contentAnnotationsEntity.getUser_token()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentAnnotationsEntity postNewContentAnnotationsXFORM(ContentAnnotationsEntity contentAnnotationsEntity) {
		try {
			return repository.save(new ContentAnnotationsEntity(contentAnnotationsEntity.getAnnotation_url(), contentAnnotationsEntity.getAnnotation_content(), contentAnnotationsEntity.getAnnotation_canvas(), contentAnnotationsEntity.getAnnotation_created(), contentAnnotationsEntity.getApplication_id(), contentAnnotationsEntity.getRecord_id(), contentAnnotationsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationJSONById(@PathVariable int id, @RequestBody ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationJSON(@RequestBody ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationXMLById(@PathVariable int id, @RequestBody ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationXML(@RequestBody ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationCSVById(@PathVariable int id, @RequestBody String data) {
		ContentAnnotationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentAnnotationInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationCSV(@RequestBody String data) {
		ContentAnnotationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentAnnotationInternal(param, param.getAnnotation_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationXFORMById(@PathVariable int id, ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> putContentAnnotationXFORM(ContentAnnotationsEntity param) {
		try {
			return putContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentAnnotationsEntity> putContentAnnotationInternal(ContentAnnotationsEntity param, int id) {
		Optional<ContentAnnotationsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentAnnotationsEntity entity = opt.get();
			entity.setAnnotation_url(param.getAnnotation_url());
			entity.setAnnotation_content(param.getAnnotation_content());
			entity.setAnnotation_canvas(param.getAnnotation_canvas());
			entity.setAnnotation_created(param.getAnnotation_created());
			entity.setApplication_id(param.getApplication_id());
			entity.setRecord_id(param.getRecord_id());
			entity.setUser_token(param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentAnnotationsEntity entity;
			if(id != 0)
				entity = new ContentAnnotationsEntity(id, param.getAnnotation_url(), param.getAnnotation_content(), param.getAnnotation_canvas(), param.getAnnotation_created(), param.getApplication_id(), param.getRecord_id(), param.getUser_token());
			else
				entity = new ContentAnnotationsEntity(param.getAnnotation_url(), param.getAnnotation_content(), param.getAnnotation_canvas(), param.getAnnotation_created(), param.getApplication_id(), param.getRecord_id(), param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationJSONById(@PathVariable int id, @RequestBody ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationJSON(@RequestBody ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationXMLById(@PathVariable int id, @RequestBody ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationXML(@RequestBody ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationCSVById(@PathVariable int id, @RequestBody String data) {
		ContentAnnotationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentAnnotationInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationCSV(@RequestBody String data) {
		ContentAnnotationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentAnnotationInternal(param, param.getAnnotation_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationXFORMById(@PathVariable int id, ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationXFORM(ContentAnnotationsEntity param) {
		try {
			return patchContentAnnotationInternal(param, param.getAnnotation_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentAnnotationsEntity> patchContentAnnotationInternal(ContentAnnotationsEntity param, int id) {
		ContentAnnotationsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getAnnotation_url() != null)
			entity.setAnnotation_url(param.getAnnotation_url());
		if(param.getAnnotation_content() != null)
			entity.setAnnotation_content(param.getAnnotation_content());
		if(param.getAnnotation_canvas() != -1)
			entity.setAnnotation_canvas(param.getAnnotation_canvas());
		if(param.getAnnotation_created() != null)
			entity.setAnnotation_created(param.getAnnotation_created());
		if(param.getApplication_id() != -1)
			entity.setApplication_id(param.getApplication_id());
		if(param.getRecord_id() != -1)
			entity.setRecord_id(param.getRecord_id());
		if(param.getUser_token() != null)
			entity.setUser_token(param.getUser_token());

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

	private ContentAnnotationsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int annotation_id = -1;
		String annotation_url = null;
		String annotation_content = null;
		int annotation_canvas = -1;
		Date annotation_created = null;
		int application_id = -1;
		int record_id = -1;
		String user_token = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("annotation_id"))
				try {
					annotation_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					annotation_id = -1;
				}
			else if(headers[i].equals("annotation_url"))
				try {
					annotation_url = lines[1].split(",")[i];
				} catch (Exception e) {
					annotation_url = null;
				}
			else if(headers[i].equals("annotation_content"))
				try {
					annotation_content = lines[1].split(",")[i];
				} catch (Exception e) {
					annotation_content = null;
				}
			else if(headers[i].equals("annotation_canvas"))
				try {
					annotation_canvas = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					annotation_canvas = -1;
				}
			else if(headers[i].equals("annotation_created"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					annotation_created = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					annotation_created = null;
				}
			else if(headers[i].equals("application_id"))
				try {
					application_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					application_id = -1;
				}
			else if(headers[i].equals("record_id"))
				try {
					record_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					record_id = -1;
				}
			else if(headers[i].equals("user_token"))
				try {
					user_token = lines[1].split(",")[i];
				} catch (Exception e) {
					user_token = null;
				}
		}

		if(annotation_url == null || annotation_content == null || annotation_canvas == -1 || annotation_created == null || application_id == -1 || record_id == -1 || user_token == null)
			return null;
		else if(annotation_id == -1)
			return new ContentAnnotationsEntity(annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);
		else
			return new ContentAnnotationsEntity(annotation_id, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);
	}
}
