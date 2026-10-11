var TEAL = '#2F7C82', WATER = '#8DB4C1', SAND = '#D9C9B3', TAUPE = '#8A7B69';
var GREEN = '#6B8F6E', OCHRE = '#C08A2E', RED = '#B4553E';

// Status colour: green = fine, ochre = watch, red = problem
var STATUS_COLOURS = {
	NORMAL: GREEN, PASS: GREEN, WORKING: GREEN, DONE: GREEN,
	WATCH: OCHRE, UNDER_REPAIR: OCHRE, IN_PROGRESS: OCHRE, SCHEDULED: WATER,
	CRITICAL: RED, FAIL: RED, FAULTY: RED,
	'NOT TESTED': SAND, NO_DATA: SAND
};
var OTHER_COLOURS = [TEAL, WATER, OCHRE, TAUPE, SAND, GREEN];

function colourFor(label, index) {
	return STATUS_COLOURS[label] || OTHER_COLOURS[index % OTHER_COLOURS.length];
}

Chart.defaults.font.family = 'system-ui, "Segoe UI", Arial, sans-serif';
Chart.defaults.color = '#5c5247';
Chart.defaults.maintainAspectRatio = false;

// counts = { "NORMAL": 5, "WATCH": 8, ... }
function doughnutChart(canvasId, counts) {
	if (!counts) return;
	var labels = Object.keys(counts);
	new Chart(document.getElementById(canvasId), {
		type: 'doughnut',
		data: {
			labels: labels,
			datasets: [{ data: Object.values(counts), backgroundColor: labels.map(colourFor) }]
		},
		options: { plugins: { legend: { position: 'right' } } }
	});
}

// labels = ["A", "B"], values = [10, 20]
function barChart(canvasId, labels, values, title, horizontal) {
	if (!labels || !values) return;
	new Chart(document.getElementById(canvasId), {
		type: 'bar',
		data: {
			labels: labels,
			datasets: [{ label: title, data: values, backgroundColor: labels.map(colourFor) }]
		},
		options: {
			indexAxis: horizontal ? 'y' : 'x',
			plugins: { legend: { display: false } },
			scales: { x: { beginAtZero: true }, y: { beginAtZero: true } }
		}
	});
}

// counts = { "DOMESTIC": 120, "INDUSTRIAL": 30 }
function countsBarChart(canvasId, counts, title, horizontal) {
	if (!counts) return;
	barChart(canvasId, Object.keys(counts), Object.values(counts), title, horizontal);
}

// Two bars per label
function twoBarChart(canvasId, labels, firstValues, firstTitle, secondValues, secondTitle) {
	if (!labels) return;
	new Chart(document.getElementById(canvasId), {
		type: 'bar',
		data: {
			labels: labels,
			datasets: [
				{ label: firstTitle, data: firstValues, backgroundColor: SAND },
				{ label: secondTitle, data: secondValues, backgroundColor: TEAL }
			]
		},
		options: { plugins: { legend: { position: 'bottom' } }, scales: { y: { beginAtZero: true } } }
	});
}
