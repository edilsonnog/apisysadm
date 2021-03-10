package sysadm.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import sysadm.api.rest.ApplicationContextLoad;
import sysadm.api.rest.model.Usuario;
import sysadm.api.rest.repository.UsuarioRepository;

@Service
@Component
public class JWTTokenAutenticacaoService {

	/* Tempo de validade do TOKEN 2 dias */
	private static final long EXPIRATION_TIME = 172800000;

	/* Uma senha unica para compor a autenticação e ajuda na segurança */
	private static final String SECRET = "SenhaExtremamenteSecreta";

	/* Prefixo padrão de Token */
	private static final String TOKEN_PREFIX = "Bearer";

	private static final String HEADER_STRING = "Authorization";

	/*Gerando token de autenticação e adicionando ao cabeçalho a resposta Http*/
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		/* Montagem do Token */
		String JWT = Jwts.builder()/* Chama o gerador de Token */
				.setSubject(username)/* Adiciona o usuário */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))/* Tempo de expiração */
				.signWith(SignatureAlgorithm.HS512, SECRET).compact();/* compactação e algoritimo de geração de senha */

		/* Junta o token mais o prefixo */
		String token = TOKEN_PREFIX + " " + JWT;

		/* Adiciona no cabeçalho http */
		response.addHeader(HEADER_STRING, token);

		ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class).atualizaTokenUser(JWT,
				username);

		/*
		 * Liberando resposta para portas diferentes que usam a API ou caso clientes web
		 */
		liberacaoCors(response);

		/* Escreve token como resposta no corpo Http */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	/* Retorna o usuario valido com token ou caso não seja valido retorno null */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		/* Pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);

		try {

			if (token != null) {

				String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

				/* Faz a validação do token do usuário na requisição */
				String user = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(tokenLimpo).getBody().getSubject();

				if (user != null) {

					Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
							.findUserByLogin(user);

					if (usuario != null) {

						if (tokenLimpo.equalsIgnoreCase(usuario.getToken())) {

							return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
									usuario.getAuthorities());
						}
					}

				}

			}
		} catch (io.jsonwebtoken.ExpiredJwtException e) {
			try {
				response.getOutputStream()
						.println("Seu TOKEN está expirado, faça o login ou informe um novo TOKEN para a Autenticação");
			} catch (IOException e1) {
			}
		}

		liberacaoCors(response);

		return null;
	}

	private void liberacaoCors(HttpServletResponse response) {

		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}

		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}

		if (response.getHeader("Access-Control-Allow-Methods") == null) {
			response.addHeader("Access-Control-Allow-Methods", "*");
		}

	}

}
