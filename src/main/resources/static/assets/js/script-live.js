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

    function renderDashboard(d) {

        // Provincia (stacked)
        ensureCharts();
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

        // Lista recintos: participación con barra
        const cont = document.getElementById('listRecintos');
        if (cont) {
            cont.innerHTML = '';
            // ordena por menor→mayor o al gusto
            const data = d.porRecinto.slice().sort((a, b) => Number(b.participacionpct || 0) - Number(a.participacionpct || 0));
            data.forEach(x => {
                const pct = Number(x.participacionpct || 0);
                const tot = Number(x.total || 0);
                const hab = Number(x.habilitados || 0);
                const row = document.createElement('div');
                row.className = 'progress-row';
                row.innerHTML = `
                            <div style="min-width:180px"><strong>${x.recinto}</strong><div class="badge-soft badge-gray">${x.municipio} · ${x.provincia}</div></div>
                            <div class="progress-wrap"><div class="progress-inner" style="width:${Math.min(pct, 100)}%; background:${pct >= 70 ? '#16a34a' : pct >= 40 ? '#f59e0b' : '#9ca3af'}"></div></div>
                            <div style="min-width:120px; text-align:right">${tot}/${hab} · ${pct.toFixed(1)}%</div>
                        `;
                cont.appendChild(row);
            });
        }
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

})();