package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ContentListsEntity;
import com.atopion.UGC_repository.repositories.ContentListsRepository;
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
@RequestMapping("/rest/contentLists")
public class ContentListsController {

	private final List<String> table_headers = Arrays.asList("list_id", "list_title", "list_description", "list_created", "application_id", "user_token");

	private final ContentListsRepository repository;

	ContentListsController(ContentListsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentListsEntity> getAllJSON(@RequestParam(name = "title", required = false) String list_title, @RequestParam(name = "description", required = false) String list_description, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date list_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, list_title, list_description, list_created, application_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentListsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentListsEntity> getAllXML(@RequestParam(name = "title", required = false) String list_title, @RequestParam(name = "description", required = false) String list_description, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date list_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, list_title, list_description, list_created, application_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentListsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "title", required = false) String list_title, @RequestParam(name = "description", required = false) String list_description, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date list_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "token", required = false) String user_token) {
		return CSVSerializer.serialize(repository.findByParams(null, list_title, list_description, list_created, application_id, user_token), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentListsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "title", required = false) String list_title, @RequestParam(name = "description", required = false) String list_description, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date list_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "token", required = false) String user_token, Model model) {

		List<ContentListsEntity> entities = repository.findByParams(null, list_title, list_description, list_created, application_id, user_token);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getList_id()), e.getList_title(), e.getList_description(), format.format(e.getList_created()), Integer.toString(e.getApplication_id()), e.getUser_token()))
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
		ContentListsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getList_id()), e.getList_title(), e.getList_description(), format.format(e.getList_created()), Integer.toString(e.getApplication_id()), e.getUser_token()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsEntity postNewContentListsJSON(@RequestBody ContentListsEntity contentListsEntity) {
		try {
			return repository.save(new ContentListsEntity(contentListsEntity.getList_title(), contentListsEntity.getList_description(), contentListsEntity.getList_created(), contentListsEntity.getApplication_id(), contentListsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsEntity postNewContentListsXML(@RequestBody ContentListsEntity contentListsEntity) {
		try {
			return repository.save(new ContentListsEntity(contentListsEntity.getList_title(), contentListsEntity.getList_description(), contentListsEntity.getList_created(), contentListsEntity.getApplication_id(), contentListsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsEntity postNewContentListsCSV(@RequestBody String data) {
		ContentListsEntity contentListsEntity = readCSV(data);

		if(contentListsEntity != null) {
			try {
				return repository.save(new ContentListsEntity(contentListsEntity.getList_title(), contentListsEntity.getList_description(), contentListsEntity.getList_created(), contentListsEntity.getApplication_id(), contentListsEntity.getUser_token()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentListsEntity postNewContentListsXFORM(ContentListsEntity contentListsEntity) {
		try {
			return repository.save(new ContentListsEntity(contentListsEntity.getList_title(), contentListsEntity.getList_description(), contentListsEntity.getList_created(), contentListsEntity.getApplication_id(), contentListsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListJSONById(@PathVariable int id, @RequestBody ContentListsEntity param) {
		try {
			return putContentListInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListJSON(@RequestBody ContentListsEntity param) {
		try {
			return putContentListInternal(param, param.getList_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListXMLById(@PathVariable int id, @RequestBody ContentListsEntity param) {
		try {
			return putContentListInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListXML(@RequestBody ContentListsEntity param) {
		try {
			return putContentListInternal(param, param.getList_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListCSVById(@PathVariable int id, @RequestBody String data) {
		ContentListsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentListInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListCSV(@RequestBody String data) {
		ContentListsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentListInternal(param, param.getList_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListXFORMById(@PathVariable int id, ContentListsEntity param) {
		try {
			return putContentListInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> putContentListXFORM(ContentListsEntity param) {
		try {
			return putContentListInternal(param, param.getList_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentListsEntity> putContentListInternal(ContentListsEntity param, int id) {
		Optional<ContentListsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentListsEntity entity = opt.get();
			entity.setList_title(param.getList_title());
			entity.setList_description(param.getList_description());
			entity.setList_created(param.getList_created());
			entity.setApplication_id(param.getApplication_id());
			entity.setUser_token(param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentListsEntity entity;
			if(id != 0)
				entity = new ContentListsEntity(id, param.getList_title(), param.getList_description(), param.getList_created(), param.getApplication_id(), param.getUser_token());
			else
				entity = new ContentListsEntity(param.getList_title(), param.getList_description(), param.getList_created(), param.getApplication_id(), param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListJSONById(@PathVariable int id, @RequestBody ContentListsEntity param) {
		try {
			return patchContentListInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListJSON(@RequestBody ContentListsEntity param) {
		try {
			return patchContentListInternal(param, param.getList_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListXMLById(@PathVariable int id, @RequestBody ContentListsEntity param) {
		try {
			return patchContentListInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListXML(@RequestBody ContentListsEntity param) {
		try {
			return patchContentListInternal(param, param.getList_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListCSVById(@PathVariable int id, @RequestBody String data) {
		ContentListsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentListInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListCSV(@RequestBody String data) {
		ContentListsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentListInternal(param, param.getList_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListXFORMById(@PathVariable int id, ContentListsEntity param) {
		try {
			return patchContentListInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentListsEntity> patchContentListXFORM(ContentListsEntity param) {
		try {
			return patchContentListInternal(param, param.getList_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentListsEntity> patchContentListInternal(ContentListsEntity param, int id) {
		ContentListsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getList_title() != null)
			entity.setList_title(param.getList_title());
		if(param.getList_description() != null)
			entity.setList_description(param.getList_description());
		if(param.getList_created() != null)
			entity.setList_created(param.getList_created());
		if(param.getApplication_id() != -1)
			entity.setApplication_id(param.getApplication_id());
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

	private ContentListsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int list_id = -1;
		String list_title = null;
		String list_description = null;
		Date list_created = null;
		int application_id = -1;
		String user_token = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("list_id"))
				try {
					list_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					list_id = -1;
				}
			else if(headers[i].equals("list_title"))
				try {
					list_title = lines[1].split(",")[i];
				} catch (Exception e) {
					list_title = null;
				}
			else if(headers[i].equals("list_description"))
				try {
					list_description = lines[1].split(",")[i];
				} catch (Exception e) {
					list_description = null;
				}
			else if(headers[i].equals("list_created"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					list_created = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					list_created = null;
				}
			else if(headers[i].equals("application_id"))
				try {
					application_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					application_id = -1;
				}
			else if(headers[i].equals("user_token"))
				try {
					user_token = lines[1].split(",")[i];
				} catch (Exception e) {
					user_token = null;
				}
		}

		if(list_title == null || list_description == null || list_created == null || application_id == -1 || user_token == null)
			return null;
		else if(list_id == -1)
			return new ContentListsEntity(list_title, list_description, list_created, application_id, user_token);
		else
			return new ContentListsEntity(list_id, list_title, list_description, list_created, application_id, user_token);
	}
}
