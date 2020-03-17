package com.atopion.UGC_repository.rest;

import com.atopion.UGC_repository.entities.ApplicationsEntity;
import com.atopion.UGC_repository.entities.ApplicationsRepository;
import com.ctc.wstx.sw.EncodingXmlWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    @GetMapping(path = "", produces = { "text/plain" })
    public @ResponseBody String getAllCSV(@RequestParam(name = "name", defaultValue = "") String name) {
        if(name.equals(""))
            return CSVSerializer.serialize(repository.findAll(), table_headers);
        else
            return CSVSerializer.serialize(repository.findByName(name), table_headers);
    }

    @GetMapping(path = "", produces = { "text/html" })
    public String getAllHTML(Model model, @RequestParam(name = "name", defaultValue = "") String name) {
        List<ApplicationsEntity> entities = null;
        if(name.equals(""))
            entities = repository.findAll();
        else
            entities = repository.findByName(name);

        List<List<String>> rows = entities.stream()
                .map(e -> List.of(Integer.toString(e.getApplication_id()), e.getApplication_name()))
                .collect(Collectors.toList());

        if(entities.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("table_headers", table_headers);
        model.addAttribute("table_rows", rows);

        return "sqlResult";
    }

    @GetMapping(path = "{id}", produces = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity getById(@PathVariable int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "{id}", produces = { "text/plain" })
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

    @PostMapping(path = "", consumes = { "application/json", "application/xml" })
    public @ResponseBody ApplicationsEntity postNewApplication(@RequestBody ApplicationsEntity applicationsEntity) {
        try {
            return repository.save(applicationsEntity);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "", consumes = { "application/x-www-form-urlencoded" })
    public @ResponseBody ApplicationsEntity postNewApplicationXFORM(ApplicationsEntity applicationsEntity) {
        try {
            return repository.save(applicationsEntity);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "", consumes = { "text/plain" })
    public @ResponseBody ApplicationsEntity postNewApplicationCSV(@RequestBody String data) {
        String[] lines = data.split("\n");
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

        if(name != null) {
            try {
                return repository.save(new ApplicationsEntity(id, name));
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

// =============================================  PUT REQUESTS  ========================================================

    //@PutMapping(path = "{id}")
}
