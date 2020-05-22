package main

import (
	"flag"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"strconv"
	"strings"

	pkg "atopion.com/pkg"
)

type Entity struct {
	name       string
	name_upper string

	col_names    []string
	col_type     []string
	col_javatype []string
	col_null     []bool
	col_inc      []bool
	col_unique   []bool
	col_foreign  []string

	primary int
}

func check(err error) {
	if err != nil {
		log.Fatal(err)
	}
}

func pathExists(path string) bool {
	_, err := os.Stat(path)
	if err == nil {
		return true
	}
	if os.IsNotExist(err) {
		return false
	}
	return true
}

func parseDDL(ddl string) Entity {

	var entity = Entity{}

	var (
		i1 int = strings.Index(ddl, "CREATE TABLE")
		i2 int = pkg.StrIndexAt(ddl, ";", i1)
	)

	if i1 == -1 || i2 == -1 {
		return entity
	}

	ddl = ddl[i1:i2]

	// Extract name
	i1 = strings.Index(ddl, "(")
	w := strings.Split(strings.Trim(ddl[0:i1], " "), " ")
	entity.name = w[len(w)-1]

	// Turn snake_case name into CamelCase name
	if strings.Contains(entity.name, "_") {
		tmp := ""
		up := false
		for i := 0; i < len(entity.name); i++ {
			if !up {
				if string(entity.name[i]) != "_" {
					tmp += string(entity.name[i])
				} else {
					up = true
				}
			} else {
				tmp += strings.ToUpper(string(entity.name[i]))
				up = false
			}
		}
	}

	entity.name_upper = strings.ToUpper(entity.name[0:1]) + entity.name[1:]

	// Extract columns
	i1 = strings.Index(ddl, "(")
	i2 = strings.LastIndex(ddl, ")")

	ddl = ddl[i1+1 : i2]
	ddl = strings.ReplaceAll(ddl, "\t", "")

	for _, c := range strings.Split(ddl, ",\n") {
		c = strings.Trim(c, "\t\n ")

		if c == "" {
			continue
		}

		if !(strings.HasPrefix(c, "CONSTRAINT") || strings.HasPrefix(c, "UNIQUE") || strings.HasPrefix(c, "PRIMARY") || strings.HasPrefix(c, "FOREIGN") || strings.HasPrefix(c, "CHECK")) {
			entity.col_names = append(entity.col_names, strings.Split(c, " ")[0])
			entity.col_type = append(entity.col_type, strings.Split(c, " ")[1])
			entity.col_null = append(entity.col_null, !strings.Contains(c, "NOT NULL"))
			entity.col_inc = append(entity.col_inc, strings.Contains(c, "AUTO_INCREMENT"))
			entity.col_unique = append(entity.col_unique, strings.Contains(c, "UNIQUE"))
			entity.col_foreign = append(entity.col_foreign, "")

			entity.col_javatype = append(entity.col_javatype, pkg.IfThenElse(strings.HasPrefix(strings.Split(c, " ")[1], "varchar"), "String", pkg.IfThenElse(strings.Split(c, " ")[1] == "int", "int", "Date")).(string))

		} else {
			if strings.HasPrefix(c, "PRIMARY") {
				for i, n := range entity.col_names {
					if strings.Contains(c, "("+n+")") {
						entity.primary = i
						entity.col_null[i] = false
						entity.col_unique[i] = true
					}
				}
			} else if strings.HasPrefix(c, "UNIQUE") || (strings.HasPrefix(c, "CONSTRAINT") && strings.Contains(c, "UNIQUE")) {
				cols_, err := pkg.StrExtract(c, "(", ")")
				if err != nil {
					continue
				}
				cols := strings.Split(cols_, ",")
				for _, n := range cols {
					for i, m := range entity.col_names {
						if n == m {
							entity.col_unique[i] = true
						}
					}
				}

			} else if (strings.HasPrefix(c, "FOREIGN KEY") || (strings.HasPrefix(c, "CONSTRAINT") && strings.Contains(c, "FOREIGN KEY"))) && strings.Contains(c, "REFERENCES") {
				foreign := strings.Trim(c, " ")
				foreign = c[strings.LastIndex(c, " "):]

				col := c[strings.Index(c, "FOREIGN KEY")+11 : strings.Index(c, "REFERENCES")]

				for i, n := range entity.col_names {
					if n == col {
						entity.col_foreign[i] = foreign
					}
				}
			}
		}
	}

	return entity
}

