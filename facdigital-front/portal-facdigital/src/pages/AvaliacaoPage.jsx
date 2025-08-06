import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

const BackIcon = () => ( <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" className="h-5 w-5 mr-2"><path d="M19 12H5"></path><polyline points="12 19 5 12 12 5"></polyline></svg> );

export default function AvaliacaoPage({ token }) {
    const { id } = useParams(); // Pega o ID da avaliação da URL
    const navigate = useNavigate();
    const [avaliacao, setAvaliacao] = useState(null);
    const [avaliadoSelecionado, setAvaliadoSelecionado] = useState(null);
    const [carregando, setCarregando] = useState(true);
    const [erro, setErro] = useState('');

    const fetchAvaliacao = useCallback(async () => {
        try {
            setCarregando(true);
            const response = await fetch(`/fac/avaliacoes/${id}`, { headers: { 'Authorization': `Bearer ${token}` } });
            if (!response.ok) throw new Error('Falha ao buscar dados da avaliação.');
            const data = await response.json();
            setAvaliacao(data);
            if (data.avaliados && data.avaliados.length > 0) {
                setAvaliadoSelecionado(data.avaliados[0]);
            }
        } catch (error) {
            setErro(error.message);
        } finally {
            setCarregando(false);
        }
    }, [id, token]);

    useEffect(() => {
        fetchAvaliacao();
    }, [fetchAvaliacao]);
    
    const handleGrauChange = async (avaliadoId, novoGrau) => {
        const avaliadosAtualizados = avaliacao.avaliados.map(a => a.id === avaliadoId ? { ...a, grau: novoGrau } : a);
        setAvaliacao({ ...avaliacao, avaliados: avaliadosAtualizados });

        try {
            await fetch(`/fac/avaliacoes/${id}/avaliados/${avaliadoId}/grau`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
                body: JSON.stringify({ grau: novoGrau }),
            });
        } catch (error) {
            setErro('Falha ao salvar o grau.');
        }
    };
    
    const todosPreenchidos = avaliacao?.avaliados.every(a => a.grau && a.grau.trim() !== '');

    if (carregando) return <div className="min-h-screen bg-gray-100 flex items-center justify-center"><p>Carregando avaliação...</p></div>;
    if (erro) return <div className="min-h-screen bg-gray-100 flex items-center justify-center"><p className="text-red-500">{erro}</p></div>;
    if (!avaliacao) return null;

    const StatusTracker = ({ status }) => (
        <div className="flex items-center justify-center space-x-4 my-4 p-4 bg-gray-50 rounded-lg">
            <div className={`flex items-center font-bold ${status === 'INICIADA' || status === 'EM_PROGRESSO' || status === 'CONCLUIDA' ? 'text-blue-600' : 'text-gray-400'}`}>INICIADA</div>
            <div className={`flex-1 h-1 ${status === 'EM_PROGRESSO' || status === 'CONCLUIDA' ? 'bg-blue-600' : 'bg-gray-300'}`}></div>
            <div className={`flex items-center font-bold ${status === 'EM_PROGRESSO' || status === 'CONCLUIDA' ? 'text-blue-600' : 'text-gray-400'}`}>EM PROGRESSO</div>
            <div className={`flex-1 h-1 ${status === 'CONCLUIDA' ? 'bg-blue-600' : 'bg-gray-300'}`}></div>
            <div className={`flex items-center font-bold ${status === 'CONCLUIDA' ? 'text-blue-600' : 'text-gray-400'}`}>CONCLUÍDA</div>
        </div>
    );

    return (
        <div className="min-h-screen bg-gray-100">
            <header className="bg-white shadow-md">
                <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8 flex justify-between items-center">
                    <h1 className="text-2xl font-bold text-gray-800">Formulário de Avaliação</h1>
                    <button onClick={() => navigate('/dashboard')} className="flex items-center bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded-lg transition"><BackIcon />Voltar ao Dashboard</button>
                </div>
            </header>
            <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                 <div className="bg-white shadow-xl rounded-2xl p-6">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4 text-sm">
                        <div><strong>Evento:</strong> {avaliacao.eventoDescricao}</div>
                        <div><strong>Avaliador:</strong> {avaliacao.avaliador.posto} {avaliacao.avaliador.nome}</div>
                        <div className="text-red-600 font-bold"><strong>Data Limite:</strong> {new Date(avaliacao.dataLimiteRemessa).toLocaleDateString('pt-BR', {timeZone: 'UTC'})}</div>
                    </div>
                    <StatusTracker status={avaliacao.status} />
                    <div className="mt-6 grid grid-cols-1 lg:grid-cols-3 gap-6">
                        <div className="lg:col-span-2 overflow-x-auto">
                            <table className="min-w-full divide-y divide-gray-200">
                                <thead className="bg-gray-50">
                                    <tr>
                                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">ANT</th>
                                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">NIP</th>
                                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Nome do Oficial</th>
                                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">OM</th>
                                        <th className="px-4 py-3 text-center text-xs font-medium text-gray-500 uppercase">Grau</th>
                                    </tr>
                                </thead>
                                <tbody className="bg-white divide-y divide-gray-200">
                                    {avaliacao.avaliados.map((avaliado) => (
                                        <tr key={avaliado.id} onClick={() => setAvaliadoSelecionado(avaliado)} className={`cursor-pointer ${avaliadoSelecionado?.id === avaliado.id ? 'bg-blue-50' : 'hover:bg-gray-50'}`}>
                                            <td className="px-4 py-4 text-sm text-gray-500">{avaliado.antiguidade}</td>
                                            <td className="px-4 py-4 text-sm text-gray-500">{avaliado.nip}</td>
                                            <td className="px-4 py-4 text-sm font-medium text-gray-900">{avaliado.nome}</td>
                                            <td className="px-4 py-4 text-sm text-gray-500">{avaliado.omSigla}</td>
                                            <td className="px-4 py-4 text-sm">
                                                <input 
                                                    type="text"
                                                    value={avaliado.grau || ''}
                                                    onChange={(e) => handleGrauChange(avaliado.id, e.target.value.toUpperCase())}
                                                    className="w-16 text-center border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500"
                                                />
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                        <div className="flex flex-col items-center bg-gray-50 p-4 rounded-lg">
                            {avaliadoSelecionado ? (
                                <>
                                    <img src={`data:image/jpeg;base64,${avaliadoSelecionado.fotoBase64}`} alt="Foto do Avaliado" className="w-40 h-52 object-cover rounded-md shadow-lg mb-4 border-4 border-white"/>
                                    <h3 className="font-bold text-lg text-gray-800">{avaliadoSelecionado.nomeDeGuerra}</h3>
                                    <p className="text-sm text-gray-600">{avaliadoSelecionado.nip}</p>
                                    <p className="text-sm text-gray-500 mt-2">{avaliadoSelecionado.omSigla}</p>
                                </>
                            ) : (
                                <div className="text-center text-gray-500">Selecione um militar na tabela para ver os detalhes.</div>
                            )}
                        </div>
                    </div>
                     <div className="mt-6 flex justify-end">
                        <button disabled={!todosPreenchidos} className="bg-green-600 hover:bg-green-700 text-white font-bold py-2 px-6 rounded-lg transition disabled:bg-gray-400">
                            Enviar Avaliação
                        </button>
                    </div>
                </div>
            </main>
        </div>
    );
};