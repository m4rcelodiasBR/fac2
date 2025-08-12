package mb.cpo.facdigital.service;

public interface EmailService {
    void enviarEmail(String para, String assunto, String texto);
}