func produceEntityFile(entity Entity) string {

	var base string = `package com.atopion.UGC_repository.rep.entities;
    
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
    
@Entity
@Table(name = "` + entity.name + `")
public class ` + entity.name_upper + `Entity {

`

	var fields = make([]string, len(entity.col_names))

	for i, col := range entity.col_names {
		fields[i] += pkg.IfThenElse(i == entity.primary, "\t@Id\n\t@GeneratedValue(strategy = GenerationType.IDENTITY)\n", "").(string)
		if strings.HasPrefix(entity.col_type[i], "varchar") {
			fields[i] += "\t@Size(max = " + entity.col_type[i][8:len(entity.col_type[i])-1] + ")\n"
		}
		fields[i] += "\t@Column(name = \"" + col + "\"" + pkg.IfThenElse(entity.col_null[i], "", ", nullable = false").(string) + pkg.IfThenElse(entity.col_unique[i], ", unique = true", "").(string) + ", columnDefinition = \"" + entity.col_type[i] + "\")\n"
		fields[i] += "\tprivate " + entity.col_javatype[i] + " " + col + ";\n\n"

	}

	for _, f := range fields {
		base += f
	}

	// Constructor
	var constr string = "\tprotected " + entity.name_upper + "Entity() {}\n\n"

	constr += "\tpublic " + entity.name_upper + "Entity("

	for i, col := range entity.col_names {
		constr += entity.col_javatype[i] + " " + col + pkg.IfThenElse(i == len(entity.col_names)-1, "", ", ").(string)
	}
	constr += ") {\n"

	for _, col := range entity.col_names {
		constr += "\t\tthis." + col + " = " + col + ";\n"
	}
	constr += "\t}\n\n"

	constr += "\tpublic " + entity.name_upper + "Entity("
	for i, col := range entity.col_names {
		if i == 0 {
			continue
		}
		constr += entity.col_javatype[i] + " " + col + pkg.IfThenElse(i == len(entity.col_names)-1, "", ", ").(string)
	}
	constr += ") {\n"

	for i, col := range entity.col_names {
		if i == 0 {
			continue
		}
		constr += "\t\tthis." + col + " = " + col + ";\n"
	}
	constr += "\t}\n\n"

	base += constr

	// Getter, Setter
	var getset string = ""

	for i, col := range entity.col_names {
		getset += "\tpublic void set" + strings.ToUpper(col[0:1]) + col[1:] + "(" + entity.col_javatype[i] + " " + col + ") {\n"
		getset += "\t\tthis." + col + " = " + col + ";\n"
		getset += "\t}\n\n"

		getset += "\tpublic " + entity.col_javatype[i] + " get" + strings.ToUpper(col[0:1]) + col[1:] + "() {\n"
		getset += "\t\treturn this." + col + ";\n"
		getset += "\t}\n\n"
	}

	base += getset

	// Object equals
	base += `	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ` + entity.name_upper + `Entity)) return false;
		` + entity.name_upper + `Entity that = (` + entity.name_upper + `Entity) o;
		return ` //application_id == that.application_id && Objects.equals(application_name, that.application_name); }`

	for i, col := range entity.col_names {
		base += "Objects.equals(" + col + ", that." + col + ")" + pkg.IfThenElse(i == len(entity.col_names)-1, ";\n\t}\n\n", " && ").(string)
	}

	// Object hashCode
	base += `	@Override
	public int hashCode() {
		return Objects.hash(`

	for i, col := range entity.col_names {
		base += col + pkg.IfThenElse(i == len(entity.col_names)-1, ");\n\t}\n\n", ", ").(string)
	}

	// Object toString
	base += `	@Override
	public String toString() {
		return "` + entity.name_upper + "Entity{"

	for i, col := range entity.col_names {
		base += col + "=" + pkg.IfThenElse(entity.col_javatype[i] == "String", "'\" + "+col+" + \"'", "\" + "+col+" + \"").(string)
		base += pkg.IfThenElse(i == len(entity.col_names)-1, "", ", ").(string)
	}
	base += "}\";\n\t}\n"

	base += "}"

	return base
}

func produceRepositoryFile(entity Entity) string {

	var base string = `package com.atopion.UGC_repository.rep.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ` + entity.name_upper + `Repository extends JpaRepository<` + entity.name_upper + `Entity, Integer> {

	@Query("select a from ` + entity.name_upper + `Entity a where `

	for i, col := range entity.col_names {
		if entity.col_javatype[i] == "String" || entity.col_javatype[i] == "Date" {
			base += "(?" + strconv.Itoa(i+1) + " is null or a." + col + " like %?" + strconv.Itoa(i+1) + "%)" + pkg.IfThenElse(i == len(entity.col_names)-1, "", " and ").(string)
		} else if entity.col_javatype[i] == "int" {
			base += "(?" + strconv.Itoa(i+1) + " is null or a." + col + " = ?" + strconv.Itoa(i+1) + ")" + pkg.IfThenElse(i == len(entity.col_names)-1, "", " and ").(string)
		}
	}

	base += `")
	List<` + entity.name_upper + `Entity> findByParams(`

	for i, col := range entity.col_names {
		base += pkg.IfThenElse(entity.col_javatype[i] == "int", "Integer", entity.col_javatype[i]).(string)
		base += " " + col + pkg.IfThenElse(i == len(entity.col_names)-1, "", ", ").(string)
	}

	base += `);

}`

	return base

}

