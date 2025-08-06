import React, { useState } from 'react';

// Ícones (podem ser movidos para um arquivo separado de ícones no futuro)
const LockIcon = () => ( <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-400"><rect x="3" y="11" width="18" height="11" rx="2" ry="2"></rect><path d="M7 11V7a5 5 0 0 1 10 0v4"></path></svg> );
const UserIcon = () => ( <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="text-gray-400"><path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path><circle cx="12" cy="7" r="4"></circle></svg> );

export default function LoginPage({ onLoginSuccess }) {
  const [nip, setNip] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [carregando, setCarregando] = useState(false);

  const handleLogin = async (e) => {
    e.preventDefault();
    setCarregando(true);
    setErro('');
    try {
      const response = await fetch('/fac/autenticacao/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nip, senha }),
      });
      if (!response.ok) throw new Error('NIP ou senha inválidos.');
      const data = await response.json();
      onLoginSuccess(data.token);
    } catch (error) {
      setErro(error.message);
    } finally {
      setCarregando(false);
    }
  };

  return (
    <div className="bg-gray-100 min-h-screen flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        <div className="bg-white rounded-2xl shadow-xl p-8">
          <div className="text-center mb-8">
             <img src="https://www.marinha.mil.br/cpo/sites/www.marinha.mil.br.cpo/files/cpo_h_branco_0.png" alt="Logo CPO Marinha" className="mx-auto h-20 mb-4" />
            <h1 className="text-2xl font-bold text-gray-800">Portal FACDigital</h1>
            <p className="text-gray-500">Acesso ao sistema de avaliação</p>
          </div>
          <form onSubmit={handleLogin}>
            <div className="mb-4 relative">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><UserIcon /></div>
              <input type="text" value={nip} onChange={(e) => setNip(e.target.value)} className="w-full pl-12 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite seu NIP" required />
            </div>
            <div className="mb-6 relative">
               <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none"><LockIcon /></div>
              <input type="password" value={senha} onChange={(e) => setSenha(e.target.value)} className="w-full pl-12 pr-4 py-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" placeholder="Digite sua senha" required />
            </div>
            {erro && <p className="text-red-500 text-sm text-center mb-4">{erro}</p>}
            <button type="submit" disabled={carregando} className="w-full bg-blue-600 hover:bg-blue-700 text-white font-bold py-3 px-4 rounded-lg transition disabled:bg-blue-300">
              {carregando ? 'Autenticando...' : 'Entrar'}
            </button>
          </form>
          <div className="text-center mt-6">
            <a href="#" className="text-sm text-blue-600 hover:underline">Esqueceu sua senha?</a>
          </div>
        </div>
        <p className="text-center text-gray-500 text-xs mt-6">&copy;{new Date().getFullYear()} Marinha do Brasil. Todos os direitos reservados.</p>
      </div>
    </div>
  );
};