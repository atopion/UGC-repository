package com.atopion.UGC_repository.webannotation;

import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationViaEntity;
import com.atopion.UGC_repository.webannotation.repositories.WebAnnotationRepository;
import com.atopion.UGC_repository.util.CSVSerializer;
import com.atopion.UGC_repository.webannotation.repositories.WebAnnotationViaRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rest/webannotation")
public class WebAnnotationController {

	//private final List<String> table_headers = Arrays.asList("web_annotation_id", "id", "created", "modified", "generated", "bodyValue", "canonical", "stylesheet_id");

	private final WebAnnotationRepository repository;

	private final WebAnnotationViaRepository viaRepository;

	WebAnnotationController(WebAnnotationRepository repository, WebAnnotationViaRepository viaRepository) {
		this.repository = repository;
		this.viaRepository = viaRepository;
	}

// =============================================  GET REQUESTS  ========================================================

	// JSON - Annotation
	@GetMapping(path = "{id}", produces = "application/json")
	public @ResponseBody WebAnnotationEntity getAllJSONById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	// JSON-LD - Annotation
	@GetMapping(path = "{id}", produces = "application/ld+json")
	public @ResponseBody WebAnnotationEntity getAllJSONLDById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping(path = "{id}", produces = "application/xml")
	public @ResponseBody WebAnnotationEntity getAllXMLById(@PathVariable int id) {
		return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	// JSONLD - Annotation Container TODO
	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody List<WebAnnotationEntity> getAllJSON(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date created, @RequestParam(name = "modified", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modified, @RequestParam(name = "generated", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date generated, @RequestParam(name = "bodyValue", required = false) String bodyValue, @RequestParam(name = "canonical", required = false) String canonical, @RequestParam(name = "id", required = false) Integer stylesheet_id) {
		return repository.findByParams(null, id, created, modified, generated, bodyValue, canonical, stylesheet_id);
	}



	@GetMapping(path = "", produces = "application/xml")
	public @ResponseBody List<WebAnnotationEntity> getAllXML(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date created, @RequestParam(name = "modified", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modified, @RequestParam(name = "generated", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date generated, @RequestParam(name = "bodyValue", required = false) String bodyValue, @RequestParam(name = "canonical", required = false) String canonical, @RequestParam(name = "id", required = false) Integer stylesheet_id) {
		return repository.findByParams(null, id, created, modified, generated, bodyValue, canonical, stylesheet_id);
	}



	/*@GetMapping(path = "", produces = "text/csv")
	public @ResponseBody String getAllCSV(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date created, @RequestParam(name = "modified", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modified, @RequestParam(name = "generated", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date generated, @RequestParam(name = "bodyValue", required = false) String bodyValue, @RequestParam(name = "canonical", required = false) String canonical, @RequestParam(name = "id", required = false) Integer stylesheet_id) {
		return CSVSerializer.serialize(repository.findByParams(null, id, created, modified, generated, bodyValue, canonical, stylesheet_id), table_headers);
	}

	@GetMapping(path = "{id}", produces = "text/csv")
	public @ResponseBody String getAllCSVById(@PathVariable int id) {
		WebAnnotationEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
	}

	@GetMapping(path = "", produces = "text/html")
	public String getAllHTML(@RequestParam(name = "id", required = false) String id, @RequestParam(name = "created", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date created, @RequestParam(name = "modified", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date modified, @RequestParam(name = "generated", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date generated, @RequestParam(name = "bodyValue", required = false) String bodyValue, @RequestParam(name = "canonical", required = false) String canonical, @RequestParam(name = "id", required = false) Integer stylesheet_id, Model model) {

		List<WebAnnotationEntity> entities = repository.findByParams(null, id, created, modified, generated, bodyValue, canonical, stylesheet_id);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> rows = entities.stream()
				.map(e -> List.of(Integer.toString(e.getWebAnnotationId()), e.getId(), format.format(e.getCreated()), format.format(e.getModified()), format.format(e.getGenerated()), e.getBodyValue(), e.getCanonical(), e.getGenerator().toString(), e.getMotivation().toString(), e.getContext().toString(), e.getCreator().toString(), e.getVia().toString(), e.getBody().toString(), e.getTarget().toString(), e.getRights().toString(), e.getAudience().toString(), e.getType().toString(), e.getStylesheet().toString()))
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
		WebAnnotationEntity e = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<List<String>> row = Collections.singletonList(List.of(Integer.toString(e.getWebAnnotationId()), e.getId(), format.format(e.getCreated()), format.format(e.getModified()), format.format(e.getGenerated()), e.getBodyValue(), e.getCanonical(), e.getGenerator().toString(), e.getMotivation().toString(), e.getContext().toString(), e.getCreator().toString(), e.getVia().toString(), e.getBody().toString(), e.getTarget().toString(), e.getRights().toString(), e.getAudience().toString(), e.getType().toString(), e.getStylesheet().toString()));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";
	}*/

// ============================================  POST REQUESTS  ========================================================

	@PostMapping(path = "", consumes = { "application/ld+json", "application/xml" }, produces = { "application/ld+json", "application/xml" })
	public @ResponseBody ResponseEntity<WebAnnotationEntity> postNewWebAnnotationJSON(@RequestBody WebAnnotationEntity webAnnotationEntity) {
		try {
			// Copy the old id value to a via property
			if(webAnnotationEntity.getId() != null) {
				WebAnnotationViaEntity viaEntity = new WebAnnotationViaEntity(webAnnotationEntity.getId(), webAnnotationEntity, null, null);
				LinkedHashSet<WebAnnotationViaEntity> hashSet = new LinkedHashSet<>(webAnnotationEntity.getVia());
				hashSet.add(viaEntity);
				webAnnotationEntity.setVia(hashSet);
			}

			System.err.println("Anno2: " + webAnnotationEntity.getRights());
			// Saving to generate the id value
			WebAnnotationEntity entity = repository.save(webAnnotationEntity);
			repository.flush();
			System.err.println("Anno2: " + entity.getRights());

			// Setting the new id value
			String id = "https://data.forum-wissen.de/rest/webannotation/" + entity.getWebAnnotationId();
			entity.setId(id);
			//entity = repository.save(entity);
			repository.updateIDOfWebAnnotation(entity.getWebAnnotationId(), id);



			// Fetch the new entity
			Optional<WebAnnotationEntity> opt = repository.findById(entity.getWebAnnotationId());
			if(opt.isEmpty())
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			System.err.println("Anno: " + opt.get().toString());


			// The response to the POST request requires a "Location" header set to the new id
			final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.put("Location", Collections.singletonList(id));

			return new ResponseEntity<>(entity, headers, HttpStatus.CREATED);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/*@PostMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationEntity postNewWebAnnotationXML(@RequestBody WebAnnotationEntity webAnnotationEntity) {
		try {
			return repository.save(new WebAnnotationEntity(webAnnotationEntity.getId(), webAnnotationEntity.getCreated(), webAnnotationEntity.getModified(), webAnnotationEntity.getGenerated(), webAnnotationEntity.getBodyValue(), webAnnotationEntity.getCanonical(), webAnnotationEntity.getGenerator(), webAnnotationEntity.getMotivation(), webAnnotationEntity.getContext(), webAnnotationEntity.getCreator(), webAnnotationEntity.getVia(), webAnnotationEntity.getBody(), webAnnotationEntity.getTarget(), webAnnotationEntity.getRights(), webAnnotationEntity.getAudience(), webAnnotationEntity.getType(), webAnnotationEntity.getStylesheet()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/

	/*@PostMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationEntity postNewWebAnnotationCSV(@RequestBody String data) {
		WebAnnotationEntity webAnnotationEntity = readCSV(data);

		if(webAnnotationEntity != null) {
			try {
				return repository.save(new WebAnnotationEntity(webAnnotationEntity.getId(), webAnnotationEntity.getCreated(), webAnnotationEntity.getModified(), webAnnotationEntity.getGenerated(), webAnnotationEntity.getBodyValue(), webAnnotationEntity.getCanonical(), webAnnotationEntity.getGenerator(), webAnnotationEntity.getMotivation(), webAnnotationEntity.getContext(), webAnnotationEntity.getCreator(), webAnnotationEntity.getVia(), webAnnotationEntity.getBody(), webAnnotationEntity.getTarget(), webAnnotationEntity.getRights(), webAnnotationEntity.getAudience(), webAnnotationEntity.getType(), webAnnotationEntity.getStylesheet()));
			} catch (Exception ex) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
			}
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}*/

	/*@PostMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public @ResponseBody WebAnnotationEntity postNewWebAnnotationXFORM(WebAnnotationEntity webAnnotationEntity) {
		try {
			return repository.save(new WebAnnotationEntity(webAnnotationEntity.getId(), webAnnotationEntity.getCreated(), webAnnotationEntity.getModified(), webAnnotationEntity.getGenerated(), webAnnotationEntity.getBodyValue(), webAnnotationEntity.getCanonical(), webAnnotationEntity.getGenerator(), webAnnotationEntity.getMotivation(), webAnnotationEntity.getContext(), webAnnotationEntity.getCreator(), webAnnotationEntity.getVia(), webAnnotationEntity.getBody(), webAnnotationEntity.getTarget(), webAnnotationEntity.getRights(), webAnnotationEntity.getAudience(), webAnnotationEntity.getType(), webAnnotationEntity.getStylesheet()));
		} catch (Exception ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/

// =============================================  PUT REQUESTS  ========================================================

	@PutMapping(path = "{id}", consumes = { "application/ld+json", "application/xml" }, produces = { "application/ld+json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationJSONById(@PathVariable int id, @RequestBody WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/*@PutMapping(path = "", consumes = { "application/ld+json", "application/xml" }, produces = { "application/ld+json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationJSON(@RequestBody WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/

	/*@PutMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationXMLById(@PathVariable int id, @RequestBody WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationXML(@RequestBody WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/

	/*@PutMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationCSVById(@PathVariable int id, @RequestBody String data) {
		WebAnnotationEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putWebAnnotationInternal(param, id);
	}

	@PutMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationCSV(@RequestBody String data) {
		WebAnnotationEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return putWebAnnotationInternal(param, param.getWebAnnotationId());
	}*/

	/*@PutMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationXFORMById(@PathVariable int id, WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> putWebAnnotationXFORM(WebAnnotationEntity param) {
		try {
			return putWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/


	private ResponseEntity<WebAnnotationEntity> putWebAnnotationInternal(WebAnnotationEntity param, int id) {
		Optional<WebAnnotationEntity> opt = repository.findById(id);
		if(opt.isPresent()) {
			WebAnnotationEntity entity = opt.get();
			entity.setCreated(param.getCreated());
			entity.setModified(param.getModified());
			entity.setGenerated(param.getGenerated());
			entity.setBodyValue(param.getBodyValue());
			entity.setCanonical(param.getCanonical());

			if(param.getGenerator() != null) param.getGenerator().forEach(agent -> agent.setAnnotationEntity(entity));
			entity.resetGenerator(param.getGenerator());

			if(param.getMotivation() != null) param.getMotivation().forEach(motivation -> motivation.setAnnotationEntity(entity));
			entity.resetMotivation(param.getMotivation());

			if(param.getContext() != null) param.getContext().forEach(context -> context.setAnnotationEntity(entity));
			entity.resetContext(param.getContext());

			if(param.getCreator() != null) param.getCreator().forEach(creator -> creator.setAnnotationEntity(entity));
			entity.resetCreator(param.getCreator());

			if(param.getVia() != null) param.getVia().forEach(via -> via.setAnnotationEntity(entity));
			entity.resetVia(param.getVia());

			if(param.getBody() != null) param.getBody().forEach(body -> body.setAnnotationEntity(entity));
			entity.resetBody(param.getBody());

			if(param.getTarget() != null) param.getTarget().forEach(target -> target.setAnnotationEntity(entity));
			entity.resetTarget(param.getTarget());

			if(param.getRights() != null) param.getRights().forEach(rights -> rights.setAnnotationEntity(entity));
			entity.resetRights(param.getRights());

			if(param.getAudience() != null) param.getAudience().forEach(audience -> audience.setAnnotationEntity(entity));
			entity.resetAudience(param.getAudience());
			System.out.println(entity.getAudience());

			if(param.getType() != null) param.getType().forEach(type -> type.setAnnotationEntity(entity));
			entity.resetType(param.getType());

			if(param.getStylesheet() != null) param.getStylesheet().forEach(stylesheet -> stylesheet.setAnnotationEntity(entity));
			entity.resetStylesheet(param.getStylesheet());

			try {
				repository.save(entity);
				return new ResponseEntity<>(entity, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		} else {
			// According to the specification, annotations can only be updated not created with PUT.
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}						
// ============================================= PATCH REQUESTS ========================================================

	@PatchMapping(path = "{id}", consumes = { "application/ld+json", "application/xml" }, produces = { "application/ld+json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationJSONById(@PathVariable int id, @RequestBody WebAnnotationEntity param) {
		try {
			System.err.println("PATCH");
			return patchWebAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	/*@PatchMapping(path = "", consumes = "application/json", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationJSON(@RequestBody WebAnnotationEntity param) {
		try {
			return patchWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationXMLById(@PathVariable int id, @RequestBody WebAnnotationEntity param) {
		try {
			return patchWebAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/xml", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationXML(@RequestBody WebAnnotationEntity param) {
		try {
			return patchWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "{id}", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationCSVById(@PathVariable int id, @RequestBody String data) {
		WebAnnotationEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchWebAnnotationInternal(param, id);
	}

	@PatchMapping(path = "", consumes = "text/csv", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationCSV(@RequestBody String data) {
		WebAnnotationEntity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patchWebAnnotationInternal(param, param.getWebAnnotationId());
	}

	@PatchMapping(path = "{id}", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationXFORMById(@PathVariable int id, WebAnnotationEntity param) {
		try {
			return patchWebAnnotationInternal(param, id);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@PatchMapping(path = "", consumes = "application/x-www-form-urlencoded", produces = { "application/json", "application/xml" })
	public ResponseEntity<WebAnnotationEntity> patchWebAnnotationXFORM(WebAnnotationEntity param) {
		try {
			return patchWebAnnotationInternal(param, param.getWebAnnotationId());
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}*/


	private ResponseEntity<WebAnnotationEntity> patchWebAnnotationInternal(WebAnnotationEntity param, int id) {
		WebAnnotationEntity entity = repository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		if(param.getCreated() != null)
			entity.setCreated(param.getCreated());
		if(param.getModified() != null)
        	entity.setModified(param.getModified());
		if(param.getGenerated() != null)
        	entity.setGenerated(param.getGenerated());
		if(param.getBodyValue() != null)
        	entity.setBodyValue(param.getBodyValue());
		if(param.getCanonical() != null)
        	entity.setCanonical(param.getCanonical());

		if(param.getGenerator() != null) {
	        param.getGenerator().forEach(el -> el.setAnnotationEntity(entity));
    	    entity.setGenerator(param.getGenerator());
		}

		if(param.getMotivation() != null) {
        	param.getMotivation().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setMotivation(param.getMotivation());
		}

		if(param.getContext() != null) {
        	param.getContext().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setContext(param.getContext());
		}

		if(param.getCreator() != null) {
			param.getCreator().forEach(el -> el.setAnnotationEntity(entity));
			entity.setCreator(param.getCreator());
		}

		if(param.getVia() != null) {
        	param.getVia().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setVia(param.getVia());
		}

		if(param.getBody() != null) {
        	param.getBody().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setBody(param.getBody());
		}

		if(param.getTarget() != null) {
        	param.getTarget().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setTarget(param.getTarget());
		}

		if(param.getRights() != null) {
        	param.getRights().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setRights(param.getRights());
		}

		if(param.getAudience() != null) {
	        param.getAudience().forEach(el -> el.setAnnotationEntity(entity));
	        entity.setAudience(param.getAudience());
		}

		if(param.getType() != null) {
        	param.getType().forEach(el -> el.setAnnotationEntity(entity));
        	entity.setType(param.getType());
		}

		if(param.getStylesheet() != null) {
			param.getStylesheet().forEach(stylesheet -> stylesheet.setAnnotationEntity(entity));
			entity.resetStylesheet(param.getStylesheet());
		}

		try {
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

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

	/*
	private WebAnnotationEntity readCSV(String csv) {
		String[] lines = csv.split("\n");
		int web_annotation_id = -1;
		String id = null;
		Date created = null;
		Date modified = null;
		Date generated = null;
		String bodyValue = null;
		String canonical = null;
		WebAnnotationStylesheetEntity stylesheet_id = null;


		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
			if(headers[i].equals("web_annotation_id"))
				try {
					web_annotation_id = Integer.parseInt(lines[1].split(",")[i]);
				} catch (Exception e) {
					web_annotation_id = -1;
				}
			else if(headers[i].equals("id"))
				try {
					id = lines[1].split(",")[i];
				} catch (Exception e) {
					id = null;
				}
			else if(headers[i].equals("created"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					created = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					created = null;
				}
			else if(headers[i].equals("modified"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					modified = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					modified = null;
				}
			else if(headers[i].equals("generated"))
				try {
					 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					generated = format.parse(lines[1].split(",")[i]);
				} catch (Exception e) {
					generated = null;
				}
			else if(headers[i].equals("bodyValue"))
				try {
					bodyValue = lines[1].split(",")[i];
				} catch (Exception e) {
					bodyValue = null;
				}
			else if(headers[i].equals("canonical"))
				try {
					canonical = lines[1].split(",")[i];
				} catch (Exception e) {
					canonical = null;
				}
			else if(headers[i].equals("stylesheet_id"))
				try {
					stylesheet_id = lines[1].split(",")[i];
				} catch (Exception e) {
					stylesheet_id = null;
				}
		}

		if(id == null || created == null || modified == null || generated == null || bodyValue == null || canonical == null || stylesheet_id == null)
			return null;
		else if(web_annotation_id == -1)
			return new WebAnnotationEntity(id, created, modified, generated, bodyValue, canonical, stylesheet_id);
		else
			return new WebAnnotationEntity(web_annotation_id, id, created, modified, generated, bodyValue, canonical, stylesheet_id);
	}

	 */
}
