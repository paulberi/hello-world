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
var seriesFilter = "^(/api/anvandargrupp|/api/kartlager/tree|/api/matningar/godkann|/api/matningstyper/id_placeholder/gransvarden|/api/matningstyper/id_placeholder/matningDataSeries|/api/matobjekt/mapinfo|/api/matobjekt/matningstyper?|/api/meddelande|/api/user/matvarden/ogranskade|/api/user/paminnelser|/api/user/user-details|SMHI Stockholm|Stockholms Hamnar - Vattenstand Niva - Malaren|Stockholms Hamnar - Vattenstand Niva - Saltsjon)(-success|-failure)?$";
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.9847222222222223, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [1.0, 500, 1500, "/api/matningar/godkann"], "isController": false}, {"data": [1.0, 500, 1500, "SMHI Stockholm"], "isController": false}, {"data": [0.9, 500, 1500, "Granska mätvärden"], "isController": true}, {"data": [1.0, 500, 1500, "/api/anvandargrupp"], "isController": false}, {"data": [1.0, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Saltsjon"], "isController": false}, {"data": [1.0, 500, 1500, "Visa och granska"], "isController": true}, {"data": [1.0, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matningstyper/id_placeholder/gransvarden"], "isController": false}, {"data": [1.0, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Malaren"], "isController": false}, {"data": [1.0, 500, 1500, "/api/matobjekt/matningstyper?"], "isController": false}, {"data": [1.0, 500, 1500, "Öppna granskningsfliken"], "isController": true}, {"data": [1.0, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}, {"data": [0.55, 500, 1500, "Godkänna mätvärden"], "isController": true}, {"data": [1.0, 500, 1500, "/api/matningstyper/id_placeholder/matningDataSeries"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 320, 0, 0.0, 45.212500000000034, 7, 270, 160.80000000000007, 206.95, 222.74000000000012, 21.085925144965735, 426.0433440341658, 10.150111092349762], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matningar/godkann", 50, 0, 0.0, 172.07999999999996, 51, 270, 214.8, 238.39999999999986, 270.0, 3.5092644581695676, 0.8087757930937676, 5.354369911566536], "isController": false}, {"data": ["SMHI Stockholm", 10, 0, 0.0, 16.2, 14, 20, 19.8, 20.0, 20.0, 0.7622532205198567, 16.898349959981708, 0.21885004573519323], "isController": false}, {"data": ["Granska mätvärden", 10, 0, 0.0, 495.8, 465, 622, 610.2, 622.0, 622.0, 0.7121492664862555, 454.84375556366615, 3.58807784236576], "isController": true}, {"data": ["/api/anvandargrupp", 10, 0, 0.0, 10.3, 9, 13, 12.8, 13.0, 13.0, 0.7422802850356295, 0.6552943141330166, 0.2196395765290974], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Saltsjon", 10, 0, 0.0, 42.50000000000001, 37, 48, 47.9, 48.0, 48.0, 0.7600516835144789, 162.47960330052445, 0.21821796382153985], "isController": false}, {"data": ["Visa och granska", 10, 0, 0.0, 200.20000000000005, 182, 251, 246.8, 251.0, 251.0, 0.7360518180479906, 327.5011529561681, 2.6482051836449285], "isController": true}, {"data": ["/api/kartlager/tree", 10, 0, 0.0, 85.3, 79, 125, 120.9, 125.0, 125.0, 0.7357810315650063, 2.8482773526598484, 0.14227016040026488], "isController": false}, {"data": ["/api/matningstyper/id_placeholder/gransvarden", 100, 0, 0.0, 8.59, 7, 21, 9.900000000000006, 14.949999999999989, 20.989999999999995, 6.881365262868153, 2.31842872625929, 1.7923806083126892], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Malaren", 10, 0, 0.0, 42.9, 36, 53, 52.8, 53.0, 53.0, 0.7602828252109785, 153.79021777351173, 0.21828432676955828], "isController": false}, {"data": ["/api/matobjekt/matningstyper?", 10, 0, 0.0, 93.50000000000001, 86, 115, 113.7, 115.0, 115.0, 0.7382798080472499, 2.1577380952380953, 0.3821895764119601], "isController": false}, {"data": ["Öppna granskningsfliken", 10, 0, 0.0, 295.6, 272, 371, 364.70000000000005, 371.0, 371.0, 0.722908985758693, 140.06255704203716, 1.0413701610279766], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 10, 0, 0.0, 106.5, 98, 134, 131.4, 134.0, 134.0, 0.7401376656058026, 137.71887547646364, 0.320919065946266], "isController": false}, {"data": ["Godkänna mätvärden", 10, 0, 0.0, 951.0, 387, 1191, 1190.1, 1191.0, 1191.0, 0.7013606396409033, 5.520954792923272, 7.269904395777809], "isController": true}, {"data": ["/api/matningstyper/id_placeholder/matningDataSeries", 100, 0, 0.0, 10.329999999999998, 8, 27, 11.0, 13.0, 26.97999999999999, 6.775985905949316, 6.823232526426345, 1.9435962698197589], "isController": false}]}, function(index, item){
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
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 320, 0, null, null, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
