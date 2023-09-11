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

    var data = {"OkPercent": 99.22178988326849, "KoPercent": 0.7782101167315175};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.6733333333333333, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.8786127167630058, 500, 1500, "/api/matningar/godkann"], "isController": false}, {"data": [0.825, 500, 1500, "SMHI Stockholm"], "isController": false}, {"data": [0.0, 500, 1500, "Granska mätvärden"], "isController": true}, {"data": [0.35833333333333334, 500, 1500, "/api/anvandargrupp"], "isController": false}, {"data": [0.6166666666666667, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Saltsjon"], "isController": false}, {"data": [0.03333333333333333, 500, 1500, "Visa och granska"], "isController": true}, {"data": [0.075, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [0.9407514450867052, 500, 1500, "/api/matningstyper/id_placeholder/gransvarden"], "isController": false}, {"data": [0.625, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Malaren"], "isController": false}, {"data": [0.5416666666666666, 500, 1500, "/api/matobjekt/matningstyper?"], "isController": false}, {"data": [0.0, 500, 1500, "Öppna granskningsfliken"], "isController": true}, {"data": [0.575, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}, {"data": [0.0, 500, 1500, "Godkänna mätvärden"], "isController": true}, {"data": [0.9060693641618497, 500, 1500, "/api/matningstyper/id_placeholder/matningDataSeries"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 1285, 10, 0.7782101167315175, 557.763424124514, 7, 5345, 1238.6000000000004, 2001.4000000000005, 4173.540000000001, 82.70047625176986, 2133.3992042693717, 25.85948561590938], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matningar/godkann", 173, 10, 5.780346820809249, 295.3815028901736, 8, 1207, 530.4, 759.6999999999998, 1138.1799999999992, 30.804843304843306, 7.654260706018519, 13.206373252759972], "isController": false}, {"data": ["SMHI Stockholm", 60, 0, 0.0, 444.88333333333316, 36, 1165, 949.1999999999999, 1066.9, 1165.0, 7.685410529012425, 170.37744572178812, 2.2065534136031766], "isController": false}, {"data": ["Granska mätvärden", 60, 0, 0.0, 9997.26666666667, 5376, 13310, 12104.5, 12512.449999999999, 13310.0, 4.496739863598891, 2464.08060698962, 17.454581756351644], "isController": true}, {"data": ["/api/anvandargrupp", 60, 0, 0.0, 1351.3500000000001, 147, 3504, 2348.0, 3089.5999999999995, 3504.0, 8.575103615835356, 7.570208660854652, 2.537359761326283], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Saltsjon", 60, 0, 0.0, 708.1833333333334, 112, 1395, 1166.0, 1313.7999999999997, 1395.0, 7.6657723265619016, 1638.745987447298, 2.2009151015714834], "isController": false}, {"data": ["Visa och granska", 60, 0, 0.0, 3942.5499999999997, 1153, 7514, 6437.4, 6772.349999999999, 7514.0, 6.594856012310398, 2915.0319781820176, 16.09374570647395], "isController": true}, {"data": ["/api/kartlager/tree", 60, 0, 0.0, 3003.966666666667, 851, 5345, 4590.7, 5126.099999999999, 5345.0, 9.675858732462506, 37.45615626511853, 1.8709179970972425], "isController": false}, {"data": ["/api/matningstyper/id_placeholder/gransvarden", 346, 0, 0.0, 245.34682080924853, 7, 1993, 515.0000000000001, 675.0999999999997, 1304.9499999999996, 32.36972588642529, 11.357872286930489, 8.436681401440733], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Malaren", 60, 0, 0.0, 715.2166666666667, 299, 2074, 1044.5, 1175.1999999999998, 2074.0, 7.288629737609329, 1474.3460163083091, 2.092633928571429], "isController": false}, {"data": ["/api/matobjekt/matningstyper?", 60, 0, 0.0, 931.016666666667, 135, 2324, 1793.6999999999998, 2272.6999999999985, 2324.0, 11.181513231457325, 21.547889780562805, 5.796588182538204], "isController": false}, {"data": ["Öppna granskningsfliken", 60, 0, 0.0, 6054.716666666668, 3508, 8248, 7324.4, 8003.449999999997, 8248.0, 6.533101045296167, 692.2110846649064, 9.415895681347997], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 60, 0, 0.0, 768.3833333333333, 140, 2008, 1292.6, 1497.2999999999995, 2008.0, 11.655011655011656, 1157.0330710955711, 5.05354020979021], "isController": false}, {"data": ["Godkänna mätvärden", 35, 2, 5.714285714285714, 3339.714285714286, 2049, 4350, 4326.6, 4346.8, 4350.0, 6.216696269982238, 48.051384880106575, 30.001457038188278], "isController": true}, {"data": ["/api/matningstyper/id_placeholder/matningDataSeries", 346, 0, 0.0, 304.4942196531793, 9, 1858, 627.6, 875.5999999999963, 1812.7399999999984, 30.568071384397914, 29.479780954589625, 8.773101367170245], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 10, 100.0, 0.7782101167315175], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 1285, 10, "500", 10, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["/api/matningar/godkann", 173, 10, "500", 10, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
