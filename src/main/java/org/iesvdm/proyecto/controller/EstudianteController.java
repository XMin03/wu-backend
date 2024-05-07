package org.iesvdm.proyecto.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.proyecto.model.entity.Aula;
import org.iesvdm.proyecto.model.entity.Estudiante;
import org.iesvdm.proyecto.model.view.EstudianteRow;
import org.iesvdm.proyecto.model.view.Option;
import org.iesvdm.proyecto.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/v1/api/estudiantes")
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private PasswordEncoder encoder;
    @GetMapping({"notBlocked"})
    public Page<EstudianteRow> allNotBlocked(@RequestParam(value = "buscar",defaultValue = "") String buscar,
                                           Pageable pageable) {
        log.info("Accediendo a todos los profesores");
        return this.estudianteService.allByFilterNotBlocked(buscar,pageable);
    }
    @GetMapping({"","/"})
    public Page<EstudianteRow> all(@RequestParam(value = "buscar",defaultValue = "") String buscar,
                                   Pageable pageable) {
        log.info("Accediendo a todas los estudiantes");
        return this.estudianteService.allByFilter(buscar,pageable);
    }
    @PostMapping({"","/"})
    public Estudiante save(@RequestBody Estudiante estudiante) {
        estudiante.setPassword(encoder.encode(estudiante.getPassword()));
        log.info("Guardando un estudiante");
        return this.estudianteService.save(estudiante);
    }
    @GetMapping("/{id}")
    public Estudiante one(@PathVariable("id") long id) {
        return this.estudianteService.one(id);
    }
    @GetMapping("/getAulas")
    public Set<Option> getAulas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Estudiante e=estudianteService.one(auth.getName());
        return this.estudianteService.getAulas(e.getId());
    }
    @GetMapping("/getAulas/{idAula}")
    public Aula oneAUla(@PathVariable("idAula") long idAula) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Estudiante e=estudianteService.one(auth.getName());
        if (e.getAulas().stream().noneMatch(aula -> aula.getId() == idAula)) {
            throw new AccessDeniedException("No eres estudiante de esa aula.");
        }
        return this.estudianteService.getAula(idAula);
    }
    @PutMapping("/{id}")
    public Estudiante replace(@PathVariable("id") long id, @RequestBody Estudiante estudiante) {
        return this.estudianteService.replace(id, estudiante);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id){
        this.estudianteService.delete(id);
    }
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/salirAula/{idAula}")
    public void salirAula(@PathVariable("idAula") long idAula){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Estudiante e = estudianteService.one(auth.getName());
        this.estudianteService.salirAula(e,idAula);
    }

}
