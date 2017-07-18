package com.allstate.compozed.springplayground.lesson;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonRepository repository;

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
    }

}