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
import com.projeto.banco.model.Conta;
import com.projeto.banco.model.PessoaFisica;
import com.projeto.banco.repository.ContaRepository;
import com.projeto.banco.service.ClienteService;
import com.projeto.banco.service.ContaService;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ContaService contaService;

    @Test
    @DisplayName("deve criar uma conta com sucesso quando o cliente não possuir conta cadastrada")
    void criarContaSucesso() {
        Cliente cliente = new PessoaFisica("Carlos", "12345678901", "carlos@email.com");
        Conta conta = new Conta();

        when(contaRepository.findByidClientId(1L)).thenReturn(Optional.empty());
        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta contaCriada = contaService.criarConta(1L, conta);

        assertNotNull(contaCriada);
        assertEquals(0.0, contaCriada.getSaldo());
        assertEquals(cliente, contaCriada.getIdCliente());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    @DisplayName("não deve permitir criar mais de uma conta para o mesmo cliente")
    void criarContaDuplicada() {
        Conta contaExistente = new Conta();
        when(contaRepository.findByidClientId(1L)).thenReturn(Optional.of(contaExistente));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            contaService.criarConta(1L, new Conta());
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    @DisplayName("deve listar todas as contas com sucesso")
    void listarContas() {
        when(contaRepository.findAll()).thenReturn(List.of(new Conta(), new Conta()));

        List<Conta> contas = contaService.listar();

        assertEquals(2, contas.size());
        verify(contaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("deve buscar conta por ID com sucesso")
    void buscarPorIdSucesso() {
        Conta conta = new Conta();
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Conta contaEncontrada = contaService.buscarPorId(1L);

        assertNotNull(contaEncontrada);
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("deve retornar 404 ao buscar ID de conta inexistente")
    void buscarPorIdInexistente() {
        when(contaRepository.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            contaService.buscarPorId(99L);
        });

        assertEquals(HttpStatus.NOT_FOUND, erro.getStatusCode());
    }

    @Test
    @DisplayName("deve visualizar o saldo da conta com sucesso")
    void visualizarSaldoSucesso() {
        Conta conta = new Conta();
        conta.setSaldo(450.0);
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        Double saldo = contaService.visualizarSaldo(1L);

        assertEquals(450.0, saldo);
    }

    @Test
    @DisplayName("deve realizar depósito com sucesso e atualizar o saldo")
    void fazerDepositoSucesso() {
        Conta conta = new Conta();
        conta.setSaldo(100.0);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

        Conta contaAtualizada = contaService.fazerDeposito(1L, 200.0);

        assertEquals(300.0, contaAtualizada.getSaldo());
        assertEquals(200.0, contaAtualizada.getValorDoDeposito());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    @DisplayName("não deve permitir depósito com valor zero ou negativo")
    void fazerDepositoValorInvalido() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            contaService.fazerDeposito(1L, 0.0);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    @DisplayName("deve realizar saque com sucesso quando houver saldo suficiente")
    void fazerSaqueSucesso() {
        Conta conta = new Conta();
        conta.setSaldo(500.0);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(inv -> inv.getArgument(0));

        Conta contaAtualizada = contaService.fazerSaque(1L, 150.0);

        assertEquals(350.0, contaAtualizada.getSaldo());
        assertEquals(150.0, contaAtualizada.getValorDoSaque());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    @DisplayName("não deve permitir saque com saldo insuficiente")
    void fazerSaqueSaldoInsuficiente() {
        Conta conta = new Conta();
        conta.setSaldo(50.0);

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            contaService.fazerSaque(1L, 100.0);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(contaRepository, never()).save(any(Conta.class));
    }

    @Test
    @DisplayName("não deve permitir saque com valor zero ou negativo")
    void fazerSaqueValorInvalido() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            contaService.fazerSaque(1L, -10.0);
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(contaRepository, never()).save(any(Conta.class));
    }
}