package br.newtonpaiva.lab.tcc.application.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import br.newtonpaiva.lab.tcc.api.alunos.response.AlunoResponse;
import br.newtonpaiva.lab.tcc.common.exception.NotFoundException;
import br.newtonpaiva.lab.tcc.domain.entity.Aluno;
import br.newtonpaiva.lab.tcc.domain.service.AlunoService;
import br.newtonpaiva.lab.tcc.domain.service.CursoService;

@ExtendWith(MockitoExtension.class)
public class AlunoControllerTest {

    @Mock
    private AlunoService alunoService;

    @Mock
    private CursoService cursoService;

    private AlunoController unit;

    @BeforeEach
    public void setup() {
        unit = new AlunoController(alunoService, cursoService);
    }

    @Test
    void test_getAll() {
        // given
        var alunos = new ArrayList<Aluno>();
        var expected = new ArrayList<AlunoResponse>();

        for (int i = 0; i < 3; i++) {
            alunos.add(new Aluno().withId(UUID.randomUUID().toString()));
            expected.add(AlunoResponse.buildFrom(alunos.get(i)));
        }

        // mock definitions
        Mockito.when(alunoService.getAlunos()).thenReturn(alunos);

        // test
        var result = unit.getAll();

        // assert
        assertEquals(201, result.getStatusCode().value());
        assertEquals(expected, result.getBody());

        // verify - garantir que o mock tenha sido chamado
        Mockito.verify(alunoService).getAlunos();
    }

    @Test
    void test_getById_withValidId_shouldReturnAluno() {
        // given
        var id = UUID.randomUUID().toString();
        var aluno = new Aluno().withId(id);

        // mock definitions
        Mockito.when(alunoService.getById(id)).thenReturn(aluno);

        // test
        var result = unit.getById(Optional.of(id));

        // assert
        assertEquals(200, result.getStatusCode().value());
        assertEquals(AlunoResponse.buildFrom(aluno), result.getBody());

        // verify
        Mockito.verify(alunoService).getById(id);
    }

    @Test
    void test_getById_withInvalidId_shouldThrowNotFoundException() {
        // given
        var id = UUID.randomUUID().toString();

        var expected = String.format("%s com ID [%s] não encontrado.", "Aluno", id);
        // mock definitions
        Mockito.when(alunoService.getById(id)).thenThrow(new NotFoundException(expected));

        // test
        try {
            unit.getById(Optional.of(id));
            fail("Expected NotFoundException");
        } catch (NotFoundException ex) {
            // assert
            assertEquals(expected, ex.getMessage());
        }

        // verify
        Mockito.verify(alunoService).getById(id);
    }

}
