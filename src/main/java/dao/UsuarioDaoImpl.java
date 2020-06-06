package dao;


import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import entidade.Usuario;
import util.JpaUtil;

/**
 * 
 * @author Tiago Batista
 *
 *         Essa classe implementa a interface, todos os metodos mostrados na
 *         interface. Lembrando de uma coisa, a implementa��o ela recebe no
 *         construtor o entityManager, a conex�o com o banco de dados, deixando
 *         assim essa classe totalemnte independente
 *
 */
public class UsuarioDaoImpl implements UsuarioDao {

	EntityManager ent=JpaUtil.getEntityManager();
	private EntityTransaction tx;

	// Construtor vai receber a conex�o para executar

	@Override
	public boolean inserir(Usuario usuario) {
		// TODO Auto-generated method stub
		//this.ent = JpaUtil.getEntityManager();
	    tx = ent.getTransaction();
		tx.begin();

		ent.persist(usuario);
		tx.commit();
		
		//ent.close();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("Salvo com sucesso");
		facesContext.addMessage(null, facesMessage);

		return true;
	}

	@Override
	public void alterar(Usuario usuario) {
		// TODO Auto-generated method stub
		//this.ent = JpaUtil.getEntityManager();
		System.out.println(usuario);
		System.out.println(usuario.getEmail());
	    tx = ent.getTransaction();
		tx.begin();

		ent.merge(usuario);
		tx.commit();
	//	ent.close();

	}

	@Override
	public void remover(Usuario usuario) {
		// TODO Auto-generated method stub
		System.out.println(usuario);
		//this.ent = JpaUtil.getEntityManager();
	    tx = ent.getTransaction();
		tx.begin();
        System.out.println("remover");
		ent.remove(usuario);
		tx.commit();
		//ent.close();
        System.out.println("removido");
	}

	/**
	 * Pesquisar, pesquisar pela chave primaria que � o email
	 */

	@Override
	public Usuario pesquisar(String email) {
		//this.ent = JpaUtil.getEntityManager();
		Usuario usuario = ent.find(Usuario.class, email);
		//ent.close();
		return usuario;
	}

	/**
	 * O metodo listar todos, faz um select * from, por�m com o JPA, vc faz a
	 * consulta pelo objeto direto assim from Usuario, que isso � o objeto usuario e
	 * n�o a tabela
	 */

	@SuppressWarnings("unchecked")
	public List<Usuario> listarTodos() {
		//this.ent = JpaUtil.getEntityManager();
		Query query = ent.createQuery("from Usuario u order by nome asc");

		List<Usuario> usuarios = query.getResultList();
		
		return usuarios;
	}

	/**
	 * O metodo listar nome, faz um select por nome usando Where e LIKE para procura
	 * 
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> listarNome(String nome) {
		// TODO Auto-generated method stub
		//this.ent = JpaUtil.getEntityManager();
		Query query = ent.createQuery("From  Usuario Where nome LIKE '%" + nome + "%' ORDER BY nome asc");

		List<Usuario> usuarios = query.getResultList();
		
		return usuarios;

	}
	
@SuppressWarnings("unchecked")
public List<Usuario> pesquisarUsuario(Usuario usuario){
		
		System.out.println(usuario);

		Query query = ent.createQuery("from Usuario u where 1=1 " + this.montarQuery(usuario));
		
		List<Usuario> usuarios = query.getResultList();
	
		return usuarios;
	}
	

	private String montarQuery(Usuario usuario) {
		
		String query = "";
		
		
		if (usuario.getNome() != null && !usuario.getNome().isEmpty()) {
			query += "and Upper(u.nome) like Upper('%" + usuario.getNome() + "%')";
		}
		
		if (usuario.getEmail() != null && !usuario.getEmail().isEmpty()) {
			query += "and u.email='"+usuario.getEmail()+"'" ; 
					 
		}
		
		if (usuario.getFuncao() != null && !usuario.getFuncao().isEmpty()) {
			query += "and Upper(u.funcao) like Upper('%"+ usuario.getFuncao() +"%')";
		}
		
		
		
		return query;
	}

}
