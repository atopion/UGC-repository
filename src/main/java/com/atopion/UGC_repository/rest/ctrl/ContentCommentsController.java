package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.ContentCommentsEntity;
import com.atopion.UGC_repository.rest.repositories.ContentCommentsRepository;
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
@RequestMapping("/rest/contentComments")
public class ContentCommentsController {

	private final List<String> table_headers = Arrays.asList("comment_id", "comment_text", "comment_created", "application_id", "record_id", "user_token");

	private final ContentCommentsRepository repository;

	ContentCommentsController(ContentCommentsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentCommentsEntity> getAllJSON(@RequestParam(name = "text", required = false) String comment_text, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date comment_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, comment_text, comment_created, application_id, record_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentCommentsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentCommentsEntity> getAllXML(@RequestParam(name = "text", required = false) String comment_text, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date comment_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return repository.findByParams(null, comment_text, comment_created, application_id, record_id, user_token);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentCommentsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "text", required = false) String comment_text, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date comment_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token) {
		return CSVSerializer.serialize(repository.findByParams(null, comment_text, comment_created, application_id, record_id, user_token), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentCommentsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "text", required = false) String comment_text, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date comment_created, @RequestParam(name = "id", required = false) Integer application_id, @RequestParam(name = "id", required = false) Integer record_id, @RequestParam(name = "token", required = false) String user_token, Model model) {

		List<ContentCommentsEntity> entities = repository.findByParams(null, comment_text, comment_created, application_id, record_id, user_token);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getComment_id()), e.getComment_text(), format.format(e.getComment_created()), Integer.toString(e.getApplication_id()), Integer.toString(e.getRecord_id()), e.getUser_token()))
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
		ContentCommentsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getComment_id()), e.getComment_text(), format.format(e.getComment_created()), Integer.toString(e.getApplication_id()), Integer.toString(e.getRecord_id()), e.getUser_token()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentCommentsEntity postNewContentCommentsJSON(@RequestBody ContentCommentsEntity contentCommentsEntity) {
		try {
			return repository.save(new ContentCommentsEntity(contentCommentsEntity.getComment_text(), contentCommentsEntity.getComment_created(), contentCommentsEntity.getApplication_id(), contentCommentsEntity.getRecord_id(), contentCommentsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentCommentsEntity postNewContentCommentsXML(@RequestBody ContentCommentsEntity contentCommentsEntity) {
		try {
			return repository.save(new ContentCommentsEntity(contentCommentsEntity.getComment_text(), contentCommentsEntity.getComment_created(), contentCommentsEntity.getApplication_id(), contentCommentsEntity.getRecord_id(), contentCommentsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentCommentsEntity postNewContentCommentsCSV(@RequestBody String data) {
		ContentCommentsEntity contentCommentsEntity = readCSV(data);

		if(contentCommentsEntity != null) {
			try {
				return repository.save(new ContentCommentsEntity(contentCommentsEntity.getComment_text(), contentCommentsEntity.getComment_created(), contentCommentsEntity.getApplication_id(), contentCommentsEntity.getRecord_id(), contentCommentsEntity.getUser_token()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentCommentsEntity postNewContentCommentsXFORM(ContentCommentsEntity contentCommentsEntity) {
		try {
			return repository.save(new ContentCommentsEntity(contentCommentsEntity.getComment_text(), contentCommentsEntity.getComment_created(), contentCommentsEntity.getApplication_id(), contentCommentsEntity.getRecord_id(), contentCommentsEntity.getUser_token()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentJSONById(@PathVariable int id, @RequestBody ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentJSON(@RequestBody ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, param.getComment_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentXMLById(@PathVariable int id, @RequestBody ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentXML(@RequestBody ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, param.getComment_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentCSVById(@PathVariable int id, @RequestBody String data) {
		ContentCommentsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentCommentInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentCSV(@RequestBody String data) {
		ContentCommentsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentCommentInternal(param, param.getComment_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentXFORMById(@PathVariable int id, ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> putContentCommentXFORM(ContentCommentsEntity param) {
		try {
			return putContentCommentInternal(param, param.getComment_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentCommentsEntity> putContentCommentInternal(ContentCommentsEntity param, int id) {
		Optional<ContentCommentsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentCommentsEntity entity = opt.get();
			entity.setComment_text(param.getComment_text());
			entity.setComment_created(param.getComment_created());
			entity.setApplication_id(param.getApplication_id());
			entity.setRecord_id(param.getRecord_id());
			entity.setUser_token(param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentCommentsEntity entity;
			if(id != 0)
				entity = new ContentCommentsEntity(id, param.getComment_text(), param.getComment_created(), param.getApplication_id(), param.getRecord_id(), param.getUser_token());
			else
				entity = new ContentCommentsEntity(param.getComment_text(), param.getComment_created(), param.getApplication_id(), param.getRecord_id(), param.getUser_token());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentJSONById(@PathVariable int id, @RequestBody ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentJSON(@RequestBody ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, param.getComment_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentXMLById(@PathVariable int id, @RequestBody ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentXML(@RequestBody ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, param.getComment_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentCSVById(@PathVariable int id, @RequestBody String data) {
		ContentCommentsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentCommentInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentCSV(@RequestBody String data) {
		ContentCommentsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentCommentInternal(param, param.getComment_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentXFORMById(@PathVariable int id, ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentCommentsEntity> patchContentCommentXFORM(ContentCommentsEntity param) {
		try {
			return patchContentCommentInternal(param, param.getComment_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentCommentsEntity> patchContentCommentInternal(ContentCommentsEntity param, int id) {
		ContentCommentsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getComment_text() != null)
			entity.setComment_text(param.getComment_text());
		if(param.getComment_created() != null)
			entity.setComment_created(param.getComment_created());
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

	private ContentCommentsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int comment_id = -1;
		String comment_text = null;
		Date comment_created = null;
		int application_id = -1;
		int record_id = -1;
		String user_token = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("comment_id"))
				try {
					comment_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					comment_id = -1;
				}
			else if(headers[i].equals("comment_text"))
				try {
					comment_text = lines[1].split(",")[i];
				} catch (Exception e) {
					comment_text = null;
				}
			else if(headers[i].equals("comment_created"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					comment_created = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					comment_created = null;
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

		if(comment_text == null || comment_created == null || application_id == -1 || record_id == -1 || user_token == null)
			return null;
		else if(comment_id == -1)
			return new ContentCommentsEntity(comment_text, comment_created, application_id, record_id, user_token);
		else
			return new ContentCommentsEntity(comment_id, comment_text, comment_created, application_id, record_id, user_token);
	}
}
