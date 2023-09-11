/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 7;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 100.0, "KoPercent": 0.0};
    var dataset = [
        {
            "label" : "KO",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "OK",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [1.0, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "GET Matrunda 19"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 17"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 51"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 16"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 50"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 52"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 14"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 48"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 51"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 52"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 47"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 49"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 50"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 43"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 46"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 45"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 27"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 42"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 41"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 39"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 38"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 33"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 39"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 38"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 33"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 27"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 7"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 8"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 5"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 6"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 6"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 48"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 7"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 49"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 8"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 46"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 9"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 47"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 9"], "isController": false}, {"data": [1.0, 500, 1500, "GET Alla Matrundor"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 2"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 3"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 4"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 5"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 3"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 4"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 1"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 1"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 2"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 19"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 45"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 42"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 43"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 14"], "isController": false}, {"data": [1.0, 500, 1500, "GET Matrunda 41"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 17"], "isController": false}, {"data": [1.0, 500, 1500, "POST Id runda 16"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1140, 0, 0.0, 89.6578947368421, 73, 202, 119.0, 134.0, 180.0, 21.301244441122616, 438.7867312166, 11.90129664318547], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["GET Matrunda 19", 20, 0, 0.0, 76.49999999999999, 75, 79, 78.9, 79.0, 79.0, 0.41741453437408693, 2.9349459448177986, 0.09253232353800558], "isController": false}, {"data": ["GET Matrunda 17", 20, 0, 0.0, 76.19999999999999, 74, 82, 81.50000000000001, 82.0, 82.0, 0.4172577818576316, 0.3296499467996328, 0.09249757468914295], "isController": false}, {"data": ["POST Id runda 51", 20, 0, 0.0, 74.5, 73, 75, 75.0, 75.0, 75.0, 0.4174668113884946, 0.4337741087083577, 0.1174125407030141], "isController": false}, {"data": ["GET Matrunda 16", 20, 0, 0.0, 76.5, 75, 80, 79.80000000000001, 80.0, 80.0, 0.4172142603834199, 0.7044565001981768, 0.09248792686234016], "isController": false}, {"data": ["POST Id runda 50", 20, 0, 0.0, 74.1, 73, 76, 75.9, 76.0, 76.0, 0.4174755254973177, 0.198545489176947, 0.11374577306030435], "isController": false}, {"data": ["POST Id runda 52", 20, 0, 0.0, 74.70000000000002, 73, 77, 77.0, 77.0, 77.0, 0.4144562334217507, 0.19710955632460211, 0.11292313391080902], "isController": false}, {"data": ["GET Matrunda 14", 20, 0, 0.0, 76.0, 75, 77, 77.0, 77.0, 77.0, 0.4148861137617724, 1.1778065749076878, 0.09197182404679914], "isController": false}, {"data": ["POST Id runda 48", 20, 0, 0.0, 85.5, 76, 163, 154.6000000000002, 163.0, 163.0, 0.4174493842621582, 3.101110806720935, 0.1528745303694427], "isController": false}, {"data": ["GET Matrunda 51", 20, 0, 0.0, 75.39999999999999, 74, 77, 76.9, 77.0, 77.0, 0.4174668113884946, 0.24664787196292895, 0.09254391229022292], "isController": false}, {"data": ["GET Matrunda 52", 20, 0, 0.0, 75.1, 74, 76, 76.0, 76.0, 76.0, 0.4144304689280756, 0.20033504113222403, 0.09187081684245425], "isController": false}, {"data": ["POST Id runda 47", 20, 0, 0.0, 75.8, 74, 80, 79.60000000000001, 80.0, 80.0, 0.4153772664022098, 1.1820403850547259, 0.12899411202724875], "isController": false}, {"data": ["POST Id runda 49", 20, 0, 0.0, 74.69999999999999, 74, 76, 76.0, 76.0, 76.0, 0.4173884007763424, 0.432877423461402, 0.1173904877183463], "isController": false}, {"data": ["GET Matrunda 50", 20, 0, 0.0, 74.8, 74, 77, 76.9, 77.0, 77.0, 0.41745809764344904, 0.2046523095869252, 0.09254198062994427], "isController": false}, {"data": ["POST Id runda 43", 20, 0, 0.0, 76.3, 74, 82, 81.50000000000001, 82.0, 82.0, 0.4159041756779238, 1.0523512882631842, 0.12469002141906504], "isController": false}, {"data": ["POST Id runda 46", 20, 0, 0.0, 76.0, 75, 77, 77.0, 77.0, 77.0, 0.4174668113884946, 2.5953063684562077, 0.16225760833263755], "isController": false}, {"data": ["POST Id runda 45", 20, 0, 0.0, 117.0, 114, 120, 119.9, 120.0, 120.0, 0.41664930627890506, 10.830033774634389, 0.2799362526561393], "isController": false}, {"data": ["GET Matrunda 27", 20, 0, 0.0, 75.6, 75, 76, 76.0, 76.0, 76.0, 0.4148172729912473, 0.8438128707429378, 0.0919565634463019], "isController": false}, {"data": ["POST Id runda 42", 20, 0, 0.0, 111.9, 109, 119, 118.60000000000001, 119.0, 119.0, 0.41675349031048137, 6.242755652219212, 0.24378451239841634], "isController": false}, {"data": ["POST Id runda 41", 20, 0, 0.0, 75.5, 75, 77, 77.0, 77.0, 77.0, 0.4174493842621582, 0.31553303068252975, 0.11536931225213944], "isController": false}, {"data": ["POST Id runda 39", 20, 0, 0.0, 84.0, 74, 149, 142.30000000000013, 149.0, 149.0, 0.4173971116119876, 1.8803250740879875, 0.14837162951832375], "isController": false}, {"data": ["POST Id runda 38", 20, 0, 0.0, 182.99999999999997, 175, 202, 200.60000000000002, 202.0, 202.0, 0.4152651467962294, 138.95607208483867, 2.8139891148623395], "isController": false}, {"data": ["POST Id runda 33", 20, 0, 0.0, 77.9, 76, 88, 87.00000000000003, 88.0, 88.0, 0.4173971116119876, 2.1839977773603807, 0.14470310021704652], "isController": false}, {"data": ["GET Matrunda 39", 20, 0, 0.0, 84.0, 74, 164, 155.2000000000002, 164.0, 164.0, 0.41742324630058647, 0.4557413958633357, 0.09253425479514955], "isController": false}, {"data": ["GET Matrunda 38", 20, 0, 0.0, 86.0, 80, 100, 98.70000000000003, 100.0, 100.0, 0.41584364279031083, 21.013505172055307, 0.09218408878261773], "isController": false}, {"data": ["GET Matrunda 33", 20, 0, 0.0, 75.50000000000001, 74, 81, 80.50000000000001, 81.0, 81.0, 0.41741453437408693, 0.48997292023208244, 0.09253232353800558], "isController": false}, {"data": ["POST Id runda 27", 20, 0, 0.0, 110.3, 108, 113, 112.9, 113.0, 113.0, 0.4145679165889352, 5.118132423356756, 0.19635296830628277], "isController": false}, {"data": ["POST Id runda 7", 20, 0, 0.0, 130.59999999999997, 125, 137, 136.8, 137.0, 137.0, 0.41509277323481797, 46.00800739902868, 0.744248370760865], "isController": false}, {"data": ["POST Id runda 8", 20, 0, 0.0, 120.59999999999998, 112, 133, 132.9, 133.0, 133.0, 0.4170750526557254, 20.412239588763995, 0.5189000166830021], "isController": false}, {"data": ["POST Id runda 5", 20, 0, 0.0, 120.9, 118, 126, 125.80000000000001, 126.0, 126.0, 0.4168056018672891, 23.562134643839613, 0.4603585309686562], "isController": false}, {"data": ["POST Id runda 6", 20, 0, 0.0, 112.0, 110, 116, 115.7, 116.0, 116.0, 0.4169967891247237, 10.737260096534756, 0.2789480474125349], "isController": false}, {"data": ["GET Matrunda 6", 20, 0, 0.0, 75.6, 75, 77, 77.0, 77.0, 77.0, 0.4172838991007532, 1.5672596444741178, 0.09209586054372092], "isController": false}, {"data": ["GET Matrunda 48", 20, 0, 0.0, 82.59999999999998, 74, 153, 145.30000000000018, 153.0, 153.0, 0.4174668113884946, 0.5165336426066628, 0.09254391229022292], "isController": false}, {"data": ["GET Matrunda 7", 20, 0, 0.0, 76.69999999999999, 76, 78, 78.0, 78.0, 78.0, 0.41542903433520967, 5.317410501007416, 0.09168648609351307], "isController": false}, {"data": ["GET Matrunda 49", 20, 0, 0.0, 74.89999999999999, 74, 76, 75.9, 76.0, 76.0, 0.4173884007763424, 0.2311125226954943, 0.09252653025022434], "isController": false}, {"data": ["GET Matrunda 8", 20, 0, 0.0, 83.6, 75, 135, 130.5000000000001, 135.0, 135.0, 0.4174493842621582, 3.20954980692966, 0.09213238363598414], "isController": false}, {"data": ["GET Matrunda 46", 20, 0, 0.0, 79.80000000000001, 75, 83, 82.9, 83.0, 83.0, 0.4174668113884946, 0.5870627035150705, 0.09254391229022292], "isController": false}, {"data": ["POST Id runda 9", 20, 0, 0.0, 144.10000000000002, 139, 155, 154.20000000000002, 155.0, 155.0, 0.41681428839380613, 66.05610971594106, 1.3684859741991957], "isController": false}, {"data": ["GET Matrunda 47", 20, 0, 0.0, 76.19999999999997, 75, 79, 78.9, 79.0, 79.0, 0.41536001329152045, 0.36830751178584037, 0.09207687794646009], "isController": false}, {"data": ["GET Matrunda 9", 20, 0, 0.0, 81.4, 77, 85, 84.9, 85.0, 85.0, 0.4173884007763424, 9.711617223532357, 0.0921189243900912], "isController": false}, {"data": ["GET Alla Matrundor", 20, 0, 0.0, 93.50000000000003, 86, 141, 136.3000000000001, 141.0, 141.0, 0.413317076195003, 1.7287471067804665, 0.11785994750873131], "isController": false}, {"data": ["GET Matrunda 2", 20, 0, 0.0, 75.2, 75, 76, 76.0, 76.0, 76.0, 0.41472265422498705, 0.9136858475894245, 0.09153058579574909], "isController": false}, {"data": ["GET Matrunda 3", 20, 0, 0.0, 77.29999999999998, 75, 83, 82.60000000000001, 83.0, 83.0, 0.4149463681819125, 2.5642388846241624, 0.09157996016514866], "isController": false}, {"data": ["GET Matrunda 4", 20, 0, 0.0, 76.3, 74, 82, 81.50000000000001, 82.0, 82.0, 0.41710984587791194, 1.82526290954973, 0.09205744645352354], "isController": false}, {"data": ["GET Matrunda 5", 20, 0, 0.0, 75.8, 75, 77, 76.9, 77.0, 77.0, 0.41723166788359234, 3.0037420465213307, 0.09208433295087097], "isController": false}, {"data": ["POST Id runda 3", 20, 0, 0.0, 119.5, 117, 125, 124.60000000000001, 125.0, 125.0, 0.4146710621799258, 21.27537916485248, 0.42924934170968876], "isController": false}, {"data": ["POST Id runda 4", 20, 0, 0.0, 112.49999999999999, 110, 118, 117.60000000000001, 118.0, 118.0, 0.4168924834285238, 13.897094650227208, 0.3098195116104557], "isController": false}, {"data": ["POST Id runda 1", 20, 0, 0.0, 117.9, 115, 130, 129.10000000000002, 130.0, 130.0, 0.4142158893215144, 10.013588222806726, 0.2325919300389363], "isController": false}, {"data": ["GET Matrunda 1", 20, 0, 0.0, 75.49999999999999, 74, 77, 77.0, 77.0, 77.0, 0.4144734115306503, 1.2709438595764082, 0.09147557715422557], "isController": false}, {"data": ["POST Id runda 2", 20, 0, 0.0, 111.7, 108, 122, 121.10000000000002, 122.0, 122.0, 0.4144390567367069, 5.576714612085042, 0.20438644887894233], "isController": false}, {"data": ["POST Id runda 19", 20, 0, 0.0, 115.29999999999998, 113, 119, 118.9, 119.0, 119.0, 0.41708375041708373, 18.2665575731982, 0.48632616991991995], "isController": false}, {"data": ["GET Matrunda 45", 20, 0, 0.0, 82.0, 77, 84, 84.0, 84.0, 84.0, 0.4169967891247237, 1.5698463106209082, 0.09243971790167214], "isController": false}, {"data": ["GET Matrunda 42", 20, 0, 0.0, 76.4, 75, 80, 79.7, 80.0, 80.0, 0.4170141784820684, 1.1594134434945789, 0.09244357276897415], "isController": false}, {"data": ["GET Matrunda 43", 20, 0, 0.0, 75.4, 75, 77, 77.0, 77.0, 77.0, 0.41588687876897484, 0.3180072520274485, 0.09219367332085672], "isController": false}, {"data": ["POST Id runda 14", 20, 0, 0.0, 110.6, 110, 113, 112.9, 113.0, 113.0, 0.4146280786134837, 7.62567441848412, 0.24659033190977694], "isController": false}, {"data": ["GET Matrunda 41", 20, 0, 0.0, 75.80000000000001, 75, 78, 77.9, 78.0, 78.0, 0.4174406712445994, 0.26049276262236226, 0.09253811755129301], "isController": false}, {"data": ["POST Id runda 17", 20, 0, 0.0, 77.0, 76, 85, 84.20000000000002, 85.0, 85.0, 0.4172751929897768, 1.105534764239516, 0.1275460306697267], "isController": false}, {"data": ["POST Id runda 16", 20, 0, 0.0, 81.00000000000001, 79, 83, 82.9, 83.0, 83.0, 0.4171881518564873, 4.397586827284106, 0.17192714851898208], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Percentile 1
            case 8:
            // Percentile 2
            case 9:
            // Percentile 3
            case 10:
            // Throughput
            case 11:
            // Kbytes/s
            case 12:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": []}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1140, 0, null, null, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