func produceControllerFile(entity Entity) string {

	var base string = `package com.atopion.UGC_repository.rep.rest;

import com.atopion.UGC_repository.rep.entities.` + entity.name_upper + `Entity;
import com.atopion.UGC_repository.rep.entities.` + entity.name_upper + `Repository;
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
@RequestMapping("/rest/` + entity.name + `")
public class ` + entity.name_upper + `Controller {

	private final List<String> table_headers = Arrays.asList("` + strings.Join(entity.col_names, "\", \"") + `");

	private final ` + entity.name_upper + `Repository repository;

	` + entity.name_upper + `Controller(` + entity.name_upper + `Repository repository) {
		this.repository = repository;
	}
`

	base += "\n"

	for _, method := range []string{"get", "post", "put", "patch", "delete", "helpers"} {

		var method_up string = strings.ToUpper(method[0:1]) + method[1:]

		for m_idx, media := range []string{"application/json", "application/xml", "text/csv", "text/html"} {
			if method == "get" {
				if m_idx == 0 {
					base += "// =============================================  GET REQUESTS  ========================================================\n\n"
				}

				for i := 0; i < 2; i++ {
					base += "\t@" + method_up + "Mapping(path = " + pkg.IfThenElse(i == 0, "\"\"", "\"{"+entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:]+"}\"").(string)
					base += ", produces = \"" + media + "\")\n"
					base += "\tpublic " + pkg.IfThenElse(!strings.Contains(media, "html"), "@ResponseBody ", "").(string)
					base += pkg.IfThenElse(!strings.Contains(media, "text"), pkg.IfThenElse(i == 0, "List<"+entity.name_upper+"Entity> ", entity.name_upper+"Entity ").(string), "String ").(string)
					base += "getAll" + strings.ToUpper(media[strings.Index(media, "/")+1:]) + pkg.IfThenElse(i == 0, "(", "ById(").(string)
					for x, col := range entity.col_names {
						if x == entity.primary {
							if i == 0 {
								continue
							}
							base += "@PathVariable " + entity.col_javatype[x] + " " + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:]
						} else {
							if i == 1 {
								continue
							}
							base += "@RequestParam(name = \"" + col[strings.Index(col, "_")+1:] + "\", required = false) " + pkg.IfThenElse(entity.col_javatype[x] == "int", "Integer", entity.col_javatype[x]).(string) + " " + col + pkg.IfThenElse(x == len(entity.col_names)-1, "", ", ").(string)
						}
					}

					if media == "text/html" {
						base += ", Model model"
					}

					base += ") {\n"

					if strings.Contains(media, "csv") {
						if i == 0 {
							base += "\t\treturn CSVSerializer.serialize(repository.findByParams(null, " + strings.Join(entity.col_names[1:], ", ") + "), table_headers);"
						} else {
							base += "\t\t" + entity.name_upper + "Entity entity = repository.findById(" + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + ").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));\n"
							base += "\t\treturn CSVSerializer.serialize(Collections.singletonList(entity), table_headers);"
						}
					} else if strings.Contains(media, "html") {
						var getter []string = make([]string, len(entity.col_names))
						for i, c := range entity.col_names {
							if entity.col_javatype[i] == "int" {
								getter[i] = "Integer.toString(e.get" + strings.ToUpper(c[0:1]) + c[1:] + "())"
							} else {
								getter[i] = "e.get" + strings.ToUpper(c[0:1]) + c[1:] + "()"
							}
						}
						if i == 0 {
							base += `
		List<` + entity.name_upper + `Entity> entities = repository.findByParams(null, ` + strings.Join(entity.col_names[1:], ", ") + `);

		List<List<String>> rows = entities.stream()
				.map(e -> List.of(` + strings.Join(getter, ", ") + `))
				.collect(Collectors.toList());

		if(entities.size() == 0) {
			model.addAttribute("table_headers", table_headers);
			model.addAttribute("table_rows", new String[0]);
		}
		else {
			model.addAttribute("table_headers", table_headers);
			model.addAttribute("table_rows", rows);

		}

		return "sqlResult";`
						} else {
							base += `		` + entity.name_upper + `Entity e = repository.findById(` + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + `).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		List<List<String>> row = Collections.singletonList(List.of(` + strings.Join(getter, ", ") + `));

		model.addAttribute("table_headers", table_headers);
		model.addAttribute("table_rows", row);

		return "sqlResult";`
						}

					} else {
						if i == 0 {
							base += "\t\treturn repository.findByParams(null, " + strings.Join(entity.col_names[1:], ", ") + ");"
						} else {
							base += "\t\treturn repository.findById(" + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + ").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));"
						}
					}

					base += "\n\t}\n\n"

				}
			} else if method == "post" {
				if m_idx == 0 {
					base += "// ============================================  POST REQUESTS  ========================================================\n\n"
				}

				if media == "text/html" {
					media = "/XFORM"
				}

				var getter []string = make([]string, 0)
				for i, c := range entity.col_names {
					if i == 0 {
						continue
					}
					getter = append(getter, entity.name+"Entity"+".get"+strings.ToUpper(c[0:1])+c[1:]+"()")
				}

				base += `	@PostMapping(path = "", consumes = "` + pkg.IfThenElse(media == "/XFORM", "application/x-www-form-urlencoded", media).(string) + `", produces = { "application/json", "application/xml" })
	public @ResponseBody ` + entity.name_upper + `Entity postNew` + entity.name_upper + strings.ToUpper(media)[strings.Index(media, "/")+1:] + "(" + pkg.IfThenElse(media == "/XFORM", "", "@RequestBody ").(string) + pkg.IfThenElse(media == "text/csv", "String data", entity.name_upper+`Entity `+entity.name+`Entity`).(string) + ") {\n"

				var t string = ""
				if media == "text/csv" {
					base += "\t\t" + entity.name_upper + "Entity " + entity.name + "Entity = readCSV(data);\n\n"
					base += "\t\tif(" + entity.name + "Entity != null) {\n"
					t = "\t"
				}

				base += t + "\t\ttry {\n"
				base += t + "\t\t\treturn repository.save(new " + entity.name_upper + `Entity(` + strings.Join(getter, ", ") + "));\n"
				base += t + "\t\t} catch (Exception ex) {\n"
				base += t + "\t\t\tthrow new ResponseStatusException(HttpStatus.BAD_REQUEST);\n"
				base += t + "\t\t}\n"
				base += t + "\t}\n"

				if media == "text/csv" {
					base += `		throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	}
`
				}
				base += "\n"

			}

			if method == "helpers" && m_idx == 0 {

				base += `
// ================================================ Helpers ============================================================

	private ` + entity.name_upper + `Entity readCSV(String csv) {
		String[] lines = csv.split("\n");
`
				for i, col := range entity.col_names {
					base += "\t\t" + entity.col_javatype[i] + " " + col + " = " + pkg.IfThenElse(entity.col_javatype[i] == "int", "-1;\n", "null;\n").(string)
				}

				base += "\n"
				base += `
		String[] headers = lines[0].split(",");
		for(int i = 0; i < headers.length; i++) {
`
				for i, col := range entity.col_names {
					base += "\t\t\t" + pkg.IfThenElse(i == 0, "if", "else if").(string) + "(headers[i].equals(\"" + col + "\"))\n"
					base += "\t\t\t\ttry {\n"
					if entity.col_javatype[i] == "int" {
						base += "\t\t\t\t\t" + col + " = Integer.parseInt(lines[1].split(\",\")[i]);\n"
						base += "\t\t\t\t" + "} catch (Exception e) {\n"
						base += "\t\t\t\t\t" + col + " = -1;\n"
					} else if entity.col_javatype[i] == "Date" {
						base += "\t\t\t\t\t" + col + " = DateFormat.parse(lines[1].split(\",\")[i]);\n"
						base += "\t\t\t\t" + "} catch (Exception e) {\n"
						base += "\t\t\t\t\t" + col + " = null;\n"
					} else {
						base += "\t\t\t\t\t" + col + " = lines[1].split(\",\")[i];\n"
						base += "\t\t\t\t" + "} catch (Exception e) {\n"
						base += "\t\t\t\t\t" + col + " = null;\n"
					}
					base += "\t\t\t\t}\n"
				}
				base += "\t\t}\n"

				var e_col []string = make([]string, 0)
				for i, col := range entity.col_names {
					if i == entity.primary {
						continue
					}
					e_col = append(e_col, col+" == "+pkg.IfThenElse(entity.col_javatype[i] == "int", "-1", "null").(string))
				}

				base += "\n"

				base += "\t\tif(" + strings.Join(e_col, " || ") + ")\n"
				base += "\t\t\treturn null;\n"

				base += `		else if(` + entity.col_names[entity.primary] + ` == ` + pkg.IfThenElse(entity.col_javatype[entity.primary] == "int", "-1", "null").(string) + `)
			return new ` + entity.name_upper + `Entity(` + strings.Join(entity.col_names[1:], ", ") + `);
		else
			return new ` + entity.name_upper + `Entity(` + strings.Join(entity.col_names, ", ") + `);
	}
`
			}

			if method == "delete" && m_idx == 0 {
				base += `
// ============================================== DELETE REQUEST =======================================================

	@DeleteMapping(path = "{` + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + `}")
	public ResponseEntity<String> deleteById(@PathVariable ` + entity.col_javatype[entity.primary] + " " + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + `) {
		try {
			repository.deleteById(` + entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:] + `);
		} catch (Exception e) {
			return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("", HttpStatus.NO_CONTENT);
	}

`
			}

			if method == "put" {

				var id string = entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:]
				var id_type string = entity.col_javatype[entity.primary]
				var single_entity string = pkg.IfThenElse(strings.HasSuffix(entity.name_upper, "s"), entity.name_upper[0:len(entity.name_upper)-1], entity.name_upper).(string)

				var getter []string = make([]string, 0)
				var setter []string = make([]string, 0)
				for i, col := range entity.col_names {
					if i == entity.primary {
						continue
					}
					getter = append(getter, "param.get"+strings.ToUpper(col[0:1])+col[1:]+"()")
					setter = append(setter, "entity.set"+strings.ToUpper(col[0:1])+col[1:]+"(")
				}

				if media == "text/html" {
					media = "/XFORM"
				}

				if m_idx == 0 {
					base += `// =============================================  PUT REQUESTS  ========================================================

`
				}

				for i := 0; i < 2; i++ {

					base += `	@PutMapping(path = "` + pkg.IfThenElse(i == 0, "{"+id+"}", "").(string) + `", consumes = "`
					base += pkg.IfThenElse(media == "/XFORM", "application/x-www-form-urlencoded", media).(string) + `", produces = { "application/json", "application/xml" })
`
					base += `	public ResponseEntity<` + entity.name_upper + `Entity> put` + single_entity + strings.ToUpper(media[strings.Index(media, "/")+1:]) + pkg.IfThenElse(i == 0, "ById(@PathVariable "+id_type+" "+id+", ", "(").(string) + pkg.IfThenElse(media != "/XFORM", "@RequestBody ", "").(string) + pkg.IfThenElse(strings.Contains(media, "csv"), "String data", entity.name_upper+`Entity param`).(string) + `) {
`
					if !strings.Contains(media, "csv") {
						base += `		try {
			return put` + single_entity + `Internal(param, ` + pkg.IfThenElse(i == 0, "id", "param.get"+strings.ToUpper(entity.col_names[entity.primary][0:1])+entity.col_names[entity.primary][1:]+"()").(string) + `);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
`
					} else {
						base += `		` + entity.name_upper + `Entity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return put` + single_entity + `Internal(param, ` + pkg.IfThenElse(i == 0, "id", "param.get"+strings.ToUpper(entity.col_names[entity.primary][0:1])+entity.col_names[entity.primary][1:]+"()").(string) + `);
`
					}

					base += "\t}\n\n"

				}

				if m_idx == 3 {
					base += `
	private ResponseEntity<` + entity.name_upper + `Entity> put` + single_entity + `Internal(` + entity.name_upper + `Entity param, ` + id_type + " " + id + `) {
		Optional<` + entity.name_upper + `Entity> opt = repository.findById(` + id + `);
		if(opt.isPresent()) {
			` + entity.name_upper + `Entity entity = opt.get();
`
					for g := 0; g < len(getter); g++ {
						base += "\t\t\t" + setter[g] + getter[g] + ");\n"
					}

					base += `			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.OK);
		} else {
			` + entity.name_upper + `Entity entity;
			if(` + id + ` != ` + pkg.IfThenElse(id_type == "int", "0", "null").(string) + `)
				entity = new ` + entity.name_upper + `Entity(` + id + `, ` + strings.Join(getter, ", ") + `);
			else
				entity = new ` + entity.name_upper + `Entity(` + strings.Join(getter, ", ") + `);
			repository.save(entity);
			return new ResponseEntity<>(entity, HttpStatus.CREATED);
		}
	}						
`
				}
			}
			if method == "patch" {

				var id string = entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:]
				var id_type string = entity.col_javatype[entity.primary]
				var single_entity string = pkg.IfThenElse(strings.HasSuffix(entity.name_upper, "s"), entity.name_upper[0:len(entity.name_upper)-1], entity.name_upper).(string)

				var getter []string = make([]string, 0)
				var setter []string = make([]string, 0)
				for i, col := range entity.col_names {
					if i == entity.primary {
						continue
					}
					getter = append(getter, "param.get"+strings.ToUpper(col[0:1])+col[1:]+"()")
					setter = append(setter, "entity.set"+strings.ToUpper(col[0:1])+col[1:]+"(")
				}

				if m_idx == 0 {
					base += `// ============================================= PATCH REQUESTS ========================================================

`
				}

				if media == "text/html" {
					media = "/XFORM"
				}

				for i := 0; i < 2; i++ {

					base += `	@PatchMapping(path = "` + pkg.IfThenElse(i == 0, "{"+id+"}", "").(string) + `", consumes = "`
					base += pkg.IfThenElse(media == "/XFORM", "application/x-www-form-urlencoded", media).(string) + `", produces = { "application/json", "application/xml" })
`
					base += `	public ResponseEntity<` + entity.name_upper + `Entity> patch` + single_entity + strings.ToUpper(media[strings.Index(media, "/")+1:]) + pkg.IfThenElse(i == 0, "ById(@PathVariable "+id_type+" "+id+", ", "(").(string) + pkg.IfThenElse(media != "/XFORM", "@RequestBody ", "").(string) + pkg.IfThenElse(strings.Contains(media, "csv"), "String data", entity.name_upper+`Entity param`).(string) + `) {
`
					if !strings.Contains(media, "csv") {
						base += `		try {
			return patch` + single_entity + `Internal(param, ` + pkg.IfThenElse(i == 0, "id", "param.get"+strings.ToUpper(entity.col_names[entity.primary][0:1])+entity.col_names[entity.primary][1:]+"()").(string) + `);
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
`
					} else {
						base += `		` + entity.name_upper + `Entity param = readCSV(data);
		if(param == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return patch` + single_entity + `Internal(param, ` + pkg.IfThenElse(i == 0, "id", "param.get"+strings.ToUpper(entity.col_names[entity.primary][0:1])+entity.col_names[entity.primary][1:]+"()").(string) + `);
`
					}

					base += "\t}\n\n"

				}

				if m_idx == 3 {
					base += `
	private ResponseEntity<` + entity.name_upper + `Entity> patch` + single_entity + `Internal(` + entity.name_upper + `Entity param, ` + id_type + " " + id + `) {
		` + entity.name_upper + `Entity entity = repository.findById(` + id + `)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

`
					for g := 0; g < len(getter); g++ {
						base += "\t\tif(" + getter[g] + " != " + pkg.IfThenElse(entity.col_javatype[g+1] == "int", "-1", "null").(string) + ")\n"
						base += "\t\t\t" + setter[g] + getter[g] + ");\n"
					}

					base += `
		repository.save(entity);
		return new ResponseEntity<>(entity, HttpStatus.OK);
	}
`
				}
			}

		}

	}

	base += "}\n"

	return base
}

