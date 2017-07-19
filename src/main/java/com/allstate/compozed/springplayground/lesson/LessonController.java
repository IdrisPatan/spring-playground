package com.allstate.compozed.springplayground.lesson;

import org.springframework.web.bind.annotation.*;

/**
 * Created by localadmin on 7/18/17.
 */

@RestController
@RequestMapping("/lessons")
final class LessonController {

    private final LessonRepository repository;

    public LessonController(LessonRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    LessonModel create(@RequestBody final LessonModel lesson) {
        return repository.save(lesson);
    }

    @GetMapping
    Iterable<LessonModel> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    LessonModel read(@PathVariable final Long id) {
        return repository.findOne(id);
    }

    @PutMapping("/{id}")
    LessonModel update(@PathVariable final Long id, @RequestBody final LessonModel newLesson) {
        LessonModel foundLesson = repository.findOne(id);
        foundLesson.setTitle(newLesson.getTitle());
        return repository.save(foundLesson);
    }

    @DeleteMapping('/{idz')

}
