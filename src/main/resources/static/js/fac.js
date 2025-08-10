$(document).ready(function () {

    // Função para atualizar o painel da foto
    function selecionarAvaliado(linha) {
        // Remove a classe ativa de todas as linhas
        $('#tabela-avaliados tbody tr').removeClass('table-active');
        // Adiciona a classe na linha clicada
        $(linha).addClass('table-active');

        // Pega os dados da linha usando o método .data() do jQuery
        const fotoBase64 = $(linha).data('foto');
        const nomeGuerra = $(linha).data('nome');
        const nip = $(linha).data('nip');

        // Atualiza os elementos no painel da foto
        $('#foto-avaliado').attr('src', 'data:image/jpeg;base64,' + fotoBase64);
        $('#nome-guerra-avaliado').text(nomeGuerra);
        console.log(nomeGuerra);
        $('#nip-avaliado').text('NIP: ' + nip);
    }

    // Adiciona o evento de clique em todas as linhas <tr> da tabela
    $('#tabela-avaliados tbody tr').on('click', function () {
        selecionarAvaliado(this);
    });

    // Simula um clique na primeira linha para exibir os dados iniciais
    const primeiraLinha = $('#tabela-avaliados tbody tr:first');
    if (primeiraLinha.length) {
        primeiraLinha.trigger('click');
    }
});