import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const LogoutIcon = () => ( <svg xmlns="[http://www.w3.org/2000/svg](http://www.w3.org/2000/svg)" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-5 w-5 mr-2"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path><polyline points="16 17 21 12 16 7"></polyline><line x1="21" y1="12" x2="9" y2="12"></line></svg> );

export default function DashboardPage({ token, onLogout }) {
    const [avaliacoes, setAvaliacoes] = useState([]);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const fetchAvaliador = async () => {
            try {
                const response = await fetch('/fac/avaliacoes/minhas', { headers: { 'Authorization': `Bearer ${token}` } });
                if (!response.ok) throw new Error('Falha ao buscar avaliações.');
                const data = await response.json();
                setAvaliacoes(data);
            } catch (error) {
                setErro(error.message);
            } finally {
                setCarregando(false);
            }
        };
        fetchAvaliador();
    }, [token]);

    const handleSelectAvaliacao = (id) => {
        navigate(`/avaliacao/${id}`); // Navega para a tela de avaliação
    };

    const StatusBadge = ({ status }) => {
        const statusMap = { 'INICIADA': 'bg-blue-100 text-blue-800', 'EM_PROGRESSO': 'bg-yellow-100 text-yellow-800', 'CONCLUIDA': 'bg-green-100 text-green-800', 'RETIFICADA': 'bg-gray-100 text-gray-800' };
        const statusText = { 'INICIADA': 'Iniciada', 'EM_PROGRESSO': 'Em Progresso', 'CONCLUIDA': 'Concluída', 'RETIFICADA': 'Retificada' };
        return <span className={`px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full ${statusMap[status] || ''}`}>{statusText[status] || status}</span>;
    };

    return (
        <div className="min-h-screen bg-gray-100">
            <header className="bg-white shadow-md">
                 <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
                    <div className="flex items-center">
                        <img src="[https://www.marinha.mil.br/cpo/sites/www.marinha.mil.br.cpo/files/cpo_h_branco_0.png](https://www.marinha.mil.br/cpo/sites/www.marinha.mil.br.cpo/files/cpo_h_branco_0.png)" alt="Logo CPO" className="h-12 mr-4"/>
                        <h1 className="text-2xl font-bold text-gray-800">Dashboard do Avaliador</h1>
                    </div>
                    <button onClick={onLogout} className="flex items-center bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded-lg transition"><LogoutIcon />Sair</button>
                </div>
            </header>
            <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                <div className="px-4 py-6 sm:px-0">
                    <div className="bg-white shadow-xl rounded-2xl p-6">
                        <h2 className="text-xl font-semibold text-gray-700 mb-4">Minhas FACs</h2>
                        {carregando && <p className="text-center text-gray-500">Carregando avaliações...</p>}
                        {erro && <p className="text-center text-red-500">{erro}</p>}
                        {!carregando && !erro && (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Evento</th>
                                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Data Limite</th>
                                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
                                            <th scope="col" className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Ação</th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {avaliacoes.map((avaliacao) => (
                                            <tr key={avaliacao.id}>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{avaliacao.eventoDescricao}</td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{new Date(avaliacao.dataLimiteRemessa).toLocaleDateString('pt-BR', {timeZone: 'UTC'})}</td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm"><StatusBadge status={avaliacao.status} /></td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                                                    <button onClick={() => handleSelectAvaliacao(avaliacao.id)} className="text-blue-600 hover:text-blue-900">
                                                        {avaliacao.status === 'CONCLUIDA' ? 'Ver Recibo' : 'Continuar'}
                                                    </button>
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    );
};