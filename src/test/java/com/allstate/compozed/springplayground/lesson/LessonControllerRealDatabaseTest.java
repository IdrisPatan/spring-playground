package com.allstate.compozed.springplayground.lesson;


        import org.junit.Test;
        import org.junit.runner.RunWith;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.annotation.Rollback;
        import org.springframework.test.context.junit4.SpringRunner;
        import org.springframework.test.web.servlet.MockMvc;

        import javax.transaction.Transactional;
        import java.util.Arrays;

        import static org.hamcrest.Matchers.is;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
        import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LessonControllerRealDatabaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LessonRepository repository;


    @Transactional
    @Rollback
    @Test
    public void listReturnsExistingLessons() throws Exception {

        // Setup
        final LessonModel lessonOne = new LessonModel();
        lessonOne.setTitle("Spelling 001 with Dale oLtts");

        final LessonModel lessonTwo = new LessonModel();
        lessonTwo.setTitle("ACID for CRUDL");

        repository.save(Arrays.asList(lessonOne, lessonTwo));

        // Exercise
        mockMvc.perform(get("/lessons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id", is(lessonOne.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is("Spelling 001 with Dale oLtts")));

        // Assert

    }

    @Transactional
    @Rollback
    @Test
    public void getReadLesson() throws Exception {

        //setup
        final LessonModel lesson = new LessonModel();
        lesson.setTitle("TV App");

        repository.save(lesson);

        Long id = lesson.getId();

        //Exercise
        mockMvc.perform(get("/lessons/{id}", id))//lesson.getId()))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$.title", is("TV App")))
                .andExpect(jsonPath("$.id", is(3)));

        // Assert
    }

}












