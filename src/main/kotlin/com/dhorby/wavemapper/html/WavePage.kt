package com.dhorby.wavemapper.html



class WavePage(waveData: String, mapsApiKey: String) {

    val testData = """
                   ['Lat', 'Long', 'Name', 'Marker'],[49.9167,-2.883,'CHANNEL LIGHTSHIP', 'medium'],[45.23,-5.0,'GASCOGNE', 'big'],[48.72,-12.43,'K1', 'small'],[51.0,-13.35,'K2', 'small'],[47.55,-8.47,'BRITTANY', 'big'],[51.15,1.78,'SANDETTIE', 'small'],[59.07,-11.42,'K5', 'big'],[53.29,-5.26,'M2', 'medium'],[51.13,-10.33,'M3', 'big'],[55.0,-10.0,'M4', 'big'],[51.41,-6.42,'M5', 'medium'],[53.04,-15.53,'M6', 'big'],[50.04,-6.04,'SEVEN STONES', 'big'],[49.0,-16.5,'PAP', 'small'],[50.0,-4.4,'E1', 'small']
    """.trimIndent()
    val waveHtml: String = """
        <html>
        <head>
            <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
             <script type="text/javascript">

                    google.charts.load("current", {
                    "packages":['map','geochart'],
                    // Note: you will need to get a mapsApiKey for your project.
                    // See: https://developers.google.com/chart/interactive/docs/basic_load_libs#load-settings
                    "mapsApiKey": "$mapsApiKey"
                });
                google.charts.setOnLoadCallback(drawMap);
                google.charts.load('current', {'packages':['corechart']});
                google.charts.setOnLoadCallback(drawChart);

                function drawChart() {
                
                var data = google.visualization.arrayToDataTable([${waveData}]);
                
                    //Graph styling and legend
                    var options = {
                        title: 'sumVar2 (%)',
                        curveType: 'function',
                        legend: { position: 'bottom' },
                        vAxis: { title: 'someVar2 %'},
                        hAxis: { title: 'someVar1'}
                    };
                
                
                    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
                
                    chart.draw(data, options);
                };
                function drawMap() {
                    var data = google.visualization.arrayToDataTable([${waveData}])

                    var waveurl = 'https://icons.iconarchive.com/icons/iconsmind/outline/'

                    var waveicon = 'Wave-icon.png'

                    var options = {
                        zoomLevel: 6,
                        showTooltip: true,
                        showInfoWindow: true,
                        useMapTypeControl: true,
                        icons: {
                            verybig: {
                                normal:   waveurl + '/72/' + waveicon,
                                selected: waveurl + '/72/' + waveicon
                            },
                            big: {
                                normal:   waveurl + '/64/' + waveicon,
                                selected: waveurl + '/64/' + waveicon
                            },
                            medium: {
                                normal:   waveurl + '/48/' + waveicon,
                                selected: waveurl + '/48/' + waveicon
                            },
                            small: {
                                normal:   waveurl + '/32/' + waveicon,
                                selected: waveurl + '/32/' + waveicon
                            },
                            verysmall: {
                                normal:   waveurl + '/24/' + waveicon,
                                selected: waveurl + '/24/' + waveicon
                            }
                        }
                    };

                    var map = new google.visualization.Map(document.getElementById('map_div'));
                    map.draw(data, options);
                    
                    

                }

 
            </script>
          
        </head>

        <body>
        <div id="map_div" style="width: 100%; height: 50%"></div>
        <div id="chart_div" style="width: 100%; height: 50%"></div>
        </body>
        </html>
    """.trimIndent()

}
