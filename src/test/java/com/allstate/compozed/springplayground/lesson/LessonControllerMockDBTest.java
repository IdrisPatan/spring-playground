package com.allstate.compozed.springplayground.lesson;

import static org.hamcrest.Matchers.equalTo;

/**
 * Created by localadmin on 7/18/17.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LessonControllerMockDB.class)
public class LessonControllerMockDBTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository repository;

    // Create Post
    @Test
    public void createDelegatesToRepository() throws Exception {

        //Setup
        when(repository.save(any(LessonModel.class))).then(returnsFirstArg());

        final MockHttpServletRequestBuilder request = post("/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Mock me another one!\"}");

        //Exercise
        final ResultActions resultActions = mockMvc.perform(request);

        //Assert
        resultActions.andExpect(status().isOk())
                      .andExpect(jsonPath("$.title", equalTo("Mock me another one!")));


        verify(this.repository).save(any(LessonModel.class));
        //Tear Down
    }

    //List Get/Lessons
    @Test
    public void testList() throws Exception {
        LessonModel lesson = new LessonModel();
        lesson.setTitle("new lesson");

        when(repository.findAll()).thenReturn(Collections.singletonList(lesson));

        MockHttpServletRequestBuilder request = get("/lessons")
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions resultActions = mockMvc.perform(request);

        resultActions.andExpect(status().isOk())
                      .andExpect(jsonPath("$[0].title", equalTo("new lesson")));

        verify(this.repository).findAll();

    }

    // Read Get/Lessons/{id}
    @Test
    public void readIdFromListShouldReturnOneLesson() throws Exception {
        //Setup
        LessonModel lesson = new LessonModel();
        lesson.setId(new Long (66));
        lesson.setTitle("lesson day08");
        Long id = lesson.getId();
        when(repository.findOne(id)).thenReturn(lesson);
        //when(repository.findAll()).thenReturn(Arrays.asList(lesson));

        //Exercise
        MockHttpServletRequestBuilder request = get("/lessons/{id}", id);

        final ResultActions resultActions = mockMvc.perform(request);

        //Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("lesson day08")));

        verify(this.repository).findOne(id);
    }

    //Update Put /Lessons/{id}
    @Test
    public void updateOneLesson() throws Exception {

        LessonModel lesson = new LessonModel();
        lesson.setId(new Long(19));
        lesson.setTitle("updated lesson");
        Long id = lesson.getId();

        when(repository.findOne(id)).thenReturn(lesson);

        LessonModel anotherLesson = new LessonModel();
        anotherLesson.setTitle("Another lesson");
        anotherLesson.setId(new Long(19));

        when(repository.save(any(LessonModel.class))).thenReturn(anotherLesson);

        MockHttpServletRequestBuilder request = put ("/lessons/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Another lesson!\"}");

        final ResultActions resultActions = mockMvc.perform(request);

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is ("Another lesson")));

        verify(this.repository).findOne(id);
        verify(this.repository).save(any(LessonModel.class));

    }

    // Delete Delete/Lessons/{id}
    @Test
    public void deleteOneLesson() throws Exception {
        LessonModel lesson = new LessonModel();
        lesson.setId((99L));
        lesson.setTitle("Delete this lesson");
        Long id = lesson.getId();

        MockHttpServletRequestBuilder request = delete("/lessons/{id}", id);

        final ResultActions resultActions = mockMvc.perform(request);

        resultActions.andExpect(status().isOk());

        verify(this.repository).delete(id);
    }

    // Delete when id does not exist
//    @Test
//    public void () attemptToDeleteLessonIfIdDoesNotExist() throws Exception {
//
//
//    }

}