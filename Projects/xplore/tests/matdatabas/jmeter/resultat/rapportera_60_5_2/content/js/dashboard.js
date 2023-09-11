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

    var data = {"OkPercent": 99.75772259236827, "KoPercent": 0.24227740763173833};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.5540688148552704, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.5583333333333333, 500, 1500, "/api/matrunda/X"], "isController": false}, {"data": [0.11666666666666667, 500, 1500, "/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan"], "isController": false}, {"data": [0.48333333333333334, 500, 1500, "/api/matrunda/X/matningstyper"], "isController": false}, {"data": [0.596116504854369, 500, 1500, "bifogad_bild"], "isController": false}, {"data": [0.25416666666666665, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [0.06666666666666667, 500, 1500, "Välja mätrunda i dropdown"], "isController": true}, {"data": [0.15833333333333333, 500, 1500, "Öppna rapporteringsfliken"], "isController": true}, {"data": [0.739513422818792, 500, 1500, "rapportera_varde"], "isController": false}, {"data": [0.6416666666666667, 500, 1500, "/api/matrunda"], "isController": false}, {"data": [0.5125, 500, 1500, "/api/matrunda/X/senastematningar"], "isController": false}, {"data": [0.0, 500, 1500, "Läsa upp mätrunda"], "isController": true}, {"data": [0.5513888888888889, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 6604, 16, 0.24227740763173833, 808.1059963658398, 15, 7416, 1377.5, 1917.25, 3933.8499999999995, 134.79476659931012, 22481.608820392707, 49.545781731573896], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matrunda/X", 240, 0, 0.0, 792.6999999999998, 26, 1645, 1160.1000000000001, 1238.55, 1549.0600000000009, 5.834305717619603, 39.554389707312325, 2.2881892533304162], "isController": false}, {"data": ["/api/matobjekt/mapinfo || Visa alla mätobjekt i kartan", 240, 0, 0.0, 2859.291666666667, 589, 7416, 4776.8, 5311.65, 6708.340000000006, 6.089979446319369, 6912.114777081378, 1.8317516303382477], "isController": false}, {"data": ["/api/matrunda/X/matningstyper", 240, 0, 0.0, 1115.4500000000003, 241, 3857, 1474.2, 1560.35, 3059.5500000000065, 5.815925943876314, 556.0125068912664, 1.6051065798720496], "isController": false}, {"data": ["bifogad_bild", 2060, 0, 0.0, 756.5864077669905, 36, 3812, 1228.0, 1368.0, 2005.7799999999997, 45.75845754014971, 16830.02007659266, 12.91425217685866], "isController": false}, {"data": ["/api/kartlager/tree", 240, 0, 0.0, 1812.3416666666665, 245, 4269, 3236.5, 3394.7999999999997, 4130.420000000001, 6.374332687046825, 24.675639425247667, 1.2325369844094445], "isController": false}, {"data": ["Välja mätrunda i dropdown", 240, 0, 0.0, 3684.891666666666, 940, 8451, 5723.9, 6191.049999999999, 7989.750000000004, 6.08580991987017, 7198.086723979993, 3.848848846865808], "isController": true}, {"data": ["Öppna rapporteringsfliken", 240, 0, 0.0, 2500.749999999999, 273, 5344, 4169.5, 4305.45, 5332.52, 6.234252019637894, 48.522449801283216, 3.117126009818947], "isController": true}, {"data": ["rapportera_varde", 2384, 16, 0.6711409395973155, 512.1023489932899, 15, 2080, 993.0, 1197.0, 1580.0, 52.490202122506496, 29.055411331960897, 26.259507175018715], "isController": false}, {"data": ["/api/matrunda", 240, 0, 0.0, 688.408333333333, 17, 2274, 1246.9, 1640.35, 2091.5500000000015, 6.295577356906773, 24.628987198992707, 1.930479775457741], "isController": false}, {"data": ["/api/matrunda/X/senastematningar", 240, 0, 0.0, 841.6916666666665, 47, 2067, 1352.4000000000005, 1635.8999999999996, 1957.940000000001, 5.833171300797201, 224.07827538644761, 1.5073298202654093], "isController": false}, {"data": ["Läsa upp mätrunda", 240, 0, 0.0, 10963.858333333326, 1971, 18071, 14788.1, 15311.4, 17326.440000000006, 5.06906602458497, 17200.821726624214, 20.338761128184007], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 720, 0, 0.0, 848.5277777777784, 21, 3297, 1445.6, 1727.599999999998, 2409.0, 17.38878423416896, 830.6199678941458, 5.766968781698305], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 16, 100.0, 0.24227740763173833], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 6604, 16, "500", 16, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["rapportera_varde", 2384, 16, "500", 16, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
