const API_URL = "http://localhost:8080/aluno";

export async function criarAluno(aluno) {
  const res = await fetch(`${API_URL}/criarAluno`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(aluno),
  });
  return await res.json();
}

export async function listarAlunos() {
  const res = await fetch(`${API_URL}/todos`);
  return await res.json();
}

export async function buscarPorMatricula(matricula) {
  const res = await fetch(`${API_URL}/${matricula}`);
  return await res.json();
}

export async function atualizarAluno(aluno) {
  const res = await fetch(`${API_URL}/atualizarAluno`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(aluno),
  });
  return await res.json();
}

export async function deletarAluno(matricula) {
  await fetch(`${API_URL}/${matricula}`, {
    method: "DELETE",
  });
}
