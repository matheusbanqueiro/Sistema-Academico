package br.edu.javacode.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.edu.javacode.dao.AlunoDAO;
import br.edu.javacode.model.Aluno;


@WebServlet("/ServletAluno")
public class ServletAluno extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Date strToDate(String data) throws Exception {
		if (data == null) {
			return null;
		}
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		Date dataF = formato.parse(data);
		return dataF;
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Aluno aluno = new Aluno();
		AlunoDAO dao;
		String cmd = request.getParameter("cmd");
		try {
			if (!cmd.equals(null)) {
				aluno.setRa(Integer.parseInt(request.getParameter("txtRa")));
				aluno.setNome(request.getParameter("txtNome"));
				aluno.setEmail(request.getParameter("txtEmail"));
				aluno.setEndereco(request.getParameter("txtEndereco"));
				aluno.setPeriodo(request.getParameter("cmbPeriodo"));
				aluno.setDataNascimento(strToDate(request.getParameter("txtData")));
			}
		} catch (Exception e) {
		}
		try {
			dao = new AlunoDAO();
			RequestDispatcher rd = null;

			if (cmd.equalsIgnoreCase("incluir")) {
				dao.salvar(aluno);
				rd = request.getRequestDispatcher("ServletAluno?cmd=listar");
			} else if (cmd.equalsIgnoreCase("listar")) {
				List alunosList = dao.todosAlunos();
				request.setAttribute("alunosList", alunosList);
				rd = request.getRequestDispatcher("jsp/mostrarAlunos.jsp");
			} else if (cmd.equalsIgnoreCase("con")) {
				aluno = dao.procurarAluno(aluno.getRa());
				HttpSession session = request.getSession(true);
				session.setAttribute("aluno", aluno);
				rd = request.getRequestDispatcher("jsp/consultarAluno.jsp");
			} else if (cmd.equalsIgnoreCase("atu")) {
				aluno = dao.procurarAluno(aluno.getRa());
				HttpSession session = request.getSession(true);
				session.setAttribute("aluno", aluno);
				rd = request.getRequestDispatcher("jsp/alterarAluno.jsp");
			} else if (cmd.equalsIgnoreCase("atualizar")) {
				dao.atualizar(aluno);
				rd = request.getRequestDispatcher("ServletAluno?cmd=listar");
			}else if (cmd.equalsIgnoreCase("excluir")) {
			    int ra = Integer.parseInt(request.getParameter("txtRa"));
			    dao.excluirAluno(ra);
			    rd = request.getRequestDispatcher("ServletAluno?cmd=listar");
			}

			rd.forward(request, response);
		} catch (Exception e) {
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

}
