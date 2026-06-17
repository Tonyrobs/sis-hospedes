const API_URL = 'http://localhost:8080/api/hospedes';
let baseHospedes = [];

// ── MÁSCARAS ──
function mascaraCPF(e) {
    let v = e.target.value.replace(/\D/g, '').slice(0, 11);
    if (v.length > 9) v = v.replace(/(\d{3})(\d{3})(\d{3})(\d+)/, '$1.$2.$3-$4');
    else if (v.length > 6) v = v.replace(/(\d{3})(\d{3})(\d+)/, '$1.$2.$3');
    else if (v.length > 3) v = v.replace(/(\d{3})(\d+)/, '$1.$2');
    e.target.value = v;
}

function mascaraTelefone(e) {
    let v = e.target.value.replace(/\D/g, '').slice(0, 11);
    if (v.length > 6) v = v.replace(/(\d{2})(\d{5})(\d+)/, '($1) $2-$3');
    else if (v.length > 2) v = v.replace(/(\d{2})(\d+)/, '($1) $2');
    e.target.value = v;
}

function mascaraCEP(e) {
    let v = e.target.value.replace(/\D/g, '').slice(0, 8);
    if (v.length > 5) v = v.slice(0, 5) + '-' + v.slice(5);
    e.target.value = v;
}

// ── ALERTAS ──
function mostrarAlerta(msg, sucesso = true) {
    const box = document.getElementById('alert-box');
    box.textContent = (sucesso ? '✅  ' : '❌  ') + msg;
    box.className = sucesso ? 'success' : 'error';
    box.style.display = 'block';
    setTimeout(() => { box.style.display = 'none'; }, 4500);
}

// ── NAVEGAÇÃO ──
function mostrarTela(tela) {
    document.getElementById('tela-consulta').classList.add('hidden');
    document.getElementById('tela-cadastro').classList.add('hidden');
    document.getElementById('menu-consulta').classList.remove('active');
    document.getElementById('menu-cadastro').classList.remove('active');

    if (tela === 'consulta') {
        document.getElementById('tela-consulta').classList.remove('hidden');
        document.getElementById('menu-consulta').classList.add('active');
        document.getElementById('page-title').innerText = 'Gestão de Hóspedes';
        document.getElementById('page-breadcrumb').innerText = 'Início / Hóspedes';
        buscarHospedes();
    } else {
        document.getElementById('tela-cadastro').classList.remove('hidden');
        document.getElementById('menu-cadastro').classList.add('active');
    }
}

function novoHospede() {
    limparFormulario();
    document.getElementById('page-title').innerText = 'Novo Hóspede';
    document.getElementById('page-breadcrumb').innerText = 'Início / Hóspedes / Novo';
    document.getElementById('form-title').innerText = 'Novo Hóspede';
    document.getElementById('form-subtitle').innerText = 'Preencha os dados obrigatórios marcados com *';
    mostrarTela('cadastro');
}

// ── CRUD ──
async function buscarHospedes() {
    const nome  = document.getElementById('filtro-nome').value;
    const cpf   = document.getElementById('filtro-cpf').value.replace(/\D/g, '');
    const email = document.getElementById('filtro-email').value;

    const params = new URLSearchParams();
    if (nome)  params.append('nomeCompleto', nome);
    if (cpf)   params.append('cpf', cpf);
    if (email) params.append('email', email);

    try {
        const response = await fetch(`${API_URL}?${params.toString()}`);
        const data = await response.json();
        if (!response.ok) throw new Error(data || 'Erro ao consultar.');
        baseHospedes = data;
        renderizarTabela(data);
    } catch (error) {
        mostrarAlerta(error.message, false);
    }
}

function limparFiltros() {
    document.getElementById('filtro-nome').value = '';
    document.getElementById('filtro-cpf').value = '';
    document.getElementById('filtro-email').value = '';
    buscarHospedes();
}

function formatarCPF(cpf) {
    if (!cpf) return '—';
    const n = cpf.replace(/\D/g, '');
    if (n.length !== 11) return cpf;
    return n.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
}

function renderizarTabela(lista) {
    const tbody = document.getElementById('tabela-hospedes');
    const count = document.getElementById('result-count');

    if (!lista || lista.length === 0) {
        count.textContent = 'Nenhum resultado encontrado';
        tbody.innerHTML = `
            <tr><td colspan="6">
                <div class="empty-state">
                    <div class="empty-icon">🔍</div>
                    <p>Nenhum hóspede encontrado</p>
                    <div class="empty-hint">Tente ajustar os filtros de busca</div>
                </div>
            </td></tr>`;
        return;
    }

    count.textContent = `${lista.length} hóspede${lista.length !== 1 ? 's' : ''} encontrado${lista.length !== 1 ? 's' : ''}`;
    tbody.innerHTML = '';

    lista.forEach(h => {
        const tr = document.createElement('tr');
        const badge = h.ativo
            ? `<span class="badge badge-active">Ativo</span>`
            : `<span class="badge badge-inactive">Inativo</span>`;

        const actions = h.ativo
            ? `<div class="btn-actions">
                   <button class="btn btn-icon btn-edit" onclick="prepararEdicao(${h.id})" title="Editar">✏️</button>
                   <button class="btn btn-icon btn-delete" onclick="inativarHospede(${h.id})" title="Inativar">🗑️</button>
               </div>`
            : `<span style="color:var(--slate);font-size:0.8rem;">—</span>`;

        tr.innerHTML = `
            <td class="id-col">#${h.id}</td>
            <td class="name-col">${h.nomeCompleto || '—'}</td>
            <td class="mono">${formatarCPF(h.cpf)}</td>
            <td>${h.email || '—'}</td>
            <td>${badge}</td>
            <td>${actions}</td>
        `;
        tbody.appendChild(tr);
    });
}

