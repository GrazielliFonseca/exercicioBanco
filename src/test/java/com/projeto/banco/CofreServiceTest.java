package com.projeto.banco;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
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
    @DisplayName("deve criar uma pessoa física com sucesso quando o documento for único")
    void criarPessoaFisicaSucesso() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");

        when(clienteRepository.existsByDocumento("12345678901")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(pf);

        Cliente clienteCriado = clienteService.criarConta(pf);

        assertNotNull(clienteCriado);
        assertEquals(0.0, clienteCriado.getSaldo()); // Verifica se inicializou o saldo com zero
        verify(clienteRepository, times(1)).save(pf);
    }

    @Test
    @DisplayName("deve criar uma pessoa jurídica com sucesso quando o documento for único")
    void criarPessoaJuridicaSucesso() {
        PessoaJuridica pj = new PessoaJuridica("Empresa LTDA", "12345678000199", "empresa@email.com");

        when(clienteRepository.existsByDocumento("12345678000199")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(pj);

        Cliente clienteCriado = clienteService.criarConta(pj);

        assertNotNull(clienteCriado);
        assertEquals(0.0, clienteCriado.getSaldo());
        verify(clienteRepository, times(1)).save(pj);
    }

    @Test
    @DisplayName("não deve permitir cadastrar cliente com documento duplicado")
    void criarClienteDocumentoDuplicado() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");

        when(clienteRepository.existsByDocumento("12345678901")).thenReturn(true);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.criarConta(pf);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("deve buscar cliente por ID com sucesso")
    void buscarPorIdSucesso() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));

        Cliente clienteEncontrado = clienteService.buscarPorId(1L);

        assertNotNull(clienteEncontrado);
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("deve retornar 404 ao buscar ID de cliente inexistente")
    void buscarPorIdInexistente() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.buscarPorId(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
    }

    @Test
    @DisplayName("deve visualizar saldo do cliente com sucesso")
    void visualizarSaldoSucesso() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");
        pf.setSaldo(300.0);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));

        Double saldo = clienteService.visualizarSaldo(1L);

        assertEquals(300.0, saldo);
    }

    @Test
    @DisplayName("deve realizar depósito com sucesso e somar ao saldo do cliente")
    void depositarSucesso() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");
        pf.setSaldo(100.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente clienteAtualizado = clienteService.depositar(1L, 200.0);

        assertEquals(300.0, clienteAtualizado.getSaldo());
        verify(clienteRepository, times(1)).save(pf);
    }

    @Test
    @DisplayName("não deve permitir depósito com valor zero ou negativo")
    void depositarValorInvalido() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.depositar(1L, 0.0);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("deve realizar saque com sucesso quando houver saldo suficiente")
    void sacarSucesso() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");
        pf.setSaldo(500.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        Cliente clienteAtualizado = clienteService.sacar(1L, 150.0);

        assertEquals(350.0, clienteAtualizado.getSaldo());
        verify(clienteRepository, times(1)).save(pf);
    }

    @Test
    @DisplayName("não deve permitir saque com saldo insuficiente")
    void sacarSaldoInsuficiente() {
        PessoaFisica pf = new PessoaFisica("Ana", "12345678901", "ana@email.com");
        pf.setSaldo(50.0);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(pf));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            clienteService.sacar(1L, 100.0);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    @DisplayName("deve listar todos os clientes com sucesso")
    void listarClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(new PessoaFisica(), new PessoaJuridica()));

        List<Cliente> clientes = clienteService.listar();

        assertEquals(2, clientes.size());
        verify(clienteRepository, times(1)).findAll();
    }
}