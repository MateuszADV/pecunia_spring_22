
const reportName = document.getElementById('chart').getAttribute('value');
// const param = document.getElementById('param').getAttribute('value');

console.log(reportName);

const param = document.querySelector("#parametr").getAttribute('value');
const typeChart = document.querySelector("#typeChart").getAttribute('value');
console.log(param);
console.log(typeChart);

const root = location.protocol + '//' + location.host;
console.log(root);

if (param) {
    les = chartUrl = "http://localhost:8080/api/v1/report/?report=" + reportName + "&param=" + param
    console.log("param nie jest pusty - " + param)
} else {
    les = chartUrl = "http://localhost:8080/api/v1/report/?report=" + reportName;
    console.log("parametr jest pusty")
}

// fetch(chartUrl)
//     .then(function(response) {
//         response.json().then(data => {
//             var ctx = document.getElementById('myChart').getContext('2d');
//             // var chartData = [[${chartData}]];
//             var chartData = data;
//             var labels = chartData.chart.labels;
//             var datasets  = chartData.chart.datasets;
//             var options = chartData.chart.options;
//             console.log("Report Name - " + reportName);
//             console.log("PRZYKLADOWY TEKST test testu 6789123456");
//             console.log(chartData)
//             new Chart(ctx, {
//                 type: typeChart,
//                 data: {
//                     labels: labels,
//                     datasets: [datasets]
//                 },
//                 options: options
//             });
//         })
//             // .catch(error => console.log("Błąd: ", error));
//         console.log("JAKIS BLAD")
//     });
//

const ctx = document.getElementById('myChart');

fetch(chartUrl)
    .then(function(response) {
        response.json().then(data => {
            var chartData = data;
            var labels = chartData.chart.labels;
            var datasets = chartData.chart.datasets;
            var options = chartData.chart.options;
            console.log("Report Name - " + reportName);
            console.log(datasets)
            console.log("----------------START------------------")
            console.log(chartData.chart.datasets.at(0))
            console.log("----------------STOP-------------------")
            console.log("PRZYKLADOWY TEKST test testu 6789123456");
            console.log(chartData)
            new Chart(ctx, {
                type: typeChart,
                data: {
                    labels: labels,
                    datasets: datasets
                },
                options: options
            });
        })
    })

