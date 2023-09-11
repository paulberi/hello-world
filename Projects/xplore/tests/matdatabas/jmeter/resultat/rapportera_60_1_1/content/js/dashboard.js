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
var seriesFilter = "^(/api/matrunda|/api/kartlager/tree|/api/matobjekt/mapinfo|/api/matrunda/X/senastematningar|/api/matrunda/X|/api/matrunda/X/matningstyper|bifogad_bild|rapportera_varde)(-success|-failure)?$";
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

    var data = {"OkPercent": 99.3849938499385, "KoPercent": 0.6150061500615006};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.44518272425249167, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.43333333333333335, 500, 1500, "/api/matrunda/X"], "isController": false}, {"data": [0.0, 500, 1500, "/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan"], "isController": false}, {"data": [0.23333333333333334, 500, 1500, "/api/matrunda/X/matningstyper"], "isController": false}, {"data": [0.4163265306122449, 500, 1500, "bifogad_bild"], "isController": false}, {"data": [0.0, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [0.0, 500, 1500, "Välja mätrunda i dropdown"], "isController": true}, {"data": [0.0, 500, 1500, "Öppna rapporteringsfliken"], "isController": true}, {"data": [0.6963087248322147, 500, 1500, "rapportera_varde"], "isController": false}, {"data": [0.375, 500, 1500, "/api/matrunda"], "isController": false}, {"data": [0.45, 500, 1500, "/api/matrunda/X/senastematningar"], "isController": false}, {"data": [0.0, 500, 1500, "Läsa upp mätrunda"], "isController": true}, {"data": [0.5305555555555556, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 3252, 20, 0.6150061500615006, 1252.9397293972922, 24, 10704, 2212.0, 3811.0, 9294.989999999976, 89.91373589913735, 16110.871596611645, 33.16915807827361], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matrunda/X", 120, 0, 0.0, 1087.216666666666, 38, 2507, 1619.7, 1653.1, 2507.0, 11.863568956994563, 110.9651121478003, 4.652936542263964], "isController": false}, {"data": ["/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan", 120, 0, 0.0, 8204.450000000004, 3298, 10704, 10216.900000000001, 10627.949999999999, 10704.0, 9.911621376063435, 11249.670903196498, 2.98122986701908], "isController": false}, {"data": ["/api/matrunda/X/matningstyper", 120, 0, 0.0, 1617.8166666666666, 341, 3582, 2336.5, 2390.8, 3582.0, 10.418475429762111, 1368.4316463904322, 2.8754246071366554], "isController": false}, {"data": ["bifogad_bild", 980, 0, 0.0, 1052.2163265306135, 53, 3130, 1770.6, 1890.0, 2308.0, 51.81347150259067, 21045.055807701967, 14.623137953367875], "isController": false}, {"data": ["/api/kartlager/tree", 120, 0, 0.0, 3254.2000000000003, 1974, 5815, 4379.8, 4417.95, 5815.0, 17.827960184222256, 69.01370524439162, 3.4472032387461002], "isController": false}, {"data": ["Välja mätrunda i dropdown", 120, 0, 0.0, 9420.683333333329, 5798, 11804, 11559.300000000001, 11732.0, 11804.0, 9.831230542356218, 11801.007349100853, 6.217645266672129], "isController": true}, {"data": ["Öppna rapporteringsfliken", 120, 0, 0.0, 4518.0166666666655, 3774, 7405, 6142.500000000007, 6652.149999999997, 7405.0, 14.212957479568875, 110.62233507047259, 7.106478739784437], "isController": true}, {"data": ["rapportera_varde", 1192, 20, 1.6778523489932886, 570.1006711409385, 30, 2172, 898.0, 1016.0, 1790.0, 55.098456133863365, 30.536063025792732, 27.56755497711935], "isController": false}, {"data": ["/api/matrunda", 120, 0, 0.0, 1263.8166666666666, 24, 4262, 1946.8000000000002, 2260.95, 4262.0, 20.920502092050206, 81.84329236401673, 6.415075836820084], "isController": false}, {"data": ["/api/matrunda/X/senastematningar", 120, 0, 0.0, 1086.4499999999998, 78, 2834, 1640.5, 1935.35, 2834.0, 13.693940431359124, 735.3021938833733, 3.5387156938263153], "isController": false}, {"data": ["Läsa upp mätrunda", 120, 0, 0.0, 14352.966666666667, 2592, 18454, 17469.100000000002, 18108.449999999993, 18454.0, 5.358578190586765, 19516.848559463473, 20.87045846543717], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 360, 0, 0.0, 1061.5388888888879, 24, 4854, 2248.3000000000034, 2707.0499999999993, 4014.0, 23.75923970432946, 1552.929661719575, 7.879915811444034], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 20, 100.0, 0.6150061500615006], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 3252, 20, "500", 20, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["rapportera_varde", 1192, 20, "500", 20, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
