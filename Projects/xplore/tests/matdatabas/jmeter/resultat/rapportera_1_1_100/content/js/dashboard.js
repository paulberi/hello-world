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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.9893864013266999, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "/api/matrunda/X"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matrunda/X/matningstyper"], "isController": false}, {"data": [1.0, 500, 1500, "bifogad_bild"], "isController": false}, {"data": [1.0, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [1.0, 500, 1500, "Välja mätrunda i dropdown"], "isController": true}, {"data": [1.0, 500, 1500, "Öppna rapporteringsfliken"], "isController": true}, {"data": [1.0, 500, 1500, "rapportera_varde"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matrunda"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matrunda/X/senastematningar"], "isController": false}, {"data": [0.68, 500, 1500, "Läsa upp mätrunda"], "isController": true}, {"data": [1.0, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 5430, 0, 0.0, 37.98158379373844, 9, 427, 81.0, 161.0, 229.6899999999996, 51.44676254903075, 8683.31815556132, 18.972862691385746], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matrunda/X", 200, 0, 0.0, 12.400000000000006, 9, 31, 17.80000000000001, 22.0, 30.960000000000036, 1.9110958223445322, 10.786381590891716, 0.7494705667354661], "isController": false}, {"data": ["/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan", 200, 0, 0.0, 166.56999999999996, 140, 427, 187.60000000000002, 242.74999999999994, 426.1200000000008, 1.9077780109506457, 2165.324316300055, 0.5738238548562489], "isController": false}, {"data": ["/api/matrunda/X/matningstyper", 200, 0, 0.0, 216.38, 175, 333, 255.60000000000002, 273.4999999999999, 332.9100000000001, 1.9077962092089322, 157.57960726822662, 0.5264697781233009], "isController": false}, {"data": ["bifogad_bild", 1640, 0, 0.0, 26.299999999999983, 11, 123, 37.0, 54.94999999999982, 91.0, 15.957032770296566, 6208.310826609569, 4.5034985064606525], "isController": false}, {"data": ["/api/kartlager/tree", 200, 0, 0.0, 82.53999999999998, 78, 182, 84.9, 86.0, 181.6800000000003, 1.9068867213943157, 7.381737269147527, 0.368714424644604], "isController": false}, {"data": ["Välja mätrunda i dropdown", 200, 0, 0.0, 191.12, 162, 476, 214.70000000000002, 287.2999999999996, 474.890000000001, 1.9073595468113718, 2243.718379555013, 1.2062186477774492], "isController": true}, {"data": ["Öppna rapporteringsfliken", 200, 0, 0.0, 99.71, 92, 214, 104.0, 106.94999999999999, 213.5500000000004, 1.9052518266601888, 14.82896197117354, 0.9526259133300944], "isController": true}, {"data": ["rapportera_varde", 1990, 0, 0.0, 21.25226130653264, 14, 151, 24.0, 36.0, 114.0, 19.10228843495623, 10.557910261240593, 9.553337770935723], "isController": false}, {"data": ["/api/matrunda", 200, 0, 0.0, 17.17000000000001, 14, 32, 20.900000000000006, 24.899999999999977, 32.0, 1.9094172458565646, 7.469849108302146, 0.5855048976552356], "isController": false}, {"data": ["/api/matrunda/X/senastematningar", 200, 0, 0.0, 48.05999999999999, 41, 84, 58.80000000000001, 67.0, 83.97000000000003, 1.9102379201329527, 60.2655999281273, 0.49356518448122716], "isController": false}, {"data": ["Läsa upp mätrunda", 200, 0, 0.0, 528.9100000000005, 258, 926, 714.9, 818.0499999999997, 925.8200000000002, 1.9055422696913973, 6465.156191940509, 7.439244464399707], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 600, 0, 0.0, 20.32000000000001, 12, 75, 33.0, 42.94999999999993, 68.85000000000014, 5.720496539099594, 236.54163806418399, 1.897037319089297], "isController": false}]}, function(index, item){
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
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 5430, 0, null, null, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