async function salvarHospede() {
    const id = document.getElementById('hospede-id').value;

    const hospede = {
        nomeCompleto:   document.getElementById('cad-nome').value,
        cpf:            document.getElementById('cad-cpf').value.replace(/\D/g, ''),
        dataNascimento: document.getElementById('cad-nascimento').value,
        telefone:       document.getElementById('cad-telefone').value.replace(/\D/g, ''),
        email:          document.getElementById('cad-email').value,
        ativo: true,
        endereco: {
            cep:         document.getElementById('cad-cep').value.replace(/\D/g, ''),
            logradouro:  document.getElementById('cad-logradouro').value,
            numero:      document.getElementById('cad-numero').value,
            complemento: document.getElementById('cad-complemento').value,
            bairro:      document.getElementById('cad-bairro').value,
            cidade:      document.getElementById('cad-cidade').value,
            estado:      document.getElementById('cad-estado').value,
        }
    };

    const url    = id ? `${API_URL}/${id}` : API_URL;
    const method = id ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(hospede)
        });
        const msg = await response.text();
        if (!response.ok) throw new Error(msg);
        mostrarAlerta(msg, true);
        limparFormulario();
        mostrarTela('consulta');
    } catch (error) {
        mostrarAlerta(error.message, false);
    }
}

function prepararEdicao(id) {
    const h = baseHospedes.find(i => i.id === id);
    if (!h) return;

    limparFormulario();
    document.getElementById('hospede-id').value = h.id;
    document.getElementById('cad-nome').value = h.nomeCompleto || '';

    const cpfEl = document.getElementById('cad-cpf');
    cpfEl.value = h.cpf || '';
    cpfEl.dispatchEvent(new Event('input'));

    if (h.dataNascimento) {
        const d = Array.isArray(h.dataNascimento)
            ? `${h.dataNascimento[0]}-${String(h.dataNascimento[1]).padStart(2,'0')}-${String(h.dataNascimento[2]).padStart(2,'0')}`
            : h.dataNascimento.split('T')[0];
        document.getElementById('cad-nascimento').value = d;
    }

    const telEl = document.getElementById('cad-telefone');
    telEl.value = h.telefone || '';
    telEl.dispatchEvent(new Event('input'));

    document.getElementById('cad-email').value = h.email || '';

    if (h.endereco) {
        const cepEl = document.getElementById('cad-cep');
        cepEl.value = h.endereco.cep || '';
        cepEl.dispatchEvent(new Event('input'));
        document.getElementById('cad-logradouro').value  = h.endereco.logradouro  || '';
        document.getElementById('cad-numero').value      = h.endereco.numero       || '';
        document.getElementById('cad-complemento').value = h.endereco.complemento  || '';
        document.getElementById('cad-bairro').value      = h.endereco.bairro       || '';
        document.getElementById('cad-cidade').value      = h.endereco.cidade       || '';
        document.getElementById('cad-estado').value      = h.endereco.estado       || '';
    }

    document.getElementById('page-title').innerText = `Editando Hóspede #${h.id}`;
    document.getElementById('page-breadcrumb').innerText = `Início / Hóspedes / Editar #${h.id}`;
    document.getElementById('form-title').innerText = `Editar Hóspede — ${h.nomeCompleto}`;
    document.getElementById('form-subtitle').innerText = `ID #${h.id} · Atualize os campos desejados e salve`;
    mostrarTela('cadastro');
}

async function inativarHospede(id) {
    if (!confirm('Confirma a inativação deste hóspede?')) return;
    try {
        const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
        const msg = await response.text();
        if (!response.ok) throw new Error(msg);
        mostrarAlerta(msg, true);
        buscarHospedes();
    } catch (error) {
        mostrarAlerta(error.message, false);
    }
}

function limparFormulario() {
    document.getElementById('hospede-id').value = '';
    document.querySelectorAll('#tela-cadastro input[type!="hidden"]').forEach(i => i.value = '');
}

// ── INIT ──
window.onload = () => {
    document.getElementById('filtro-cpf').addEventListener('input', mascaraCPF);
    document.getElementById('cad-cpf').addEventListener('input', mascaraCPF);
    document.getElementById('cad-telefone').addEventListener('input', mascaraTelefone);
    document.getElementById('cad-cep').addEventListener('input', mascaraCEP);
    buscarHospedes();
};
