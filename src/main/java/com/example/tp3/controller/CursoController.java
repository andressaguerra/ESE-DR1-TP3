package com.example.tp3.controller;

import com.example.tp3.model.AlunoRepository;
import com.example.tp3.model.Curso;
import com.example.tp3.model.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private AlunoRepository alunoRepository;

    @PostMapping("/aluno/{alunoId}")
    public ResponseEntity<?> addCursoAluno(@PathVariable Long alunoId, @RequestBody Curso curso) {
        return alunoRepository.findById(alunoId).map(aluno -> {
            curso.getAlunos().add(aluno);
            Curso savedCurso = cursoRepository.save(curso);
            return ResponseEntity.ok(savedCurso);
        }).orElseThrow(() -> new RuntimeException("Aluno não encontrado com Id " + alunoId));
    }

    @PostMapping("/{cursoId}/alunos/{alunoId}")
    public ResponseEntity<?> addExistingCursoToAluno(@PathVariable Long cursoId, @PathVariable Long alunoId) {
        return cursoRepository.findById(cursoId).map(curso -> {
            return alunoRepository.findById(alunoId).map(aluno -> {
                curso.getAlunos().add(aluno);
                cursoRepository.save(curso);
                return ResponseEntity.ok().build();
            }).orElseThrow(() -> new RuntimeException("Aluno não encontrado com Id " + alunoId));
        }).orElseThrow(() -> new RuntimeException("Curso não encontrado com Id " + cursoId));
    }

    @GetMapping
    public List<Curso> getAllCursos() {
        return cursoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Curso getCurso(@PathVariable Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com Id " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurso(@PathVariable Long id) {
        return cursoRepository.findById(id).map(curso -> {
            cursoRepository.delete(curso);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new RuntimeException("Curso não encontrado com Id " + id));
    }

    @PutMapping("/{id}")
    public Curso updateCurso(@PathVariable Long id, @RequestBody Curso cursoUpdate) {
        return cursoRepository.findById(id).map(curso -> {
            curso.setTitulo(cursoUpdate.getTitulo());
            return cursoRepository.save(curso);
        }).orElseThrow(() -> new RuntimeException("Curso não encontrado com Id " + id));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<?> getAllCursosAluno(@PathVariable Long alunoId) {
        List<Curso> cursos = cursoRepository.findByAlunosId(alunoId);
        if(cursos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cursos);
    }
}
