package com.projeto.banco;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.projeto.banco.model.Transferencia;
import com.projeto.banco.repository.ClienteRepository;
import com.projeto.banco.repository.TransferenciaRepository;
import com.projeto.banco.service.ClienteService;
import com.projeto.banco.service.TransferenciaService;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private TransferenciaService transferenciaService;

    @Test
    @DisplayName("deve registrar transferência com sucesso quando houver saldo suficiente")
    void fazerTransferenciaSucesso() {

        Cliente cliente = new PessoaFisica("Roberto", "55544433322", "roberto@email.com");
        cliente.setSaldo(500.0);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(transferenciaRepository.save(any(Transferencia.class))).thenAnswer(inv -> inv.getArgument(0));

        Transferencia transferencia = transferenciaService.fazerTransferencia(1L, 150.0, "123-4", "567-8");

        assertNotNull(transferencia);
        assertEquals(150.0, transferencia.getValorDaTrasferencia());
        assertEquals("123-4", transferencia.getContaOrigem());
        assertEquals("567-8", transferencia.getContaDestino());
        
        // Verifica se o saldo do cliente foi descontado corretamente
        assertEquals(350.0, cliente.getSaldo());
        verify(clienteRepository, times(1)).save(cliente);
        verify(transferenciaRepository, times(1)).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("não deve permitir transferir para a mesma conta de origem")
    void transferenciaMesmaConta() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            transferenciaService.fazerTransferencia(1L, 100.0, "123-4", "123-4");
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("400 BAD_REQUEST \"A conta de origem não pode ser igual à conta de destino.\"", erro.getMessage());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("não deve permitir transferência com saldo insuficiente")
    void transferenciaSemSaldo() {
    
        Cliente cliente = new PessoaFisica("Roberto", "55544433322", "roberto@email.com");
        cliente.setSaldo(50.0);

        when(clienteService.buscarPorId(1L)).thenReturn(cliente);

        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            transferenciaService.fazerTransferencia(1L, 150.0, "123-4", "567-8");
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        assertEquals("400 BAD_REQUEST \"Saldo insuficiente para realizar a transferência.\"", erro.getMessage());
        
        verify(clienteRepository, never()).save(any(Cliente.class));
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }

    @Test
    @DisplayName("não deve permitir transferência com valor zero ou negativo")
    void transferenciaValorInvalido() {
        ResponseStatusException erro = assertThrows(ResponseStatusException.class, () -> {
            transferenciaService.fazerTransferencia(1L, 0.0, "123-4", "567-8");
        });

        assertEquals(HttpStatus.BAD_REQUEST, erro.getStatusCode());
        verify(transferenciaRepository, never()).save(any(Transferencia.class));
    }
}