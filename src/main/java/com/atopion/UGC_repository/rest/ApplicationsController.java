package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ApplicationsEntity;
import com.atopion.UGC_repository.entities.ApplicationsRepository;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @GetMapping(path = "", produces = { "application/json", "application/xml" })
    public @ResponseBody List<ApplicationsEntity> getAll(@RequestParam(name = "name", defaultValue = "") String name) {
        if(name.equals(""))
            return repository.findAll();
        else
            return repository.findByName(name);
    }

    @GetMapping(path = "", produces = { "text/csv" })
    public @ResponseBody String getAllCSV(@RequestParam(name = "name", defaultValue = "") String name) {
        if(name.equals(""))
            return CSVSerializer.serialize(repository.findAll(), table_headers);
        else
            return CSVSerializer.serialize(repository.findByName(name), table_headers);
    }

    @GetMapping(path = "", produces = { "text/html" })
    public String getAllHTML(Model model, @RequestParam(name = "name", defaultValue = "") String name) {
        List<ApplicationsEntity> entities;
        if(name.equals(""))
            entities = repository.findAll();
        else
            entities = repository.findByName(name);

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

    @GetMapping(path = "{id}", produces = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity getById(@PathVariable int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "{id}", produces = { "text/csv" })
    public @ResponseBody String getByIdCSV(@PathVariable int id) {
        ApplicationsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CSVSerializer.serialize(Collections.singletonList(entity), table_headers);
    }

    @GetMapping(path = "{id}", produces = { "text/html" })
    public String getByIdHTML(@PathVariable int id, Model model) {
        ApplicationsEntity entity = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<List<String>> row = Collections.singletonList(List.of(Integer.toString(entity.getApplication_id()), entity.getApplication_name()));

        model.addAttribute("table_headers", table_headers);
        model.addAttribute("table_rows", row);

        return "sqlResult";
    }

// =============================================  POST REQUESTS  =======================================================

    @PostMapping(path = "", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity postNewApplication(@RequestBody ApplicationsEntity applicationsEntity) {
        try {
            return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "", consumes = { "application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity postNewApplicationXFORM(ApplicationsEntity applicationsEntity) {
        try {
            return repository.save(new ApplicationsEntity(applicationsEntity.getApplication_name()));
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "", consumes = { "text/csv" }, produces = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity postNewApplicationCSV(@RequestBody String data) {
        ApplicationsEntity entity = readCSV(data);

        if(entity != null) {
            try {
                return repository.save(new ApplicationsEntity(entity.getApplication_name()));
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

// =============================================  PUT REQUESTS  ========================================================

    /**
     *  The PUT method replaces an existing entity or creates a new one, depending whether "id" already exists.
     */
    @PutMapping(path = "{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplicationById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
        try {
            return putApplicationInternal(param, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{id}", consumes = { "application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplicationByIdXFORM(@PathVariable int id, ApplicationsEntity param) {
        try {
            return putApplicationInternal(param, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "{id}", consumes = { "text/csv" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplicationByIdCSV(@PathVariable int id, @RequestBody String data) {
        ApplicationsEntity param = readCSV(data);
        if(param == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return putApplicationInternal(param, id);
    }

    @PutMapping(path = "", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplication(@RequestBody ApplicationsEntity param) {
        try {
            return putApplicationInternal(param, param.getApplication_id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "", consumes = { "application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplicationXFORM(ApplicationsEntity param) {
        try {
            return putApplicationInternal(param, param.getApplication_id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "", consumes = { "text/csv" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> putApplicationCSV(@RequestBody String data) {
        ApplicationsEntity param = readCSV(data);
        if(param == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return putApplicationInternal(param, param.getApplication_id());
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

    /**
     * Returns 404 on non existing ids
     *
     */

    @PatchMapping(path = "{id}", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplicationById(@PathVariable int id, @RequestBody ApplicationsEntity param) {
        try {
            return patchApplicationInternal(param, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "{id}", consumes = { "application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplicationByIdXFORM(@PathVariable int id, ApplicationsEntity param) {
        try {
            return patchApplicationInternal(param, id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "{id}", consumes = { "text/csv" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplicationByIdCSV(@PathVariable int id, @RequestBody String data) {
        ApplicationsEntity param = readCSV(data);
        if(param == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return patchApplicationInternal(param, id);
    }

    @PatchMapping(path = "", consumes = { "application/json", "application/xml" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplication(@RequestBody ApplicationsEntity param) {
        try {
            return patchApplicationInternal(param, param.getApplication_id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "", consumes = { "application/x-www-form-urlencoded" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplicationXFORM(ApplicationsEntity param) {
        try {
            return patchApplicationInternal(param, param.getApplication_id());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping(path = "", consumes = { "text/csv" }, produces = { "application/json", "application/xml" })
    public ResponseEntity<ApplicationsEntity> patchApplicationCSV(@RequestBody String data) {
        ApplicationsEntity param = readCSV(data);
        if(param == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        return patchApplicationInternal(param, param.getApplication_id());
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

    @DeleteMapping(path = "", consumes = { "application/json", "application/xml" })
    public ResponseEntity<String> deleteApplication(@RequestBody ApplicationsEntity param) {
        try {
            repository.deleteById(param.getApplication_id());
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "", consumes = { "application/x-www-form-urlencoded" })
    public ResponseEntity<String> deleteApplicationXFORM(ApplicationsEntity param) {
        try {
            repository.deleteById(param.getApplication_id());
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "", consumes = { "text/csv" })
    public ResponseEntity<String> deleteApplicationCSV(@RequestBody String data) {
        ApplicationsEntity param = readCSV(data);
        if(param == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        try {
            repository.deleteById(param.getApplication_id());
        } catch (Exception e) {
            return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
    }

// ================================================ Helpers ============================================================
    private ApplicationsEntity readCSV(String csv) {
        String[] lines = csv.split("\n");
        String name = null;
        int id = -1;

        String[] headers = lines[0].split(",");
        for(int i = 0; i < headers.length; i++) {
            if(headers[i].equals("application_name"))
                try {
                    name = lines[1].split(",")[i];
                } catch (Exception e) {
                    name = null;
                }
            else if(headers[i].equals("application_id"))
                try {
                    id = Integer.parseInt(lines[1].split(",")[i]);
                } catch (Exception e) {
                    id = -1;
                }
        }

        if(name == null)
            return null;
        else if(id == -1)
            return new ApplicationsEntity(name);
        else
            return new ApplicationsEntity(id, name);
    }
}
