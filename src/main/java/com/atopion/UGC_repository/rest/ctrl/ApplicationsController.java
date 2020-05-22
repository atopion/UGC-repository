package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.ApplicationsEntity;
import com.atopion.UGC_repository.rest.repositories.ApplicationsRepository;
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
@RequestMapping("/rest/applications")
public class ApplicationsController {

	private final List<String> table_headers = Arrays.asList("application_id", "application_name");

	private final ApplicationsRepository repository;

	ApplicationsController(ApplicationsRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<ApplicationsEntity> getAllJSON(@RequestParam(name = "name", required = false) String application_name) {
		return repository.findByParams(null, application_name);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody ApplicationsEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<ApplicationsEntity> getAllXML(@RequestParam(name = "name", required = false) String application_name) {
		return repository.findByParams(null, application_name);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody ApplicationsEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "name", required = false) String application_name) {
		return CSVSerializer.serialize(repository.findByParams(null, application_name), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		ApplicationsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "name", required = false) String application_name, Model model) {

		List<ApplicationsEntity> entities = repository.findByParams(null, application_name);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getApplication_id()), e.getApplication_name()))
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
		ApplicationsEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getApplication_id()), e.getApplication_name()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody ApplicationsEntity postNewApplicationsJSON(@RequestBody ApplicationsEntity applicationsEntity) {
		try {
			return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody ApplicationsEntity postNewApplicationsXML(@RequestBody ApplicationsEntity applicationsEntity) {
		try {
			return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody ApplicationsEntity postNewApplicationsCSV(@RequestBody String data) {
		ApplicationsEntity applicationsEntity = readCSV(data);

		if(applicationsEntity != null) {
			try {
				return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody ApplicationsEntity postNewApplicationsXFORM(ApplicationsEntity applicationsEntity) {
		try {
			return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationJSONById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationJSON(@RequestBody ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, param.getApplication_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationXMLById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationXML(@RequestBody ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, param.getApplication_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationCSVById(@PathVariable int id, @RequestBody String data) {
		ApplicationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putApplicationInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationCSV(@RequestBody String data) {
		ApplicationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putApplicationInternal(param, param.getApplication_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationXFORMById(@PathVariable int id, ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> putApplicationXFORM(ApplicationsEntity param) {
		try {
			return putApplicationInternal(param, param.getApplication_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ApplicationsEntity> putApplicationInternal(ApplicationsEntity param, int id) {
		Optional<ApplicationsEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			ApplicationsEntity entity = opt.get();
			entity.setApplication_name(param.getApplication_name());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			ApplicationsEntity entity;
			if(id != 0)
				entity = new ApplicationsEntity(id, param.getApplication_name());
			else
				entity = new ApplicationsEntity(param.getApplication_name());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationJSONById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationJSON(@RequestBody ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, param.getApplication_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationXMLById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationXML(@RequestBody ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, param.getApplication_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationCSVById(@PathVariable int id, @RequestBody String data) {
		ApplicationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchApplicationInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationCSV(@RequestBody String data) {
		ApplicationsEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchApplicationInternal(param, param.getApplication_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationXFORMById(@PathVariable int id, ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<ApplicationsEntity> patchApplicationXFORM(ApplicationsEntity param) {
		try {
			return patchApplicationInternal(param, param.getApplication_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<ApplicationsEntity> patchApplicationInternal(ApplicationsEntity param, int id) {
		ApplicationsEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getApplication_name() != null)
			entity.setApplication_name(param.getApplication_name());

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

	private ApplicationsEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int application_id = -1;
		String application_name = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("application_id"))
				try {
					application_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					application_id = -1;
				}
			else if(headers[i].equals("application_name"))
				try {
					application_name = lines[1].split(",")[i];
				} catch (Exception e) {
					application_name = null;
				}
		}

		if(application_name == null)
			return null;
		else if(application_id == -1)
			return new ApplicationsEntity(application_name);
		else
			return new ApplicationsEntity(application_id, application_name);
	}
}
