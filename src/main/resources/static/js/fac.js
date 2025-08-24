/**
 * Script para a interatividade da página de avaliação da FAC.
 * @author Marcelo Dias
 */
$(function () {
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    if (header && token) {
        $(document).ajaxSend(function(e, xhr, options) {
            xhr.setRequestHeader(header, token);
        });
    }
});

$(document).ready(function () {

    function selecionarAvaliado(linha) {
        $('#tabela-avaliados tbody tr').removeClass('table-active');
        $(linha).addClass('table-active');

        const fotoBase64 = $(linha).data('foto');
        const nomeGuerra = $(linha).data('nome');
        const nip = $(linha).data('nip');

        $('#foto-avaliado').attr('src', 'data:image/jpeg;base64,' + fotoBase64);
        $('#nome-guerra-avaliado').text(nomeGuerra);
        $('#nip-avaliado').text('NIP: ' + nip);
    }

    $('#tabela-avaliados tbody tr').on('click', function () {
        selecionarAvaliado(this);
    });

    const primeiraLinha = $('#tabela-avaliados tbody tr:first');
    if (primeiraLinha.length) {
        primeiraLinha.trigger('click');
    }

    $('#tabela-avaliados').on('change', 'input.grau-input', function() {
        const input = $(this);
        const avaliacaoId = input.data('avaliacao-id');
        const avaliadoId = input.data('avaliado-id');
        const grau = input.val();
        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        input.css('border-color', '#ffc107');

        $.ajax({
            url: `/fac/avaliacao/${avaliacaoId}/avaliado/${avaliadoId}`,
            type: 'POST',
            data: { grau: grau },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                input.css('border-color', '#198754');
                setTimeout(() => input.css('border-color', ''), 2000);
            },
            error: function(err) {
                input.css('border-color', '#dc3545');
                alert('Erro ao salvar o grau. Verifique sua conexão e o console do navegador (F12).');
            }
        });
    });

    // --- NOVA FUNCIONALIDADE: NAVEGAÇÃO COM ENTER ---
    $('#tabela-avaliados').on('keydown', 'input.grau-input', function(event) {
        // Verifica se a tecla pressionada foi o "Enter"
        if (event.key === 'Enter') {
            // Previne o comportamento padrão do Enter (que seria submeter o formulário)
            event.preventDefault();

            // Pega todos os inputs de grau da tabela em uma lista
            const inputs = $('input.grau-input');
            // Encontra o índice (posição) do input atual na lista
            const currentIndex = inputs.index(this);

            // Calcula o índice do próximo input
            const nextIndex = currentIndex + 1;

            // Se houver um próximo input na lista, move o foco para ele
            if (nextIndex < inputs.length) {
                inputs.eq(nextIndex).focus();
            } else {
                // Opcional: se for o último, pode mover o foco para o botão de enviar
                $('.btn-success').focus();
            }
        }
    });
});