Chart.register(ChartDataLabels);
(function () {

    let lastTotal = 0;
    let lineT = [];
    let lineY = [];
    

    const cv = document.getElementById('chartVotos');
    const cl = document.getElementById('chartLinea');
    if (!cv || !cl || typeof Chart === 'undefined') {
        console.error('Chart.js o canvas no disponibles.');
        return;
    }

    const COLORS = {
        PDC: '#006E62',
        LIBRE: '#003B6E',
        NULO: '#f59e0b',
        BLANCO: '#9ca3af'
    };

    // Usamos tu misma paleta:
const PIE_COLORS = [COLORS.PDC, COLORS.LIBRE, COLORS.NULO, COLORS.BLANCO];

    const labelsTipos = ['PDC', 'LIBRE', 'NULO', 'BLANCO'];
    const bgColors = labelsTipos.map(l => COLORS[l]);

    const chartVotos = new Chart(cv.getContext('2d'), {
        type: 'bar',
        data: {
            labels: labelsTipos,
            datasets: [{
                label: 'Votos',
                data: [0, 0, 0, 0],
                backgroundColor: bgColors
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            animation: { duration: 300 },
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } },
            plugins: {
                legend: { display: false },
                datalabels: {
                    formatter: (value) => value || '',
                    anchor: 'end',
                    align: 'end',
                    offset: 4,
                    color: '#111827',
                    backgroundColor: '#ffffff',
                    borderRadius: 6,
                    padding: { top: 2, bottom: 2, left: 6, right: 6 },
                    font: { weight: 700, size: 11 },
                    clamp: true
                }
            }
        }
    });

    const chartLinea = new Chart(cl.getContext('2d'), {
        type: 'line',
        data: { labels: lineT, datasets: [{ label: 'Total acumulado', data: lineY, tension: 0.3, fill: false }] },
        options: {
            animation: { duration: 250 },
            responsive: true,
            maintainAspectRatio: false,
            scales: { y: { beginAtZero: true, ticks: { precision: 0 } } }
        }
    });

    const ctxT = document.getElementById('chartTorta').getContext('2d');
    const chartTorta = new Chart(ctxT, {
        type: 'doughnut',
        data: {
            labels: ['PDC', 'LIBRE', 'NULO', 'BLANCO'],
            datasets: [{
                data: [0, 0, 0, 0],
                backgroundColor: bgColors,
                borderColor: '#ffffff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            cutout: '60%',
            plugins: {
                legend: { position: 'bottom' },
                datalabels: {
                    formatter: (value, ctx) => {
                        const data = ctx.chart.data.datasets[0].data;
                        const total = data.reduce((a, b) => a + b, 0) || 1;
                        const pct = (value * 100 / total).toFixed(1);
                        return value ? `${value} (${pct}%)` : '';
                    },
                    color: '#111827',
                    backgroundColor: '#ffffff',
                    borderRadius: 6,
                    padding: { top: 3, bottom: 3, left: 6, right: 6 },
                    font: { weight: 700, size: 11 },
                    anchor: 'end',
                    align: 'end',
                    offset: 8,
                    clamp: true
                }
            }
        }
    });

    function renderResumen(r) {
        document.getElementById('kpi-total').textContent = r.total ?? 0;
        document.getElementById('kpi-pdc').textContent = r.pdc ?? 0;
        document.getElementById('kpi-libre').textContent = r.libre ?? 0;
        document.getElementById('kpi-nulo').textContent = r.nulo ?? 0;
        document.getElementById('kpi-blanco').textContent = r.blanco ?? 0;
        document.getElementById('kpi-updated').textContent = 'Actualizado: ' + (r.updatedAt || '—');

        const vals = [r.pdc || 0, r.libre || 0, r.nulo || 0, r.blanco || 0];
        chartTorta.data.datasets[0].data = vals;
        chartTorta.update('active');

        chartVotos.data.datasets[0].data = vals;
        chartVotos.update('active');

        if ((r.total || 0) !== lastTotal) {
            lastTotal = r.total || 0;
            const label = new Date().toLocaleTimeString();
            lineT.push(label);
            lineY.push(lastTotal);
            if (lineT.length > 60) { lineT.shift(); lineY.shift(); }
            chartLinea.update('active');
        }
    }

    let ticker = null;
    function startPolling() {
        if (ticker) return;
        ticker = setInterval(() => {
            if (document.hidden) return;
            fetch('/votol/api/resumen', { cache: 'no-store' })
                .then(r => r.ok ? r.json() : Promise.reject(r.status))
                .then(renderResumen)
                .catch(() => { });
        }, 2000);
    }
    function stopPolling() { if (ticker) { clearInterval(ticker); ticker = null; } }

    document.addEventListener('visibilitychange', () => {
        if (document.hidden) stopPolling(); else startPolling();
    });

    fetch('/votol/api/resumen', { cache: 'no-store' })
        .then(r => r.json()).then(r => { renderResumen(r); startPolling(); })
        .catch(() => startPolling());


    // CONFIGURACION DASHBOARD
    let chartProv = null, chartMun = null;

    function ensureCharts() {
        if (!chartProv) {
            chartProv = new Chart(document.getElementById('chartProv').getContext('2d'), {
                type: 'bar',
                data: {
                    labels: [], datasets: [
                        { label: 'PDC', data: [], backgroundColor: COLORS.PDC, stack: 's' },
                        { label: 'LIBRE', data: [], backgroundColor: COLORS.LIBRE, stack: 's' },
                        { label: 'NULO', data: [], backgroundColor: COLORS.NULO, stack: 's' },
                        { label: 'BLANCO', data: [], backgroundColor: COLORS.BLANCO, stack: 's' },
                    ]
                },
                options: {
                    responsive: true, maintainAspectRatio: false,
                    scales: { x: { stacked: true }, y: { stacked: true, beginAtZero: true, ticks: { precision: 0 } } }
                }
            });
        }
        if (!chartMun) {
            chartMun = new Chart(document.getElementById('chartMun').getContext('2d'), {
                type: 'bar',
                data: { labels: [], datasets: [{ label: 'Votos', data: [], backgroundColor: '#3b82f6' }] },
                options: {
                    indexAxis: 'y', responsive: true, maintainAspectRatio: false,
                    scales: { x: { beginAtZero: true, ticks: { precision: 0 } } },
                    plugins: { legend: { display: false } }
                }
            });
        }
    }

    /*LISTAR RECINTOS*/
      let REC_ALL = [];     // fuente completa (del backend)
  let REC_FILT = [];    // filtrada + ordenada para pintar
  // Virtualización
  const ROW_H = 44;     // alto aproximado de cada fila (ajústalo si cambias estilos)
  let startIdx = 0, endIdx = 0;

  function prepararRecintos(raw){
    // normaliza y calcula % con seguridad
    return raw.map(x => {
      const hab = Number(x.habilitados || 0);
      const tot = Number(x.total || 0);
      const pct = hab > 0 ? (tot / hab) * 100 : 0;
      return {
        recinto: String(x.recinto || ''),
        municipio: String(x.municipio || ''),
        provincia: String(x.provincia || ''),
        habilitados: hab,
        total: tot,
        participacionpct: pct
      };
    });
  }

  function filtrarYOrdenar(){
    const q = (document.getElementById('rec-buscar').value || '').toLowerCase().trim();
    const solo = document.getElementById('rec-solo-con-datos').checked;
    const orden = document.getElementById('rec-orden').value;

    let arr = REC_ALL.slice();

    if (solo){
      arr = arr.filter(r => r.total > 0 || r.participacionpct > 0);
    }
    if (q){
      arr = arr.filter(r => 
        r.recinto.toLowerCase().includes(q) ||
        r.municipio.toLowerCase().includes(q) ||
        r.provincia.toLowerCase().includes(q)
      );
    }

    switch(orden){
      case 'participacion-desc': arr.sort((a,b)=>b.participacionpct-a.participacionpct); break;
      case 'participacion-asc':  arr.sort((a,b)=>a.participacionpct-b.participacionpct); break;
      case 'total-desc':         arr.sort((a,b)=>b.total-a.total); break;
      case 'total-asc':          arr.sort((a,b)=>a.total-b.total); break;
      case 'nombre-asc':         arr.sort((a,b)=>a.recinto.localeCompare(b.recinto, 'es')); break;
    }
    REC_FILT = arr;
  }

  // Virtual render: pinta solo los visibles
  function renderVirtual(){
    const vp = document.getElementById('recintos-viewport');
    const list = document.getElementById('recintos-list');
    const top = document.getElementById('recintos-spacer-top');
    const bot = document.getElementById('recintos-spacer-bot');
    if (!vp) return;

    const vpH = vp.clientHeight;
    const scrollTop = vp.scrollTop;

    const total = REC_FILT.length;
    const visibleCount = Math.ceil(vpH / ROW_H) + 6; // margen de seguridad
    startIdx = Math.max(0, Math.floor(scrollTop / ROW_H) - 3);
    endIdx = Math.min(total, startIdx + visibleCount);

    // Espaciadores
    const topH = startIdx * ROW_H;
    const botH = (total - endIdx) * ROW_H;
    top.style.height = topH + 'px';
    bot.style.height = botH + 'px';

    // Fragment para minimizar reflows
    const frag = document.createDocumentFragment();
    list.innerHTML = '';
    for (let i = startIdx; i < endIdx; i++){
      const x = REC_FILT[i];
      const pct = Math.min(100, Math.max(0, x.participacionpct || 0));
      const color = pct >= 70 ? '#16a34a' : (pct >= 40 ? '#f59e0b' : '#9ca3af');

      const row = document.createElement('div');
      row.className = 'progress-row';
      row.style.height = ROW_H + 'px';
      row.innerHTML = `
        <div>
          <strong>${escapeHtml(x.recinto)}</strong>
          <div class="badge-soft text-muted">${escapeHtml(x.municipio)} · ${escapeHtml(x.provincia)}</div>
        </div>
        <div class="progress-wrap"><div class="progress-inner" style="width:${pct.toFixed(1)}%;background:${color}"></div></div>
        <div style="text-align:right">${x.total}/${x.habilitados} · ${pct.toFixed(1)}%</div>
      `;
      frag.appendChild(row);
    }
    list.appendChild(frag);
  }

  function escapeHtml(s){
    return String(s).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
  }

  // Eventos de controles
  function wireRecintosUI(){
    const vp = document.getElementById('recintos-viewport');
    ['rec-buscar','rec-orden','rec-solo-con-datos'].forEach(id=>{
      const el = document.getElementById(id);
      if (!el) return;
      const evt = id==='rec-buscar' ? 'input' : 'change';
      el.addEventListener(evt, ()=>{
        filtrarYOrdenar();
        // reajusta espaciadores y vuelve a pintar
        renderVirtual();
      });
    });
    if (vp){
      vp.addEventListener('scroll', renderVirtual, {passive:true});
      window.addEventListener('resize', renderVirtual);
    }
  }

    function renderDashboard(d) {

        // Provincia (stacked)
        ensureCharts();
        renderRecintoPieWidget(d);

        const provLabels = d.porProvincia.map(x => x.provincia);
        const sum = key => d.porProvincia.map(x => Number(x[key] || 0));
        chartProv.data.labels = provLabels;
        chartProv.data.datasets[0].data = sum('pdc');
        chartProv.data.datasets[1].data = sum('libre');
        chartProv.data.datasets[2].data = sum('nulo');
        chartProv.data.datasets[3].data = sum('blanco');
        chartProv.update('active');

        // Top municipios (horizontal)
        chartMun.data.labels = d.topMunicipios.map(x => x.municipio);
        chartMun.data.datasets[0].data = d.topMunicipios.map(x => Number(x.total || 0));
        chartMun.update('active');

        // ===== NUEVO: RECINTOS =====
        REC_ALL = prepararRecintos(d.porRecinto || []);
            filtrarYOrdenar();
            wireRecintosUI();     // solo hace bindings si no existen
            renderVirtual();      // pinta lo visible
        }

    // polling ligero (puedes unificar con tu intervalo existente)
    (function () {
        function tick() {
            fetch('/votol/api/dashboard', { cache: 'no-store' })
                .then(r => r.ok ? r.json() : Promise.reject(r.status))
                .then(renderDashboard)
                .catch(() => { });
        }
        tick();
        setInterval(() => { if (!document.hidden) tick(); }, 3000);
    })();

    const CATS = ['PDC', 'LIBRE', 'NULO', 'BLANCO'];
    const num = v => Number(v || 0);
const sumArr = a => a.reduce((s,n)=>s+num(n),0);

let PIE_REC = null;     // instancia de Chart
let REC_DATA = [];
let REC_SELECTED_KEY = null;     // índice seleccionado actualmente
let REC_USER_SELECTED = false;      // copia de d.porRecinto normalizada

function ensureRecintoPie() {
  if (PIE_REC) return;
  const ctx = document.getElementById('recinto-pie');
  PIE_REC = new Chart(ctx, {
    type: 'doughnut',
    data: {
      labels: CATS,
      datasets: [{
        data: [0,0,0,0],
        backgroundColor: PIE_COLORS,
        borderWidth: 1,
        borderColor: 'rgba(0,0,0,.06)'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: true,
      plugins: {
        legend: { position: 'right' },
        tooltip: {
          callbacks: {
            label: (ctx) => {
              const val = num(ctx.parsed);
              const total = sumArr(ctx.dataset.data);
              const pct = total ? (val*100/total) : 0;
              return ` ${ctx.label}: ${val.toLocaleString()} (${pct.toFixed(1)}%)`;
            }
          }
        }
      },
      cutout: '60%'
    }
  });
}

function fillRecintoSelect(data) {
  const sel = document.getElementById('recinto-select');
  const prev = sel.value;           // preserva la selección visual si coincide
  sel.innerHTML = '';
  data.forEach(r => {
    const opt = document.createElement('option');
    opt.value = r.key;
    opt.textContent = `${r.recinto} — ${r.municipio} · ${r.provincia}`;
    sel.appendChild(opt);
  });
  // intenta mantener selección visible si sigue existiendo
  if (prev && data.some(x => x.key === prev)) sel.value = prev;
}

function findIndexByKey(key) {
  if (!key) return -1;
  for (let i = 0; i < REC_DATA.length; i++) if (REC_DATA[i].key === key) return i;
  return -1;
}

function updateRecintoPieByKey(key) {
  const idx = findIndexByKey(key);
  if (!PIE_REC || idx < 0) return;
  const r = REC_DATA[idx];
  const vals = [num(r.pdc), num(r.libre), num(r.nulo), num(r.blanco)];
  PIE_REC.data.datasets[0].data = vals;
  PIE_REC.update(); // sin modo

  const total = vals.reduce((a,b)=>a+b,0);
  const ul = document.getElementById('recinto-stats');
  ul.innerHTML = `
    <li><span><span class="badge-dot" style="background:${PIE_COLORS[0]}"></span>PDC</span><strong>${vals[0].toLocaleString()}</strong></li>
    <li><span><span class="badge-dot" style="background:${PIE_COLORS[1]}"></span>LIBRE</span><strong>${vals[1].toLocaleString()}</strong></li>
    <li><span><span class="badge-dot" style="background:${PIE_COLORS[2]}"></span>NULO</span><strong>${vals[2].toLocaleString()}</strong></li>
    <li><span><span class="badge-dot" style="background:${PIE_COLORS[3]}"></span>BLANCO</span><strong>${vals[3].toLocaleString()}</strong></li>
    <li class="mt-2"><span>Total votos</span><strong>${total.toLocaleString()}</strong></li>
  `;
}

function wireRecintoSelect() {
  const sel = document.getElementById('recinto-select');
  if (!sel._wired) {
    sel.addEventListener('change', () => {
      REC_SELECTED_KEY = sel.value;   // guarda la clave
      REC_USER_SELECTED = true;
      updateRecintoPieByKey(REC_SELECTED_KEY);
    });
    sel._wired = true;
  }
}

// ======== Integra con tu renderDashboard(d) ========
// Llama estas 5 líneas dentro de TU renderDashboard(d) después de recibir datos.
function renderRecintoPieWidget(d) {
  // normaliza
REC_DATA = (d.porRecinto || []).map(r => {
  const rec = String(r.recinto || '');
  const mun = String(r.municipio || '');
  const pro = String(r.provincia || '');
  return {
    key: `${rec}|${mun}|${pro}`,   // clave estable
    recinto: rec,
    municipio: mun,
    provincia: pro,
    pdc: num(r.pdc), libre: num(r.libre), nulo: num(r.nulo), blanco: num(r.blanco)
  };
});

  ensureRecintoPie();

const prevKey = REC_SELECTED_KEY;
fillRecintoSelect(REC_DATA);
wireRecintoSelect();

// Determinar qué mostrar
let keyToShow = null;

if (REC_USER_SELECTED && prevKey && REC_DATA.some(x => x.key === prevKey)) {
  keyToShow = prevKey;                           // respeta la elección del usuario
} else {
  // auto-selección: el de mayor total
  let max = -1, best = null;
  for (const r of REC_DATA) {
    const t = num(r.pdc) + num(r.libre) + num(r.nulo) + num(r.blanco);
    if (t > max) { max = t; best = r.key; }
  }
  keyToShow = best || (REC_DATA[0] && REC_DATA[0].key) || null;
  REC_SELECTED_KEY = keyToShow;
  // no marcar REC_USER_SELECTED aquí
}

// pinta y sincroniza select visual
const sel = document.getElementById('recinto-select');
if (keyToShow) {
  sel.value = keyToShow;
  updateRecintoPieByKey(keyToShow);
}
}

// === ACTAS POR MUNICIPIO (selector escribible) ===
(function initActasPorMunicipio() {
  const inp = document.getElementById('inp-mun-actas');
  const dl  = document.getElementById('dl-mun-actas');
  const kpi = document.getElementById('kpi-actas-mun');

  // Si los elementos no existen aún, no hacemos nada.
  if (!inp || !dl || !kpi) {
    console.warn('[actas] Elementos no encontrados en el DOM');
    return;
  }

  let MUN_DATA = []; // { id, text, total }

  // util: quitar tildes para búsqueda flexible
  const norm = s => (s || '')
    .toString()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .toLowerCase()
    .trim();

  function cargarMunicipios() {
    fetch('/votol/api/actas/por-municipio', { cache: 'no-store' })
      .then(r => {
        if (!r.ok) throw new Error('HTTP ' + r.status);
        return r.json();
      })
      .then(list => {
        // normalizamos claves que pueden venir como idmunicipio/idMunicipio/…
        MUN_DATA = list.map(x => ({
          id: Number(x.idmunicipio ?? x.idMunicipio ?? x.id),
          text: String(x.municipio ?? x.nombre ?? '').trim(),
          total: Number(x.totalmesas ?? x.totalMesas ?? 0)
        }));

        // llena el datalist
        dl.innerHTML = '';
        for (const m of MUN_DATA.sort((a,b)=>a.text.localeCompare(b.text,'es'))) {
          const opt = document.createElement('option');
          opt.value = m.text;
          // (opcional) etiqueta visible en algunos navegadores
          opt.label = `${m.text} — ${m.total} acta${m.total===1?'':'s'}`;
          dl.appendChild(opt);
        }

        // si el usuario ya escribió algo, intenta mostrar su KPI
        if (inp.value) mostrarKpi(inp.value);
      })
      .catch(err => {
        console.error('[actas] Error cargando municipios:', err);
        kpi.textContent = '—';
      });
  }

  function mostrarKpi(nombre) {
    const txt = norm(nombre);
    if (!txt) { kpi.textContent = '—'; return; }

    // match exacto o por prefijo/contiene (más amigable)
    let found = MUN_DATA.find(m => norm(m.text) === txt);
    if (!found) found = MUN_DATA.find(m => norm(m.text).startsWith(txt));
    if (!found) found = MUN_DATA.find(m => norm(m.text).includes(txt));

    kpi.textContent = found ? found.total.toLocaleString() : '—';
  }

  // cuando el usuario cambia o escribe, actualizamos KPI
  inp.addEventListener('change',   () => mostrarKpi(inp.value));
  inp.addEventListener('input',    () => mostrarKpi(inp.value)); // en vivo

  // carga inicial
  cargarMunicipios();

  // si tus actas cambian con el tiempo, puedes recargar cada cierto tiempo:
  // setInterval(cargarMunicipios, 10000);
})();




})();