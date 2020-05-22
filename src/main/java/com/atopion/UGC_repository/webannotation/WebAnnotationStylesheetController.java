package com.atopion.UGC_repository.webannotation;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStylesheetEntity;
import com.atopion.UGC_repository.webannotation.repositories.WebAnnotationStylesheetRepository;
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
@RequestMapping("/rest/webannotationStylesheet")
public class WebAnnotationStylesheetController {

	private final List<String> table_headers = Arrays.asList("stylesheet_id", "value");

	private final WebAnnotationStylesheetRepository repository;

	WebAnnotationStylesheetController(WebAnnotationStylesheetRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<WebAnnotationStylesheetEntity> getAllJSON(@RequestParam(name = "value", required = false) String value) {
		return repository.findByParams(null, value);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody WebAnnotationStylesheetEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<WebAnnotationStylesheetEntity> getAllXML(@RequestParam(name = "value", required = false) String value) {
		return repository.findByParams(null, value);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody WebAnnotationStylesheetEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "value", required = false) String value) {
		return CSVSerializer.serialize(repository.findByParams(null, value), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		WebAnnotationStylesheetEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "value", required = false) String value, Model model) {

		List<WebAnnotationStylesheetEntity> entities = repository.findByParams(null, value);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getStylesheetId()), e.getValue()))
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
		WebAnnotationStylesheetEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getStylesheetId()), e.getValue()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================
/*
	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationStylesheetEntity postNewWebAnnotationStylesheetJSON(@RequestBody WebAnnotationStylesheetEntity webAnnotationStylesheetEntity) {
		try {
			return repository.save(new WebAnnotationStylesheetEntity(webAnnotationStylesheetEntity.getValue()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationStylesheetEntity postNewWebAnnotationStylesheetXML(@RequestBody WebAnnotationStylesheetEntity webAnnotationStylesheetEntity) {
		try {
			return repository.save(new WebAnnotationStylesheetEntity(webAnnotationStylesheetEntity.getValue()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationStylesheetEntity postNewWebAnnotationStylesheetCSV(@RequestBody String data) {
		WebAnnotationStylesheetEntity webAnnotationStylesheetEntity = readCSV(data);

		if(webAnnotationStylesheetEntity != null) {
			try {
				return repository.save(new WebAnnotationStylesheetEntity(webAnnotationStylesheetEntity.getValue()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationStylesheetEntity postNewWebAnnotationStylesheetXFORM(WebAnnotationStylesheetEntity webAnnotationStylesheetEntity) {
		try {
			return repository.save(new WebAnnotationStylesheetEntity(webAnnotationStylesheetEntity.getValue()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetJSONById(@PathVariable int id, @RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetJSON(@RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetXMLById(@PathVariable int id, @RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetXML(@RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetCSVById(@PathVariable int id, @RequestBody String data) {
		WebAnnotationStylesheetEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putWebAnnotationStylesheetInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetCSV(@RequestBody String data) {
		WebAnnotationStylesheetEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetXFORMById(@PathVariable int id, WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetXFORM(WebAnnotationStylesheetEntity param) {
		try {
			return putWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<WebAnnotationStylesheetEntity> putWebAnnotationStylesheetInternal(WebAnnotationStylesheetEntity param, int id) {
		Optional<WebAnnotationStylesheetEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			WebAnnotationStylesheetEntity entity = opt.get();
			entity.setValue(param.getValue());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			WebAnnotationStylesheetEntity entity;
			if(id != 0)
				entity = new WebAnnotationStylesheetEntity(id, param.getValue());
			else
				entity = new WebAnnotationStylesheetEntity(param.getValue());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetJSONById(@PathVariable int id, @RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetJSON(@RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetXMLById(@PathVariable int id, @RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetXML(@RequestBody WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetCSVById(@PathVariable int id, @RequestBody String data) {
		WebAnnotationStylesheetEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchWebAnnotationStylesheetInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetCSV(@RequestBody String data) {
		WebAnnotationStylesheetEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetXFORMById(@PathVariable int id, WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetXFORM(WebAnnotationStylesheetEntity param) {
		try {
			return patchWebAnnotationStylesheetInternal(param, param.getStylesheet_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<WebAnnotationStylesheetEntity> patchWebAnnotationStylesheetInternal(WebAnnotationStylesheetEntity param, int id) {
		WebAnnotationStylesheetEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getValue() != null)
			entity.setValue(param.getValue());

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

	private WebAnnotationStylesheetEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int stylesheet_id = -1;
		String value = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("stylesheet_id"))
				try {
					stylesheet_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					stylesheet_id = -1;
				}
			else if(headers[i].equals("value"))
				try {
					value = lines[1].split(",")[i];
				} catch (Exception e) {
					value = null;
				}
		}

		if(value == null)
			return null;
		else if(stylesheet_id == -1)
			return new WebAnnotationStylesheetEntity(value);
		else
			return new WebAnnotationStylesheetEntity(stylesheet_id, value);
	}*/
}
