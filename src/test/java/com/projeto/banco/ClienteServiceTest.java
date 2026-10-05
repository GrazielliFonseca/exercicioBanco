package com.projeto.banco;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.projeto.banco.model.Cliente;
import com.projeto.banco.model.PessoaFisica;
import com.projeto.banco.model.PessoaJuridica;
import com.projeto.banco.repository.ClienteRepository;
import com.projeto.banco.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    @DisplayName("métodos específicos getCpf e getCnpj retornam o documento corretamente")
    void metodosEspecificosDocumento() {
        PessoaFisica pf = new PessoaFisica("Carlos", "98765432100", null);
        PessoaJuridica pj = new PessoaJuridica("Empresa SA", "12345678000188", null);

        assertEquals("98765432100", pf.getCpf());
        assertEquals("12345678000188", pj.getCnpj());
    }

    @Test
    @DisplayName("deve cadastrar um cliente com sucesso quando o documento não existe")
    void criarContaSucesso() {
        Cliente pf = new PessoaFisica("Ana Souza", "11144477705", "ana@email.com");

        when(clienteRepository.existsByDocumento(pf.getDocumento())).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(pf);

        Cliente clienteCriado = clienteService.criarConta(pf);

        assertNotNull(clienteCriado);
        assertEquals("11144477705", clienteCriado.getDocumento());
        verify(clienteRepository, times(1)).save(pf);
    }

    @Test
    @DisplayName("não deve permitir cadastrar cliente com documento duplicado")
    void criarContaDocumentoDuplicado() {
        Cliente pf2 = new PessoaFisica("Outra Ana", "11144477705", "outra@email.com");

        when(clienteRepository.existsByDocumento("11144477705")).thenReturn(true);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.criarConta(pf2);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("deve buscar cliente por ID com sucesso")
    void buscarPorIdSucesso() {
        Cliente pf = new PessoaFisica("Ana Souza", "11144477705", "ana@email.com");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));

        Cliente clienteEncontrado = clienteService.buscarPorId(1L);

        assertNotNull(clienteEncontrado);
        assertEquals("Ana Souza", clienteEncontrado.getNome());
    }

    @Test
    @DisplayName("deve retornar 404 ao buscar ID inexistente")
    void buscarPorIdInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.buscarPorId(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
    }
}