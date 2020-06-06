package controle;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpSession;

import dao.UsuarioDao;
import dao.UsuarioDaoImpl;
import entidade.Usuario;
import util.CalculoIdade;

/**
 * 
 * @author Tiago Batista
 *
 *         LoginBean, classe responsavel pela logica de logar no sistema
 *
 */

@ManagedBean(name = "LoginBean")
@SessionScoped
public class LoginBean {

	private String usuarioTXT;
	private String senhaTXT;
	UsuarioDao usuarioDao;

	Usuario usuario;
	private String mensagem;
	EntityManagerFactory factory;
	private Usuario usuarioLogado;
	private LocalDate dataNascimento;
	private String data;
	private String confirmaSenha;

	private static final String PESQUISAR = "pesquisarUsuario.xhtml";

	private static final String Manter = "manterUsuario.xhtml";

	private static final String Login = "index.xhtml";

	private static final String Senha = "esqueciSenha.xhtml";

	public LoginBean() {
		this.usuario = new Usuario();
		this.usuarioDao = new UsuarioDaoImpl();

	}

	public void entrar() throws IOException {

		if (this.usuarioTXT.equals("admin") && this.senhaTXT.equals("admin")) {
			this.usuarioLogado = new Usuario();
			this.usuarioLogado.setNome("admin");
			HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(true);
			sessao.setAttribute("usuarioLogado", usuarioLogado);
			FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR+"?faces-redirect=true&amp;includeViewParams=true");
		} else {
			if (this.usuarioDao.pesquisar(this.usuarioTXT) != null) {
				if (this.usuarioDao.pesquisar(this.usuarioTXT).getSenha().equals(this.senhaTXT)) {
					this.usuarioLogado = usuarioDao.pesquisar(this.usuarioTXT);
					this.usuario = this.usuarioLogado;
					HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(true);
					sessao.setAttribute("usuarioLogado", usuarioLogado);
					
					//"/paginas/feira/manterFeira.xhtml?faces-redirect=true&amp;includeViewParams=true";

					FacesContext.getCurrentInstance().getExternalContext().redirect(PESQUISAR+"?faces-redirect=true&amp;includeViewParams=true");
				} else {
					this.mensagem = " Senha inválida";
				}
			} else {

				this.mensagem = " Usuario não cadastrado";
			}

		}
	}
	
	public boolean habilitaBotao() {
		return this.usuario.getEmail().equals(this.usuarioLogado.getEmail());
	}

	public void abrirManterUsuario() throws IOException {
        this.usuarioLogado=null;
        HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
				.getSession(true);
		sessao.setAttribute("usuarioLogado", usuarioLogado);
		FacesContext.getCurrentInstance().getExternalContext().redirect(Manter+"?faces-redirect=true&amp;includeViewParams=true");
	}

	public void abrirEsqueciSenha() throws IOException {

		if (this.usuarioDao.pesquisar(this.usuarioTXT) != null) {
			this.usuario = new Usuario();
			this.usuario.setEmail(this.usuarioTXT);

		} else {
			this.usuario.setEmail("");

		}

		FacesContext.getCurrentInstance().getExternalContext().redirect(Senha);
	}

	public void novaSenha() throws IOException {

		if (this.usuarioDao.pesquisar(this.usuario.getEmail()) != null) {

			Usuario usuarioAtual = this.usuarioDao.pesquisar(this.usuario.getEmail());
			System.out.println(usuarioAtual.getNome());
			System.out.println(usuarioAtual.getDataNascimento());
			System.out.println(this.usuario.getNome());
			System.out.println(this.usuario.getDataNascimento());
			if (this.usuario.getNome().equals(usuarioAtual.getNome())
					&& this.usuario.getDataNascimento().equals(usuarioAtual.getDataNascimento())) {
				usuarioAtual.setSenha(this.usuario.getSenha());
				usuarioDao.alterar(usuarioAtual);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
						usuarioAtual.getNome() + " senha alterada com sucesso."));
				this.mensagem = " Senha alterada com sucesso.";
				usuarioAtual = new Usuario();
				System.out.println("alterado");
				abrirLogin();
			} else {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " dados inválidos."));
				usuarioAtual = new Usuario();
				this.usuario = new Usuario();

			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "", " Usuário inválido."));

			this.mensagem = " Usuário inválido.";

		}

	}

	public void convertDate() throws ParseException {

		System.out.println(data);
		dataNascimento = CalculoIdade.format(this.data);
		this.usuario.setIdade(CalculoIdade.idade(dataNascimento));
		System.out.println(this.usuario.getIdade());
		// this.usuario.setDataNascimento(dataNascimento);
		this.usuario.setDataNascimento(new SimpleDateFormat("dd/MM/yyyy").parse(data));

	}

	public void abrirLogin() throws IOException {
		this.usuarioLogado=null;
		FacesContext.getCurrentInstance().getExternalContext().redirect(Login);

	}

	public String getUsuarioTXT() {
		return usuarioTXT;
	}

	public void setUsuarioTXT(String usuarioTXT) {
		this.usuarioTXT = usuarioTXT;
	}

	public String getSenhaTXT() {
		return senhaTXT;
	}

	public void setSenhaTXT(String senhaTXT) {
		this.senhaTXT = senhaTXT;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

}
