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

    var data = {"OkPercent": 99.54853273137698, "KoPercent": 0.45146726862302483};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.7024714828897338, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.76, 500, 1500, "/api/matningar/godkann"], "isController": false}, {"data": [0.8833333333333333, 500, 1500, "SMHI Stockholm"], "isController": false}, {"data": [0.008333333333333333, 500, 1500, "Granska mätvärden"], "isController": true}, {"data": [0.925, 500, 1500, "/api/anvandargrupp"], "isController": false}, {"data": [0.7125, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Saltsjon"], "isController": false}, {"data": [0.15833333333333333, 500, 1500, "Visa och granska"], "isController": true}, {"data": [0.4125, 500, 1500, "/api/kartlager/tree"], "isController": false}, {"data": [0.89, 500, 1500, "/api/matningstyper/id_placeholder/gransvarden"], "isController": false}, {"data": [0.65, 500, 1500, "Stockholms Hamnar - Vattenstand Niva - Malaren"], "isController": false}, {"data": [0.825, 500, 1500, "/api/matobjekt/matningstyper?"], "isController": false}, {"data": [0.0875, 500, 1500, "Öppna granskningsfliken"], "isController": true}, {"data": [0.8125, 500, 1500, "/api/matobjekt/mapinfo"], "isController": false}, {"data": [0.0, 500, 1500, "Godkänna mätvärden"], "isController": true}, {"data": [0.8945454545454545, 500, 1500, "/api/matningstyper/id_placeholder/matningDataSeries"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 2215, 10, 0.45146726862302483, 461.16252821670435, 8, 2790, 906.4000000000001, 1186.3999999999987, 1866.5600000000013, 93.73280859887436, 2980.092812076827, 29.50470085639626], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Throughput", "Received", "Sent"], "items": [{"data": ["/api/matningar/godkann", 275, 10, 3.6363636363636362, 505.4218181818179, 9, 2205, 896.2, 1119.2, 1527.440000000001, 14.014881255733359, 3.3887544911324023, 6.100902765390888], "isController": false}, {"data": ["SMHI Stockholm", 120, 0, 0.0, 381.49166666666656, 16, 2304, 782.3000000000003, 1013.2999999999992, 2258.2199999999984, 5.424954792043399, 120.26552610759494, 1.5575553797468353], "isController": false}, {"data": ["Granska mätvärden", 120, 0, 0.0, 5704.641666666667, 1260, 12454, 9871.8, 10343.699999999999, 12218.169999999991, 5.0643595695294366, 2953.7800531493986, 18.01758842846592], "isController": true}, {"data": ["/api/anvandargrupp", 120, 0, 0.0, 310.4166666666667, 9, 1013, 621.0, 696.6999999999999, 1011.1099999999999, 5.218071922424664, 4.606579119015524, 1.5440193286080792], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Saltsjon", 120, 0, 0.0, 592.7833333333332, 61, 1663, 1007.6, 1150.5, 1656.2799999999997, 5.509388916945962, 1177.7663875625547, 1.5817972085762821], "isController": false}, {"data": ["Visa och granska", 120, 0, 0.0, 3210.374999999999, 147, 8338, 6612.100000000001, 7334.05, 8290.749999999998, 5.226708480334509, 2306.4199956307984, 11.060860188270395], "isController": true}, {"data": ["/api/kartlager/tree", 120, 0, 0.0, 1251.508333333333, 186, 2790, 1935.4, 2358.2999999999993, 2776.3499999999995, 5.157519233248808, 19.96524046933425, 0.9972546954914686], "isController": false}, {"data": ["/api/matningstyper/id_placeholder/gransvarden", 550, 0, 0.0, 359.8963636363638, 8, 2102, 686.9000000000001, 880.3499999999998, 1441.5500000000009, 25.77078062037297, 9.02837567941149, 6.716143636959986], "isController": false}, {"data": ["Stockholms Hamnar - Vattenstand Niva - Malaren", 120, 0, 0.0, 662.2, 70, 1980, 1057.3, 1480.7999999999984, 1956.479999999999, 5.424219138453194, 1097.2125305112327, 1.5573441667043348], "isController": false}, {"data": ["/api/matobjekt/matningstyper?", 120, 0, 0.0, 462.9500000000001, 77, 1479, 961.9000000000004, 1174.8499999999997, 1448.5499999999988, 5.215577190542421, 8.568181447757302, 2.7050713338838666], "isController": false}, {"data": ["Öppna granskningsfliken", 120, 0, 0.0, 2494.2666666666687, 461, 4116, 3569.1000000000004, 3828.8, 4100.669999999999, 5.101823902044981, 724.3196156493134, 7.354299083797457], "isController": true}, {"data": ["/api/matobjekt/mapinfo", 120, 0, 0.0, 469.3916666666665, 96, 1066, 743.7, 936.1999999999987, 1065.58, 5.237430167597765, 710.0696670276493, 2.2709169867318435], "isController": false}, {"data": ["Godkänna mätvärden", 55, 2, 3.6363636363636362, 6125.7818181818175, 3826, 8575, 7799.2, 8204.199999999999, 8575.0, 2.7893295466071613, 21.947199655771378, 13.708227412770057], "isController": true}, {"data": ["/api/matningstyper/id_placeholder/matningDataSeries", 550, 0, 0.0, 343.3672727272726, 12, 1774, 680.5000000000002, 848.9499999999992, 1171.3100000000002, 25.402983695903192, 24.933623879959356, 7.290097022077502], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 10, 100.0, 0.45146726862302483], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 2215, 10, "500", 10, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["/api/matningar/godkann", 275, 10, "500", 10, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
