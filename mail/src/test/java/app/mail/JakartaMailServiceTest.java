package app.mail;

import org.junit.jupiter.api.Test;

public class JakartaMailServiceTest {
    @Test
    void canSendEmail() {
        String username = System.getenv("SMTP_CREDENCIAL_USERNAME");
        String password = System.getenv("SMTP_CREDENCIAL_PASSWORD");
        String host = System.getenv("SMTP_HOST");
        int port = Integer.parseInt(System.getenv("SMTP_PORT"));

        var mailService = new JakartaMailService(host, port, username, password);

        mailService.send("mat.esperandio@gmail.com", "Título teste 1", "Conteúdo teste1");
        mailService.send("mat.esperandio@gmail.com", "Título teste 2", "Conteúdo teste2");
    }
}
