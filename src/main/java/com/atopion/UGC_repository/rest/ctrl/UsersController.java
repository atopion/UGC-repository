package com.atopion.UGC_repository.rest.ctrl;

import com.atopion.UGC_repository.rest.entities.UsersEntity;
import com.atopion.UGC_repository.rest.repositories.UsersRepository;
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
@RequestMapping("/rest/users")
public class UsersController {

	private final List<String> table_headers = Arrays.asList("user_id", "user_token", "user_name", "user_email");

	private final UsersRepository repository;

	UsersController(UsersRepository repository) {
		this.repository = repository;
	}

// =============================================  GET REQUESTS  ========================================================

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<UsersEntity> getAllJSON(@RequestParam(name = "token", required = false) String user_token, @RequestParam(name = "name", required = false) String user_name, @RequestParam(name = "email", required = false) String user_email) {
		return repository.findByParams(null, user_token, user_name, user_email);
	}

	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody UsersEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<UsersEntity> getAllXML(@RequestParam(name = "token", required = false) String user_token, @RequestParam(name = "name", required = false) String user_name, @RequestParam(name = "email", required = false) String user_email) {
		return repository.findByParams(null, user_token, user_name, user_email);
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody UsersEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "token", required = false) String user_token, @RequestParam(name = "name", required = false) String user_name, @RequestParam(name = "email", required = false) String user_email) {
		return CSVSerializer.serialize(repository.findByParams(null, user_token, user_name, user_email), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		UsersEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "token", required = false) String user_token, @RequestParam(name = "name", required = false) String user_name, @RequestParam(name = "email", required = false) String user_email, Model model) {

		List<UsersEntity> entities = repository.findByParams(null, user_token, user_name, user_email);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getUser_id()), e.getUser_token(), e.getUser_name(), e.getUser_email()))
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
		UsersEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getUser_id()), e.getUser_token(), e.getUser_name(), e.getUser_email()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public @ResponseBody UsersEntity postNewUsersJSON(@RequestBody UsersEntity usersEntity) {
		try {
			return repository.save(new UsersEntity(usersEntity.getUser_token(), usersEntity.getUser_name(), usersEntity.getUser_email()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody UsersEntity postNewUsersXML(@RequestBody UsersEntity usersEntity) {
		try {
			return repository.save(new UsersEntity(usersEntity.getUser_token(), usersEntity.getUser_name(), usersEntity.getUser_email()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody UsersEntity postNewUsersCSV(@RequestBody String data) {
		UsersEntity usersEntity = readCSV(data);

		if(usersEntity != null) {
			try {
				return repository.save(new UsersEntity(usersEntity.getUser_token(), usersEntity.getUser_name(), usersEntity.getUser_email()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}

	@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody UsersEntity postNewUsersXFORM(UsersEntity usersEntity) {
		try {
			return repository.save(new UsersEntity(usersEntity.getUser_token(), usersEntity.getUser_name(), usersEntity.getUser_email()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserJSONById(@PathVariable int id, @RequestBody UsersEntity param) {
		try {
			return putUserInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserJSON(@RequestBody UsersEntity param) {
		try {
			return putUserInternal(param, param.getUser_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserXMLById(@PathVariable int id, @RequestBody UsersEntity param) {
		try {
			return putUserInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserXML(@RequestBody UsersEntity param) {
		try {
			return putUserInternal(param, param.getUser_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserCSVById(@PathVariable int id, @RequestBody String data) {
		UsersEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putUserInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserCSV(@RequestBody String data) {
		UsersEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putUserInternal(param, param.getUser_id());
	}

	@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserXFORMById(@PathVariable int id, UsersEntity param) {
		try {
			return putUserInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> putUserXFORM(UsersEntity param) {
		try {
			return putUserInternal(param, param.getUser_id());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<UsersEntity> putUserInternal(UsersEntity param, int id) {
		Optional<UsersEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			UsersEntity entity = opt.get();
			entity.setUser_token(param.getUser_token());
			entity.setUser_name(param.getUser_name());
			entity.setUser_email(param.getUser_email());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			UsersEntity entity;
			if(id != 0)
				entity = new UsersEntity(id, param.getUser_token(), param.getUser_name(), param.getUser_email());
			else
				entity = new UsersEntity(param.getUser_token(), param.getUser_name(), param.getUser_email());
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserJSONById(@PathVariable int id, @RequestBody UsersEntity param) {
		try {
			return patchUserInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserJSON(@RequestBody UsersEntity param) {
		try {
			return patchUserInternal(param, param.getUser_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserXMLById(@PathVariable int id, @RequestBody UsersEntity param) {
		try {
			return patchUserInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserXML(@RequestBody UsersEntity param) {
		try {
			return patchUserInternal(param, param.getUser_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserCSVById(@PathVariable int id, @RequestBody String data) {
		UsersEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchUserInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserCSV(@RequestBody String data) {
		UsersEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchUserInternal(param, param.getUser_id());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserXFORMById(@PathVariable int id, UsersEntity param) {
		try {
			return patchUserInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<UsersEntity> patchUserXFORM(UsersEntity param) {
		try {
			return patchUserInternal(param, param.getUser_id());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}


	private ResponseEntity<UsersEntity> patchUserInternal(UsersEntity param, int id) {
		UsersEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getUser_token() != null)
			entity.setUser_token(param.getUser_token());
		if(param.getUser_name() != null)
			entity.setUser_name(param.getUser_name());
		if(param.getUser_email() != null)
			entity.setUser_email(param.getUser_email());

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

	private UsersEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int user_id = -1;
		String user_token = null;
		String user_name = null;
		String user_email = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("user_id"))
				try {
					user_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					user_id = -1;
				}
			else if(headers[i].equals("user_token"))
				try {
					user_token = lines[1].split(",")[i];
				} catch (Exception e) {
					user_token = null;
				}
			else if(headers[i].equals("user_name"))
				try {
					user_name = lines[1].split(",")[i];
				} catch (Exception e) {
					user_name = null;
				}
			else if(headers[i].equals("user_email"))
				try {
					user_email = lines[1].split(",")[i];
				} catch (Exception e) {
					user_email = null;
				}
		}

		if(user_token == null || user_name == null || user_email == null)
			return null;
		else if(user_id == -1)
			return new UsersEntity(user_token, user_name, user_email);
		else
			return new UsersEntity(user_id, user_token, user_name, user_email);
	}
}
