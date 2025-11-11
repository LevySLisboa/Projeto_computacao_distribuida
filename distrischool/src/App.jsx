import React, { useState } from 'react';
import { Users, BookOpen, Calendar, LogIn, UserPlus, LogOut, Clock, GraduationCap, Menu, X } from 'lucide-react';
import { criarUsuario } from './services/usuarioService';

const DistriSchool = () => {
  const [currentUser, setCurrentUser] = useState(null);
  const [view, setView] = useState('login');
  const [menuOpen, setMenuOpen] = useState(false);
  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    senha: '',
    tipo: 'aluno'
  });

const handleCadastro = async () => {
  try {
    const novoUsuario = await  criarUsuario(formData);
    alert(`Usuário criado com sucesso: ${novoUsuario.nome}`);
  } catch (err) {
    alert('Erro ao cadastrar usuário');
    console.error(err);


  }
};

  // Dados mockados para demonstração
  const mockTurmas = [
    { id: 1, nome: 'Matemática Avançada', disciplina: 'Matemática', horario: 'Segunda e Quarta, 08:00 - 10:00', professor: 'Prof. João Silva' },
    { id: 2, nome: 'Física Experimental', disciplina: 'Física', horario: 'Terça e Quinta, 14:00 - 16:00', professor: 'Prof. Maria Santos' },
    { id: 3, nome: 'Programação I', disciplina: 'Computação', horario: 'Quarta e Sexta, 10:00 - 12:00', professor: 'Prof. Carlos Oliveira' },
  ];

  const mockMinhasTurmas = [
    { id: 1, nome: 'Matemática Avançada', disciplina: 'Matemática', horario: 'Segunda e Quarta, 08:00 - 10:00', professor: 'Prof. João Silva', alunos: 25 },
    { id: 3, nome: 'Programação I', disciplina: 'Computação', horario: 'Quarta e Sexta, 10:00 - 12:00', professor: 'Prof. Carlos Oliveira', alunos: 30 },
  ];

  const mockAlunos = [
    { id: 1, nome: 'Ana Carolina Silva' },
    { id: 2, nome: 'Bruno Santos' },
    { id: 3, nome: 'Carla Oliveira' },
    { id: 4, nome: 'Daniel Costa' },
  ];

  const handleLogin = () => {
    // Simula login - integração será feita posteriormente
    setCurrentUser({ nome: 'Usuário Teste', tipo: 'aluno' });
    setView('turmas-disponiveis');
  };

  const handleLoginProfessor = () => {
    setCurrentUser({ nome: 'Professor Teste', tipo: 'professor' });
    setView('minhas-turmas');
  };

  const handleLogout = () => {
    setCurrentUser(null);
    setView('login');
  };

  if (!currentUser) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4">
        <div className="max-w-md mx-auto pt-12">
          <div className="bg-white rounded-2xl shadow-xl p-8">
            <div className="text-center mb-8">
              <div className="inline-flex items-center justify-center w-16 h-16 bg-indigo-600 rounded-full mb-4">
                <GraduationCap className="text-white" size={32} />
              </div>
              <h1 className="text-3xl font-bold text-gray-800">DistriSchool</h1>
              <p className="text-gray-600 mt-2">Plataforma Educacional</p>
            </div>

            <div className="flex gap-2 mb-6">
              <button
                onClick={() => setView('login')}
                className={`flex-1 py-2 px-4 rounded-lg font-medium transition ${
                  view === 'login' 
                    ? 'bg-indigo-600 text-white' 
                    : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                }`}
              >
                <LogIn className="inline mr-2" size={18} />
                Login
              </button>
              <button
                onClick={() => setView('register')}
                className={`flex-1 py-2 px-4 rounded-lg font-medium transition ${
                  view === 'register' 
                    ? 'bg-indigo-600 text-white' 
                    : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                }`}
              >
                <UserPlus className="inline mr-2" size={18} />
                Cadastro
              </button>
            </div>

            {view === 'login' ? (
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    placeholder="seu@email.com"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Senha</label>
                  <input
                    type="password"
                    placeholder="••••••••"
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  />
                </div>
                <button
                  onClick={handleLogin}
                  className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition"
                >
                  Entrar como Aluno (Demo)
                </button>
                <button
                  onClick={handleLoginProfessor}
                  className="w-full bg-emerald-600 text-white py-3 rounded-lg font-semibold hover:bg-emerald-700 transition"
                >
                  Entrar como Professor (Demo)
                </button>
              </div>
            ) : (
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Nome Completo</label>
                  <input
                    type="text"
                    placeholder="Seu nome completo"
                    value={formData.nome}
                    onChange={(e) => setFormData({ ...formData, nome: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <input
                    type="email"
                    placeholder="seu@email.com"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Senha</label>
                  <input
                    type="password"
                    placeholder="••••••••"
                    value={formData.senha}
                    onChange={(e) => setFormData({ ...formData, senha: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Tipo de Usuário</label>
                  <select
                    value={formData.tipo}
                    onChange={(e) => setFormData({ ...formData, tipo: e.target.value })}
                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                  >
                    <option value="aluno">Aluno</option>
                    <option value="professor">Professor</option>
                    <option value="tecnico">Técnico Administrativo</option>
                  </select>
                </div>
                <button
                  onClick={handleCadastro}
                  className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition"
                >
                  Cadastrar
                </button>
              </div>

            )}
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 py-4">
          <div className="flex justify-between items-center">
            <div className="flex items-center gap-3">
              <div className="w-10 h-10 bg-indigo-600 rounded-lg flex items-center justify-center">
                <GraduationCap className="text-white" size={24} />
              </div>
              <div className="hidden sm:block">
                <h1 className="text-xl font-bold text-gray-800">DistriSchool</h1>
                <p className="text-sm text-gray-600">{currentUser.nome} - {currentUser.tipo}</p>
              </div>
            </div>
            <button
              onClick={handleLogout}
              className="flex items-center gap-2 px-4 py-2 text-gray-600 hover:text-gray-800 hover:bg-gray-100 rounded-lg transition"
            >
              <LogOut size={18} />
              <span className="hidden sm:inline">Sair</span>
            </button>
          </div>
        </div>
      </nav>

      <div className="max-w-7xl mx-auto px-4 py-8">
        <div className="flex gap-2 mb-6 overflow-x-auto pb-2">
          <button
            onClick={() => setView('minhas-turmas')}
            className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
              view === 'minhas-turmas'
                ? 'bg-indigo-600 text-white'
                : 'bg-white text-gray-600 hover:bg-gray-100 shadow-sm'
            }`}
          >
            <BookOpen className="inline mr-2" size={18} />
            Minhas Turmas
          </button>
          {currentUser.tipo === 'aluno' && (
            <button
              onClick={() => setView('turmas-disponiveis')}
              className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
                view === 'turmas-disponiveis'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-white text-gray-600 hover:bg-gray-100 shadow-sm'
              }`}
            >
              <Calendar className="inline mr-2" size={18} />
              Turmas Disponíveis
            </button>
          )}
          {currentUser.tipo === 'professor' && (
            <button
              onClick={() => setView('criar-turma')}
              className={`px-4 py-2 rounded-lg font-medium whitespace-nowrap transition ${
                view === 'criar-turma'
                  ? 'bg-indigo-600 text-white'
                  : 'bg-white text-gray-600 hover:bg-gray-100 shadow-sm'
              }`}
            >
              <UserPlus className="inline mr-2" size={18} />
              Criar Turma
            </button>
          )}
        </div>

        {view === 'minhas-turmas' && (
          <div>
            <h2 className="text-2xl font-bold text-gray-800 mb-4">
              {currentUser.tipo === 'professor' ? 'Turmas que Leciono' : 'Minhas Turmas'}
            </h2>
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {mockMinhasTurmas.map(turma => (
                <div key={turma.id} className="bg-white rounded-xl shadow-sm p-6 border border-gray-200 hover:shadow-md transition">
                  <h3 className="text-lg font-bold text-gray-800 mb-2">{turma.nome}</h3>
                  <p className="text-gray-600 mb-1">
                    <span className="font-medium">Disciplina:</span> {turma.disciplina}
                  </p>
                  <p className="text-gray-600 mb-1">
                    <span className="font-medium">Professor:</span> {turma.professor}
                  </p>
                  <p className="text-gray-600 mb-3 flex items-center">
                    <Clock className="inline mr-1" size={16} />
                    {turma.horario}
                  </p>
                  {currentUser.tipo === 'professor' && (
                    <div className="mt-4 pt-4 border-t">
                      <p className="text-sm font-medium text-gray-700 mb-3">
                        <Users className="inline mr-1" size={16} />
                        Alunos Inscritos: {turma.alunos}
                      </p>
                      <button className="w-full py-2 px-4 bg-indigo-50 text-indigo-600 rounded-lg hover:bg-indigo-100 transition font-medium">
                        Ver Lista de Alunos
                      </button>
                    </div>
                  )}
                  {currentUser.tipo === 'aluno' && (
                    <button
                      onClick={() => alert('Integração com backend pendente')}
                      className="w-full mt-3 py-2 px-4 bg-red-50 text-red-600 rounded-lg hover:bg-red-100 transition font-medium"
                    >
                      Cancelar Inscrição
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        )}

        {view === 'turmas-disponiveis' && currentUser.tipo === 'aluno' && (
          <div>
            <h2 className="text-2xl font-bold text-gray-800 mb-4">Turmas Disponíveis</h2>
            <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
              {mockTurmas.map(turma => (
                <div key={turma.id} className="bg-white rounded-xl shadow-sm p-6 border border-gray-200 hover:shadow-md transition">
                  <h3 className="text-lg font-bold text-gray-800 mb-2">{turma.nome}</h3>
                  <p className="text-gray-600 mb-1">
                    <span className="font-medium">Disciplina:</span> {turma.disciplina}
                  </p>
                  <p className="text-gray-600 mb-1">
                    <span className="font-medium">Professor:</span> {turma.professor}
                  </p>
                  <p className="text-gray-600 mb-4 flex items-center">
                    <Clock className="inline mr-1" size={16} />
                    {turma.horario}
                  </p>
                  <button
                    onClick={() => alert('Integração com backend pendente')}
                    className="w-full py-2 px-4 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700 transition font-medium"
                  >
                    Inscrever-se
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}

        {view === 'criar-turma' && currentUser.tipo === 'professor' && (
          <div className="max-w-2xl mx-auto">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Criar Nova Turma</h2>
            <div className="bg-white rounded-xl shadow-sm p-6 space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Nome da Turma</label>
                <input
                  type="text"
                  placeholder="Ex: Turma A - Manhã"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Disciplina</label>
                <input
                  type="text"
                  placeholder="Ex: Matemática"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Horário</label>
                <input
                  type="text"
                  placeholder="Ex: Segunda e Quarta, 08:00 - 10:00"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Descrição (opcional)</label>
                <textarea
                  rows="4"
                  placeholder="Descreva o conteúdo e objetivos da turma..."
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-indigo-500 focus:border-transparent resize-none"
                />
              </div>
              <button
                onClick={() => alert('Integração com backend pendente')}
                className="w-full bg-indigo-600 text-white py-3 rounded-lg font-semibold hover:bg-indigo-700 transition"
              >
                Criar Turma
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default DistriSchool;