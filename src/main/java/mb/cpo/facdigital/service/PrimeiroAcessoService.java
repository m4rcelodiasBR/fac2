package mb.cpo.facdigital.service;

import mb.cpo.facdigital.dto.DadosPrimeiroAcessoDTO;

public interface PrimeiroAcessoService {

    void iniciarProcesso(String nip, String email);

    void finalizarProcesso(DadosPrimeiroAcessoDTO dados);

}