package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.ContentLikesEntity;
import com.atopion.UGC_repository.rest.repositories.ContentLikesRepository;
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
@RequestMapping("/rest/contentLikes")
public class ContentLikesController {

	private final List<String> table_headers = Arrays.asList("record_id", "cuby_like_level_1", "cuby_like_level_2", "cuby_like_level_3");

	private final ContentLikesRepository repository;

	ContentLikesController(ContentLikesRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ContentLikesEntity> getAllJSON(@RequestParam(name = "like_level_1", required = false) Integer cuby_like_level_1, @RequestParam(name = "like_level_2", required = false) Integer cuby_like_level_2, @RequestParam(name = "like_level_3", required = false) Integer cuby_like_level_3) {
		return repository.findByParams(null, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ContentLikesEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ContentLikesEntity> getAllXML(@RequestParam(name = "like_level_1", required = false) Integer cuby_like_level_1, @RequestParam(name = "like_level_2", required = false) Integer cuby_like_level_2, @RequestParam(name = "like_level_3", required = false) Integer cuby_like_level_3) {
		return repository.findByParams(null, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ContentLikesEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "like_level_1", required = false) Integer cuby_like_level_1, @RequestParam(name = "like_level_2", required = false) Integer cuby_like_level_2, @RequestParam(name = "like_level_3", required = false) Integer cuby_like_level_3) {
		return CSVSerializer.serialize(repository.findByParams(null, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ContentLikesEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "like_level_1", required = false) Integer cuby_like_level_1, @RequestParam(name = "like_level_2", required = false) Integer cuby_like_level_2, @RequestParam(name = "like_level_3", required = false) Integer cuby_like_level_3, Model model) {

		List<ContentLikesEntity> entities = repository.findByParams(null, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getRecord_id()), Integer.toString(e.getCuby_like_level_1()), Integer.toString(e.getCuby_like_level_2()), Integer.toString(e.getCuby_like_level_3())))
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
		ContentLikesEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getRecord_id()), Integer.toString(e.getCuby_like_level_1()), Integer.toString(e.getCuby_like_level_2()), Integer.toString(e.getCuby_like_level_3())));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikesEntity postNewContentLikesJSON(@RequestBody ContentLikesEntity contentLikesEntity) {
		try {
			return repository.save(new ContentLikesEntity(contentLikesEntity.getCuby_like_level_1(), contentLikesEntity.getCuby_like_level_2(), contentLikesEntity.getCuby_like_level_3()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikesEntity postNewContentLikesXML(@RequestBody ContentLikesEntity contentLikesEntity) {
		try {
			return repository.save(new ContentLikesEntity(contentLikesEntity.getCuby_like_level_1(), contentLikesEntity.getCuby_like_level_2(), contentLikesEntity.getCuby_like_level_3()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikesEntity postNewContentLikesCSV(@RequestBody String data) {
		ContentLikesEntity contentLikesEntity = readCSV(data);

		if(contentLikesEntity != null) {
			try {
				return repository.save(new ContentLikesEntity(contentLikesEntity.getCuby_like_level_1(), contentLikesEntity.getCuby_like_level_2(), contentLikesEntity.getCuby_like_level_3()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ContentLikesEntity postNewContentLikesXFORM(ContentLikesEntity contentLikesEntity) {
		try {
			return repository.save(new ContentLikesEntity(contentLikesEntity.getCuby_like_level_1(), contentLikesEntity.getCuby_like_level_2(), contentLikesEntity.getCuby_like_level_3()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeJSONById(@PathVariable int id, @RequestBody ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeJSON(@RequestBody ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeXMLById(@PathVariable int id, @RequestBody ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeXML(@RequestBody ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeCSVById(@PathVariable int id, @RequestBody String data) {
		ContentLikesEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentLikeInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeCSV(@RequestBody String data) {
		ContentLikesEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putContentLikeInternal(param, param.getRecord_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeXFORMById(@PathVariable int id, ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> putContentLikeXFORM(ContentLikesEntity param) {
		try {
			return putContentLikeInternal(param, param.getRecord_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentLikesEntity> putContentLikeInternal(ContentLikesEntity param, int id) {
		Optional<ContentLikesEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ContentLikesEntity entity = opt.get();
			entity.setCuby_like_level_1(param.getCuby_like_level_1());
			entity.setCuby_like_level_2(param.getCuby_like_level_2());
			entity.setCuby_like_level_3(param.getCuby_like_level_3());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ContentLikesEntity entity;
			if(id != 0)
				entity = new ContentLikesEntity(id, param.getCuby_like_level_1(), param.getCuby_like_level_2(), param.getCuby_like_level_3());
			else
				entity = new ContentLikesEntity(param.getCuby_like_level_1(), param.getCuby_like_level_2(), param.getCuby_like_level_3());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeJSONById(@PathVariable int id, @RequestBody ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeJSON(@RequestBody ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeXMLById(@PathVariable int id, @RequestBody ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeXML(@RequestBody ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeCSVById(@PathVariable int id, @RequestBody String data) {
		ContentLikesEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentLikeInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeCSV(@RequestBody String data) {
		ContentLikesEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchContentLikeInternal(param, param.getRecord_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeXFORMById(@PathVariable int id, ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ContentLikesEntity> patchContentLikeXFORM(ContentLikesEntity param) {
		try {
			return patchContentLikeInternal(param, param.getRecord_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ContentLikesEntity> patchContentLikeInternal(ContentLikesEntity param, int id) {
		ContentLikesEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getCuby_like_level_1() != -1)
			entity.setCuby_like_level_1(param.getCuby_like_level_1());
		if(param.getCuby_like_level_2() != -1)
			entity.setCuby_like_level_2(param.getCuby_like_level_2());
		if(param.getCuby_like_level_3() != -1)
			entity.setCuby_like_level_3(param.getCuby_like_level_3());

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

	private ContentLikesEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int record_id = -1;
		int cuby_like_level_1 = -1;
		int cuby_like_level_2 = -1;
		int cuby_like_level_3 = -1;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("record_id"))
				try {
					record_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					record_id = -1;
				}
			else if(headers[i].equals("cuby_like_level_1"))
				try {
					cuby_like_level_1 = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					cuby_like_level_1 = -1;
				}
			else if(headers[i].equals("cuby_like_level_2"))
				try {
					cuby_like_level_2 = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					cuby_like_level_2 = -1;
				}
			else if(headers[i].equals("cuby_like_level_3"))
				try {
					cuby_like_level_3 = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					cuby_like_level_3 = -1;
				}
		}

		if(cuby_like_level_1 == -1 || cuby_like_level_2 == -1 || cuby_like_level_3 == -1)
			return null;
		else if(record_id == -1)
			return new ContentLikesEntity(cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);
		else
			return new ContentLikesEntity(record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);
	}
}