func produceControllerTestFile(entity Entity) string {

	var base string = `package com.atopion.UGC_repository.rep.rest;

import com.atopion.UGC_repository.rep.entities.` + entity.name_upper + `Entity;
import com.atopion.UGC_repository.rep.entities.` + entity.name_upper + `Repository;
import com.atopion.UGC_repository.testutil.AccessTokens;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-integrationtests.properties")
public class ` + entity.name_upper + `ControllerTest {

`
	// Add demo content
	var json string = "\tprivate final String[] resultJSON  = new String[]{"
	var xml string = "\tprivate final String[] resultXML   = new String[]{"
	var csv string = "\tprivate final String[] resultCSV   = new String[]{"
	var html string = "\tprivate final String[] resultHTML  = new String[]{"
	var xform string = "\tprivate final String[] resultXFORM = new String[]{"

	var content [][]string = make([][]string, 4)
	for x := 0; x < 4; x++ {
		var mdx int = pkg.IfThenElse(x == 0, 1, x).(int)
		content[x] = make([]string, len(entity.col_names))
		for i, col := range entity.col_names {
			content[x][i] = pkg.IfThenElse(entity.col_javatype[i] == "int", strconv.Itoa(mdx), pkg.IfThenElse(entity.col_javatype[i] == "String", "demo_"+col[strings.Index(col, "_")+1:]+strconv.Itoa(mdx), "200"+strconv.Itoa(mdx)+"-01-01 0:00:00")).(string)
		}
	}

	for i := 0; i < 4; i++ {

		if i == 0 {
			csv += "\"" + strings.Join(entity.col_names, ",") + "\", "
			html += "\"<tr>\\n<th>" + strings.Join(entity.col_names, "</th>\\n<th>") + "</th>\\n</tr>\", "
		} else {
			json += `"{`
			xml += `"`
			csv += `"`
			html += `"<tr>\n`
			xform += `"`
			for x, col := range entity.col_names {

				json += `\"` + col + `\":` + pkg.IfThenElse(entity.col_javatype[x] != "int", `\"`+content[i][x]+`\"`, content[i][x]).(string)
				xml += "<" + col + ">" + content[i][x] + "</" + col + ">"
				csv += content[i][x]
				html += "<td>" + content[i][x] + "</td>\\n"
				xform += col + "=" + content[i][x]

				if x != len(entity.col_names)-1 {
					json += ","
					csv += ","
					xform += "&"
				}
			}
			json += `}"`
			xml += `"`
			csv += `"`
			html += `</tr>"`
			xform += `"`
		}

		if i != 3 && i != 0 {
			csv += ", "
			html += ", "
			json += ", "
			xml += ", "
			xform += ", "
		}
	}

	base += json + "};\n"
	base += xml + "};\n"
	base += csv + "};\n"
	base += html + "};\n"
	base += xform + "};\n\n"

	base += `	@Autowired
	private MockMvc mvc;

	@Autowired
	private ` + entity.name_upper + `Repository repository;

	@Autowired
	private DataSource dataSource;

	private ` + entity.name_upper + `Entity demo1, demo3;

	@BeforeEach
	void setUp() {
		demo1 = new ` + entity.name_upper + `Entity("` + strings.Join(content[1][1:], "\", \"") + `");
		` + entity.name_upper + `Entity demo2 = new ` + entity.name_upper + `Entity("` + strings.Join(content[2][1:], "\", \"") + `");
		
		demo3 = new ` + entity.name_upper + `Entity(3, "` + strings.Join(content[3][1:], "\", \"") + `");

		repository.save(demo1);
		repository.save(demo2);

		repository.flush();
	}

	@AfterEach
	void tearDown() {
		repository.deleteAll();
		repository.flush();

		try (Connection connection = dataSource.getConnection();
			Statement statement = connection.createStatement()) {
			statement.executeUpdate("ALTER TABLE ` + entity.name + ` ALTER COLUMN ` + entity.col_names[entity.primary] + ` RESTART WITH 1;");

		} catch (Exception e) {
			fail("Unexpected exception occured: " + e.getMessage());
		}
	}
`

	// Add test cases

	var id string = entity.col_names[entity.primary][strings.Index(entity.col_names[entity.primary], "_")+1:]
	var id_type string = entity.col_javatype[entity.primary]
	var single_entity string = pkg.IfThenElse(strings.HasSuffix(entity.name_upper, "s"), entity.name_upper[0:len(entity.name_upper)-1], entity.name_upper).(string)

	var getter []string = make([]string, 0)
	var setter []string = make([]string, 0)
	for _, col := range entity.col_names {
		getter = append(getter, ".get"+strings.ToUpper(col[0:1])+col[1:]+"()")
		setter = append(setter, ".set"+strings.ToUpper(col[0:1])+col[1:]+"(")
	}
	var param_i int = pkg.IfThenElse(entity.primary == 0, 1, 0).(int)
	var param string = entity.col_names[param_i][strings.Index(entity.col_names[param_i], "_")+1:] + "=\" + demo1" + getter[param_i]
	var token string = "/\" + demo1" + getter[entity.primary]
	//var token_3 string = "/\" + demo3" + getter[entity.primary]
	var counter int = 0

	//var content_json string = ""

	_ = id
	_ = id_type
	_ = single_entity

	for i, method := range []string{"GET", "POST", "PUT", "PATCH", "DELETE"} {

		for k, byid := range []string{"", "_id", "_param"} {

			for l, ty := range []string{"format", "accept"} {

				for j, media := range []string{"JSON", "XML", "CSV", "HTML", "XFORM"} {

					_ = i
					_ = j
					_ = k
					_ = byid
					_ = l

					// ================================== FUNCTIONALITY ==================================================

					// GET
					if method == "GET" && media != "XFORM" {

						var mediatype string = "MediaType." + pkg.IfThenElse(media == "CSV", "valueOf(\"text/csv\")", pkg.IfThenElse(media == "HTML", "TEXT_HTML", "APPLICATION_"+pkg.IfThenElse(media == "XFORM", "FORM_URLENCODED", media).(string))).(string)

						counter++

						base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_functionality() {

		try {
`
						base += `			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name
						base += pkg.IfThenElse(byid == "_id", token, "").(string)
						base += pkg.IfThenElse(byid == "_id" && ty == "format", " + \"", "").(string)
						base += pkg.IfThenElse(byid == "_param", "?"+param, "").(string)
						base += pkg.IfThenElse(byid == "_id" && ty != "format", "", "").(string)
						base += pkg.IfThenElse(byid == "" && ty != "format", "\"", "").(string)
						base += pkg.IfThenElse(byid == "_param" && ty == "format", " + \"&", pkg.IfThenElse(ty == "format", "?", "")).(string)
						base += pkg.IfThenElse(ty == "format", "format="+strings.ToLower(media)+`"))`, `)).accept(`+mediatype+`)`).(string) + `).andReturn();
`
						base += `
			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(` + pkg.IfThenElse(media == "CSV", `"text/csv"`, mediatype+`_VALUE`).(string) + `));
`
						base += `
			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(result` + media + "[0]));\n"

						if media == "CSV" || media == "HTML" {
							base += `			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(result` + media + "[1]));\n"
						}

						if byid == "" {
							base += `			assertThat("Could not find expected content. ", result.getResponse().getContentAsString().replace(" ", "").replace("\t", ""), containsString(result` + media + pkg.IfThenElse(media == "CSV" || media == "HTML", "[2]", "[1]").(string) + "));\n"
						}

						base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						// POST

					} else if method == "POST" && media != "HTML" && byid == "" && ty == "format" {
						var mediatype string = "MediaType." + pkg.IfThenElse(media == "CSV", "valueOf(\"text/csv\")", pkg.IfThenElse(media == "HTML", "TEXT_HTML", "APPLICATION_"+pkg.IfThenElse(media == "XFORM", "FORM_URLENCODED", media).(string))).(string)

						counter++

						base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_functionality() {

		try {
`

						var content string = ""
						if media == "JSON" {
							content = `"{" + resultJSON[2].substring(resultJSON[2].indexOf(",")+1)`
						} else if media == "XML" {
							content = `"<entity>" + resultXML[2].substring(resultXML[2].indexOf(">", resultXML[2].indexOf(">")+1)+1) + "</entity>"`
						} else if media == "CSV" {
							content = `resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[3].substring(resultCSV[3].indexOf(",")+1)`
						} else if media == "XFORM" {
							content = `resultXFORM[2].substring(resultXFORM[2].indexOf("&")+1)`
						}

						base += `
			String content = ` + content + `;
`
						base += `			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name
						base += pkg.IfThenElse(media == "XML", `?format=xml"`, `"`).(string)
						base += `)).contentType(` + mediatype + `).content(content)).andReturn();
`
						base += `
			assertEquals("Wrong status code. ", 200, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(` + pkg.IfThenElse(media == "XML", mediatype, "MediaType.APPLICATION_JSON").(string) + `_VALUE));

`
						base += `
			assertTrue("Could not find expected id 3: ", repository.existsById(3));
			assertEquals("Could not find expected entry: ", demo3, repository.findById(3).orElse(null));
`

						base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						// POST - robustness

						for x := 0; x < 2; x++ {

							counter++

							base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_robustness_` + strconv.Itoa(x+1) + `() {

		try {
`

							var content string = ""
							var prim string = entity.col_names[entity.primary]
							if media == "JSON" {
								content = pkg.IfThenElse(x == 0, `"{\"`+prim+`\":4}"`, `""`).(string)
							} else if media == "XML" {
								content = pkg.IfThenElse(x == 0, `"<entity><`+prim+">"+"4"+"</"+prim+`></entity>"`, `""`).(string)
							} else if media == "CSV" {
								content = pkg.IfThenElse(x == 0, `"`+prim+`\n4"`, `""`).(string)
							} else if media == "XFORM" {
								content = pkg.IfThenElse(x == 0, `"`+prim+"=4"+`"`, `""`).(string)
							}

							base += `
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + `"`
							base += `)).contentType(` + mediatype + `).content(` + content + `)).andReturn();
`
							base += `
			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());
`
							base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						}

						// PUT

					} else if method == "PUT" && media != "HTML" && byid != "_param" && ty == "format" {
						var mediatype string = "MediaType." + pkg.IfThenElse(media == "CSV", "valueOf(\"text/csv\")", pkg.IfThenElse(media == "HTML", "TEXT_HTML", "APPLICATION_"+pkg.IfThenElse(media == "XFORM", "FORM_URLENCODED", media).(string))).(string)

						for x := 0; x < 2; x++ {

							counter++

							base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_functionality_` + strconv.Itoa(x+1) + `() {

		try {
`

							var content string = ""
							var idx string = pkg.IfThenElse(x == 0, "2", "0").(string)
							var idx_1 string = pkg.IfThenElse(x == 0, "3", "1").(string)
							if media == "JSON" {
								if byid != "" {
									content = `"{" + resultJSON[` + idx + `].substring(resultJSON[` + idx + `].indexOf(",")+1)`
								} else {
									content = `resultJSON[` + idx + `]`
								}
							} else if media == "XML" {
								if byid != "" {
									content = `"<entity>" + resultXML[` + idx + `].substring(resultXML[` + idx + `].indexOf(">", resultXML[` + idx + `].indexOf(">")+1)+1) + "</entity>"`
								} else {
									content = `"<entity>" + resultXML[` + idx + `] + "</entity>"`
								}
							} else if media == "CSV" {
								if byid != "" {
									content = `resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[` + idx_1 + `].substring(resultCSV[` + idx_1 + `].indexOf(",")+1)`
								} else {
									content = `resultCSV[0] + "\n" + resultCSV[` + idx_1 + `]`
								}
							} else if media == "XFORM" {
								if byid != "" {
									content = `resultXFORM[` + idx + `].substring(resultXFORM[` + idx + `].indexOf("&")+1)`
								} else {
									content = `resultXFORM[` + idx + `]`
								}
							}

							base += `
			String content = ` + content + `;
`

							base += `
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + "/"
							base += pkg.IfThenElse(byid == "", "", idx_1).(string) + pkg.IfThenElse(media == "XML", "?format=xml", "").(string)

							base += `")).contentType(` + mediatype + `)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", ` + pkg.IfThenElse(x == 0, "201", "200").(string) + `, result.getResponse().getStatus());
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(` + pkg.IfThenElse(media == "XML", mediatype, "MediaType.APPLICATION_JSON").(string) + `_VALUE));

			assertTrue("Could not find expected id ` + idx_1 + `: ", repository.existsById(` + idx_1 + `));
			assertEquals("Could not find expected entry: ", demo` + idx_1 + `, repository.findById(` + idx_1 + `).orElse(null));
`

							base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`
							// PUT - robustness

							counter++

							base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_robustness_` + strconv.Itoa(x+1) + `() {

		try {
`

							var prim string = entity.col_names[entity.primary]
							if media == "JSON" {
								content = pkg.IfThenElse(x == 0, `"{\"`+prim+`\":4}"`, `""`).(string)
							} else if media == "XML" {
								content = pkg.IfThenElse(x == 0, `"<entity><`+prim+">"+"4"+"</"+prim+`></entity>"`, `""`).(string)
							} else if media == "CSV" {
								content = pkg.IfThenElse(x == 0, `"`+prim+`\n4"`, `""`).(string)
							} else if media == "XFORM" {
								content = pkg.IfThenElse(x == 0, `"`+prim+"=4"+`"`, `""`).(string)
							}

							base += `
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + `"`
							base += `)).contentType(` + mediatype + `).content(` + content + `)).andReturn();
`
							base += `
			assertEquals("Wrong status code. ", 400, result.getResponse().getStatus());
`
							base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						}

						// PATCH - functionality

					} else if method == "PATCH" && media != "HTML" && byid != "_param" && ty == "format" {
						var mediatype string = "MediaType." + pkg.IfThenElse(media == "CSV", "valueOf(\"text/csv\")", pkg.IfThenElse(media == "HTML", "TEXT_HTML", "APPLICATION_"+pkg.IfThenElse(media == "XFORM", "FORM_URLENCODED", media).(string))).(string)

						for x := 0; x < 2; x++ {

							counter++

							base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + pkg.IfThenElse(x == 1, `_robustness_`, `_functionality_`).(string) + strconv.Itoa(x+1) + `() {

		try {
`

							var content string = ""
							var idx string = pkg.IfThenElse(x == 0, "0", "2").(string)
							var idx_1 string = pkg.IfThenElse(x == 0, "1", "3").(string)
							if media == "JSON" {
								if byid != "" {
									content = `"{" + resultJSON[` + idx + `].substring(resultJSON[` + idx + `].indexOf(",")+1)`
								} else {
									content = `resultJSON[` + idx + `]`
								}
							} else if media == "XML" {
								if byid != "" {
									content = `"<entity>" + resultXML[` + idx + `].substring(resultXML[` + idx + `].indexOf(">", resultXML[` + idx + `].indexOf(">")+1)+1) + "</entity>"`
								} else {
									content = `"<entity>" + resultXML[` + idx + `] + "</entity>"`
								}
							} else if media == "CSV" {
								if byid != "" {
									content = `resultCSV[0].substring(resultCSV[0].indexOf(",")+1) + "\n" + resultCSV[` + idx_1 + `].substring(resultCSV[` + idx_1 + `].indexOf(",")+1)`
								} else {
									content = `resultCSV[0] + "\n" + resultCSV[` + idx_1 + `]`
								}
							} else if media == "XFORM" {
								if byid != "" {
									content = `resultXFORM[` + idx + `].substring(resultXFORM[` + idx + `].indexOf("&")+1)`
								} else {
									content = `resultXFORM[` + idx + `]`
								}
							}

							base += `
			String content = ` + content + `;
`

							base += `
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + "/"
							base += pkg.IfThenElse(byid == "", "", idx_1).(string) + pkg.IfThenElse(media == "XML", "?format=xml", "").(string)

							base += `")).contentType(` + mediatype + `)
					.content(content)).andReturn();

			assertEquals("Wrong status code. ", ` + pkg.IfThenElse(x == 1, "404", "200").(string) + `, result.getResponse().getStatus());
`
							if x == 0 {
								base += `							
			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(` + pkg.IfThenElse(media == "XML", mediatype, "MediaType.APPLICATION_JSON").(string) + `_VALUE));

			assertTrue("Could not find expected id ` + idx_1 + `: ", repository.existsById(` + idx_1 + `));
			assertEquals("Could not find expected entry: ", demo` + idx_1 + `, repository.findById(` + idx_1 + `).orElse(null));
`
							}

							base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`
							// PATCH - robustness

							counter++

							base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_robustness_` + strconv.Itoa(pkg.IfThenElse(x == 0, 1, 3).(int)) + `() {

		try {
`

							var prim string = entity.col_names[entity.primary]
							if media == "JSON" {
								content = pkg.IfThenElse(x == 0, `"{\"`+prim+`\":4}"`, `""`).(string)
							} else if media == "XML" {
								content = pkg.IfThenElse(x == 0, `"<entity><`+prim+">"+"4"+"</"+prim+`></entity>"`, `""`).(string)
							} else if media == "CSV" {
								content = pkg.IfThenElse(x == 0, `"`+prim+`\n4"`, `""`).(string)
							} else if media == "XFORM" {
								content = pkg.IfThenElse(x == 0, `"`+prim+"=4"+`"`, `""`).(string)
							}

							base += `
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + `"`
							base += `)).contentType(` + mediatype + `).content(` + content + `)).andReturn();
`
							base += `
			assertEquals("Wrong status code. ", ` + pkg.IfThenElse((x == 0 && media != "CSV") || media == "XFORM", "404", "400").(string) + `, result.getResponse().getStatus());
`
							base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						}

						// DELETE - functionality
					} else if method == "DELETE" && media == "JSON" && byid == "" && ty == "format" {

						counter++

						base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_functionality() {

		try {

			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + `/1"))).andReturn();

			assertEquals("Wrong status code. ", 204, result.getResponse().getStatus());
			assertFalse("Content was not deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

						// DELETE - robustness

						counter++

						base += `
	@Test
	void test_` + method + "_" + media + byid + "_" + ty + `_robustness() {

		try {
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name + `/19"))).andReturn();

			assertEquals("Wrong status code. ", 500, result.getResponse().getStatus());
			assertTrue("Wrong content was deleted. ", repository.existsById(1));
			assertTrue("Wrong content was deleted. ", repository.existsById(2));

		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`

					}

				}
				//  GET - robustness

				if method == "GET" {

					counter++

					var _param string = entity.col_names[param_i][strings.Index(entity.col_names[param_i], "_")+1:] + "=xtc\""
					var _token string = "/21\""
					var _media string = "xlsx"
					var _mediatype string = "MediaType.IMAGE_GIF"
					//var _resultmedia string = "MediaType.APPLICATION_JSON"

					base += `
	@Test
	void test_` + method + "_TEST" + byid + "_" + ty + `_robustness() {

		try {
`

					base += `			
			MvcResult result = mvc.perform(AccessTokens.withValidHeaders(` + strings.ToLower(method) + `("/rest/` + entity.name
					base += pkg.IfThenElse(byid == "_id", _token, "").(string)
					base += pkg.IfThenElse(byid == "_param", "?"+_param, "").(string)
					base += pkg.IfThenElse(byid == "_id" && ty == "format", " + \"", "").(string)
					base += pkg.IfThenElse(byid == "_id" && ty != "format", "", "").(string)
					base += pkg.IfThenElse(byid == "" && ty != "format", "\"", "").(string)
					base += pkg.IfThenElse(byid == "_param" && ty == "format", " + \"&", pkg.IfThenElse(ty == "format", "?", "")).(string)
					base += pkg.IfThenElse(ty == "format", "format="+strings.ToLower(_media)+`"))`, `)).accept(`+_mediatype+`)`).(string) + `).andReturn();
`
					base += `
			assertEquals("Wrong status code. ", ` + pkg.IfThenElse(byid == "_id", "404", "200").(string) + `, result.getResponse().getStatus());`
					//			assertThat("Wrong content type. ", result.getResponse().getContentType(), containsString(` + pkg.IfThenElse(_media == "CSV", `"text/csv"`, _resultmedia+`_VALUE`).(string) + `));
					base += `

`

					base += `
		} catch (Exception e) {
			fail("Unexpected exception occurred: " + e.getMessage());
		}
	}
`
				}

			}
		}
	}

	base += "}\n"

	fmt.Println("Number of tests: " + strconv.Itoa(counter))

	return base

}

func main() {

	var file string
	var dir string

	flag.StringVar(&file, "input", "", "Input file containing Data-Definition-Language (required)")
	flag.StringVar(&file, "i", "", "Input file containing Data-Definition-Language (required) (shorthand)")

	flag.StringVar(&dir, "output", ".", "Output directory. If non is provided, the current directory will be used")
	flag.StringVar(&dir, "o", ".", "Output directory. If non is provided, the current directory will be used (shorthand)")

	flag.Parse()

	if file == "" {
		flag.Usage()
		return
	}

	if strings.HasSuffix(dir, "/") {
		dir = dir[0 : len(dir)-1]
	}

	base_dir := dir + "/src/main/java/com/atopion/UGC_repository/"
	test_base_dir := dir + "/src/test/java/com/atopion/UGC_repository/"

	entity_dir := pkg.IfThenElse(pathExists(base_dir+"entities"), base_dir+"entities", dir).(string)
	controller_dir := pkg.IfThenElse(pathExists(base_dir+"rest"), base_dir+"rest", dir).(string)
	test_dir := pkg.IfThenElse(pathExists(test_base_dir+"rest"), test_base_dir+"rest", dir).(string)

	fmt.Println("PATH: " + base_dir + "entities")

	fmt.Println("Input: " + file)

	text, err := ioutil.ReadFile(file)
	check(err)

	ddls := strings.Split(string(text), ";")

	for i, ddl := range ddls {
		if strings.Trim(ddl, "\t\n ;,") == "" {
			continue
		}

		ddl = ddl + ";"

		e := parseDDL(ddl)
		fmt.Printf("("+strconv.Itoa(i)+") Entity: %+v\n\n", e)

		fmt.Println("Producing: " + e.name_upper + "Entity.java")
		err := ioutil.WriteFile(entity_dir+"/"+e.name_upper+"Entity.java", []byte(produceEntityFile(e)), 0644)
		check(err)

		fmt.Println("\n\nProducing: " + e.name_upper + "Repository.java")
		err = ioutil.WriteFile(entity_dir+"/"+e.name_upper+"Repository.java", []byte(produceRepositoryFile(e)), 0644)
		check(err)

		fmt.Println("\n\nProducing: " + e.name_upper + "Controller.java")
		err = ioutil.WriteFile(controller_dir+"/"+e.name_upper+"Controller.java", []byte(produceControllerFile(e)), 0644)
		check(err)

		fmt.Println("\n\nProducing: " + e.name_upper + "ControllerTest.java")
		err = ioutil.WriteFile(test_dir+"/"+e.name_upper+"ControllerTest.java", []byte(produceControllerTestFile(e)), 0644)
		check(err)
	}

}
